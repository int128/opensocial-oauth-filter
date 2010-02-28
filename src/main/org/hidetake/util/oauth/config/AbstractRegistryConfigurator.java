package org.hidetake.util.oauth.config;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.signature.RSA_SHA1;


/**
 * Registry configurator base class.
 * 
 * @author hidetake.org
 *
 */
public abstract class AbstractRegistryConfigurator implements RegistryConfigurator
{

	protected OAuthAccessor createAccessorRSASHA1(String consumerKey, String certificate)
	{
		OAuthConsumer consumer = new OAuthConsumer(null, consumerKey, null, null);
		consumer.setProperty(RSA_SHA1.X509_CERTIFICATE, certificate);
		OAuthAccessor oauthAccessor = new OAuthAccessor(consumer);
		return oauthAccessor;
	}

}
