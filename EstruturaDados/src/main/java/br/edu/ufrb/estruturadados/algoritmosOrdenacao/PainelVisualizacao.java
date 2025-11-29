package br.edu.ufrb.estruturadados.algoritmosOrdenacao;

import br.edu.ufrb.estruturadados.Tema;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class PainelVisualizacao extends JPanel {

    public PainelVisualizacao() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Tema.BORDER));
    }

    private List<Integer> numeros;
    private int indiceA = -1, indiceB = -1;

    public void atualizarLista(List<Integer> numeros) {
        this.numeros = numeros;
        repaint();
    }

    public void destacarIndices(int a, int b) {
        this.indiceA = a;
        this.indiceB = b;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (numeros == null || numeros.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Tema.TEXT_SECONDARY);
            g2.setFont(Tema.FONT_SUBHEADING);

            String msg = "A lista está vazia! Adicione ou gere valores para iniciar a visualização.";
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(msg)) / 2;
            int y = getHeight() / 2;
            g2.drawString(msg, x, y);
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int baseY = height - 50;
        int n = numeros.size();

        // Configurações de layout
        int margem = 30;
        int espacoEntreBarras = 6;

        // Calcula a largura das barras considerando os espaços entre elas
        int larguraDisponivel = width - 2 * margem - (n - 1) * espacoEntreBarras;
        int barWidth = Math.max(18, larguraDisponivel / n);

        // Centraliza o gráfico horizontalmente
        int totalWidth = n * barWidth + (n - 1) * espacoEntreBarras;
        int inicioX = (width - totalWidth) / 2;

        // Calcula escala de altura
        int maxValor = numeros.stream().max(Integer::compareTo).orElse(1);
        double escala = (double) (height - 120) / maxValor;

        for (int i = 0; i < n; i++) {
            int valor = numeros.get(i);
            int altura = (int) (valor * escala);
            int x = inicioX + i * (barWidth + espacoEntreBarras);
            int y = baseY - altura;

            // Cores baseadas no estado
            Color corBarra;
            if (i == indiceA) {
                corBarra = Tema.SORT_BAR_COMPARING;
            } else if (i == indiceB) {
                corBarra = Tema.ERROR;
            } else {
                corBarra = Tema.SORT_BAR_DEFAULT;
            }

            // Gradiente para a barra
            GradientPaint barGradient = new GradientPaint(
                    x, y, corBarra,
                    x, y + altura, corBarra.darker(),
                    false
            );
            g2d.setPaint(barGradient);
            g2d.fillRoundRect(x, y, barWidth, altura, 5, 5);

            // Sombra para profundidade
            g2d.setColor(Tema.comTransparencia(Color.BLACK, 20));
            g2d.fillRoundRect(x + 1, y + 1, barWidth, altura, 5, 5);

            // Borda destacada
            g2d.setColor(corBarra.darker());
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(x, y, barWidth, altura, 5, 5);

            String texto = String.valueOf(valor);
            g2d.setColor(Tema.TEXT_PRIMARY);
            g2d.setFont(Tema.FONT_BODY);
            int strWidth = g2d.getFontMetrics().stringWidth(texto);
            g2d.drawString(texto, x + (barWidth - strWidth) / 2, baseY + 18);
        }

        g2d.dispose();
    }
}
