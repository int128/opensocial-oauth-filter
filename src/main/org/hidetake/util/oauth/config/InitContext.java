package org.hidetake.util.oauth.config;

import java.util.List;

import javax.servlet.FilterConfig;

import org.hidetake.util.oauth.ValidationEventListener;
import org.hidetake.util.oauth.model.OpenSocialAccessor;

/**
 * Servlet filter initialization context.
 * 
 * @author hidetake.org
 *
 */
public interface InitContext
{

	public FilterConfig getFilterConfig();
	
	public OpenSocialAccessor getOpenSocialAccessor();
	
	public List<ValidationEventListener> getValidationEventListeners();

}
