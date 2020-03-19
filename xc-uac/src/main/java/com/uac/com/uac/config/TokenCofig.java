package com.uac.com.uac.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class TokenCofig {
	//基于远程访问时token的配置
//	 @Bean public TokenStore tokenStore() {
//
//	 	return new InMemoryTokenStore();
//	 }



	//jwt 令牌存储方案
	private String SIGNING_KEY = "lft_data_center";

	@Bean
	public TokenStore tokenStore() {
		// JWT令牌存储方案
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(SIGNING_KEY); // 对称秘钥，资源服务器使用该秘钥来验证
		return converter;
	}
}
