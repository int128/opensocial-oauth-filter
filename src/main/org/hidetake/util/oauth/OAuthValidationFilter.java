package org.hidetake.util.oauth;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthValidator;
import net.oauth.SimpleOAuthValidator;

/**
 * OAuth signature validation filter for mixi apps.
 * Some initialize parameters should be defined in web.xml.
 * 
 * <ul>
 * <li>net.oauth.OAuthAccessor: Required, specify an accessor class.</li>
 * <li>org.hidetake.util.oauth.ValidationEventListener: Optional, specify event listeners, comma separated.</li>
 * </ul>
 * 
 * <p>
 * web.xml exmaple:
 * <code><pre>
 *  &lt;filter&gt;
 *      &lt;filter-name&gt;oauth-filter&lt;/filter-name&gt;
 *      &lt;filter-class&gt;org.hidetake.util.oauth.OAuthValidationFilter&lt;/filter-class&gt;
 *      &lt;init-param&gt;
 *      	&lt;param-name&gt;net.oauth.OAuthAccessor&lt;/param-name&gt;
 *      	&lt;param-value&gt;org.hidetake.util.oauth.provider.MixiOAuthAccessor&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 *  &lt;/filter&gt;
 * </pre></code>
 * </p>
 * 
 * @author hidetake.org
 *
 */
public class OAuthValidationFilter implements Filter
{
	private final OAuthValidator validator = new SimpleOAuthValidator();
	private final List<ValidationEventListener> eventListeners = new ArrayList<ValidationEventListener>();
	private OAuthAccessor accessor;

	@Override
	public void init(FilterConfig config)
	throws ServletException
	{
		try {
			// load accessor
			String accessorName = config.getInitParameter("net.oauth.OAuthAccessor");
			if(accessorName == null) {
				throw new ServletException(
					"Servlet filter parameter 'net.oauth.OAuthAccessor' not defined");
			}
			else {
				accessor = (OAuthAccessor) Class.forName(accessorName).newInstance();
			}

			// load event listeners
			String eventListenerNames = config.getInitParameter("org.hidetake.util.oauth.ValidationEventListener");
			if(eventListenerNames == null) {
			}
			else {
				for(String eventListenerName : eventListenerNames.split(",")) {
					ValidationEventListener eventListener =
						(ValidationEventListener) Class.forName(eventListenerName).newInstance();
					eventListeners.add(eventListener);
				}
			}
			
			// initialize listeners
			for(ValidationEventListener eventListener : eventListeners) {
				eventListener.init(config);
			}
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

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	throws IOException, ServletException
	{
		HttpServletRequest hreq = (HttpServletRequest) req;
		HttpServletResponse hres = (HttpServletResponse) res;
		
		try {
			// is skipping validation?
			boolean skip = false;
			for(ValidationEventListener eventListener : eventListeners) {
				skip |= eventListener.isSkippingValidation(req, res);
			}
			
			if(!skip) {
				// construct a message object
				StringBuilder url = parseRequestUrl(hreq);
				for(ValidationEventListener eventListener : eventListeners) {
					eventListener.manipulateURL(url, hreq);
				}
				OAuthMessage message = new OAuthMessage(hreq.getMethod(), url.toString(),
					parseRequestParameters(hreq));
				
				// validate signature
				validator.validateMessage(message, accessor);
				for(ValidationEventListener eventListener : eventListeners) {
					eventListener.onValidationComplete(hres);
				}
			}
			
			// next filter
			chain.doFilter(req, res);
		}
		catch (OAuthException e) {
			boolean sent = false;
			for(ValidationEventListener eventListener : eventListeners) {
				sent |= eventListener.onOAuthException(hreq, hres, e);
			}
			if(!sent) {
				// default behavior
				hres.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		}
		catch (URISyntaxException e) {
			boolean sent = false;
			for(ValidationEventListener eventListener : eventListeners) {
				sent |= eventListener.onURISyntaxException(hreq, hres, e);
			}
			if(!sent) {
				// default behavior
				hres.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		}
	}

	@Override
	public void destroy()
	{
	}

	/**
     * Constructs and returns the full URL associated with the passed request
     * object.
     * 
     * @param  request Servlet request object with methods for retrieving the
     *         various components of the request URL
     * @see    http://wiki.opensocial.org/index.php?title=Validating_Signed_Requests
     */
	protected static final StringBuilder parseRequestUrl(HttpServletRequest request)
	{
		StringBuilder url = new StringBuilder();

		String scheme = request.getScheme();
		url.append(scheme);
		url.append("://");
		url.append(request.getServerName());
		
		int port = request.getLocalPort();
		if (port == 0) {
			//nothing
		}
		else if ((scheme.equals("http") && port != 80)||(scheme.equals("https") && port != 443)) {
			url.append(":");
			url.append(port);
		}
		url.append(request.getRequestURI());
		
		return url;
	}

   /**
    * Constructs and returns a List of OAuth.Parameter objects, one per
    * parameter in the passed request.
    * 
    * @param  request Servlet request object with methods for retrieving the
    *         full set of parameters passed with the request
    * @see    http://wiki.opensocial.org/index.php?title=Validating_Signed_Requests
    */
	protected static final List<OAuth.Parameter> parseRequestParameters(HttpServletRequest request)
	{
		List<OAuth.Parameter> parameters = new ArrayList<OAuth.Parameter>();

		for (Object e : request.getParameterMap().entrySet()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) e;

			for (String value : entry.getValue()) {
				parameters.add(new OAuth.Parameter(entry.getKey(), value));
			}
		}

		return parameters;
	}

}
