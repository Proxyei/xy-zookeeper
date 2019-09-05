# zookeeper笔记

## 学习目标

## 1、构造原理

## 2、如何集群

# 1、zookeeper是什么

一句话：zookeeper是一个分布式的、高性能 的、开源的协调服务，是为分布式系统提供一致性服务的软件。

zookeeper=文件系统+通知机制。

# 2、zookeeper能做什么

命名服务、配置维护、集群管理、分布式消息同步和协调机制、负载均衡、对dubbo的支持。

# 3、zoo.cfg参数

## 1、tickTime

心跳通信间隔时间，维护服务器之间或者服务器与客户端之间的连接，单位毫秒。

## 2、initLimit

leader与fllower建立初始连接容忍的时限。

## 3、syncLimit

集群中leader与follower之间的最大响应时间，假如响应超过syncLimit*tickTime，则认为follower掉线。从服务器中删除follower。

## 4、dataDir



## 5、clientPort

# 4、znode数据模型

## znode节点参数解释

## 疑问：如何得知节点下还有其他节点的节点名字

```shell
[zk: localhost:2181(CONNECTED) 35] get /xy
weiwei # /xy节点的值
cZxid = 0x4
ctime = Tue Sep 03 01:44:57 CST 2019
mZxid = 0x5
mtime = Tue Sep 03 01:47:33 CST 2019
pZxid = 0x6
cversion = 1
dataVersion = 1
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 6 # /xy节点的值的数据长度
numChildren = 1 # /xy节点下有一个孩子
```

# 5、znode类型

```shel
[zk: localhost:2181(CONNECTED) 2]create /xy1 val1 # 表示创建永久节点
[zk: localhost:2181(CONNECTED) 2]create -s /xy2 /val2 # 表示创建永久的带序列号的节点
[zk: localhost:2181(CONNECTED) 2]create -e /xy3 /val3 # 创建临时节点
[zk: localhost:2181(CONNECTED) 2]create -e -s /xy4 val4 # 创建临时带序列号的节点
```



# 6、zookeeper常用命令

1. create 
2. update

# 7、Java操作zookeeper

## 1、节点变化



## 2、节点数据变化



# 8、通知机制



## 1、session



## 2、watch 

在getData，exist，getChirlden都有watch，关键点需要设置watche。

### 1、值监控

使用getData，exist

### 2、节点监控

使用getChirldren。

# 9、选举机制

1. 集群一般是基数台服务器，因为需要超过半数以上的服务器才能正常运行，而且一般leader选myid最大的。
2. 只要leader选举出来了，即使后面myid更大的加入集群也是follower。

# 10、需求实现

做一个监控用户的上线下线提示。