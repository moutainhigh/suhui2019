package org.suhui.modules.toB.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.suhui.common.api.vo.Result;
import org.suhui.common.constant.CommonConstant;
import org.suhui.common.util.PasswordUtil;
import org.suhui.common.util.UUIDGenerator;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.toB.entity.OrderAssurer;
import org.suhui.modules.toB.entity.OrderAssurerMoneyChange;
import org.suhui.modules.toB.entity.PayUserInfo;
import org.suhui.modules.toB.entity.PayUserLogin;
import org.suhui.modules.toB.mapper.OrderAssurerMapper;
import org.suhui.modules.toB.service.IOrderAssurerMoneyChangeService;
import org.suhui.modules.toB.service.IOrderAssurerService;
import org.suhui.modules.toB.service.IPayUserInfoService;
import org.suhui.modules.toB.service.IPayUserLoginService;
import org.suhui.modules.utils.BaseUtil;

/**
 * 类说明：承兑商相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-07 22:30
 **/
@Service
public class OrderAssurerServiceImpl extends ServiceImpl<OrderAssurerMapper, OrderAssurer> implements IOrderAssurerService {

    @Autowired
    private IPayUserLoginService iPayUserLoginService;

    @Autowired
    private IPayUserInfoService iPayUserInfoService;

    @Autowired
    private IOrderAssurerMoneyChangeService iOrderAssurerMoneyChangeService;

    /**
     * 添加
     *
     * @param
     * @return
     */
    @Override
    public OrderAssurer addAssurerMain(JSONObject data) {
        System.out.println(data);
        String phone = data.getString("phone");
        String pwd = data.getString("password");
        String areacode = data.getString("areacode");
        String countryCode = data.getString("countryCode");
        String assurerName = data.getString("assurerName");
        String ensureProportion = data.getString("ensureProportion");

        String userno = UUIDGenerator.generate();
        // 盐值
        String salt = oConvertUtils.randomGen(8);
        // 密码加密
        String passwordEncode = PasswordUtil.encrypt(phone, pwd + "", salt);
        PayUserLogin payUserLogin = new PayUserLogin();
        payUserLogin.setLoginName(phone);
        payUserLogin.setPassword(passwordEncode);
        payUserLogin.setSalt(salt);
        // 设置有效状态  状态 0-默认 1-有效 2-无效
        payUserLogin.setStatus(0);
        payUserLogin.setUserNo(userno); // 通过生成的uuid
        payUserLogin.setAreacode(areacode);
        payUserLogin.setUserType(CommonConstant.usertype_toB_acceptor);
        iPayUserLoginService.save(payUserLogin);
        PayUserInfo payUserInfo = new PayUserInfo();
        payUserInfo.setUserNo(userno);
        payUserInfo.setPhoneNo(phone);
        payUserInfo.setUserType(CommonConstant.usertype_toB_acceptor);
        iPayUserInfoService.save(payUserInfo);
        OrderAssurer orderAssurer = new OrderAssurer();
        orderAssurer.setUserNo(userno);
        orderAssurer.setAssurerName(assurerName);
        orderAssurer.setCountryCode(countryCode);
        orderAssurer.setAssurerPhone(phone);
        orderAssurer.setAssurerRate(data.getDouble("assurerRate"));
        orderAssurer.setTotalLimit(data.getDouble("totalLimit") * 100);
        orderAssurer.setCanUseLimit(data.getDouble("totalLimit") * 100);
        orderAssurer.setEnsureProportion(data.getDouble("ensureProportion"));
        this.save(orderAssurer);
        return orderAssurer;
    }

    /**
     * 批量审核
     *
     * @param
     * @return
     */
    @Override
    public Result<Object> auditPassAssurer(String ids) {
        String[] idArr = ids.split(",");
        Result<Object> result = new Result<>();
        boolean check = true;
        for (String orderId : idArr) {
            OrderAssurer orderAssurer = getById(orderId);
            if (!BaseUtil.Base_HasValue(orderAssurer)) {
                result = Result.error(513, "承兑商不存在");
                check = false;
                break;
            }
            if (!orderAssurer.getAssurerState().equals("to_audit")) {
                result = Result.error(514, "【待审核】状态的承兑商可执行该操作");
                check = false;
                break;
            }
            if (orderAssurer.getTotalLimit() <= 0) {
                result = Result.error(514, "承兑商限额必须大于0");
                check = false;
                break;
            }
            if(!BaseUtil.Base_HasValue(orderAssurer.getCardType())){
                result = Result.error(515, "承兑商缺少实名认证信息");
                check = false;
                break;
            }
            if(!BaseUtil.Base_HasValue(orderAssurer.getCardNo())){
                result = Result.error(515, "承兑商缺少实名认证信息");
                check = false;
                break;
            }
            if(!BaseUtil.Base_HasValue(orderAssurer.getCardFrontPicture())){
                result = Result.error(515, "承兑商缺少实名认证信息");
                check = false;
                break;
            }
            if(!BaseUtil.Base_HasValue(orderAssurer.getCardBackPicture())){
                result = Result.error(515, "承兑商缺少实名认证信息");
                check = false;
                break;
            }
            orderAssurer.setAssurerState("normal");
            updateById(orderAssurer);
        }
        if (!check) {
            return result;
        }
        return Result.ok("操作成功");
    }

    /**
     * 更改 保证金、租赁金
     *
     * @param
     * @return
     */
    @Override
    public Result<Object> changeAssurerMoney(JSONObject jsonObject) {
        String assurerId = jsonObject.getString("assurerId");
        String classChange = jsonObject.getString("classChange");
        String typeChange = jsonObject.getString("typeChange");
        Double changeMoney = jsonObject.getDouble("changeMoney");
        String changeText = jsonObject.getString("changeText");
        OrderAssurer orderAssurer = getById(assurerId);
        if (!BaseUtil.Base_HasValue(orderAssurer)) {
            return Result.error("该承兑商不存在");
        }
        // 保证金
        if ("ensure".equals(classChange)) {
            if ("sub".equals(typeChange)) {
                if (orderAssurer.getEnsureMoney() < changeMoney * 100) {
                    return Result.error("保证金额度不足");
                }
            }
        } else if ("lease".equals(classChange)) { // 租赁金
            if ("sub".equals(typeChange)) {
                if (orderAssurer.getLeaseMoney() < changeMoney * 100) {
                    return Result.error("租赁金额度不足");
                }
            }
        }
        OrderAssurerMoneyChange orderAssurerMoneyChange = new OrderAssurerMoneyChange();
        orderAssurerMoneyChange.setAssurerId(assurerId);
        orderAssurerMoneyChange.setAssurerName(orderAssurer.getAssurerName());
        orderAssurerMoneyChange.setAssurerPhone(orderAssurer.getAssurerPhone());
        orderAssurerMoneyChange.setChangeClass(classChange);
        orderAssurerMoneyChange.setChangeType(typeChange);
        orderAssurerMoneyChange.setChangeMoney(changeMoney);
        orderAssurerMoneyChange.setChangeText(changeText);
        iOrderAssurerMoneyChangeService.save(orderAssurerMoneyChange);
        return Result.ok("执行成功");
    }


}
