package com.ice;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ice.dao.bo.UsrBo;
import com.ice.service.UsrService;

import misc.Misc;

public class MyBatisSpringTest
{
	@Test
	public void testConnectMyBatis()
	{
		ApplicationContext cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
		UsrService service = (UsrService) cxt.getBean("usrService");
		UsrBo usr = service.queryUsr(1);
		if (usr == null)
			System.out.println("为空!");
		else
			System.out.println(Misc.obj2json(usr));
	}
}
