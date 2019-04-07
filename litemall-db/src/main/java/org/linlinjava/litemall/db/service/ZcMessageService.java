package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.ZcMessageMapper;
import org.linlinjava.litemall.db.domain.ZcMessage;
import org.linlinjava.litemall.db.domain.ZcMessageExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 小程序中车留言service
 */
@Service
public class ZcMessageService {
  @Resource
  private ZcMessageMapper zcMessageMapper;

  public void add(ZcMessage message) {
    zcMessageMapper.insertSelective(message);
  }

  public List<ZcMessage> querySelective(String phone, String name,
                                        Integer page, Integer limit, String sort, String order) {
    ZcMessageExample example = new ZcMessageExample();
    ZcMessageExample.Criteria criteria = example.createCriteria();

    if (!StringUtils.isEmpty(phone)) {
      criteria.andPhoneLike("%" + phone + "%");
    }

    if (!StringUtils.isEmpty(name)) {
      criteria.andNameLike("%" + name + "%");
    }

    if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
      example.setOrderByClause(sort + " " + order);
    }

    PageHelper.startPage(page, limit);
    return zcMessageMapper.selectByExample(example);
  }

  public int countSelective(String phone, String name) {
    ZcMessageExample example = new ZcMessageExample();
    ZcMessageExample.Criteria criteria = example.createCriteria();

    if (!StringUtils.isEmpty(phone)) {
      criteria.andPhoneLike("%" + phone + "%");
    }

    if (!StringUtils.isEmpty(name)) {
      criteria.andNameLike("%" + name + "%");
    }

    return (int)zcMessageMapper.countByExample(example);
  }

  public void deleteById(Integer id) {
    zcMessageMapper.deleteByPrimaryKey(id);
  }
}
