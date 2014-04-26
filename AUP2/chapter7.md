### C程序启动和终止
![C程序启动和终止](images/charpter7_1.png)
内核执行C程序时, 通过内核的exec启动一个进程, 然后在进程中运行一个`C启动例程`, 并作为程序的起始地址, 然后调用main方法.

有8种方法终止进程:

1. 从main方法返回
1. 调用exit
1. 调用_exti 或 _Exit
1. 最后一个线程从启动例程返回
1. 最后一个线程调用pthread_exit
1. 调用abort
1. 接到一个信号终止
1. 最后一个线程对取消请求做出响应

基中最后三个为异常终止方法.

exit函数:
```c
#include <stdlib.h>
void exit(int status);
void _Exit(int status);
#include <unistd.h>
void _exit(int status);
```
**注意:** `exit`和`_exit`,`_Exit`的区别是, 前者先执行一些清理动作(如关闭进程打开的标准IO流),再进入内核; 而后者是直接进入内核.

#### atexit函数
```c
#include <stdlib.h>
int atexit(void (*func)(void)); //返回值: 成功返回0
```
上面我们说`exit`方法在退出进入内核之前会执行一些清楚动作, 如挂载的终止处理程序, 打开的流文件. 这些挂载的终止处理程序就是通过`atexit`方法实现的.看如下代码段:
```c
static void my_exit1(void);
static void my_exit2(void);


int main(int argc, char *argv[]){
    if(atexit(my_exit2) != 0){
        err_sys("can not register 2");
    }
    if(atexit(my_exit1) != 0){
        err_sys("can not register 1");
    }
    if(atexit(my_exit1) != 0){
        err_sys("can not register 1");
    }

    printf("main is done\n");
 	return 0;
}

static void my_exit1(void){
    printf("1 is exit\n");
}

static void my_exit2(void){
    printf("2 is exit\n");
}
```
打印:
```bash
main is done
1 is exit
1 is exit
2 is exit
```
执行的顺序和挂载的顺序是相反的.

### C程序的存储空间布局
![程序存储](images/chpater7_3.png)

* 栈(stack). 自动变量及函数调用时所需要的信息都保存在这里. 函数的返回地址; 递归调用函数时, 每次都使用一个新的栈空间.
* 堆(heap). 动态存储分配空间
* 未初始化数据(bss). 函数外的任何声明, 但是未初始化数据. 内核会将数据初始化为0或者空指针. 如`long sum[1000]`
* 初始化数据. 通常称为数据段, 包含程序中明确需要初始化值的变量. 如`int maxcount=99`
* 正文段. 由cpu执行的机器指令.通常是共享的. 

#### 存储器分配
```c
#include <stdlib.h>
void *malloc(size_t size);
void *calloc(size_t nobj, size_t size);
void *realloc(void *ptr, size_t newsize);

void free(void *ptr);
```

###环境变量
每个程序启动时都会从内核中收到一张环境表(参考上图中,环境变量表存放在栈空间上面), 就像参数表一样. 放在environ变量中. 如下程序遍历所有的环境变量:
```c
extern char** environ; //外部系统预定义的环境表变量

int main(int argc, char *argv[]){
    for(int i = 0; environ[i] != NULL; i++){
        printf("%s\n", environ[i]);
    }
}
```

#### 环境变量获取和设置函数
```c
#include <stdlib.h>
char *getenv(const char *name);
int putenv(char *nameValue);
int setenv(const char *name, const char *value, /*1为覆盖,0为忽略*/int rewrite);
int unsetenv(const char *name);
```

### setjmp和longjmp函数
```c
#include <setjmp.h>
int setjmp(jmp_buf env);
void longjmp(jmp_buf env, int val);
```
这两个函数一般成对出现,就像goto语句一样, 只是`goto`是函数内的跳转, 而`longjmp`是栈帧之间的跳转.


