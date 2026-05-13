package com.linkgame.entity;

/**
 * 难度枚举类
 * 
 * 定义了连连看游戏的两种难度模式：简单模式和困难模式
 * 每种难度包含游戏面板的行数、列数、方块类型数量和时间限制等配置信息
 * 
 * @author LinkGame Team
 * @version 1.0
 */
public enum Difficulty {
    /**
     * 简单模式
     * - 游戏面板：8行 x 12列（实际使用区域为4x4和3-6行7-10列）
     * - 方块类型：5种不同的图案
     * - 时间限制：300秒（5分钟）
     * - 特点：面板较大但方块分布稀疏，适合新手
     */
    EASY("简单模式", 8, 12, 5, 300),
    
    /**
     * 困难模式
     * - 游戏面板：10行 x 10列（完全填满）
     * - 方块类型：12种不同的图案
     * - 时间限制：600秒（10分钟）
     * - 特点：面板紧凑且图案种类多，挑战性更高
     */
    HARD("困难模式", 10, 10, 12, 600);
    
    /** 难度名称的中文描述 */
    private String name;
    
    /** 游戏面板的行数 */
    private int rows;
    
    /** 游戏面板的列数 */
    private int cols;
    
    /** 方块类型数量（即有多少种不同的图案） */
    private int tileTypes;
    
    /** 游戏时间限制（以秒为单位） */
    private int timeLimit;
    
    /**
     * 难度构造函数
     * 
     * @param name 难度名称
     * @param rows 面板行数
     * @param cols 面板列数
     * @param tileTypes 方块类型数量
     * @param timeLimit 时间限制（秒）
     */
    Difficulty(String name, int rows, int cols, int tileTypes, int timeLimit) {
        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.tileTypes = tileTypes;
        this.timeLimit = timeLimit;
    }
    
    /**
     * 获取难度名称
     * 
     * @return 难度的中文描述
     */
    public String getName() {
        return name;
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
     * 获取面板列数
     * 
     * @return 游戏面板的列数
     */
    public int getCols() {
        return cols;
    }
    
    /**
     * 获取方块类型数量
     * 
     * @return 游戏中不同图案的数量
     */
    public int getTileTypes() {
        return tileTypes;
    }
    
    /**
     * 获取时间限制
     * 
     * @return 游戏时间限制（秒）
     */
    public int getTimeLimit() {
        return timeLimit;
    }
}