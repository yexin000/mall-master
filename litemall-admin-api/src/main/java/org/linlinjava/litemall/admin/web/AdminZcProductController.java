package org.linlinjava.litemall.admin.web;

import jodd.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.core.storage.StorageService;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.ZcProduct;
import org.linlinjava.litemall.db.domain.ZcProducttype;
import org.linlinjava.litemall.db.service.ZcProductService;
import org.linlinjava.litemall.db.service.ZcProducttypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
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
  private StorageService storageService;
  @Autowired
  private ZcProducttypeService zcProducttypeService;
  @Autowired
  private PlatformTransactionManager txManager;

  @GetMapping("/list")
  public Object list(String productNum, String productName, String productType,
                     @RequestParam(defaultValue = "1") Integer page,
                     @RequestParam(defaultValue = "10") Integer limit,
                     @Sort @RequestParam(defaultValue = "add_time") String sort,
                     @Order @RequestParam(defaultValue = "desc") String order) {

    List<ZcProduct> productList = zcProductService.querySelective(productNum, productName, productType, null, null, page, limit, sort, order, null, null);
    int total = zcProductService.countSelective(productNum, productName, productType, null, null, null, null);
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


  @PostMapping("/importPics")
  public Object importPics(@LoginAdmin Integer adminId, @RequestParam("file") MultipartFile file) throws Exception {
    if (adminId == null) {
      return ResponseUtil.unlogin();
    }

    String originalFilename = file.getOriginalFilename();
    String extUpp = StringUtil.toUpperCase(originalFilename.substring(originalFilename.lastIndexOf(".") + 1));
    if (!extUpp.matches("^[(JPG)|(PNG)]+$")) {
      return ResponseUtil.fail();
    }

    if (file.getSize() > 5 * 1024 * 1024) {
      return ResponseUtil.fail();
    }

    String fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
    String[] fileNameFilters = fileName.split("_");
    if (fileNameFilters.length == 2 && ("1".equals(fileNameFilters[1]) || "2".equals(fileNameFilters[1]))) {
      String url = storageService.store(file.getInputStream(), file.getSize(), file.getContentType(), originalFilename);
      if ("1".equals(fileNameFilters[1])) {
        zcProductService.updateRealpicByProductNum(fileNameFilters[0], url);
      } else if ("2".equals(fileNameFilters[1])) {
        zcProductService.updateSnapshotByProductNum(fileNameFilters[0], url);
      }
    } else {
      return ResponseUtil.fail();
    }

    Map<String, Object> data = new HashMap<>();
    return ResponseUtil.ok(data);
  }

  /**
   * 查询产品类型列表
   * @return
   */
  @GetMapping("getProductTypes")
  public Object getProductTypes() {
    List<ZcProducttype> producttypes = zcProducttypeService.queryAllType();
    return ResponseUtil.ok(producttypes);
  }
}
