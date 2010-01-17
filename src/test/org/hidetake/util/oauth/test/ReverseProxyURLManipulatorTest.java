package org.hidetake.util.oauth.test;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import org.hidetake.util.oauth.listener.ReverseProxyURLManipulator;
import org.junit.Test;

public class ReverseProxyURLManipulatorTest extends ReverseProxyURLManipulator
{

	@Test
	public void testManipulateURL1()
	{
		final StringBuilder input = new StringBuilder("http://proxy/test");
		final HttpServletRequest context = new HttpServletRequestStub()
		{
			public String getHeader(String name)
			{
				if("X-Forwarded-Host".equals(name)) {
					return "www.example.com";
				}
				return null;
			}
		};
		final String expected = "http://www.example.com/test";
		
		manipulateURL(input, context);
		Assert.assertEquals(expected, input.toString());
	}

	@Test
	public void testManipulateURL2()
	{
		final StringBuilder input = new StringBuilder("http://proxy/test");
		final HttpServletRequest context = new HttpServletRequestStub()
		{
			public String getHeader(String name)
			{
				return null;
			}
		};
		final String expected = "http://proxy/test";
		
		manipulateURL(input, context);
		Assert.assertEquals(expected, input.toString());
	}

}
