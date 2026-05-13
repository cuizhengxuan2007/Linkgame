package com.linkgame.service;

import com.linkgame.entity.Difficulty;
import com.linkgame.entity.ScoreRecord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 排行榜服务类，负责管理游戏分数的保存、加载和查询
 * 
 * 提供以下功能：
 * 1. 保存玩家游戏成绩到文件
 * 2. 从文件加载所有成绩记录
 * 3. 获取前N名的高分记录
 * 4. 按难度筛选高分记录
 * 
 * 所有成绩记录按分数降序排列，最多保留100条记录
 * 数据持久化存储在 data/scores.dat 文件中
 * 
 * @author Linkgame Team
 * @version 1.0
 */
public class LeaderboardService {
    /** 分数数据文件的存储路径 */
    private static final String SCORE_FILE = "data/scores.dat";
    
    /**
     * 保存玩家的游戏成绩
     * 
     * 执行流程：
     * 1. 确保数据目录存在
     * 2. 加载现有的成绩记录
     * 3. 创建新的成绩记录（包含当前时间戳）
     * 4. 将新记录添加到列表并排序
     * 5. 只保留前100条记录
     * 6. 序列化保存到文件
     * 
     * @param username 玩家用户名
     * @param score 游戏得分
     * @param difficulty 游戏难度
     * @param time 游戏用时（秒）
     */
    public static void saveScore(String username, int score, Difficulty difficulty, long time) {
        File file = new File(SCORE_FILE);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
            System.out.println("Created directory: " + dir.getAbsolutePath());
        }
        List<ScoreRecord> records = loadScores();
        
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ScoreRecord record = new ScoreRecord(username, score, difficulty, time, date);
        records.add(record);
        System.out.println("Adding score: " + username + ", " + score + ", " + difficulty + ", " + time);
        
        Collections.sort(records);
        
        if (records.size() > 100) {
            records = records.subList(0, 100);
        }
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(records);
            System.out.println("Saved " + records.size() + " records to " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error saving scores: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 从文件中加载所有成绩记录
     * 
     * 如果文件不存在或读取失败，返回空列表
     * 
     * @return 成绩记录列表
     */
    public static List<ScoreRecord> loadScores() {
        File file = new File(SCORE_FILE);
        System.out.println("Score file path: " + file.getAbsolutePath());
        System.out.println("File exists: " + file.exists());
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<ScoreRecord> records = (List<ScoreRecord>) ois.readObject();
            System.out.println("Loaded " + records.size() + " records");
            return records;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading scores: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取前N名的高分记录（所有难度）
     * 
     * @param limit 要返回的记录数量上限
     * @return 前N名的成绩记录列表
     */
    public static List<ScoreRecord> getTopScores(int limit) {
        List<ScoreRecord> records = loadScores();
        Collections.sort(records);
        
        if (records.size() > limit) {
            return records.subList(0, limit);
        }
        return records;
    }
    
    /**
     * 获取指定难度的前N名高分记录
     * 
     * @param difficulty 要筛选的难度
     * @param limit 要返回的记录数量上限
     * @return 指定难度的前N名成绩记录列表
     */
    public static List<ScoreRecord> getTopScoresByDifficulty(Difficulty difficulty, int limit) {
        List<ScoreRecord> records = loadScores();
        List<ScoreRecord> filtered = new ArrayList<>();
        
        for (ScoreRecord record : records) {
            if (record.getDifficulty() == difficulty) {
                filtered.add(record);
            }
        }
        
        Collections.sort(filtered);
        
        if (filtered.size() > limit) {
            return filtered.subList(0, limit);
        }
        return filtered;
    }
}