package com.mycompany.chess;
import com.mycompany.chess.pieces.piece;
import java.io.Serializable;


public class move implements Serializable{
    private static final long serialVersionUID = 1L;
    int oldCol;
    int oldRow;
    int newCol;
    int newRow;
    
    piece piece;
    piece capture;
    
    public move(Board board, piece piece, int newCol, int newRow){
        this.oldCol = piece.coloumn;
        this.oldRow = piece.row;
        this.newCol = newCol;
        this.newRow = newRow;
        
        this.piece = piece;
        this.capture = board.getPiece(newCol,newRow);
    }
}
