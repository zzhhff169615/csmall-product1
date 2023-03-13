package cn.tedu.csmall.product1;

import cn.tedu.csmall.product1.pojo.dto.AdminLoginInfoDTO;
import cn.tedu.csmall.product1.pojo.vo.AdminListItemVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IAdminService {
    String login(AdminLoginInfoDTO adminLoginInfoDTO);

    List<AdminListItemVO> list();
}
