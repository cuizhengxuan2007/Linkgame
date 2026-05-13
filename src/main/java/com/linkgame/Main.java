package com.linkgame;

import com.linkgame.ui.LoginFrame;

/**
 * 连连看游戏主程序入口
 * 
 * 启动游戏应用程序，显示登录界面
 * 
 * @author LinkGame Team
 * @version 1.0
 * @see LoginFrame
 */
public class Main {
    /**
     * 程序主入口方法
     * 
     * 创建并显示登录窗口，开始游戏流程
     * 
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
    }
}