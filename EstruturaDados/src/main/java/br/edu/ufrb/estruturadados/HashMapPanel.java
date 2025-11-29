package br.edu.ufrb.estruturadados;

import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class HashMapPanel extends JPanel {

    private static final int CAPACIDADE = 15;
    private Integer[] tabelaHash = new Integer[CAPACIDADE];
    private int tamanhoAtual = 0;

    private PainelDesenhoHashMap painelDesenho;
    private JTextField campoValor, campoQtdAleatorio;
    private JButton botaoInserir, botaoBuscar, botaoRemover, botaoLimpar, botaoGerar;
    private JLabel labelInfo;

    private final Color COR_SUCESSO = new Color(76, 175, 80);
    private final Color COR_ERRO = new Color(244, 67, 54);

    public HashMapPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Tema.BACKGROUND);

        JPanel painelControles = ComponentesUI.criarPainelModerno();
        painelControles.setLayout(new GridBagLayout());
        painelControles.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Tema.BORDER),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;

        campoValor = ComponentesUI.criarCampoTextoModerno(6);
        campoQtdAleatorio = ComponentesUI.criarCampoTextoModerno(5);
        botaoInserir = ComponentesUI.criarBotaoModerno("Inserir", Tema.SUCCESS);
        botaoLimpar = ComponentesUI.criarBotaoModerno("Limpar tabela", Tema.WARNING);
        botaoBuscar = ComponentesUI.criarBotaoModerno("Buscar", Tema.SECONDARY);
        botaoRemover = ComponentesUI.criarBotaoModerno("Remover", Tema.ERROR);
        botaoGerar = ComponentesUI.criarBotaoModerno("Gerar aleatórios", Tema.PRIMARY);

        gbc.gridy = 0;
        gbc.gridx = 0;

        painelControles.add(ComponentesUI.criarLabelEstilizado("Valor:", "subheading"), gbc);

        gbc.gridx = 1;
        painelControles.add(campoValor, gbc);
        gbc.gridx = 2;
        painelControles.add(botaoInserir, gbc);
        gbc.gridx = 3;
        painelControles.add(botaoBuscar, gbc);
        gbc.gridx = 4;
        painelControles.add(botaoRemover, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        painelControles.add(ComponentesUI.criarLabelEstilizado("Qtd. aleatória:", "subheading"), gbc);
        gbc.gridx = 1;
        painelControles.add(campoQtdAleatorio, gbc);
        gbc.gridx = 2;
        painelControles.add(botaoGerar, gbc);
        gbc.gridx = 3;
        painelControles.add(botaoLimpar, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 5;
        labelInfo = new JLabel("Info: Insira um valor para calcular o índice.");
        painelControles.add(labelInfo, gbc);

        add(painelControles, BorderLayout.NORTH);

        painelDesenho = new PainelDesenhoHashMap();
        add(new JScrollPane(painelDesenho), BorderLayout.CENTER);

        JTextArea textoDefinicao = ComponentesUI.criarAreaTextoEstilizada();
        textoDefinicao.setText(getDefinicao());
        JScrollPane scrollPane = new JScrollPane(textoDefinicao);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(Tema.FIELD_WIDTH_CODE, Tema.FIELD_HEIGHT_INFO));
        add(scrollPane, BorderLayout.SOUTH);

        botaoInserir.addActionListener(e -> executarAcao("inserir"));
        botaoBuscar.addActionListener(e -> executarAcao("buscar"));
        botaoRemover.addActionListener(e -> executarAcao("remover"));
        botaoGerar.addActionListener(e -> gerarAleatorios());
        botaoLimpar.addActionListener(e -> limpar());

        atualizarVisualizacao();
    }

    private void limpar() {
        tabelaHash = new Integer[CAPACIDADE];
        tamanhoAtual = 0;
        labelInfo.setText("Info: Tabela foi limpa.");
        atualizarVisualizacao();
    }

    private void executarAcao(String acao) {

        try {
            int valor = Integer.parseInt(campoValor.getText().trim());

            bloquearControles(true);
            SwingWorker<Void, String> worker = new SwingWorker<>() {
                private int index;

                @Override
                protected Void doInBackground() throws Exception {
                    index = valor % CAPACIDADE;

                    publish("Calculando índice para o valor " + valor + "...");
                    Thread.sleep(500);

                    publish(valor + " % " + CAPACIDADE + " = " + index);
                    Thread.sleep(800);

                    switch (acao) {
                        case "inserir":
                            if (tabelaHash[index] != null) {
                                painelDesenho.destacarTemporario(index, COR_ERRO, 2000);
                                aviso("COLISÃO! Índice " + index + " já ocupado por " + tabelaHash[index] + ".");
                            } else {
                                painelDesenho.destacarTemporario(index, COR_SUCESSO, 2000);
                                aviso("Índice " + index + " livre. Inserindo valor " + valor + ".");
                                tabelaHash[index] = valor;
                                tamanhoAtual++;
                            }
                            break;
                        case "buscar":
                            if (tabelaHash[index] != null && tabelaHash[index].equals(valor)) {
                                painelDesenho.destacarTemporario(index, COR_SUCESSO, 2000);
                                aviso("Valor " + valor + " encontrado no índice " + index + ".");
                            } else {
                                painelDesenho.destacarTemporario(index, COR_ERRO, 2000);
                                aviso("Valor " + valor + " NÃO encontrado no índice " + index + ".");
                            }
                            break;
                        case "remover":

                            if (tabelaHash[index] != null && tabelaHash[index].equals(valor)) {
                                painelDesenho.destacarTemporario(index, COR_SUCESSO, 2000);
                                aviso("Valor " + valor + " removido do índice " + index + ".");
                                tabelaHash[index] = null;
                                tamanhoAtual--;
                            } else {
                                painelDesenho.destacarTemporario(index, COR_ERRO, 2000);
                                aviso("Valor " + valor + " NÃO encontrado.");
                            }
                            break;
                    }

                    Thread.sleep(1000);
                    return null;
                }

                @Override
                protected void process(java.util.List<String> chunks) {
                    labelInfo.setText("Info: " + chunks.get(chunks.size() - 1));
                }

                @Override
                protected void done() {
                    atualizarVisualizacao();
                    bloquearControles(false);
                    campoValor.setText("");
                }
            };
            worker.execute();

        } catch (NumberFormatException ex) {
            erro("O valor deve ser um número inteiro válido.");
        }
    }

    private void gerarAleatorios() {

        try {
            int qtd = Integer.parseInt(campoQtdAleatorio.getText().trim());
            if (qtd <= 0) {
                erro("A quantidade deve ser maior que zero.");
                return;
            }

            Random rand = new Random();
            int inseridos = 0;
            int tentados = 0;
            int limiteTentativas = Math.max(100, qtd * 5);
            while (inseridos < qtd && tamanhoAtual < CAPACIDADE && tentados < limiteTentativas) {
                int valorAleatorio = rand.nextInt(1000) + 1;
                int index = valorAleatorio % CAPACIDADE;
                if (tabelaHash[index] == null) {
                    tabelaHash[index] = valorAleatorio;
                    inseridos++;
                    tamanhoAtual++;
                }
                tentados++;
            }

            if (inseridos == 0) {
                labelInfo.setText("Info: Não foi possível inserir novos valores (colisões ou tabela cheia).");
            } else if (inseridos < qtd) {
                labelInfo.setText("Info: Inseridos " + inseridos + " de " + qtd + " valores (outros colidiram).");
            } else {
                labelInfo.setText("Info: " + inseridos + " valores aleatórios inseridos com sucesso!");
            }

            atualizarVisualizacao();

        } catch (NumberFormatException ex) {
            erro("A quantidade deve ser um número inteiro válido.");
        }
    }

    private void atualizarVisualizacao() {
        painelDesenho.atualizar(tabelaHash, CAPACIDADE);
    }

    private void bloquearControles(boolean bloquear) {
        botaoInserir.setEnabled(!bloquear);
        botaoBuscar.setEnabled(!bloquear);
        botaoRemover.setEnabled(!bloquear);
        botaoLimpar.setEnabled(!bloquear);
        botaoGerar.setEnabled(!bloquear);
        campoValor.setEnabled(!bloquear);
        campoQtdAleatorio.setEnabled(!bloquear);
    }

    private String getDefinicao() {
        return """
        Hash Map Simples (Tabela Hash):

        Estrutura de dados que associa chaves a valores, armazenando os elementos em posições determinadas por uma função hash. 
        Nesta implementação, a função hash é calculada como valor % capacidade, mapeando a chave para um índice no array e permitindo acesso direto aos valores armazenados. 
        Quando duas chaves diferentes produzem o mesmo índice, ocorre uma colisão. Nesta versão simplificada, as colisões são apenas detectadas, sem estratégias de resolução, pois o foco é compreender o mapeamento e como as colisões podem acontecer.

        Vantagens:
        - Inserção, busca e remoção geralmente muito rápidas, graças ao acesso direto via índice.
        - Estrutura eficiente para armazenar e recuperar pares de chave-valor.

        Desvantagens:
        - Pode sofrer colisões, o que reduz o desempenho se não forem tratadas.
        - Requer uma função hash eficiente para distribuir os elementos de forma uniforme.

        Complexidade (assumindo uma boa função hash):
        - Melhor caso: O(1)
        - Caso médio: O(1)
        - Pior caso (muitas colisões): O(n)""";
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    class PainelDesenhoHashMap extends JPanel {

        private Integer[] tabelaHash;
        private int capacidade;
        private int indexDestaque = -1;
        private Color corDestaque = Color.GRAY;

        public PainelDesenhoHashMap() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Tema.BORDER));
        }

        public void atualizar(Integer[] tabelaHash, int capacidade) {
            this.tabelaHash = tabelaHash;
            this.capacidade = capacidade;
            this.indexDestaque = -1;
            repaint();
        }

        public void destacarTemporario(int index, Color cor, int msDestaque) {
            this.indexDestaque = index;
            this.corDestaque = cor;
            repaint();

            // remove destaque depois do tempo
            new Timer(msDestaque, e -> {
                indexDestaque = -1;
                repaint();
            }).start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = 100, h = 60, gap = 20, cols = 5;
            int startX = (getWidth() - (cols * (w + gap))) / 2;
            int startY = 40;

            for (int i = 0; i < CAPACIDADE; i++) {
                int r = i / cols;
                int c = i % cols;
                int x = startX + c * (w + gap);
                int y = startY + r * (h + gap);

                boolean ocupado = tabelaHash[i] != null;

                Color corFundo;
                int espessuraBorda;
                Color corBorda;

                if (i == indexDestaque) {
                    corFundo = corDestaque;
                    espessuraBorda = 4;
                    corBorda = corDestaque.darker();
                } else {
                    corFundo = ocupado ? Tema.PRIMARY_LIGHT : Color.WHITE;
                    espessuraBorda = 1;
                    corBorda = Tema.BORDER.darker();
                }

                // Desenha o fundo do bucket
                g2d.setColor(corFundo);
                g2d.fillRoundRect(x, y, w, h, 10, 10);

                // Desenha a borda
                g2d.setColor(corBorda);
                g2d.setStroke(new BasicStroke(espessuraBorda));
                g2d.drawRoundRect(x, y, w, h, 10, 10);

                // Desenha o índice [i]
                g2d.setColor(Tema.TEXT_SECONDARY);
                g2d.setFont(Tema.FONT_CODE);
                g2d.drawString("[" + i + "]", x + 5, y + 20);

                // Desenha o valor armazenado (se houver)
                if (ocupado) {
                    g2d.setColor(Tema.TEXT_PRIMARY);
                    g2d.setFont(Tema.FONT_HEADING);
                    String v = String.valueOf(tabelaHash[i]);

                    // Centraliza melhor o texto
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(v);
                    int textX = x + (w - textWidth) / 2;
                    int textY = y + h / 2 + fm.getAscent() / 2;

                    g2d.drawString(v, textX, textY);
                }
            }
        }
    }
}
