package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.ZcCategory;
import org.linlinjava.litemall.db.domain.ZcProduct;
import org.linlinjava.litemall.db.domain.ZcProducttype;
import org.linlinjava.litemall.db.service.ZcCategoryService;
import org.linlinjava.litemall.db.service.ZcProductService;
import org.linlinjava.litemall.db.service.ZcProducttypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小程序中车产品接口
 */
@RestController
@RequestMapping("/wx/zc")
public class WxZcController {
  private final Log logger = LogFactory.getLog(WxZcController.class);

  @Autowired
  private ZcProductService zcProductService;
  @Autowired
  private ZcCategoryService zcCategoryService;
  @Autowired
  private ZcProducttypeService zcProducttypeService;

  /**
   * 查询产品应用平台列表
   * @return
   */
  @GetMapping("getTrainTypes")
  public Object getTrainTypes() {
    List<ZcCategory> l1CatList = zcCategoryService.queryL1();
    List<Map<String, Object>> data = new ArrayList<>(l1CatList.size());
    for (ZcCategory category : l1CatList){
      Map<String, Object> d = new HashMap<>(2);
      d.put("traintype", category.getCode());
      d.put("text", category.getName());
      data.add(d);
    }
    return ResponseUtil.ok(data);
  }

  /**
   * 查询产品应用车型列表
   * @return
   */
  @GetMapping("getTrains")
  public Object getTrains() {
    List<ZcCategory> l2CatList = zcCategoryService.queryL2WithPcode();
    List<Map<String, Object>> data = new ArrayList<>(l2CatList.size());
    for (ZcCategory category : l2CatList){
      Map<String, Object> d = new HashMap<>(3);
      d.put("traintype", category.getPcode());
      d.put("trainId", category.getCode());
      d.put("name", category.getName());
      data.add(d);
    }
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

  /**
   * 查询产品应用平台列表和产品车型列表
   * @return
   */
  @GetMapping("getTypesAndTrains")
  public Object getTypesAndTrains() {
    List<ZcCategory> l1CatList = zcCategoryService.queryL1();
    List<ZcCategory> l2CatList = zcCategoryService.queryL2WithPcode();
    List<Map<String, Object>> trainTypes = new ArrayList<>(l1CatList.size());
    for (ZcCategory category : l1CatList){
      Map<String, Object> d = new HashMap<>(2);
      d.put("traintype", category.getCode());
      d.put("text", category.getName());
      trainTypes.add(d);
    }

    List<Map<String, Object>> trains = new ArrayList<>(l2CatList.size());
    for (ZcCategory category : l2CatList){
      Map<String, Object> d = new HashMap<>(3);
      d.put("traintype", category.getPcode());
      d.put("trainId", category.getCode());
      d.put("name", category.getName());
      trains.add(d);
    }

    Map<String, Object> result = new HashMap<>();
    result.put("trainTypes", trainTypes);
    result.put("trains", trains);

    return ResponseUtil.ok(result);
  }

  /**
   * 产品列表查询接口
   * @param productName
   * @param platform
   * @param trainType
   * @param page
   * @param size
   * @param sort
   * @param order
   * @return
   */
  @GetMapping("list")
  public Object list(String productName, String platform, String trainType, String productType,
                     @RequestParam(defaultValue = "1") Integer page,
                     @RequestParam(defaultValue = "2000") Integer size,
                     @Sort(accepts = {"productnum"}) @RequestParam(defaultValue = "productnum") String sort,
                     @Order @RequestParam(defaultValue = "asc") String order) {
    // TODO 添加到搜索历史

    // 产品列表
    List<ZcProduct> productList = zcProductService.querySelective(null , productName, productType,
        platform, trainType, page, size, sort, order);
    // 产品总数
    int total = zcProductService.countSelective(null, productName, productType, platform, trainType);

    Map<String, Object> result = new HashMap<String, Object>();
    result.put("data", productList);
    result.put("total", total);

    // 产品列表包含产品类型列表
    List<ZcProducttype> producttypeList = zcProductService.queryProductType(productName, platform, trainType);
    result.put("typeList", producttypeList);
    return ResponseUtil.ok(result);
  }

  /**
   * 根据应用车型查询产品列表
   * @param trainType
   * @param sort
   * @param order
   * @return
   */
  @GetMapping("listByTrainType")
  public Object listByTrainType(String trainType,
                     @Sort(accepts = {"productnum"}) @RequestParam(defaultValue = "productnum") String sort,
                     @Order @RequestParam(defaultValue = "asc") String order) {
    // 产品列表
    List<ZcProduct> productList = zcProductService.queryByTrainType(trainType, sort, order);
    // 产品总数
    int total = zcProductService.countSelective(null, null, null, null, trainType);
    List<ZcProducttype> producttypeList = zcProductService.queryProductType(null, null, trainType);

    Map<String, Object> result = new HashMap<String, Object>();
    result.put("data", productList);
    result.put("total", total);
    result.put("typeList", producttypeList);

    return ResponseUtil.ok(result);
  }

  @GetMapping("listByProductType")
  public Object listByProductType(String productType,
                                @Sort(accepts = {"productnum"}) @RequestParam(defaultValue = "productnum") String sort,
                                @Order @RequestParam(defaultValue = "asc") String order) {
    // 产品总数
    int total = zcProductService.countSelective(null, null, productType, null, null);
    // 产品列表
    List<ZcProduct> productList = zcProductService.queryByProductType(productType, sort, order);
    List<Map<String, Object>> productPropList = new ArrayList<>();
    Map<String, Object> dataMap = new HashMap<>();
    if (!CollectionUtils.isEmpty(productList)) {
      for (ZcProduct product : productList) {
        String stuctType = product.getStructtype();
        if (dataMap.containsKey(stuctType)) {
          ArrayList<ZcProduct> list = (ArrayList<ZcProduct>) dataMap.get(stuctType);
          list.add(product);
          dataMap.put(stuctType, list);
        } else {
          ArrayList<ZcProduct> list = new ArrayList<>();
          list.add(product);
          dataMap.put(stuctType, list);
        }
      }

      for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
        Map<String, Object> d = new HashMap<>(2);
        d.put("productPropName", entry.getKey());
        d.put("productlist", entry.getValue());
        productPropList.add(d);
      }
    }

    Map<String, Object> result = new HashMap<String, Object>();
    result.put("total", total);
    result.put("productPropList", productPropList);

    return ResponseUtil.ok(result);
  }

  @GetMapping("getById")
  public Object getById(Integer productId) {
    ZcProduct productInfo = zcProductService.findById(productId);
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("productInfo", productInfo);

    return ResponseUtil.ok(result);
  }
}
