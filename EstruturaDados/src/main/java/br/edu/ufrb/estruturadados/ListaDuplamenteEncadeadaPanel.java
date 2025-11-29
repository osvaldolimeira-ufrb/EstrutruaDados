package br.edu.ufrb.estruturadados;

import java.awt.*;
import javax.swing.*;

public class ListaDuplamenteEncadeadaPanel extends JPanel {

    private static class Node {

        String data;
        Node prev;
        Node next;

        Node(String data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }

    private Node head; // Ponteiro para o início da lista
    private Node tail; // Ponteiro para o fim da lista
    private int size = 0;

    private JTextField campoValor, campoIndice;
    private VisualizacaoPanel painelDesenho;

    public ListaDuplamenteEncadeadaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Tema.BACKGROUND);

        JPanel painelControles = ComponentesUI.criarPainelModerno();
        painelControles.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        campoValor = ComponentesUI.criarCampoTextoModerno(8);
        campoIndice = ComponentesUI.criarCampoTextoModerno(4);
        JButton btnAddIni = ComponentesUI.criarBotaoModerno("Adicionar no início", Tema.PRIMARY);
        JButton btnAddFim = ComponentesUI.criarBotaoModerno("Adicionar no fim", Tema.SECONDARY);
        JButton btnRemover = ComponentesUI.criarBotaoModerno("Remover (por índice)", Tema.ERROR);

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelControles.add(ComponentesUI.criarLabelEstilizado("Valor:", "subheading"), gbc);
        gbc.gridx = 1;
        painelControles.add(campoValor, gbc);
        gbc.gridx = 2;
        painelControles.add(btnAddIni, gbc);
        gbc.gridx = 3;
        painelControles.add(btnAddFim, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelControles.add(ComponentesUI.criarLabelEstilizado("Índice:", "subheading"), gbc);
        gbc.gridx = 1;
        painelControles.add(campoIndice, gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painelControles.add(btnRemover, gbc);

        add(painelControles, BorderLayout.NORTH);

        painelDesenho = new VisualizacaoPanel();
        add(new JScrollPane(painelDesenho), BorderLayout.CENTER);

        JTextArea textoDefinicao = ComponentesUI.criarAreaTextoEstilizada();
        textoDefinicao.setText(getDefinicao());

        JScrollPane scrollPane = new JScrollPane(textoDefinicao);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(Tema.FIELD_WIDTH_CODE, Tema.FIELD_HEIGHT_INFO));
        add(scrollPane, BorderLayout.SOUTH);

        btnAddFim.addActionListener(e -> adicionarNoFim());
        btnAddIni.addActionListener(e -> adicionarNoInicio());
        btnRemover.addActionListener(e -> removerElemento());
    }

    private String getDefinicao() {
        return "LISTA DUPLAMENTE ENCADEADA:\n\n"
                + "Uma versão avançada da lista encadeada. Cada 'nó' armazena um valor, um ponteiro para o próximo nó, e um ponteiro para o nó anterior. "
                + "Isso permite que a lista seja percorrida em ambas as direções (para frente e para trás).\n"
                + "Prós: Remoção de um nó é mais eficiente (se você já tem a referência para ele) e a travessia reversa é trivial.\n"
                + "Contras: Usa mais memória devido ao ponteiro extra para o nó anterior.";
    }

    // --- Lógica de Manipulação da Lista ---
    private void adicionarNoInicio() {
        String valor = campoValor.getText().trim();
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Insira um valor.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Node novoNo = new Node(valor);
        if (head == null) { // Lista vazia
            head = novoNo;
            tail = novoNo;
        } else {
            head.prev = novoNo;
            novoNo.next = head;
            head = novoNo;
        }
        size++;
        campoValor.setText("");
        painelDesenho.repaint();
    }

    private void adicionarNoFim() {
        String valor = campoValor.getText().trim();
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Insira um valor.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Node novoNo = new Node(valor);
        if (head == null) { // Lista vazia
            head = novoNo;
            tail = novoNo;
        } else {
            tail.next = novoNo;
            novoNo.prev = tail;
            tail = novoNo;
        }
        size++;
        campoValor.setText("");
        painelDesenho.repaint();
    }

    private void removerElemento() {
        if (head == null) {
            JOptionPane.showMessageDialog(this, "A lista está vazia!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int indice = Integer.parseInt(campoIndice.getText().trim());
            if (indice < 0 || indice >= size) {
                JOptionPane.showMessageDialog(this, "Índice inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Node noParaRemover;
            if (indice == 0) { // Remover o primeiro
                noParaRemover = head;
                head = head.next;
                if (head != null) {
                    head.prev = null;
                } else {
                    tail = null; // A lista ficou vazia
                }
            } else if (indice == size - 1) { // Remover o último
                noParaRemover = tail;
                tail = tail.prev;
                tail.next = null;
            } else { // Remover do meio
                Node atual = head;
                for (int i = 0; i < indice; i++) {
                    atual = atual.next;
                }
                noParaRemover = atual;
                atual.prev.next = atual.next;
                atual.next.prev = atual.prev;
            }
            size--;
            campoIndice.setText("");
            painelDesenho.repaint();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Insira um índice numérico válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Classe de Desenho ---
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

            if (head == null) {
                g2d.setFont(Tema.FONT_SUBHEADING);
                g2d.setColor(Tema.TEXT_SECONDARY);
                g2d.drawString("Lista Vazia.", getWidth() / 2 - 40, getHeight() / 2);
                return;
            }

            int w = 80, h = 40, gap = 50, x = 20, y = 80;
            Node atual = head;

            while (atual != null) {
                // Nó Central
                g2d.setColor(Tema.NODE_DATA);
                g2d.fillRoundRect(x + 20, y, w - 40, h, 5, 5);
                g2d.setColor(Color.WHITE);
                g2d.drawString(atual.data, x + 35, y + 25);

                // Ponteiros (Caixas)
                g2d.setColor(Tema.NODE_POINTER);
                g2d.fillRoundRect(x, y, 20, h, 5, 5); // Prev
                g2d.fillRoundRect(x + w - 20, y, 20, h, 5, 5); // Next

                // Setas
                if (atual.next != null) {
                    g2d.setColor(Tema.PRIMARY_DARK);
                    g2d.drawLine(x + w, y + 10, x + w + gap, y + 10); // Ida
                    g2d.fillPolygon(new int[]{x + w + gap, x + w + gap - 5, x + w + gap - 5}, new int[]{y + 10, y + 7, y + 13}, 3);
                }
                if (atual.prev != null) {
                    g2d.setColor(Tema.ERROR);
                    g2d.drawLine(x, y + 30, x - gap, y + 30); // Volta
                    g2d.fillPolygon(new int[]{x - gap, x - gap + 5, x - gap + 5}, new int[]{y + 30, y + 27, y + 33}, 3);
                }

                if (atual == head) {
                    g2d.drawString("HEAD", x + 25, y - 10);
                }
                if (atual == tail) {
                    g2d.drawString("TAIL", x + 25, y + h + 20);
                }

                x += w + gap;
                atual = atual.next;
            }
            setPreferredSize(new Dimension(x, 200));
            revalidate();
        }
    }
}
