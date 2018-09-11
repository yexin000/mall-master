package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.ZcCategory;
import org.linlinjava.litemall.db.domain.ZcProduct;
import org.linlinjava.litemall.db.service.ZcCategoryService;
import org.linlinjava.litemall.db.service.ZcProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
  public Object list(String productName, String platform, String trainType,
                     @RequestParam(defaultValue = "1") Integer page,
                     @RequestParam(defaultValue = "20") Integer size,
                     @Sort(accepts = {"productnum"}) @RequestParam(defaultValue = "productnum") String sort,
                     @Order @RequestParam(defaultValue = "asc") String order) {
    // TODO 添加到搜索历史

    List<ZcProduct> productList = zcProductService.querySelective(null , productName, null,
        platform, trainType, page, size, sort, order);
    int total = zcProductService.countSelective(null, productName, null, platform, trainType);
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("data", productList);
    result.put("total", total);
    return ResponseUtil.ok(result);
  }

}
