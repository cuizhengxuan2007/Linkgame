package com.linkgame.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {
    private static final String USER_DATA_DIR = "data/users/";
    private static final String SAVE_DATA_DIR = "data/saves/";
    
    static {
        new File(USER_DATA_DIR).mkdirs();
        new File(SAVE_DATA_DIR).mkdirs();
    }
    
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
    
    public static void saveUsers(Map<String, String> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_DIR + "users.dat"))) {
            oos.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
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
    
    public static String getSavePath(String username) {
        return SAVE_DATA_DIR + username + ".dat";
    }
    
    public static boolean saveFileExists(String username) {
        return new File(getSavePath(username)).exists();
    }
}