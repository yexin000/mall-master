package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.ZcMessage;
import org.linlinjava.litemall.db.service.ZcMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户留言后台控制层
 */
@RestController
@RequestMapping("/admin/message")
@Validated
public class AdminZcMessageController {
  private final Log logger = LogFactory.getLog(AdminZcMessageController.class);

  @Autowired
  private ZcMessageService zcMessageService;

  @GetMapping("/list")
  public Object list(String phone, String name,
                     @RequestParam(defaultValue = "1") Integer page,
                     @RequestParam(defaultValue = "10") Integer limit,
                     @Sort @RequestParam(defaultValue = "createtime") String sort,
                     @Order @RequestParam(defaultValue = "desc") String order) {
    List<ZcMessage> messageList = zcMessageService.querySelective(phone, name, page, limit, sort, order);
    int total = zcMessageService.countSelective(phone, name);
    Map<String, Object> data = new HashMap<>();
    data.put("total", total);
    data.put("items", messageList);

    return ResponseUtil.ok(data);
  }

  @PostMapping("/delete")
  public Object delete(@LoginAdmin Integer adminId, @RequestBody ZcMessage message){
    if (adminId == null) {
      return ResponseUtil.unlogin();
    }
    zcMessageService.deleteById(message.getId());
    return ResponseUtil.ok();
  }
}
