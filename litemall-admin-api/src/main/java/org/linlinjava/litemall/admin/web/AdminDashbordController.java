package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.service.*;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/dashboard")
@Validated
public class AdminDashbordController {
    private final Log logger = LogFactory.getLog(AdminDashbordController.class);

    @Autowired
    private LitemallUserService userService;
    @Autowired
    private ZcProductService zcProductService;

    @GetMapping("")
    public Object info(@LoginAdmin Integer adminId){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        int userTotal = userService.count();
        int zcProductTotal = zcProductService.countSelective(null, null, null, null, null, null, null);
        Map<String, Integer> data = new HashMap<>();
        data.put("userTotal", userTotal);
        data.put("zcProductTotal", zcProductTotal);

        return ResponseUtil.ok(data);
    }

}
