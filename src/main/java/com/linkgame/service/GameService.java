package com.linkgame.service;

import com.linkgame.entity.Difficulty;
import com.linkgame.entity.GameState;
import com.linkgame.util.FileUtil;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GameService {
    public static int[][] createBoard(Difficulty difficulty) {
        if (difficulty == Difficulty.EASY) {
            return createEasyBoard();
        } else {
            return createHardBoard();
        }
    }
    
    private static int[][] createEasyBoard() {
        int totalTiles = 4 * 4 * 2;
        int pairs = totalTiles / 2;
        
        int[] tiles = new int[totalTiles];
        for (int i = 0; i < pairs; i++) {
            tiles[i * 2] = i % 5 + 1;
            tiles[i * 2 + 1] = i % 5 + 1;
        }
        
        Random random = new Random();
        for (int i = tiles.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = tiles[i];
            tiles[i] = tiles[j];
            tiles[j] = temp;
        }
        
        int[][] board = new int[8][12];
        int index = 0;
        
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                board[i][j] = tiles[index++];
            }
        }
        
        for (int i = 3; i <= 6; i++) {
            for (int j = 7; j <= 10; j++) {
                board[i][j] = tiles[index++];
            }
        }
        
        return board;
    }
    
    private static int[][] createHardBoard() {
        int rows = 10;
        int cols = 10;
        int totalTiles = rows * cols;
        int pairs = totalTiles / 2;
        
        int[] tiles = new int[totalTiles];
        for (int i = 0; i < pairs; i++) {
            tiles[i * 2] = i % 12 + 1;
            tiles[i * 2 + 1] = i % 12 + 1;
        }
        
        Random random = new Random();
        for (int i = tiles.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = tiles[i];
            tiles[i] = tiles[j];
            tiles[j] = temp;
        }
        
        int[][] board = new int[rows][cols];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = tiles[index++];
            }
        }
        
        return board;
    }
    
    public static boolean canConnect(int[][] board, int r1, int c1, int r2, int c2) {
        return getConnectionPath(board, r1, c1, r2, c2) != null;
    }
    
    public static List<Point> getConnectionPath(int[][] board, int r1, int c1, int r2, int c2) {
        if (r1 == r2 && c1 == c2) {
            return null;
        }
        
        if (board[r1][c1] != board[r2][c2] || board[r1][c1] == 0) {
            return null;
        }
        
        int rows = board.length;
        int cols = board[0].length;
        
        int[][] extendedBoard = new int[rows + 2][cols + 2];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(board[i], 0, extendedBoard[i + 1], 1, cols);
        }
        
        int er1 = r1 + 1, ec1 = c1 + 1, er2 = r2 + 1, ec2 = c2 + 1;
        
        List<Point> path = directConnect(extendedBoard, er1, ec1, er2, ec2);
        if (path != null) {
            return adjustPath(path);
        }
        
        path = oneCornerConnect(extendedBoard, er1, ec1, er2, ec2);
        if (path != null) {
            return adjustPath(path);
        }
        
        path = twoCornerConnect(extendedBoard, er1, ec1, er2, ec2);
        if (path != null) {
            return adjustPath(path);
        }
        
        return null;
    }
    
    private static List<Point> adjustPath(List<Point> path) {
        List<Point> adjusted = new ArrayList<>();
        for (Point p : path) {
            adjusted.add(new Point(p.x - 1, p.y - 1));
        }
        return adjusted;
    }
    
    private static List<Point> directConnect(int[][] board, int r1, int c1, int r2, int c2) {
        if (r1 == r2) {
            int minC = Math.min(c1, c2);
            int maxC = Math.max(c1, c2);
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
        
        if (c1 == c2) {
            int minR = Math.min(r1, r2);
            int maxR = Math.max(r1, r2);
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
    
    private static List<Point> oneCornerConnect(int[][] board, int r1, int c1, int r2, int c2) {
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
    
    private static List<Point> twoCornerConnect(int[][] board, int r1, int c1, int r2, int c2) {
        int rows = board.length;
        int cols = board[0].length;
        
        for (int c = 0; c < cols; c++) {
            if (c != c1 && c != c2) {
                boolean canPass1 = (c == 0 || c == cols - 1) || board[r1][c] == 0;
                boolean canPass2 = (c == 0 || c == cols - 1) || board[r2][c] == 0;
                
                if (canPass1 && canPass2) {
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
        
        for (int r = 0; r < rows; r++) {
            if (r != r1 && r != r2) {
                boolean canPass1 = (r == 0 || r == rows - 1) || board[r][c1] == 0;
                boolean canPass2 = (r == 0 || r == rows - 1) || board[r][c2] == 0;
                
                if (canPass1 && canPass2) {
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
        
        if (r1 == r2 || c1 == c2) {
            return null;
        }
        
        List<Point> borderPath = tryBorderConnection(board, r1, c1, r2, c2);
        if (borderPath != null) {
            return borderPath;
        }
        
        return null;
    }
    
    private static List<Point> tryBorderConnection(int[][] board, int r1, int c1, int r2, int c2) {
        int rows = board.length;
        int cols = board[0].length;
        
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
    
    public static boolean hasValidMoves(int[][] board) {
        List<Point> tiles = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 0) {
                    tiles.add(new Point(i, j));
                }
            }
        }
        
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
    
    public static void saveGame(GameState state) {
        state.setSaveTime(new Date());
        FileUtil.saveObject(state, FileUtil.getSavePath(state.getUsername()));
    }
    
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
    
    public static boolean hasSaveFile(String username) {
        return FileUtil.saveFileExists(username);
    }
}
