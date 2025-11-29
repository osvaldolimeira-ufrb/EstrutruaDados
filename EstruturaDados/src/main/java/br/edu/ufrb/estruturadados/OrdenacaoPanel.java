package br.edu.ufrb.estruturadados;

import br.edu.ufrb.estruturadados.algoritmosOrdenacao.BubbleSort;
import br.edu.ufrb.estruturadados.algoritmosOrdenacao.InsertionSort;
import br.edu.ufrb.estruturadados.algoritmosOrdenacao.PainelVisualizacao;
import br.edu.ufrb.estruturadados.algoritmosOrdenacao.SelectionSort;
import br.edu.ufrb.estruturadados.algoritmosOrdenacao.SortAlgorithm;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;

public class OrdenacaoPanel extends JPanel {

    private static final int MAX_TAMANHO = 30;
    private static final int DELAY_MIN = 50, DELAY_MAX = 1000;

    private final Map<String, SortAlgorithm> algoritmos;
    private final List<Integer> numeros = new ArrayList<>();

    private JTextField campoEntrada, campoTamanho;
    private JButton botaoAdicionar, botaoLimpar, botaoOrdenar, botaoAleatorio, botaoPausar;
    private JComboBox<String> seletorMetodo;
    private JSlider sliderVelocidade;
    private JTextArea textoDefinicao;
    private PainelVisualizacao painelDesenho;

    private SwingWorker<Void, List<Integer>> worker;
    private final AtomicBoolean pausado = new AtomicBoolean(false);

    public OrdenacaoPanel() {
        algoritmos = new LinkedHashMap<>();
        algoritmos.put("Bubble Sort", new BubbleSort());
        algoritmos.put("Selection Sort", new SelectionSort());
        algoritmos.put("Insertion Sort", new InsertionSort());
        configurarInterface();
    }

