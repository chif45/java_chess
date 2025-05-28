package com.mycompany.chess;

import com.mycompany.chess.pieces.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Board extends JPanel{
    public int titleSize = 85;
    
    int cols = 8;
    int rows = 8;
    
    ArrayList<piece> pieceList = new ArrayList<>();
    
    public piece selectedPiece;
    
    Input input = new Input(this);

    
    public CheckScanner checkScanner = new CheckScanner(this);
    
    public int enPassantTile = -1;
    
    public boolean isWhiteToMove = true;
    public boolean isGameOver = false;
    public boolean isWhiteChoose;
    
    ChessServer server;
    ChessClient client;
    

    public Board(boolean isWhite)
    {
        isWhiteChoose = isWhite;
        this.setPreferredSize(new Dimension(cols * titleSize, rows * titleSize));
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        addPieces(isWhiteChoose);
    }
    public void setClient(ChessClient client){
        this.client = client;
    }
    public void setServer(ChessServer server){
        this.server = server;
    }   
    public piece getPiece(int col, int row){
        
        for(piece piece : pieceList){
            if (piece.coloumn == col && piece.row == row){
                return piece;
            }
        }
        
        
        return null;
    
    }
    
    public void makeMove(move move) throws IOException{
        if (move.piece.name.equals("Pawn")){
            movePawn(move);
        }else if (move.piece.name.equals("King")){
            moveKing(move);
        }
        if (server != null) {
    System.out.println("Sending a move via the server");
    server.sendMove(move);
}
else if (client != null) {
    System.out.println("Sending a move via the client");
    client.sendMove(move);
}
else {
    System.err.println("Error: Neither server nor client initialized!");
}
        move.piece.coloumn = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPosition = move.newCol * titleSize;
        move.piece.yPosition = move.newRow * titleSize;
        
        move.piece.isFirstMove = false;
        
        capture(move.capture);
        
        isWhiteToMove = !isWhiteToMove;
        
        updateGameState();
       
    }
    public void TheOpponentSurrender(){
        String color = isWhiteChoose ? "Чёрные":"Белые";
        JOptionPane.showMessageDialog(null, color + "выиграли", "Результаты партии", JOptionPane.INFORMATION_MESSAGE);
    }
    public void makeOponnentMove(move move) throws IOException{
        if (move.piece.name.equals("Pawn")){
            movePawn(move);
        }else if (move.piece.name.equals("King")){
            moveKing(move);
        }
      
        move.piece.coloumn = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPosition = move.newCol * titleSize;
        move.piece.yPosition = move.newRow * titleSize;
        
        move.piece.isFirstMove = false;
        
        capture(move.capture);
        
        isWhiteToMove = !isWhiteToMove;
        
        updateGameState();
       
    }
    private void moveKing(move move){
        if (Math.abs(move.piece.coloumn - move.newCol) == 2){
            piece rook;
            if (move.piece.coloumn < move.newCol){
                rook = getPiece(7, move.piece.row);
                rook.coloumn = 5;
            }else{
                rook = getPiece(0,move.piece.row);
                rook.coloumn  = 3;
            }
            rook.xPosition = rook.coloumn * titleSize;
        }
    }
    
    public void movePawn(move move){
        
        //на проходе
        int colorIndex = move.piece.isWhite ? 1 : -1;
        if(!isWhiteChoose){
            colorIndex = move.piece.isWhite ? -1 : 1;
        }
        if (getTileNum(move.newCol, move.newRow) == enPassantTile){
            move.capture = getPiece(move.newCol, move.newRow + colorIndex);
        }
        if (Math.abs(move.piece.row - move.newRow) == 2){
            enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex);
        }else{
            enPassantTile = -1;
        }
        
        //promotions
        colorIndex = move.piece.isWhite ? 0 : 7;
        if (move.newRow == colorIndex){
            promotePawn(move);
        }

    }
    
    public void promotePawn(move move){
        pieceList.add(new Queen(this,move.newCol, move.newRow, move.piece.isWhite));
        capture(move.piece);
    }
    
    public void capture(piece piece){
        pieceList.remove(piece);
    }
    
    public boolean isValidMove(move move){
        
        if(isGameOver){
            return false;
        }
        if(move.piece.isWhite != isWhiteToMove){
            return false;
        }
        if(sameTeam(move.piece, move.capture)){
            return false;
        }
        if(!move.piece.isValidMovement(move.newCol, move.newRow)){
            return false;
        }
        if(move.piece.moveCollidesWithPiece(move.newCol, move.newRow)){
            return false;
        }
        if(checkScanner.isKingChecked(move)){
            return false;
        }
        if(move.piece.isWhite != isWhiteChoose){
            return false;
        }
            
        return true;
    }
    
    public boolean sameTeam(piece p1, piece p2){
        if(p1 == null || p2 == null){
            return false;
        }
        return p1.isWhite == p2.isWhite;
    }
    public int getTileNum(int col, int row){
        return row * rows + col;
    
    }
    
    piece findKing(boolean isWhite){
        for (piece piece : pieceList){
            if (isWhite == piece.isWhite && piece.name.equals("King")){
                return piece;
            }
        }
        return null;
    }
    
    public void addPieces(Boolean isWhite)
    {
        if(isWhite){
        //knights
        //black
        pieceList.add(new Knight(this,1,0,false));
        pieceList.add(new Knight(this,6,0,false));
        //white
        pieceList.add(new Knight(this,1,7,true));
        pieceList.add(new Knight(this,6,7,true));
        //pawn
        //white
        pieceList.add(new Pawn(this,0,6,true,isWhite));
        pieceList.add(new Pawn(this,1,6,true,isWhite));
        pieceList.add(new Pawn(this,2,6,true,isWhite));
        pieceList.add(new Pawn(this,3,6,true,isWhite));
        pieceList.add(new Pawn(this,4,6,true,isWhite));
        pieceList.add(new Pawn(this,5,6,true,isWhite));
        pieceList.add(new Pawn(this,6,6,true,isWhite));
        pieceList.add(new Pawn(this,7,6,true,isWhite));
        //black
        pieceList.add(new Pawn(this,0,1,false,isWhite));
        pieceList.add(new Pawn(this,1,1,false,isWhite));
        pieceList.add(new Pawn(this,2,1,false,isWhite));
        pieceList.add(new Pawn(this,3,1,false,isWhite));
        pieceList.add(new Pawn(this,4,1,false,isWhite));
        pieceList.add(new Pawn(this,5,1,false,isWhite));
        pieceList.add(new Pawn(this,6,1,false,isWhite));
        pieceList.add(new Pawn(this,7,1,false,isWhite));
        //Rook
        //white
        pieceList.add(new Rook(this,7,7,true));
        pieceList.add(new Rook(this,0,7,true));
        //black
        pieceList.add(new Rook(this,7,0,false));
        pieceList.add(new Rook(this,0,0,false));
        //Bishop
        //white
        pieceList.add(new Bishop(this,2,7,true));
        pieceList.add(new Bishop(this,5,7,true));
        //black
        pieceList.add(new Bishop(this,2,0,false));
        pieceList.add(new Bishop(this,5,0,false));
        //king
        //white
        pieceList.add(new King(this,4,7,true));
        //black
        pieceList.add(new King(this,4,0,false));
        //queen
        //white
        pieceList.add(new Queen(this,3,7,true));
        //black
        pieceList.add(new Queen(this,3,0,false));
        }
        else{
        // Knights 
        //black
        pieceList.add(new Knight(this, 1, 7, false));  
        pieceList.add(new Knight(this, 6, 7, false));  
        //white
        pieceList.add(new Knight(this, 1, 0, true));   
        pieceList.add(new Knight(this, 6, 0, true));   

        // Pawns 
        // black
        pieceList.add(new Pawn(this, 0, 6, false,isWhite));  
        pieceList.add(new Pawn(this, 1, 6, false,isWhite));   
        pieceList.add(new Pawn(this, 2, 6, false,isWhite));  
        pieceList.add(new Pawn(this, 3, 6, false,isWhite));   
        pieceList.add(new Pawn(this, 4, 6, false,isWhite));   
        pieceList.add(new Pawn(this, 5, 6, false,isWhite));  
        pieceList.add(new Pawn(this, 6, 6, false,isWhite));  
        pieceList.add(new Pawn(this, 7, 6, false,isWhite));  
        // white
        pieceList.add(new Pawn(this, 0, 1, true,isWhite));    
        pieceList.add(new Pawn(this, 1, 1, true,isWhite));    
        pieceList.add(new Pawn(this, 2, 1, true,isWhite));    
        pieceList.add(new Pawn(this, 3, 1, true,isWhite));  
        pieceList.add(new Pawn(this, 4, 1, true,isWhite)); 
        pieceList.add(new Pawn(this, 5, 1, true,isWhite));   
        pieceList.add(new Pawn(this, 6, 1, true,isWhite));   
        pieceList.add(new Pawn(this, 7, 1, true,isWhite));   

        // Rooks
        // black
        pieceList.add(new Rook(this, 0, 7, false));   
        pieceList.add(new Rook(this, 7, 7, false));   
        // white
        pieceList.add(new Rook(this, 0, 0, true));    
        pieceList.add(new Rook(this, 7, 0, true));    

        // Bishops
        // black
        pieceList.add(new Bishop(this, 2, 7, false)); 
        pieceList.add(new Bishop(this, 5, 7, false)); 
        // white
        pieceList.add(new Bishop(this, 2, 0, true));  
        pieceList.add(new Bishop(this, 5, 0, true)); 

        // Kings
        // black
        pieceList.add(new King(this, 4, 7, false));   
        // white
        pieceList.add(new King(this, 4, 0, true));    

        // Queens (ферзи)
        // black
        pieceList.add(new Queen(this, 3, 7, false)); 
        // white
        pieceList.add(new Queen(this, 3, 0, true));  
        }
    }
    
    private void updateGameState(){
        piece king = findKing(isWhiteToMove);
        
        if(checkScanner.isGameOver(king)){
            if(checkScanner.isKingChecked(new move(this, king, king.coloumn, king.row))){
                if(isWhiteToMove){
                    JOptionPane.showMessageDialog(null, "Чёрные выиграли", "Результаты партии", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null, "Белые выиграли", "Результаты партии", JOptionPane.INFORMATION_MESSAGE);
                } 
        }
        }
    }
    
     protected void paintComponent(Graphics figure)
    {
        Graphics2D g2d = (Graphics2D) figure;
        
        //рисуем доску
        for (int row = 0; row < rows; row++)
            for (int coloumn = 0; coloumn < cols; coloumn++)
            {
                g2d.setColor((coloumn + row) % 2 == 0 ?
                Color.decode("#b4d8ab") : Color.decode("#43633c"));
                g2d.fillRect(coloumn * titleSize, row * titleSize,titleSize,titleSize);
            }
        //рисуем ходы
        if (selectedPiece != null)
        for (int row = 0; row < rows; row++)
            for (int coloumn = 0; coloumn < cols; coloumn++){
                if (isValidMove(new move(this,selectedPiece,coloumn,row))){
                    g2d.setColor(new Color(68,180,57,190));
                    g2d.fillRect(coloumn * titleSize,row * titleSize,titleSize,titleSize);
            }
            
            }
        //рисуем фигуры
        for (piece Piece : pieceList)
        {
           Piece.paint(g2d); 
        }
    }
     // Синхронизированный метод для смены хода
public synchronized void switchTurn() {
    isWhiteToMove = !isWhiteToMove;
}

// Метод для сброса позиции выбранной фигуры
public synchronized void resetSelectedPiecePosition() {
    if (selectedPiece != null) {
        selectedPiece.xPosition = selectedPiece.coloumn * titleSize;
        selectedPiece.yPosition = selectedPiece.row * titleSize;
        selectedPiece = null;
    }
}

}
