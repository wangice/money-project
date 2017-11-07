package com.ice.dao.bo;

public class TempFileBo
{
	/** 主键. */
	private Integer id;
	/** 文件存放路径. */
	private String path;
	/** 写入完成 ? true : false. */
	private boolean w;
	/** 文件正文. */
	private byte[] dat;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public boolean isW()
	{
		return w;
	}

	public void setW(boolean w)
	{
		this.w = w;
	}

	public byte[] getDat()
	{
		return dat;
	}

	public void setDat(byte[] dat)
	{
		this.dat = dat;
	}

}
