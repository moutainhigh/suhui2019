package org.suhui.modules.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.suhui.common.api.vo.Result;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.common.system.query.QueryGenerator;
import org.suhui.modules.order.entity.OrderAssurer;
import org.suhui.modules.order.entity.OrderAssurerAccount;
import org.suhui.modules.order.entity.OrderAssurerAccount;
import org.suhui.modules.order.entity.OrderMain;
import org.suhui.modules.order.service.IOrderAssurerAccountService;
import org.suhui.modules.order.vo.OrderAssurerPage;
import org.suhui.modules.utils.BaseUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/assurer/account")
@Slf4j
public class AssurerAccountController {

    @Autowired
    private IOrderAssurerAccountService orderAssurerAccountService;

    /**
     * 分页列表查询
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<IPage<OrderAssurerAccount>> queryPageList(OrderAssurerAccount orderAssurerAccount,
                                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                     HttpServletRequest req) {
        Result<IPage<OrderAssurerAccount>> result = new Result<IPage<OrderAssurerAccount>>();
        QueryWrapper<OrderAssurerAccount> queryWrapper = QueryGenerator.initQueryWrapper(orderAssurerAccount, req.getParameterMap());
        Page<OrderAssurerAccount> page = new Page<OrderAssurerAccount>(pageNo, pageSize);
        IPage<OrderAssurerAccount> pageList = orderAssurerAccountService.page(page, queryWrapper);
        List<OrderAssurerAccount> orderAssurers = pageList.getRecords();
        if (BaseUtil.Base_HasValue(orderAssurers)) {
            for(int i=0;i<orderAssurers.size();i++){
                orderAssurers.get(i).changeMoneyToBig();
            }
        }
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     *   添加
     * @param orderAssurerPage
     * @return
     */
    @PostMapping(value = "/add")
    public Result<OrderAssurerAccount> add(@RequestBody OrderAssurerAccount orderAssurerPage) {
        Result<OrderAssurerAccount> result = new Result<OrderAssurerAccount>();
        try {
            OrderAssurerAccount OrderAssurerAccount = new OrderAssurerAccount();
            BeanUtils.copyProperties(orderAssurerPage, OrderAssurerAccount);
            orderAssurerAccountService.save(OrderAssurerAccount);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     *  编辑
     * @param orderAssurerPage
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<OrderAssurerAccount> edit(@RequestBody OrderAssurerAccount orderAssurerPage) {
        Result<OrderAssurerAccount> result = new Result<OrderAssurerAccount>();
        OrderAssurerAccount orderAssurerAccount = new OrderAssurerAccount();
        BeanUtils.copyProperties(orderAssurerPage, orderAssurerAccount);
        OrderAssurerAccount orderAssurerEntity = orderAssurerAccountService.getById(orderAssurerAccount.getId());
        if(orderAssurerEntity==null) {
            result.error500("未找到对应实体");
        }else {
            boolean ok = orderAssurerAccountService.updateById(orderAssurerAccount);
            result.success("修改成功!");
        }

        return result;
    }


    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<OrderAssurerAccount> delete(@RequestParam(name = "id", required = true) String id) {
        Result<OrderAssurerAccount> result = new Result<OrderAssurerAccount>();
        OrderAssurerAccount orderAssurerAccount = orderAssurerAccountService.getById(id);
        if (orderAssurerAccount == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = orderAssurerAccountService.removeById(id);
            if (ok) {
                result.success("删除成功!");
            }
        }

        return result;
    }


    /**
     *  批量删除
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<OrderAssurerAccount> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        Result<OrderAssurerAccount> result = new Result<OrderAssurerAccount>();
        if(ids==null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        }else {
            this.orderAssurerAccountService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }
}
