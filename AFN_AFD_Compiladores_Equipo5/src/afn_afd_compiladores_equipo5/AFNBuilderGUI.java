package afn_afd_compiladores_equipo5;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

/**
 * AFNBuilderGUI GUI para NetBeans que permite: - Crear AFN básicos (símbolo
 * inferior/superior) con índice manual. - Unir / concatenar AFNs existentes. -
 * Aplicar cerraduras: Kleene (*), positiva (+). - Generar AFD (construcción por
 * subconjuntos) y mostrar tabla de transiciones en JDialog.
 *
 * Requiere las clases del paquete: AFN, Estado, Transicion, SimbEspecial (tus
 * implementaciones).
 *
 * NOTA: Las operaciones usan directamente los métodos que incluiste en AFN.java
 * y modifican las instancias in-place (siguiendo tu API).
 */
public class AFNBuilderGUI extends JFrame {

    private JTextField txtIndice, txtSimboloInf, txtSimboloSup,txtToken;
    private DefaultListModel<String> modelLista;
    private JList<String> listAFNs;
    private JTextArea txtAreaExpresion;

    // Mapa índice -> AFN real
    private Map<String, AFN> afnMap = new LinkedHashMap<>();

    public AFNBuilderGUI() {
        super("AFN Builder - NetBeans");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 620);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(8, 8));
        add(main);

        // LEFT: creación y lista
        JPanel left = new JPanel(new BorderLayout(8, 8));
        left.setBorder(BorderFactory.createTitledBorder("Crear AFN básico"));

        JPanel form = new JPanel(new GridLayout(6, 2, 6, 6));
        form.add(new JLabel("Índice (manual):"));
        txtIndice = new JTextField();
        form.add(txtIndice);

        form.add(new JLabel("Símbolo inferior (char):"));
        txtSimboloInf = new JTextField();
        form.add(txtSimboloInf);

        form.add(new JLabel("Símbolo superior (char) (opcional):"));
        txtSimboloSup = new JTextField();
        form.add(txtSimboloSup);

        form.add(new JLabel("Token (número, opcional):"));
        txtToken = new JTextField();
        form.add(txtToken);

        JButton btnCrear = new JButton("Crear AFN");
        form.add(btnCrear);

  
        left.add(form, BorderLayout.NORTH);

        modelLista = new DefaultListModel<>();
        listAFNs = new JList<>(modelLista);
        listAFNs.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scList = new JScrollPane(listAFNs);
        scList.setBorder(BorderFactory.createTitledBorder("AFNs creados / combinados"));
        left.add(scList, BorderLayout.CENTER);

        main.add(left, BorderLayout.WEST);

        // CENTER: operaciones y área de descripción
        JPanel center = new JPanel(new BorderLayout(8, 8));

        JPanel ops = new JPanel(new GridLayout(3, 3, 6, 6));
        ops.setBorder(BorderFactory.createTitledBorder("Operaciones sobre AFN seleccionado(s)"));

        JButton btnConcat = new JButton("Concatenar");
        JButton btnUnir = new JButton("Unir (|)");
        JButton btnKleene = new JButton("Cerradura *");
        JButton btnPos = new JButton("Cerradura +");
        JButton btnGenerarAFD = new JButton("Generar AFD (Tabla)");
        JButton btnEliminar = new JButton("Eliminar Seleccionados");
        JButton btnRefresh = new JButton("Refrescar lista/expresión");
        JButton btnDuplicar = new JButton("Duplicar AFN (copia)");

        ops.add(btnConcat);
        ops.add(btnUnir);
        ops.add(btnKleene);
        ops.add(btnPos);
        ops.add(btnGenerarAFD);
        ops.add(btnEliminar);
        ops.add(btnRefresh);
        ops.add(btnDuplicar);

        center.add(ops, BorderLayout.NORTH);

        txtAreaExpresion = new JTextArea();
        txtAreaExpresion.setEditable(false);
        txtAreaExpresion.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scExpr = new JScrollPane(txtAreaExpresion);
        scExpr.setBorder(BorderFactory.createTitledBorder("Información del AFN seleccionado"));
        center.add(scExpr, BorderLayout.CENTER);

        main.add(center, BorderLayout.CENTER);

        // RIGHT: instrucciones
        JPanel right = new JPanel(new BorderLayout(8, 8));

        JTextArea ayuda = new JTextArea();
        ayuda.setEditable(false);
        ayuda.setLineWrap(true);
        ayuda.setWrapStyleWord(true);

        right.add(new JScrollPane(ayuda), BorderLayout.CENTER);

        main.add(right, BorderLayout.EAST);

        // ---- Listeners ----
        btnCrear.addActionListener(e -> crearAFNAction());

        listAFNs.addListSelectionListener(e -> mostrarInfoSeleccion());

        btnEliminar.addActionListener(e -> eliminarSeleccionados());
        btnRefresh.addActionListener(e -> listAFNs.repaint());

        btnConcat.addActionListener(e -> operarBinaria("concat"));
        btnUnir.addActionListener(e -> operarBinaria("union"));

        btnKleene.addActionListener(e -> operarUnaria("kleene"));
        btnPos.addActionListener(e -> operarUnaria("plus"));

        btnGenerarAFD.addActionListener(e -> generarAFDAction());

        btnDuplicar.addActionListener(e -> duplicarAFNAction());
        
        
        JButton btnAnalizador = new JButton("Abrir Analizador Léxico");
