package org.suhui.modules.api.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.suhui.common.api.vo.Result;
import org.suhui.common.constant.CommonConstant;
import org.suhui.modules.suhui.suhui.service.IPayUserInfoService;
import org.suhui.modules.system.entity.SysUser;
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
            result.setCode(0);
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
                result.setCode(0);
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

        String id = params.get("id")+"" ;

        PayUserInfo payUserInfo = iPayUserInfoService.getById(id) ;

        if(payUserInfo==null) {
            result.success("has no user");
            result.setCode(0);
        }else{
            payUserInfo.setPicture(fileData);
            iPayUserInfoService.updateById(payUserInfo) ;
            result.success("update avatar success!");
            result.setCode(CommonConstant.SC_OK_200);
        }

        return result;
    }
}
