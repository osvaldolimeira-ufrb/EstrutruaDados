package br.edu.ufrb.estruturadados;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class GrafosDijkstraPanel extends JPanel {

    // Estruturas de Dados do Grafo
    private List<Node> nos;
    private Map<Node, Map<Node, Integer>> adjacencia; // Map<Origem, Map<Destino, Peso>>

    // Controle de Interação
    private Node noSelecionado;
    private Node noInicio;
    private Node noFim;

    // Controle Visual
    private List<Node> caminhoDestacado; // Para pintar o resultado do algoritmo
    private VisualizacaoPanel painelDesenho;
    private JTextArea textoLog;

    public GrafosDijkstraPanel() {
        nos = new ArrayList<>();
        adjacencia = new HashMap<>();
        caminhoDestacado = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Tema.BACKGROUND);

        JPanel painelControles = ComponentesUI.criarPainelModerno();
        painelControles.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton btnLimpar = ComponentesUI.criarBotaoModerno("Limpar grafo", Tema.ERROR);
        JButton btnBFS = ComponentesUI.criarBotaoModerno("Executar busca (BFS)", Tema.SECONDARY);
        JButton btnDijkstra = ComponentesUI.criarBotaoModerno("Executar Dijkstra", Tema.PRIMARY);
        JLabel lblInstrucao = ComponentesUI.criarLabelEstilizado("<html>Clique esquerdo: Criar Nó / Conectar Nós<br>Clique direito: Definir Início (Verde) / Fim (Vermelho)</html>", "body");

        painelControles.add(btnLimpar);
        painelControles.add(btnBFS);
        painelControles.add(btnDijkstra);
        painelControles.add(lblInstrucao);

        add(painelControles, BorderLayout.NORTH);

        painelDesenho = new VisualizacaoPanel();
        add(painelDesenho, BorderLayout.CENTER);

        textoLog = ComponentesUI.criarAreaTextoEstilizada();
        textoLog.setText(getDefinicao());
        JScrollPane scrollLog = new JScrollPane(textoLog);
        scrollLog.setBorder(null);
        scrollLog.setPreferredSize(new Dimension(Tema.FIELD_WIDTH_CODE, Tema.FIELD_HEIGHT_INFO));
        add(scrollLog, BorderLayout.SOUTH);

        btnLimpar.addActionListener(e -> limparGrafo());
        btnBFS.addActionListener(e -> executarBFS());
        btnDijkstra.addActionListener(e -> executarDijkstra());
    }

    private String getDefinicao() {
        return "GRAFOS (Dijkstra e BFS):\n\n"
                + "Uma estrutura de dados não-linear composta por vértices (nós) e arestas (conexões). "
                + "Muito utilizada para modelar redes, rotas de GPS e relacionamentos complexos.\n\n"
                + "Algoritmos de Busca:\n"
                + "- BFS (O(V + E)): Explora camada por camada. Ideal para grafos sem peso.\n"
                + "- Dijkstra (O(E log V)): Usa uma fila de prioridade para encontrar o caminho de menor custo acumulado.\n\n"
                + "Prós: O Dijkstra garante matematicamente o caminho mais curto global em grafos com pesos positivos.\n"
                + "Contras: O Dijkstra não suporta arestas com pesos negativos e é computacionalmente mais lento que a BFS se o grafo não tiver pesos.";
    }

    private void limparGrafo() {
        nos.clear();
        adjacencia.clear();
        noSelecionado = null;
        noInicio = null;
        noFim = null;
        caminhoDestacado.clear();
        painelDesenho.repaint();
    }

    // --- Lógica dos Algoritmos ---
    private void executarBFS() {
        if (noInicio == null || noFim == null) {
            JOptionPane.showMessageDialog(this, "Defina um nó de Início e Fim com o botão direito.");
            return;
        }

        caminhoDestacado.clear();
        Queue<Node> fila = new LinkedList<>();
        Set<Node> visitados = new HashSet<>();
        Map<Node, Node> anterior = new HashMap<>();

        fila.add(noInicio);
        visitados.add(noInicio);

        boolean encontrou = false;

        while (!fila.isEmpty()) {
            Node atual = fila.poll();
            if (atual.equals(noFim)) {
                encontrou = true;
                break;
            }

            if (adjacencia.containsKey(atual)) {
                for (Node vizinho : adjacencia.get(atual).keySet()) {
                    if (!visitados.contains(vizinho)) {
                        visitados.add(vizinho);
                        anterior.put(vizinho, atual);
                        fila.add(vizinho);
                    }
                }
            }
        }

        if (encontrou) {
            reconstruirCaminho(anterior);
            aviso("BFS: Caminho encontrado!\nPassos: " + (caminhoDestacado.size() - 1));
        } else {
            erro("BFS: Destino inalcançável a partir do início.");
            painelDesenho.repaint();
        }
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void executarDijkstra() {
        if (noInicio == null || noFim == null) {
            JOptionPane.showMessageDialog(this, "Defina um nó de Início e Fim com o botão direito.");
            return;
        }

        caminhoDestacado.clear();

        // Distâncias iniciais
        Map<Node, Integer> distancias = new HashMap<>();
        Map<Node, Node> anterior = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(distancias::get));

        for (Node n : nos) {
            distancias.put(n, Integer.MAX_VALUE);
        }
        distancias.put(noInicio, 0);
        pq.add(noInicio);

        while (!pq.isEmpty()) {
            Node atual = pq.poll();

            if (atual.equals(noFim)) {
                break; // Chegamos (otimização)
            }
            // Se distância atual é infinito, ignora
            if (distancias.get(atual) == Integer.MAX_VALUE) {
                continue;
            }

            if (adjacencia.containsKey(atual)) {
                for (Map.Entry<Node, Integer> aresta : adjacencia.get(atual).entrySet()) {
                    Node vizinho = aresta.getKey();
                    int peso = aresta.getValue();
                    int novaDist = distancias.get(atual) + peso;

                    if (novaDist < distancias.get(vizinho)) {
                        // Remova da fila antes de atualizar o valor no mapa
                        pq.remove(vizinho);
                        // atualização dos valores
                        distancias.put(vizinho, novaDist);
                        anterior.put(vizinho, atual);
                        // Adiciona novo valor de prioridade
                        pq.add(vizinho);
                    }
                }
            }
        }

        if (distancias.get(noFim) != Integer.MAX_VALUE) {
            reconstruirCaminho(anterior);
            aviso("Dijkstra: Caminho Mínimo encontrado!\nCusto Total: " + distancias.get(noFim));
        } else {
            erro("Dijkstra: Destino inalcançável.");
            painelDesenho.repaint();
        }
    }

    private void reconstruirCaminho(Map<Node, Node> anterior) {
        Node atual = noFim;
        while (atual != null) {
            caminhoDestacado.add(0, atual); // Adiciona no início
            atual = anterior.get(atual);
        }
        painelDesenho.repaint();
    }

    // --- Classes Internas e Visualização ---
    private class Node {

        String id;
        int x, y;

        public Node(String id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        // Usado para mapas e comparações
        @Override
        public String toString() {
            return id;
        }
    }

    private class VisualizacaoPanel extends JPanel {

        public VisualizacaoPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Tema.BORDER));
            MouseHandler h = new MouseHandler();
            addMouseListener(h);
            addMouseMotionListener(h);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (nos.isEmpty()) {
                g2d.setFont(Tema.FONT_SUBHEADING);
                g2d.setColor(Tema.TEXT_SECONDARY);
                g2d.drawString("Grafo vazio. Adicione elementos para visualizar.", getWidth() / 2 - 40, getHeight() / 2);
                return;
            }

            // Arestas
            for (Node origem : adjacencia.keySet()) {
                for (Map.Entry<Node, Integer> entry : adjacencia.get(origem).entrySet()) {
                    Node destino = entry.getKey();
                    boolean inPath = caminhoDestacado.contains(origem) && caminhoDestacado.contains(destino)
                            && Math.abs(caminhoDestacado.indexOf(origem) - caminhoDestacado.indexOf(destino)) == 1;

                    g2d.setColor(inPath ? Tema.WARNING : Tema.BORDER.darker());
                    g2d.setStroke(new BasicStroke(inPath ? 3 : 2));
                    g2d.drawLine(origem.x, origem.y, destino.x, destino.y);

                    int mx = (origem.x + destino.x) / 2;
                    int my = (origem.y + destino.y) / 2;
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(mx - 10, my - 10, 20, 20);
                    g2d.setColor(Tema.TEXT_PRIMARY);
                    g2d.setFont(Tema.FONT_CODE);
                    String p = String.valueOf(entry.getValue());
                    g2d.drawString(p, mx - g2d.getFontMetrics().stringWidth(p) / 2, my + 5);
                }
            }

            // Nós
            int r = 18;
            for (Node n : nos) {
                Color fill = Tema.PRIMARY_LIGHT;
                if (n == noInicio) {
                    fill = Tema.SUCCESS;
                } else if (n == noFim) {
                    fill = Tema.ERROR;
                } else if (caminhoDestacado.contains(n)) {
                    fill = Tema.WARNING;
                }

                g2d.setColor(fill);
                g2d.fillOval(n.x - r, n.y - r, 2 * r, 2 * r);

                g2d.setColor(n == noSelecionado ? Tema.PRIMARY : Color.DARK_GRAY);
                g2d.setStroke(new BasicStroke(n == noSelecionado ? 3 : 1));
                g2d.drawOval(n.x - r, n.y - r, 2 * r, 2 * r);

                g2d.setColor(Color.BLACK);
                g2d.setFont(Tema.FONT_BUTTON);
                g2d.drawString(n.id, n.x - 4, n.y + 5);
            }
        }
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            Node clicado = getNoEm(e.getX(), e.getY());

            if (SwingUtilities.isRightMouseButton(e)) {
                // Botão Direito: Menu de Contexto
                if (clicado != null) {
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem itemInicio = new JMenuItem("Definir como Início");
                    JMenuItem itemFim = new JMenuItem("Definir como Fim");

                    itemInicio.addActionListener(ev -> {
                        noInicio = clicado;
                        painelDesenho.repaint();
                    });
                    itemFim.addActionListener(ev -> {
                        noFim = clicado;
                        painelDesenho.repaint();
                    });

                    menu.add(itemInicio);
                    menu.add(itemFim);
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
                return;
            }

            // Botão Esquerdo
            if (clicado == null) {
                // Criar novo nó
                String id = String.valueOf(nos.size());
                Node novo = new Node(id, e.getX(), e.getY());
                nos.add(novo);
                painelDesenho.repaint();
            } else {
                // Lógica de seleção e criação de aresta
                if (noSelecionado == null) {
                    noSelecionado = clicado;
                } else {
                    if (noSelecionado != clicado) {
                        // Adicionar aresta
                        String pesoStr = JOptionPane.showInputDialog("Peso da aresta:", "1");
                        try {
                            int peso = Integer.parseInt(pesoStr);
                            // Adiciona (Grafo Direcionado para simplificar Dijkstra)
                            adjacencia.computeIfAbsent(noSelecionado, k -> new HashMap<>()).put(clicado, peso);
                            adjacencia.computeIfAbsent(clicado, k -> new HashMap<>()).put(noSelecionado, peso);
                        } catch (NumberFormatException ex) {
                            // Cancelou ou inválido
                        }
                        noSelecionado = null;
                    } else {
                        noSelecionado = null; // Deseleciona
                    }
                }
                painelDesenho.repaint();
            }
        }
    }

    private Node getNoEm(int x, int y) {
        int raio = 20;
        for (Node n : nos) {
            if (Math.abs(n.x - x) < raio && Math.abs(n.y - y) < raio) {
                return n;
            }
        }
        return null;
    }
}
