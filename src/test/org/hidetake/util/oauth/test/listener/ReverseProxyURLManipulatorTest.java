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
package org.hidetake.util.oauth.test.listener;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import org.hidetake.util.oauth.listener.ReverseProxyURLManipulator;
import org.hidetake.util.oauth.test.HttpServletRequestStub;
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
