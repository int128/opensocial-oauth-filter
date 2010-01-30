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


import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hidetake.util.oauth.config.InitContext;
import org.hidetake.util.oauth.model.OpenSocialException;

import net.oauth.OAuthException;
import net.oauth.OAuthMessage;

/**
 * OAuth signature validation event listener.
 * 
 * @author hidetake.org
 * 
 */
public interface ValidationEventListener
{

	/**
	 * Called by the web container to indicate to a filter that it is being placed into service.
	 * The servlet container calls the init method exactly once after instantiating the filter.
	 * The init method must complete successfully before the filter is asked to do any filtering work.
	 * 
	 * The web container cannot place the filter into service if the init method either
	 * <ol>
	 * <li>Throws a ServletException</li>
	 * <li>Does not return within a time period defined by the web container</li> 
	 * </ol>
	 * 
	 * @param context initialization context object
	 * @see javax.servlet.Filter#init(FilterConfig)
	 */
	public void init(InitContext context) throws ServletException;
	
	/**
	 * Must return true if want to skip validation.
	 * 
	 * @param req servlet request object
	 * @param res servlet response object
	 * @return true if skips validation, false otherwise
	 */
	public boolean isSkippingValidation(ServletRequest req, ServletResponse res);

	/**
	 * Called if validation was failed, caused by {@link OAuthException}.
	 * 
	 * <p>Default implementation: sends 403 response to web browser.</p>
	 * 
	 * @return true if response has been sent, false otherwise 
	 * @param request servlet request object
	 * @param response servlet response object
	 * @param e exception
	 */
	public boolean onOAuthException(HttpServletRequest request, HttpServletResponse response, OAuthException e);

	/**
	 * Called if validation was failed, caused by {@link OpenSocialException}.
	 * 
	 * <p>Default implementation: sends 403 response to web browser.</p>
	 * 
	 * @return true if response has been sent, false otherwise 
	 * @param request servlet request object
	 * @param response servlet response object
	 * @param e exception
	 */
	public boolean onOpenSocialException(HttpServletRequest request, HttpServletResponse response, OpenSocialException e);

	/**
	 * Called if validation was succeed.
	 * 
	 * <p>Default implementation: do nothing.</p>
	 * 
	 * @param response
	 */
	public void onValidationComplete(HttpServletResponse response);

	/**
	 * Called just before validation.
	 * Manipulate the URL that will be passed to {@link OAuthMessage} constructor.
	 * 
	 * <p>Default implementation: do nothing.</p>
	 * 
	 * @param url string builder which contains the URL parsed from servlet request object
	 * @param request servlet request object
	 */
	public void manipulateURL(StringBuilder url, HttpServletRequest request);

}
