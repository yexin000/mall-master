package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.ZcProduct;
import org.linlinjava.litemall.db.service.ZcProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
