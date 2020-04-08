package org.suhui.modules.api.controller;

import com.alibaba.fastjson.JSONObject;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.suhui.common.constant.CommonConstant;
import org.suhui.common.api.vo.Result;
import org.suhui.common.system.query.QueryGenerator;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.suhui.suhui.entity.PayCurrencyType;
import org.suhui.modules.suhui.suhui.service.IPayCurrencyTypeService;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 账户类型（account_type_code） 可以在数据库文档里面找到
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="channel_type_code")
@RestController
@RequestMapping("/api/login/ChannelTypeCode")

public class ApiChannelTypeCodeController {

  /**
   *
   * @param request
   * @param response
   * @return
   */
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public Result<JSONObject> getList(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
       
        List<String> channelList = new ArrayList<>();
        JSONObject channel = new JSONObject();

        ////////////////////////////所有电子钱包
        // 中国的电子钱包        
        channel.put("channelAreacode", "+86");   
        
        channel.put("channelType", "1");
        channel.put("channelName", "支付宝");
        channel.put("channelNameLong", "支付宝(中国)+86");
        channel.put("notes", "digital wallet");   
        channelList.add(channel.toString());
        ////////////////////////////所有银行
        // 中国的银行
        channel.put("channelAreacode", "+86");   

        channel.put("notes", "bank account");   
        channel.put("channelType", "101");
        channel.put("channelName", "工商银行");
        channel.put("channelNameLong", "工商银行(中国)+86");
        channelList.add(channel.toString());
        channel.put("channelType", "102");
        channel.put("channelName", "中国银行");
        channel.put("channelNameLong", "中国银行(中国)+86");
        channelList.add(channel.toString());
        channel.put("channelType", "103");
        channel.put("channelName", "农业银行");
        channel.put("channelNameLong", "农业银行(中国)+86");
        channelList.add(channel.toString());
        channel.put("channelType", "104");
        channel.put("channelName", "建设银行");
        channel.put("channelNameLong", "建设银行(中国)+86");
        channelList.add(channel.toString());
        channel.put("channelType", "105");
        channel.put("channelName", "招商银行");
        channel.put("channelNameLong", "招商银行(中国)+86");
        channelList.add(channel.toString());
        channel.put("channelType", "106");
        channel.put("channelName", "民生银行");
        channel.put("channelNameLong", "民生银行(中国)+86");
        channelList.add(channel.toString());

        // 韩国的银行
        channel.put("channelAreacode", "+82");   

        channel.put("channelType", "201");
        channel.put("channelName", "有利银行");
        channel.put("channelNameLong", "有利银行(韩国)+82");
        channelList.add(channel.toString());
        channel.put("channelType", "202");
        channel.put("channelName", "国民银行");
        channel.put("channelNameLong", "国民银行(韩国)+82");
        channelList.add(channel.toString());

        obj.put("channelList", channelList);

        result.setResult(obj);
        result.success("get ChannelTypeCodeList success");
        //result.setCode(CommonConstant.SC_OK_200);
        result.setCode(200);
        return result;
    }


}
