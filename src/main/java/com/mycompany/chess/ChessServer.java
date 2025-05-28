package com.mycompany.chess;
import com.mycompany.chess.Board;
import com.mycompany.chess.move;
import com.mycompany.chess.pieces.piece;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class ChessServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final Board board;
    private volatile boolean isRunning;
    public String Nickname;
    public String NickClient;

    public ChessServer(Board board, int port, String Nick) throws IOException {
        this.board = board;
        this.serverSocket = new ServerSocket(port);
        this.isRunning = true;
        this.Nickname = Nick;
    }

    public void start() {
        System.out.println("Server started. Waiting for player...");
        
        new Thread(() -> {
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Player connected!");
                
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());
                
                PlayerInfo playerInfo = new PlayerInfo(Nickname,!board.isWhiteChoose );
                out.writeObject(playerInfo);
                out.flush();
                    
                Object received = in.readObject();
                if (received instanceof PlayerInfo) {
                    PlayerInfo clientInfo = (PlayerInfo) received;
                    System.out.println("Received PlayerInfo from client:" + clientInfo.getUsername());
                    this.NickClient = clientInfo.getUsername();
                    onClientInfoReceived(clientInfo.getUsername());
                }
                System.out.println("playerInfo was sended");
                
                new Thread(this::receiveMessages).start();

                
            } catch (IOException e) {
                if (isRunning) {
                    System.err.println("Connection error:" + e.getMessage());
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ChessServer.class.getName()).log(Level.SEVERE, null, ex);
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
            System.out.println("Message received: " + obj);

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
                    System.out.println("Unknown string: " + message);
                }
            } else {
                System.out.println("Unknown object type: " + obj.getClass());
            }
        }
    } catch (EOFException e) {
        System.out.println("The server has gone down");
    } catch (Exception e) {
        if (isRunning) {
            System.err.println("Connection error:" + e.getMessage());
        }
    } finally {
        closeConnection();
    }
}
     
public void onClientInfoReceived(String clientNickname) {

    }
    public String getNick(){
        return NickClient;
    }

public synchronized void sendMove(move move) throws IOException {
    if (out == null || clientSocket.isClosed()) {
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
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Server shutdown error:" + e.getMessage());
        }
    }
    public boolean isRunning() {
        return isRunning;
    }
    private void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection:" + e.getMessage());
        }
    }
}