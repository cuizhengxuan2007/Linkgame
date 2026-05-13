package com.linkgame.ui;

import com.linkgame.entity.Difficulty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 难度选择界面窗口
 * 
 * 在游戏开始前让用户选择难度等级
 * 提供简单模式和困难模式两个选项
 * 
 * @author LinkGame Team
 * @version 1.0
 * @see Difficulty
 * @see GameFrame
 */
public class DifficultyFrame extends JFrame {
    /** 当前用户名 */
    private String username;
    
    /** 是否为游客模式 */
    private boolean isGuest;
    
    /**
     * 构造函数，初始化难度选择窗口
     * 
     * @param username 用户名
     * @param isGuest 是否为游客模式
     */
    public DifficultyFrame(String username, boolean isGuest) {
        this.username = username;
        this.isGuest = isGuest;
        
        setTitle("选择难度");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // 窗口居中
        
        // 主面板，使用BorderLayout布局
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // 标题标签
        JLabel titleLabel = new JLabel("选择游戏难度", JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // 按钮面板，使用GridLayout布局
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        
        // 简单模式按钮
        JButton easyButton = new JButton("简单模式");
        easyButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        easyButton.setBackground(new Color(144, 238, 144));
        easyButton.setForeground(Color.BLACK);
        easyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame(Difficulty.EASY);
            }
        });
        
        // 困难模式按钮
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
    
    /**
     * 启动游戏
     * 
     * 关闭当前窗口并打开游戏窗口
     * 
     * @param difficulty 选择的难度等级
     */
    private void startGame(Difficulty difficulty) {
        dispose();
        new GameFrame(username, isGuest, difficulty);
    }
}
