package org.hidetake.util.oauth.test.config;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.hidetake.util.oauth.config.ExtensionRegistryManager;
import org.hidetake.util.oauth.extension.AllowLocalhost;
import org.hidetake.util.oauth.extension.ValidationLogger;
import org.hidetake.util.oauth.extensionpoint.ExtensionPoint;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class ExtensionRegistryManagerTest
{

	@Before
	public void before()
	{
		ExtensionRegistryManager.get().reset();
	}
	
	@Test
	public void test1() throws Exception
	{
		final InputStream stream = ExtensionRegistryManagerTest.class.getResourceAsStream("config1.xml");
		ExtensionRegistryManager.register(stream);
		
		final Set<String> expects = new HashSet<String>();
		expects.add(AllowLocalhost.class.getName());
		expects.add(ValidationLogger.class.getName());
		
		final Set<String> actuals = new HashSet<String>();
		for(ExtensionPoint extensionPoint : ExtensionRegistryManager.get().getAllExtensions()) {
			actuals.add(extensionPoint.getClass().getName());
		}
		
		Assert.assertEquals(expects, actuals);
	}

	@After
	public void after()
	{
		ExtensionRegistryManager.get().reset();
	}

}
