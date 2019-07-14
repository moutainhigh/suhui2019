package org.suhui.modules.api.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.suhui.common.api.vo.Result;
import org.suhui.common.constant.CommonConstant;
import org.suhui.common.system.api.ISysBaseAPI;
import org.suhui.common.system.util.JwtUtil;
import org.suhui.common.system.vo.LoginUser;
import org.suhui.common.util.PasswordUtil;
import org.suhui.common.util.RedisUtil;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.shiro.vo.DefContants;
import org.suhui.modules.system.entity.SysDepart;
import org.suhui.modules.system.entity.SysUser;
import org.suhui.modules.system.service.ISysDepartService;
import org.suhui.modules.system.service.ISysLogService;
import org.suhui.modules.system.service.ISysUserService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.*;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/api/v1/system")
@Api(tags="用户登录")
@Slf4j
public class ApiSystemController {
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private ISysLogService logService;
	@Autowired
    private RedisUtil redisUtil;
	@Autowired
    private ISysDepartService sysDepartService;

	/**
	 * 获取验证码
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/sms" , method = RequestMethod.POST)
	public Result<JSONObject> sms(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params) {
		//用户退出逻辑
		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();

		Random random = new Random();
		int x = random.nextInt(899999);
		x = x+100000;
		System.out.println(x+"");

		String phone = params.get("phone")+"" ;
		HttpSession session = request.getSession();
		session.setAttribute("smsCode_"+phone ,x);
		session.setMaxInactiveInterval(300);

		obj.put("smsCode", x+"");
		result.setResult(obj);
		result.success("发送验证码成功");
		result.setCode(CommonConstant.SC_OK_200);
		return result ;
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
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.port", "465");//端口
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.ssl.enable", "true");


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
		result.success("发送验证码成功");
		result.setCode(CommonConstant.SC_OK_200);
		return result ;
	}

}
