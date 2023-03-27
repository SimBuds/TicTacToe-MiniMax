// Casey Hsu - 101376814
public class Board {

    public final char[][] board;
    public static final int AREA = 3;

    public Board() {
        board = new char[AREA][AREA];
        startingBoard();
    }

    public Board(char[][] inputBoard) {
        board = new char[AREA][AREA];
        for (int row = 0; row < AREA; row++) {
            for (int col = 0; col < AREA; col++) {
                board[row][col] = inputBoard[row][col];
            }
        }
    }

    public void startingBoard() {
        for (int row = 0; row < AREA; row++) {
            for (int col = 0; col < AREA; col++) {
                board[row][col] = ' ';
            }
        }
    }

    public boolean makeMove(int row, int col, char symbol) {
        if (isValidMove(row, col)) {
            board[row][col] = symbol;
            return true;
        }
        return false;
    }

    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < AREA && col >= 0 && col < AREA && board[row][col] == ' ';
    }

    public boolean checkWinner(char symbol) {
        for (int i = 0; i < AREA; i++) {
            if ((board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) ||
                (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol)) {
                return true;
            }
        }
        return (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
               (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol);
    }

    public boolean checkDraw() {
        for (int row = 0; row < AREA; row++) {
            for (int col = 0; col < AREA; col++) {
                if (board[row][col] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public char getSymbolAt(int row, int col) {
        return board[row][col];
    }

    public char[][] getBoard() {
        char[][] boardCopy = new char[AREA][AREA];
        for (int row = 0; row < AREA; row++) {
            for (int col = 0; col < AREA; col++) {
                boardCopy[row][col] = board[row][col];
            }
        }
        return boardCopy;
    }
}