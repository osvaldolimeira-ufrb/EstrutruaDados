package br.edu.ufrb.estruturadados;

public class EstruturaDados {

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("Não foi possível aplicar o Look and Feel Nimbus.");
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            VisualizadorFrame frame = new VisualizadorFrame();
            frame.setVisible(true);
        });
    }
}
