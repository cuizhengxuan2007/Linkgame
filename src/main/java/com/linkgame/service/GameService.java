package com.linkgame.service;

import com.linkgame.entity.Difficulty;
import com.linkgame.entity.GameState;
import com.linkgame.util.FileUtil;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 游戏服务类
 * 
 * 提供连连看游戏的核心业务逻辑，包括：
 * - 游戏面板的创建和初始化
 * - 方块连接检测算法（支持0个、1个、2个拐角的连接）
 * - 游戏状态判断（完成、有效移动等）
 * - 游戏存档和读档功能
 * 
 * 该类采用静态方法设计，无需实例化即可使用
 * 
 * @author LinkGame Team
 * @version 1.0
 * @see Difficulty
 * @see GameState
 * @see FileUtil
 */
public class GameService {
    
    /**
     * 根据难度创建游戏面板
     * 
     * @param difficulty 游戏难度枚举值
     * @return 初始化完成的二维数组表示的游戏面板
     */
    public static int[][] createBoard(Difficulty difficulty) {
        if (difficulty == Difficulty.EASY) {
            return createEasyBoard();
        } else {
            return createHardBoard();
        }
    }
    
    /**
     * 创建简单模式的游戏面板
     * 
     * 面板特点：
     * - 总大小：8行 x 12列
     * - 实际使用区域：两个4x4的方块区域
     *   * 第一个区域：行1-4，列1-4
     *   * 第二个区域：行3-6，列7-10
     * - 方块类型：5种（数字1-5，显示为A-E）
     * - 总共32个方块（16对）
     * 
     * @return 简单模式的游戏面板二维数组
     */
    private static int[][] createEasyBoard() {
        // 计算总方块数和配对数
        int totalTiles = 4 * 4 * 2;  // 32个方块
        int pairs = totalTiles / 2;   // 16对
        
        // 创建方块数组，每对方块使用相同的数字
        int[] tiles = new int[totalTiles];
        for (int i = 0; i < pairs; i++) {
            tiles[i * 2] = i % 5 + 1;      // 使用1-5循环
            tiles[i * 2 + 1] = i % 5 + 1;
        }
        
        // 使用Fisher-Yates洗牌算法随机打乱方块
        Random random = new Random();
        for (int i = tiles.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = tiles[i];
            tiles[i] = tiles[j];
            tiles[j] = temp;
        }
        
        // 初始化8x12的面板（周围留空便于边界连接）
        int[][] board = new int[8][12];
        int index = 0;
        
        // 填充第一个4x4区域（行1-4，列1-4）
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                board[i][j] = tiles[index++];
            }
        }
        
        // 填充第二个4x4区域（行3-6，列7-10）
        for (int i = 3; i <= 6; i++) {
            for (int j = 7; j <= 10; j++) {
                board[i][j] = tiles[index++];
            }
        }
        
        return board;
    }
    
    /**
     * 创建困难模式的游戏面板
     * 
     * 面板特点：
     * - 总大小：10行 x 10列
     * - 完全填满，无空白区域
     * - 方块类型：12种（数字1-12，显示为A-L）
     * - 总共100个方块（50对）
     * 
     * @return 困难模式的游戏面板二维数组
     */
    private static int[][] createHardBoard() {
        int rows = 10;
        int cols = 10;
        int totalTiles = rows * cols;  // 100个方块
        int pairs = totalTiles / 2;     // 50对
        
        // 创建方块数组，每对方块使用相同的数字
        int[] tiles = new int[totalTiles];
        for (int i = 0; i < pairs; i++) {
            tiles[i * 2] = i % 12 + 1;     // 使用1-12循环
            tiles[i * 2 + 1] = i % 12 + 1;
        }
        
        // 使用Fisher-Yates洗牌算法随机打乱方块
        Random random = new Random();
        for (int i = tiles.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = tiles[i];
            tiles[i] = tiles[j];
            tiles[j] = temp;
        }
        
        // 填充10x10的面板
        int[][] board = new int[rows][cols];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = tiles[index++];
            }
        }
        
        return board;
    }
    
    /**
     * 检查两个位置的方块是否可以连接
     * 
     * @param board 游戏面板
     * @param r1 第一个方块的行号
     * @param c1 第一个方块的列号
     * @param r2 第二个方块的行号
     * @param c2 第二个方块的列号
     * @return 如果可以连接返回true，否则返回false
     */
    public static boolean canConnect(int[][] board, int r1, int c1, int r2, int c2) {
        return getConnectionPath(board, r1, c1, r2, c2) != null;
    }
    
    /**
     * 获取两个方块之间的连接路径
     * 
     * 连连看的核心算法，支持三种连接方式：
     * 1. 直线连接（0个拐角）
     * 2. 一个拐角连接
     * 3. 两个拐角连接（包括经过边界的连接）
     * 
     * 算法使用扩展面板技术，在原始面板周围添加一圈空白区域，
     * 使得边界连接可以通过内部路径来实现。
     * 
     * @param board 游戏面板
     * @param r1 第一个方块的行号
     * @param c1 第一个方块的列号
     * @param r2 第二个方块的行号
     * @param c2 第二个方块的列号
     * @return 连接路径的点列表，如果无法连接则返回null
     */
    public static List<Point> getConnectionPath(int[][] board, int r1, int c1, int r2, int c2) {
        // 检查是否为同一个位置
        if (r1 == r2 && c1 == c2) {
            return null;
        }
        
        // 检查两个方块的类型是否相同且不为空
        if (board[r1][c1] != board[r2][c2] || board[r1][c1] == 0) {
            return null;
        }
        
        int rows = board.length;
        int cols = board[0].length;
        
        // 创建扩展面板（四周各加一行/列空白区域）
        int[][] extendedBoard = new int[rows + 2][cols + 2];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(board[i], 0, extendedBoard[i + 1], 1, cols);
        }
        
        // 转换坐标到扩展面板
        int er1 = r1 + 1, ec1 = c1 + 1, er2 = r2 + 1, ec2 = c2 + 1;
        
        // 尝试直线连接
        List<Point> path = directConnect(extendedBoard, er1, ec1, er2, ec2);
        if (path != null) {
            return adjustPath(path);
        }
        
        // 尝试一个拐角连接
        path = oneCornerConnect(extendedBoard, er1, ec1, er2, ec2);
        if (path != null) {
            return adjustPath(path);
        }
        
        // 尝试两个拐角连接
        path = twoCornerConnect(extendedBoard, er1, ec1, er2, ec2);
        if (path != null) {
            return adjustPath(path);
        }
        
        return null;
    }
    
    /**
     * 调整路径坐标，从扩展面板坐标转换回原始面板坐标
     * 
     * @param path 扩展面板中的路径
     * @return 调整后的原始面板路径
     */
    private static List<Point> adjustPath(List<Point> path) {
        List<Point> adjusted = new ArrayList<>();
        for (Point p : path) {
            adjusted.add(new Point(p.x - 1, p.y - 1));
        }
        return adjusted;
    }
    
    /**
     * 检查直线连接（0个拐角）
     * 
     * 检查两个点是否在同一行或同一列，并且中间没有其他方块阻挡
     * 
     * @param board 扩展面板
     * @param r1 起点行号
     * @param c1 起点列号
     * @param r2 终点行号
     * @param c2 终点列号
     * @return 如果可以直线连接，返回路径点列表；否则返回null
     */
    private static List<Point> directConnect(int[][] board, int r1, int c1, int r2, int c2) {
        // 检查是否在同一行
        if (r1 == r2) {
            int minC = Math.min(c1, c2);
            int maxC = Math.max(c1, c2);
            // 检查中间是否有阻挡
            for (int c = minC + 1; c < maxC; c++) {
                if (board[r1][c] != 0) {
                    return null;
                }
            }
            List<Point> path = new ArrayList<>();
            path.add(new Point(r1, c1));
            path.add(new Point(r2, c2));
            return path;
        }
        
        // 检查是否在同一列
        if (c1 == c2) {
            int minR = Math.min(r1, r2);
            int maxR = Math.max(r1, r2);
            // 检查中间是否有阻挡
            for (int r = minR + 1; r < maxR; r++) {
                if (board[r][c1] != 0) {
                    return null;
                }
            }
            List<Point> path = new ArrayList<>();
            path.add(new Point(r1, c1));
            path.add(new Point(r2, c2));
            return path;
        }
        
        return null;
    }
    
    /**
     * 检查一个拐角连接
     * 
     * 通过一个中间点实现连接，有两种可能的拐角：
     * 1. (r1,c1) -> (r1,c2) -> (r2,c2)
     * 2. (r1,c1) -> (r2,c1) -> (r2,c2)
     * 
     * @param board 扩展面板
     * @param r1 起点行号
     * @param c1 起点列号
     * @param r2 终点行号
     * @param c2 终点列号
     * @return 如果可以一拐角连接，返回路径点列表；否则返回null
     */
    private static List<Point> oneCornerConnect(int[][] board, int r1, int c1, int r2, int c2) {
        // 尝试第一个拐角：(r1,c1) -> (r1,c2) -> (r2,c2)
        if (board[r1][c2] == 0) {
            List<Point> path1 = directConnect(board, r1, c1, r1, c2);
            List<Point> path2 = directConnect(board, r1, c2, r2, c2);
            if (path1 != null && path2 != null) {
                List<Point> path = new ArrayList<>();
                path.add(new Point(r1, c1));
                path.add(new Point(r1, c2));
                path.add(new Point(r2, c2));
                return path;
            }
        }
        
        // 尝试第二个拐角：(r1,c1) -> (r2,c1) -> (r2,c2)
        if (board[r2][c1] == 0) {
            List<Point> path1 = directConnect(board, r1, c1, r2, c1);
            List<Point> path2 = directConnect(board, r2, c1, r2, c2);
            if (path1 != null && path2 != null) {
                List<Point> path = new ArrayList<>();
                path.add(new Point(r1, c1));
                path.add(new Point(r2, c1));
                path.add(new Point(r2, c2));
                return path;
            }
        }
        
        return null;
    }
    
    /**
     * 检查两个拐角连接
     * 
     * 通过两个中间点实现连接，包括：
     * 1. 水平扫描：寻找可以垂直穿过的列
     * 2. 垂直扫描：寻找可以水平穿过的行
     * 3. 边界连接：通过面板边缘的连接
     * 
     * @param board 扩展面板
     * @param r1 起点行号
     * @param c1 起点列号
     * @param r2 终点行号
     * @param c2 终点列号
     * @return 如果可以两拐角连接，返回路径点列表；否则返回null
     */
    private static List<Point> twoCornerConnect(int[][] board, int r1, int c1, int r2, int c2) {
        int rows = board.length;
        int cols = board[0].length;
        
        // 水平扫描：寻找可以作为中转的列
        for (int c = 0; c < cols; c++) {
            if (c != c1 && c != c2) {
                // 检查该列在起点行和终点行是否可达
                boolean canPass1 = (c == 0 || c == cols - 1) || board[r1][c] == 0;
                boolean canPass2 = (c == 0 || c == cols - 1) || board[r2][c] == 0;
                
                if (canPass1 && canPass2) {
                    // 尝试路径：(r1,c1) -> (r1,c) -> (r2,c) -> (r2,c2)
                    if (directConnect(board, r1, c1, r1, c) != null &&
                        directConnect(board, r1, c, r2, c) != null &&
                        directConnect(board, r2, c, r2, c2) != null) {
                        List<Point> path = new ArrayList<>();
                        path.add(new Point(r1, c1));
                        path.add(new Point(r1, c));
                        path.add(new Point(r2, c));
                        path.add(new Point(r2, c2));
                        return path;
                    }
                }
            }
        }
        
        // 垂直扫描：寻找可以作为中转的行
        for (int r = 0; r < rows; r++) {
            if (r != r1 && r != r2) {
                // 检查该行在起点列和终点列是否可达
                boolean canPass1 = (r == 0 || r == rows - 1) || board[r][c1] == 0;
                boolean canPass2 = (r == 0 || r == rows - 1) || board[r][c2] == 0;
                
                if (canPass1 && canPass2) {
                    // 尝试路径：(r1,c1) -> (r,c1) -> (r,c2) -> (r2,c2)
                    if (directConnect(board, r1, c1, r, c1) != null &&
                        directConnect(board, r, c1, r, c2) != null &&
                        directConnect(board, r, c2, r2, c2) != null) {
                        List<Point> path = new ArrayList<>();
                        path.add(new Point(r1, c1));
                        path.add(new Point(r, c1));
                        path.add(new Point(r, c2));
                        path.add(new Point(r2, c2));
                        return path;
                    }
                }
            }
        }
        
        // 如果在同行或同列，不需要尝试边界连接
        if (r1 == r2 || c1 == c2) {
            return null;
        }
        
        // 尝试边界连接
        List<Point> borderPath = tryBorderConnection(board, r1, c1, r2, c2);
        if (borderPath != null) {
            return borderPath;
        }
        
        return null;
    }
    
    /**
     * 尝试通过边界的连接
     * 
     * 检查是否可以通过面板的四个边缘（上、下、左、右）实现连接
     * 
     * @param board 扩展面板
     * @param r1 起点行号
     * @param c1 起点列号
     * @param r2 终点行号
     * @param c2 终点列号
     * @return 如果可以边界连接，返回路径点列表；否则返回null
     */
    private static List<Point> tryBorderConnection(int[][] board, int r1, int c1, int r2, int c2) {
        int rows = board.length;
        int cols = board[0].length;
        
        // 尝试通过上边界
        if (directConnect(board, r1, c1, 0, c1) != null &&
            directConnect(board, 0, c1, 0, c2) != null &&
            directConnect(board, 0, c2, r2, c2) != null) {
            List<Point> path = new ArrayList<>();
            path.add(new Point(r1, c1));
            path.add(new Point(0, c1));
            path.add(new Point(0, c2));
            path.add(new Point(r2, c2));
            return path;
        }
        
        // 尝试通过下边界
        if (directConnect(board, r1, c1, rows - 1, c1) != null &&
            directConnect(board, rows - 1, c1, rows - 1, c2) != null &&
            directConnect(board, rows - 1, c2, r2, c2) != null) {
            List<Point> path = new ArrayList<>();
            path.add(new Point(r1, c1));
            path.add(new Point(rows - 1, c1));
            path.add(new Point(rows - 1, c2));
            path.add(new Point(r2, c2));
            return path;
        }
        
        // 尝试通过左边界
        if (directConnect(board, r1, c1, r1, 0) != null &&
            directConnect(board, r1, 0, r2, 0) != null &&
            directConnect(board, r2, 0, r2, c2) != null) {
            List<Point> path = new ArrayList<>();
            path.add(new Point(r1, c1));
            path.add(new Point(r1, 0));
            path.add(new Point(r2, 0));
            path.add(new Point(r2, c2));
            return path;
        }
        
        // 尝试通过右边界
        if (directConnect(board, r1, c1, r1, cols - 1) != null &&
            directConnect(board, r1, cols - 1, r2, cols - 1) != null &&
            directConnect(board, r2, cols - 1, r2, c2) != null) {
            List<Point> path = new ArrayList<>();
            path.add(new Point(r1, c1));
            path.add(new Point(r1, cols - 1));
            path.add(new Point(r2, cols - 1));
            path.add(new Point(r2, c2));
            return path;
        }
        
        return null;
    }
    
    /**
     * 检查游戏是否完成
     * 
     * 遍历整个面板，如果所有位置都为空（值为0），则游戏完成
     * 
     * @param board 游戏面板
     * @return 如果所有方块都被消除返回true，否则返回false
     */
    public static boolean isGameComplete(int[][] board) {
        for (int[] row : board) {
            for (int tile : row) {
                if (tile != 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * 获取剩余的方块对数
     * 
     * 统计面板中非空位置的数量，除以2得到剩余对数
     * 
     * @param board 游戏面板
     * @return 剩余未消除的方块对数
     */
    public static int getRemainingPairs(int[][] board) {
        int count = 0;
        for (int[] row : board) {
            for (int tile : row) {
                if (tile != 0) {
                    count++;
                }
            }
        }
        return count / 2;
    }
    
    /**
     * 检查棋盘是否还有有效的移动
     * 
     * 遍历所有非空方块对，检查是否存在可以连接的相同方块
     * 
     * @param board 游戏面板
     * @return 如果存在可消除的方块对返回true，否则返回false
     */
    public static boolean hasValidMoves(int[][] board) {
        // 收集所有非空方块的位置
        List<Point> tiles = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 0) {
                    tiles.add(new Point(i, j));
                }
            }
        }
        
        // 检查所有方块对是否可以连接
        for (int i = 0; i < tiles.size(); i++) {
            for (int j = i + 1; j < tiles.size(); j++) {
                Point p1 = tiles.get(i);
                Point p2 = tiles.get(j);
                if (board[p1.x][p1.y] == board[p2.x][p2.y] && 
                    canConnect(board, p1.x, p1.y, p2.x, p2.y)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 保存游戏进度
     * 
     * 设置保存时间并调用文件工具保存游戏状态
     * 
     * @param state 要保存的游戏状态对象
     * @see FileUtil#saveObject(Object, String)
     */
    public static void saveGame(GameState state) {
        state.setSaveTime(new Date());
        FileUtil.saveObject(state, FileUtil.getSavePath(state.getUsername()));
    }
    
    /**
     * 加载游戏进度
     * 
     * 从文件中读取游戏状态，并验证用户名是否匹配
     * 
     * @param username 用户名
     * @return 加载的游戏状态对象，如果加载失败或用户名不匹配则返回null
     * @see FileUtil#loadObject(String)
     */
    public static GameState loadGame(String username) {
        Object obj = FileUtil.loadObject(FileUtil.getSavePath(username));
        if (obj instanceof GameState) {
            GameState state = (GameState) obj;
            if (state.getUsername().equals(username)) {
                return state;
            }
        }
        return null;
    }
    
    /**
     * 检查用户是否有存档文件
     * 
     * @param username 用户名
     * @return 如果存在存档文件返回true，否则返回false
     */
    public static boolean hasSaveFile(String username) {
        return FileUtil.saveFileExists(username);
    }
}
