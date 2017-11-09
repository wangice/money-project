package com.ice.manager.job;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ice.dao.bo.TempFileBo;
import com.ice.manager.cfg.Cfg;
import com.ice.service.TempFileService;

import misc.Misc;

/**
 * 将数据库中的临时文件存储表移动到服务器指定位置中
 * 
 * create on: 2017年11月8日 下午3:57:08
 * 
 * @author: ice
 *
 */
@Service("job4UploadFileMove")
public class Job4UploadFileMove
{
	Logger logger = LoggerFactory.getLogger("Job4UploadFileMove");

	@Autowired
	TempFileService tempService;

	public void init()
	{
		while (true)
		{
			Misc.sleep(100);
			try
			{
				doit();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void doit() throws Exception
	{
		List<TempFileBo> tempFils = tempService.queryTempFilesByW(true);
		if (tempFils == null)
			return;
		for (TempFileBo tempFile : tempFils)
		{
			String path = Misc.printf2Str("%s%s", Cfg.pwd, tempFile.getPath());
			System.out.println(path);
			File dir = new File(path.substring(0, path.lastIndexOf("/")));
			dir.mkdirs();
			if (!dir.exists())
			{
				if (logger.isErrorEnabled())
					logger.error("it is a fault,do not mkdir file");
			}
			FileOutputStream out = null;
			try
			{
				out = new FileOutputStream(new File(path));
				out.write(tempFile.getDat());
			} catch (Exception e)
			{
				e.printStackTrace();
			} finally
			{
				Misc.closeOutputStream(out);
			}
			tempService.delteTempFile(tempFile);
		}
	}
}
