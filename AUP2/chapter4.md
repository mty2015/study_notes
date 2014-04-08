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

1. 如果要操作一个文件,则要对包含该文件的所有目录具有`执行`权限, 如想打开/home/tony/a.txt, 则要对目录/home,/home/tony都有执行权限
1. 删除一个文件时,要对包含它的目录们都有写和执行的权限. 对该文件本身不要求有读,写权限.
1. 判断权限时, 按照当前进程的有效用户 --> 有效组用户 --> 其它用户的顺序检查执行, 且有`短路`特性. 即如果前面检查如果成功或者失败, 都不会继续检查. 比如, 当前进程用户删除某个文件, 而这个进程用户就是文件的所有者, 则比较u(rwx)权限就可以了. 而不会继续去检查组和其它用户的权限

### umask
```c
#include <sys/stat.h>
mode_t umask(mode_t cmask);
```
参数`cmask`可以理解成如果关闭的`标识位`; `cmask`可以是下面表格中的值的`与`的逻辑运算结果:

屏蔽位|说明
---|---
0400|用户读
0200|用户写
0100|用户执行
...|...

### chown, fchown, lchown函数

```c
#include <unistd.h>
int chown(const char *pathname, uid_t owner, gid_t group);
int fchown(int fd, uid_t owner, gid_t group);
int lchown(const char *pathname, uid_t owner, gid_t group);
```

**注意:** 如果`owner`或`group`为-1,则不更改之前的值.

### 文件截短
```c
#include <unistd.h>
int truncate(const char *pathname, off_t length);
int ftruncate(int fd, off_t length);
```
这两个函数把现有文件截取到`length`长度的地方.

### unix/linux文件系统结构

当我们读取一个文件(目录也是一种文件类型)时要经过几步: 先根据文件名读取对应的`inode`节点编号, 再根据`inode编号`找到对应的`数据块`(data block)位置. 一篇通俗易懂的文章:[inode介绍](http://www.ruanyifeng.com/blog/2011/12/inode.html)

### symlink和readlink函数

```c
#include <unistd.h>
int symlink(const char *actualpath, const char *sympath);
```

对应于shell命令:`ln -s actualpath sympath`; 要注意的是actualpath可以并不存在.

```c
#include <unistd.h>
ssize_t readlink(const char *pathname, char *buf, size_t bufsize);
```
因为open函数打开符号连接参数时,会跟踪到真实的目标文件去, 所以我们如果想读取符号连接本身的话, 可以调用这个函数. 它读取的内容就是该链接的`名字`. 


### 文件的时间

字段|说明|例子|ls(1)选项
---|---|---|---
st_atime|文件数据的最后访问时间|read|-u
st_mtime|文件数据的最后修改时间|write|默认
st_ctime|i结点状态的最后修改时间|chmod,chown|-c

修改文件`最后访问`,`修改`时间:
```c
#include <utime.h>
int utime(const char *pathname, const struct utimbuf *times);
```
如果times为空(`NULL`), 则设置为系统当前时间; 否之, 则取times中的值设置. 

### chdir, fchdir, getcwd函数

```c
#include <unistd.h>
int chdir(const char *pathname);
int fchdir(int fd);
char *getcwd(char *buf, size_t size);
```
`chdir`对应于shell的`cd`, `getcwd`对应于shell的`pwd`



