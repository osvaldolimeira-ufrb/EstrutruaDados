package br.edu.ufrb.estruturadados;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ArrayPanel extends JPanel {

    private List<String> array;
    private JTextField campoDeEntrada;
    private JButton botaoAdicionar, botaoRemover;
    private VisualizacaoPanel painelDesenho;

    public ArrayPanel() {
        array = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Tema.BACKGROUND);

        JPanel painelControles = ComponentesUI.criarPainelModerno();
        painelControles.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        painelControles.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel label = ComponentesUI.criarLabelEstilizado("Valor:", "subheading");
        painelControles.add(label);

        campoDeEntrada = ComponentesUI.criarCampoTextoModerno(10);
        botaoAdicionar = ComponentesUI.criarBotaoModerno("Adicionar", Tema.SUCCESS);
        botaoRemover = ComponentesUI.criarBotaoModerno("Remover (por índice)", Tema.ERROR);

        painelControles.add(campoDeEntrada);
        painelControles.add(botaoAdicionar);
        painelControles.add(botaoRemover);
        add(painelControles, BorderLayout.NORTH);

        painelDesenho = new VisualizacaoPanel();
        add(new JScrollPane(painelDesenho), BorderLayout.CENTER);

        JTextArea textoDefinicao = ComponentesUI.criarAreaTextoEstilizada();
        textoDefinicao.setText(getDefinicao());

        JScrollPane scrollPane = new JScrollPane(textoDefinicao);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(Tema.FIELD_WIDTH_CODE, Tema.FIELD_HEIGHT_INFO));
        add(scrollPane, BorderLayout.SOUTH);

        botaoAdicionar.addActionListener(e -> adicionarElemento());
        botaoRemover.addActionListener(e -> removerElemento());
    }

    private String getDefinicao() {
        return "ARRAY (ou Lista Dinâmica):\n\n"
                + "É uma coleção de itens armazenados em locais de memória contíguos. "
                + "Os elementos podem ser acessados diretamente usando um índice numérico.\n"
                + "Prós: Acesso rápido aos elementos (O(1)).\n"
                + "Contras: A inserção ou remoção de elementos no meio da lista pode ser lenta (O(n)), pois exige o deslocamento de outros elementos.";
    }

    private void adicionarElemento() {
        String valor = campoDeEntrada.getText().trim();
        if (!valor.isEmpty()) {
            array.add(valor);
            campoDeEntrada.setText("");
            painelDesenho.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerElemento() {
        String indiceStr = campoDeEntrada.getText().trim();
        try {
            int indice = Integer.parseInt(indiceStr);
            if (indice >= 0 && indice < array.size()) {
                array.remove(indice);
                campoDeEntrada.setText("");
                painelDesenho.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Índice inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um índice numérico válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Classe para gerenciar o desenho
    private class VisualizacaoPanel extends JPanel {

        public VisualizacaoPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Tema.BORDER));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (array.isEmpty()) {
                g2d.setColor(Tema.TEXT_SECONDARY);
                g2d.setFont(Tema.FONT_SUBHEADING);
                String msg = "Array vazio. Adicione elementos para visualizar.";
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(msg)) / 2;
                int y = getHeight() / 2;
                g2d.drawString(msg, x, y);
                return;
            }

            int boxWidth = 70;
            int boxHeight = 50;
            int startX = 30;
            int y = 60;
            int espacamento = 15;

            // Centraliza o array horizontalmente
            int totalWidth = array.size() * boxWidth + (array.size() - 1) * espacamento;
            if (totalWidth < getWidth() - 60) {
                startX = (getWidth() - totalWidth) / 2;
            }

            int x = startX;

            for (int i = 0; i < array.size(); i++) {
                x = startX + i * (boxWidth + espacamento);

                // Gradiente para as caixas
                GradientPaint gradient = new GradientPaint(
                        x, y, Tema.ARRAY_BOX,
                        x, y + boxHeight, Tema.ARRAY_BOX.darker(),
                        false
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(x, y, boxWidth, boxHeight, 10, 10);

                // Sombra
                g2d.setColor(Tema.comTransparencia(Color.BLACK, 25));
                g2d.fillRoundRect(x + 2, y + 2, boxWidth, boxHeight, 10, 10);

                // Borda destacada
                g2d.setColor(Tema.ARRAY_BORDER);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(x, y, boxWidth, boxHeight, 10, 10);

                // Texto com sombra para legibilidade
                String valor = array.get(i);
                g2d.setFont(Tema.FONT_BUTTON);
                FontMetrics fm = g2d.getFontMetrics();
                int stringWidth = fm.stringWidth(valor);

                // Sombra do texto
                g2d.setColor(Tema.comTransparencia(Color.BLACK, 150));
                g2d.drawString(valor,
                        x + (boxWidth - stringWidth) / 2 + 1,
                        y + boxHeight / 2 + fm.getAscent() / 2 + 1);

                // Texto principal branco
                g2d.setColor(Color.WHITE);
                g2d.drawString(valor,
                        x + (boxWidth - stringWidth) / 2,
                        y + boxHeight / 2 + fm.getAscent() / 2);

                String indiceStr = String.valueOf(i);
                g2d.setColor(Tema.TEXT_SECONDARY);
                g2d.setFont(Tema.FONT_BODY);
                int indexStringWidth = g2d.getFontMetrics().stringWidth(indiceStr);
                g2d.drawString(indiceStr,
                        x + (boxWidth - indexStringWidth) / 2,
                        y + boxHeight + 25);
            }

            setPreferredSize(new Dimension(x + espacamento + 100, 200));
            revalidate();
        }
    }
}
