### 引言
在第三章,学习了`文件IO`(系统IO),这是一种无缓冲. 这里学习的`标准IO`(对`系统IO`的封装)是由ISO C标准说明, 它在系统IO的基础上处理了很多细节, 如分配系统最优的缓冲区长度.

`标准IO`的所有函数操作对象都是`FILE`, 我们称为流. 该结构通常包含: 对应的实际文件描述符, 用于该流缓冲区的指针, 缓冲区长度及缓冲区当前内容信息,出错标志.

### 缓冲
我们说`标准IO`相对于`系统IO`而言, 就是做了很多缓冲优化处理. 标准IO的缓冲有三种类型:

1. 全缓冲
1. 行缓冲
1. 不带缓冲; 如标准出错流stderr就是不带缓冲的, 便出错信息及时反馈打印出来

下列函数更改缓冲类型:
```c
#include <stdion.h>
void setbuf(FILE *fp, char *buf);
int setvbuf(FILE *fp, char *buf, int mode, size_t bufSize);
```
`setbuf`用指定的`buf`作为缓冲存储区域, 其长度一定是`BUFSIZE`(在stdio.h中定义); 而`setvbuf`则提供更细粒度的控制, 如`mode`指定缓冲类型(_IOFBF:全缓冲;_IOLBF:行缓冲;_IONBF:无缓冲), `bufSize`指定缓冲区大小. 

### 打开流
```c
#include <stdio.h>
FILE *fopen(const char *pathname, const char *type);
FILE *freopen(const char *pathname, const char *type, FILE *fp);
FILE *fdopen(int fd, const char *type);
```
参数type有几种值: `r`, `w`, `a` ,`r+`, `w+`, `a+`

### 关闭流
```c
#include <stdio.h>
int fclose(FILE *fp);
```
在文件关闭前, 会flush缓冲区的数据. 当进程终止时, 会自动关闭所有打开的流.

### 读写流
标准IO读写流有三种方式: 每次一个字符; 每次一行; 读取指定数据结构和长度(二进制IO)

#### 每次一个字符
```c
#include <stdio.h>
//读
int getc(FILE *fp);
int fgetc(FILE *fp);
int getchar(void);
//写
int putc(int c, FILE *fp);
int fputc(int c, FILE *fp);
int putchar(int c);
```

`getc`和`fgetc`的区别是:`getc`是宏, 而`fgetc`是真正的函数

#### 每次一行
```c
//读
char *fgets(char *buf, int n, FILE *fp);
char *gets(char *buf);
//写
int fputs(const char *str, FILE *fp);
int puts(const char *str);
```

#### 二进制IO
```c
size_t fread(void *ptr, size_t size, size_t nobj, FILE *fp);
size_t fwrite(const void *ptr, size_t size, size_t nobj, FILE *fp);
```


