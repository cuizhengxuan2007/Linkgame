package com.linkgame.entity;

public enum Difficulty {
    EASY("简单模式", 8, 12, 5, 300),
    HARD("困难模式", 10, 10, 12, 600);
    
    private String name;
    private int rows;
    private int cols;
    private int tileTypes;
    private int timeLimit;
    
    Difficulty(String name, int rows, int cols, int tileTypes, int timeLimit) {
        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.tileTypes = tileTypes;
        this.timeLimit = timeLimit;
    }
    
    public String getName() {
        return name;
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    public int getTileTypes() {
        return tileTypes;
    }
    
    public int getTimeLimit() {
        return timeLimit;
    }
}