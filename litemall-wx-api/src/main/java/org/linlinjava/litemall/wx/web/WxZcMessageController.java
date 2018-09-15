package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.ZcMessage;
import org.linlinjava.litemall.db.service.ZcMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序中车留言接口
 */
@RestController
@RequestMapping("/wx/message")
public class WxZcMessageController {
  private final Log logger = LogFactory.getLog(WxZcMessageController.class);

  @Autowired
  private ZcMessageService zcMessageService;

  @Autowired
  private PlatformTransactionManager txManager;

  @PostMapping("save")
  public Object save(@RequestBody ZcMessage message) {
    // 开启事务管理
    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    TransactionStatus status = txManager.getTransaction(def);

    try {
      zcMessageService.add(message);
    } catch (Exception ex) {
      txManager.rollback(status);
      logger.error("系统内部错误", ex);
    }
    txManager.commit(status);

    return ResponseUtil.ok();
  }
}
