package org.suhui.modules.suhui.suhui.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 账户资金变更聚合流水表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("biz_asset_change_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="biz_asset_change_record对象", description="账户资金变更聚合流水表")
public class BizAssetChangeRecord {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**支付流水*/
	@Excel(name = "支付流水", width = 15)
    @ApiModelProperty(value = "支付流水")
	private String payNo;
	/**记账流水*/
	@Excel(name = "记账流水", width = 15)
    @ApiModelProperty(value = "记账流水")
	private String billNo;
	/**用户编号*/
	@Excel(name = "用户编号", width = 15)
    @ApiModelProperty(value = "用户编号")
	private String userNo;
	/**用户类型（1-个人 2-企业）*/
	@Excel(name = "用户类型（1-个人 2-企业）", width = 15)
    @ApiModelProperty(value = "用户类型（1-个人 2-企业）")
	private Integer userType;
	/**账户编号*/
	@Excel(name = "账户编号", width = 15)
    @ApiModelProperty(value = "账户编号")
	private String accountNo;
	/**账户类型*/
	@Excel(name = "账户类型", width = 15)
    @ApiModelProperty(value = "账户类型")
	private Integer accountType;
	/**身份编号*/
	@Excel(name = "身份编号", width = 15)
    @ApiModelProperty(value = "身份编号")
	private Integer identityNo;
	/**变更类型 1-增加 2-减少 4-冻结 5-解冻*/
	@Excel(name = "变更类型 1-增加 2-减少 4-冻结 5-解冻", width = 15)
    @ApiModelProperty(value = "变更类型 1-增加 2-减少 4-冻结 5-解冻")
	private Integer changeType;
	/**变更金额*/
	@Excel(name = "变更金额", width = 15)
    @ApiModelProperty(value = "变更金额")
	private Integer changeAmount;
	/**变更前冻结金额*/
	@Excel(name = "变更前冻结金额", width = 15)
    @ApiModelProperty(value = "变更前冻结金额")
	private Integer frozenAmountBefore;
	/**变更前可用金额*/
	@Excel(name = "变更前可用金额", width = 15)
    @ApiModelProperty(value = "变更前可用金额")
	private Integer availableAmountBefore;
	/**变更后冻结金额*/
	@Excel(name = "变更后冻结金额", width = 15)
    @ApiModelProperty(value = "变更后冻结金额")
	private Integer frozenAmountAfter;
	/**变更后可用金额*/
	@Excel(name = "变更后可用金额", width = 15)
    @ApiModelProperty(value = "变更后可用金额")
	private Integer availableAmountAfter;
	/**变更时间*/
    @ApiModelProperty(value = "变更时间")
	private Date changeTime;
	/**记账时间*/
    @ApiModelProperty(value = "记账时间")
	private Date billTime;
	/**记账类型 1-资产增加 2-转账(废弃) 3-资产减少 4-冻结 5-解冻 6-冻结资产减少*/
	@Excel(name = "记账类型 1-资产增加 2-转账(废弃) 3-资产减少 4-冻结 5-解冻 6-冻结资产减少", width = 15)
    @ApiModelProperty(value = "记账类型 1-资产增加 2-转账(废弃) 3-资产减少 4-冻结 5-解冻 6-冻结资产减少")
	private Integer billType;
	/**业务类型编码*/
	@Excel(name = "业务类型编码", width = 15)
    @ApiModelProperty(value = "业务类型编码")
	private String payBizType;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**记账数据*/
	@Excel(name = "记账数据", width = 15)
    @ApiModelProperty(value = "记账数据")
	private String billJson;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private Date updateTime;
}
