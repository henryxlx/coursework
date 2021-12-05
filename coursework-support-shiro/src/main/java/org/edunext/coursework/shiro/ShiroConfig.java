package org.edunext.coursework.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.edunext.coursework.kernel.service.AppUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xulixin
 */
@Configuration
public class ShiroConfig {

    static final String DEFAULT_HASH_ALGORITHM_NAME = Sha256Hash.ALGORITHM_NAME;
    static final int DEFAULT_HASH_ITERATIONS = 1024;

    /**
     * 设置用于匹配密码的CredentialsMatcher
     */
    @Bean
    public HashedCredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法，这里使用更安全的sha256算法
        credentialsMatcher.setHashAlgorithmName(DEFAULT_HASH_ALGORITHM_NAME);
        // 数据库存储的密码字段使用HEX还是BASE64方式加密，false是使用BASE64方式加密
        credentialsMatcher.setStoredCredentialsHexEncoded(false);
        // 散列迭代次数
        credentialsMatcher.setHashIterations(DEFAULT_HASH_ITERATIONS);
        return credentialsMatcher;
    }

    @Bean
    public UserRealm userRealm(AppUserService accountService,
                               HashedCredentialsMatcher credentialsMatcher) {

        UserRealm userRealm = new UserRealm(accountService);
        // 配置使用哈希密码匹配
        userRealm.setCredentialsMatcher(credentialsMatcher);
        return userRealm;
    }

    /**
     * 配置security并设置userReaml，避免报错：xxxx required a bean named 'authorizer' that could not be found.
     */
    @Bean
    public DefaultWebSecurityManager securityManager(UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(mySessionManager());
        return securityManager;
    }

    @Bean
    public DefaultWebSessionManager mySessionManager(){
        DefaultWebSessionManager defaultSessionManager = new DefaultWebSessionManager();
        // 将sessionIdUrlRewritingEnabled属性设置成false
        defaultSessionManager.setSessionIdUrlRewritingEnabled(false);
        return defaultSessionManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        // 配置拦截器,实现无权限返回401,而不是跳转到登录页
        // filters.put("authc", new FormLoginFilter());
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        // 拦截器
        shiroFilterFactoryBean.setFilterChainDefinitionMap(getFilterChainDefinitionMap());
        return shiroFilterFactoryBean;
    }

    private Map<String, String> getFilterChainDefinitionMap() {
        Map<String, String> map = new LinkedHashMap<>();
        // 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边
        map.put("/install", "anon");
        map.put("/install/step?", "anon");
        map.put("/login", "anon");
        map.put("/password/reset", "anon");
        map.put("/search", "anon");
        map.put("/register", "anon");
        map.put("/index", "anon");
        map.put("/", "anon");
        map.put("/assets/**", "anon");
        map.put("/bundles/**", "anon");
        map.put("/install/**", "anon");
        map.put("/themes/**", "anon");

        // authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        // all other paths require a logged in user
        map.put("/logout", "logout");
        map.put("/**", "authc");
        return map;
    }

}
