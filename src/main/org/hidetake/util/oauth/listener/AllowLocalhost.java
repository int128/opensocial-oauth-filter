package org.hidetake.util.oauth.listener;

import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthException;

import org.hidetake.util.oauth.ValidationEventListener;

public class AllowLocalhost implements ValidationEventListener
{

	private static final Logger log = Logger.getLogger(AllowLocalhost.class.getName());
	
	@Override
	public void init(FilterConfig config) throws ServletException
	{
	}
	
	@Override
	public boolean isSkippingValidation(ServletRequest arg0, ServletResponse arg1)
	{
		if("127.0.0.1".equals(arg0.getRemoteAddr())) {
			log.info("validation disabled: 127.0.0.1");
			return true;
		}
		return false;
	}

	@Override
	public void manipulateURL(StringBuilder arg0, HttpServletRequest arg1)
	{
	}

	@Override
	public boolean onOAuthException(HttpServletRequest arg0, HttpServletResponse arg1, OAuthException arg2)
	{
		return false;
	}

	@Override
	public boolean onURISyntaxException(HttpServletRequest arg0, HttpServletResponse arg1, URISyntaxException arg2)
	{
		return false;
	}

	@Override
	public void onValidationComplete(HttpServletResponse arg0)
	{
	}

}