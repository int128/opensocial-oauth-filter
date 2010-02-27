package org.hidetake.util.oauth.config;

import org.hidetake.util.oauth.extensionpoint.ExtensionPoint;
import org.hidetake.util.oauth.util.NoSuchNodeException;
import org.hidetake.util.oauth.util.XPathEvaluator;

public interface RegistryConfigurator
{

	public void configure(AppRegistry registry) throws ConfigurationException;

	public void configure(ExtensionRegistry registry) throws ConfigurationException;

}
