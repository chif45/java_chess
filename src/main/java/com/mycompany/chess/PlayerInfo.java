package com.mycompany.chess;

import java.io.Serializable;

public class PlayerInfo implements Serializable {
    private final String username;
    private final boolean isWhite;
    
    public PlayerInfo(String username, boolean isWhite) {
        this.username = username;
        this.isWhite = isWhite;
    }
    
    public String getUsername() {
        return username;
    }
    
    public boolean isWhite() {
        return isWhite;
    }
}