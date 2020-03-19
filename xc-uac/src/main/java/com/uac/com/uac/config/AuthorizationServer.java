package com.uac.com.uac.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {


    public AuthorizationServer() {
        super();
    }


    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * AuthorizationServerSecurityConfigurer：
     * 用来配置令牌端点的安全约束.
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }



    @Autowired
    private PasswordEncoder passwordEncoder;



    //将客户端信息存储到数据库
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }



    /**
     * ClientDetailsServiceConfigurer：用来配置客户端详情服务（ClientDetailsService），
     * 客户端详情信息在 这里进行初始化，你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

         clients.withClientDetails(clientDetailsService);
        
        
       // clients.inMemory()// 使用in-memory存储
//                .withClient("lft")// client_id
//                .secret(new BCryptPasswordEncoder().encode("lft"))// 客户端密钥
//                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit",
//                        "refresh_token")// 该client允许的授权类型authorization_code,password,refresh_token,implicit,client_credentials
//                .scopes("all")// 允许的授权范围
//                .autoApprove(false)// false跳转到授权页面
//                // 加上验证回调地址
//                .authorities("p2")
//                .redirectUris("http://www.baidu.com");

    }

    /**
     * AuthorizationServerEndpointsConfigurer：
     * 用来配置令牌（token）的访问端点和令牌服务(token services)。
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .authorizationCodeServices(authorizationCodeServices)
                .tokenServices(tokenService())
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)
                .pathMapping("/oauth/confirm_access", "/lft/confirm_access");
    }


//    @Bean
//    public AuthorizationServerTokenServices tokenService() {
//        DefaultTokenServices service = new DefaultTokenServices();
//        service.setClientDetailsService(clientDetailsService);
//        service.setSupportRefreshToken(true);
//        service.setTokenStore(tokenStore);
//        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
//        service.setRefreshTokenValiditySeconds(259200);// 刷新令牌默认有效期3天
//        return service;
//    }

    //基于JWT token 配置令牌增强
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService);
        service.setSupportRefreshToken(true);
        service.setTokenStore(tokenStore);
        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200);// 刷新令牌默认有效期3天
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);
        return service;
    }

//    @Bean
//    public AuthorizationCodeServices authorizationCodeServices() {
//        // 设置授权码模式的授权码如何 存取，暂时采用内存方式
//        return new InMemoryAuthorizationCodeServices();
//    }
    
    

    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);//设置授权码模式的授权码如何存取
       
    }
}
