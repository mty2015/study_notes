### 引言
上一章，我们主要是讨论了针对文件的IO相关的操作，如read,write。也说明了这些操作是无缓冲的操作。这些操作对象都是`普通文件`。而这章中我们说到的`文件`，一般包括`文件`和`目录`。我们要学习的是文件系统的一些特性和性质。包括：文件，目录，链接。

### stat、fstat和lstat函数

```c
#include <sys/stat.h>
int stat(const char *pathname, struct stat *buf);
int fstat(int fd, struct stat *buf);
int lstat(const char *pathname, struct stat *buf);
```
其中，stat和lstat功能差不多，只是lstat不是影响的文件目标本身，而是符号链接本身（就像文件的快捷方式，一种特殊文件）。`struct stat`的结构如下：

```c
struct stat {
  mode_t st_mode; /* 文件类型(type)和权限模型(permission mode)*/
  ino_t st_ino;
  dev_t st_dev;
  dev_t st_rdev;
  nlink_t st_nlink;
  uid_t st_uid;
  gid_t st_gid;
  off_t st_size;
  time_t st_atime;
  time_t st_mtime;
  time_t st_ctime;
  blksize_t st_blksize;
  blkcnt_t st_blocks;
}
```

### 文件类型
1. 普通文件，不管是文件还是二进制文件，对系统内核来说都是一样的。除了某进执行的二进制文件，因为系统要理解其约定的格式
1. 目录文件(directory)，包含其它文件
1. 块特殊文件(block special file),提供对`设备`带缓冲的访问
1. 字符特殊文件(character special file)，提供对`设备`不带缓冲的访问
1. FIFO，用于进程间通信
1. Socket，用于网络间通信
1. 符号链接(symbolic link)，指向另外一个文件

### 文件访问权限
文件的权限位有9个，分别是 u(rwx)g(rwx)o(rwx)，u代表文件用户，g代表文件的组名，o代表文件的其它用户。r是读权限，w是写权限,x是执行权限.如：
```bash
tony@tony:~/cstudy$ ll test.c 
-rw-rw-r-- 1 tony tony 950  4月  7 14:58 test.c
```
test.c文件的权限为: 用户有读写权限，没有执行权限；组别内的用户有读写权限，也没有执行权限；其它用户只有读权限

系统内核判断当前用户是否对一个文件(泛指普通文件，目录，符号链接...)具有什么样的访问权限,要根据两方面的数据来计算: 一是文件自身的权限属性,在`st_mode`字段中;另一方面是当前进程的有效用户和所在的用户组. 当前用户的id和组id是登录时读取相关配置信息获取的. 这里有几点需要注意:
1. df 





