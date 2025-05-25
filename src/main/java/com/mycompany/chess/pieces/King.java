package com.mycompany.chess.pieces;

import com.mycompany.chess.Board;
import com.mycompany.chess.move;
import java.awt.image.BufferedImage;

public class King extends piece {
    public King(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.coloumn = col;
        this.row = row;
        this.xPosition = col * board.titleSize;
        this.yPosition = row * board.titleSize;
        this.isWhite = isWhite;
        this.name = "King";
        

        int spriteX = 0 * sheetScale; 
        int spriteY = isWhite ? 0 : sheetScale; 
        this.sprite = sheet.getSubimage(spriteX, spriteY, sheetScale, sheetScale)
                .getScaledInstance(board.titleSize, board.titleSize, BufferedImage.SCALE_SMOOTH);
    }
    
    public boolean isValidMovement(int col, int row){
        return Math.abs((col - this.coloumn) * (row - this.row)) == 1 || Math.abs(col - this.coloumn) + Math.abs(row - this.row) == 1 || canCastle(col,row);
    
    }
    private boolean canCastle(int col, int row){
        if (this.row == row){
            if (col == 6){
                piece rook = board.getPiece(7,row);
                if (rook != null && rook.isFirstMove && isFirstMove){
                    return board.getPiece(5,row) == null &&
                            board.getPiece(6,row) == null && 
                            !board.checkScanner.isKingChecked(new move(board,this,5,row));
                }
            }else if (col == 2){
                piece rook = board.getPiece(0,row);
                if (rook != null && rook.isFirstMove && isFirstMove){
                    return board.getPiece(3,row) == null &&
                            board.getPiece(2,row) == null && 
                            board.getPiece(1,row) == null && 
                            !board.checkScanner.isKingChecked(new move(board,this,3,row));
                }
            }
        }
        return false;
    }
    
}
