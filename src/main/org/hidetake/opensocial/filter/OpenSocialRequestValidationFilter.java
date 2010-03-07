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
package org.hidetake.opensocial.filter;

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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.oauth.OAuthMessage;

import org.hidetake.opensocial.filter.config.ConfigurationException;
import org.hidetake.opensocial.filter.config.RegistryConfigurator;
import org.hidetake.opensocial.filter.config.XmlRegistryConfigurator;
import org.hidetake.opensocial.filter.extensionpoint.AccessControl;
import org.hidetake.opensocial.filter.extensionpoint.FilterInitializing;
import org.hidetake.opensocial.filter.extensionpoint.RequestURL;
import org.hidetake.opensocial.filter.extensionpoint.Validation;
import org.hidetake.opensocial.filter.model.AppRegistry;
import org.hidetake.opensocial.filter.model.ExtensionRegistry;
import org.hidetake.opensocial.filter.model.OpenSocialException;
import org.hidetake.opensocial.filter.model.OpenSocialRequest;
import org.hidetake.opensocial.filter.model.OpenSocialRequestValidator;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * OpenSocial request validation filter.
 * 
 * <p>
 * web.xml exmaple:
 * <code><pre>
 *  &lt;filter&gt;
 *      &lt;filter-name&gt;opensocial-oauth-filter&lt;/filter-name&gt;
 *      &lt;filter-class&gt;org.hidetake.opensocial.filter.OpenSocialRequestValidationFilter&lt;/filter-class&gt;
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
	private ExtensionRegistry extensionRegistry;

	public void init(FilterConfig filterConfig) throws ServletException
	{
		// determine configuration method
		RegistryConfigurator configurator;
		
		String configClass = filterConfig.getInitParameter("config-class");
		if(configClass != null) {
			// instantiate
			try {
				configurator = (RegistryConfigurator) Class.forName(configClass).newInstance();
			}
			catch (InstantiationException e) {
				throw new ServletException(e);
			}
			catch (IllegalAccessException e) {
				throw new ServletException(e);
			}
			catch (ClassNotFoundException e) {
				throw new ServletException(e);
			}
		}
		else {
			// get XML path
			String configPath = filterConfig.getInitParameter("config");
			if(configPath == null) {
				configPath = "WEB-INF/opensocial-oauth-filter.xml";
			}
			
			// load configuration
			try {
				String realPath = filterConfig.getServletContext().getRealPath(configPath);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				//factory.setNamespaceAware(true);
				Document xml = factory.newDocumentBuilder().parse(new FileInputStream(realPath));
				
				configurator = new XmlRegistryConfigurator(xml);
			}
			catch (FileNotFoundException e) {
				throw new ServletException(e);
			}
			catch (SAXException e) {
				throw new ServletException(e);
			}
			catch (IOException e) {
				throw new ServletException(e);
			}
			catch (ParserConfigurationException e) {
				throw new ServletException(e);
			}
		}

		// apply configuration
		try {
			// register apps
			AppRegistry appRegistry = new AppRegistry();
			configurator.configure(appRegistry);
			
			validator = new OpenSocialRequestValidator(appRegistry);
			
			// register extensions
			extensionRegistry = new ExtensionRegistry();
			configurator.configure(extensionRegistry);
		}
		catch (ConfigurationException e) {
			throw new ServletException(e);
		}
		
		// notify init()
		for(FilterInitializing extension : extensionRegistry.getExtensions(FilterInitializing.class)) {
			extension.init(filterConfig, validator, extensionRegistry);
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
			// skips validation?
			boolean skip = false;
			for(AccessControl extension : extensionRegistry.getExtensions(AccessControl.class)) {
				skip |= extension.skipValidation(request, response);
			}
			
			if(skip) {
				// notify skipped
				for(Validation extension : extensionRegistry.getExtensions(Validation.class)) {
					extension.skipped(request, response);
				}
			}
			else {
				// create oauth message
				StringBuilder url = OpenSocialRequest.parseRequestUrl(request);
				
				for(RequestURL extension : extensionRegistry.getExtensions(RequestURL.class)) {
					extension.postprocess(url, request);
				}
				
				OAuthMessage message = new OAuthMessage(request.getMethod(),
					url.toString(), OpenSocialRequest.parseRequestParameters(request));
				
				// construct request object
				OpenSocialRequest openSocialRequest = OpenSocialRequest.create(message, request);
				
				// validate request
				validator.validate(openSocialRequest);
				
				// notify passed
				for(Validation extension : extensionRegistry.getExtensions(Validation.class)) {
					extension.passed(request, response, openSocialRequest);
				}
			}
			
			// call next filter
			chain.doFilter(request, response);
		}
		catch (OpenSocialException e) {
			// notify failed
			for(Validation extension : extensionRegistry.getExtensions(Validation.class)) {
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
