package org.suhui.modules.api.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.suhui.common.api.vo.Result;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.modules.pay.entity.UserChargeRecord;
import org.suhui.modules.pay.service.IUserChargeRecordService;
import org.suhui.modules.system.entity.SysUser;
import org.suhui.modules.system.service.ISysUserService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Slf4j
@Api(tags="支付中心")
@RestController
@RequestMapping("/api/pay")
public class ApiPayController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IUserChargeRecordService userChargeRecordService;

    /**
     *   添加
     * @param params
     * @return
     */
    @AutoLog(value = "充值")
    @ApiOperation(value="充值-添加", notes="充值-添加")
    @RequestMapping(value = "/charge",method = RequestMethod.POST)
    public Result<JSONObject> add(HttpServletRequest request, @RequestParam Map<String, Object> params ){
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        int userId = (int)params.get("userId") ;
        int chargeType = (int)params.get("chargeType");
        BigDecimal amount=(BigDecimal)params.get("amount");
        String payPassword=params.get("payPassword")+"";
        int currencyType=(int)params.get("currencyType");
        SysUser sysUser = sysUserService.getById(userId) ;

        if(sysUser==null) {
            result.setResult(obj);
            result.success("has no user");
            result.setCode(0);
        }
        //check pay
        if(!StringUtils.equals(sysUser.getPayPassword(),payPassword)){
            result.setResult(obj);
            result.success("payPassword is wrong");
            result.setCode(0);
        }
        //save
        UserChargeRecord record=new UserChargeRecord();
        record.setUserId(userId);
        record.setCreateTime(new Date());
        record.setAmount(amount);
        record.setChargeType(chargeType);
        record.setCurrencyType(currencyType);
        record.setAmount(amount);
        userChargeRecordService.save(record);
        //edit user amout

        //


        return result ;
    }
}
