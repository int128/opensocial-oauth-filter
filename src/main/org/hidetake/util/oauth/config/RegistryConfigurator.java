package org.hidetake.util.oauth.config;


public interface RegistryConfigurator
{

	public void configure(AppRegistry registry) throws ConfigurationException;

	public void configure(ExtensionRegistry registry) throws ConfigurationException;

}
