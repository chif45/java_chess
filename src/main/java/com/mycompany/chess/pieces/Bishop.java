package com.mycompany.chess.pieces;

import com.mycompany.chess.Board;
import java.awt.image.BufferedImage;

public class Bishop extends piece {
    public Bishop(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.coloumn = col;
        this.row = row;
        this.xPosition = col * board.titleSize;
        this.yPosition = row * board.titleSize;
        this.isWhite = isWhite;
        this.name = "Bishop";
        

        int spriteX = 2 * sheetScale; 
        int spriteY = isWhite ? 0 : sheetScale; 
        this.sprite = sheet.getSubimage(spriteX, spriteY, sheetScale, sheetScale)
                .getScaledInstance(board.titleSize, board.titleSize, BufferedImage.SCALE_SMOOTH);
    }
    public boolean isValidMovement(int col, int row){
        return Math.abs(this.coloumn - col) == Math.abs(this.row - row);
    }
    
    public boolean moveCollidesWithPiece(int col, int row){
       //лево верх
       if(this.coloumn > col && this.row > row)
           for(int i = 1; i < Math.abs(this.coloumn - col); i++)
               if(board.getPiece(this.coloumn - i, this.row - i) != null)
                   return true;
       //право верх
       if(this.coloumn < col && this.row > row)
           for(int i = 1; i < Math.abs(this.coloumn - col); i++)
               if(board.getPiece(this.coloumn + i, this.row - i) != null)
                   return true;
       
        //лево низ
       if(this.coloumn > col && this.row < row)
           for(int i = 1; i < Math.abs(this.coloumn - col); i++)
               if(board.getPiece(this.coloumn - i, this.row + i) != null)
                   return true;
       //право низ
       if(this.coloumn < col && this.row < row)
           for(int i = 1; i < Math.abs(this.coloumn - col); i++)
               if(board.getPiece(this.coloumn + i, this.row + i) != null)
                   return true;
       
        return false;
    
    }
}
