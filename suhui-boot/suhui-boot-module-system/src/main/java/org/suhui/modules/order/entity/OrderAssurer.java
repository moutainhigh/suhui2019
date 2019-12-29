package org.suhui.modules.order.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 去
 * @Author: jeecg-boot
 * @Date:   2019-12-29
 * @Version: V1.0
 */
@Data
@TableName("order_assurer")
public class OrderAssurer implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户编号*/
	private java.lang.String userNo;
	/**承兑商名称*/
	private java.lang.String assurerName;
	/**承兑商国家码*/
	private java.lang.String countryCode;
	/**承兑商状态(待审核(to_audit)、正常接单(normal)、余额不足(lack_balance)、禁止接单(ban_order))*/
	private java.lang.String assurerState;
	/**费率*/
	private java.lang.Double assurerRate;
	/**可用额度*/
	private java.lang.Integer canUseLimit;
	/**已用额度*/
	private java.lang.Integer usedLimit;
	/**总额度=已用额度+可用额度*/
	private java.lang.Integer totalLimit;
	/**策略(单一制(one)、多单制(more))*/
	private java.lang.String assurerStrategy;
	/**创建人*/
	private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**更新人*/
	private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**删除状态（0，正常，1已删除）*/
	private java.lang.String delFlag;
}
