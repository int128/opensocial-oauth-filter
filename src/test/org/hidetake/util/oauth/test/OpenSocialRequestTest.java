/*
 * Copyright (C) 2009-2010 hidetake.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hidetake.util.oauth.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;

import org.hidetake.util.oauth.model.OpenSocialRequest;
import org.junit.Test;

public class OpenSocialRequestTest
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
		
		final StringBuilder url = OpenSocialRequest.parseRequestUrl(input);
		assertThat(url.toString(), is("http://www.example.com/test"));
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
		
		final StringBuilder url = OpenSocialRequest.parseRequestUrl(input);
		assertThat(url.toString(), is("http://www.example.com/test"));
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
		
		final StringBuilder url = OpenSocialRequest.parseRequestUrl(input);
		assertThat(url.toString(), is("http://www.example.com:8080/test"));
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
		
		final StringBuilder url = OpenSocialRequest.parseRequestUrl(input);
		assertThat(url.toString(), is("https://www.example.com/test"));
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
		
		final StringBuilder url = OpenSocialRequest.parseRequestUrl(input);
		assertThat(url.toString(), is("https://www.example.com/test"));
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
		
		final StringBuilder url = OpenSocialRequest.parseRequestUrl(input);
		assertThat(url.toString(), is("https://www.example.com:8443/test"));
	}

}
