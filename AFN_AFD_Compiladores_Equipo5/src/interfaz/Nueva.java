
package interfaz;

import afn_afd_compiladores_equipo5.*;
import interfaz.ventanitas.*;
import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

public class Nueva extends javax.swing.JFrame {
    // Variables globales para gestión de AFNs

    // Variables de los componentes de menú
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JPanel jPanel1;

    public int contadorGlobalAFN = 1;
    public java.util.List<String> indicesExistentes = new java.util.ArrayList<>();


    public Nueva() {
        initComponents();
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Constructor de AFN - Equipo 5");

        // Menú Operaciones AFN
        jMenu1.setText("Operaciones AFN");
        //Basico
        jMenuItem1.setText("Básico");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BasicoDialog dialog = new BasicoDialog(Nueva.this); // Pasar Nueva.this
                dialog.setVisible(true);
            }
        });
        jMenu1.add(jMenuItem1);
        //Unir
        jMenuItem2.setText("Unir");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UnirDialog dialog = new UnirDialog(Nueva.this); // Pasar Nueva.this
                dialog.setVisible(true);
            }
        });

        jMenu1.add(jMenuItem2);
        //Concatenar
        jMenuItem3.setText("Concatenar"); // FALTABA ESTA LÍNEA
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConcatenarDialog dialog = new ConcatenarDialog(Nueva.this); // Pasar Nueva.this
                dialog.setVisible(true);
            }
        });
        jMenu1.add(jMenuItem3);
        // Cerradura *
        jMenuItem4.setText("Cerradura *");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CerraduraKleeneDialog dialog = new CerraduraKleeneDialog(Nueva.this);
                dialog.setVisible(true);
            }
        });
        jMenu1.add(jMenuItem4);
        // Cerradura +
        jMenuItem5.setText("Cerradura +");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CerraduraPositivaDialog dialog = new CerraduraPositivaDialog(Nueva.this);
                dialog.setVisible(true);
            }
        });
        jMenu1.add(jMenuItem5);
        // Opcional
        jMenuItem6.setText("Opcional");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpcionalDialog dialog = new OpcionalDialog(Nueva.this);
                dialog.setVisible(true);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem7.setText("Expresión Regular -> AFN");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarEjemplo("Expresión Regular -> AFN");
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuBar1.add(jMenu1);

        // Menú Analizador Léxico
        jMenu2.setText("Analizador Léxico");

        jMenuItem8.setText("Unión para Analizador Léxico");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarEjemplo("Unión para Analizador Léxico");
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuItem9.setText("Convertir AFN a AFD");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarEjemplo("Convertir AFN a AFD");
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuItem10.setText("Analizar una cadena");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarEjemplo("Analizar una cadena");
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuItem11.setText("Probar analizador Léxico");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarEjemplo("Probar analizador Léxico");
            }
        });
        jMenu2.add(jMenuItem11);

        jMenuBar1.add(jMenu2);

        // Menú Análisis Sintáctico
        jMenu3.setText("Análisis Sintáctico");

        // Submenú Descenso Recursivo
        JMenu subMenuDescensoRecursivo = new JMenu("Descenso Recursivo");

        jMenuItem12 = new JMenuItem("Calculadora");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarEjemplo("Descenso Recursivo - Calculadora");
            }
        });
        subMenuDescensoRecursivo.add(jMenuItem12);

        jMenu3.add(subMenuDescensoRecursivo);

        jMenuItem13 = new JMenuItem("Descenso Rec Gramatica de Gramaticas");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarEjemplo("Descenso Rec Gramatica de Gramaticas");
            }
        });
        jMenu3.add(jMenuItem13);

        jMenuItem14 = new JMenuItem("Análisis LL(1)");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarEjemplo("Análisis LL(1)");
            }
        });
        jMenu3.add(jMenuItem14);

        jMenuItem15 = new JMenuItem("Análisis SLR");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarEjemplo("Análisis SLR");
            }
        });
        jMenu3.add(jMenuItem15);

        jMenuItem16 = new JMenuItem("Análisis LR Canónico");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarEjemplo("Análisis LR Canónico");
            }
        });
        jMenu3.add(jMenuItem16);

        JMenuItem jMenuItem17 = new JMenuItem("Ejemplo matrices");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarEjemplo("Ejemplo matrices");
            }
        });
        jMenu3.add(jMenuItem17);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        // Panel principal
        JLabel lblTitulo = new JLabel("Sistema Constructor de AFN/AFD - Equipo 5", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel lblInstrucciones = new JLabel("<html><div style='text-align: center;'>"
                + "Seleccione una operación del menú superior<br>"
                + "para comenzar a trabajar con AFN y AFD"
                + "</div></html>", JLabel.CENTER);

        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(lblTitulo, BorderLayout.NORTH);
        jPanel1.add(lblInstrucciones, BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 661, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }

    // Método para mostrar ventanas de ejemplo
    private void mostrarEjemplo(String operacion) {
        JDialog dialog = new JDialog(this, operacion, true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("<html><div style='text-align: center;'>"
                + "Ejemplo: " + operacion + "<br><br>"
                + "Aquí iría la implementación de: <b>" + operacion + "</b>"
                + "</div></html>", JLabel.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialog.dispose());

        panel.add(label, BorderLayout.CENTER);
        panel.add(btnCerrar, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            // Usar el look and feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Si no funciona, usar el look and feel por defecto
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Nueva().setVisible(true);
            }
        });
    }

}