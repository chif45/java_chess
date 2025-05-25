package com.mycompany.chess.pieces;

import com.mycompany.chess.Board;
import java.awt.image.BufferedImage;

public class Pawn extends piece {
    public Pawn(Board board, int col, int row, boolean isWhite, boolean isWhiteH) {
        super(board);
        this.coloumn = col;
        this.row = row;
        this.xPosition = col * board.titleSize;
        this.yPosition = row * board.titleSize;
        this.isWhite = isWhite;
        this.name = "Pawn";
        this.isWhiteChosen = isWhiteH;
        

        int spriteX = 5 * sheetScale; 
        int spriteY = isWhite ? 0 : sheetScale; 
        this.sprite = sheet.getSubimage(spriteX, spriteY, sheetScale, sheetScale)
                .getScaledInstance(board.titleSize, board.titleSize, BufferedImage.SCALE_SMOOTH);
    }
    
    public boolean isValidMovement(int col, int row){
        int colorIndex = isWhite ? 1 : -1;
        if(!isWhiteChosen){
            colorIndex = isWhite ? -1 : 1;
        }
        //push pawn 1
        if (this.coloumn == col && row == this.row - colorIndex && board.getPiece(col,row) == null)
            return true;
        
         //push pawn 2
        if (isFirstMove && this.coloumn == col && row == this.row - colorIndex * 2 && board.getPiece(col,row) == null && board.getPiece(col,row + colorIndex) == null)
            return true;
        
        //capture left
        if(col == this.coloumn - 1 && row == this.row - colorIndex && board.getPiece(col,row) != null)
            return true;
        //capture right
        if(col == this.coloumn + 1 && row == this.row - colorIndex && board.getPiece(col,row) != null)
            return true;
        
        // на проходе налево(en passant)
        if(board.getTileNum(col,row) == board.enPassantTile && col == this.coloumn - 1 && row == this.row - colorIndex && board.getPiece(col,row + colorIndex)!= null){
            return true;
        }
        //en passant right
        if(board.getTileNum(col,row) == board.enPassantTile && col == this.coloumn + 1 && row == this.row - colorIndex && board.getPiece(col,row + colorIndex)!= null){
            return true;
        }
        
        return false;
    }
}
