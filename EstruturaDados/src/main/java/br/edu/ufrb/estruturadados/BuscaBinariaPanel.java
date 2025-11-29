package br.edu.ufrb.estruturadados;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class BuscaBinariaPanel extends JPanel {

    private DefaultListModel<Integer> modeloLista;
    private JTextField campoValor, campoBusca, campoTamanho;
    private JButton btnAdicionar, btnLimpar, btnAleatorio, btnBuscar;
    private VisualizacaoPanel painelDesenho;

    public BuscaBinariaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Tema.BACKGROUND);

        JPanel painelControles = ComponentesUI.criarPainelModerno();
        painelControles.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        modeloLista = new DefaultListModel<>();

        campoValor = ComponentesUI.criarCampoTextoModerno(5);
        campoTamanho = ComponentesUI.criarCampoTextoModerno(5);
        campoBusca = ComponentesUI.criarCampoTextoModerno(5);

        btnAdicionar = ComponentesUI.criarBotaoModerno("Adicionar", Tema.SUCCESS);
        btnLimpar = ComponentesUI.criarBotaoModerno("Limpar", Tema.ERROR);
        btnAleatorio = ComponentesUI.criarBotaoModerno("Gerar aleatório", Tema.SECONDARY);
        btnBuscar = ComponentesUI.criarBotaoModerno("Busca binária", Tema.PRIMARY);

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelControles.add(ComponentesUI.criarLabelEstilizado("Valor:", "subheading"), gbc);
        gbc.gridx = 1;
        painelControles.add(campoValor, gbc);
        gbc.gridx = 2;
        painelControles.add(btnAdicionar, gbc);
        gbc.gridx = 3;
        painelControles.add(btnLimpar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        painelControles.add(ComponentesUI.criarLabelEstilizado("Tamanho:", "subheading"), gbc);
        gbc.gridx = 1;
        painelControles.add(campoTamanho, gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        painelControles.add(btnAleatorio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        painelControles.add(ComponentesUI.criarLabelEstilizado("Buscar:", "subheading"), gbc);
        gbc.gridx = 1;
        painelControles.add(campoBusca, gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        painelControles.add(btnBuscar, gbc);
        add(painelControles, BorderLayout.NORTH);

        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBackground(Tema.BACKGROUND);

        painelDesenho = new VisualizacaoPanel();
        painelCentral.add(new JScrollPane(painelDesenho), BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);

        JTextArea textoDefinicao = ComponentesUI.criarAreaTextoEstilizada();
        textoDefinicao.setText(getDefinicao());
        JScrollPane scrollPane = new JScrollPane(textoDefinicao);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(Tema.FIELD_WIDTH_CODE, Tema.FIELD_HEIGHT_INFO));
        add(scrollPane, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(campoValor.getText());
                modeloLista.addElement(valor);
                campoValor.setText("");
                ordenarLista();
                painelDesenho.revalidate();
                painelDesenho.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um número válido.");
            }
        });

        btnAleatorio.addActionListener(e -> {
            modeloLista.clear();
            int tamanho = 10;
            try {
                if (!campoTamanho.getText().isEmpty()) {
                    tamanho = Integer.parseInt(campoTamanho.getText());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um tamanho válido.");
            }
            Random rand = new Random();
            for (int i = 0; i < tamanho; i++) {
                modeloLista.addElement(rand.nextInt(100));
            }
            ordenarLista();
            painelDesenho.revalidate();
            painelDesenho.repaint();
        });

        btnBuscar.addActionListener(e -> {
            try {
                int valorBusca = Integer.parseInt(campoBusca.getText());
                java.util.List<Integer> valores = java.util.Collections.list(modeloLista.elements());
                int[] array = valores.stream().mapToInt(Integer::intValue).toArray();
                int idx = buscaBinaria(array, valorBusca);
                if (modeloLista.isEmpty()) {
                    erro("A lista está vazia! Adicione ou gere valores para iniciar a busca.");
                } else if (idx >= 0) {
                    aviso("Valor encontrado no índice: " + idx);
                } else {
                    erro("Valor não encontrado.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um número válido para busca.");
            }
        });

        btnLimpar.addActionListener(e -> {
            modeloLista.clear();
            painelDesenho.revalidate();
            painelDesenho.repaint();
        });

    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private String getDefinicao() {
        return "Busca Binária:\n"
                + "Procura um valor em uma lista ordenada dividindo o intervalo de busca pela metade a cada passo.\n\n"
                + "Complexidade:\n"
                + "Melhor caso: O(1)\n"
                + "Caso médio/pior: O(log n)";
    }

    private void ordenarLista() {
        java.util.List<Integer> valores = java.util.Collections.list(modeloLista.elements());
        java.util.Collections.sort(valores);
        modeloLista.clear();
        for (int v : valores) {
            modeloLista.addElement(v);
        }
    }

    private int buscaBinaria(int[] arr, int valor) {
        int inicio = 0, fim = arr.length - 1;
        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            if (arr[meio] == valor) {
                return meio;
            } else if (arr[meio] < valor) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }
        return -1;
    }

    private class VisualizacaoPanel extends JPanel {

        private int destaque = -1;

        public VisualizacaoPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Tema.BORDER));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (modeloLista.isEmpty()) {
                g2d.setFont(Tema.FONT_SUBHEADING);
                g2d.setColor(Tema.TEXT_SECONDARY);
                g2d.drawString("A lista está vazia! Adicione ou gere valores para iniciar a busca.", getWidth() / 2 - 40, getHeight() / 2);
                return;
            }

            int w = 50, h = 50, gap = 5;
            int x = 20, y = 40;

            List<Integer> lista = Collections.list(modeloLista.elements());
            for (int i = 0; i < lista.size(); i++) {
                Color c = (i == destaque) ? Tema.SUCCESS : Tema.ARRAY_BOX;
                g2d.setColor(c);
                g2d.fillRoundRect(x, y, w, h, 8, 8);

                g2d.setColor(c.darker());
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(x, y, w, h, 8, 8);

                g2d.setColor(Color.WHITE);
                g2d.setFont(Tema.FONT_HEADING);
                String s = String.valueOf(lista.get(i));
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(s, x + (w - fm.stringWidth(s)) / 2, y + 32);

                g2d.setColor(Tema.TEXT_SECONDARY);
                g2d.setFont(Tema.FONT_CODE);
                g2d.drawString(String.valueOf(i), x + w / 2 - 3, y + h + 15);

                x += w + gap;
            }
            setPreferredSize(new Dimension(x + 50, 150));
            revalidate();
        }
    }
}
