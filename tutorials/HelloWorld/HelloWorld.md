# 1. 安装DevEco Studio
### DevEco Studio介绍
HUAWEI DevEco Studio（以下简称DevEco Studio）是基于IntelliJ IDEA Community开源版本打造，面向华为终端全场景多设备的一站式集成开发环境（IDE），为开发者提供工程模板创建、开发、编译、调试、发布等E2E的HarmonyOS应用开发服务。

我们可以通过它，更方便地新建打开HarmonyOS应用，以及完成后续的编译，调试，模拟器和设备部署。如果你之前使用过IntelliJ IDEA的任意一款，那么安装和使用完全不成问题。

### DevEco Studio下载地址
目前只有windows版本和mac版本：
[下载地址](https://developer.harmonyos.com/cn/develop/deveco-studio#download)

### DevEco Studio安装
感觉没啥好说的，一路点“下一步”，需要的话更改下默认的安装目录即可。

# 2. 配置开发环境（windows版本）
1.双击桌面**DevEco Studio**快捷方式，首次启动，选择默认地域**China**。

![image-20210426144346688](HelloWorld.assets/image-20210426144346688.png)

2.同意隐私政策。

![image-20210426144516000](HelloWorld.assets/image-20210426144516000.png)

3.选择SDK安装目录。

![image-20210426144801228](HelloWorld.assets/image-20210426144801228.png)

4. 如果是内网环境，则需要配置代理，否则可忽略此步骤。

	4.1 设置**DevEco Studio**代理，用于下载SDK。
	![image-20210426145522733](HelloWorld.assets/image-20210426145522733.png)
	4.2 设置**npm代理**，用于JS语言开发HarmonyOS应用的依赖：进入DevEco Studio安装目录下的tools\nodejs目录，运行命令设置代理。注意将特殊字符替换为ASCII码。
```
npm config set proxy http://user:password@proxyserver:port
npm config set https-proxy http://user:password@proxyserver:port
```

代理设置完成后，执行如下命令进行验证。

```
npm info express
```


# 3. 创建和运行第一个应用Hello World
1. 点击**Create HarmonyOS Project**创建项目，选择**Phone**设备，选择**Empty Feature Ability(JS)**模板后，点击“Next”。![image-20210426150334139](HelloWorld.assets/image-20210426150334139.png)

2. 输入项目名称**HelloWorld**，包名**com.harmonyos.hellowrold**，以及选择项目路径后，点击**Finish**完成项目创建。

   ![image-20210426150549611](HelloWorld.assets/image-20210426150549611.png)

3. 等待SDK下载完，Gradle下载依赖并编译完后，从**Tools-->HVD Manager**启动模拟器。 如果遇到错误，请参见下面的**常见错误**。

![image-20210426152143150](HelloWorld.assets/image-20210426152143150.png)

4. 登录华为账号，并授权**DevEco Studio**。

![image-20210426151412778](HelloWorld.assets/image-20210426151412778.png)

5.选择**P40**设备，启动模拟器。

![image-20210426151803704](HelloWorld.assets/image-20210426151803704.png)

6. 设备正常启动后，点击**Run Entry**图标，启动应用。恭喜你，跑完了**HarmonyOS**的第一个应用。

   ![image-20210426152742873](HelloWorld.assets/image-20210426152742873.png)



# 4. 常见问题
- 问题：使用**Empty Feature Ability(JS)**创建项目后，报错
```
A problem occurred configuring project ':entry'.
Unable to download the HarmonyOS SDK. Install js:2.1.0.25 fail!
```
  问题原因：npm依赖无法下载，大概率由于npm代理原因或网络导致
  解决方案：参考面提到的方法，配置npm代理

- 问题：**Tools-->HVD Manager**启动模拟器后IE白屏，导致无法登陆和打开模拟器。
  问题原因：登陆界面浏览器兼容问题
  解决方案：复制链接，使用Chrome打开登陆。建议把机器的默认浏览器设置为Chrome。

- 问题：启动应用时，**Run**窗口显示Failure[INSTALL_PARSE_FAILED_USESDK_ERROR]错误
  问题原因：当前应用config.json配置的SDK版本，与模拟器版本不匹配。
  解决方案：修改config.json，去掉releaseType(用来标识运行所需API版本的)。以及确认“apiVersion.target”是否与模拟器API版本一致。

- 问题：设备的状态为**invalid**，无法启动模拟器
  问题原因：后台模拟器状态问题
  解决方案：多点下**Refresh**按钮，将设备重置为**Ready**状态

