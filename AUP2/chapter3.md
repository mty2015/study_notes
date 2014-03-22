### 文件描述符
Unix系统中大多数文件I/O只需用到五个函数：`open`,`read`,`write`,`lseek`,`close`.

当程序打开，创建文件时，系统内核向进程返回一个文件描述符（非负整数）。对于标准的输入，标准输出，标准错误分别对应数字：0，1，2. 在头文件`<unistd.h>`中定义

### open 函数
```c
#include <fcntl.h>
int open(const char * pathname, int oflg, mode_t mode);
```

### create 函数
```c
#include <fctnl.h>
int create(const char * pathname, mode_t mode);
```
此函数等于：
```c
open(pathname, O_WRONLY | O_CREAT | O_TRUNC, mode);
```
1. `O_WRONLY`: 只写打开
1. `O_CREAT`: 如果文件不存在，则创建它
1. `O_TRUNC`: 如果文件存在，则将其长度截为0

### close函数
```c
#include <unistd.h>
int close(int fileds);
```

### lseek函数
** 每个打开的文件都有一个与其关联的当前文件偏移量(current file offset) **

```c
#include <unistd.h>
off_t lseek(int fileds, off_t offset, int whence);
```
`whence`有如下三种值:

1. `SEEK_SET` 从0(文件开始)偏移
2. `SEEK_CUR` 从当前值开始偏移
3. `SEEK_END` 从文件末尾开始偏移

**注意:** offset值可以为负

对于一个管道(Pipe), FIFO， 网络套接字(Socket)，调用lseek会返回-1。并将errno设置为`ESPIPE`

lseek函数可以传入的偏移值大于文件末尾的位置值，如果当前的偏移值大于末尾位置，并且此时继续写入数据的话，从末尾的位置到新写入的数据位置全部是`空位`，不用占据存储空间。下面看个写入空洞数据的程序：
```c
#include "apue.h"
#include <fcntl.h>

int main(int argc, char *argv[]){
    char buf1[] = "abcdefghij";
    char buf2[] = "ABCDEFGHIJ";

    int fd = creat("file.data", FILE_MODE);
    if(fd < 0){
        err_sys("creat error");
    }

    if(write(fd, buf1, 10) != 10){
        err_sys("buf1 write error");
    }

    if(lseek(fd, 16384, SEEK_SET) == -1){//直接定位到16384的位置,那么从10到16383的位置上数据全部是0
        err_sys("lseek is error");
    }

    if(write(fd, buf2, 10) != 10){
        err_sys("write buf2 error");
    }

    exit(0);
 	
}
```

如果此时通过ls查看生成的文件属性：

```sh
tony@tony:~/cstudy$ ls -lhs
8.0K -rw-r--r-- 1 tony tony  17K  3月 22 23:04 file.data
```
可以看到文件的大小是17K，但是占据的存储空间(磁盘)才8K，这说明空洞数据不占据存储。








