package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.ZcProduct;
import org.linlinjava.litemall.db.service.ZcProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中车产品信息控制层
 * @author yx
 */
@RestController
@RequestMapping("/admin/product")
@Validated
public class AdminZcProductController {
  private final Log logger = LogFactory.getLog(AdminZcProductController.class);

  @Autowired
  private ZcProductService zcProductService;
  @Autowired
  private PlatformTransactionManager txManager;

  @GetMapping("/list")
  public Object list(String productNum, String productName, String productType,
                     @RequestParam(defaultValue = "1") Integer page,
                     @RequestParam(defaultValue = "10") Integer limit,
                     @Sort @RequestParam(defaultValue = "add_time") String sort,
                     @Order @RequestParam(defaultValue = "desc") String order) {

    List<ZcProduct> productList = zcProductService.querySelective(productNum, productName, productType, page, limit, sort, order);
    int total = zcProductService.countSelective(productNum, productName, productType);
    Map<String, Object> data = new HashMap<>();
    data.put("total", total);
    data.put("items", productList);

    return ResponseUtil.ok(data);
  }

  @PostMapping("/create")
  public Object create(@LoginAdmin Integer adminId, @RequestBody ZcProduct product) {
    if (adminId == null) {
      return ResponseUtil.unlogin();
    }

    // 开启事务管理
    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    TransactionStatus status = txManager.getTransaction(def);

    try {
      zcProductService.add(product);
    } catch (Exception ex) {
      txManager.rollback(status);
      logger.error("系统内部错误", ex);
    }
    txManager.commit(status);

    return ResponseUtil.ok();
  }

  @GetMapping("/detail")
  public Object detail(@LoginAdmin Integer adminId, @NotNull Integer id) {
    if (adminId == null) {
      return ResponseUtil.unlogin();
    }

    ZcProduct product = zcProductService.findById(id);

    Map<String, Object> data = new HashMap<>();
    data.put("product", product);

    return ResponseUtil.ok(data);
  }

  @PostMapping("/update")
  public Object update(@LoginAdmin Integer adminId, @RequestBody ZcProduct product) {
    if (adminId == null) {
      return ResponseUtil.unlogin();
    }

    // 开启事务管理
    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    TransactionStatus status = txManager.getTransaction(def);

    try {
      zcProductService.updateById(product);
    } catch (Exception ex) {
      txManager.rollback(status);
      logger.error("系统内部错误", ex);
    }
    txManager.commit(status);

    return ResponseUtil.ok();
  }

  @PostMapping("/delete")
  public Object delete(@LoginAdmin Integer adminId, @RequestBody ZcProduct product) {
    if (adminId == null) {
      return ResponseUtil.unlogin();
    }

    // 开启事务管理
    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    TransactionStatus status = txManager.getTransaction(def);
    try {

      Integer productId = product.getId();
      zcProductService.deleteById(productId);
    } catch (Exception ex) {
      txManager.rollback(status);
      logger.error("系统内部错误", ex);
    }
    txManager.commit(status);
    return ResponseUtil.ok();
  }

  @PostMapping("/import")
  public Object create(@LoginAdmin Integer adminId, @RequestParam("file") MultipartFile file) throws Exception {
    if (adminId == null) {
      return ResponseUtil.unlogin();
    }
    String fileName = file.getOriginalFilename();
    zcProductService.batchImport(fileName, file);

    Map<String, Object> data = new HashMap<>();
    return ResponseUtil.ok(data);
  }
}
