package org.hidetake.util.oauth.listener;

import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthException;

import org.hidetake.util.oauth.ValidationEventListener;

/**
 * 
 * Logger for validation events.
 * 
 * @author hidetake.org
 *
 */
public class ValidationEventLogger implements ValidationEventListener
{
	private static final Logger log = Logger.getLogger(ValidationEventLogger.class.getName());

	@Override
	public void init(FilterConfig config, List<ValidationEventListener> listenerList)
	throws ServletException
	{
		for(ValidationEventListener listener : listenerList) {
			log.info("Registered event listener: " + listener.getClass().getName());
		}
	}
	
	@Override
	public void manipulateURL(StringBuilder url, HttpServletRequest request)
	{
		log.fine("OAuth message URL: " + url);
	}

	@Override
	public boolean onOAuthException(HttpServletRequest request, HttpServletResponse response, OAuthException e)
	{
		log.severe("Validation failed: " + e.getLocalizedMessage());
		return false;
	}

	@Override
	public boolean onURISyntaxException(HttpServletRequest request, HttpServletResponse response, URISyntaxException e)
	{
		log.severe("Validation failed: " + e.getLocalizedMessage());
		return false;
	}

	@Override
	public void onValidationComplete(HttpServletResponse response)
	{
		log.info("Validation complete");
	}

	@Override
	public boolean isSkippingValidation(ServletRequest req, ServletResponse res)
	{
		return false;
	}

}
