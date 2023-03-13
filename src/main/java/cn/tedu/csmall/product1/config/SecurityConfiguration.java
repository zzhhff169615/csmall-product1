package cn.tedu.csmall.product1.config;

import cn.tedu.csmall.product1.filter.JwtAuthorizationFilter;
import cn.tedu.csmall.product1.web.JsonResult;
import cn.tedu.csmall.product1.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    // 【注意】配置AuthenticationManager对象时
    // 不要使用authenticationManager()方法，如果使用此方法，在测试时可能导致死循环，从而内存溢出
    // 必须使用authenticationManagerBean()方法
//     @Bean
//     @Override
//     protected AuthenticationManager authenticationManager() throws Exception {
//         return super.authenticationManager();
//     }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置Spring Security创建Session的策略：STATELESS=从不使用Session，NEVER=不主动创建Session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 将JWT过滤器添加到Spring Security框架的过滤器链中合适的位置
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        // 处理未通过认证时导致的拒绝访问
        http.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                String message = "您当前未登录，请登录！";
                log.warn("{}", message);
                response.setContentType("application/json; charset=utf-8");
                PrintWriter writer = response.getWriter();
                JsonResult<Void> jsonResult = JsonResult.fail(ServiceCode.ERROR_UNAUTHORIZED, message);
                String jsonString = JSON.toJSONString(jsonResult);
                writer.println(jsonString);
                writer.close();
            }
        });

        // 禁用“防止伪造的跨域攻击”的防御机制
        http.csrf().disable();

        // 允许复杂请求的跨域访问
        http.cors();

        // 白名单
        // 使用1个星号，表示通配此层级的任意资源，例如：/admins/*，可以匹配：/admins/add-new、/admins/delete
        // 但是，不可以匹配多个层级，例如：/admins/*，不可以匹配：/admins/9527/delete
        // 使用2个连续的星号，表示通配若干层级的任意资源，例如：/admins/*，可以匹配：/admins/add-new、/admins/9527/delete
        String[] urls = {
                "/doc.html",
                "/**/*.css",
                "/**/*.js",
                "/swagger-resources",
                "/v2/api-docs",
                "/admins/login" // 管理员登录的URL
        };

        // 基于请求的访问控制
        http.authorizeRequests() // 对请求进行授权
                .mvcMatchers(urls) // 匹配某些请求
                .permitAll() // 直接许可，即不需要认证即可访问
                .anyRequest() // 任意请求
                .authenticated(); // 要求通过认证的

        // 如果调用以下方法，当需要访问通过认证的资源，但是未通过认证时，将自动跳转到登录页面
        // 如果未调用以下方法，将响应403
        // http.formLogin();

        // super.configure(http); // 不要保留调用父类同名方法的代码，不要保留！不要保留！不要保留！
    }

}
