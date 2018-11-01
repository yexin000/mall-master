package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.ZcProducttypeMapper;
import org.linlinjava.litemall.db.domain.ZcProducttype;
import org.linlinjava.litemall.db.domain.ZcProducttypeExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 小程序中车产品类型service
 */
@Service
public class ZcProducttypeService {
  @Resource
  private ZcProducttypeMapper zcProducttypeMapper;

  public List<ZcProducttype> queryAllType() {
    ZcProducttypeExample example = new ZcProducttypeExample();
    example.setOrderByClause("producttype asc");
    return zcProducttypeMapper.selectByExample(example);
  }

  public List<ZcProducttype> queryTypesByPlatforms(List<String> platFormList) {
    return zcProducttypeMapper.selectByPlatforms(platFormList);
  }
}
