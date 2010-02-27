package org.hidetake.util.oauth.config;

import org.hidetake.util.oauth.model.AppRegistry;
import org.hidetake.util.oauth.model.ExtensionRegistry;


public interface RegistryConfigurator
{

	public void configure(AppRegistry registry) throws ConfigurationException;

	public void configure(ExtensionRegistry registry) throws ConfigurationException;

}
