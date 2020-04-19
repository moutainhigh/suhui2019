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
import org.suhui.modules.toB.entity.*;
import org.suhui.modules.toB.mapper.OrderAssurerMapper;
import org.suhui.modules.toB.mapper.OrderMainMapper;
import org.suhui.modules.toB.service.IOrderAssurerMoneyChangeService;
import org.suhui.modules.toB.service.IOrderAssurerService;
import org.suhui.modules.toB.service.IPayUserInfoService;
import org.suhui.modules.toB.service.IPayUserLoginService;
import org.suhui.modules.utils.BaseUtil;

import java.util.*;

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

    @Autowired
    private OrderAssurerMapper orderAssurerMapper;

    @Autowired
    private OrderAssurerAccountServiceImpl orderAssurerAccountServiceImpl;
    @Autowired
    private OrderMainMapper orderMainMapper;

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
        String assurerRate = data.getString("assurerRate");
        String totalLimit = data.getString("totalLimit");
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
        orderAssurer.setAssurerRate(data.getDouble(assurerRate));
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
        for (String id : idArr) {
            OrderAssurer orderAssurer = getById(id);
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
            if (!BaseUtil.Base_HasValue(orderAssurer.getCardType())) {
                result = Result.error(515, "承兑商缺少实名认证信息");
                check = false;
                break;
            }
            if (!BaseUtil.Base_HasValue(orderAssurer.getCardNo())) {
                result = Result.error(515, "承兑商缺少实名认证信息");
                check = false;
                break;
            }
            if (!BaseUtil.Base_HasValue(orderAssurer.getCardFrontPicture())) {
                result = Result.error(515, "承兑商缺少实名认证信息");
                check = false;
                break;
            }
            if (!BaseUtil.Base_HasValue(orderAssurer.getCardBackPicture())) {
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

    /**
     * 承兑商基本信息（userNo）
     *
     * @param
     * @return
     */
    @Override
    public OrderAssurer getAssurerByUserNo(String userNo) {
        OrderAssurer orderAssurer = orderAssurerMapper.getAssurerByUserNo(userNo);
        return orderAssurer;
    }


    /**
     * 为订单获取最优承兑商
     *
     * @param
     * @return
     */
    @Override
    public Map<String, Object> getAssurerByOrder(OrderMain orderMain, String targetAreaCode) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("order_money", orderMain.getAssurerCnyMoney());
        // 选择多个合适的承兑商
        List<OrderAssurer> orderAssurers = orderAssurerMapper.getAssurerByOrderData(paramMap);
        if (!BaseUtil.Base_HasValue(orderAssurers)) {
            resultMap.put("state", "error");
            resultMap.put("message", "未找到合适的承兑商");
            return resultMap;
        }
        // 按费率排序
        orderAssurers = this.orderAssurersList(orderAssurers);
        OrderAssurer orderAssurer = null;
        OrderAssurerAccount orderAssurerAccount = null;
        List<OrderAssurer> useList = new ArrayList<>();
        for (int i = 0; i < orderAssurers.size(); i++) {
            OrderAssurer assurer = orderAssurers.get(i);
            // 为承兑商选择一个支付账号,同时排除掉没有账号得承兑商和账号支付宝金额不足的承兑商
            OrderAssurerAccount account = orderAssurerAccountServiceImpl.getAssurerAccountByOrderPay(assurer.getId(), orderMain.getAssurerCnyMoney(), orderMain.getMerchantCollectionMethod(), targetAreaCode);
            if (BaseUtil.Base_HasValue(account)) {
                //判断承兑商保证金及租赁金是否足够
                if (checkAssurerLeaseEnsure(orderMain, assurer)) {
                    if (!BaseUtil.Base_HasValue(useList)) {
                        useList.add(assurer);
                    } else if (assurer.getAssurerRate().equals(useList.get(0).getAssurerRate())) {
                        // 如果与第一个承兑商费率一样，则随机选择承兑商
                        useList.add(assurer);
                    }
                }
            }
        }
        if (BaseUtil.Base_HasValue(useList)) {
            orderAssurer = useList.get(BaseUtil.getRandomInt(0, useList.size()));
            orderAssurerAccount = orderAssurerAccountServiceImpl.getAssurerAccountByOrderPay(orderAssurer.getId(), orderMain.getAssurerCnyMoney(), orderMain.getMerchantCollectionMethod(), targetAreaCode);
        }
        // 如果没找到账号,说明没有承兑商合适
        if (!BaseUtil.Base_HasValue(orderAssurerAccount)) {
            resultMap.put("state", "error");
            resultMap.put("message", "未找到合适的承兑商账户");
            return resultMap;
        }
//         为承兑商选择一个收款账户
//        OrderAssurerAccount orderAssurerAccountCollection = orderAssurerAccountServiceImpl.getAssurerAccountByOrderCollection(orderAssurer.getId(), orderMain.getMerchantCollectionMethod(), orderMain.getMerchantCollectionAreaCode());
        resultMap.put("state", "success");
        resultMap.put("orderAssurer", orderAssurer);
        resultMap.put("orderAssurerAccountPay", orderAssurerAccount);
//        resultMap.put("orderAssurerAccountCollection", orderAssurerAccountCollection);
        return resultMap;
    }

    /**
     * 为承兑商排序，目前默认以费率排序，后期可加入其它排序规则
     */
    public List<OrderAssurer> orderAssurersList(List<OrderAssurer> orderAssurers) {
        Collections.sort(orderAssurers, new Comparator<OrderAssurer>() {
            @Override
            public int compare(OrderAssurer a, OrderAssurer b) {
                if (a.getAssurerRate() > b.getAssurerRate())
                    return -1;
                else
                    return 1;
            }
        });
        return orderAssurers;
    }

    /**
     * 判断承兑商保证金及租赁金是否足够
     */
    public Boolean checkAssurerLeaseEnsure(OrderMain orderMain, OrderAssurer assurer) {
        // 租赁金需扣减金额
        Double leaseMoney = BaseUtil.mul(orderMain.getAssurerCnyMoney(), assurer.getAssurerRate(), 0);
        if (assurer.getLeaseMoney() < leaseMoney) {
            return false;
        }
        // 承兑商未完成订单总金额
        Double notFinishOrderMoney = orderMainMapper.sumAssurerNotFinishMoney(assurer.getId());
        if (!BaseUtil.Base_HasValue(notFinishOrderMoney)) {
            notFinishOrderMoney = 0.0;
        }
        // 如果未完成订单金额+该订单金额>承兑商保证金，承兑商不能接这单
        if (BaseUtil.add(notFinishOrderMoney, orderMain.getAssurerCnyMoney(), 0) > assurer.getEnsureMoney()) {
            return false;
        }
        return true;
    }
}
