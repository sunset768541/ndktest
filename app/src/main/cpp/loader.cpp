#include <stdlib.h>
#include <dlfcn.h>

typedef int (*FUNC)(int, int);
//代码段
void test(){
    char * str="this is test";
    void *handle;
    char *error;
    FUNC func = NULL;
    //打开动态链接库
    handle = dlopen("./libmath.so", RTLD_LAZY);//extern段
    //获取一个函数
    *(void **) (&func) = dlsym(handle, "add");//extern段
    printf("add: %d\n", (*func)(2,7));//extern段
        //关闭动态链接库
    dlclose(handle);//extern段
}
//plt段，延时加载，