package org.suhui.modules.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.suhui.common.api.vo.Result;
import org.suhui.common.constant.CommonConstant;
import org.suhui.common.system.util.JwtUtil;
import org.suhui.common.system.vo.LoginUser;
import org.suhui.common.util.RedisUtil;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.suhui.suhui.service.IPayUserInfoService;
import org.suhui.modules.suhui.suhui.service.IPayUserLoginService;
import org.suhui.modules.system.entity.SysUser;
import org.suhui.modules.system.service.ISysUserService;
import org.suhui.modules.utils.SmsUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import org.suhui.modules.suhui.suhui.entity.*;

@RestController
@RequestMapping("/api/system")
@Api(tags="用户登录")
@Slf4j
public class AppSystemApiController {

    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

    @Autowired
    private IPayUserInfoService iPayUserInfoService ;

    @Autowired
    @Lazy
    private ISysUserService sysUserService;

    @Autowired
    @Lazy
    private IPayUserLoginService iPayUserLoginService ;

    @Autowired
    @Lazy
    private RedisUtil redisUtil;
    /**
     * 获取验证码
     * @param params
     * @return
     */
    @RequestMapping(value = "/sms" , method = RequestMethod.POST)
    public Result<JSONObject> sms(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();

        Random random = new Random();
        int x = random.nextInt(899999);
        x = x+100000;
        System.out.println(x+"");

        String phone = params.get("phone")+"" ;
        String areaCode = params.get("areacode") + "" ;
        HttpSession session = request.getSession();
        session.setAttribute("smsCode_"+phone ,x);
        session.setMaxInactiveInterval(300);
        String rtn ="" ;
        //  areaCode 发送类型：1：国内  2：国际
        if(areaCode.equals("+86")){
            rtn = 	SmsUtil.sendSms(phone,x+"" , 1);
        }else{
            rtn = SmsUtil.sendSms(areaCode+phone,x+"" , 2);
        }
        //obj.put("smsCode", x+"");
        JSONObject object = JSONObject.parseObject(rtn) ;
        String status = (String)object.get("status") ;
        if(status.equals("success")){
            result.setResult(obj);
            result.success("发送验证码成功 Verification code sent successfully" );
            result.setCode(CommonConstant.SC_OK_200);
            return result ;
        }else{
            result.setResult(obj);
            result.success("验证码发送失败 failed when sending verification code ");
            result.setCode(530);
            return result ;
        }

    }

    /**
     * 获取验证码
     * @param params
     * @return
     */
    @RequestMapping(value = "/email" , method = RequestMethod.POST)
    public Result<JSONObject> email(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();

        Random random = new Random();
        int x = random.nextInt(899999);
        x = x+100000;
        System.out.println(x+"");
        String email = params.get("email")+"" ;

        //创建连接对象 连接到邮件服务器
        Properties properties = new Properties();
        //设置发送邮件的基本参数
        //发送邮件服务器
        properties.setProperty("mail.transport.protocol", "smtp");//使用的协议
        properties.setProperty("mail.smtp.host", "cloud.mysubmail.com");
        properties.setProperty("mail.smtp.port", "25");//端口
//		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.auth", "true");
//		properties.setProperty("mail.smtp.ssl.enable", "true");


        //设置发送邮件的账号和密码
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //两个参数分别是发送邮件的账户和密码
                return new PasswordAuthentication("suhuicorp@gmail.com","Su@@##2019hu");
            }
        });
        try {
            //创建邮件对象
            Message message = new MimeMessage(session);
            //设置发件人
            message.setFrom(new InternetAddress("suhuicorp@gmail.com"));
            //设置收件人
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(email));
            //设置主题
            message.setSubject("这是一份测试邮件");
            //设置邮件正文  第二个参数是邮件发送的类型
            message.setContent("email code >>>"+x,"text/html;charset=UTF-8");
            //发送一封邮件
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        HttpSession sessionHttp = request.getSession();
        sessionHttp.setAttribute("smsCode_"+email ,x);
        sessionHttp.setMaxInactiveInterval(30);

        obj.put("smsCode", x+"");
        result.setResult(obj);
        result.success("发送验证码成功 Verification code sent successfully");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }

    /**
     * 上传个人图像
     * @param
     * @return
     */
    @RequestMapping(value = "/upload-img" , method = RequestMethod.POST)
    public Result<?> uploadImg(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params) {
        Result<?> result = new Result<>();
        try {
            String ctxPath = uploadpath;
            String fileName = null;
            String bizPath = "files";
            String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile mf = multipartRequest.getFile("file");// 获取上传文件对象
            String orgName = mf.getOriginalFilename();// 获取文件名
            fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
            String savePath = file.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), savefile);
            String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
            if (dbpath.contains("\\")) {
                dbpath = dbpath.replace("\\", "/");
            }
            String id = params.get("id")+"" ;

            PayUserInfo payUserInfo = iPayUserInfoService.getById(id) ;

            if(payUserInfo==null) {
                result.success("has no user");
                result.setCode(517);
            }else{
                payUserInfo.setPicture(dbpath);
                iPayUserInfoService.updateById(payUserInfo) ;
                result.success("update avatar success!");
                result.setCode(CommonConstant.SC_OK_200);
            }

        } catch (IOException e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return result;
    }


