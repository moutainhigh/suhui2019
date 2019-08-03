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
 * @Description: app用户登陆
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("pay_user_login")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="pay_user_login对象", description="app用户登陆")
public class PayUserLogin {
    
	/**id*/
	@TableId(type = IdType.AUTO)
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
	/**用户登录名*/
	@Excel(name = "用户登录名", width = 15)
    @ApiModelProperty(value = "用户登录名")
	private String loginName;
	/**加密的密码*/
	@Excel(name = "加密的密码", width = 15)
    @ApiModelProperty(value = "加密的密码")
	private String password;
	/**状态 0-默认 1-有效 2-无效*/
	@Excel(name = "状态 0-默认 1-有效 2-无效", width = 15)
    @ApiModelProperty(value = "状态 0-默认 1-有效 2-无效")
	private Integer status;
	/**createTime*/
    @ApiModelProperty(value = "createTime")
	private Date createTime;
	/**updateTime*/
    @ApiModelProperty(value = "updateTime")
	private Date updateTime;

	@ApiModelProperty(value = "salt")
    private String salt ;

	@ApiModelProperty(value = "areacode")
	private String areacode ;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
}
