package br.edu.ufrb.estruturadados;

import java.awt.*;

public class Tema {

    public static final Integer FIELD_HEIGHT_INFO = 150;

    public static final Integer FIELD_WIDTH_CODE = 100;

    // ========== CORES PRIMÁRIAS ==========
    /**
     * Azul escuro moderno para headers e elementos principais
     */
    public static final Color PRIMARY_DARK = new Color(30, 58, 138);

    /**
     * Azul vibrante para elementos interativos
     */
    public static final Color PRIMARY = new Color(59, 130, 246);

    /**
     * Azul claro para backgrounds sutis
     */
    public static final Color PRIMARY_LIGHT = new Color(147, 197, 253);

    // ========== CORES SECUNDÁRIAS ==========
    /**
     * Roxo moderno para elementos especiais
     */
    public static final Color SECONDARY = new Color(139, 92, 246);

    /**
     * Rosa/vermelho para destaques e acentos
     */
    public static final Color ACCENT = new Color(236, 72, 153);

    // ========== CORES NEUTRAS ==========
    /**
     * Cinza muito claro para background principal
     */
    public static final Color BACKGROUND = new Color(249, 250, 251);

    /**
     * Branco puro para superfícies
     */
    public static final Color SURFACE = Color.WHITE;

    /**
     * Cinza claro para bordas
     */
    public static final Color BORDER = new Color(229, 231, 235);

    /**
     * Quase preto para texto principal
     */
    public static final Color TEXT_PRIMARY = new Color(17, 24, 39);

    /**
     * Cinza médio para texto secundário
     */
    public static final Color TEXT_SECONDARY = new Color(107, 114, 128);

    // ========== CORES DE STATUS ==========
    /**
     * Verde para sucesso e ações positivas
     */
    public static final Color SUCCESS = new Color(34, 197, 94);

    /**
     * Laranja para avisos
     */
    public static final Color WARNING = new Color(251, 146, 60);

    /**
     * Vermelho para erros e ações destrutivas
     */
    public static final Color ERROR = new Color(239, 68, 68);

    /**
     * Azul para informações
     */
    public static final Color INFO = new Color(59, 130, 246);

    // ========== CORES PARA VISUALIZAÇÕES ==========
    /**
     * Cor para caixas de Array/Lista
     */
    public static final Color ARRAY_BOX = new Color(99, 102, 241);

    /**
     * Borda para caixas de Array
     */
    public static final Color ARRAY_BORDER = new Color(67, 56, 202);

    /**
     * Cor para dados de nós em listas ligadas
     */
    public static final Color NODE_DATA = new Color(34, 197, 94);

    /**
     * Cor para ponteiros em listas ligadas
     */
    public static final Color NODE_POINTER = new Color(59, 130, 246);

    /**
     * Cor para topo de pilha
     */
    public static final Color STACK_TOP = new Color(251, 146, 60);

    /**
     * Cor para corpo de pilha
     */
    public static final Color STACK_BODY = new Color(147, 197, 253);

    /**
     * Cor para nós de árvore
     */
    public static final Color TREE_NODE = new Color(139, 92, 246);

    /**
     * Cor para arestas de árvore
     */
    public static final Color TREE_EDGE = new Color(107, 114, 128);

    /**
     * Cor padrão para barras de ordenação
     */
    public static final Color SORT_BAR_DEFAULT = new Color(99, 102, 241);

    /**
     * Cor para barras sendo comparadas
     */
    public static final Color SORT_BAR_COMPARING = new Color(251, 146, 60);

    /**
     * Cor para barras já ordenadas
     */
    public static final Color SORT_BAR_SORTED = new Color(34, 197, 94);

    // ========== FONTES ==========
    /**
     * Fonte para títulos principais
     */
    public static Font FONT_HEADING;

    /**
     * Fonte para subtítulos
     */
    public static Font FONT_SUBHEADING;

    /**
     * Fonte para texto do corpo
     */
    public static Font FONT_BODY;

    /**
     * Fonte para botões
     */
    public static Font FONT_BUTTON;

    /**
     * Fonte para código
     */
    public static Font FONT_CODE;

