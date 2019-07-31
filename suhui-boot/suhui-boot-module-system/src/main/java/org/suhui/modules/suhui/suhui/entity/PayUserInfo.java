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
 * @Description: 用户信息表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("pay_user_info")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="pay_user_info对象", description="用户信息表")
public class PayUserInfo {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**用户编号*/
	@Excel(name = "用户编号", width = 15)
    @ApiModelProperty(value = "用户编号")
	private String userNo;
	/**用户类型 0-默认 1-个人 2-企业*/
	@Excel(name = "用户类型 0-默认 1-个人 2-企业", width = 15)
    @ApiModelProperty(value = "用户类型 0-默认 1-个人 2-企业")
	private Integer userType;
	/**用户真实姓名*/
	@Excel(name = "用户真实姓名", width = 15)
    @ApiModelProperty(value = "用户真实姓名")
	private String userName;
	/**证件类型 1-身份证 2-军官证 3-护照*/
	@Excel(name = "证件类型 1-身份证 2-军官证 3-护照", width = 15)
    @ApiModelProperty(value = "证件类型 1-身份证 2-军官证 3-护照")
	private Integer cardType;
	/**证件号码*/
	@Excel(name = "证件号码", width = 15)
    @ApiModelProperty(value = "证件号码")
	private String cardNo;
	/**国籍编号，手机号前缀*/
	@Excel(name = "国籍编号，手机号前缀", width = 15)
    @ApiModelProperty(value = "国籍编号，手机号前缀")
	private String countryNo;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
	private String phoneNo;
	/**邮箱*/
	@Excel(name = "邮箱", width = 15)
    @ApiModelProperty(value = "邮箱")
	private String email;
	/**用户类型 0-默认 1-男人 2-女人*/
	@Excel(name = "用户类型 0-默认 1-男人 2-女人", width = 15)
    @ApiModelProperty(value = "用户类型 0-默认 1-男人 2-女人")
	private Integer sex;
	/**会员生日 20190722格式*/
	@Excel(name = "会员生日 20190722格式", width = 15)
    @ApiModelProperty(value = "会员生日 20190722格式")
	private String birthday;
	/**用户身份认证 0-未实名认证 1-已经实名认证*/
	@Excel(name = "用户身份认证 0-未实名认证 1-已经实名认证", width = 15)
    @ApiModelProperty(value = "用户身份认证 0-未实名认证 1-已经实名认证")
	private Integer cardCheck;
	/**手机号是否验证 0-未验证 1-已经验证*/
	@Excel(name = "手机号是否验证 0-未验证 1-已经验证", width = 15)
    @ApiModelProperty(value = "手机号是否验证 0-未验证 1-已经验证")
	private Integer phoneCheck;
	/**邮箱是否验证 0-未验证 1-已经验证*/
	@Excel(name = "邮箱是否验证 0-未验证 1-已经验证", width = 15)
    @ApiModelProperty(value = "邮箱是否验证 0-未验证 1-已经验证")
	private Integer emailCheck;
	/**registerTime*/
    @ApiModelProperty(value = "registerTime")
	private Date registerTime;
	/**createTime*/
    @ApiModelProperty(value = "createTime")
	private Date createTime;
	/**updateTime*/
    @ApiModelProperty(value = "updateTime")
	private Date updateTime;
	/**会员级别 1-普通会员 2-青铜 3-白银 4-黄金 5-钻石*/
	@Excel(name = "会员级别 1-普通会员 2-青铜 3-白银 4-黄金 5-钻石", width = 15)
    @ApiModelProperty(value = "会员级别 1-普通会员 2-青铜 3-白银 4-黄金 5-钻石")
	private Integer userLevel;
}
