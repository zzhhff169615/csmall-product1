package cn.tedu.csmall.product1.filter;

import cn.tedu.csmall.product1.security.LoginPrincipal;
import cn.tedu.csmall.product1.web.JsonResult;
import cn.tedu.csmall.product1.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Value("${csmall.jwt.secret-key}")
    private String secretKey;
    private static final int JWT_MIN_LENGTH = 113;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("JWT过滤器开始执行...");
        String jwt = request.getHeader("Authorization");
        System.out.println(jwt);
        if(!StringUtils.hasText(jwt) || jwt.length()<JWT_MIN_LENGTH){
            filterChain.doFilter(request,response);
            return;
        }

        Claims claims = null;
        response.setContentType("application/json; charset=utf-8");
        try{
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
        }catch (MalformedJwtException e){
            String message = "非法访问";
            PrintWriter writer = response.getWriter();
            JsonResult<Void> jsonResult = JsonResult.fail(ServiceCode.ERROR_JWT_MALFORMED,message);
            String jsonString = JSON.toJSONString(jsonResult);
            writer.println(jsonString);
            writer.close();
        }catch (SignatureException e) {
            String message = "非法访问！";
            log.warn("解析JWT时出现SignatureException，将响应错误消息：{}", message);
            PrintWriter writer = response.getWriter();
            JsonResult<Void> jsonResult = JsonResult.fail(ServiceCode.ERROR_JWT_SIGNATURE, message);
            String jsonString = JSON.toJSONString(jsonResult);
            writer.println(jsonString);
            writer.close();
            return;
        } catch (ExpiredJwtException e) {
            String message = "您的登录信息已过期，请重新登录！";
            log.warn("解析JWT时出现ExpiredJwtException，将响应错误消息：{}", message);
            PrintWriter writer = response.getWriter();
            JsonResult<Void> jsonResult = JsonResult.fail(ServiceCode.ERROR_JWT_EXPIRED, message);
            String jsonString = JSON.toJSONString(jsonResult);
            writer.println(jsonString);
            writer.close();
            return;
        } catch (Throwable e) {
            String message = "服务器忙，请稍后再次尝试！（开发过程中，如果看到此提示，请检查控制台的信息，并补充处理异常的方法）";
            log.warn("解析JWT时出现Throwable，将响应错误消息：{}", message);
            log.warn("异常类型：{}", e.getClass());
            log.warn("异常信息：{}", e.getMessage());
            e.printStackTrace(); // 打印异常的跟踪信息，主要是为了在开发阶段更好的检查出现异常的原因
            PrintWriter writer = response.getWriter();
            JsonResult<Void> jsonResult = JsonResult.fail(ServiceCode.ERROR_UNKNOWN, message);
            String jsonString = JSON.toJSONString(jsonResult);
            writer.println(jsonString);
            writer.close();
            return;
        }
        Long id = claims.get("id",Long.class);
        String username = claims.get("username",String.class);
        String authoritiesJsonString = claims.get("authoritiesJsonString",String.class);
        LoginPrincipal loginPrincipal = new LoginPrincipal();
        loginPrincipal.setId(id);
        loginPrincipal.setUsername(username);
        List<SimpleGrantedAuthority> authorities = JSON.parseArray(authoritiesJsonString,SimpleGrantedAuthority.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginPrincipal,null,authorities);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        filterChain.doFilter(request,response);
    }
}
