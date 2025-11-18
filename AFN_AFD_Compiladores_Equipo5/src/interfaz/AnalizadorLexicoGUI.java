package interfaz;


import javax.swing.*;
import java.awt.*;

import java.io.*;

public class AnalizadorLexicoGUI extends JFrame {

    private int[][] tablaAFD;
    private JTextArea txtEntrada;
    private JLabel lblResultado;
    private int[] tokensAFD;

    public AnalizadorLexicoGUI() {
        super("Analizador Léxico");

        JButton btnCargar = new JButton("Cargar Tabla AFD");
        JButton btnAnalizar = new JButton("Analizar Texto");
        txtEntrada = new JTextArea(10, 40);
        lblResultado = new JLabel("Estado: Esperando...");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCargar);
        panelBotones.add(btnAnalizar);

        setLayout(new BorderLayout());
        add(panelBotones, BorderLayout.NORTH);
        add(new JScrollPane(txtEntrada), BorderLayout.CENTER);
        add(lblResultado, BorderLayout.SOUTH);

        btnCargar.addActionListener(e -> cargarAFD());
        btnAnalizar.addActionListener(e -> analizarTexto());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void cargarAFD() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                int numEstados = Integer.parseInt(br.readLine().trim());
                tablaAFD = new int[numEstados][257];
                 tokensAFD = new int[numEstados];

                 String[] tokensStr = br.readLine().split(";");
            for (int i = 0; i < numEstados; i++) {
                tokensAFD[i] = Integer.parseInt(tokensStr[i]);
            }
            
                for (int i = 0; i < numEstados; i++) {
                    String[] valores = br.readLine().split(";");
                    for (int j = 0; j < 257; j++) {
                        tablaAFD[i][j] = Integer.parseInt(valores[j]);
                    }
                }

                JOptionPane.showMessageDialog(this, "✅ AFD cargado correctamente (" + numEstados + " estados)");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error al cargar el archivo: " + ex.getMessage());
            }
        }
    }

    private void analizarTexto() {
    if (tablaAFD == null) {
        JOptionPane.showMessageDialog(this, "Primero carga un AFD.");
        return;
    }

    String texto = txtEntrada.getText();
    int estadoActual = 0;
    int tokenReconocido = -1;


    for (char c : texto.toCharArray()) {
        int ascii = (int) c;
        if (ascii < 0 || ascii > 256) {
            lblResultado.setText("❌ Símbolo fuera del rango ASCII.");
            return;
        }

        int siguienteEstado = tablaAFD[estadoActual][ascii];
        if (siguienteEstado == -1) {
            lblResultado.setText("❌ Cadena NO válida.");
            return;
        }
        estadoActual = siguienteEstado;
        
        // VERIFICAR TOKEN DEL ESTADO FINAL
    int tokenFinal = tokensAFD[estadoActual];
    if (tokenFinal != -1) {
        lblResultado.setText("✅ Cadena válida - Token reconocido: " + tokenFinal);
    } else {
        lblResultado.setText("⚠️ Cadena válida pero sin token asignado (estado no de aceptación)");
    }
    }

    // Aquí deberías verificar si el estado final es de aceptación
    // Necesitarías información adicional sobre qué estados son de aceptación
 if (tokenReconocido != -1) {
        lblResultado.setText("✅ Cadena válida - Token: " + tokenReconocido);
    } else {
        lblResultado.setText("✅ Cadena válida (sin token específico)");
    }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AnalizadorLexicoGUI::new);
    }
}
