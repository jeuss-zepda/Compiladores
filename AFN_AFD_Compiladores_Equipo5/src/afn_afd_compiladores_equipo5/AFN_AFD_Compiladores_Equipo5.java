package afn_afd_compiladores_equipo5;

import interfaz.*;

import javax.swing.UIManager;

public class AFN_AFD_Compiladores_Equipo5 {
    public static void main(String[] args) {
        // Configurar el look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Crear y mostrar la GUI
        java.awt.EventQueue.invokeLater(() -> {
            new AFNBuilderGUI().setVisible(true);
        });
    }
}
