package org.hidetake.util.oauth.listener;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthException;

import org.hidetake.util.oauth.OpenSocialException;
import org.hidetake.util.oauth.ValidationEventListener;

public class AllowLocalhost implements ValidationEventListener
{

	private static final Logger log = Logger.getLogger(AllowLocalhost.class.getName());
	
	public void init(FilterConfig config, List<ValidationEventListener> listenerList)
	throws ServletException
	{
	}
	
	public boolean isSkippingValidation(ServletRequest arg0, ServletResponse arg1)
	{
		if("127.0.0.1".equals(arg0.getRemoteAddr())) {
			log.info("validation disabled: 127.0.0.1");
			return true;
		}
		return false;
	}

	public void manipulateURL(StringBuilder arg0, HttpServletRequest arg1)
	{
	}

	public boolean onOAuthException(HttpServletRequest arg0, HttpServletResponse arg1, OAuthException arg2)
	{
		return false;
	}

	public boolean onOpenSocialException(HttpServletRequest request, HttpServletResponse response, OpenSocialException e)
	{
		return false;
	}

	public void onValidationComplete(HttpServletResponse arg0)
	{
	}

}
