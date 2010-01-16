package org.hidetake.util.oauth.listener;

import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthException;

import org.hidetake.util.oauth.ValidationEventListener;

/**
 * 
 * Logger class for validation events.
 * 
 * @author hidetake.org
 *
 */
public class ValidationEventLogger implements ValidationEventListener
{
	private static final Logger log = Logger.getLogger(ValidationEventLogger.class.getName());

	@Override
	public void manipulateURL(StringBuilder url, HttpServletRequest request)
	{
		log.info("OAuth message URL: " + url);
	}

	@Override
	public boolean onOAuthException(HttpServletRequest request, HttpServletResponse response, OAuthException e)
	{
		log.warning("Validation failed: " + e.getLocalizedMessage());
		return false;
	}

	@Override
	public boolean onURISyntaxException(HttpServletRequest request, HttpServletResponse response, URISyntaxException e)
	{
		log.warning("Validation failed: " + e.getLocalizedMessage());
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