//    /**
//     * 上传个人图像
//     * @param
//     * @return
//     */
//    @RequestMapping(value = "/upload-img_new" , method = RequestMethod.POST)
//    public Result<?> uploadImgNew(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params) {
//        Result<?> result = new Result<>();
//        try {
//            String ctxPath = uploadpath;
//            String fileName = null;
//            String bizPath = "files";
//            String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
//            File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
//            if (!file.exists()) {
//                file.mkdirs();// 创建文件根目录
//            }
//
//            String fileData = params.get("file") +"";
//            fileName = params.get("fileName")+"";
//            String savePath = file.getPath() + File.separator + fileName;
//            File savefile = new File(savePath);
//            FileCopyUtils.copy(fileData.getBytes(), savefile);
//            String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
//            if (dbpath.contains("\\")) {
//                dbpath = dbpath.replace("\\", "/");
//            }
//            String id = params.get("id")+"" ;
//
//            PayUserInfo payUserInfo = iPayUserInfoService.getById(id) ;
//
//            if(payUserInfo==null) {
//                result.success("has no user");
//                result.setCode(0);
//            }else{
//                payUserInfo.setPicture(dbpath);
//                iPayUserInfoService.updateById(payUserInfo) ;
//                result.success("update avatar success!");
//                result.setCode(CommonConstant.SC_OK_200);
//            }
//
//        } catch (IOException e) {
//            result.setSuccess(false);
//            result.setMessage(e.getMessage());
//            log.error(e.getMessage(), e);
//        }
//        return result;
//    }

    /**
     * 上传个人图像
     * @param
     * @return
     */
    @RequestMapping(value = "/upload-img_new" , method = RequestMethod.POST)
    public Result<?> uploadImgNew(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params) {
        Result<?> result = new Result<>();

        String fileData = params.get("file") +"";

        String id = "" ;
        String userno = params.get("userno")+"" ;
        String usertype = params.get("usertype")+"" ;
        if(usertype == null || usertype.equals("")|| usertype.equals("null")){
            result.success("please check usertype");
            result.setCode(519);
            return result;
        }
        PayUserInfo payUserInfoPararm = new PayUserInfo() ;
        payUserInfoPararm.setUserNo(userno);
        payUserInfoPararm.setUserType(Integer.parseInt(usertype));
        PayUserInfo payUserInfoDb = iPayUserInfoService.getOne(new QueryWrapper<PayUserInfo>(payUserInfoPararm));
        id = payUserInfoDb.getId()+"" ;

        PayUserInfo payUserInfo = iPayUserInfoService.getById(id) ;

        if(payUserInfo==null) {
            result.success("has no user");
            result.setCode(517);
        }else{
            payUserInfo.setPicture(fileData);
            iPayUserInfoService.updateById(payUserInfo) ;
            result.success("update avatar success!");
            result.setCode(CommonConstant.SC_OK_200);
        }

        return result;
    }

    /**
     * 上传个人图像
     * @param
     * @return
     */
    @RequestMapping(value = "/getpayuserinfo" , method = RequestMethod.POST)
    public Result getPayUserInfo(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params) {
        Result result = new Result();



        String userno = params.get("userno")+"" ;
        String usertype = params.get("usertype")+"" ;
        if(usertype == null || usertype.equals("")|| usertype.equals("null")){
            result.success("please check usertype");
            result.setCode(519);
            return result;
        }
        PayUserInfo payUserInfoPararm = new PayUserInfo() ;
        payUserInfoPararm.setUserNo(userno);
        payUserInfoPararm.setUserType(Integer.parseInt(usertype));
        PayUserInfo payUserInfoDb = iPayUserInfoService.getOne(new QueryWrapper<PayUserInfo>(payUserInfoPararm));

        File savefile = new File("");
        try {
            FileCopyUtils.copy( payUserInfoDb.getPicture().getBytes(), savefile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(payUserInfoDb==null) {
            result.success("has no user");
            result.setCode(517);
        }else{

            result.setResult("" +payUserInfoDb.getPicture());
            result.success("update avatar success!");
            result.setCode(CommonConstant.SC_OK_200);
        }

        return result;
    }



/**
 * 判断是否登陆
 * @param
 * @return
 */
    @RequestMapping(value = "/isLogin" , method = RequestMethod.POST)
    public Result isLogin(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params) {
        Result result = new Result();

        String token = params.get("token")+"" ;
        String usernameparam = params.get("phone")+"" ; // 用户名
        String areacode = params.get("areacode")+"" ;
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            result.setResult("token非法无效");
            return result;
        }

        LoginUser loginUser = new LoginUser();
        SysUser sysUser = sysUserService.getUserByName(username);
        if(sysUser == null){
            PayUserLogin payUserLogin = iPayUserLoginService.getUserByPhone(username,areacode) ;

            if (!jwtTokenRefresh(token, username, payUserLogin.getPassword())) {
//                throw new AuthenticationException("Token失效，请重新登录!");
                result.setResult("Token失效，请重新登录!");
                result.setCode(560);
                return result;
            }

            // 判断用户状态
            if (payUserLogin.getStatus() == 2) {
//                throw new AuthenticationException("账号无效,请联系管理员!");
                result.setResult("账号无效,请联系管理员!");
                result.setCode(561);
                return result;
            }
            BeanUtils.copyProperties(payUserLogin, loginUser);
        }else{
            // 校验token是否超时失效 & 或者账号密码是否错误
            if (!jwtTokenRefresh(token, username, sysUser.getPassword())) {
//            throw new AuthenticationException("Token失效，请重新登录!");
                result.setResult("Token失效，请重新登录!");
                result.setCode(560);
                return result;
            }

            // 判断用户状态
            if (sysUser.getStatus() != 1) {
//            throw new AuthenticationException("账号已被锁定,请联系管理员!");
                result.setResult("账号无效,请联系管理员!");
                result.setCode(561);
                return result;
            }

            BeanUtils.copyProperties(sysUser, loginUser);
        }

        result.setCode(200);
        result.setResult("success");
        result.setMessage("已登陆状态");
        return result;
    }


    /**
     * JWTToken刷新生命周期 （解决用户一直在线操作，提供Token失效问题）
     * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)
     * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
     * 3、当该用户这次请求JWTToken值还在生命周期内，则会通过重新PUT的方式k、v都为Token值，缓存中的token值生命周期时间重新计算(这时候k、v值一样)
     * 4、当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，则表示该用户一直在操作只是JWT的token失效了，程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
     * 5、当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
     * 6、每次当返回为true情况下，都会给Response的Header中设置Authorization，该Authorization映射的v为cache对应的v值。
     * 7、注：当前端接收到Response的Header中的Authorization值会存储起来，作为以后请求token使用
     * 参考方案：https://blog.csdn.net/qq394829044/article/details/82763936
     *
     * @param userName
     * @param passWord
     * @return
     */
    public boolean

    jwtTokenRefresh(String token, String userName, String passWord) {
        String cacheToken = String.valueOf(redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + token));
        if (oConvertUtils.isNotEmpty(cacheToken)) {
            // 校验token有效性
            if (!JwtUtil.verify(token, userName, passWord)) {
                String newAuthorization = JwtUtil.sign(userName, passWord);
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, newAuthorization);
                // 设置超时时间
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
            } else {
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, cacheToken);
                // 设置超时时间
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
            }
            return true;
        }
        return false;
    }
}
