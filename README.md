
# suhui-fundTransfer   

The repo for suihui fundTransfer project.    



## postman三个地址    
登陆注册    
https://www.getpostman.com/collections/a73ac6f4fa7c8bf6edaa     
postman文档    
https://documenter.getpostman.com/view/1895285/SVmySxzb?version=latest    


充值提现    
https://www.getpostman.com/collections/ff6f2d91d7529a4a7456     
postman文档     
https://documenter.getpostman.com/view/1895285/SVmySxze?version=latest    

支付汇总      
https://www.getpostman.com/collections/74176a8b6a4e4e00f43c    
postman文档     
https://documenter.getpostman.com/view/1895285/SVmySxzg?version=latest     



#### Description    
速汇项目



#### Software Architecture
Software architecture description

## Jenkins

```
# download Jenkins
wget "http://mirrors.jenkins.io/war-stable/latest/jenkins.war" 

# start Jenkins  
nohup `java -jar jenkins.war --httpPort=11000` &

### this one is the correct one!!
nohup java -jar jenkins.war --httpPort=11000 > jenkins.log &


`java -jar jenkins.war --httpPort=11000 > jenkins.log` & disown

your_command & disown

nohup java -jar jenkins.war --httpPort=11000 &

userName and Pass:suhuiadmin/suhuiadmin

http://3.93.15.101:11000
http://3.93.15.101:11000/job/suhuibackend/configure

# nohup java -jar -Xms64m -Xmx512m /root/mailCheck_fat.jar > /root/mail.log &


# set git and jenkins using ssh public and private keys
https://sharadchhetri.com/2018/12/16/how-to-setup-jenkins-credentials-for-git-repo-access/

# How to Integrate Your GitHub Repository to Your Jenkins Project
https://www.blazemeter.com/blog/how-to-integrate-your-github-repository-to-your-jenkins-project/

# 
https://www.haowenbo.com/articles/2019/07/24/1563964257491.html

```


draft
```sh
echo "BUILD_NUMBER" :: $BUILD_NUMBER
echo "BUILD_ID" :: $BUILD_ID
echo "BUILD_DISPLAY_NAME" :: $BUILD_DISPLAY_NAME
echo "JOB_NAME" :: $JOB_NAME
echo "JOB_BASE_NAME" :: $JOB_BASE_NAME
echo "BUILD_TAG" :: $BUILD_TAG
echo "EXECUTOR_NUMBER" :: $EXECUTOR_NUMBER
echo "NODE_NAME" :: $NODE_NAME
echo "NODE_LABELS" :: $NODE_LABELS
echo "WORKSPACE" :: $WORKSPACE
echo "JENKINS_HOME" :: $JENKINS_HOME
echo "JENKINS_URL" :: $JENKINS_URL
echo "BUILD_URL" ::$BUILD_URL
echo "JOB_URL" :: $JOB_URL
pwd
cd /home/ubuntu/suhui2019
git checkout master
git pull

cd /home/ubuntu/suhui2019/suhui-boot/suhui-boot-module-system
pwd
sudo JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 /home/ubuntu/apache-maven-3.6.2/bin/mvn clean package
cp /home/ubuntu/suhui2019/suhui-boot/suhui-boot-module-system/target/suhui-boot-module-system-2.0.1.jar /home/ubuntu/

ls -al
process_id=$(ps -ef | grep suhui-boot-module-system-2.0.1.jar | grep -v "grep" | awk '{print $2}')
# 如果该项目正在运行，就杀死项目进程
if [ -n "$process_id" ]
then 
	echo "stop old suhui-boot-module-system-2.0.1.jar"
    sudo kill -9 $process_id
else 
	echo "suhui-boot-module-system-2.0.1.jar not started yet; no need to kill it"
fi

#!/bin/bash
sudo su root -c "nohup java -jar -Xms64m -Xmx512m /home/ubuntu/suhui-boot-module-system-2.0.1.jar >/home/ubuntu/suhui.log &"



#### draft only
for i in {1,2,3}    #
    do
	sudo su -s /bin/bash jenkins
	nohup java -jar -Xms64m -Xmx512m /home/ubuntu/suhui-boot-module-system-2.0.1.jar >/home/ubuntu/suhui.log &   
done


sudo nohup java -Xms64m -Xmx512m -jar  /home/ubuntu/suhui-boot-module-system-2.0.1.jar >/home/ubuntu/suhui.log & 


# 第一次运行前要在这个目录下（/home/ubuntu/suhui2019/suhui-boot/）执行 `mvn clean install`以生成项目必要的jar包 
# org.jeecgframework.boot:suhui-boot-module-system:jar:2.0.1

# wget http://mirror.rise.ph/apache/maven/maven-3/3.6.2/binaries/apache-maven-3.6.2-bin.tar.gz

# 需要在Jenkins中配置好JDK的位置（http://3.93.15.101:11000/configureTools/）。否则会容易出现这个错误：
# No compiler is provided in this environment. Perhaps you are running on a JRE rather than a JDK?
# https://stackoverflow.com/questions/36622142/maven-builds-with-wrong-jdk-on-jenkins/36623758
# 有时候，还要移除openJDK
# sudo apt-get install openjdk-8-jdk    # 不要安装错误，千万不要安jre版本

# 如果修改了配置，例如Redis的端口的配置，那么需要重新编译整个项目，也就是需要登录到Java服务器上，在parent层次（suhui-boot目录）下，使用sudo JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 /home/ubuntu/apache-maven-3.6.2/bin/mvn clean install

```



