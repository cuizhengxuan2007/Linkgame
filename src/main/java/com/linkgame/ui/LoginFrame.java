package com.linkgame.ui;

import com.linkgame.service.UserService;
import com.linkgame.service.GameService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    
    public LoginFrame() {
        setTitle("连连看游戏 - 登录");
        setSize(550, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        JLabel titleLabel = new JLabel("连连看游戏");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setForeground(new Color(66, 133, 244));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(usernameLabel, gbc);
        
        usernameField = new JTextField(20);
        usernameField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);
        
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);
        
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);
        
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        statusLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(statusLabel, gbc);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton loginButton = new JButton("登录");
        loginButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        loginButton.setBackground(new Color(66, 133, 244));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(80, 30));
        loginButton.addActionListener(new LoginListener());
        
        JButton registerButton = new JButton("注册");
        registerButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        registerButton.setBackground(new Color(76, 175, 80));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(80, 30));
        registerButton.addActionListener(new RegisterListener());
        
        JButton guestButton = new JButton("游客模式");
        guestButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        guestButton.setBackground(new Color(255, 152, 0));
        guestButton.setForeground(Color.WHITE);
        guestButton.setPreferredSize(new Dimension(100, 30));
        guestButton.addActionListener(new GuestListener());
        
        JButton leaderboardButton = new JButton("排行榜");
        leaderboardButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        leaderboardButton.setBackground(new Color(128, 0, 128));
        leaderboardButton.setForeground(Color.WHITE);
        leaderboardButton.setPreferredSize(new Dimension(100, 30));
        leaderboardButton.addActionListener(new LeaderboardListener());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(guestButton);
        buttonPanel.add(leaderboardButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        add(panel);
    }
    
    private class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("请输入用户名和密码");
                return;
            }
            
            if (UserService.login(username, password)) {
                dispose();
                if (GameService.hasSaveFile(username)) {
                    int choice = JOptionPane.showConfirmDialog(null, 
                        "检测到存档，是否继续游戏？", "存档检测", 
                        JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        new GameFrame(username, false, true);
                        return;
                    }
                }
                new DifficultyFrame(username, false);
            } else {
                statusLabel.setText("用户名或密码错误");
            }
        }
    }
    
    private class RegisterListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("请输入用户名和密码");
                return;
            }
            
            if (UserService.register(username, password)) {
                statusLabel.setText("注册成功，请登录");
            } else {
                statusLabel.setText("用户名已存在");
            }
        }
    }
    
    private class GuestListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new DifficultyFrame("游客", true);
        }
    }
    
    private class LeaderboardListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new LeaderboardFrame();
        }
    }
}
