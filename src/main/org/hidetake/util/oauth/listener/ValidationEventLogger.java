package org.hidetake.util.oauth.listener;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthException;

import org.hidetake.util.oauth.ValidationEventListener;
import org.hidetake.util.oauth.config.InitContext;
import org.hidetake.util.oauth.model.OpenSocialException;

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

	public void init(InitContext context) throws ServletException
	{
		for(ValidationEventListener listener : context.getValidationEventListeners()) {
			log.info("Registered event listener: " + listener.getClass().getName());
		}
	}
	
	public void manipulateURL(StringBuilder url, HttpServletRequest request)
	{
		log.fine("OAuth message URL: " + url);
	}

	public boolean onOAuthException(HttpServletRequest request, HttpServletResponse response, OAuthException e)
	{
		log.severe("Validation failed: " + e.getLocalizedMessage());
		e.printStackTrace();
		return false;
	}

	public boolean onOpenSocialException(HttpServletRequest request, HttpServletResponse response, OpenSocialException e)
	{
		log.severe("Validation failed: " + e.getLocalizedMessage());
		return false;
	}

	public void onValidationComplete(HttpServletResponse response)
	{
		log.info("Validation complete");
	}

	public boolean isSkippingValidation(ServletRequest req, ServletResponse res)
	{
		return false;
	}

}
