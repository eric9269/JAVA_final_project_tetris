package tetris;

import java.awt.Color;
import java.util.Random;

final class GameState {
    static final int BOARD_WIDTH = 10;
    static final int BOARD_HEIGHT = 20;

    private final Random random = new Random();
    private Color[][] board;
    private Piece currentPiece;
    private int score;
    private int linesCleared;
    private boolean gameOver;

    GameState() {
        restart();
    }

    void restart() {
        board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
        score = 0;
        linesCleared = 0;
        gameOver = false;
        spawnPiece();
    }

    Color[][] getBoardSnapshot() {
        Color[][] snapshot = new Color[BOARD_HEIGHT][BOARD_WIDTH];
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            System.arraycopy(board[row], 0, snapshot[row], 0, BOARD_WIDTH);
        }
        return snapshot;
    }

    Piece getCurrentPiece() {
        return currentPiece;
    }

    int getScore() {
        return score;
    }

    int getLinesCleared() {
        return linesCleared;
    }

    boolean isGameOver() {
        return gameOver;
    }

    void tick() {
        if (!gameOver && !moveCurrentPiece(0, 1)) {
            lockCurrentPiece();
        }
    }

    boolean moveCurrentPiece(int dx, int dy) {
        if (gameOver) {
            return false;
        }

        Piece moved = currentPiece.move(dx, dy);
        if (isValidPosition(moved)) {
            currentPiece = moved;
            return true;
        }

        return false;
    }

    void rotateCurrentPiece() {
        if (gameOver) {
            return;
        }

        Piece rotated = currentPiece.rotateClockwise();
        if (isValidPosition(rotated)) {
            currentPiece = rotated;
            return;
        }

        Piece kickedLeft = rotated.move(-1, 0);
        if (isValidPosition(kickedLeft)) {
            currentPiece = kickedLeft;
            return;
        }

        Piece kickedRight = rotated.move(1, 0);
        if (isValidPosition(kickedRight)) {
            currentPiece = kickedRight;
        }
    }

    void dropCurrentPiece() {
        if (gameOver) {
            return;
        }

        while (moveCurrentPiece(0, 1)) {
            score += 1;
        }
        lockCurrentPiece();
    }

    private void lockCurrentPiece() {
        for (int[] cell : currentPiece.getCells()) {
            int boardX = currentPiece.getX() + cell[0];
            int boardY = currentPiece.getY() + cell[1];
            if (boardY >= 0 && boardY < BOARD_HEIGHT && boardX >= 0 && boardX < BOARD_WIDTH) {
                board[boardY][boardX] = currentPiece.getType().getColor();
            }
        }

        int removed = clearFullLines();
        if (removed > 0) {
            linesCleared += removed;
            score += calculateLineScore(removed);
        }

        spawnPiece();
    }

    private void spawnPiece() {
        Tetromino[] types = Tetromino.values();
        Tetromino type = types[random.nextInt(types.length)];
        currentPiece = new Piece(type, BOARD_WIDTH / 2 - 2, 0);
        if (!isValidPosition(currentPiece)) {
            gameOver = true;
        }
    }

    private boolean isValidPosition(Piece piece) {
        for (int[] cell : piece.getCells()) {
            int boardX = piece.getX() + cell[0];
            int boardY = piece.getY() + cell[1];

            if (boardX < 0 || boardX >= BOARD_WIDTH || boardY >= BOARD_HEIGHT) {
                return false;
            }
            if (boardY >= 0 && board[boardY][boardX] != null) {
                return false;
            }
        }
        return true;
    }

    private int clearFullLines() {
        int cleared = 0;

        for (int row = BOARD_HEIGHT - 1; row >= 0; row--) {
            if (isFullLine(row)) {
                cleared++;
                removeLine(row);
                row++;
            }
        }

        return cleared;
    }

    private boolean isFullLine(int row) {
        for (int col = 0; col < BOARD_WIDTH; col++) {
            if (board[row][col] == null) {
                return false;
            }
        }
        return true;
    }

    private void removeLine(int removedRow) {
        for (int row = removedRow; row > 0; row--) {
            System.arraycopy(board[row - 1], 0, board[row], 0, BOARD_WIDTH);
        }
        for (int col = 0; col < BOARD_WIDTH; col++) {
            board[0][col] = null;
        }
    }

    private int calculateLineScore(int removedLines) {
        switch (removedLines) {
            case 1:
                return 100;
            case 2:
                return 300;
            case 3:
                return 500;
            case 4:
                return 800;
            default:
                return 0;
        }
    }
}
