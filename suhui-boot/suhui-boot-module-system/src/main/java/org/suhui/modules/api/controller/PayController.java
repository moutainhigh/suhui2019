package org.suhui.modules.api.controller;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.suhui.modules.api.model.PaySaPi;
import org.suhui.modules.api.utils.HttpClientUtils;
import org.suhui.modules.api.utils.PayUtil;


@Controller
@RequestMapping("/pays")
public class PayController {

	/**
	 * 支付接口   发起本接口前  需要先处理本地数据库 把对应数据存储到数据库  根据不同类型回调
	 * @param request
	 * @param price
	 * @param istype
	 * @param orderuid
	 * @param goodsname
	 * @return
	 */
	@RequestMapping("/pay")
	@ResponseBody
	public Map<String, Object> pay(HttpServletRequest request, String price, int istype,
								   String orderuid ,String goodsname ) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> remoteMap = new HashMap<String, Object>();

		remoteMap.put("price", price);
		remoteMap.put("istype", istype);
		remoteMap.put("orderid", PayUtil.getOrderIdByUUId());
		remoteMap.put("orderuid", orderuid); // 可以传递用户 id  方便查询那个用户的信息
		remoteMap.put("goodsname", goodsname);

		resultMap.put("data", PayUtil.payOrder(remoteMap));


		String url = "https://pay.sxhhjc.cn/?format=json" ;
		String resultStr = HttpClientUtils.doPostRequest(url,resultMap) ;

		Map<String, Object> returnMap = (Map<String, Object>) JSON.parse(resultStr);

		return returnMap;
	}

	/**
	 * 收款通知
	 * @param request
	 * @param response
	 * @param paySaPi
	 */
	@RequestMapping("/notifyPay")
	public void notifyPay(HttpServletRequest request, HttpServletResponse response, PaySaPi paySaPi) {
		// 保证密钥一致性
		if (PayUtil.checkPayKey(paySaPi)) {
			// TODO 做自己想做的
			System.out.println("转账数据");
		} else {
			// TODO 该怎么做就怎么做
			System.out.println("监测不对");
		}
	}

	/**
	 * 返回界面   支付成功后 返回到付款界面
	 * @param request
	 * @param response
	 * @param orderid
	 * @return
	 */
	@RequestMapping("/returnPay")
	public ModelAndView returnPay(HttpServletRequest request, HttpServletResponse response, String orderid) {
		boolean isTrue = false;
		ModelAndView view = null;
		// 根据订单号查找相应的记录:根据结果跳转到不同的页面


		System.out.println("returnPay");
		if (isTrue) {
			view = new ModelAndView("/正确的跳转地址");
		} else {
			view = new ModelAndView("/没有支付成功的地址");
		}
		return view;
	}
}
