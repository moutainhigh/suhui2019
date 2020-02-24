package org.suhui.modules.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.suhui.common.api.vo.Result;
import org.suhui.common.util.PasswordUtil;
import org.suhui.common.util.UUIDGenerator;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.order.entity.OrderAssurer;
import org.suhui.modules.order.entity.OrderAssurerAccount;
import org.suhui.modules.order.entity.OrderAssurerMoneyChange;
import org.suhui.modules.order.entity.OrderMain;
import org.suhui.modules.order.mapper.OrderAssurerAccountMapper;
import org.suhui.modules.order.mapper.OrderAssurerMapper;
import org.suhui.modules.order.mapper.OrderMainMapper;
import org.suhui.modules.order.service.IOrderAssurerMoneyChangeService;
import org.suhui.modules.order.service.IOrderAssurerService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.suhui.modules.suhui.suhui.entity.PayUserInfo;
import org.suhui.modules.suhui.suhui.entity.PayUserLogin;
import org.suhui.modules.suhui.suhui.service.IPayUserInfoService;
import org.suhui.modules.suhui.suhui.service.IPayUserLoginService;
import org.suhui.modules.utils.BaseUtil;

import java.io.Serializable;
import java.util.*;

/**
 * @Description: 去
 * @Author: jeecg-boot
 * @Date: 2019-12-29
 * @Version: V1.0
 */
@Service
public class OrderAssurerServiceImpl extends ServiceImpl<OrderAssurerMapper, OrderAssurer> implements IOrderAssurerService {

    @Autowired
    private OrderAssurerMapper orderAssurerMapper;
    @Autowired
    private OrderMainMapper orderMainMapper;
    @Autowired
    private OrderAssurerAccountMapper orderAssurerAccountMapper;

    @Autowired
    OrderAssurerAccountServiceImpl orderAssurerAccountService;


    @Autowired
    private IPayUserLoginService iPayUserLoginService;

    @Autowired
    private IPayUserInfoService iPayUserInfoService;

    @Autowired
    private IOrderAssurerMoneyChangeService iOrderAssurerMoneyChangeService;

    @Override
    @Transactional
    public void saveMain(OrderAssurer orderAssurer, List<OrderAssurerAccount> orderAssurerAccountList) {
        orderAssurerMapper.insert(orderAssurer);
        for (OrderAssurerAccount entity : orderAssurerAccountList) {
            //外键设置
            entity.setAssurerId(orderAssurer.getId());
            if (entity.getAccountType().equals("alipay")) {
                entity.setPayCanUseLimit(2000000.0);
                entity.setPayLimit(2000000.0);
            }
            orderAssurerAccountMapper.insert(entity);
        }
    }

    @Override
    @Transactional
    public void updateMain(OrderAssurer orderAssurer, List<OrderAssurerAccount> orderAssurerAccountList) {
        orderAssurerMapper.updateById(orderAssurer);

        //1.先删除子表数据
        orderAssurerAccountMapper.deleteByMainId(orderAssurer.getId());

        //2.子表数据重新插入
        for (OrderAssurerAccount entity : orderAssurerAccountList) {
            //外键设置
            entity.setAssurerId(orderAssurer.getId());
            orderAssurerAccountMapper.insert(entity);
        }
    }

    @Override
    @Transactional
    public void delMain(String id) {
        orderAssurerAccountMapper.deleteByMainId(id);
        orderAssurerMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            orderAssurerAccountMapper.deleteByMainId(id.toString());
            orderAssurerMapper.deleteById(id);
        }
    }

    /**
     * 为订单获取最优承兑商
     */
    @Override
    public Map<String, Object> getAssurerByOrder(OrderMain orderMain) {
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
            OrderAssurerAccount account = orderAssurerAccountService.getAssurerAccountByOrderPay(assurer.getId(), orderMain.getAssurerCnyMoney(), orderMain.getUserCollectionMethod(), orderMain.getUserCollectionAreaCode());
            if (BaseUtil.Base_HasValue(account)) {
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
            orderAssurerAccount = orderAssurerAccountService.getAssurerAccountByOrderPay(orderAssurer.getId(), orderMain.getAssurerCnyMoney(), orderMain.getUserCollectionMethod(), orderMain.getUserCollectionAreaCode());
        }
        // 如果没找到账号,说明没有承兑商合适
        if (!BaseUtil.Base_HasValue(orderAssurerAccount)) {
            resultMap.put("state", "error");
            resultMap.put("message", "未找到合适的承兑商账户");
            return resultMap;
        }
        // 为承兑商选择一个收款账户
        OrderAssurerAccount orderAssurerAccountCollection = orderAssurerAccountService.getAssurerAccountByOrderCollection(orderAssurer.getId(), orderMain.getUserPayMethod(), orderMain.getUserPayAreaCode());
        resultMap.put("state", "success");
        resultMap.put("orderAssurer", orderAssurer);
        resultMap.put("orderAssurerAccountPay", orderAssurerAccount);
        resultMap.put("orderAssurerAccountCollection", orderAssurerAccountCollection);
        return resultMap;
    }

    /**
     * 判断承兑商保证金及租赁金是否足够
     */
    @Override
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

    @Override
    public OrderAssurer getAssurerByUserNo(String userNo) {
        OrderAssurer orderAssurer = orderAssurerMapper.getAssurerByUserNo(userNo);
        return orderAssurer;
    }

    /**
     * 改变在线状态
     */
    @Override
    public OrderAssurer updateAssurer(OrderAssurer data) {
        if (!BaseUtil.Base_HasValue(data.getId())) {
            return null;
        }
        updateById(data);
        return data;
    }

    /**
     * 后台添加注册承兑商
     */
    @Override
    public OrderAssurer addAssurerMain(JSONObject data) {
        System.out.println(data);
        String phone = data.getString("phone");
        String pwd = data.getString("password");
        String areacode = data.getString("areacode");

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
        payUserLogin.setUserType(3);
        iPayUserLoginService.save(payUserLogin);
        PayUserInfo payUserInfo = new PayUserInfo();
        payUserInfo.setUserNo(userno);
        payUserInfo.setPhoneNo(phone);
        payUserInfo.setUserType(3);
        iPayUserInfoService.save(payUserInfo);
        OrderAssurer orderAssurer = new OrderAssurer();
        orderAssurer.setUserNo(userno);
        orderAssurer.setAssurerName(phone);
        orderAssurer.setAssurerPhone(phone);
        orderAssurer.setAssurerRate(data.getDouble("assurerRate"));
        orderAssurer.setTotalLimit(data.getDouble("totalLimit") * 100);
        orderAssurer.setCanUseLimit(data.getDouble("totalLimit") * 100);
        this.save(orderAssurer);
        return orderAssurer;
    }

    /**
     * 批量审核承兑商
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
                result = Result.error(514, "承兑商总限额必须大于0");
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
     * 更改承兑商金额
     */
    @Override
    public Result<Object> changeAssurerMoneyMain(JSONObject jsonObject) {
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

}