ops.add(btnAnalizador);

btnAnalizador.addActionListener(e -> {
    new AnalizadorLexicoGUI(); // <-- abre la ventana del analizador
});



    }

    // -------------------------
    // Acciones
private void crearAFNAction() {
    String idx = txtIndice.getText().trim();
    String sInf = txtSimboloInf.getText().trim();
    String sSup = txtSimboloSup.getText().trim();
    String tokenStr = txtToken.getText().trim();

    if (idx.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Escribe un índice para el AFN (ej. A1).", "Índice requerido", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (afnMap.containsKey(idx)) {
        JOptionPane.showMessageDialog(this, "Índice ya existe. Elige otro.", "Índice duplicado", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (sInf.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debes escribir al menos el símbolo inferior.", "Símbolo requerido", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (sInf.length() != 1 || (!sSup.isEmpty() && sSup.length() != 1)) {
        JOptionPane.showMessageDialog(this, "Los símbolos deben ser caracteres individuales (1 char) si se especifican.", "Formato inválido", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Procesar token
    int token = -1;
    if (!tokenStr.isEmpty()) {
        try {
            token = Integer.parseInt(tokenStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Token debe ser un número entero.", "Token inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

    char inf = sInf.charAt(0);
    AFN nuevo = new AFN();
    if (sSup.isEmpty()) {
        nuevo = nuevo.crearBasico(inf);
    } else {
        char sup = sSup.charAt(0);
        nuevo = nuevo.crearBasico(inf, sup);
    }

    // ASIGNAR TOKEN al estado de aceptación
    if (token != -1) {
        for (Estado estado : nuevo.EdosAcept) {
            estado.Token = token;
        }
    }

    afnMap.put(idx, nuevo);
    modelLista.addElement(idx);

    // Limpiar campos
    txtIndice.setText("");
    txtSimboloInf.setText("");
    txtSimboloSup.setText("");
    txtToken.setText("");

    JOptionPane.showMessageDialog(this, "AFN creado: " + idx + (token != -1 ? " con token: " + token : ""));
}


    private void mostrarInfoSeleccion() {
        String sel = listAFNs.getSelectedValue();
        if (sel == null) {
            txtAreaExpresion.setText("");
            return;
        }
        AFN a = afnMap.get(sel);
        if (a == null) {
            txtAreaExpresion.setText("AFN no encontrado.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Índice: ").append(sel).append("\n");
        sb.append("Estados totales: ").append(a.Estados.size()).append("\n");
        sb.append("Estado inicial: ").append(a.EdoInicial == null ? "null" : a.EdoInicial.toString()).append("\n");
        sb.append("Estados acept: ").append(a.EdosAcept.size()).append("\n");
        sb.append("Alfabeto: ").append(a.Alfabeto.toString()).append("\n\n");

        sb.append("Transiciones (por estado):\n");
        for (Estado e : a.Estados) {
            sb.append("  Estado ").append(e.toString());
            if (e.EdoAcept) {
                sb.append(" (Acept)");
            }
            sb.append("\n");
            for (Transicion t : e.Transiciones) {
                sb.append("    -> ").append(transicionToString(t)).append("\n");
            }
        }
        txtAreaExpresion.setText(sb.toString());
    }

    private void eliminarSeleccionados() {

        java.util.List<String> sel = listAFNs.getSelectedValuesList();

        if (sel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona AFN(s) para eliminar.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "Eliminar " + sel.size() + " AFN(s)?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        for (String s : sel) {
            afnMap.remove(s);
            modelLista.removeElement(s);
        }
        txtAreaExpresion.setText("");
    }

    // duplicar un AFN (crea nueva referencia copiando estructura superficialmente)
    private void duplicarAFNAction() {
        String sel = listAFNs.getSelectedValue();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Selecciona 1 AFN para duplicar.");
            return;
        }
        AFN original = afnMap.get(sel);
        if (original == null) {
            return;
        }
        String nuevoIdx = JOptionPane.showInputDialog(this, "Introduce índice para la copia del AFN:");
        if (nuevoIdx == null || nuevoIdx.trim().isEmpty()) {
            return;
        }
        if (afnMap.containsKey(nuevoIdx)) {
            JOptionPane.showMessageDialog(this, "Índice ya existente.");
            return;
        }

        AFN copia = new AFN();
        // Intentaremos "copiar" estados referenciando los mismos objetos (advertencia).
        copia.Estados.addAll(original.Estados);
        copia.EdoInicial = original.EdoInicial;
        copia.Alfabeto.addAll(original.Alfabeto);
        copia.EdosAcept.addAll(original.EdosAcept);
        afnMap.put(nuevoIdx, copia);
        modelLista.addElement(nuevoIdx);
        JOptionPane.showMessageDialog(this, "Copia creada (nota: copia superficial): " + nuevoIdx);
    }

    // Operaciones binarias: concatenar o unir -> requieren al menos 2 AFNs seleccionados
    private void operarBinaria(String tipo) {

        java.util.List<String> sel = listAFNs.getSelectedValuesList();
        if (sel.size() < 2) {
            JOptionPane.showMessageDialog(this, "Selecciona al menos 2 AFNs (primero será el izquierdo).");
            return;
        }
        String a = sel.get(0);
        String b = sel.get(1);

        String nuevoIdx = JOptionPane.showInputDialog(this, "Introduce índice para el AFN resultado:");
        if (nuevoIdx == null || nuevoIdx.trim().isEmpty()) {
            return;
        }
        if (afnMap.containsKey(nuevoIdx)) {
            JOptionPane.showMessageDialog(this, "Índice ya existe.");
            return;
        }

        AFN fa = afnMap.get(a);
        AFN fb = afnMap.get(b);
        if (fa == null || fb == null) {
            JOptionPane.showMessageDialog(this, "AFN(s) no encontrados.");
            return;
        }

        // Atención: tus métodos mutan 'fa' in-place. Para respetar la semántica de "nuevo índice",
        // hacemos una duplicación superficial de 'fa' para aplicar la operación sobre copia (si el usuario desea).
        // Pero sin clon profundo disponible, vamos a usar 'fa' directamente y advertir al usuario.
        int option = JOptionPane.showConfirmDialog(this,
                "¿Continuar? (Puedes duplicar antes si lo deseas.)",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        AFN result;
        if ("concat".equals(tipo)) {
            result = fa.concatenar(fb);
        } else { // union
            result = fa.unirAFN(fb);
        }

        afnMap.put(nuevoIdx, result);
        modelLista.addElement(nuevoIdx);
        JOptionPane.showMessageDialog(this, "AFN creado: " + nuevoIdx);
    }

    // Operaciones unarias: kleene, plus
    private void operarUnaria(String tipo) {
        java.util.List<String> sel = listAFNs.getSelectedValuesList();
        if (sel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona 1 AFN para la operación.");
            return;
        }
        String baseIdx = sel.get(0);
        AFN base = afnMap.get(baseIdx);
        if (base == null) {
            return;
        }

        String nuevoIdx = JOptionPane.showInputDialog(this, "Introduce índice para el AFN resultado:");
        if (nuevoIdx == null || nuevoIdx.trim().isEmpty()) {
            return;
        }
        if (afnMap.containsKey(nuevoIdx)) {
            JOptionPane.showMessageDialog(this, "Índice ya existe.");
            return;
        }

        int option = JOptionPane.showConfirmDialog(this,
                " ¿Continuar?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        AFN res;
        if ("kleene".equals(tipo)) {
            res = base.cerraduraKleene();
        } else { // plus
            res = base.cerraduraPos();
        }

        afnMap.put(nuevoIdx, res);
        modelLista.addElement(nuevoIdx);
        JOptionPane.showMessageDialog(this, "AFN creado: " + nuevoIdx);
    }

    // -------------------------
    // Generar AFD (construcción por subconjuntos)
    // -------------------------
    private void generarAFDAction() {
        String sel = listAFNs.getSelectedValue();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Selecciona 1 AFN para convertir a AFD.");
            return;
        }
        AFN afn = afnMap.get(sel);
        if (afn == null) {
            JOptionPane.showMessageDialog(this, "AFN no encontrado.");
            return;
        }

        //AFD afd = afn.ConvertirAFD();
        
        //System.out.println("Pasa de conversión");
        
        //afd.guardarAFD("Archivo",afd);
        
        //System.out.println("Pasa de guardar");

        // Ejecutamos la construcción por subconjuntos y mostramos la tabla
        AFDTable afdTable = buildAFDTable(afn);
        showAFDDialog(sel, afdTable);
        
    }

    // Representación de una fila de la tabla AFD: origen, simbolo (string), destino
    private static class AFDRow {

        String origen;
        String simbolo;
        String destino;

        AFDRow(String o, String s, String d) {
            origen = o;
            simbolo = s;
            destino = d;
        }
    }

    // Estructura que contiene filas y marcas de estados aceptores
    private static class AFDTable {

        List<AFDRow> rows = new ArrayList<>();
        Set<String> estados = new LinkedHashSet<>(); // D0, D1...
        Set<String> aceptadores = new HashSet<>();
        Map<String, Integer> tokensPorEstado = new HashMap<>();
    }

    /**
     * Construye la tabla del AFD a partir de un AFN usando tu CerraduraEpsilon,
     * IrA
     */
    private AFDTable buildAFDTable(AFN afn) {
        AFDTable table = new AFDTable();

        // 1) obtener conjunto de "símbolos" a considerar. No usamos solo AFN.Alfabeto porque allí puede
        // faltar la información de rangos; en su lugar inspeccionamos todas las transiciones.
        List<SymbolToken> tokens = collectSymbolTokens(afn);

        // 2) Subconjuntos: map Set<Estado> -> nombre D#
        Map<Set<Estado>, String> nameMap = new HashMap<>();
        List<Set<Estado>> worklist = new ArrayList<>();

        // estado inicial = cerradura epsilon del estado inicial del AFN
        Set<Estado> start = afn.CerraduraEpsilon(afn.EdoInicial);
        nameMap.put(start, "D0");
        table.estados.add("D0");
        
         int tokenStart = obtenerTokenDeConjunto(start, afn);
    table.tokensPorEstado.put("D0", tokenStart);
    
        if (containsAccept(start, afn)) {
            table.aceptadores.add("D0");
        }
        worklist.add(start);

        int dcount = 1;

        while (!worklist.isEmpty()) {
            Set<Estado> current = worklist.remove(0);
            String currentName = nameMap.get(current);

            // Para cada token (símbolo simple o rango)
            for (SymbolToken tok : tokens) {
                // calcula destino = union_{c in token.range} IrA(current, c)
                Set<Estado> destino = new HashSet<>();
                for (char c = tok.low; c <= tok.high; c++) {
                    Set<Estado> r = afn.IrA(current, c);
                    if (r != null) {
                        destino.addAll(r);
                    }
                }
                if (destino.isEmpty()) {
                    continue;
                }

                // asignar nombre D# si nuevo
                String destName = nameMap.get(destino);
                if (destName == null) {
                    destName = "D" + dcount++;
                    nameMap.put(destino, destName);
                    table.estados.add(destName);
                    
                     int tokenDest = obtenerTokenDeConjunto(destino, afn);
                table.tokensPorEstado.put(destName, tokenDest);
                
                    if (containsAccept(destino, afn)) {
                        table.aceptadores.add(destName);
                    }
                    worklist.add(destino);
                }
                // añadir fila para cada token como símbolo textual
                table.rows.add(new AFDRow(currentName, tok.toString(), destName));
            }
        }
        
        guardarAFDTablaArchivo(table, "ArchivoSalida");
        return table;
    }
    
public void guardarAFDTablaArchivo(AFDTable table, String nombreArchivo) {
    try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
        
        // Primera línea: número de estados
        pw.println(table.estados.size());
        StringBuilder tokensLine = new StringBuilder();
        for (String estado : table.estados) {
            // Aquí necesitas mapear el estado D# a su token correspondiente
            // Esto requiere mantener un mapeo de estados AFD a tokens
            int token = obtenerTokenParaEstado(estado, table); // Necesitas implementar esto
            tokensLine.append(token).append(";");
        }
        if (tokensLine.length() > 0) {
            tokensLine.setLength(tokensLine.length() - 1); // quitar último ;
        }
        pw.println(tokensLine.toString());
        
        
        // Obtener todos los símbolos únicos (para las columnas)
        Set<Character> simbolos = new TreeSet<>();
        for (AFDRow row : table.rows) {
            // Convertir el string del símbolo de vuelta a char
            String simboloStr = row.simbolo;
            if (simboloStr.length() == 3 && simboloStr.startsWith("'") && simboloStr.endsWith("'")) {
                // Formato 'x'
                char c = simboloStr.charAt(1);
                simbolos.add(c);
            } else if (simboloStr.length() > 3 && simboloStr.contains("-")) {
                // Formato 'x-y' - agregar todos los caracteres del rango
                String[] partes = simboloStr.replace("'", "").split("-");
                if (partes.length == 2) {
                    char inicio = partes[0].charAt(0);
                    char fin = partes[1].charAt(0);
                    for (char c = inicio; c <= fin; c++) {
                        simbolos.add(c);
                    }
                }
            }
        }
        
        // Para cada estado, crear una línea con 257 valores (ASCII 0-256)
        for (String estado : table.estados) {
            int estadoIndex = Integer.parseInt(estado.substring(1)); // Extraer número de "D0", "D1", etc.
            
            // Inicializar array con -1 (sin transición)
            int[] transiciones = new int[257];
            for (int i = 0; i < 257; i++) {
                transiciones[i] = -1;
            }
            
            // Llenar las transiciones que existen
            for (AFDRow row : table.rows) {
                if (row.origen.equals(estado)) {
                    String simboloStr = row.simbolo;
                    String destEstado = row.destino;
                    int destIndex = Integer.parseInt(destEstado.substring(1));
                    
                    if (simboloStr.length() == 3 && simboloStr.startsWith("'") && simboloStr.endsWith("'")) {
                        // Símbolo individual
                        char c = simboloStr.charAt(1);
                        transiciones[(int)c] = destIndex;
                    } else if (simboloStr.length() > 3 && simboloStr.contains("-")) {
                        // Rango de símbolos
                        String[] partes = simboloStr.replace("'", "").split("-");
                        if (partes.length == 2) {
                            char inicio = partes[0].charAt(0);
                            char fin = partes[1].charAt(0);
                            for (char c = inicio; c <= fin; c++) {
                                transiciones[(int)c] = destIndex;
                            }
                        }
                    }
                }
            }
            
            // Escribir la línea para este estado
            StringBuilder linea = new StringBuilder();
            for (int i = 0; i < 257; i++) {
                linea.append(transiciones[i]);
                if (i < 256) {
                    linea.append(";");
                }
            }
            pw.println(linea.toString());
        }
        
        System.out.println("✅ Archivo AFD guardado correctamente en: " + nombreArchivo);
        
    } catch (IOException ex) {
        System.err.println("Error al guardar archivo AFD: " + ex.getMessage());
    }
}


    // símbolo token, admite rangos low..high (inclusive)
    private static class SymbolToken {

        char low, high;

        SymbolToken(char l, char h) {
            low = l;
            high = h;
        }

        @Override
        public String toString() {
            if (low == high) {
                return "'" + low + "'";
            } else {
                return "'" + low + "-" + high + "'";
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(low, high);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof SymbolToken)) {
                return false;
            }
            SymbolToken s = (SymbolToken) o;
            return s.low == low && s.high == high;
        }
    }

    // Recolecta tokens de símbolos (incluye rangos) inspeccionando las transiciones del AFN
    private List<SymbolToken> collectSymbolTokens(AFN afn) {
        Set<SymbolToken> set = new LinkedHashSet<>();
        for (Estado e : afn.Estados) {
            for (Transicion t : e.Transiciones) {
                // ignorar epsilon
                if (t.SimboloInf == SimbEspecial.EPSILON) {
                    continue;
                }
                // si Transicion tiene rango (se construyó con c1,c2), intento leer t.SimboloSup si existe.
                // asumimos que si existe campo SimboloSup, está accesible. Si no existe, usamos SimboloInf como único.
                try {
                    // intento reflejar si hay SimboloSup (campo público)
                    char sup = t.SimboloSup; // si no existe, lanzará error en tiempo de compilación; pero en tu Transicion lo debe tener
                    char inf = t.SimboloInf;
                    set.add(new SymbolToken(inf, sup));
                } catch (Throwable ex) {
                    // Si no hay SimboloSup público, tomamos solo SimboloInf
                    set.add(new SymbolToken(t.SimboloInf, t.SimboloInf));
                }
            }
        }

        // Si el alfabeto del AFN contiene puntos individuales que no aparecieron en transiciones (poco probable),
        // los añadimos como tokens individuales.
        for (Character c : afn.Alfabeto) {
            SymbolToken tok = new SymbolToken(c, c);
            if (!set.contains(tok)) {
                set.add(tok);
            }
        }

        // convert to list and return
        return new ArrayList<>(set);
    }

    private boolean containsAccept(Set<Estado> subset, AFN afn) {
        for (Estado e : subset) {
            if (afn.EdosAcept.contains(e)) {
                return true;
            }
        }
        return false;
    }

    // Muestra JDialog con la tabla
    private void showAFDDialog(String afnIndex, AFDTable table) {
        JDialog dialog = new JDialog(this, "Tabla de transiciones - AFD de " + afnIndex, true);
        dialog.setSize(800, 520);
        dialog.setLocationRelativeTo(this);

        JPanel p = new JPanel(new BorderLayout(8,8));

        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setText("AFN: " + afnIndex + "\nEstados DFA (subconjuntos): " + table.estados.size() +
                "\nEstados aceptadores: " + table.aceptadores + "\n\n(Estado marcado como aceptador si contiene algún estado acept en el AFN)");
        p.add(new JScrollPane(info), BorderLayout.NORTH);

        String[] cols = new String[] {"Estado", "Símbolo", "Destino"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        JTable tabla = new JTable(tm);

        for (AFDRow r : table.rows) {
            String origen = r.origen + (table.aceptadores.contains(r.origen) ? " (A)" : "");
            String destino = r.destino + (table.aceptadores.contains(r.destino) ? " (A)" : "");
            tm.addRow(new Object[] { origen, r.simbolo, destino });
        }

        p.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCerrar = new JButton("Cerrar");
        bottom.add(btnCerrar);
        p.add(bottom, BorderLayout.SOUTH);

        btnCerrar.addActionListener(e -> dialog.dispose());

        dialog.add(p);
        dialog.setVisible(true);
    }

// Método auxiliar para obtener el índice de un símbolo en el alfabeto
    private int getIndiceSimbolo(Character simbolo, Set<Character> alfabeto) {
        int index = 0;
        for (Character c : alfabeto) {
            if (c.equals(simbolo)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    // Representación amigable de una transición (usa campos de Transicion)
    private String transicionToString(Transicion t) {
        if (t.SimboloInf == SimbEspecial.EPSILON) {
            return "ε -> " + (t.EdoDestino == null ? "null" : t.EdoDestino.toString());
        }
        try {
            char sup = t.SimboloSup;
            if (t.SimboloInf == sup) {
                return t.SimboloInf + " -> " + (t.EdoDestino == null ? "null" : t.EdoDestino.toString());
            } else {
                return t.SimboloInf + "-" + sup + " -> " + (t.EdoDestino == null ? "null" : t.EdoDestino.toString());
            }
        } catch (Throwable ex) {
            return t.SimboloInf + " -> " + (t.EdoDestino == null ? "null" : t.EdoDestino.toString());
        }
    }

    // -------------------------
    // Main
    // -------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AFNBuilderGUI g = new AFNBuilderGUI();
            g.setVisible(true);
        });
    }
    
    /**
 * Obtiene el token para un conjunto de estados del AFN.
 * Si hay múltiples tokens en el conjunto, devuelve el primero encontrado.
 * Si no hay tokens, devuelve -1.
 */
private int obtenerTokenDeConjunto(Set<Estado> conjuntoEstados, AFN afn) {
    for (Estado estado : conjuntoEstados) {
        // Si el estado está en los estados de aceptación del AFN y tiene token
        if (afn.EdosAcept.contains(estado) && estado.Token != -1) {
            return estado.Token;
        }
    }
    return -1; // No hay token asignado
}

private int obtenerTokenParaEstado(String estado, AFDTable table) {
    return table.tokensPorEstado.getOrDefault(estado, -1);
}
}

