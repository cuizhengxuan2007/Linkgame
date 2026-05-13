package com.linkgame.entity;

import java.io.Serializable;

public class ScoreRecord implements Serializable, Comparable<ScoreRecord> {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private int score;
    private Difficulty difficulty;
    private long time;
    private String date;
    
    public ScoreRecord() {}
    
    public ScoreRecord(String username, int score, Difficulty difficulty, long time, String date) {
        this.username = username;
        this.score = score;
        this.difficulty = difficulty;
        this.time = time;
        this.date = date;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public Difficulty getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    
    public long getTime() {
        return time;
    }
    
    public void setTime(long time) {
        this.time = time;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    @Override
    public int compareTo(ScoreRecord other) {
        return Integer.compare(other.score, this.score);
    }
}