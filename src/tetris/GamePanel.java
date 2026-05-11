package tetris;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

final class GamePanel extends JPanel {
    private static final int CELL_SIZE = 30;
    private static final int BOARD_PIXEL_WIDTH = GameState.BOARD_WIDTH * CELL_SIZE;
    private static final int BOARD_PIXEL_HEIGHT = GameState.BOARD_HEIGHT * CELL_SIZE;
    private static final int SIDE_PANEL_WIDTH = 180;
    private static final int TIMER_DELAY_MS = 500;
    private static final Color BACKGROUND = new Color(24, 26, 32);
    private static final Color BOARD_BACKGROUND = new Color(34, 37, 46);
    private static final Color GRID = new Color(57, 61, 74);
    private static final Color TEXT = new Color(238, 238, 238);

    private final GameState state = new GameState();
    private final Timer timer;

    GamePanel() {
        setPreferredSize(new Dimension(BOARD_PIXEL_WIDTH + SIDE_PANEL_WIDTH, BOARD_PIXEL_HEIGHT));
        setBackground(BACKGROUND);
        setFocusable(true);
        setupKeyBindings();

        timer = new Timer(TIMER_DELAY_MS, event -> {
            state.tick();
            repaint();
        });
        timer.start();
    }

    private void setupKeyBindings() {
        bindKey("moveLeft", KeyEvent.VK_LEFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                state.moveCurrentPiece(-1, 0);
                repaint();
            }
        });

        bindKey("moveRight", KeyEvent.VK_RIGHT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                state.moveCurrentPiece(1, 0);
                repaint();
            }
        });

        bindKey("softDrop", KeyEvent.VK_DOWN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                state.moveCurrentPiece(0, 1);
                repaint();
            }
        });

        bindKey("rotate", KeyEvent.VK_UP, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                state.rotateCurrentPiece();
                repaint();
            }
        });

        bindKey("hardDrop", KeyEvent.VK_SPACE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                state.dropCurrentPiece();
                repaint();
            }
        });

        bindKey("restart", KeyEvent.VK_R, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (state.isGameOver()) {
                    state.restart();
                    timer.restart();
                    repaint();
                }
            }
        });
    }

    private void bindKey(String name, int keyCode, AbstractAction action) {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0), name);
        actionMap.put(name, action);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBoardBackground(g);
        drawLockedCells(g);
        drawCurrentPiece(g);
        drawGrid(g);
        drawSidePanel(g);

        if (state.isGameOver()) {
            drawGameOver(g);
        }

        g.dispose();
    }

    private void drawBoardBackground(Graphics2D g) {
        g.setColor(BOARD_BACKGROUND);
        g.fillRect(0, 0, BOARD_PIXEL_WIDTH, BOARD_PIXEL_HEIGHT);
    }

    private void drawLockedCells(Graphics2D g) {
        Color[][] board = state.getBoardSnapshot();
        for (int row = 0; row < GameState.BOARD_HEIGHT; row++) {
            for (int col = 0; col < GameState.BOARD_WIDTH; col++) {
                if (board[row][col] != null) {
                    drawCell(g, col, row, board[row][col]);
                }
            }
        }
    }

    private void drawCurrentPiece(Graphics2D g) {
        Piece piece = state.getCurrentPiece();
        if (piece == null) {
            return;
        }

        for (int[] cell : piece.getCells()) {
            int x = piece.getX() + cell[0];
            int y = piece.getY() + cell[1];
            if (y >= 0) {
                drawCell(g, x, y, piece.getType().getColor());
            }
        }
    }

    private void drawCell(Graphics2D g, int col, int row, Color color) {
        int x = col * CELL_SIZE;
        int y = row * CELL_SIZE;

        g.setColor(color);
        g.fillRoundRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4, 6, 6);
        g.setColor(color.brighter());
        g.drawLine(x + 5, y + 5, x + CELL_SIZE - 7, y + 5);
        g.setColor(color.darker());
        g.drawLine(x + 5, y + CELL_SIZE - 6, x + CELL_SIZE - 7, y + CELL_SIZE - 6);
    }

    private void drawGrid(Graphics2D g) {
        g.setColor(GRID);
        g.setStroke(new BasicStroke(1));
        for (int col = 0; col <= GameState.BOARD_WIDTH; col++) {
            int x = col * CELL_SIZE;
            g.drawLine(x, 0, x, BOARD_PIXEL_HEIGHT);
        }
        for (int row = 0; row <= GameState.BOARD_HEIGHT; row++) {
            int y = row * CELL_SIZE;
            g.drawLine(0, y, BOARD_PIXEL_WIDTH, y);
        }
    }

    private void drawSidePanel(Graphics2D g) {
        int panelX = BOARD_PIXEL_WIDTH;
        g.setColor(BACKGROUND);
        g.fillRect(panelX, 0, SIDE_PANEL_WIDTH, BOARD_PIXEL_HEIGHT);

        g.setColor(TEXT);
        g.setFont(new Font("SansSerif", Font.BOLD, 26));
        g.drawString("TETRIS", panelX + 28, 52);

        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.drawString("Score", panelX + 24, 112);
        g.setFont(new Font("SansSerif", Font.PLAIN, 20));
        g.drawString(String.valueOf(state.getScore()), panelX + 24, 140);

        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.drawString("Lines", panelX + 24, 190);
        g.setFont(new Font("SansSerif", Font.PLAIN, 20));
        g.drawString(String.valueOf(state.getLinesCleared()), panelX + 24, 218);

        g.setFont(new Font("SansSerif", Font.BOLD, 15));
        g.drawString("Controls", panelX + 24, 296);
        g.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g.drawString("Left / Right: Move", panelX + 24, 326);
        g.drawString("Up: Rotate", panelX + 24, 350);
        g.drawString("Down: Soft drop", panelX + 24, 374);
        g.drawString("Space: Hard drop", panelX + 24, 398);
        g.drawString("R: Restart", panelX + 24, 422);
    }

    private void drawGameOver(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, BOARD_PIXEL_WIDTH, BOARD_PIXEL_HEIGHT);

        g.setColor(TEXT);
        g.setFont(new Font("SansSerif", Font.BOLD, 34));
        drawCenteredString(g, "GAME OVER", 250);

        g.setFont(new Font("SansSerif", Font.PLAIN, 16));
        drawCenteredString(g, "Press R to restart", 292);
    }

    private void drawCenteredString(Graphics2D g, String text, int y) {
        FontMetrics metrics = g.getFontMetrics();
        int x = (BOARD_PIXEL_WIDTH - metrics.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }
}
