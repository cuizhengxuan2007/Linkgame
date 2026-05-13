package com.linkgame.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件操作工具类，提供数据的序列化和反序列化功能
 * 
 * 主要功能包括：
 * 1. 用户数据的加载和保存
 * 2. 通用对象的序列化存储和读取
 * 3. 存档文件路径管理
 * 
 * 所有数据均以Java对象序列化的形式存储
 * 自动创建必要的目录结构
 * 
 * @author Linkgame Team
 * @version 1.0
 */
public class FileUtil {
    /** 用户数据文件存储目录 */
    private static final String USER_DATA_DIR = "data/users/";
    
    /** 游戏存档文件存储目录 */
    private static final String SAVE_DATA_DIR = "data/saves/";
    
    /**
     * 静态初始化块
     * 在类加载时自动创建所需的数据目录
     */
    static {
        new File(USER_DATA_DIR).mkdirs();
        new File(SAVE_DATA_DIR).mkdirs();
    }
    
    /**
     * 加载用户数据
     * 
     * 从 data/users/users.dat 文件中读取用户信息
     * 如果文件不存在或读取失败，返回空的HashMap
     * 
     * @return 用户数据映射表（用户名->密码）
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> loadUsers() {
        File file = new File(USER_DATA_DIR + "users.dat");
        if (!file.exists()) {
            return new HashMap<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                return (Map<String, String>) obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
    
    /**
     * 保存用户数据
     * 
     * 将用户映射表序列化写入到 data/users/users.dat 文件
     * 
     * @param users 要保存的用户数据映射表
     */
    public static void saveUsers(Map<String, String> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_DIR + "users.dat"))) {
            oos.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 加载任意Java对象
     * 
     * 通用的对象反序列化方法，可用于读取各种序列化对象
     * 
     * @param path 文件路径
     * @return 反序列化后的对象，如果文件不存在或读取失败则返回null
     */
    public static Object loadObject(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 保存任意Java对象
     * 
     * 通用的对象序列化方法，自动创建父目录
     * 
     * @param obj 要保存的对象
     * @param path 文件保存路径
     */
    public static void saveObject(Object obj, String path) {
        try {
            File file = new File(path);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取用户存档文件的路径
     * 
     * @param username 用户名
     * @return 存档文件的完整路径（格式：data/saves/用户名.dat）
     */
    public static String getSavePath(String username) {
        return SAVE_DATA_DIR + username + ".dat";
    }
    
    /**
     * 检查用户的存档文件是否存在
     * 
     * @param username 用户名
     * @return 存档文件存在返回true，不存在返回false
     */
    public static boolean saveFileExists(String username) {
        return new File(getSavePath(username)).exists();
    }
}