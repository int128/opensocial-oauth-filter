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
package org.hidetake.util.oauth;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hidetake.util.oauth.config.AppRegistry;
import org.hidetake.util.oauth.config.AppRegistryFactory;
import org.hidetake.util.oauth.config.ConfigurationException;
import org.hidetake.util.oauth.config.ExtensionRegistryManager;
import org.hidetake.util.oauth.extensionpoint.AccessControl;
import org.hidetake.util.oauth.extensionpoint.FilterInitializing;
import org.hidetake.util.oauth.extensionpoint.Validation;
import org.hidetake.util.oauth.model.OpenSocialException;
import org.hidetake.util.oauth.model.OpenSocialRequest;
import org.hidetake.util.oauth.model.OpenSocialRequestValidator;

/**
 * OpenSocial request validation filter.
 * 
 * <p>
 * web.xml exmaple:
 * <code><pre>
 *  &lt;filter&gt;
 *      &lt;filter-name&gt;oauth-filter&lt;/filter-name&gt;
 *      &lt;filter-class&gt;org.hidetake.util.oauth.OAuthValidationFilter&lt;/filter-class&gt;
 *  &lt;/filter&gt;
 * </pre></code>
 * </p>
 * 
 * @author hidetake.org
 *
 */
public class OpenSocialRequestValidationFilter implements Filter
{

	private OpenSocialRequestValidator validator;

	public void init(FilterConfig filterConfig) throws ServletException
	{
		try {
			String configPath = filterConfig.getInitParameter("config");
			if(configPath == null) {
				configPath = "WEB-INF/opensocial-oauth-filter.xml";
			}
			
			// load configuration
			String realPath = filterConfig.getServletContext().getRealPath(configPath);

			// initialize validator
			AppRegistryFactory appRegistryFactory = new AppRegistryFactory();
			AppRegistry registry = appRegistryFactory.create(new FileInputStream(realPath));
			validator = new OpenSocialRequestValidator(registry);
			
			// register extensions
			ExtensionRegistryManager.register(new FileInputStream(realPath));
			
			for(FilterInitializing extension : ExtensionRegistryManager.get().getExtensions(
				FilterInitializing.class)) {
				extension.init(filterConfig, registry);
			}
		}
		catch (ConfigurationException e) {
			throw new ServletException(e);
		}
		catch (FileNotFoundException e) {
			throw new ServletException(e);
		}
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	throws IOException, ServletException
	{
		doFilterHttp((HttpServletRequest) req, (HttpServletResponse) res, chain);
	}

	private void doFilterHttp(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	throws IOException, ServletException
	{
		try {
			// is skipping validation?
			boolean skip = false;
			for(AccessControl extension : ExtensionRegistryManager.get().getExtensions(AccessControl.class)) {
				skip |= extension.skipValidation(request, response);
			}
			
			if(!skip) {
				// construct request object
				OpenSocialRequest openSocialRequest = OpenSocialRequest.create(request);
				
				// validate request
				validator.validate(openSocialRequest);
				
				for(Validation extension : ExtensionRegistryManager.get().getExtensions(Validation.class)) {
					extension.passed(request, response, openSocialRequest);
				}
			}
			
			// call next filter
			chain.doFilter(request, response);
		}
		catch (OpenSocialException e) {
			for(Validation extension : ExtensionRegistryManager.get().getExtensions(Validation.class)) {
				extension.failed(request, response, e);
			}
			
			// default behavior
			if(!response.isCommitted()) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		}
	}

	public void destroy()
	{
	}

}
