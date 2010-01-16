package org.hidetake.util.oauth.test;

import javax.servlet.http.HttpServletRequest;

import org.hidetake.util.oauth.OAuthValidationFilter;
import org.junit.Assert;
import org.junit.Test;

public class OAuthValidationFilterTest extends OAuthValidationFilter
{

	@Test
	public void testParseRequestUrl1()
	{
		final HttpServletRequest input = new HttpServletRequestStub()
		{
			public int getLocalPort() {return 0;}
			public String getServerName() {return "www.example.com";}
			public String getScheme(){return "http";}
			public String getRequestURI(){return "/test";}
		};
		final String expected = "http://www.example.com/test";
		
		final StringBuilder url = parseRequestUrl(input);
		Assert.assertEquals(expected, url.toString());
	}

	@Test
	public void testParseRequestUrl2()
	{
		final HttpServletRequest input = new HttpServletRequestStub()
		{
			public int getLocalPort() {return 80;}
			public String getServerName() {return "www.example.com";}
			public String getScheme(){return "http";}
			public String getRequestURI(){return "/test";}
		};
		final String expected = "http://www.example.com/test";
		
		final StringBuilder url = parseRequestUrl(input);
		Assert.assertEquals(expected, url.toString());
	}

	@Test
	public void testParseRequestUrl3()
	{
		final HttpServletRequest input = new HttpServletRequestStub()
		{
			public int getLocalPort() {return 8080;}
			public String getServerName() {return "www.example.com";}
			public String getScheme(){return "http";}
			public String getRequestURI(){return "/test";}
		};
		final String expected = "http://www.example.com:8080/test";
		
		final StringBuilder url = parseRequestUrl(input);
		Assert.assertEquals(expected, url.toString());
	}

	@Test
	public void testParseRequestUrl4()
	{
		final HttpServletRequest input = new HttpServletRequestStub()
		{
			public int getLocalPort() {return 0;}
			public String getServerName() {return "www.example.com";}
			public String getScheme(){return "https";}
			public String getRequestURI(){return "/test";}
		};
		final String expected = "https://www.example.com/test";
		
		final StringBuilder url = parseRequestUrl(input);
		Assert.assertEquals(expected, url.toString());
	}

	@Test
	public void testParseRequestUrl5()
	{
		final HttpServletRequest input = new HttpServletRequestStub()
		{
			public int getLocalPort() {return 443;}
			public String getServerName() {return "www.example.com";}
			public String getScheme(){return "https";}
			public String getRequestURI(){return "/test";}
		};
		final String expected = "https://www.example.com/test";
		
		final StringBuilder url = parseRequestUrl(input);
		Assert.assertEquals(expected, url.toString());
	}

	@Test
	public void testParseRequestUrl6()
	{
		final HttpServletRequest input = new HttpServletRequestStub()
		{
			public int getLocalPort() {return 8443;}
			public String getServerName() {return "www.example.com";}
			public String getScheme(){return "https";}
			public String getRequestURI(){return "/test";}
		};
		final String expected = "https://www.example.com:8443/test";
		
		final StringBuilder url = parseRequestUrl(input);
		Assert.assertEquals(expected, url.toString());
	}

}
