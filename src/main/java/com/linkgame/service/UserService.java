package com.linkgame.service;

import com.linkgame.util.FileUtil;

import java.util.Map;

/**
 * 用户服务类，提供用户注册、登录等核心业务功能
 * 
 * 主要功能包括：
 * 1. 用户注册（检查用户名是否已存在）
 * 2. 用户登录（验证用户名和密码）
 * 3. 检查用户是否存在
 * 4. 重新加载用户数据
 * 
 * 用户数据通过FileUtil进行持久化存储
 * 
 * @author Linkgame Team
 * @version 1.0
 * @see FileUtil
 */
public class UserService {
    /** 用户数据映射表，键为用户名，值为密码 */
    private static Map<String, String> users = FileUtil.loadUsers();
    
    /**
     * 用户注册
     * 
     * 注册流程：
     * 1. 验证用户名和密码不为空
     * 2. 检查用户名是否已被注册
     * 3. 将新用户信息添加到用户表
     * 4. 保存用户数据到文件
     * 
     * @param username 要注册的用户名
     * @param password 要设置的密码
     * @return 注册成功返回true，用户名已存在或参数为空返回false
     */
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
    
    /**
     * 用户登录
     * 
     * 验证提供的用户名和密码是否与存储的信息匹配
     * 
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回true，失败返回false
     */
    public static boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        
        String storedPassword = users.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }
    
    /**
     * 检查用户是否存在
     * 
     * @param username 要检查的用户名
     * @return 用户存在返回true，不存在返回false
     */
    public static boolean userExists(String username) {
        return users.containsKey(username);
    }
    
    /**
     * 重新加载用户数据
     * 
     * 从文件重新读取用户数据，用于同步外部修改
     */
    public static void reloadUsers() {
        users = FileUtil.loadUsers();
    }
}