    // Inicialização das fontes com fallback
    static {
        try {
            // Tenta usar fontes modernas do sistema
            FONT_HEADING = new Font("Segoe UI", Font.BOLD, 18);
            FONT_SUBHEADING = new Font("Segoe UI", Font.BOLD, 14);
            FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
            FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 12);
            FONT_CODE = new Font("Consolas", Font.PLAIN, 12);

            // Verifica se as fontes estão disponíveis
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] fontNames = ge.getAvailableFontFamilyNames();

            boolean hasSegoe = false;
            boolean hasConsolas = false;

            for (String fontName : fontNames) {
                if (fontName.equalsIgnoreCase("Segoe UI")) {
                    hasSegoe = true;
                }
                if (fontName.equalsIgnoreCase("Consolas")) {
                    hasConsolas = true;
                }
            }

            // Fallback para Linux
            if (!hasSegoe) {
                FONT_HEADING = new Font("DejaVu Sans", Font.BOLD, 18);
                FONT_SUBHEADING = new Font("DejaVu Sans", Font.BOLD, 14);
                FONT_BODY = new Font("DejaVu Sans", Font.PLAIN, 13);
                FONT_BUTTON = new Font("DejaVu Sans", Font.BOLD, 12);
            }

            if (!hasConsolas) {
                FONT_CODE = new Font("Monospaced", Font.PLAIN, 12);
            }

        } catch (Exception e) {
            // Fallback final para fontes padrão
            FONT_HEADING = new Font(Font.SANS_SERIF, Font.BOLD, 18);
            FONT_SUBHEADING = new Font(Font.SANS_SERIF, Font.BOLD, 14);
            FONT_BODY = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
            FONT_BUTTON = new Font(Font.SANS_SERIF, Font.BOLD, 12);
            FONT_CODE = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        }
    }

    // ========== CONSTANTES DE ESTILO ==========
    /**
     * Raio de borda arredondada padrão
     */
    public static final int BORDER_RADIUS = 8;

    /**
     * Raio de borda arredondada para botões
     */
    public static final int BUTTON_RADIUS = 6;

    /**
     * Padding padrão para componentes
     */
    public static final int PADDING_DEFAULT = 10;

    /**
     * Padding para painéis
     */
    public static final int PADDING_PANEL = 15;

    /**
     * Espessura de borda padrão
     */
    public static final int BORDER_WIDTH = 1;

    /**
     * Espessura de borda para elementos destacados
     */
    public static final int BORDER_WIDTH_THICK = 2;

    // ========== MÉTODOS UTILITÁRIOS ==========
    /**
     * Cria um gradiente vertical suave entre duas cores.
     *
     * @param c1 Cor inicial (topo)
     * @param c2 Cor final (base)
     * @param height Altura do gradiente
     * @return GradientPaint configurado
     */
    public static GradientPaint criarGradienteVertical(Color c1, Color c2, int height) {
        return new GradientPaint(0, 0, c1, 0, height, c2, false);
    }

    /**
     * Cria uma cor com transparência.
     *
     * @param color Cor base
     * @param alpha Valor de transparência (0-255)
     * @return Nova cor com transparência
     */
    public static Color comTransparencia(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    /**
     * Aplica o tema visual ao UIManager do Swing.
     */
    public static void aplicarTemaSistema() {
        try {
            // Usa o Look and Feel do sistema
            String systemLaf = javax.swing.UIManager.getSystemLookAndFeelClassName();
            javax.swing.UIManager.setLookAndFeel(systemLaf);

            // Personaliza cores do sistema
            javax.swing.UIManager.put("Panel.background", BACKGROUND);
            javax.swing.UIManager.put("Button.background", PRIMARY);
            javax.swing.UIManager.put("Button.foreground", Color.WHITE);
            javax.swing.UIManager.put("TextField.background", SURFACE);
            javax.swing.UIManager.put("TextField.border",
                    javax.swing.BorderFactory.createLineBorder(BORDER));
            javax.swing.UIManager.put("ComboBox.background", SURFACE);
            javax.swing.UIManager.put("ComboBox.border",
                    javax.swing.BorderFactory.createLineBorder(BORDER));
            javax.swing.UIManager.put("TextArea.background", BACKGROUND);

        } catch (Exception e) {
            System.err.println("Erro ao aplicar tema visual: " + e.getMessage());
        }
    }
}
