package org.hidetake.util.oauth;

import java.net.URISyntaxException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	 * Called if validation was failed, caused by {@link URISyntaxException}.
	 * 
	 * <p>Default implementation: sends 403 response to web browser.</p>
	 * 
	 * @return true if response has been sent, false otherwise 
	 * @param request servlet request object
	 * @param response servlet response object
	 * @param e exception
	 */
	public boolean onURISyntaxException(HttpServletRequest request, HttpServletResponse response, URISyntaxException e);

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