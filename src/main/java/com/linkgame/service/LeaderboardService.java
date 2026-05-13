package com.linkgame.service;

import com.linkgame.entity.Difficulty;
import com.linkgame.entity.ScoreRecord;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardService {
    private static final String SCORE_FILE = "data/scores.dat";
    
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
    
    public static List<ScoreRecord> getTopScores(int limit) {
        List<ScoreRecord> records = loadScores();
        Collections.sort(records);
        
        if (records.size() > limit) {
            return records.subList(0, limit);
        }
        return records;
    }
    
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