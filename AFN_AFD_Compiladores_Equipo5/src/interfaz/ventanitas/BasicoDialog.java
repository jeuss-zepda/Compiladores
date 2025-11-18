package interfaz.ventanitas;

import interfaz.Nueva;
import javax.swing.*;
import java.awt.*;

public class BasicoDialog extends JDialog {
    private JTextField txtSimboloInf;
    private JTextField txtSimboloSup;
    private JLabel lblIndice;
    private Nueva ventanaPrincipal;

    public BasicoDialog(Nueva parent) {
        super(parent, "Crear AFN Básico", true);
        this.ventanaPrincipal = parent;
        setSize(400, 200);
        setLocationRelativeTo(parent);
        inicializarComponentes();
        generarNuevoIndice();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelEntrada = new JPanel(new GridLayout(3, 2, 5, 5));

        panelEntrada.add(new JLabel("Índice generado:"));
        lblIndice = new JLabel();
        panelEntrada.add(lblIndice);

        panelEntrada.add(new JLabel("Símbolo Inf:"));
        txtSimboloInf = new JTextField();
        txtSimboloInf.setPreferredSize(new Dimension(50, 25));
        panelEntrada.add(txtSimboloInf);

        panelEntrada.add(new JLabel("Símbolo Sup (opcional):"));
        txtSimboloSup = new JTextField();
        txtSimboloSup.setPreferredSize(new Dimension(50, 25));
        panelEntrada.add(txtSimboloSup);

        JPanel panelBotones = new JPanel();
        JButton btnCrear = new JButton("Crear AFN");
        btnCrear.addActionListener(e -> {
            String indice = lblIndice.getText();
            ventanaPrincipal.indicesExistentes.add(indice);
            JOptionPane.showMessageDialog(this, "AFN Básico creado con índice: " + indice);
            dispose();
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnCrear);
        panelBotones.add(btnCancelar);

        panelPrincipal.add(panelEntrada, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        add(panelPrincipal);
    }

    private void generarNuevoIndice() {
        String nuevoIndice = "AFN_" + ventanaPrincipal.contadorGlobalAFN++;
        lblIndice.setText(nuevoIndice);
    }
}
