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
package org.hidetake.util.oauth.test.extension;

import org.hidetake.util.oauth.config.ExtensionRegistryManager;
import org.hidetake.util.oauth.extensionpoint.ExtensionPoint;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExtensionRegistryTest
{
	
	private static interface TestExtensionPoint extends ExtensionPoint
	{
		public void notify(String message);
	}

	private static interface HogeExtensionPoint extends ExtensionPoint
	{
		public void hoge(String message);
	}

	private static class SingleExtension implements TestExtensionPoint
	{
		private final String expected;
		private boolean notified = false;
		public SingleExtension(String expected)
		{
			this.expected = expected;
		}
		public void notify(String message)
		{
			Assert.assertEquals(expected, message);
			notified = true;
		}
		public boolean isNotified()
		{
			return notified;
		}
	}

	private static class MultipleExtension implements TestExtensionPoint, HogeExtensionPoint
	{
		private final String expected;
		private boolean notified = false;
		private boolean hoged = false;
		public MultipleExtension(String expected)
		{
			this.expected = expected;
		}
		public void notify(String message)
		{
			Assert.assertEquals(expected, message);
			notified = true;
		}
		public void hoge(String message)
		{
			Assert.assertEquals(expected, message);
			hoged = true;
		}
		public boolean isNotified()
		{
			return notified;
		}
		public boolean isHoged()
		{
			return hoged;
		}
	}

	@Test
	public void test1() throws Exception
	{
		final String expected = "hage";
		SingleExtension single = new SingleExtension(expected);
		ExtensionRegistryManager.get().register(single);
		
		//some...
		
		for(TestExtensionPoint extension :
			ExtensionRegistryManager.get().getExtensions(TestExtensionPoint.class)) {
			extension.notify(expected);
		}
		
		Assert.assertTrue(single.isNotified());
	}

	@Test
	public void test2() throws Exception
	{
		final String expected = "hage";
		MultipleExtension multiple = new MultipleExtension(expected);
		ExtensionRegistryManager.get().register(multiple);
		
		//some...
		
		for(TestExtensionPoint extension :
			ExtensionRegistryManager.get().getExtensions(TestExtensionPoint.class)) {
			extension.notify(expected);
		}
		
		for(HogeExtensionPoint extension :
			ExtensionRegistryManager.get().getExtensions(HogeExtensionPoint.class)) {
			extension.hoge(expected);
		}
		
		Assert.assertTrue(multiple.isNotified());
		Assert.assertTrue(multiple.isHoged());
	}

	@Before
	public void before()
	{
		ExtensionRegistryManager.get().reset();
	}

	@After
	public void after()
	{
		ExtensionRegistryManager.get().reset();
	}

}
