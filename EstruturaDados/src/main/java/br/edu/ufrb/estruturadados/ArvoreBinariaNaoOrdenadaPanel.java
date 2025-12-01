package br.edu.ufrb.estruturadados;

import java.awt.*;
import javax.swing.*;

public class ArvoreBinariaNaoOrdenadaPanel extends JPanel {

    private static class No {

        String valor;
        No esquerda;
        No direita;

        No(String valor) {
            this.valor = valor;
            this.esquerda = null;
            this.direita = null;
        }
    }

    // Classe auxiliar para passar múltiplas informações da busca recursiva de remoção
    private static class InfoUltimoNo {

        No no = null;
        No pai = null;
        int nivel = 0;
    }

    private No raiz;
    private JTextField campoDeEntrada;
    private JButton botaoAdicionar, botaoRemover;
    private VisualizacaoPanel painelDesenho;

    public ArvoreBinariaNaoOrdenadaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Tema.BACKGROUND);

        JPanel painelControles = ComponentesUI.criarPainelModerno();
        campoDeEntrada = ComponentesUI.criarCampoTextoModerno(8);
        botaoAdicionar = ComponentesUI.criarBotaoModerno("Adicionar", Tema.PRIMARY);
        botaoRemover = ComponentesUI.criarBotaoModerno("Remover (por valor)", Tema.ERROR);

        painelControles.add(ComponentesUI.criarLabelEstilizado("Valor:", "subheading"));
        painelControles.add(campoDeEntrada);
        painelControles.add(botaoAdicionar);
        painelControles.add(botaoRemover);
        add(painelControles, BorderLayout.NORTH);

        painelDesenho = new VisualizacaoPanel();
        JScrollPane scrollArvore = new JScrollPane(
                painelDesenho,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scrollArvore.setBorder(null);
        add(scrollArvore, BorderLayout.CENTER);

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
        return "ÁRVORE BINÁRIA NÃO ORDENADA:\n\n"
                + "É uma estrutura de dados hierárquica em que cada nó tem no máximo dois filhos, "
                + "geralmente referidos como filho da esquerda e filho da direita. O nó no topo da hierarquia é chamado de raiz.\n"
                + "Nesta visualização (não ordenada), os nós são adicionados no primeiro espaço disponível da esquerda para a direita, nível por nível.\n"
                + "Prós: Representação eficiente de hierarquias. A busca pode ser eficiente (O(log n)) se a árvore for balanceada (como em uma Árvore de Busca Binária).\n"
                + "Contras: Em uma árvore não ordenada como esta, a busca é O(n), pois pode ser necessário percorrer todos os nós.";
    }

    // --- MÉTODOS DE MANIPULAÇÃO DA ÁRVORE ---
    private void adicionarElemento() {
        String valor = campoDeEntrada.getText().trim();
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        No novoNo = new No(valor);
        if (raiz == null) {
            raiz = novoNo;
        } else {
            // Itera através dos níveis da árvore para encontrar um lugar para inserir
            int altura = calcularAltura(raiz);
            for (int nivel = 1; nivel <= altura + 1; nivel++) {
                if (tentarInserirNoNivel(raiz, novoNo, nivel, 1)) {
                    break; // Sai do loop assim que a inserção for bem-sucedida
                }
            }
        }

        campoDeEntrada.setText("");
        painelDesenho.repaint();
    }

    private void removerElemento() {
        String valor = campoDeEntrada.getText().trim();
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor a ser removido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (raiz == null) {
            JOptionPane.showMessageDialog(this, "A árvore está vazia.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        No noParaRemover = buscarNo(raiz, valor);

        if (noParaRemover == null) {
            JOptionPane.showMessageDialog(this, "Valor não encontrado na árvore.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Encontrar o último nó (o mais profundo e à direita)
        InfoUltimoNo info = new InfoUltimoNo();
        encontrarUltimoNo(raiz, null, 1, info);
        No ultimoNo = info.no;
        No paiDoUltimo = info.pai;

        // Caso especial: se o nó a ser removido é o último nó
        if (noParaRemover == ultimoNo) {
            // Se for a raiz
            if (paiDoUltimo == null) {
                raiz = null;
            } else { // Se não for a raiz
                if (paiDoUltimo.direita == ultimoNo) {
                    paiDoUltimo.direita = null;
                } else {
                    paiDoUltimo.esquerda = null;
                }
            }
        } else {
            // Troca o valor do nó a ser removido pelo valor do último nó
            noParaRemover.valor = ultimoNo.valor;

            // Remove a referência ao último nó do seu pai
            if (paiDoUltimo.direita == ultimoNo) {
                paiDoUltimo.direita = null;
            } else {
                paiDoUltimo.esquerda = null;
            }
        }

        campoDeEntrada.setText("");
        painelDesenho.repaint();
    }

    // --- MÉTODOS AUXILIARES RECURSIVOS ---
    /**
     * Calcula a altura de uma árvore/sub-árvore.
     */
    private int calcularAltura(No no) {
        if (no == null) {
            return 0;
        }
        return 1 + Math.max(calcularAltura(no.esquerda), calcularAltura(no.direita));
    }

    /**
     * Tenta inserir um novo nó em um nível específico. Retorna true se
     * conseguiu.
     */
    private boolean tentarInserirNoNivel(No noAtual, No novoNo, int nivelAlvo, int nivelAtual) {
        if (noAtual == null) {
            return false;
        }

        // Se estamos no nível PAI do nível alvo, tentamos inserir nos filhos
        if (nivelAtual == nivelAlvo - 1) {
            if (noAtual.esquerda == null) {
                noAtual.esquerda = novoNo;
                return true;
            }
            if (noAtual.direita == null) {
                noAtual.direita = novoNo;
                return true;
            }
        }

        // Continua a busca recursivamente nos níveis inferiores
        if (tentarInserirNoNivel(noAtual.esquerda, novoNo, nivelAlvo, nivelAtual + 1)) {
            return true;
        }
        return tentarInserirNoNivel(noAtual.direita, novoNo, nivelAlvo, nivelAtual + 1);
    }

    /**
     * Busca um nó com um valor específico na árvore.
     */
    private No buscarNo(No noAtual, String valor) {
        if (noAtual == null) {
            return null;
        }
        if (noAtual.valor.equals(valor)) {
            return noAtual;
        }
        No encontradoNaEsquerda = buscarNo(noAtual.esquerda, valor);
        if (encontradoNaEsquerda != null) {
            return encontradoNaEsquerda;
        }
        return buscarNo(noAtual.direita, valor);
    }

    /**
     * Encontra o último nó da árvore (mais profundo, mais à direita) e seu pai.
     */
    private void encontrarUltimoNo(No noAtual, No pai, int nivel, InfoUltimoNo info) {
        if (noAtual == null) {
            return;
        }

        // Se encontrarmos um nó em um nível mais profundo, ele se torna o novo "último"
        // O ">=" garante que, no mesmo nível, o nó mais à direita será o escolhido
        if (nivel >= info.nivel) {
            info.no = noAtual;
            info.pai = pai;
            info.nivel = nivel;
        }

        encontrarUltimoNo(noAtual.esquerda, noAtual, nivel + 1, info);
        encontrarUltimoNo(noAtual.direita, noAtual, nivel + 1, info);
    }

    // --- CLASSE INTERNA DE DESENHO ---
    private class VisualizacaoPanel extends JPanel {

        public VisualizacaoPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Tema.BORDER));
        }

        private void atualizarTamanhoPanel() {
            int altura = calcularAltura(raiz);
            int largura = (int) Math.pow(2, altura) * 50;
            int totalHeight = altura * 80 + 100;

            setPreferredSize(new Dimension(
                    Math.max(largura, 400),
                    Math.max(totalHeight, 300)
            ));
            revalidate();
        }

        @Override
        protected void paintComponent(Graphics g) {
            atualizarTamanhoPanel();

            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (raiz != null) {
                desenhar((Graphics2D) g, raiz, getWidth() / 2, 40, getWidth() / 4);
            } else {
                g2d.setFont(Tema.FONT_SUBHEADING);
                g2d.setColor(Tema.TEXT_SECONDARY);
                g2d.drawString("Árvore vazia. Adicione elementos para visualizar.", getWidth() / 2 - 40, getHeight() / 2);
            }
        }

        private void desenhar(Graphics2D g, No no, int x, int y, int offset) {
            if (no == null) {
                return;
            }
            if (no.esquerda != null) {
                g.setColor(Tema.TREE_EDGE);
                g.setStroke(new BasicStroke(2));
                g.drawLine(x, y, x - offset, y + 60);
                desenhar(g, no.esquerda, x - offset, y + 60, offset / 2);
            }
            if (no.direita != null) {
                g.setColor(Tema.TREE_EDGE);
                g.drawLine(x, y, x + offset, y + 60);
                desenhar(g, no.direita, x + offset, y + 60, offset / 2);
            }

            g.setColor(Tema.TREE_NODE);
            g.fillOval(x - 20, y - 20, 40, 40);
            g.setColor(Color.WHITE);
            g.drawString(no.valor, x - 5, y + 5);
        }
    }
}
