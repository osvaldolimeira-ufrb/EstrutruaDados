package br.edu.ufrb.estruturadados;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class ComponentesUI {

    /**
     * Cria um botão moderno com estilo arredondado e efeito hover.
     *
     * @param texto Texto do botão
     * @param corFundo Cor de fundo do botão
     * @return Botão estilizado
     */
    public static JButton criarBotaoModerno(String texto, Color corFundo) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Aplicar opacidade se desabilitado
                if (!isEnabled()) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                }

                // Desenha botão arredondado
                Color corAtual = getBackground();
                if (!isEnabled()) {
                    int r = corAtual.getRed();
                    int g_color = corAtual.getGreen();
                    int b = corAtual.getBlue();
                    int gray = (r + g_color + b) / 3;
                    corAtual = new Color(
                            (r + gray * 2) / 3,
                            (g_color + gray * 2) / 3,
                            (b + gray * 2) / 3
                    );
                }

                g2d.setColor(corAtual);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(),
                        Tema.BUTTON_RADIUS, Tema.BUTTON_RADIUS);

                int shadowAlpha = isEnabled() ? 20 : 10;
                g2d.setColor(Tema.comTransparencia(Color.BLACK, shadowAlpha));
                g2d.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2,
                        Tema.BUTTON_RADIUS, Tema.BUTTON_RADIUS);

                // Borda
                g2d.setColor(corAtual.darker());
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1,
                        Tema.BUTTON_RADIUS, Tema.BUTTON_RADIUS);

                // Texto (com opacidade reduzida se desabilitado)
                Color corTexto = isEnabled() ? getForeground()
                        : new Color(getForeground().getRed(),
                                getForeground().getGreen(),
                                getForeground().getBlue(), 120);
                g2d.setColor(corTexto);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };

        // Armazenar cor original como propriedade do cliente
        botao.putClientProperty("originalColor", corFundo);

        botao.setFont(Tema.FONT_BUTTON);
        botao.setBackground(corFundo);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(false);
        botao.setOpaque(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setMargin(new Insets(5, 15, 5, 15));
        botao.setPreferredSize(new Dimension(calcularLargura(botao) + 30, 35));
        botao.setMinimumSize(new Dimension(40, 35));
        botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Efeito hover (apenas quando habilitado)
        botao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (botao.isEnabled()) {
                    Color originalColor = (Color) botao.getClientProperty("originalColor");
                    botao.setBackground(originalColor.darker());
                    botao.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (botao.isEnabled()) {
                    Color originalColor = (Color) botao.getClientProperty("originalColor");
                    botao.setBackground(originalColor);
                    botao.repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (botao.isEnabled()) {
                    Color originalColor = (Color) botao.getClientProperty("originalColor");
                    botao.setBackground(originalColor.darker().darker());
                    botao.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (botao.isEnabled()) {
                    Color originalColor = (Color) botao.getClientProperty("originalColor");
                    botao.setBackground(originalColor);
                    botao.repaint();
                }
            }
        });

        botao.addPropertyChangeListener("enabled", evt -> {
            Color originalColor = (Color) botao.getClientProperty("originalColor");
            if (originalColor != null) {
                botao.setBackground(originalColor);
                botao.setCursor(botao.isEnabled()
                        ? new Cursor(Cursor.HAND_CURSOR)
                        : new Cursor(Cursor.DEFAULT_CURSOR));
                botao.repaint();
            }
        });

        return botao;
    }

    private static int calcularLargura(JButton botao) {
        FontMetrics fm = botao.getFontMetrics(botao.getFont());
        return fm.stringWidth(botao.getText());
    }

    /**
     * Cria um campo de texto moderno com bordas arredondadas.
     *
     * @param colunas Número de colunas (largura)
     * @return Campo de texto estilizado
     */
    public static JTextField criarCampoTextoModerno(int colunas) {
        JTextField campo = new JTextField(colunas) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(),
                        Tema.BORDER_RADIUS, Tema.BORDER_RADIUS);

                super.paintComponent(g);

                Color borderColor = hasFocus() ? Tema.PRIMARY : Tema.BORDER;
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(hasFocus() ? 2 : 1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1,
                        Tema.BORDER_RADIUS, Tema.BORDER_RADIUS);

                g2d.dispose();
            }
        };

        campo.setFont(Tema.FONT_BODY);
        campo.setBackground(Tema.SURFACE);
        campo.setForeground(Tema.TEXT_PRIMARY);
        campo.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        campo.setOpaque(false);

        return campo;
    }

    /**
     * Cria um painel moderno com bordas arredondadas e sombra sutil.
     *
     * @return Painel estilizado
     */
    public static JPanel criarPainelModerno() {
        JPanel painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(Tema.comTransparencia(Color.BLACK, 8));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4,
                        Tema.BORDER_RADIUS + 2, Tema.BORDER_RADIUS + 2);

                // Painel principal
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2,
                        Tema.BORDER_RADIUS, Tema.BORDER_RADIUS);

                g2d.setColor(Tema.BORDER);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2,
                        Tema.BORDER_RADIUS, Tema.BORDER_RADIUS);

                g2d.dispose();
            }
        };

        painel.setOpaque(false);
        painel.setBackground(Tema.SURFACE);
        return painel;
    }

    /**
     * Cria um painel com gradiente de fundo.
     *
     * @param corInicio Cor inicial do gradiente
     * @param corFim Cor final do gradiente
     * @return Painel com gradiente
     */
    public static JPanel criarPainelGradiente(Color corInicio, Color corFim) {
        JPanel painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = Tema.criarGradienteVertical(
                        corInicio, corFim, getHeight());
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.dispose();
            }
        };

        painel.setOpaque(false);
        return painel;
    }

    /**
     * Cria um label estilizado.
     *
     * @param texto Texto do label
     * @param estilo Estilo: "heading", "subheading", "body"
     * @return Label estilizado
     */
    public static JLabel criarLabelEstilizado(String texto, String estilo) {
        JLabel label = new JLabel(texto);

        switch (estilo.toLowerCase()) {
            case "heading":
                label.setFont(Tema.FONT_HEADING);
                label.setForeground(Tema.TEXT_PRIMARY);
                break;
            case "subheading":
                label.setFont(Tema.FONT_SUBHEADING);
                label.setForeground(Tema.TEXT_PRIMARY);
                break;
            default:
                label.setFont(Tema.FONT_BODY);
                label.setForeground(Tema.TEXT_SECONDARY);
        }

        return label;
    }

    /**
     * Cria uma área de texto estilizada.
     *
     * @return JTextArea estilizada
     */
    public static JTextArea criarAreaTextoEstilizada() {
        JTextArea area = new JTextArea();
        area.setFont(Tema.FONT_BODY);
        area.setForeground(Tema.TEXT_PRIMARY);
        area.setBackground(new Color(248, 250, 252));
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.BORDER),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        return area;
    }

    /**
     * Cria um ComboBox estilizado.
     *
     * @param itens Itens do ComboBox
     * @return ComboBox estilizado
     */
    public static JComboBox<String> criarComboBoxModerno(String[] itens) {
        UIManager.put("ComboBox.focus", BorderFactory.createEmptyBorder());
        UIManager.put("ComboBox.border", BorderFactory.createEmptyBorder());
        UIManager.put("ComboBox.selectionBackground", Tema.PRIMARY);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);

        JComboBox<String> combo = new JComboBox<>(itens);
        combo.setFont(Tema.FONT_BODY);
        combo.setBackground(Tema.SURFACE);
        combo.setForeground(Tema.TEXT_PRIMARY);

        combo.setFocusable(false);

        combo.setBorder(new LineBorder(Tema.BORDER, 1, true));

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, false); // força sem foco
                setFont(Tema.FONT_BODY);
                setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

                if (isSelected) {
                    setBackground(Tema.PRIMARY);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(Tema.SURFACE);
                    setForeground(Tema.TEXT_PRIMARY);
                }

                return this;
            }
        });

        combo.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton();
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setOpaque(false);
                btn.setFocusable(false);
                return btn;
            }
        });

        combo.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        javax.swing.plaf.basic.ComboPopup popup
                                = (javax.swing.plaf.basic.ComboPopup) combo.getUI()
                                        .getAccessibleChild(combo, 0);

                        if (popup instanceof javax.swing.plaf.basic.BasicComboPopup basicPopup) {
                            basicPopup.setBorder(new LineBorder(Tema.BORDER, 1, true));
                            basicPopup.setOpaque(false);
                        }

                    } catch (Exception ignored) {
                        // Se não for Basic L&F, ignore
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });

        return combo;
    }

    /**
     * Cria um slider estilizado.
     *
     * @param min Valor mínimo
     * @param max Valor máximo
     * @param valor Valor inicial
     * @return Slider estilizado
     */
    public static JSlider criarSliderModerno2(int min, int max, int valor) {
        JSlider slider = new JSlider(min, max, valor);
        slider.setFont(Tema.FONT_BODY);
        slider.setBackground(Tema.BACKGROUND);
        slider.setForeground(Tema.TEXT_PRIMARY);

        return slider;
    }

    /**
     * Cria uma borda moderna arredondada.
     *
     * @param cor Cor da borda
     * @param espessura Espessura da borda
     * @return Border estilizada
     */
    public static Border criarBordaArredondada(Color cor, int espessura) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cor, espessura),
                BorderFactory.createEmptyBorder(
                        Tema.PADDING_PANEL,
                        Tema.PADDING_PANEL,
                        Tema.PADDING_PANEL,
                        Tema.PADDING_PANEL
                )
        );
    }
}
