package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.ZcProductMapper;
import org.linlinjava.litemall.db.domain.ZcProduct;
import org.linlinjava.litemall.db.domain.ZcProductExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 中车产品查询service
 */
@Service
public class ZcProductService {
  @Resource
  private ZcProductMapper zcProductMapper;

  public List<ZcProduct> querySelective(String productNum, String productName, String productType, Integer page, Integer limit, String sort, String order) {
    ZcProductExample example = new ZcProductExample();
    ZcProductExample.Criteria criteria = example.createCriteria();

    if (!StringUtils.isEmpty(productType)) {
      criteria.andProducttypeEqualTo(productType);
    }

    if (!StringUtils.isEmpty(productNum)) {
      criteria.andProductnumLike("%" + productNum + "%");
    }

    if (!StringUtils.isEmpty(productName)) {
      criteria.andProductnameLike("%" + productName + "%");
    }
    PageHelper.startPage(page, limit);
    return zcProductMapper.selectByExample(example);
  }

  public int countSelective(String productNum, String productName, String productType) {
    ZcProductExample example = new ZcProductExample();
    ZcProductExample.Criteria criteria = example.createCriteria();

    if(!StringUtils.isEmpty(productType)){
      criteria.andProducttypeEqualTo(productType);
    }
    return (int)zcProductMapper.countByExample(example);
  }
}
