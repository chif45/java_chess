package com.mycompany.chess.pieces;

import com.mycompany.chess.Board;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;


public class piece implements Serializable {
    private static final long serialVersionUID = 1L;
    public int coloumn, row, xPosition,yPosition;
    
    public boolean isWhite, isWhiteChosen;
    public String name;
    public int value;
    
    BufferedImage sheet;
    protected int sheetScale;
    Image sprite;
    Board board;
    
    public boolean isFirstMove = true;
    
    public piece(Board board) {
        this.board = board;
        loadImage(); 
    }
    
    public boolean isValidMovement(int col, int row){return true;}
    public boolean moveCollidesWithPiece(int col, int row){return false;}
    
    private void loadImage() {
        try {
            sheet = ImageIO.read(getClass().getResource("/pieces.png"));
            if (sheet == null) {
                throw new IOException("Файл изображения не найден!");
            }
            sheetScale = sheet.getWidth() / 6;
        } catch (IOException e) {
            e.printStackTrace();
            sheet = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            sheetScale = 16;
        }
    }
    
    public void paint(Graphics2D g2d) {
        if (sprite != null) {
            g2d.drawImage(sprite, xPosition, yPosition, null);
        } else {
            // Отладочный вариант (если спрайт не загрузился)
            g2d.setColor(isWhite ? Color.WHITE : Color.BLACK);
            g2d.fillRect(xPosition, yPosition, board.titleSize, board.titleSize);
        }
    }
}