## Redis

重启服务器后要注意开启Redis
先杀死老的redis，然后重启（设置6380端口），
系统上可能有多个Redis安装，因此用下面这个命令关掉Redis（这个Redis没有配置好）
sudo /etc/init.d/redis-server stop

然后使用下面这个命令打开正常配置的Redis
# start
sudo su root -c "nohup /usr/local/redis/bin/redis-server /usr/local/redis/bin/redis.conf  --port 6380 &"


```sh

sudo lsof -i -P -n | grep LISTEN

/usr/local/redis/bin/redis-cli -h 127.0.0.1 -p 6380 -a suhuiadmin




redis pass:suhuiadmin

# see all keys in Redis
KEYS * 

# set key 123
set key 123


```



## start backend   

```
cd suhui-boot/suhui-boot-module-system
mvn clean install
java -jar target/suhui-boot-module-system-2.0.1.jar
### AWS上的命令
java -jar -Xms64m -Xmx512m target/suhui-boot-module-system-2.0.1.jar

/usr/local/jdk1.8.0_211/jre/bin/java -Djava.util.logging.config.file=/home/ubuntu/tomcat/conf/logging.properties -Dj

仅仅是这一个命令
# nohup java -jar -Xms64m -Xmx512m /root/mailCheck_fat.jar > /root/mail.log &


通过 http://localhost:3000 访问项目即可进入系统，默认账号密码： admin/123456

```

#### 目前的管理界面
```
http://3.93.15.101:3333   用户名密码：suhuiadmin   suhuiadmin

```

## start frontend    

linux

```
# first install yarn
curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | sudo apt-key add -
echo "deb https://dl.yarnpkg.com/debian/ stable main" | sudo tee /etc/apt/sources.list.d/yarn.list
sudo apt update
sudo apt install yarn
yarn  add node-pre-gyp@0.10.0
yarn global add serve

yarn install
yarn upgrade caniuse-lite browserslist

yarn run serve


yarn run build


# 这个不用
yarn run serve


```


```#
# first install npm
sudo apt install npm

# 
cd ant-design-suhui-vue
npm install
npm run build

```

mac
```
cd ant-design-suhui-vue

brew install yarn
-- yarn 1.19.1
-- auto install Node v12.12.0
yarn upgrade

yarn global add node-pre-gyp@0.10.0

yarn global add serve
yarn install
yarn run serve


# install nginx
brew install nginx 

## start nginx
launchctl load /usr/local/Cellar/nginx/1.17.3_1/homebrew.mxcl.nginx.plist  

## stop nginx
launchctl unload /usr/local/Cellar/nginx/1.17.3_1/homebrew.mxcl.nginx.plist  

# install redis
brew install redis

## Start Redis server using configuration file.
redis-server /usr/local/etc/redis.conf

## stop redis  -- mac
redis-cli shutdown
### Try killall redis-server. You may also use ps aux to find the name and pid of your server, and then kill it with kill -9 here_pid_number.

```

因为Vue设置的代理，这里需要先启动后端--即上文的 start backend     
```
yarn run build

yarn run serve


```


## JeecgBoot 开发文档   
```
http://doc.jeecg.com/1273752
http://jeecg-boot.mydoc.io/?t=345670
```

#### Installation

1. xxxx
2. xxxx
3. xxxx

#### Instructions

1. xxxx
2. xxxx
3. xxxx

#### Contribution

1. Fork the repository
2. Create Feat_xxx branch
3. Commit your code
4. Create Pull Request

# test in master
# test in master
# test in master
