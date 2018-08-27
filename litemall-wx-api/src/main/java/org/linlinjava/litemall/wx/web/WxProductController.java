package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.ZcProduct;
import org.linlinjava.litemall.db.service.ZcProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中车产品控制层
 */
@RestController
@RequestMapping("/wx/product")
public class WxProductController {
    private final Log logger = LogFactory.getLog(WxIndexController.class);

    @Autowired
    private ZcProductService zcProductService;

    @RequestMapping("/list")
    public Object list(String productNum, String productName, String productType, String platform,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order){
        // TODO 添加到搜索历史

        // 查询产品列表及总数
        //查询列表数据
        List<ZcProduct> productList = zcProductService.querySelective(productNum, productName, productType, page, size, sort, order);
        int total = zcProductService.countSelective(productType, platform, productName);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("dataList", productList);
        result.put("total", total);
        return ResponseUtil.ok(result);
    }
}
