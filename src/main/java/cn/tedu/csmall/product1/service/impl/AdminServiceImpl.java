package cn.tedu.csmall.product1.service.impl;

import cn.tedu.csmall.product1.IAdminService;
import cn.tedu.csmall.product1.pojo.dto.AdminLoginInfoDTO;
import cn.tedu.csmall.product1.pojo.vo.AdminListItemVO;
import cn.tedu.csmall.product1.security.AdminDetails;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {
    @Value("${csmall.jwt.secret-key}")
    private String secretKey;
    @Value("${csmall.jwt.duration-in-minute}")
    private Long durationInMinute;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public String login(AdminLoginInfoDTO adminLoginInfoDTO) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(adminLoginInfoDTO.getUsername(),adminLoginInfoDTO.getPassword());
        Authentication authenticateResult = authenticationManager.authenticate(authentication);
        AdminDetails adminDetails = (AdminDetails)authenticateResult.getPrincipal();
        Map<String,Object> claims = new HashMap<>();
        claims.put("id",adminDetails.getId());
        claims.put("username",adminDetails.getUsername());
        String authoritiesJsonString = JSON.toJSONString(adminDetails.getAuthorities());
        claims.put("authoritiesJsonString",authoritiesJsonString);
        String jwt = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                // Payload：数据，具体表现为Claims
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + durationInMinute*60*1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return jwt;
    }

    @Override
    public List<AdminListItemVO> list() {
        List<AdminListItemVO> list = new ArrayList<>();
        AdminListItemVO a = new AdminListItemVO();
        a.setUsername("swj");
        list.add(a);
        return list;
    }
}
