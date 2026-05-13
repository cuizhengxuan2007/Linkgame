package com.linkgame.ui;

import com.linkgame.entity.Difficulty;
import com.linkgame.entity.ScoreRecord;
import com.linkgame.service.LeaderboardService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LeaderboardFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private String[] columnNames = {"排名", "玩家", "分数", "难度", "用时", "日期"};
    
    public LeaderboardFrame() {
        setTitle("排行榜");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("游戏排行榜", JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton allButton = new JButton("全部");
        allButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        allButton.addActionListener(e -> loadAllScores());
        
        JButton easyButton = new JButton("简单模式");
        easyButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        easyButton.addActionListener(e -> loadScoresByDifficulty(Difficulty.EASY));
        
        JButton hardButton = new JButton("困难模式");
        hardButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        hardButton.addActionListener(e -> loadScoresByDifficulty(Difficulty.HARD));
        
        buttonPanel.add(allButton);
        buttonPanel.add(easyButton);
        buttonPanel.add(hardButton);
        panel.add(buttonPanel, BorderLayout.NORTH);
        
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        add(panel);
        loadAllScores();
        setVisible(true);
    }
    
    private void loadAllScores() {
        List<ScoreRecord> records = LeaderboardService.getTopScores(20);
        updateTable(records);
    }
    
    private void loadScoresByDifficulty(Difficulty difficulty) {
        List<ScoreRecord> records = LeaderboardService.getTopScoresByDifficulty(difficulty, 20);
        updateTable(records);
    }
    
    private void updateTable(List<ScoreRecord> records) {
        System.out.println("Updating table with " + records.size() + " records");
        tableModel.setRowCount(0);
        
        int rank = 1;
        for (ScoreRecord record : records) {
            String timeStr = formatTime(record.getTime());
            String difficultyStr = record.getDifficulty() == Difficulty.EASY ? "简单" : "困难";
            System.out.println("Record " + rank + ": " + record.getUsername() + ", " + record.getScore());
            
            tableModel.addRow(new Object[]{
                rank++,
                record.getUsername(),
                record.getScore(),
                difficultyStr,
                timeStr,
                record.getDate()
            });
        }
        
        if (records.isEmpty()) {
            System.out.println("No records found");
            tableModel.addRow(new Object[]{"-", "暂无记录", "-", "-", "-", "-"});
        }
    }
    
    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
}