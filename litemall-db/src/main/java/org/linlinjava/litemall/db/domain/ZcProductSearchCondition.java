package org.linlinjava.litemall.db.domain;

/**
 * 中车产品搜索条件
 */
public class ZcProductSearchCondition {
  /**
   * 字段名称
   */
  private String key;
  /**
   * 左边界
   */
  private String low;
  /**
   * 右边界
   */
  private String high;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getLow() {
    return low;
  }

  public void setLow(String low) {
    this.low = low;
  }

  public String getHigh() {
    return high;
  }

  public void setHigh(String high) {
    this.high = high;
  }
}
