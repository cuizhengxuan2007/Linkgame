package com.linkgame.entity;

import java.io.Serializable;

/**
 * 用户类
 * 
 * 用于表示游戏用户的基本信息，包含用户名和密码
 * 实现了Serializable接口以支持用户数据的持久化存储
 * 
 * @author LinkGame Team
 * @version 1.0
 * @see Serializable
 */
public class User implements Serializable {
    /** 序列化版本号，用于确保序列化和反序列化的兼容性 */
    private static final long serialVersionUID = 1L;
    
    /** 用户名，用于登录标识 */
    private String username;
    
    /** 用户密码，用于身份验证 */
    private String password;
    
    /**
     * 默认构造函数
     * 创建一个空的用户对象
     */
    public User() {}
    
    /**
     * 带参数的构造函数
     * 
     * @param username 用户名
     * @param password 密码
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    /**
     * 获取用户名
     * 
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * 设置用户名
     * 
     * @param username 要设置的用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * 获取密码
     * 
     * @return 用户密码
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * 设置密码
     * 
     * @param password 要设置的新密码
     */
    public void setPassword(String password) {
        this.password = password;
    }
}