# Buffer/Cache含义
Buffer用于磁盘的读写缓存。Cache用于文件的读写缓存。

# bcc memleak
`memleak`可以动态追踪内存泄漏。

# Swap
当内存不足时，通过回收buff/cache（也称文件页）来释放内存。
还一个方法是回收不活跃进程的动态堆内存（也称匿名页）。

# 常用分析工具
![001](images/memory_01.png)
![002](images/memory_02.png)
![003](images/memory_03.png)
![004](images/memory_04.png)
