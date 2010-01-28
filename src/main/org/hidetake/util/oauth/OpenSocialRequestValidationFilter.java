package org.hidetake.util.oauth;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthException;
import net.oauth.OAuthMessage;

import org.hidetake.util.oauth.config.InitContext;
import org.hidetake.util.oauth.config.OpenSocialAccessorConfigurationException;
import org.hidetake.util.oauth.config.OpenSocialAccessorFactory;
import org.hidetake.util.oauth.model.OpenSocialAccessor;
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
			InputStream configStream = new FileInputStream(realPath);
			
			OpenSocialAccessorFactory openSocialAccessorFactory = new OpenSocialAccessorFactory(configStream);
			final OpenSocialAccessor accessor = openSocialAccessorFactory.getOpenSocialAccessor();
			
			validator = new OpenSocialRequestValidator(accessor);
			validationEventListeners = openSocialAccessorFactory.getValidationEventListeners();
			
			// notify initialization
			InitContext context = new InitContext()
			{
				public List<ValidationEventListener> getValidationEventListeners()
				{
					return validationEventListeners;
				}
				
				public OpenSocialAccessor getOpenSocialAccessor()
				{
					return accessor;
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
		catch (FileNotFoundException e) {
			throw new ServletException(e);
		}
		catch (OpenSocialAccessorConfigurationException e) {
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
		catch (OAuthException e) {
			boolean sent = false;
			for(ValidationEventListener eventListener : validationEventListeners) {
				sent |= eventListener.onOAuthException(request, response, e);
			}
			if(!sent) {
				// default behavior
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		}
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
