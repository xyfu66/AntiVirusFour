# 无笔安全

 欢迎Star

>项目简介：
 无笔安全是基于Android studio平台下设计的一款管家软件，支持离线提供所有检测服务，包括人脸识别和面部检测，力求手机管理与健康管理为一体，
为用户提供更好的服务。

>功能划分
 1. 人脸检测的功能分为两个模块，注册人脸和人脸杀毒。
 2. 手机卫士的功能共分为九个模块，依次为手机防盗，通信卫士，软件管家，进程管理，流量统计，手机杀毒，缓存清理，空文件整理，高级工具。
 3. 无笔健康功能分为六个小模块和一个推荐功能。推荐功能类似某宝，是某商品的介绍页面。六个小模块依次为健康报告，运动健康，健康新闻，智能设备，健康菜谱，课程。
 4. 流量统计功能，阅读新鲜事功能。

![Image1](https://github.com/xyfu66/AntiVirusFour/blob/master/img/图片1.png)
![Image1](https://github.com/xyfu66/AntiVirusFour/blob/master/img/图片2.png)
![Image1](https://github.com/xyfu66/AntiVirusFour/blob/master/img/100.png)
![Image1](https://github.com/xyfu66/AntiVirusFour/blob/master/img/图片3.png)
![Image1](https://github.com/xyfu66/AntiVirusFour/blob/master/img/图片4.png)
![Image1](https://github.com/xyfu66/AntiVirusFour/blob/master/img/图片5.png)
![Image1](https://github.com/xyfu66/AntiVirusFour/blob/master/img/图片6.png)
![Image1](https://github.com/xyfu66/AntiVirusFour/blob/master/img/图片7.png)
![Image1](https://github.com/xyfu66/AntiVirusFour/blob/master/img/图片8.png)
![Image1](https://github.com/xyfu66/AntiVirusFour/blob/master/img/图片9.png)
![Image1](https://github.com/xyfu66/AntiVirusFour/blob/master/img/图片10.png)
![Image1](https://github.com/xyfu66/AntiVirusFour/blob/master/img/图片11.png)
![Image1](https://github.com/xyfu66/AntiVirusFour/blob/master/img/图片12.png)
![Image1](https://github.com/xyfu66/AntiVirusFour/blob/master/img/图片13.png)


>工程如何使用？
 1. 下载代码:    
    git clone https://github.com/xyfu66/AntiVirusFour.git 或者直接下载压缩包
 
 2. 因为本应用使用了虹软开源的SDK，所以，要想构建自己的工程，请前往[虹软官网](http://www.arcsoft.com.cn/ai/arcface.html)申请appid和sdkkey。    
    修改 ArcFaceDemo-master\src\main\java\com\arcsoft\sdk_demo\FaceDB.java 下面的对应的值:    
   
    ```java    
    public static String appid = "xxxx"; 		
    public static String fd_key = "xxxx";    
    public static String ft_key = "xxxx";
    public static String fr_key = "xxxx";
    ```

3. Android Studio3.0 中直接打开或者导入Project,编译运行即可。    

> 人脸模块如何使用?（类似于虹软的Demo使用方法）    

 1. 点击第一个按钮 打开图片或者拍一张带人脸的照片，确认后自动执行人脸，弹出注册框，注册第一个人脸。    
 注册界面底部会展示已注册的信息列表，点击列表项，则可以执行删除操作。   
 2. 点击第二个按钮 选择打开前置或者后置的镜头进行检测。
 
> 人脸数据的保存方式?  

　以注册时人名为关键索引，保存在face.txt中。  
　创建的 name.data 则为实际的数据存储文件，保存了所有特征信息。  
　同一个名字可以注册多个不同状态角度的人脸，在name.data 中连续保存，占用的数据文件长度为:  
　N * {4字节(特征数据长度) + 22020字节(特征数据信息)}
  
> 支持的API-LEVEL?  

　24-26    　

---------------
> FAQ
1. 直接运行的话Gradle提示的错误有很多，但应该都不是大问题，自行修改即可。     
	
2. 加载图片注册时Crash.    
 NV21格式限制高度不能为奇数，宽度可以随意，demo没有对这个做保护，请自行注意加载注册的图片尺寸。
    
3. 年龄和性别检测结果准确度不够.    
 Video的接口性能优先，Image的接口准确度优先。    
 
4. 有些功能是不完善的，勿喷。
