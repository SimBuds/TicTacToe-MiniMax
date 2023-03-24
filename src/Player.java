import java.util.Random;
import java.util.Scanner;

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
        } else {
            makeHumanMove(board);
        }
    }
    
    
    private int getValidInput(Scanner scanner) {
        int input;
        while (true) {
            try {
                input = scanner.nextInt();
                if (input >= 1 && input <= 3) {
                    return input;
                } else {
                    System.out.println("Invalid input! Please enter a number between 1 and 3.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number between 1 and 3.");
                scanner.nextLine();
            }
        }
    }

    public void makeHumanMove(Board board) {
        
        Scanner scanner = new Scanner(System.in);
        int row, col;
        boolean validMove = false;
        while (!validMove) {
            System.out.println("Please enter the row you want to play (1-3): ");
            row = getValidInput(scanner) - 1;
            System.out.println("Please enter the column you want to play (1-3): ");
            col = getValidInput(scanner) - 1;
            validMove = board.makeMove(row, col, symbol);
            if (!validMove) {
                System.out.println("Invalid move... Please Try again.");
            }
        }  
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
        int[] bestMove = new int[2]; // row and col of the best move
        bestMove[0] = -1;
        bestMove[1] = -1;
    
        for (int row = 0; row < Board.AREA; row++) {
            for (int col = 0; col < Board.AREA; col++) {
                if (board.isValidMove(row, col)) {
                    board.makeMove(row, col, symbol);
                    int score = minimax(board, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE, getOpponentSymbol(symbol));
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

    private int minimax(Board board, int depth, boolean isMaximizing, int alpha, int beta, char currentPlayerSymbol) {
        if (board.checkDraw()) {
            if (board.checkWinner(symbol)) {
                return 10 - depth;
            } else if (board.checkWinner(getOpponentSymbol(symbol))) {
                return depth - 10;
            } else {
                return 0;
            }
        }
    
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int row = 0; row < Board.AREA; row++) {
                for (int col = 0; col < Board.AREA; col++) {
                    if (board.isValidMove(row, col)) {
                        board.makeMove(row, col, currentPlayerSymbol);
                        int score = minimax(board, depth + 1, false, alpha, beta, getOpponentSymbol(currentPlayerSymbol));
                        board.makeMove(row, col, ' ');
                        bestScore = Math.max(score, bestScore);
                        alpha = Math.max(alpha, bestScore);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int row = 0; row < Board.AREA; row++) {
                for (int col = 0; col < Board.AREA; col++) {
                    if (board.isValidMove(row, col)) {
                        board.makeMove(row, col, currentPlayerSymbol);
                        int score = minimax(board, depth + 1, true, alpha, beta, getOpponentSymbol(currentPlayerSymbol));
                        board.makeMove(row, col, ' ');
                        bestScore = Math.min(score, bestScore);
                        beta = Math.min(beta, bestScore);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return bestScore;
        }
    }
    

    private char getOpponentSymbol(char currentPlayerSymbol) {
        if (currentPlayerSymbol == 'X') {
            return 'O';
        } else {
            return 'X';
        }
    }
    

    public boolean isAI() {
        return isAI;
    }

    public boolean isSmartAI() {
        return smartAI;
    }

}