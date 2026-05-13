package com.linkgame.ui;

import com.linkgame.entity.Difficulty;
import com.linkgame.entity.GameState;
import com.linkgame.service.GameService;
import com.linkgame.service.LeaderboardService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 游戏主界面窗口
 * 
 * 连连看游戏的核心界面，提供完整的游戏功能：
 * - 显示游戏面板和方块
 * - 处理方块选择和消除逻辑
 * - 实时显示游戏状态（分数、时间、连消等）
 * - 支持游戏保存和加载
 * - 游戏结束判定和分数记录
 * 
 * 游戏玩法：
 * 1. 点击第一个方块选中
 * 2. 点击第二个相同类型的方块
 * 3. 如果两个方块可以通过不超过两个拐角的路径连接，则消除
 * 4. 消除所有方块即可获胜
 * 
 * @author LinkGame Team
 * @version 1.0
 * @see Difficulty
 * @see GameState
 * @see GameService
 * @see LeaderboardService
 */
public class GameFrame extends JFrame {
    /** 当前用户名 */
    private String username;
    
    /** 是否为游客模式（游客无法保存游戏） */
    private boolean isGuest;
    
    /** 当前游戏难度 */
    private Difficulty difficulty;
    
    /** 游戏面板的二维数组表示 */
    private int[][] board;
    
    /** 当前选中的方块行号，-1表示未选中 */
    private int selectedRow = -1;
    
    /** 当前选中的方块列号，-1表示未选中 */
    private int selectedCol = -1;
    
    /** 连接路径的点列表，用于绘制连接线 */
    private List<Point> connectionPath;
    
    /** 当前游戏分数 */
    private int score = 0;
    
    /** 当前连消次数 */
    private int combo = 0;
    
    /** 游戏已用时间（秒） */
    private long time = 0;
    
    /** 游戏剩余时间（秒） */
    private long remainingTime = 0;
    
    /** 游戏计时器 */
    private Timer timer;
    
    /** 用户名标签 */
    private JLabel userLabel;
    
    /** 分数标签 */
    private JLabel scoreLabel;
    
    /** 已用时间标签 */
    private JLabel timeLabel;
    
    /** 剩余时间标签 */
    private JLabel remainingLabel;
    
    /** 连消标签 */
    private JLabel comboLabel;
    
    /** 游戏状态标签 */
    private JLabel statusLabel;
    
    /** 游戏面板 */
    private JPanel gamePanel;
    
    /** 方块大小（像素） */
    private static final int TILE_SIZE = 45;
    
    /** 方块颜色数组 */
    private static final Color[] COLORS = {
        Color.WHITE, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, 
        Color.ORANGE, Color.PINK, new Color(128, 0, 128), Color.CYAN, Color.MAGENTA,
        Color.LIGHT_GRAY, Color.DARK_GRAY, new Color(165, 42, 42)
    };
    
    /**
     * 构造函数，创建新游戏
     * 
     * @param username 当前用户名
     * @param isGuest 是否为游客模式
     * @param difficulty 游戏难度
     */
    public GameFrame(String username, boolean isGuest, Difficulty difficulty) {
        this.username = username;
        this.isGuest = isGuest;
        this.difficulty = difficulty;
        
        initNewGame();
        initUI();
        startTimer();
    }
    
    /**
     * 构造函数，加载保存的游戏或创建新游戏
     * 
     * @param username 当前用户名
     * @param isGuest 是否为游客模式
     * @param loadSave 是否加载保存的游戏
     */
    public GameFrame(String username, boolean isGuest, boolean loadSave) {
        this.username = username;
        this.isGuest = isGuest;
        
        if (loadSave) {
            loadGame();
        } else {
            difficulty = Difficulty.EASY;
            initNewGame();
        }
        
        initUI();
        startTimer();
    }
    
    /**
     * 初始化新游戏
     */
    private void initNewGame() {
        board = GameService.createBoard(difficulty);
        score = 0;
        combo = 0;
        time = 0;
        remainingTime = difficulty.getTimeLimit();
        selectedRow = -1;
        selectedCol = -1;
        connectionPath = null;
    }
    
