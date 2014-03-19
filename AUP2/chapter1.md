### unix 体系结构

内核 --> 系统调用／库函数 --> shell --> 应用软件

### 用户标识

用户id,组id，对应的信息存放在 `/etc/passwd` 和 `/etc/group` 中

打印当前登录用户id和组id:

```c
int main(int argc, char *argv[]){
    printf("uid = %d, gid = %d\n", getuid(), getgid());
    exit(0);	
}
```