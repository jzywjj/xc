package com.uac.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@SessionAttributes("authorizationRequest")
public class ConfirmAccessController {
	/**
	 * 提交到  /oauth/authorize
	 * @param model
	 * @param request
	 * @param response
	 * @param attr
	 * @throws IOException
	 */
	@RequestMapping("/lft/confirm_access")
	public void ConfirmAccess(Map<String, Object> model, HttpServletRequest request,HttpServletResponse response,RedirectAttributes attr) throws IOException {
		AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
		String clientId = authorizationRequest.getClientId();
		Collection<? extends GrantedAuthority> authorities = authorizationRequest.getAuthorities();
		Map<String, String> approvalParameters = authorizationRequest.getApprovalParameters();
		String redirectUri = authorizationRequest.getRedirectUri();
		Set<String> scope = authorizationRequest.getScope();
		
		attr.addFlashAttribute("clientId", clientId);
		attr.addFlashAttribute("authorities", authorities);
		
		response.sendRedirect("http://www.baidu.com");
	}
}
