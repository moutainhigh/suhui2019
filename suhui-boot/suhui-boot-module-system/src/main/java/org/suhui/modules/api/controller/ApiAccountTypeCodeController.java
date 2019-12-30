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
import org.suhui.common.constant.CurrencyConstant;
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
@Api(tags="account_type_code")
@RestController
@RequestMapping("/api/login/accountTypeCode")

public class ApiAccountTypeCodeController {

  /**
   *
   * @param request
   * @param response
   * @return
   */
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public Result<JSONObject> add(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();

        List<Map<String,String>> listRtn =new ArrayList<Map<String,String>>();

        Map<String, String> mapdb = new HashMap<String,String>();

        mapdb.put("CNY", CurrencyConstant.CNY);
        mapdb.put("USD", CurrencyConstant.USD);
        mapdb.put("PHP", CurrencyConstant.PHP);
        mapdb.put("KRW", CurrencyConstant.KRW);

        listRtn.add(mapdb) ;

        obj.put("data", listRtn) ;
        result.setResult(obj);
        result.success("getAccountTypeCode success");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }

}
