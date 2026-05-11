package tetris;

final class Piece {
    private final Tetromino type;
    private final int[][] cells;
    private final int x;
    private final int y;

    Piece(Tetromino type, int x, int y) {
        this(type, type.getCells(), x, y);
    }

    private Piece(Tetromino type, int[][] cells, int x, int y) {
        this.type = type;
        this.cells = cells;
        this.x = x;
        this.y = y;
    }

    Tetromino getType() {
        return type;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    int[][] getCells() {
        int[][] copy = new int[cells.length][2];
        for (int i = 0; i < cells.length; i++) {
            copy[i][0] = cells[i][0];
            copy[i][1] = cells[i][1];
        }
        return copy;
    }

    Piece move(int dx, int dy) {
        return new Piece(type, getCells(), x + dx, y + dy);
    }

    Piece rotateClockwise() {
        if (type == Tetromino.O) {
            return this;
        }

        int[][] rotated = new int[cells.length][2];
        for (int i = 0; i < cells.length; i++) {
            int oldX = cells[i][0];
            int oldY = cells[i][1];
            rotated[i][0] = 3 - oldY;
            rotated[i][1] = oldX;
        }

        return normalize(new Piece(type, rotated, x, y));
    }

    private static Piece normalize(Piece piece) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        for (int[] cell : piece.cells) {
            minX = Math.min(minX, cell[0]);
            minY = Math.min(minY, cell[1]);
        }

        int[][] normalized = new int[piece.cells.length][2];
        for (int i = 0; i < piece.cells.length; i++) {
            normalized[i][0] = piece.cells[i][0] - minX;
            normalized[i][1] = piece.cells[i][1] - minY;
        }

        return new Piece(piece.type, normalized, piece.x, piece.y);
    }
}
