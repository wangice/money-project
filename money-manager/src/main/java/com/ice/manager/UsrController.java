package com.ice.manager;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.ice.dao.bo.TempFileBo;
import com.ice.service.TempFileService;
import com.ice.service.UsrService;

import misc.Dateu;
import misc.Misc;
import misc.Net;

@Controller
@RequestMapping("/usr")
public class UsrController
{
	@Resource
	public UsrService usrService;

	@Resource
	public TempFileService tempFileService;

	@ResponseBody
	@RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
	public String uploadImg(HttpServletRequest request) throws Exception
	{
		System.out.println("进入");
		String thumbnail = request.getParameter("thumbnail");/** 是否略缩图. */
		long startTime = System.currentTimeMillis();
		CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (!resolver.isMultipart(request))
			return "失败";
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = multiRequest.getFileNames();
		if (!iterator.hasNext())
			return "失败";
		while (iterator.hasNext())
		{
			System.out.println("文件名：" + iterator.next().toString());
			String name = iterator.next().toString();
			int index = name.lastIndexOf(".");
			if (index < 0 || index > name.length() - 2)
				return "失败";
			String suffix = name.substring(index, name.length());/* 文件后缀名. */
			if (!".jpg".equals(suffix) && !".png".equals(suffix))
				return "不支持";
			MultipartFile file = multiRequest.getFile(name);
			System.out.println("文件名1:" + file.getOriginalFilename());
			if (file.isEmpty())
				return "失败";
			byte[] by = Net.readAll(file.getInputStream());
			if (by == null)
				return "失败";
			Date now = new Date();
			int r = Misc.randInt();
			String fileName = Misc.printf2Str("%s%08X%s", Dateu.parseDateyyyyMMddHHmmss2(now), r, suffix);
			String path = UsrController.createFilePath(now, "img", fileName);
			TempFileBo img = new TempFileBo();
			img.setPath(path);
			img.setW(false);
			img.setDat(by);
			int success = tempFileService.save(img);
			if (success > 0)/* 文件保存成功. */
				tempFileService.updateTempFile(true);
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
					tempFileService.updateTempFile(true);
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("运行时间：" + (endTime - startTime) + "ms");
		return "成功";
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
