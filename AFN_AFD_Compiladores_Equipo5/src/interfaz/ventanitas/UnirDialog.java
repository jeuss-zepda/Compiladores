package interfaz.ventanitas;

import interfaz.Nueva;
import javax.swing.*;
import java.awt.*;

public class UnirDialog extends JDialog {
    private JComboBox<String> cmbAFN1;
    private JComboBox<String> cmbAFN2;
    private JLabel lblIndiceResultado;
    private Nueva ventanaPrincipal;

    public UnirDialog(Nueva parent) {
        super(parent, "Unir AFNs", true);
        this.ventanaPrincipal = parent;
        setSize(400, 200);
        setLocationRelativeTo(parent);
        inicializarComponentes();
        generarNuevoIndice();
        actualizarComboBox();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelEntrada = new JPanel(new GridLayout(3, 2, 5, 5));

        panelEntrada.add(new JLabel("Índice resultado:"));
        lblIndiceResultado = new JLabel();
        panelEntrada.add(lblIndiceResultado);

        panelEntrada.add(new JLabel("AFN 1:"));
        cmbAFN1 = new JComboBox<>();
        panelEntrada.add(cmbAFN1);

        panelEntrada.add(new JLabel("AFN 2:"));
        cmbAFN2 = new JComboBox<>();
        panelEntrada.add(cmbAFN2);

        JPanel panelBotones = new JPanel();
        JButton btnUnir = new JButton("Unir AFNs");
        btnUnir.addActionListener(e -> {
            String indice = lblIndiceResultado.getText();
            ventanaPrincipal.indicesExistentes.add(indice);
            JOptionPane.showMessageDialog(this, "AFNs unidos con índice: " + indice);
            dispose();
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnUnir);
        panelBotones.add(btnCancelar);

        panelPrincipal.add(panelEntrada, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        add(panelPrincipal);
    }

    private void actualizarComboBox() {
        if (cmbAFN1 != null && cmbAFN2 != null) {
            cmbAFN1.removeAllItems();
            cmbAFN2.removeAllItems();
            for (String indice : ventanaPrincipal.indicesExistentes) {
                cmbAFN1.addItem(indice);
                cmbAFN2.addItem(indice);
            }
        }
    }

    private void generarNuevoIndice() {
        String nuevoIndice = "AFN_" + ventanaPrincipal.contadorGlobalAFN++;
        lblIndiceResultado.setText(nuevoIndice);
    }
}
