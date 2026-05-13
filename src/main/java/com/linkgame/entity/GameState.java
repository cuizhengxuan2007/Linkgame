package com.linkgame.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 游戏状态类
 * 
 * 用于保存和加载游戏进度，实现了Serializable接口以支持对象序列化
 * 包含了游戏的所有关键状态信息，如用户信息、游戏面板、分数、时间等
 * 
 * @author LinkGame Team
 * @version 1.0
 * @see Serializable
 */
public class GameState implements Serializable {
    /** 序列化版本号，用于确保序列化和反序列化的兼容性 */
    private static final long serialVersionUID = 1L;
    
    /** 当前游戏的用户名 */
    private String username;
    
    /** 
     * 游戏面板的二维数组表示
     * - 0表示空位置
     * - 1-13表示不同类型的方块（对应A-M字母）
     */
    private int[][] board;
    
    /** 当前游戏分数 */
    private int score;
    
    /** 游戏已用时间（秒） */
    private long time;
    
    /** 游戏剩余时间（秒） */
    private long remainingTime;
    
    /** 游戏保存的时间戳 */
    private Date saveTime;
    
    /** 游戏面板的行数 */
    private int rows;
    
    /** 游戏面板的列数 */
    private int cols;
    
    /** 游戏难度设置 */
    private Difficulty difficulty;
    
    /**
     * 默认构造函数
     * 创建一个空的游戏状态对象
     */
    public GameState() {}
    
    /**
     * 获取用户名
     * 
     * @return 当前游戏的用户名
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * 设置用户名
     * 
     * @param username 要设置的用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * 获取游戏面板
     * 
     * @return 表示游戏状态的二维数组
     */
    public int[][] getBoard() {
        return board;
    }
    
    /**
     * 设置游戏面板
     * 
     * @param board 要设置的二维数组表示的游戏面板
     */
    public void setBoard(int[][] board) {
        this.board = board;
    }
    
    /**
     * 获取当前分数
     * 
     * @return 游戏分数
     */
    public int getScore() {
        return score;
    }
    
    /**
     * 设置分数
     * 
     * @param score 要设置的分数值
     */
    public void setScore(int score) {
        this.score = score;
    }
    
    /**
     * 获取已用时间
     * 
     * @return 游戏已用时间（秒）
     */
    public long getTime() {
        return time;
    }
    
    /**
     * 设置已用时间
     * 
     * @param time 要设置的时间（秒）
     */
    public void setTime(long time) {
        this.time = time;
    }
    
    /**
     * 获取剩余时间
     * 
     * @return 游戏剩余时间（秒）
     */
    public long getRemainingTime() {
        return remainingTime;
    }
    
    /**
     * 设置剩余时间
     * 
     * @param remainingTime 要设置的剩余时间（秒）
     */
    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }
    
    /**
     * 获取保存时间
     * 
     * @return 游戏保存时的日期时间
     */
    public Date getSaveTime() {
        return saveTime;
    }
    
    /**
     * 设置保存时间
     * 
     * @param saveTime 要设置的保存时间
     */
    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }
    
    /**
     * 获取面板行数
     * 
     * @return 游戏面板的行数
     */
    public int getRows() {
        return rows;
    }
    
    /**
     * 设置面板行数
     * 
     * @param rows 要设置的行数
     */
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    /**
     * 获取面板列数
     * 
     * @return 游戏面板的列数
     */
    public int getCols() {
        return cols;
    }
    
    /**
     * 设置面板列数
     * 
     * @param cols 要设置的列数
     */
    public void setCols(int cols) {
        this.cols = cols;
    }
    
    /**
     * 获取难度设置
     * 
     * @return 游戏难度枚举值
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }
    
    /**
     * 设置难度
     * 
     * @param difficulty 要设置的游戏难度
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
