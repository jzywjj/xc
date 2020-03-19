package com.gateway.config;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public class CheckToken {
	private ResourceServerTokenServices resourceServerTokenServices;

	private AccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();

	protected final Log logger = LogFactory.getLog(getClass());

	private WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator = new DefaultWebResponseExceptionTranslator();

	public CheckToken(ResourceServerTokenServices resourceServerTokenServices) {
		this.resourceServerTokenServices = resourceServerTokenServices;
	}
	
	/**
	 * @param exceptionTranslator the exception translator to set
	 */
	public void setExceptionTranslator(WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

	/**
	 * @param accessTokenConverter the accessTokenConverter to set
	 */
	public void setAccessTokenConverter(AccessTokenConverter accessTokenConverter) {
		this.accessTokenConverter = accessTokenConverter;
	}

	@RequestMapping(value = "/oauth/check_token")
	@ResponseBody
	public Map<String, ?> checkToken(@RequestParam("token") String value) {

		OAuth2AccessToken token = resourceServerTokenServices.readAccessToken(value);
		if (token == null) {
			throw new InvalidTokenException("Token was not recognised");
		}

		if (token.isExpired()) {
			throw new InvalidTokenException("Token has expired");
		}

		OAuth2Authentication authentication = resourceServerTokenServices.loadAuthentication(token.getValue());

		Map<String, Object> response = (Map<String, Object>)accessTokenConverter.convertAccessToken(token, authentication);

		// gh-1070
		response.put("active", true);	// Always true if token exists and not expired

		return response;
	}
}
