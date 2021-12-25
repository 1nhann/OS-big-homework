# 操作系统课设

## 概述

操作系统课设，但是是个 java web 项目。。。

前端用 react 实现，后端用的 springboot 。。。

## 启动

### 前后端分离

#### 后端

直接 idea 里面跑就行，或者编译成 jar 然后跑

#### 前端

因为是前后端分离，所以需要配置下 后端的 url ：

```javascript
frontend/src/pages/users/service.ts:9
9: const baseUrl = 'http://192.168.50.16:8883/';
// 表示后端跑在 192.168.50.16 的 8883 端口
```

然后安装依赖：

```shell
yarn
```

然后运行：

```shell
yarn start run
```



### 前后端集成

先编译下前端：

```shell
yarn start build
```

然后生成 `dist/index.html` 、`dist/umi.css` 、`dist/umi.js` 三个文件，放到 后端项目的 `src/main/resources/static ` 下面

然后直接 idea 里面 run 就行

## 成果展示

可以建目录、建文件、写文件、删文件、读文件

并实时展示模拟的磁盘和内存的使用情况

![image-20211225165222432](D:\Program Files (x86)\my_works\TyporaImages\image-20211225165222432.png)















