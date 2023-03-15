package cn.tedu.csmall.product1.controller;

import cn.tedu.csmall.product1.IAdminService;
import cn.tedu.csmall.product1.pojo.dto.AdminLoginInfoDTO;
import cn.tedu.csmall.product1.pojo.vo.AdminListItemVO;
import cn.tedu.csmall.product1.security.LoginPrincipal;
import cn.tedu.csmall.product1.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admins")
@Api(tags = "01. 管理员管理模块")
public class AdminController {
    @Autowired
    @Qualifier("adminServiceImpl")
    private IAdminService adminService;
    @ApiOperation("管理员登录")
    @ApiOperationSupport(order = 10)
    @PostMapping("/login")
    public JsonResult<String> login(AdminLoginInfoDTO adminLoginInfoDTO){
        String jwt = adminService.login(adminLoginInfoDTO);
        return JsonResult.ok(jwt);
    }
    @ApiOperation("查询管理员列表")
    @ApiOperationSupport(order = 420)
    @PreAuthorize("hasAuthority('/ams/admin/read')")
    @GetMapping("")
    public JsonResult<List<AdminListItemVO>> list(
            @ApiIgnore @AuthenticationPrincipal LoginPrincipal loginPrincipal) {
        log.debug("开始处理【查询管理员列表】的请求，参数：无");
        log.debug("当事人信息：{}", loginPrincipal);
        log.debug("当事人信息中的ID：{}", loginPrincipal.getId());
        log.debug("当事人信息中的用户名：{}", loginPrincipal.getUsername());
        List<AdminListItemVO> list = adminService.list();
        return JsonResult.ok(list);
    }
}
