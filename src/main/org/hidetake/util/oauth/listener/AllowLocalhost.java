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

public class AllowLocalhost implements ValidationEventListener
{

	private static final Logger log = Logger.getLogger(AllowLocalhost.class.getName());
	
	public void init(InitContext context)
	throws ServletException
	{
	}
	
	public boolean isSkippingValidation(ServletRequest arg0, ServletResponse arg1)
	{
		if("127.0.0.1".equals(arg0.getRemoteAddr()) || "0:0:0:0:0:0:0:1".equals(arg0.getRemoteAddr())) {
			log.info("Validation disabled: " + arg0.getRemoteHost() + " [" + arg0.getRemoteAddr() + "]");
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
