import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChessGame extends JFrame {

    private JPanel chessBoard;
    private JLabel[][] squares;
    private JLabel selectedPiece;
    private int[] selectedPiecePos;

    public ChessGame() {
        // Set the window title and size
        setTitle("Chess Game");
        setSize(800, 800);

        // Create the chess board
        chessBoard = new JPanel();
        chessBoard.setLayout(new GridLayout(8, 8));
        squares = new JLabel[8][8];

        // Create the chess pieces and add them to the board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JLabel square = new JLabel();
                if ((i + j) % 2 == 0) {
                    square.setBackground(Color.WHITE);
                } else {
                    square.setBackground(Color.BLACK);
                }
                squares[i][j] = square;
                chessBoard.add(square);
            }
        }

        // Add the chess pieces to their initial positions
        JLabel rook1 = new JLabel(new ImageIcon("rook.png"));
        squares[0][0].add(rook1);
        JLabel knight1 = new JLabel(new ImageIcon("knight.png"));
        squares[1][0].add(knight1);
        JLabel bishop1 = new JLabel(new ImageIcon("bishop.png"));
        squares[2][0].add(bishop1);
        JLabel queen = new JLabel(new ImageIcon("queen.png"));
        squares[3][0].add(queen);
        JLabel king = new JLabel(new ImageIcon("king.png"));
        squares[4][0].add(king);
        JLabel bishop2 = new JLabel(new ImageIcon("bishop.png"));
        squares[5][0].add(bishop2);
        JLabel knight2 = new JLabel(new ImageIcon("knight.png"));
        squares[6][0].add(knight2);
        JLabel rook2 = new JLabel(new ImageIcon("rook.png"));
        squares[7][0].add(rook2);

        for (int i = 0; i < 8; i++) {
            JLabel pawn = new JLabel(new ImageIcon("pawn.png"));
            squares[i][1].add(pawn);
        }

        // Add the board to the window
        add(chessBoard);

        // Add mouse listener for chess board
        chessBoard.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / 100;
                int y = e.getY() / 100;
                if (selectedPiece == null) {
                    // Select piece
                    if (squares[x][y].getComponentCount() > 0) {
                        selectedPiece = (JLabel) squares[x][y].getComponent(0);
                        selectedPiecePos = new int[] {x, y};
                    }
                } else {
                    // Move piece
                    if (isValidMove(selectedPiecePos[0], selectedPiecePos[1], x, y)) {
                        squares[x][y].removeAll();
                        squares[x][y].add(selectedPiece);
                        squares[selectedPiecePos[0]][selectedPiecePos[1]].removeAll();
                        selectedPiece = null;
                        selectedPiecePos = null;
                    } else {
                        // Invalid move
                        JOptionPane.showMessageDialog(null, "Invalid move!");
                    }
                }
            }
        });

        // Show the window
        setVisible(true);
    }

        
    
    private boolean isValidMove(int fromX, int fromY, int toX, int toY) {
        // Add your chess move validation logic here
        // Example: only allow the pawn to move forward and capture diagonally
        if (selectedPiece.getIcon().equals(new ImageIcon("pawn.png"))) {
            if (fromX == toX && fromY + 1 == toY) {
                return true;
            } else if (fromX + 1 == toX && fromY + 1 == toY) {
                return true;
            } else if (fromX - 1 == toX && fromY + 1 == toY) {
                return true;
            } else {
                return false;
            }
        }
        if (selectedPiece.getIcon().equals(new ImageIcon("rook.png"))) {
            if (fromX == toX || fromY == toY) {
                return true;
            } else {
                return false;
            }
        }

        if (selectedPiece.getIcon().equals(new ImageIcon("knight.png"))) {
            if ((Math.abs(fromX - toX) == 2 && Math.abs(fromY - toY) == 1) || (Math.abs(fromX - toX) == 1 && Math.abs(fromY - toY) == 2)) {
                return true;
            } else {
                return false;
            }
        }

        if (selectedPiece.getIcon().equals(new ImageIcon("bishop.png"))) {
            if (Math.abs(fromX - toX) == Math.abs(fromY - toY)) {
                return true;
            } else {
                return false;
            }
        }
        
        if (selectedPiece.getIcon().equals(new ImageIcon("queen.png"))) {
            if((Math.abs(fromX - toX) == Math.abs(fromY - toY))|| fromX == toX || fromY == toY) {
                return true;
            }else{
                return false;
            }
        }
        
        if (selectedPiece.getIcon().equals(new ImageIcon("king.png"))) {
            if((Math.abs(fromX - toX) <= 1) && (Math.abs(fromY - toY) <= 1)) {
                return true;
            }else{
                return false;
            }
        }
        
        

        return true; // for now just return true for all other pieces
    }


    private boolean isCheck(int kingX, int kingY) {
        // Check if any opponent's pieces can attack the king's position
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (squares[i][j].getComponentCount() > 0) {
                    JLabel piece = (JLabel) squares[i][j].getComponent(0);
                    if (!piece.getIcon().equals(new ImageIcon("king.png"))
                            && isValidMove(i, j, kingX, kingY)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isCheckmate() {
        int kingX = -1, kingY = -1;
        // Find the position of the king
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (squares[i][j].getComponentCount() > 0) {
                    JLabel piece = (JLabel) squares[i][j].getComponent(0);
                    if (piece.getIcon().equals(new ImageIcon("king.png"))) {
                        kingX = i;
                        kingY = j;
                        break;
                    }
                }
            }
        }
    
        if (isCheck(kingX, kingY)) {
            // Iterate through all the pieces on the board
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (squares[i][j].getComponentCount() > 0) {
                        // Iterate through all the possible moves for the piece
                        for (int x = 0; x < 8; x++) {
                            for (int y = 0; y < 8; y++) {
                                if (isValidMove(i, j, x, y)) {
                                    // If there's a valid move, it's not checkmate
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            // If no valid move is found, it's checkmate
            return true;
        } else {
            // If the king is not in check, it's not checkmate
            return false;
        }
    }

    public static void main(String[] args) {
        new ChessGame();
    }
}

