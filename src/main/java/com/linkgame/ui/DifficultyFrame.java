package com.linkgame.ui;

import com.linkgame.entity.Difficulty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DifficultyFrame extends JFrame {
    private String username;
    private boolean isGuest;
    
    public DifficultyFrame(String username, boolean isGuest) {
        this.username = username;
        this.isGuest = isGuest;
        
        setTitle("选择难度");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel titleLabel = new JLabel("选择游戏难度", JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        
        JButton easyButton = new JButton("简单模式");
        easyButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        easyButton.setBackground(new Color(144, 238, 144));
        easyButton.setForeground(Color.BLACK);
        easyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame(Difficulty.EASY);
            }
        });
        
        JButton hardButton = new JButton("困难模式");
        hardButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        hardButton.setBackground(new Color(255, 160, 122));
        hardButton.setForeground(Color.BLACK);
        hardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame(Difficulty.HARD);
            }
        });
        
        buttonPanel.add(easyButton);
        buttonPanel.add(hardButton);
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        add(panel);
        setVisible(true);
    }
    
    private void startGame(Difficulty difficulty) {
        dispose();
        new GameFrame(username, isGuest, difficulty);
    }
}
