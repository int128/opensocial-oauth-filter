package org.hidetake.util.oauth.provider;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;
import net.oauth.signature.RSA_SHA1;

/**
 * OAuth signature validator for mixi apps.
 * 
 * @author hidetake.org
 * 
 */
public class MixiOAuthAccessor extends OAuthAccessor
{

	private static final long serialVersionUID = 1L;
	
	private static final String CERT_MIXI_JP =
		"-----BEGIN CERTIFICATE-----\n"
		+ "MIICdzCCAeCgAwIBAgIJAOi/chE0MhufMA0GCSqGSIb3DQEBBQUAMDIxCzAJBgNV\n"
		+ "BAYTAkpQMREwDwYDVQQKEwhtaXhpIEluYzEQMA4GA1UEAxMHbWl4aS5qcDAeFw0w\n"
		+ "OTA0MjgwNzAyMTVaFw0xMDA0MjgwNzAyMTVaMDIxCzAJBgNVBAYTAkpQMREwDwYD\n"
		+ "VQQKEwhtaXhpIEluYzEQMA4GA1UEAxMHbWl4aS5qcDCBnzANBgkqhkiG9w0BAQEF\n"
		+ "AAOBjQAwgYkCgYEAwEj53VlQcv1WHvfWlTP+T1lXUg91W+bgJSuHAD89PdVf9Ujn\n"
		+ "i92EkbjqaLDzA43+U5ULlK/05jROnGwFBVdISxULgevSpiTfgbfCcKbRW7hXrTSm\n"
		+ "jFREp7YOvflT3rr7qqNvjm+3XE157zcU33SXMIGvX1uQH/Y4fNpEE1pmX+UCAwEA\n"
		+ "AaOBlDCBkTAdBgNVHQ4EFgQUn2ewbtnBTjv6CpeT37jrBNF/h6gwYgYDVR0jBFsw\n"
		+ "WYAUn2ewbtnBTjv6CpeT37jrBNF/h6ihNqQ0MDIxCzAJBgNVBAYTAkpQMREwDwYD\n"
		+ "VQQKEwhtaXhpIEluYzEQMA4GA1UEAxMHbWl4aS5qcIIJAOi/chE0MhufMAwGA1Ud\n"
		+ "EwQFMAMBAf8wDQYJKoZIhvcNAQEFBQADgYEAR7v8eaCaiB5xFVf9k9jOYPjCSQIJ\n"
		+ "58nLY869OeNXWWIQ17Tkprcf8ipxsoHj0Z7hJl/nVkSWgGj/bJLTVT9DrcEd6gLa\n"
		+ "H5TbGftATZCAJ8QJa3X2omCdB29qqyjz4F6QyTi930qekawPBLlWXuiP3oRNbiow\n"
		+ "nOLWEi16qH9WuBs=\n"
		+ "-----END CERTIFICATE-----";
	
	public MixiOAuthAccessor()
	{
		super(getConsumer());
	}

	private static final OAuthConsumer getConsumer()
	{
		OAuthServiceProvider provider = new OAuthServiceProvider(null, null, null);
		OAuthConsumer consumer = new OAuthConsumer(null, "mixi.jp", null, provider);
		consumer.setProperty(RSA_SHA1.X509_CERTIFICATE, CERT_MIXI_JP);
		return consumer;
	}
	
}
