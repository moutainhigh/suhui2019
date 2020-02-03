package org.suhui.modules.order.vo;

import java.util.List;
import org.suhui.modules.order.entity.OrderAssurer;
import org.suhui.modules.order.entity.OrderAssurerAccount;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * @Description: 去
 * @Author: jeecg-boot
 * @Date:   2019-12-29
 * @Version: V1.0
 */
@Data
public class OrderAssurerPage {
	
	/**id*/
	private java.lang.String id;
	/**用户编号*/
  	@Excel(name = "用户编号", width = 15)
	private java.lang.String userNo;
	/**承兑商名称*/
  	@Excel(name = "承兑商名称", width = 15)
	private java.lang.String assurerName;
	/**承兑商国家码*/
  	@Excel(name = "承兑商国家码", width = 15)
	private java.lang.String countryCode;
	/**承兑商状态(待审核(to_audit)、正常接单(normal)、余额不足(lack_balance)、禁止接单(ban_order))*/
  	@Excel(name = "承兑商状态(待审核(to_audit)、正常接单(normal)、余额不足(lack_balance)、禁止接单(ban_order))", width = 15)
	private java.lang.String assurerState;
	/**费率*/
  	@Excel(name = "费率", width = 15)
	private java.lang.Double assurerRate;
	/**可用额度*/
  	@Excel(name = "可用额度", width = 15)
	private java.lang.Double canUseLimit;
	/**已用额度*/
  	@Excel(name = "已用额度", width = 15)
	private java.lang.Double usedLimit;
	/**总额度=已用额度+可用额度*/
  	@Excel(name = "总额度=已用额度+可用额度", width = 15)
	private java.lang.Double totalLimit;
	/**策略(单一制(one)、多单制(more))*/
  	@Excel(name = "策略(单一制(one)、多单制(more))", width = 15)
	private java.lang.String assurerStrategy;
	/**创建人*/
  	@Excel(name = "创建人", width = 15)
	private java.lang.String createBy;
	/**创建时间*/
  	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
  	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**更新人*/
  	@Excel(name = "更新人", width = 15)
	private java.lang.String updateBy;
	/**更新时间*/
  	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
  	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**删除状态（0，正常，1已删除）*/
  	@Excel(name = "删除状态（0，正常，1已删除）", width = 15)
	private java.lang.String delFlag;
	
	@ExcelCollection(name="客户明细")
	private List<OrderAssurerAccount> orderAssurerAccountList;
	
}
