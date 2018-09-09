package org.linlinjava.litemall.db.service;


import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.ZcCategoryMapper;
import org.linlinjava.litemall.db.domain.ZcCategory;
import org.linlinjava.litemall.db.domain.ZcCategoryExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 中车平台/车型service
 */
@Service
public class ZcCategoryService {
  @Resource
  private ZcCategoryMapper zcCategoryMapper;

  public List<ZcCategory> querySelective(String name, Integer pid, Integer id, Integer page, Integer limit, String sort, String order) {
    ZcCategoryExample example = new ZcCategoryExample();
    ZcCategoryExample.Criteria criteria = example.createCriteria();

    if (!StringUtils.isEmpty(name)) {
      criteria.andNameLike("%" + name + "%");
    }

    if (null != pid && pid > 0) {
      criteria.andPidEqualTo(pid);
    }

    if (null != id && id > 0) {
      criteria.andIdEqualTo(id);
    }

    PageHelper.startPage(page, limit);
    return zcCategoryMapper.selectByExample(example);
  }

  public int countSelective(String name, Integer pid, Integer id) {
    ZcCategoryExample example = new ZcCategoryExample();
    ZcCategoryExample.Criteria criteria = example.createCriteria();

    if (!StringUtils.isEmpty(name)) {
      criteria.andNameLike("%" + name + "%");
    }

    if (null != pid && pid > 0) {
      criteria.andPidEqualTo(pid);
    }

    if (null != id && id > 0) {
      criteria.andIdEqualTo(id);
    }
    return (int)zcCategoryMapper.countByExample(example);
  }

  public void add(ZcCategory product) {
    zcCategoryMapper.insertSelective(product);
  }

  public List<ZcCategory> queryL1() {
    ZcCategoryExample example = new ZcCategoryExample();
    example.or().andPidEqualTo(0);
    return zcCategoryMapper.selectByExample(example);
  }

  public List<ZcCategory> queryL2() {
    ZcCategoryExample example = new ZcCategoryExample();
    example.or().andPidNotEqualTo(0);
    return zcCategoryMapper.selectByExample(example);
  }

  public ZcCategory queryById(Integer id) {
    return zcCategoryMapper.selectByPrimaryKey(id);
  }

  public void deleteById(Integer id) {
    zcCategoryMapper.deleteByPrimaryKey(id);
  }

  public void updateById(ZcCategory category) {
    zcCategoryMapper.updateByPrimaryKeySelective(category);
  }
}
