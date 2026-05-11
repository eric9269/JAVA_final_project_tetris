package tetris;

import java.awt.Color;

enum Tetromino {
    I(new Color(0, 188, 212), new int[][] {
            {0, 0}, {1, 0}, {2, 0}, {3, 0}
    }),
    O(new Color(255, 193, 7), new int[][] {
            {0, 0}, {1, 0}, {0, 1}, {1, 1}
    }),
    T(new Color(156, 39, 176), new int[][] {
            {1, 0}, {0, 1}, {1, 1}, {2, 1}
    }),
    S(new Color(76, 175, 80), new int[][] {
            {1, 0}, {2, 0}, {0, 1}, {1, 1}
    }),
    Z(new Color(244, 67, 54), new int[][] {
            {0, 0}, {1, 0}, {1, 1}, {2, 1}
    }),
    J(new Color(63, 81, 181), new int[][] {
            {0, 0}, {0, 1}, {1, 1}, {2, 1}
    }),
    L(new Color(255, 152, 0), new int[][] {
            {2, 0}, {0, 1}, {1, 1}, {2, 1}
    });

    private final Color color;
    private final int[][] cells;

    Tetromino(Color color, int[][] cells) {
        this.color = color;
        this.cells = cells;
    }

    Color getColor() {
        return color;
    }

    int[][] getCells() {
        int[][] copy = new int[cells.length][2];
        for (int i = 0; i < cells.length; i++) {
            copy[i][0] = cells[i][0];
            copy[i][1] = cells[i][1];
        }
        return copy;
    }
}
