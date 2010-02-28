package org.hidetake.util.oauth.config;

import org.hidetake.util.oauth.model.AppRegistry;
import org.hidetake.util.oauth.model.ExtensionRegistry;


/**
 * Applying configuration to registries.
 * 
 * @author hidetake.org
 *
 */
public interface RegistryConfigurator
{

	/**
	 * Apply configuration to application registry.
	 * @param registry
	 * @throws ConfigurationException
	 */
	public void configure(AppRegistry registry) throws ConfigurationException;

	/**
	 * Apply configuration to extension registry.
	 * @param registry
	 * @throws ConfigurationException
	 */
	public void configure(ExtensionRegistry registry) throws ConfigurationException;

}
