package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.ZcCategory;
import org.linlinjava.litemall.db.service.ZcCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中车产品平台/车型控制层
 * @author yx
 */
@RestController
@RequestMapping("/admin/zcCategory")
@Validated
public class AdminZcCategoryController {
  private final Log logger = LogFactory.getLog(AdminZcCategoryController.class);

  @Autowired
  private ZcCategoryService zcCategoryService;
  @Autowired
  private PlatformTransactionManager txManager;

  @GetMapping("/list")
  public Object list(String name, Integer pid, Integer id,
                     @RequestParam(defaultValue = "1") Integer page,
                     @RequestParam(defaultValue = "10") Integer limit,
                     @Sort @RequestParam(defaultValue = "createtime") String sort,
                     @Order @RequestParam(defaultValue = "desc") String order) {

    List<ZcCategory> productList = zcCategoryService.querySelective(name, pid, id, page, limit, sort, order);
    int total = zcCategoryService.countSelective(name, pid, id);
    Map<String, Object> data = new HashMap<>();
    data.put("total", total);
    data.put("items", productList);

    return ResponseUtil.ok(data);
  }

  @PostMapping("/create")
  public Object create(@LoginAdmin Integer adminId, @RequestBody ZcCategory product) {
    if (adminId == null) {
      return ResponseUtil.unlogin();
    }

    // 开启事务管理
    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    TransactionStatus status = txManager.getTransaction(def);

    try {
      zcCategoryService.add(product);
    } catch (Exception ex) {
      txManager.rollback(status);
      logger.error("系统内部错误", ex);
    }
    txManager.commit(status);

    return ResponseUtil.ok();
  }

  @GetMapping("/l1")
  public Object catL1(@LoginAdmin Integer adminId) {
    if (adminId == null) {
      return ResponseUtil.unlogin();
    }

    // 所有一级分类目录
    List<ZcCategory> l1CatList = zcCategoryService.queryL1();
    List<Map<String, Object>> data = new ArrayList<>(l1CatList.size());
    for (ZcCategory category : l1CatList){
      Map<String, Object> d = new HashMap<>(2);
      d.put("code", category.getCode());
      d.put("value", category.getId());
      d.put("label", category.getName());
      data.add(d);
    }
    return ResponseUtil.ok(data);
  }

  @GetMapping("/l2")
  public Object catL2(@LoginAdmin Integer adminId) {
    if (adminId == null) {
      return ResponseUtil.unlogin();
    }

    // 所有一级分类目录
    List<ZcCategory> l2CatList = zcCategoryService.queryL2();
    List<Map<String, Object>> data = new ArrayList<>();
    Map<String, Object> dataMap = new HashMap<>();
    for (ZcCategory category : l2CatList){
      String keyId = String.valueOf(category.getPid());
      Map<String, Object> d = new HashMap<>(3);
      d.put("code", category.getCode());
      d.put("value", category.getId());
      d.put("label", category.getName());
      if (dataMap.containsKey(keyId)) {
        ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) dataMap.get(keyId);
        list.add(d);
        dataMap.put(keyId, list);
      } else {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        list.add(d);
        dataMap.put(keyId, list);
      }
    }

    for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
      Map<String, Object> d = new HashMap<>(3);
      Integer id = Integer.valueOf(entry.getKey());
      ZcCategory pCategory = zcCategoryService.queryById(id);
      if (null != pCategory) {
        d.put("code", pCategory.getCode());
      }
      d.put("value", entry.getKey());
      d.put("list", entry.getValue());
      data.add(d);
    }
    return ResponseUtil.ok(data);
  }

  @PostMapping("/delete")
  public Object delete(@LoginAdmin Integer adminId, @RequestBody ZcCategory category){
    if (adminId == null) {
      return ResponseUtil.unlogin();
    }
    zcCategoryService.deleteById(category.getId());
    return ResponseUtil.ok();
  }

  @PostMapping("/update")
  public Object update(@LoginAdmin Integer adminId, @RequestBody ZcCategory category){
    if (adminId == null) {
      return ResponseUtil.unlogin();
    }
    zcCategoryService.updateById(category);
    return ResponseUtil.ok();
  }
}
