package com.mycompany.chess.pieces;

import com.mycompany.chess.Board;
import java.awt.image.BufferedImage;

public class Knight extends piece {
    public Knight(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.coloumn = col;
        this.row = row;
        this.xPosition = col * board.titleSize;
        this.yPosition = row * board.titleSize;
        this.isWhite = isWhite;
        this.name = "Knight";
        

        int spriteX = 3 * sheetScale; 
        int spriteY = isWhite ? 0 : sheetScale; 
        this.sprite = sheet.getSubimage(spriteX, spriteY, sheetScale, sheetScale)
                .getScaledInstance(board.titleSize, board.titleSize, BufferedImage.SCALE_SMOOTH);
    }
    
    public boolean isValidMovement(int col, int row){
        return Math.abs(col - this.coloumn) * Math.abs(row - this.row) == 2;
    }
}
