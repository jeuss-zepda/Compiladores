package interfaz.ventanitas;

import interfaz.Nueva;
import javax.swing.*;
import java.awt.*;

public class CerraduraKleeneDialog extends JDialog {
    private JComboBox<String> cmbAFN;
    private JLabel lblIndiceResultado;
    private Nueva ventanaPrincipal;

    public CerraduraKleeneDialog(Nueva parent) {
        super(parent, "Cerradura * (Kleene)", true);
        this.ventanaPrincipal = parent;
        setSize(400, 200);
        setLocationRelativeTo(parent);
        inicializarComponentes();
        generarNuevoIndice();
        actualizarComboBox();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelEntrada = new JPanel(new GridLayout(2, 2, 5, 5));

        panelEntrada.add(new JLabel("Índice resultado:"));
        lblIndiceResultado = new JLabel();
        panelEntrada.add(lblIndiceResultado);

        panelEntrada.add(new JLabel("AFN:"));
        cmbAFN = new JComboBox<>();
        panelEntrada.add(cmbAFN);

        JPanel panelBotones = new JPanel();
        JButton btnAplicar = new JButton("Aplicar Cerradura *");
        btnAplicar.addActionListener(e -> {
            String indice = lblIndiceResultado.getText();
            ventanaPrincipal.indicesExistentes.add(indice);
            JOptionPane.showMessageDialog(this, "Cerradura * aplicada con índice: " + indice);
            dispose();
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnAplicar);
        panelBotones.add(btnCancelar);

        panelPrincipal.add(panelEntrada, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        add(panelPrincipal);
    }

    private void actualizarComboBox() {
        if (cmbAFN != null) {
            cmbAFN.removeAllItems();
            for (String indice : ventanaPrincipal.indicesExistentes) {
                cmbAFN.addItem(indice);
            }
        }
    }

    private void generarNuevoIndice() {
        String nuevoIndice = "AFN_" + ventanaPrincipal.contadorGlobalAFN++;
        lblIndiceResultado.setText(nuevoIndice);
    }
}
