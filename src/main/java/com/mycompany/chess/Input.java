
package com.mycompany.chess;

import com.mycompany.chess.pieces.piece;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Input extends MouseAdapter {

    Board board;
     public Input (Board board){
        this.board = board;
    }
     
    @Override
    public void mousePressed(MouseEvent e) {
       int col = e.getX() / board.titleSize;
       int row = e.getY() / board.titleSize;
       
       piece pieceXY = board.getPiece(col, row);
       
       if(pieceXY != null){
           board.selectedPiece = pieceXY;
       }
    
    
    }

    @Override
    public void mouseReleased(MouseEvent e) {
       int col = e.getX() / board.titleSize;
       int row = e.getY() / board.titleSize;
       
       if(board.selectedPiece != null){
           move move = new move(board, board.selectedPiece, col, row);
           
           if (board.isValidMove(move)){
               try {
                   board.makeMove(move);
               } catch (IOException ex) {
                   Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, ex);
               }
           }else{
               board.selectedPiece.xPosition = board.selectedPiece.coloumn * board.titleSize;
               board.selectedPiece.yPosition = board.selectedPiece.row * board.titleSize;
           }
           
       }
       board.selectedPiece = null;
       board.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
       
        if (board.selectedPiece != null){
            board.selectedPiece.xPosition = e.getX() - board.titleSize / 2;
            board.selectedPiece.yPosition = e.getY() - board.titleSize / 2;
            
            board.repaint();
        }
        
    }

    
}
