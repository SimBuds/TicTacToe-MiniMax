// TicTacToe Group Project
// Casey Hsu - 101376814
// Matthew Price - 100723485
// Lukas Canji - 101 329 428
// Feriel Maamer 101091286

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JFrame implements ActionListener {

    Board board;
    Player player1;
    Player player2;
    Player currentPlayer;
    boolean gameOver;
    
    private JButton[][] buttons;
    private static final String FRAME_TITLE = "CFML - TicTacToe";
    private static final Color BUTTON_COLOR = new Color(50, 50, 50);
    private static final Color BUTTON_TEXT_COLOR = new Color(150, 0, 50);
    private static final Color BOARD_BACKGROUND_COLOR = new Color(255, 255, 255);
    private static final Font BUTTON_FONT = new Font("Calibri", Font.BOLD, 40);

    public Game() {
        board = new Board();
        gameOver = false;
        buttons = new JButton[Board.AREA][Board.AREA];
    }

    public void launch() {
        initializeGUI();
        initializePlayers();
    }

    private void initializeGUI() {
        setTitle(FRAME_TITLE);
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(Board.AREA, Board.AREA));
        getContentPane().setBackground(BOARD_BACKGROUND_COLOR);
        setLocationRelativeTo(null);
    
        for (int row = 0; row < Board.AREA; row++) {
            for (int col = 0; col < Board.AREA; col++) {
                buttons[row][col] = new JButton("");
                buttons[row][col].addActionListener(this);
                buttons[row][col].setFont(BUTTON_FONT);
                buttons[row][col].setBackground(BUTTON_COLOR);
                buttons[row][col].setForeground(BUTTON_TEXT_COLOR);
                buttons[row][col].setFocusPainted(false);
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
                int[] move = currentPlayer.getMove(board);
                board.makeMove(move[0], move[1], currentPlayer.getSymbol());
                updateBoard();
                checkGameState();
            }
        }
    }    

    private void resetGame() {
        board = new Board();
        gameOver = false;
        for (int row = 0; row < Board.AREA; row++) {
            for (int col = 0; col < Board.AREA; col++) {
                buttons[row][col].setText("");
                buttons[row][col].setEnabled(true);
            }
        }
        initializePlayers();
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

        Object[] options2 = {"First", "Second"};
        int choice2 = JOptionPane.showOptionDialog(this, "Do you want to go first?", "First or Second?",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options2, options2[0]);
        if (choice2 == 0) {
            currentPlayer = player1;
        } else if (choice2 == 1) {
            currentPlayer = player2;
        } else {
            System.exit(0);
        }
        if (currentPlayer.isAI()) {
            int[] move = currentPlayer.getMove(board);
            board.makeMove(move[0], move[1], currentPlayer.getSymbol());
            updateBoard();
            checkGameState();
        } else {
            JOptionPane.showMessageDialog(this, "Player " + currentPlayer.getSymbol() + " goes first!", "First Move",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }    
}