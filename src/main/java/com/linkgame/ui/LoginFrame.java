package com.linkgame.ui;

import com.linkgame.service.UserService;
import com.linkgame.service.GameService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 登录界面窗口
 * 
 * 提供用户登录、注册、游客模式和查看排行榜的功能
 * 是游戏的入口界面，包含用户名和密码输入框以及多个功能按钮
 * 
 * 主要功能：
 * - 用户登录验证
 * - 新用户注册
 * - 游客模式进入游戏
 * - 查看排行榜
 * - 自动检测存档并提示继续游戏
 * 
 * @author LinkGame Team
 * @version 1.0
 * @see UserService
 * @see GameService
 * @see DifficultyFrame
 * @see GameFrame
 * @see LeaderboardFrame
 */
public class LoginFrame extends JFrame {
    /** 用户名输入框 */
    private JTextField usernameField;
    
    /** 密码输入框 */
    private JPasswordField passwordField;
    
    /** 状态提示标签，用于显示错误信息 */
    private JLabel statusLabel;
    
    /**
     * 构造函数，初始化登录窗口
     * 
     * 设置窗口标题、大小、关闭操作等属性，并初始化界面组件
     */
    public LoginFrame() {
        setTitle("连连看游戏 - 登录");
        setSize(550, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // 窗口居中显示
        setResizable(false);  // 禁止调整窗口大小
        
        initComponents();
    }
    
    /**
     * 初始化界面组件
     * 
     * 创建并布局所有UI元素，包括：
     * - 标题标签
     * - 用户名和密码输入框
     * - 状态提示标签
     * - 登录、注册、游客模式、排行榜按钮
     */
    private void initComponents() {
        // 主面板，使用GridBagLayout布局
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // 标题标签
        JLabel titleLabel = new JLabel("连连看游戏");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setForeground(new Color(66, 133, 244));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // 用户名标签
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(usernameLabel, gbc);
        
        // 用户名输入框
        usernameField = new JTextField(20);
        usernameField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);
        
        // 密码标签
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);
        
        // 密码输入框
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);
        
        // 状态提示标签
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        statusLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(statusLabel, gbc);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        // 登录按钮
        JButton loginButton = new JButton("登录");
        loginButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        loginButton.setBackground(new Color(66, 133, 244));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(80, 30));
        loginButton.addActionListener(new LoginListener());
        
        // 注册按钮
        JButton registerButton = new JButton("注册");
        registerButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        registerButton.setBackground(new Color(76, 175, 80));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(80, 30));
        registerButton.addActionListener(new RegisterListener());
        
        // 游客模式按钮
        JButton guestButton = new JButton("游客模式");
        guestButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        guestButton.setBackground(new Color(255, 152, 0));
        guestButton.setForeground(Color.WHITE);
        guestButton.setPreferredSize(new Dimension(100, 30));
        guestButton.addActionListener(new GuestListener());
        
        // 排行榜按钮
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
    
    /**
     * 登录按钮的事件监听器
     * 
     * 验证用户输入的用户名和密码，登录成功后：
     * - 检测是否有存档，如有则询问是否继续游戏
     * - 选择继续则加载存档，否则进入难度选择界面
     */
    private class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            // 检查输入是否为空
            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("请输入用户名和密码");
                return;
            }
            
            // 验证用户名和密码
            if (UserService.login(username, password)) {
                dispose();  // 关闭登录窗口
                
                // 检测是否有存档
                if (GameService.hasSaveFile(username)) {
                    int choice = JOptionPane.showConfirmDialog(null, 
                        "检测到存档，是否继续游戏？", "存档检测", 
                        JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        // 加载存档继续游戏
                        new GameFrame(username, false, true);
                        return;
                    }
                }
                
                // 进入难度选择界面
                new DifficultyFrame(username, false);
            } else {
                statusLabel.setText("用户名或密码错误");
            }
        }
    }
    
    /**
     * 注册按钮的事件监听器
     * 
     * 使用输入的用户名和密码进行注册，注册成功后提示用户登录
     */
    private class RegisterListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            // 检查输入是否为空
            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("请输入用户名和密码");
                return;
            }
            
            // 执行注册
            if (UserService.register(username, password)) {
                statusLabel.setText("注册成功，请登录");
            } else {
                statusLabel.setText("用户名已存在");
            }
        }
    }
    
    /**
     * 游客模式按钮的事件监听器
     * 
     * 以游客身份直接进入难度选择界面，无法保存游戏进度
     */
    private class GuestListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new DifficultyFrame("游客", true);
        }
    }
    
    /**
     * 排行榜按钮的事件监听器
     * 
     * 打开排行榜窗口查看所有玩家的分数记录
     */
    private class LeaderboardListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new LeaderboardFrame();
        }
    }
}