    private void configurarInterface() {
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

        campoEntrada = ComponentesUI.criarCampoTextoModerno(5);
        seletorMetodo = ComponentesUI.criarComboBoxModerno(
                algoritmos.keySet().toArray(String[]::new));
        botaoAdicionar = ComponentesUI.criarBotaoModerno("Adicionar", Tema.SUCCESS);
        botaoLimpar = ComponentesUI.criarBotaoModerno("Limpar", Tema.WARNING);
        botaoOrdenar = ComponentesUI.criarBotaoModerno("Ordenar", Tema.PRIMARY);
        botaoPausar = ComponentesUI.criarBotaoModerno("Pausar", Tema.INFO);
        botaoLimpar.setEnabled(false);
        botaoOrdenar.setEnabled(false);
        botaoPausar.setEnabled(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelControles.add(ComponentesUI.criarLabelEstilizado("Valor:", "subheading"), gbc);

        gbc.gridx = 1;
        painelControles.add(campoEntrada, gbc);

        gbc.gridx = 2;
        painelControles.add(botaoAdicionar, gbc);

        gbc.gridx = 3;
        painelControles.add(botaoLimpar, gbc);

        gbc.gridx = 4;
        painelControles.add(ComponentesUI.criarLabelEstilizado("Método:", "subheading"), gbc);

        gbc.gridx = 5;
        painelControles.add(seletorMetodo, gbc);

        gbc.gridx = 6;
        painelControles.add(botaoOrdenar, gbc);

        gbc.gridx = 7;
        painelControles.add(botaoPausar, gbc);

        campoTamanho = ComponentesUI.criarCampoTextoModerno(5);
        botaoAleatorio = ComponentesUI.criarBotaoModerno("Gerar aleatório", Tema.SECONDARY);

        gbc.gridx = 0;
        gbc.gridy = 1;
        painelControles.add(ComponentesUI.criarLabelEstilizado("Tamanho:", "subheading"), gbc);

        gbc.gridx = 1;
        painelControles.add(campoTamanho, gbc);

        gbc.gridx = 2;
        painelControles.add(botaoAleatorio, gbc);

        JLabel labelVel = ComponentesUI.criarLabelEstilizado("Velocidade (ms):", "subheading");

        sliderVelocidade = new JSlider(DELAY_MIN, DELAY_MAX, 750);
        sliderVelocidade.setMajorTickSpacing(250);
        sliderVelocidade.setMinorTickSpacing(50);
        sliderVelocidade.setPaintTicks(true);
        sliderVelocidade.setPaintLabels(true);
        sliderVelocidade.setBackground(Tema.SURFACE);
        sliderVelocidade.setFont(Tema.FONT_BODY);

        java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
        labelTable.put(DELAY_MIN, new JLabel(String.valueOf(DELAY_MIN)));
        labelTable.put(250, new JLabel("250"));
        labelTable.put(500, new JLabel("500"));
        labelTable.put(750, new JLabel("750"));
        labelTable.put(DELAY_MAX, new JLabel(String.valueOf(DELAY_MAX)));
        sliderVelocidade.setLabelTable(labelTable);

        gbc.gridx = 0;
        gbc.gridy = 2;
        painelControles.add(labelVel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painelControles.add(sliderVelocidade, gbc);
        add(painelControles, BorderLayout.NORTH);

        painelDesenho = new PainelVisualizacao();
        painelDesenho.setBackground(Tema.BACKGROUND);
        add(painelDesenho, BorderLayout.CENTER);

        textoDefinicao = ComponentesUI.criarAreaTextoEstilizada();
        textoDefinicao.setText(algoritmos.get("Bubble Sort").getDescricao());

        JScrollPane scrollDescricao = new JScrollPane(textoDefinicao);
        scrollDescricao.setBorder(null);
        scrollDescricao.setPreferredSize(new Dimension(Tema.FIELD_WIDTH_CODE, Tema.FIELD_HEIGHT_INFO));
        add(scrollDescricao, BorderLayout.SOUTH);

        botaoAdicionar.addActionListener(e -> {
            adicionarNumero();
            atualizarEstadoBotoes();
        });

        botaoLimpar.addActionListener(e -> {
            limparLista();
            atualizarEstadoBotoes();
        });

        botaoAleatorio.addActionListener(e -> {
            gerarAleatorio();
            atualizarEstadoBotoes();
        });
        botaoOrdenar.addActionListener(e -> iniciarOrdenacao());
        botaoPausar.addActionListener(e -> alternarPausa());
        seletorMetodo.addActionListener(e -> {
            var metodo = (String) seletorMetodo.getSelectedItem();
            textoDefinicao.setText(algoritmos.get(metodo).getDescricao());
        });
    }

    private void atualizarEstadoBotoes() {
        boolean ordenando = (worker != null && !worker.isDone());

        botaoAdicionar.setEnabled(!ordenando && numeros.size() < MAX_TAMANHO);
        botaoLimpar.setEnabled(!ordenando && !numeros.isEmpty());
        botaoAleatorio.setEnabled(!ordenando);
        botaoOrdenar.setEnabled(!ordenando && numeros.size() > 1 && !estaOrdenado());
        seletorMetodo.setEnabled(!ordenando);
        campoEntrada.setEnabled(!ordenando);
        campoTamanho.setEnabled(!ordenando);
    }

    private void adicionarNumero() {
        try {
            int valor = Integer.parseInt(campoEntrada.getText().trim());
            if (valor < 0) {
                throw new NumberFormatException();
            }
            if (numeros.size() >= MAX_TAMANHO) {
                aviso("Máximo de " + MAX_TAMANHO + " elementos atingido.");
                return;
            }
            numeros.add(valor);
            campoEntrada.setText("");
            painelDesenho.atualizarLista(numeros);
        } catch (NumberFormatException ex) {
            erro("Digite um número inteiro positivo.");
        }
    }

    private void limparLista() {
        numeros.clear();
        painelDesenho.atualizarLista(numeros);
    }

    private void gerarAleatorio() {
        try {
            int tamanho = Integer.parseInt(campoTamanho.getText().trim());
            if (tamanho <= 0 || tamanho > MAX_TAMANHO) {
                erro("Informe um tamanho entre 1 e " + MAX_TAMANHO);
                return;
            }
            numeros.clear();
            Random r = new Random();
            for (int i = 0; i < tamanho; i++) {
                numeros.add(r.nextInt(90) + 10);
            }
            painelDesenho.atualizarLista(numeros);
        } catch (NumberFormatException ex) {
            erro("Digite um tamanho válido (inteiro).");
        }
    }

    private boolean estaOrdenado() {
        for (int i = 0; i < numeros.size() - 1; i++) {
            if (numeros.get(i) > numeros.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    private void iniciarOrdenacao() {
        if (numeros.isEmpty()) {
            aviso("Nenhum número para ordenar.");
            return;
        }

        if (estaOrdenado()) {
            aviso("A lista já está ordenada.");
            return;
        }

        bloquear(true);
        botaoPausar.setEnabled(true);
        sliderVelocidade.setEnabled(false);
        pausado.set(false);

        var metodo = (String) seletorMetodo.getSelectedItem();
        var algoritmo = algoritmos.get(metodo);
        int valorSlider = sliderVelocidade.getValue();
        int delay = DELAY_MAX - valorSlider + DELAY_MIN;

        worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                algoritmo.sort(numeros, (a, b) -> {
                    painelDesenho.destacarIndices(a, b);
                    painelDesenho.atualizarLista(numeros);
                    try {
                        while (pausado.get()) {
                            Thread.sleep(50);
                        }
                        Thread.sleep(delay);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                });
                return null;
            }

            @Override
            protected void done() {
                painelDesenho.destacarIndices(-1, -1);
                bloquear(false);
                botaoPausar.setEnabled(false);
                sliderVelocidade.setEnabled(true);
                atualizarEstadoBotoes();
                aviso("Ordenação concluída!");
            }
        };
        worker.execute();
    }

    private void alternarPausa() {
        pausado.set(!pausado.get());
        botaoPausar.setText(pausado.get() ? "Continuar" : "Pausar");
    }

    private void bloquear(boolean b) {
        botaoAdicionar.setEnabled(!b);
        botaoLimpar.setEnabled(!b);
        botaoAleatorio.setEnabled(!b);
        botaoOrdenar.setEnabled(!b);
        seletorMetodo.setEnabled(!b);
        campoEntrada.setEnabled(!b);
        campoTamanho.setEnabled(!b);
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
