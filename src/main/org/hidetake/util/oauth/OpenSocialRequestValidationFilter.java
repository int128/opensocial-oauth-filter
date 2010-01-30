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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthMessage;

import org.hidetake.util.oauth.config.AppRegistry;
import org.hidetake.util.oauth.config.AppRegistryFactory;
import org.hidetake.util.oauth.config.ConfigurationException;
import org.hidetake.util.oauth.config.InitContext;
import org.hidetake.util.oauth.model.OpenSocialApp;
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
	private List<ValidationEventListener> validationEventListeners;

	public void init(final FilterConfig filterConfig) throws ServletException
	{
		try {
			String configPath = filterConfig.getInitParameter("config");
			if(configPath == null) {
				configPath = "WEB-INF/opensocial-oauth-filter.xml";
			}
			
			// load configuration
			String realPath = filterConfig.getServletContext().getRealPath(configPath);

			// create an app registry
			AppRegistryFactory appRegistryFactory = new AppRegistryFactory();
			final AppRegistry registry = appRegistryFactory.create(realPath);
			
			validator = new OpenSocialRequestValidator(registry);
			validationEventListeners = appRegistryFactory.getValidationEventListeners(realPath);
			
			// notify initialization
			InitContext context = new InitContext()
			{
				public List<ValidationEventListener> getValidationEventListeners()
				{
					return validationEventListeners;
				}
				
				public OpenSocialApp getOpenSocialAccessor()
				{
					//FIXME:
					return registry.getList().get(0);
				}
				
				public FilterConfig getFilterConfig()
				{
					return filterConfig;
				}
			};
			
			for(ValidationEventListener eventListener : validationEventListeners) {
				eventListener.init(context);
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

	public void doFilterHttp(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	throws IOException, ServletException
	{
		try {
			// is skipping validation?
			boolean skip = false;
			for(ValidationEventListener eventListener : validationEventListeners) {
				skip |= eventListener.isSkippingValidation(request, response);
			}
			
			if(!skip) {
				// construct message object
				StringBuilder url = OpenSocialRequestValidator.parseRequestUrl(request);
				for(ValidationEventListener eventListener : validationEventListeners) {
					eventListener.manipulateURL(url, request);
				}
				OAuthMessage message = new OAuthMessage(request.getMethod(), url.toString(),
					OpenSocialRequestValidator.parseRequestParameters(request));
				
				// construct request object
				OpenSocialRequest openSocialRequest = new OpenSocialRequest(message, request);
				
				// validate request
				validator.validate(openSocialRequest);
				
				// notify success
				for(ValidationEventListener eventListener : validationEventListeners) {
					eventListener.onValidationComplete(response);
				}
			}
			
			// call next filter
			chain.doFilter(request, response);
		}
//		catch (OAuthException e) {
//			boolean sent = false;
//			for(ValidationEventListener eventListener : validationEventListeners) {
//				sent |= eventListener.onOAuthException(request, response, e);
//			}
//			if(!sent) {
//				// default behavior
//				response.sendError(HttpServletResponse.SC_FORBIDDEN);
//			}
//		}
		catch (OpenSocialException e) {
			boolean sent = false;
			for(ValidationEventListener eventListener : validationEventListeners) {
				sent |= eventListener.onOpenSocialException(request, response, e);
			}
			if(!sent) {
				// default behavior
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		}
	}

	public void destroy()
	{
	}

}
