# 线程概念
在一个进程内, 可以有多个控制线程, 线程包含了表示进程内执行环境必需的信息, 其中包括标识线程的ID, 一组寄存器, 栈, 调度优先级和策略, 信息屏蔽字, error变量.

# 线程标识
在Linux中用无符号长整形表示线程id, 在有的操作系统用一个数据结构定义.
线程的比较:
```c
#include <pthread.h>
int pthread_equal(pthread_t tid1, pthread_t tid2); // 相等返回0, 否则返回非0
```

# 线程创建
```c
#include "apue.h"
#include <pthread.h>

pthread_t ntid;

void printtids(const char *s){
    pid_t pid;
    pthread_t tid;
    pid = getpid();
    tid = pthread_self();
    printf("%s pid %u tid %u (0x%x)\n", s, (unsigned int)pid, (unsigned int)tid, (unsigned int)tid);
}

void * thr_fn(void *arg){
    printtids("new thread :");
    sleep(1000);
    return ((void *)0);
}

int main(int argc, char *argv[]){
    int err;
    err = pthread_create(&ntid, NULL, thr_fn, NULL);
    if(err != 0){
        err_quit("can not create thread : %s\n", strerror(err));
    }
    printtids("main thread :");
    sleep(10000);
    exit(0);
}  
```
输出:
```bash
main thread : pid 3726 tid 3970209536 (0xeca49700)
new thread : pid 3726 tid 3961923328 (0xec262700)
```

# 线程退出
```c
#include <pthread.h>
void pthread_exit(void *rval_ptr);
```
进程中的其它线程可以调用join来得到一个线程退出时的状态:
```c
int pthread_join(pthread_t thread, void **rval_ptr); // 成功返回0, 出错返回错误码
```

# 线程同步
```c
#include <pthread.h>
int pthread_mutex_lock(pthread_mutex_t *mutex);
int pthread_mutex_trylock(pthread_mutex_t *mutex);
int pthread_mutex_unlock(pthread_mutex_t *mutex);
// 成功返回0,出错返回错误码
```
























