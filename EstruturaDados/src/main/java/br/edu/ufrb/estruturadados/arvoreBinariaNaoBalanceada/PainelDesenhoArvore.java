package br.edu.ufrb.estruturadados.arvoreBinariaNaoBalanceada;

import br.edu.ufrb.estruturadados.Tema;
import java.awt.*;
import javax.swing.*;

public class PainelDesenhoArvore extends JPanel {

    private ArvoreBinaria arvore;
    private ArvoreBinaria.No noDestacado;
    private Color corDestaqueBusca = new Color(255, 100, 100); // Vermelho
    private Color corDestaquePercorrendo = new Color(255, 215, 0); // Dourado

    public PainelDesenhoArvore(ArvoreBinaria arvore) {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Tema.BORDER));
        this.arvore = arvore;
        this.noDestacado = null;
        setBackground(Color.WHITE);
        setOpaque(true);
    }

    public void setArvore(ArvoreBinaria arvore) {
        this.arvore = arvore;
        repaint();
    }

    public void destacarNo(ArvoreBinaria.No no, boolean isBusca) {
        this.noDestacado = no;
        if (isBusca) {
            corDestaqueBusca = new Color(255, 100, 100);
        } else {
            corDestaqueBusca = corDestaquePercorrendo;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (arvore.getRaiz() == null) {

            g2d.setFont(Tema.FONT_SUBHEADING);
            g2d.setColor(Tema.TEXT_SECONDARY);
            g2d.drawString("Árvore vazia. Adicione elementos para visualizar.", getWidth() / 2 - 40, getHeight() / 2);
            return;
        }

        desenharNo(g2d, arvore.getRaiz(), getWidth() / 2, 50, getWidth() / 4, 0);

        g2d.dispose();
    }

    private int calcularAlturaNo(ArvoreBinaria.No no) {
        if (no == null) {
            return -1;
        }
        return 1 + Math.max(calcularAlturaNo(no.esquerda), calcularAlturaNo(no.direita));
    }

    private void desenharNo(Graphics2D g, ArvoreBinaria.No no, int x, int y, int espacamento, int nivel) {
        // Cor do nó
        if (no == noDestacado) {
            g.setColor(corDestaqueBusca);
        } else {
            g.setColor(new Color(135, 206, 250)); // Azul céu
        }
        g.fillOval(x - 20, y - 20, 40, 40);

        // Borda do nó
        g.setColor(Color.BLACK);
        g.drawOval(x - 20, y - 20, 40, 40);

        // Valor do nó
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        String textoValor = String.valueOf(no.valor);
        FontMetrics fm = g.getFontMetrics();
        int strWidth = fm.stringWidth(textoValor);
        g.drawString(textoValor, x - strWidth / 2, y + 5);

        // Altura do nó 
        String textoAltura = "h=" + calcularAlturaNo(no);
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        strWidth = g.getFontMetrics().stringWidth(textoAltura);
        g.drawString(textoAltura, x - strWidth / 2 + 30, y + 5);

        // Desenha conexões e filhos
        int yFilho = y + 80;
        if (no.esquerda != null) {
            int xFilho = x - espacamento;
            g.setColor(Color.BLACK);
            g.drawLine(x - 15, y + 15, xFilho + 5, yFilho - 15);
            desenharNo(g, no.esquerda, xFilho, yFilho, Math.max(30, espacamento / 2), nivel + 1);
        }
        if (no.direita != null) {
            int xFilho = x + espacamento;
            g.setColor(Color.BLACK);
            g.drawLine(x + 15, y + 15, xFilho - 5, yFilho - 15);
            desenharNo(g, no.direita, xFilho, yFilho, Math.max(30, espacamento / 2), nivel + 1);
        }
    }
}
