package com.mycompany.chess;
import java.io.Serializable;

public class MoveData implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public int oldCol;
    public int oldRow;
    public int newCol;
    public int newRow;


    public MoveData(int oldCol, int oldRow, int newCol, int newRow) {
        this.oldCol =  oldCol;
        this.oldRow = 7 - oldRow;
        this.newCol = newCol;
        this.newRow = 7 - newRow;

    }
}