package com.ice.dao.bo;

import java.util.Date;

public class UsrBo {
	/** 主键. */
	private Integer id;
	/** 电话号码. */
	private String phone;
	/** 账号. */
	private String account;
	/** 用户名. */
	private String usrName;
	/** 密码. */
	private String pwd;
	/** 角色. */
	private Integer role;
	/** 身份证号. */
	private String identity;
	/** 身份证状态. */
	private int identityStatus;
	/** 邮箱. */
	private String email;
	/** 电站图片(大图). */
	public String picBig;
	/** 电站图片(小图). */
	public String picSmall;
	/** 创建时间. */
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone == null ? null : phone.trim();
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account == null ? null : account.trim();
	}

	public String getUsrName() {
		return usrName;
	}

	public void setUsrName(String usrName) {
		this.usrName = usrName == null ? null : usrName.trim();
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd == null ? null : pwd.trim();
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity == null ? null : identity.trim();
	}

	public int getIdentityStatus() {
		return identityStatus;
	}

	public void setIdentityStatus(int identityStatus) {
		this.identityStatus = identityStatus;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getPicBig() {
		return picBig;
	}

	public void setPicBig(String picBig) {
		this.picBig = picBig;
	}

	public String getPicSmall() {
		return picSmall;
	}

	public void setPicSmall(String picSmall) {
		this.picSmall = picSmall;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
