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
