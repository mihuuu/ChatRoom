# ChatRoom

### 项目简介

* 使用 Java 开发的聊天应用，通过 Socket 实现网络通信
* 使用 Swing 设计 GUI 
* 通过 JDBC 连接 MySQL 数据库
* 创建线程池支持多个用户线程

![chat](https://i.loli.net/2019/03/30/5c9f333bb9e8a.png)

### 项目结构

* server包
  * ChatServer 服务端
  * UserThread 用户线程
* client包
  * Main 客户端主程序
  * ChatRoom 聊天主界面
  * LoginDialog 登录界面
  * RegDialog 注册界面
* data包
  * DataBase 数据库

### 运行程序
1. 运行 ChatServer 类，启动服务端；
2. 运行 Main 类（客户端，可并发运行）
3. 初始界面中，点击注册或登录；登录成功后会进入聊天主界面。

### 待优化

- [ ] 支持发送图片 / 文件
- [ ] BIO 重构为 NIO（考虑换 Netty ）