package cn.tedu.csmall.product1.service;

import cn.tedu.csmall.product1.pojo.dto.AdminLoginInfoDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface IAdminService {
    String login(AdminLoginInfoDTO adminLoginInfoDTO);
}
