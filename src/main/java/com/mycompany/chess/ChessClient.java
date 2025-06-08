package com.mycompany.chess;

import com.mycompany.chess.pieces.piece;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ChessClient {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final Board board;
    private volatile boolean isRunning;
    public String NickServer;

    public ChessClient(Board board, String host, int port) throws IOException {
        this.board = board;
        this.socket = new Socket(host, port);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.isRunning = true;
    }

public String getNick(){
        return NickServer;
    }
    
 public void onPlayerInfoReceived(PlayerInfo info) {
        // Пустая реализация по умолчанию
    }
 public void sendPlayerInfo(PlayerInfo playerInfo) throws IOException {
        out.writeObject(playerInfo);
        out.flush();
        System.out.println("PlayerInfo sent to server:" + playerInfo.getUsername());
    }
public void start() {
    new Thread(() -> {
        try {
            Object received = in.readObject();
            
            if (!(received instanceof PlayerInfo)) {
                throw new IOException("Получен некорректный формат данных");
            }

            PlayerInfo playerInfo = (PlayerInfo) received;
            
            SwingUtilities.invokeLater(() -> {
                    this.NickServer = playerInfo.getUsername();
                    onPlayerInfoReceived(playerInfo); 
                    System.out.println("playerInfo was received");
                });
            
            new Thread(this::receiveMessages).start();
        } catch (Exception e) {
            if (isRunning) {
                SwingUtilities.invokeLater(() -> 
                    JOptionPane.showMessageDialog(null, 
                        "Ошибка получения данных: " + e.getMessage(),
                        "Ошибка", 
                        JOptionPane.ERROR_MESSAGE));
            }
        }
    }).start();
}
public void SendSurrender() throws IOException{
        out.writeObject("Surrender");
        out.flush();
    }
private void receiveMessages() {
    try {
        while (isRunning) {
            Object obj = in.readObject();
            System.out.println("Получено сообщение: " + obj);

            if (obj instanceof MoveData) {
                MoveData data = (MoveData) obj;
                piece piece = board.getPiece(data.oldCol, data.oldRow);
                move move = new move(board, piece, data.newCol, data.newRow);

                SwingUtilities.invokeLater(() -> {
                    synchronized (board) {
                        try {
                            board.makeOponnentMove(move);
                        } catch (IOException ex) {
                            Logger.getLogger(ChessClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        board.repaint();
                    }
                });

            } else if (obj instanceof String) {
                String message = (String) obj;
                if ("Surrender".equals(message)) {
                    SwingUtilities.invokeLater(() -> {
                        board.TheOpponentSurrender();
                    });
                } else {
                    System.out.println("Неизвестная строка: " + message);
                }
            }
            else {
                System.out.println("Неизвестный тип объекта: " + obj.getClass());
            }
        }
    } catch (EOFException e) {
        System.out.println("The server has gone down");
    } catch (Exception e) {
        if (isRunning) {
            System.err.println("Connection error:" + e.getMessage());
            if(!board.isGameOver){
            SwingUtilities.invokeLater(() -> {
                        board.TheOpponentSurrender();
            });
        }
            
        }
    } finally {
        closeConnection();
    }
}


public synchronized void sendMove(move move) throws IOException {
    if (out == null) {
        throw new IOException("Connection lost or not established!");
    }
    System.out.println("Sending a move:" + move);
    MoveData data = new MoveData(move.oldCol,move.oldRow,move.newCol,move.newRow);
    out.writeObject(data);
    out.flush();
    System.out.println("The move has been sent.");
}

public void stop() {
        isRunning = false;
        closeConnection();
    }

private void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Ошибка закрытия соединения: " + e.getMessage());
        }
    }
}