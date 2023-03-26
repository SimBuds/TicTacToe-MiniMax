import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Game extends JFrame implements ActionListener{

    Board board;
    Player player1;
    Player player2;
    Player currentPlayer;
    boolean gameOver;
    Scanner scanner;
    private boolean aiTurn = false;
    private JButton[][] buttons;
    private static final String FRAME_TITLE = "Bar Down Tic Tac Toe";

    public Game() {
        board = new Board();
        scanner = new Scanner(System.in);
        gameOver = false;
        buttons = new JButton[Board.AREA][Board.AREA];
        initializePlayers();
    }

    private void initializeGUI() {
        setTitle(FRAME_TITLE);
        setSize(350, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(Board.AREA, Board.AREA));

        for (int row = 0; row < Board.AREA; row++) {
            for (int col = 0; col < Board.AREA; col++) {
                buttons[row][col] = new JButton("");
                buttons[row][col].addActionListener(this);
                add(buttons[row][col]);
            }
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int row = 0; row < Board.AREA; row++) {
            for (int col = 0; col < Board.AREA; col++) {
                if (e.getSource() == buttons[row][col]) {
                    boolean moveMade = board.makeMove(row, col, currentPlayer.getSymbol());
                    if (moveMade) {
                        buttons[row][col].setText(Character.toString(currentPlayer.getSymbol()));
                        buttons[row][col].setEnabled(false);
                    }
                }
            }
        }
        checkGameState();
    }

    private void checkGameState() {
        if (board.checkWinner(currentPlayer.getSymbol())) {
            JOptionPane.showMessageDialog(this, "Player " + currentPlayer.getSymbol() + " wins!", "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            gameOver = true;
        } else if (board.checkDraw()) {
            JOptionPane.showMessageDialog(this, "It's a tie!", "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            gameOver = true;
        }
        if (gameOver) {
            if (JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Play Again?",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        } else {
            currentPlayer = (currentPlayer == player1) ? player2 : player1;
            if (currentPlayer.isAI()) {
                aiTurn = true;
                currentPlayer.makeMove(board);
                updateBoard();
                checkGameState();
            }
        }
    }

    private void resetGame() {
        board = new Board();
        gameOver = false;
        currentPlayer = player1;
        for (int row = 0; row < Board.AREA; row++) {
            for (int col = 0; col < Board.AREA; col++) {
                buttons[row][col].setText("");
                buttons[row][col].setEnabled(true);
            }
        }
    }

    private void updateBoard() {
        for (int row = 0; row < Board.AREA; row++) {
            for (int col = 0; col < Board.AREA; col++) {
                if (board.getBoard()[row][col] != ' ') {
                    buttons[row][col].setText(Character.toString(board.getBoard()[row][col]));
                    buttons[row][col].setEnabled(false);
                }
            }
        }
    }

    public void initializePlayers() {
        initializeGUI();
        Object[] options = {"Human", "AI"};
        int choice = JOptionPane.showOptionDialog(this, "Choose your opponent:", "Opponent Selection",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            player1 = new Player('X', false, false);
            player2 = new Player('O', false, false);
        } else if (choice == 1) {
            if (JOptionPane.showConfirmDialog(this, "You really want easy mode?", "AI Selection Menu",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                player1 = new Player('X', false, false);
                player2 = new Player('O', true, false);
            } else if (JOptionPane.showConfirmDialog(this, "Do you want to play against a hard AI?", "AI Selection Menu",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                player1 = new Player('X', false, false);
                player2 = new Player('O', true, true);
            } else {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
        currentPlayer = player1;
        if (currentPlayer.isAI()) {
            aiTurn = true;
            currentPlayer.makeMove(board);
            updateBoard();
            checkGameState();
        }
    }    
}