import java.util.Random;

public class Player {
    private char symbol;
    private boolean isAI;
    private boolean smartAI;

    public Player(char symbol, boolean isAI, boolean smartAI) {
        this.symbol = symbol;
        this.isAI = isAI;
        this.smartAI = smartAI;
    }
    public char getSymbol() {
        return symbol;
    }

    public void makeMove(Board board) {
        if (isAI) {
            if (smartAI) {
                int[] bestMove = makeSmartAIMove(board);
                board.makeMove(bestMove[0], bestMove[1], getSymbol());
            } else {
                makeAIMove(board);
            }
        }
    }

    public int[] getMove(Board board) {
        if (isAI) {
            if (smartAI) {
                return makeSmartAIMove(board);
            } else {
                return makeAIMove(board);
            }
        }
        return new int[]{-1, -1};
    }

    public int[] makeAIMove(Board board) {
        Random random = new Random();
        int row, col;
        boolean validMove = false;
        while (!validMove) {
            row = random.nextInt(3);
            col = random.nextInt(3);
            if (board.isValidMove(row, col)) {
                validMove = true;
                return new int[]{row, col};
            }
        }
        return new int[]{-1, -1};
    }

    public int[] makeSmartAIMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[2];
        for (int row = 0; row < Board.AREA; row++) {
            for (int col = 0; col < Board.AREA; col++) {
                if (board.isValidMove(row, col)) {
                    board.makeMove(row, col, symbol);
                    int score = minimax(board, 0, false);
                    board.makeMove(row, col, ' ');
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = row;
                        bestMove[1] = col;
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(Board board, int depth, boolean isMaximizing) {
        if (board.checkWinner(symbol)) {
            return 10 - depth;
        } else if (board.checkWinner(getOpponentSymbol(symbol))) {
            return depth - 10;
        } else if (board.checkDraw()) {
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int row = 0; row < Board.AREA; row++) {
                for (int col = 0; col < Board.AREA; col++) {
                    if (board.isValidMove(row, col)) {
                        board.makeMove(row, col, symbol);
                        int score = minimax(board, depth + 1, false);
                        board.makeMove(row, col, ' ');
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int row = 0; row < Board.AREA; row++) {
                for (int col = 0; col < Board.AREA; col++) {
                    if (board.isValidMove(row, col)) {
                        board.makeMove(row, col, getOpponentSymbol(symbol));
                        int score = minimax(board, depth + 1, true);
                        board.makeMove(row, col, ' ');
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    private char getOpponentSymbol(char symbol) {
        return (symbol == 'X') ? 'O' : 'X';
    }

    public boolean isAI() {
        return isAI;
    }

    public boolean isSmartAI() {
        return smartAI;
    }
}