    /**
     * 初始化用户界面
     */
    private void initUI() {
        setTitle("连连看游戏");
        
        int rows = board.length;
        int cols = board[0].length;
        int width = cols * TILE_SIZE + 50;
        int height = rows * TILE_SIZE + 120;
        setSize(Math.max(width, 500), Math.max(height, 400));
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        userLabel = new JLabel("用户: " + username);
        userLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        
        scoreLabel = new JLabel("分数: " + score);
        scoreLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        
        timeLabel = new JLabel("时间: " + formatTime(time));
        timeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        
        remainingLabel = new JLabel("剩余: " + formatTime(remainingTime));
        remainingLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        
        comboLabel = new JLabel("连消: x0");
        comboLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        
        statusLabel = new JLabel("选择第一个图案");
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        statusLabel.setForeground(Color.BLUE);
        
        topPanel.add(userLabel);
        topPanel.add(scoreLabel);
        topPanel.add(timeLabel);
        topPanel.add(remainingLabel);
        topPanel.add(comboLabel);
        topPanel.add(statusLabel);
        
        add(topPanel, BorderLayout.NORTH);
        
        gamePanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g);
            }
        };
        gamePanel.setPreferredSize(new Dimension(cols * TILE_SIZE, rows * TILE_SIZE));
        gamePanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / TILE_SIZE;
                int row = e.getY() / TILE_SIZE;
                
                if (row >= 0 && row < rows && col >= 0 && col < cols) {
                    handleClick(row, col);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(gamePanel);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton restartButton = new JButton("重新开始");
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timer.cancel();
                initNewGame();
                gamePanel.repaint();
                startTimer();
            }
        });
        
        JButton saveButton = new JButton("保存游戏");
        saveButton.setEnabled(!isGuest);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        });
        
        JButton logoutButton = new JButton("退出登录");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timer.cancel();
                dispose();
                new LoginFrame();
            }
        });
        
        bottomPanel.add(restartButton);
        bottomPanel.add(saveButton);
        bottomPanel.add(logoutButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    /**
     * 启动游戏计时器
     */
    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                time++;
                remainingTime--;
                
                timeLabel.setText("时间: " + formatTime(time));
                remainingLabel.setText("剩余: " + formatTime(remainingTime));
                
                if (remainingTime <= 0) {
                    timer.cancel();
                    gameOver(false);
                }
                
                updateProgress();
            }
        }, 1000, 1000);
    }
    
    /**
     * 格式化时间字符串
     * 
     * @param seconds 秒数
     * @return 格式化后的时间字符串（MM:SS）
     */
    private String formatTime(long seconds) {
        long mins = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }
    
    /**
     * 更新游戏进度
     */
    private void updateProgress() {
        int remainingPairs = GameService.getRemainingPairs(board);
        int totalPairs;
        if (difficulty == Difficulty.EASY) {
            totalPairs = 16;
        } else {
            totalPairs = 50;
        }
        int progress = (int) ((1 - (double) remainingPairs / totalPairs) * 100);
        statusLabel.setText("进度: " + progress + "% | 剩余配对: " + remainingPairs);
    }
    
    /**
     * 处理方块点击事件
     * 
     * @param row 方块行号
     * @param col 方块列号
     */
    private void handleClick(int row, int col) {
        if (board[row][col] == 0) {
            return;
        }
        
        if (selectedRow == -1) {
            selectedRow = row;
            selectedCol = col;
            statusLabel.setText("选择第二个图案");
            gamePanel.repaint();
        } else if (selectedRow == row && selectedCol == col) {
            selectedRow = -1;
            selectedCol = -1;
            statusLabel.setText("选择第一个图案");
            gamePanel.repaint();
        } else {
            if (board[selectedRow][selectedCol] == board[row][col]) {
                connectionPath = GameService.getConnectionPath(board, selectedRow, selectedCol, row, col);
                if (connectionPath != null) {
                    gamePanel.repaint();
                    
                    int finalSelectedRow = selectedRow;
                    int finalSelectedCol = selectedCol;
                    int finalRow = row;
                    int finalCol = col;
                    char symbol = (char) ('A' + board[row][col] - 1);
                    
                    javax.swing.Timer delayTimer = new javax.swing.Timer(300, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            board[finalSelectedRow][finalSelectedCol] = 0;
                            board[finalRow][finalCol] = 0;
                            
                            combo++;
                            int baseScore = 10;
                            int bonusScore = 0;
                            
                            if (combo >= 3) {
                                bonusScore = (combo - 2) * 5;
                            }
                            
                            score += baseScore + bonusScore;
                            scoreLabel.setText("分数: " + score);
                            comboLabel.setText("连消: x" + combo);
                            
                            if (bonusScore > 0) {
                                statusLabel.setText(String.format("消除了[%c]x2, +%d分 (连消x%d)", symbol, baseScore + bonusScore, combo));
                            } else {
                                statusLabel.setText(String.format("消除了[%c]x2, +%d分", symbol, baseScore));
                            }
                            
                            connectionPath = null;
                            gamePanel.repaint();
                            
                            if (GameService.isGameComplete(board)) {
                                timer.cancel();
                                gameOver(true);
                            } else if (!GameService.hasValidMoves(board)) {
                                timer.cancel();
                                gameOver(false);
                            }
                            
                            updateProgress();
                        }
                    });
                    delayTimer.setRepeats(false);
                    delayTimer.start();
                } else {
                    statusLabel.setText("无法连接，请重新选择");
                    combo = 0;
                    comboLabel.setText("连消: x0");
                }
            } else {
                statusLabel.setText("图案不同，重新选择");
                combo = 0;
                comboLabel.setText("连消: x0");
                selectedRow = row;
                selectedCol = col;
                return;
            }
            
            selectedRow = -1;
            selectedCol = -1;
        }
    }
    
    /**
     * 绘制游戏面板
     * 
     * @param g Graphics对象
     */
    private void drawBoard(Graphics g) {
        int rows = board.length;
        int cols = board[0].length;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = j * TILE_SIZE;
                int y = i * TILE_SIZE;
                
                if (board[i][j] == 0) {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
                } else {
                    g.setColor(COLORS[board[i][j]]);
                    g.fillRoundRect(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4, 8, 8);
                    g.setColor(Color.BLACK);
                    g.drawRoundRect(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4, 8, 8);
                    
                    g.setColor(Color.BLACK);
                    g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
                    String symbol = String.valueOf((char) ('A' + board[i][j] - 1));
                    int textX = x + (TILE_SIZE - g.getFontMetrics().stringWidth(symbol)) / 2;
                    int textY = y + (TILE_SIZE + g.getFontMetrics().getHeight()) / 2 - 5;
                    g.drawString(symbol, textX, textY);
                }
                
                if (i == selectedRow && j == selectedCol) {
                    g.setColor(Color.RED);
                    g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
                    g.drawRect(x + 1, y + 1, TILE_SIZE - 2, TILE_SIZE - 2);
                }
            }
        }
        
        drawConnectionLine(g);
    }
    
    /**
     * 绘制连接线
     * 
     * @param g Graphics对象
     */
    private void drawConnectionLine(Graphics g) {
        if (connectionPath != null && connectionPath.size() >= 2) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3));
            
            for (int i = 0; i < connectionPath.size() - 1; i++) {
                Point p1 = connectionPath.get(i);
                Point p2 = connectionPath.get(i + 1);
                
                int x1 = p1.y * TILE_SIZE + TILE_SIZE / 2;
                int y1 = p1.x * TILE_SIZE + TILE_SIZE / 2;
                int x2 = p2.y * TILE_SIZE + TILE_SIZE / 2;
                int y2 = p2.x * TILE_SIZE + TILE_SIZE / 2;
                
                g2d.drawLine(x1, y1, x2, y2);
            }
            
            g2d.setStroke(new BasicStroke(1));
        }
    }
    
    /**
     * 游戏结束处理
     * 
     * @param won 是否获胜
     */
    private void gameOver(boolean won) {
        String message;
        if (won) {
            message = String.format("恭喜通关！\n难度: %s\n得分: %d\n用时: %s\n连消最高: x%d",
                difficulty.getName(), score, formatTime(time), combo);
        } else {
            if (remainingTime <= 0) {
                message = String.format("时间耗尽！\n难度: %s\n得分: %d\n用时: %s",
                    difficulty.getName(), score, formatTime(time));
            } else {
                message = String.format("游戏结束！\n棋盘已无可消除配对\n难度: %s\n得分: %d\n用时: %s",
                    difficulty.getName(), score, formatTime(time));
            }
        }
        
        JOptionPane.showMessageDialog(this, message, "游戏结束", JOptionPane.INFORMATION_MESSAGE);
        
        if (!isGuest) {
            saveGame();
        }
        
        LeaderboardService.saveScore(username, score, difficulty, time);
        
        int choice = JOptionPane.showConfirmDialog(this, "是否重新开始游戏？", "重新开始", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            timer.cancel();
            initNewGame();
            gamePanel.repaint();
            startTimer();
        } else {
            dispose();
            new LoginFrame();
        }
    }
    
    /**
     * 保存游戏状态
     */
    private void saveGame() {
        GameState state = new GameState();
        state.setUsername(username);
        state.setBoard(board);
        state.setScore(score);
        state.setTime(time);
        state.setRemainingTime(remainingTime);
        state.setDifficulty(difficulty);
        state.setRows(board.length);
        state.setCols(board[0].length);
        
        GameService.saveGame(state);
        JOptionPane.showMessageDialog(this, "游戏已保存", "保存成功", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 加载游戏状态
     */
    private void loadGame() {
        GameState state = GameService.loadGame(username);
        if (state != null) {
            board = state.getBoard();
            score = state.getScore();
            time = state.getTime();
            remainingTime = state.getRemainingTime();
            difficulty = state.getDifficulty();
            combo = 0;
            selectedRow = -1;
            selectedCol = -1;
            connectionPath = null;
        } else {
            difficulty = Difficulty.EASY;
            initNewGame();
        }
    }
}
