package com.order.contoller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

//	@Autowired
//	private ResourceServerTokenServices resourceServerTokenServices;
//	
//	@Autowired
//	private AccessTokenConverter accessTokenConverter;
//	
	@Autowired
	private ApplicationContext ctx;
	@Autowired
	JwtAccessTokenConverter jwtAccessTokenConverter;

	@RequestMapping("/hello/one")
	@PreAuthorize("hasAuthority('p1')")
	public String he(HttpServletRequest req, @RequestHeader(name = "Authorization") String authorization) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof OAuth2Authentication)) {
			// 无token访问网关内资源的情况，目 前仅有uua服务直接暴露
			return null;
		}
		OAuth2Authentication oauth2Authentication = (OAuth2Authentication) authentication;
		Authentication userAuthentication = oauth2Authentication.getUserAuthentication();
		Object principal = userAuthentication.getPrincipal();

		String token = authorization.replace("Bearer", "").trim();

	return"hello one";
	}

	@RequestMapping("/hello/two")
    @PreAuthorize("hasAuthority('p2')")
    public String he1() {

        return "hello two";
    }

	@RequestMapping("/hello/three")
	public String he2() {

		return "hello three";
	}

	public void ss() {

	}

//    private Map<String, ?> checkToken(String value) {
//
//		OAuth2AccessToken token = resourceServerTokenServices.readAccessToken(value);
//		if (token == null) {
//			throw new InvalidTokenException("Token was not recognised");
//		}
//
//		if (token.isExpired()) {
//			throw new InvalidTokenException("Token has expired");
//		}
//
//		OAuth2Authentication authentication = resourceServerTokenServices.loadAuthentication(token.getValue());
//
//		Map<String, Object> response = (Map<String, Object>)accessTokenConverter.convertAccessToken(token, authentication);
//
//		// gh-1070
//		response.put("active", true);	// Always true if token exists and not expired
//
//		return response;
//	}
}
