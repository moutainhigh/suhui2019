package org.suhui.modules.order.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.order.entity.OrderAssurer;
import org.suhui.modules.order.entity.OrderAssurerMoneyChange;
import org.suhui.modules.order.mapper.OrderAssurerMapper;
import org.suhui.modules.order.mapper.OrderAssurerMoneyChangeMapper;
import org.suhui.modules.order.service.impl.OrderAssurerMoneyChangeServiceImpl;
import org.suhui.modules.order.service.impl.OrderAssurerServiceImpl;
import org.suhui.modules.utils.BaseUtil;

import java.util.Date;
import java.util.List;

@Component
public class OrderScheduleService {


    @Autowired
    OrderAssurerServiceImpl orderAssurerService;

    @Autowired
    OrderAssurerMapper orderAssurerMapper;

    @Autowired
    OrderAssurerMoneyChangeServiceImpl orderAssurerMoneyChangeService;
    // 每5000毫秒执行一次
    @Scheduled(fixedRate = 5000)
    @Transactional
    public void reportCurrentTime() throws IllegalAccessException {
        List<OrderAssurerMoneyChange> orderAssurerMoneyChangeList = orderAssurerMoneyChangeService.getInitData();
        if (BaseUtil.Base_HasValue(orderAssurerMoneyChangeList)) {
            for (OrderAssurerMoneyChange orderAssurerMoneyChange : orderAssurerMoneyChangeList) {
                OrderAssurer orderAssurer = orderAssurerMapper.getByIdForUpdate(orderAssurerMoneyChange.getAssurerId());
                String classChange = orderAssurerMoneyChange.getChangeClass();
                String typeChange = orderAssurerMoneyChange.getChangeType();
                Double changeMoney = orderAssurerMoneyChange.getChangeMoney()*100;
                orderAssurerMoneyChange.setFlag("finish");
                if (BaseUtil.Base_HasValue(orderAssurer)) {
                    // 保证金
                    if ("ensure".equals(classChange)) {
                        if ("sub".equals(typeChange)) {
                            if (orderAssurer.getEnsureMoney() > changeMoney) {
                                orderAssurer.setEnsureMoney(BaseUtil.sub(orderAssurer.getEnsureMoney(),changeMoney,0));
                            }else{
                                orderAssurerMoneyChange.setFlag("error");
                                orderAssurerMoneyChange.setErrorText("保证金小于扣减金额");
                            }
                        } else {
                            double ensureChangeMoney = BaseUtil.div(changeMoney,orderAssurer.getEnsureProportion(),0);
                            orderAssurer.setEnsureMoney(BaseUtil.add(orderAssurer.getEnsureMoney(),ensureChangeMoney,0));
                        }
                    } else if ("lease".equals(classChange)) { // 租赁金
                        if ("sub".equals(typeChange)) {
                            if (orderAssurer.getLeaseMoney() > changeMoney) {
                                // 非订单扣减，变更总额度
                                if(!BaseUtil.Base_HasValue(orderAssurerMoneyChange.getOrderId())){
                                    // 总额度扣减金额
                                    Double subMoney = BaseUtil.div(changeMoney,orderAssurer.getAssurerRate(),0);
                                    if(orderAssurer.getTotalLimit() > subMoney){
                                        // 扣减后总额度
                                        Double totalMoney = BaseUtil.sub(orderAssurer.getTotalLimit(),subMoney,0);
                                        if (totalMoney > (orderAssurer.getUsedLimit()+orderAssurer.getPayLockMoney())) {
                                            orderAssurer.setTotalLimit(totalMoney);
                                            orderAssurer.setCanUseLimit(totalMoney - orderAssurer.getUsedLimit()-orderAssurer.getPayLockMoney());
                                        }else{
                                            orderAssurerMoneyChange.setFlag("error");
                                            orderAssurerMoneyChange.setErrorText("租赁金扣减后总额度小于已使用额度+订单锁定额度");
                                        }
                                    }else{
                                        orderAssurerMoneyChange.setFlag("error");
                                        orderAssurerMoneyChange.setErrorText("总额度小于扣减金额");
                                    }
                                }
                                orderAssurer.setLeaseMoney(BaseUtil.sub(orderAssurer.getLeaseMoney(),changeMoney,0));
                            }
                        } else {
                            // 总额度新增金额
                            Double addMoney = BaseUtil.div(changeMoney,orderAssurer.getAssurerRate(),0);
                            Double totalMoney = BaseUtil.add(orderAssurer.getTotalLimit(),addMoney,0);
                            orderAssurer.setTotalLimit(totalMoney);
                            orderAssurer.setCanUseLimit(totalMoney - orderAssurer.getUsedLimit()-orderAssurer.getPayLockMoney());
                            orderAssurer.setLeaseMoney(BaseUtil.add(orderAssurer.getLeaseMoney(),changeMoney,0));
                        }
                    }
                }else{
                    orderAssurerMoneyChange.setFlag("error");
                    orderAssurerMoneyChange.setErrorText("承兑商不存在");
                }
                orderAssurerService.updateById(orderAssurer);
                orderAssurerMoneyChangeService.updateById(orderAssurerMoneyChange);
            }
        }
    }

}
