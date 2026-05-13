package com.linkgame.service;

import com.linkgame.util.FileUtil;

import java.util.Map;

public class UserService {
    private static Map<String, String> users = FileUtil.loadUsers();
    
    public static boolean register(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return false;
        }
        
        if (users.containsKey(username)) {
            return false;
        }
        
        users.put(username, password);
        FileUtil.saveUsers(users);
        return true;
    }
    
    public static boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        
        String storedPassword = users.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }
    
    public static boolean userExists(String username) {
        return users.containsKey(username);
    }
    
    public static void reloadUsers() {
        users = FileUtil.loadUsers();
    }
}