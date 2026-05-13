package com.linkgame.entity;

import java.io.Serializable;
import java.util.Date;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private int[][] board;
    private int score;
    private long time;
    private long remainingTime;
    private Date saveTime;
    private int rows;
    private int cols;
    private Difficulty difficulty;
    
    public GameState() {}
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public int[][] getBoard() {
        return board;
    }
    
    public void setBoard(int[][] board) {
        this.board = board;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public long getTime() {
        return time;
    }
    
    public void setTime(long time) {
        this.time = time;
    }
    
    public long getRemainingTime() {
        return remainingTime;
    }
    
    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }
    
    public Date getSaveTime() {
        return saveTime;
    }
    
    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }
    
    public int getRows() {
        return rows;
    }
    
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    public void setCols(int cols) {
        this.cols = cols;
    }
    
    public Difficulty getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
