# 手写MyBatis ORM 框架
## 项目说明
从零到一手写一个MyBatis ORM框架。
从中体会MyBatis的实现的过程。


## 文档



## 实现思路
1 
2
3 在上一部分 我们使用了MapperRegistry 对包路径进行了扫描 注册映射器 并且在 
DefaultSqlSession.java 中通过MapperRegistry.getMapper(type) 获取到对应的MapperProxyFactory
那么我们可以把这些命名空间 以及 SQL描述 映射信息统一维护到每一个DAO 对应的 xml文件之中
每一个xml文件就是我们的一个源头 通过 对于 xml文件的解析和处理就可以完成 Mapper映射器的注册和SQL的管理。
这样就可以让我们对他进行操作以及使用了 
首先 我们需要定义SqlSessionFactoryBuilder 工厂建造者模式类，通过入口IO的方式对XML文件进行解析
当前 我们主要以解析SQL部分为主 注册映射器 串联出 整个部分的流程 
文件解析之后 我们会存放到Configuration 配置类之中
我们在DefaultSqlSession.java中获取Mapper和执行selectOne 也同样是需要再Configuration 配置类之中进行去读操作


## 目录结构

一定会补齐。


## 你的点赞鼓励，是我前进的动力~

## 你的点赞鼓励，是我前进的动力~

## 你的点赞鼓励，是我前进的动力~

## 

