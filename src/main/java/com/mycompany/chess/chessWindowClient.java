package com.mycompany.chess;
import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class chessWindowClient extends javax.swing.JFrame {

    public chessWindowClient(String userName, String IP, String Port) throws IOException {
        this.getContentPane().setBackground(Color.decode("#cad5be"));
        int intPort = Integer.parseInt(Port);
        board = new Board(isWhite);

        
        client = new ChessClient(board, IP, intPort) {
            @Override
            public void onPlayerInfoReceived(PlayerInfo playerinfo) {
                SwingUtilities.invokeLater(() -> {
                    jLabel2.setText(playerinfo.getUsername());
                    board.isWhiteChoose = playerinfo.isWhite();
                    board.pieceList.clear();
                    board.addPieces(board.isWhiteChoose);
                    board.repaint();
                });
                 try {
                    PlayerInfo clientInfo = new PlayerInfo(userName, board.isWhiteChoose);
                    sendPlayerInfo(clientInfo);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, 
                        "Ошибка отправки ника: " + e.getMessage(),
                        "Ошибка", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
            
        };
        client.start();
       board.setClient(client);
        initComponents();
        jLabel7.setText("Вы: " + userName);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel jPanel1 = board;
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel1.setText("Противник:");

        jLabel7.setText("jLabel7");

        jButton1.setText("Сдаться");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addComponent(jLabel7)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 110, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(98, 98, 98)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 379, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(99, 99, 99))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    ChessServer server;
    Board board;
    boolean isWhite;
    ChessClient client;
            
    public void sendMove(move move) {
    if (server != null) {
        try {
            server.sendMove(move);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Ошибка отправки хода",
                "Сетевая ошибка",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

public boolean isServerRunning() {
    return server != null && server.isRunning();
}
 public void YouSurrender() {
    String color = board.isWhiteChoose ? "Чёрные":"Белые";
    JOptionPane.showMessageDialog(null, color + " выиграли", "Результаты партии", JOptionPane.INFORMATION_MESSAGE);
    board.isGameOver = true;
 }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      int confirm = JOptionPane.showConfirmDialog(null, "Вы уверены, что хотите сдаться?", "Подтверждение", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            if (client != null) {
                client.SendSurrender();
            }
            YouSurrender(); // метод, который завершает партию для себя
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ошибка при отправке команды сдачи: " + ex.getMessage());
        }
    }
    }//GEN-LAST:event_jButton1ActionPerformed
public void GameDrawn() {
    JOptionPane.showMessageDialog(null, "Партия завершилась вничью.");
    board.isGameOver = true;
}
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new chessWindowClient(args[0], args[1], args[2]).setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(chessWindowClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    // End of variables declaration//GEN-END:variables
}
