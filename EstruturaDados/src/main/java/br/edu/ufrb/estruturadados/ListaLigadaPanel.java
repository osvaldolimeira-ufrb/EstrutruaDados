package br.edu.ufrb.estruturadados;

import java.awt.*;
import java.util.LinkedList;
import javax.swing.*;

public class ListaLigadaPanel extends JPanel {

    private LinkedList<String> lista;
    private JTextField campoValor, campoIndice;
    private JButton botaoAddInicio, botaoAddFim, botaoRemover;
    private VisualizacaoPanel painelDesenho;

    public ListaLigadaPanel() {
        lista = new LinkedList<>();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Tema.BACKGROUND);

        JPanel painelControles = ComponentesUI.criarPainelModerno();
        painelControles.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoValor = ComponentesUI.criarCampoTextoModerno(8);
        campoIndice = ComponentesUI.criarCampoTextoModerno(4);
        botaoAddInicio = ComponentesUI.criarBotaoModerno("Adicionar no início", Tema.PRIMARY);
        botaoAddFim = ComponentesUI.criarBotaoModerno("Adicionar no fim", Tema.SECONDARY);
        botaoRemover = ComponentesUI.criarBotaoModerno("Remover (por índice)", Tema.ERROR);

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelControles.add(ComponentesUI.criarLabelEstilizado("Valor:", "subheading"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        painelControles.add(campoValor, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        painelControles.add(botaoAddInicio, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        painelControles.add(botaoAddFim, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        painelControles.add(ComponentesUI.criarLabelEstilizado("Índice:", "subheading"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        painelControles.add(campoIndice, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        painelControles.add(botaoRemover, gbc);

        add(painelControles, BorderLayout.NORTH);

        painelDesenho = new VisualizacaoPanel();
        add(painelDesenho, BorderLayout.CENTER);

        JTextArea textoDefinicao = ComponentesUI.criarAreaTextoEstilizada();
        textoDefinicao.setText(getDefinicao());
        JScrollPane scrollPane = new JScrollPane(textoDefinicao);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(Tema.FIELD_WIDTH_CODE, Tema.FIELD_HEIGHT_INFO));
        add(scrollPane, BorderLayout.SOUTH);

        botaoAddFim.addActionListener(e -> adicionarNoFim());
        botaoAddInicio.addActionListener(e -> adicionarNoInicio());
        botaoRemover.addActionListener(e -> removerElemento());
    }

    private String getDefinicao() {
        return "LISTA LIGADA (ou Encadeada):\n\n"
                + "É uma coleção linear de elementos chamados 'nós'. Cada nó armazena um valor e uma referência (ou 'ponteiro') para o próximo nó da sequência. "
                + "Diferente do array, os nós não precisam estar em posições contíguas de memória.\n"
                + "Prós: Inserção e remoção de elementos são muito eficientes (O(1)) se a posição for conhecida (início/fim).\n"
                + "Contras: O acesso a um elemento no meio da lista é lento (O(n)), pois é preciso percorrer a lista desde o início.";
    }

    private void adicionarNoFim() {
        String valor = campoValor.getText().trim();
        if (!valor.isEmpty()) {
            lista.addLast(valor);
            campoValor.setText("");
            painelDesenho.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarNoInicio() {
        String valor = campoValor.getText().trim();
        if (!valor.isEmpty()) {
            lista.addFirst(valor);
            campoValor.setText("");
            painelDesenho.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerElemento() {
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "A lista está vazia!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int indice = Integer.parseInt(campoIndice.getText().trim());
            if (indice >= 0 && indice < lista.size()) {
                lista.remove(indice);
                campoIndice.setText("");
                painelDesenho.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Índice fora dos limites da lista.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um índice numérico válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Classe interna responsável por todo o desenho da lista
    private class VisualizacaoPanel extends JPanel {

        private static final int NODE_WIDTH = 80;
        private static final int NODE_HEIGHT = 45;
        private static final int GAP = 40;

        public VisualizacaoPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Tema.BORDER));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (lista.isEmpty()) {
                g2d.setFont(Tema.FONT_SUBHEADING);
                g2d.setColor(Tema.TEXT_SECONDARY);
                String msg = "Lista vazia.";
                int x = (getWidth() - g2d.getFontMetrics().stringWidth(msg)) / 2;
                g2d.drawString(msg, x, getHeight() / 2);
                return;
            }

            int startX = 30;
            int y = getHeight() / 2 - NODE_HEIGHT / 2;

            // Desenha HEAD
            g2d.setFont(Tema.FONT_BUTTON);
            g2d.setColor(Tema.PRIMARY_DARK);
            g2d.drawString("HEAD", startX, y - 10);

            for (int i = 0; i < lista.size(); i++) {
                int x = startX + i * (NODE_WIDTH + GAP);

                // Nó (Parte de Dados)
                g2d.setColor(Tema.NODE_DATA); // Verde do tema
                g2d.fillRoundRect(x, y, NODE_WIDTH * 2 / 3, NODE_HEIGHT, 8, 8);

                // Nó (Parte do Ponteiro)
                g2d.setColor(Tema.NODE_POINTER); // Azul do tema
                g2d.fillRoundRect(x + NODE_WIDTH * 2 / 3 - 5, y, NODE_WIDTH / 3 + 5, NODE_HEIGHT, 8, 8);

                // Divisória
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(x + NODE_WIDTH * 2 / 3, y, x + NODE_WIDTH * 2 / 3, y + NODE_HEIGHT);

                // Borda Geral
                g2d.setColor(Tema.PRIMARY_DARK);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(x, y, NODE_WIDTH, NODE_HEIGHT, 8, 8);

                // Valor
                g2d.setColor(Color.WHITE);
                g2d.setFont(Tema.FONT_BUTTON);
                String val = lista.get(i);
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(val, x + (NODE_WIDTH * 2 / 3 - fm.stringWidth(val)) / 2, y + 28);

                // Ponto
                g2d.fillOval(x + NODE_WIDTH - 15, y + NODE_HEIGHT / 2 - 3, 6, 6);

                // Seta
                if (i < lista.size() - 1) {
                    g2d.setColor(Tema.NODE_POINTER);
                    g2d.setStroke(new BasicStroke(2));
                    int x1 = x + NODE_WIDTH;
                    int x2 = x + NODE_WIDTH + GAP;
                    int my = y + NODE_HEIGHT / 2;
                    g2d.drawLine(x1, my, x2, my);
                    g2d.fillPolygon(new int[]{x2, x2 - 8, x2 - 8}, new int[]{my, my - 5, my + 5}, 3);
                } else {
                    g2d.setColor(Tema.TEXT_SECONDARY);
                    g2d.setFont(Tema.FONT_CODE);
                    g2d.drawString("NULL", x + NODE_WIDTH + 5, y + NODE_HEIGHT / 2 + 5);
                }
            }
        }
    }
}
