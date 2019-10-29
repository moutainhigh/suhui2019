
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

nohup java -jar jenkins.war --httpPort=11000 > jenkins.log &

`java -jar jenkins.war --httpPort=11000 > jenkins.log` & disown

your_command & disown

nohup java -jar jenkins.war --httpPort=11000 &

userName and Pass:suhuiadmin/suhuiadmin



```

## start backend   

```
cd suhui-boot/suhui-boot-module-system
mvn clean install
java -jar target/suhui-boot-module-system-2.0.1.jar
### AWS上的命令
java -jar -Xms64m -Xmx512m /home/ubuntu/suhui-boot-module-system-2.0.1.jar
/usr/local/jdk1.8.0_211/jre/bin/java -Djava.util.logging.config.file=/home/ubuntu/tomcat/conf/logging.properties -Dj


通过 http://localhost:3000 访问项目即可进入系统，默认账号密码： admin/123456
```

#### 目前的管理界面
```
http://3.93.15.101:3333   用户名密码：suhuiadmin   suhuiadmin

```

## start frontend    



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


#### Gitee Feature

1. You can use Readme\_XXX.md to support different languages, such as Readme\_en.md, Readme\_zh.md
2. Gitee blog [blog.gitee.com](https://blog.gitee.com)
3. Explore open source project [https://gitee.com/explore](https://gitee.com/explore)
4. The most valuable open source project [GVP](https://gitee.com/gvp)
5. The manual of Gitee [https://gitee.com/help](https://gitee.com/help)
6. The most popular members  [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)