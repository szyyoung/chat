### 模块
##### client 
    chat 客户端 
##### protocol
    chat 协议  提供protobuf实现
##### server
    chat 服务端 维护客户端长连接，消息接收，消息下发，消息转发
##### restful
    chat 业务端 提供注册 登录 服务器地址选择
    
##### common
    chat 各模块 共用功能
    
    
    
### 基础环境搭建

###### mysql
    ```
    //本机创建文件挂载目录： /Users/young/docker/mysql
    docker pull mysql
    docker run -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 -v /Users/young/docker/mysql/data:/var/lib/mysql:rw -v /Users/young/docker/mysql/log:/var/log/mysql:rw -v /Users/young/docker/mysql/config/my.cnf:/etc/mysql/my.cnf:rw  --name mysql8 -d mysql    
    ```
######### my.cnf配置
    ```
    [mysqld]
    pid-file        = /var/run/mysqld/mysqld.pid
    socket          = /var/run/mysqld/mysqld.sock
    datadir         = /var/lib/mysql
    secure-file-priv= NULL
    # Disabling symbolic-links is recommended to prevent assorted security risks
    symbolic-links=0
    
    # Custom config should go here
    !includedir /etc/mysql/conf.d/
    
    default_authentication_plugin= mysql_native_password
    ```

###### redis 简单的单机模式
    ```
    docker pull redis
    docker run -itd --name redis-test -p 6379:6379 redis
    ```
    
###### zookeeper 简单的单机模式
    ```
    docker pull zookeeper
    docker run --privileged=true -d --name zookeeper --publish 2181:2181  -d zookeeper:latest
    ```
    
    