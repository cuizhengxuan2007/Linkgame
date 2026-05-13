package com.linkgame.entity;

import java.io.Serializable;

/**
 * 分数记录类
 * 
 * 用于存储玩家在游戏中的成绩记录，包括用户名、分数、难度、用时和日期等信息
 * 实现了Comparable接口以支持按分数排序（降序），实现Serializable接口以支持持久化存储
 * 
 * @author LinkGame Team
 * @version 1.0
 * @see Comparable
 * @see Serializable
 */
public class ScoreRecord implements Serializable, Comparable<ScoreRecord> {
    /** 序列化版本号，用于确保序列化和反序列化的兼容性 */
    private static final long serialVersionUID = 1L;
    
    /** 玩家用户名 */
    private String username;
    
    /** 游戏得分 */
    private int score;
    
    /** 游戏难度 */
    private Difficulty difficulty;
    
    /** 游戏用时（秒） */
    private long time;
    
    /** 游戏完成日期（格式：yyyy-MM-dd HH:mm:ss） */
    private String date;
    
    /**
     * 默认构造函数
     * 创建一个空的分数记录对象
     */
    public ScoreRecord() {}
    
    /**
     * 带参数的构造函数
     * 
     * @param username 玩家用户名
     * @param score 游戏得分
     * @param difficulty 游戏难度
     * @param time 游戏用时（秒）
     * @param date 游戏完成日期
     */
    public ScoreRecord(String username, int score, Difficulty difficulty, long time, String date) {
        this.username = username;
        this.score = score;
        this.difficulty = difficulty;
        this.time = time;
        this.date = date;
    }
    
    /**
     * 获取用户名
     * 
     * @return 玩家用户名
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
     * 获取分数
     * 
     * @return 游戏得分
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
     * 获取难度
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
    
    /**
     * 获取用时
     * 
     * @return 游戏用时（秒）
     */
    public long getTime() {
        return time;
    }
    
    /**
     * 设置用时
     * 
     * @param time 要设置的时间（秒）
     */
    public void setTime(long time) {
        this.time = time;
    }
    
    /**
     * 获取日期
     * 
     * @return 游戏完成日期字符串
     */
    public String getDate() {
        return date;
    }
    
    /**
     * 设置日期
     * 
     * @param date 要设置的日期字符串
     */
    public void setDate(String date) {
        this.date = date;
    }
    
    /**
     * 比较方法，用于按分数降序排序
     * 分数高的记录排在前面
     * 
     * @param other 要比较的另一个分数记录
     * @return 负数表示当前记录分数更高，正数表示另一记录分数更高，0表示分数相同
     */
    @Override
    public int compareTo(ScoreRecord other) {
        // 降序排列：分数高的在前
        return Integer.compare(other.score, this.score);
    }
}