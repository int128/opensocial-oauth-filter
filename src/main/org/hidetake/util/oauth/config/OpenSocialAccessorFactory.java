package org.hidetake.util.oauth.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;

import org.hidetake.util.oauth.ValidationEventListener;
import org.hidetake.util.oauth.model.OpenSocialAccessor;
import org.xml.sax.SAXException;

/**
 * Configuration XML file parser.
 * 
 * @author hidetake.org
 *
 */
public class OpenSocialAccessorFactory
{

	private final XPathEvaluator evaluator;
	
	public OpenSocialAccessorFactory(InputStream stream) throws OpenSocialAccessorConfigurationException
	{
		try {
			evaluator = new XPathEvaluator(stream);
		}
		catch (SAXException e) {
			throw new OpenSocialAccessorConfigurationException(e);
		}
		catch (IOException e) {
			throw new OpenSocialAccessorConfigurationException(e);
		}
		catch (ParserConfigurationException e) {
			throw new OpenSocialAccessorConfigurationException(e);
		}
	}

	public OpenSocialAccessor getOpenSocialAccessor()
	{
		// get application data
		String appId = evaluator.getString("/config/opensocial-apps/opensocial-app/@app-id");
		String appUrl = evaluator.getString("/config/opensocial-apps/opensocial-app/@app-url");
		String containerId = evaluator.getString("/config/opensocial-apps/opensocial-app/@container-id");
		
		// get oauth data
		OAuthAccessor oauthAccessor = getOAuthAccessor(containerId);
		
		return new OpenSocialAccessor(appId, appUrl, oauthAccessor);
	}

	public OAuthAccessor getOAuthAccessor(String containerId)
	{
		// get container data
		Map<QName, Object> variableMap = new HashMap<QName, Object>();
		variableMap.put(new QName("id"), containerId);
		evaluator.setVariable(variableMap);
		
		String consumerKey = evaluator.getString(
			"/config/opensocial-containers/opensocial-container[@id=$id]/oauth/@consumer-key"
			);
		String signatureMethod = evaluator.getString(
			"/config/opensocial-containers/opensocial-container[@id=$id]/oauth/@signature-method"
			);
		String cert = evaluator.getString(
			"/config/opensocial-containers/opensocial-container[@id=$id]/oauth/certificate/text()"
			).trim();
		
		// initialize accessor
		OAuthConsumer consumer = new OAuthConsumer(null, consumerKey, null, null);
		consumer.setProperty(signatureMethod + ".X509Certificate", cert);
		OAuthAccessor oauthAccessor = new OAuthAccessor(consumer);
		return oauthAccessor;
	}

	public List<ValidationEventListener> getValidationEventListeners() throws OpenSocialAccessorConfigurationException
	{
		try {
			List<ValidationEventListener> result = new ArrayList<ValidationEventListener>();
			
			for(String id : evaluator.getNodeValueList("/config/features/feature/@id")) {
				ValidationEventListener eventListener = (ValidationEventListener) Class.forName(id).newInstance();
				result.add(eventListener);
			}
			
			return result;
		}
		catch (InstantiationException e) {
			throw new OpenSocialAccessorConfigurationException(e);
		}
		catch (IllegalAccessException e) {
			throw new OpenSocialAccessorConfigurationException(e);
		}
		catch (ClassNotFoundException e) {
			throw new OpenSocialAccessorConfigurationException(e);
		}
	}

}
