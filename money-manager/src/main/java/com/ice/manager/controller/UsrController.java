package com.ice.manager.controller;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.ice.dao.bo.TempFileBo;
import com.ice.manager.http.HttpTrans;
import com.ice.manager.http.Rsp.RspErr;
import com.ice.service.TempFileService;
import com.ice.service.UsrService;

import misc.Crypto;
import misc.Dateu;
import misc.Misc;
import misc.Net;

@Controller
public class UsrController
{
	Logger log = LoggerFactory.getLogger("UsrController");

	@Resource
	public UsrService usrService;

	@Resource
	public TempFileService tempFileService;

	/** 登录. */
	public void login(HttpTrans trans, String sign, String salt, String action) throws Exception
	{
		if (!Crypto.sha1StrLowerCase((sign + salt + action).getBytes()).equals(sign))/* sign不匹配. */
		{
			if (log.isDebugEnabled())
				log.debug("sign error");
			trans.end(RspErr.ERR_SIGN);
			return;
		}

	}

	/** 上传文件. */
	public void uploadImg(HttpServletRequest request, HttpTrans trans) throws Exception
	{
		String thumbnail = request.getParameter("thumbnail");/** 是否略缩图. */
		long startTime = System.currentTimeMillis();
		CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (!resolver.isMultipart(request))
		{
			if (log.isDebugEnabled())
				log.debug("it is not multipart");
			trans.end(RspErr.ERR_FAIL, "it is not multipart");
			return;
		}
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = multiRequest.getFileNames();
		if (!iterator.hasNext())
		{
			if (log.isDebugEnabled())
				log.debug("it is not file");
			trans.end(RspErr.ERR_FAIL, "it is not file");
			return;
		}
		while (iterator.hasNext())
		{
			MultipartFile file = multiRequest.getFile(iterator.next().toString());
			if (file == null)
			{
				if (log.isDebugEnabled())
					log.debug("it is not file name");
				trans.end(RspErr.ERR_FAIL, "it is not file name");
				return;
			}
			String name = file.getOriginalFilename();
			int index = name.lastIndexOf(".");
			if (index < 0 || index > name.length() - 2)
			{
				trans.end(RspErr.ERR_FAIL, "file name length is error");
				return;
			}
			String suffix = name.substring(index, name.length());/* 文件后缀名. */
			if (!".jpg".equals(suffix) && !".png".equals(suffix))
			{
				trans.end(RspErr.ERR_UNSUPPORTED, "only supported jpg and png");
				return;
			}
			if (file.isEmpty())
			{
				trans.end(RspErr.ERR_FAIL, "file not is empty");
				return;
			}
			byte[] by = Net.readAll(file.getInputStream());
			if (by == null)
			{
				trans.end(RspErr.ERR_FAIL, "file not is empty");
				return;
			}
			Date now = new Date();
			int r = Misc.randInt();
			String fileName = Misc.printf2Str("%s%08X%s", Dateu.parseDateyyyyMMddHHmmss2(now), r, suffix);
			String path = UsrController.createFilePath(now, "img", fileName);
			TempFileBo img = new TempFileBo();
			img.setPath(path);
			img.setW(false);
			img.setDat(by);
			if (tempFileService == null)
			{
				System.out.println("为空");
			}
			int success = tempFileService.save(img);
			System.out.println(success);
			if (success > 0)/* 文件保存成功. */
				tempFileService.updateTempFileByPath(path, true);
			/** -------------------------------- */
			/**                                  */
			/** 小图. */
			/**                                  */
			/** -------------------------------- */
			if ("true".equals(thumbnail))/** 是否保存为略缩图. */
			{
				UsrController.makeThumbnail(150, 150, suffix.substring(1, suffix.length()), by);
				fileName = Misc.printf2Str("%s%08X%s", Dateu.parseDateyyyyMMddHHmmssms2(now), r + 1, suffix); /* 文件名. */
				path = UsrController.createFilePath(now, "img", fileName);
				//
				img = new TempFileBo();
				img.setPath(path);
				img.setW(false);
				img.setDat(by);
				success = tempFileService.save(img);
				if (success > 0)/* 文件保存成功. */
					tempFileService.updateTempFileByPath(path, true);
			}
		}
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled())
			log.debug("upload 运行时间: {} ms", (endTime - startTime));
		trans.end(RspErr.ERR_NONE);
	}

	/** 生成略缩图. */
	private static byte[] makeThumbnail(int w, int h, String type, byte[] src) throws Exception
	{
		BufferedImage bi = ImageIO.read(new ByteArrayInputStream(src));
		BufferedImage tag = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);/* 缩略图. */
		Graphics g = tag.getGraphics();
		g.drawImage(bi, 0, 0, w, h, null);
		g.dispose();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(tag, type, bos);
		return bos.toByteArray();
	}

	/** 构造文件保存路径, 这是一个相对路径, 如: img/2017/07/21/20170721093313928BBBF.png. */
	private static String createFilePath(Date ts, String prefix/* 文件前缀路径. */, String path)
	{
		int y = Dateu.yearInt(ts);
		int m = Dateu.monthInt(ts);
		int d = Dateu.dayInt(ts);
		//
		String result = Misc.printf2Str("%s/%d/%02d/%02d/", prefix, y, m, d);
		return result + path;
	}
}
