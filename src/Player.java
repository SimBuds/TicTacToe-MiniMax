import java.util.Random;
// Casey Hsu - 101376814
public class Player {

    private char symbol;
    private boolean isAI;
    private boolean smartAI;
    private int[] move;

    public Player(char symbol, boolean isAI, boolean smartAI) {
        this.symbol = symbol;
        this.isAI = isAI;
        this.smartAI = smartAI;
    }
    public char getSymbol() {
        return symbol;
    }

    public void placeMove(Board board) {
        move = getMove(board);
        if (move[0] != -1 && move[1] != -1) {
            board.makeMove(move[0], move[1], symbol);
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
        int[] bestMove = new int[]{-1, -1};
        int bestValue = Integer.MIN_VALUE;
        char opponentSymbol = getOpponentSymbol(symbol);
    
        for (int row = 0; row < Board.AREA; row++) {
            for (int col = 0; col < Board.AREA; col++) {
                if (board.isValidMove(row, col)) {
                    Board tempBoard = new Board(board.getBoard());
                    tempBoard.makeMove(row, col, symbol);
                    int moveValue = minimax(tempBoard, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE, opponentSymbol);
                    if (moveValue > bestValue) {
                        bestValue = moveValue;
                        bestMove = new int[]{row, col};
                    }
                }
            }
        }
        return bestMove;
    }
    
    public int minimax(Board board, int depth, boolean isMaximizing, int alpha, int beta, char currentSymbol) {
        if (board.checkWinner(symbol)) {
            return 10 - depth;
        } else if (board.checkWinner(getOpponentSymbol(symbol))) {
            return depth - 10;
        } else if (board.checkDraw()) {
            return 0;
        }
    
        char opponentSymbol = getOpponentSymbol(currentSymbol);
    
        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int row = 0; row < Board.AREA; row++) {
                for (int col = 0; col < Board.AREA; col++) {
                    if (board.isValidMove(row, col)) {
                        Board tempBoard = new Board(board.getBoard());
                        tempBoard.makeMove(row, col, currentSymbol);
                        int eval = minimax(tempBoard, depth + 1, false, alpha, beta, opponentSymbol);
                        maxEval = Math.max(maxEval, eval);
                        alpha = Math.max(alpha, eval);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int row = 0; row < Board.AREA; row++) {
                for (int col = 0; col < Board.AREA; col++) {
                    if (board.isValidMove(row, col)) {
                        Board tempBoard = new Board(board.getBoard());
                        tempBoard.makeMove(row, col, currentSymbol);
                        int eval = minimax(tempBoard, depth + 1, true, alpha, beta, opponentSymbol);
                        minEval = Math.min(minEval, eval);
                        beta = Math.min(beta, eval);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return minEval;
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