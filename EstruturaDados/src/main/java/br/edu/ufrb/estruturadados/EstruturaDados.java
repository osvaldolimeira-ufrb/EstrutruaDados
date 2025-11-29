package br.edu.ufrb.estruturadados;

public class EstruturaDados {

    public static void main(String[] args) {
        Tema.aplicarTemaSistema();

        javax.swing.SwingUtilities.invokeLater(() -> {
            VisualizadorFrame frame = new VisualizadorFrame();
            frame.setVisible(true);
        });
    }
}
