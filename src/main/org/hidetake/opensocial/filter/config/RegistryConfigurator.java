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
package org.hidetake.opensocial.filter.config;

import org.hidetake.opensocial.filter.model.AppRegistry;
import org.hidetake.opensocial.filter.model.ExtensionRegistry;


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
