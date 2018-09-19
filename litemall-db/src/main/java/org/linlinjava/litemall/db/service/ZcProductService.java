package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.linlinjava.litemall.db.dao.ZcProductMapper;
import org.linlinjava.litemall.db.domain.ZcCategory;
import org.linlinjava.litemall.db.domain.ZcProduct;
import org.linlinjava.litemall.db.domain.ZcProductExample;
import org.linlinjava.litemall.db.domain.ZcProducttype;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中车产品查询service
 */
@Service
public class ZcProductService {
  @Resource
  private ZcProductMapper zcProductMapper;

  @Resource
  private ZcCategoryService zcCategoryService;

  public static final Map<String, String> PRODUCT_TYPE_MAP = new HashMap<String, String>() {
    {
      put( "01", "球铰关节" );
      put( "02", "橡胶垫" );
      put( "03", "止挡" );
      put( "04", "空气弹簧" );
      put( "05", "抗侧滚扭杆" );
      put( "06", "连杆组件" );
      put( "07", "牵引装置" );
      put( "08", "橡胶堆" );
      put( "09", "锥形簧" );
      put( "10", "V形簧" );
      put( "11", "其他" );
    }
  };

  public static final List<String> SHEET_NAME_LIST = new ArrayList<String>(){{
    add("球铰关节");
    add("橡胶垫");
    add("止挡");
    add("空气弹簧");
    add("抗侧滚扭杆");
    add("连杆组件");
    add("牵引装置");
    add("橡胶堆");
    add("锥形簧");
    add("V形簧");
    add("其他悬挂系列产品");
  }};

  public static final String QJGX_TYPE = "01"; // 球铰关节
  public static final String XJDIAN_TYPE = "02"; // 橡胶垫
  public static final String ZHID_TYPE = "03"; // 止挡
  public static final String KQTH_TYPE = "04"; // 空气弹簧
  public static final String KCGNG_TYPE = "05"; // 抗侧滚扭杆
  public static final String LGZJ_TYPE = "06"; // 连杆组件
  public static final String QYZZ_TYPE = "07"; // 牵引装置
  public static final String XJDUI_TYPE = "08"; // 橡胶堆
  public static final String ZXH_TYPE = "09"; // 锥形簧
  public static final String VXH_TYPE = "10"; // V形簧
  public static final String QT_TYPE = "11"; // 其他

  public List<ZcProduct> querySelective(String productNum, String productName, String productType,
                                        String platform, String trainType,
                                        Integer page, Integer limit, String sort, String order) {
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

    if (!StringUtils.isEmpty(platform)) {
      criteria.andPlatformEqualTo(platform);
    }

    if (!StringUtils.isEmpty(trainType)) {
      criteria.andTraintypeEqualTo(trainType);
    }
    PageHelper.startPage(page, limit);
    return zcProductMapper.selectByExample(example);
  }

  public List<ZcProduct> queryByTrainType(String trainType, String sort, String order) {
    ZcProductExample example = new ZcProductExample();
    ZcProductExample.Criteria criteria = example.createCriteria();

    if (!StringUtils.isEmpty(trainType)) {
      criteria.andTraintypeEqualTo(trainType);
    }

    if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
      example.setOrderByClause(sort + " " + order);
    }

    return zcProductMapper.selectByExample(example);
  }

  public List<ZcProduct> queryByProductType(String productType, String sort, String order) {

    ZcProductExample example = new ZcProductExample();
    ZcProductExample.Criteria criteria = example.createCriteria();

    if (!StringUtils.isEmpty(productType)) {
      criteria.andProducttypeEqualTo(productType);
    }

    if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
      example.setOrderByClause(sort + " " + order);
    }

    return zcProductMapper.selectByExample(example);
  }

  public int countSelective(String productNum, String productName, String productType,
                            String platform, String trainType) {
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

    if (!StringUtils.isEmpty(platform)) {
      criteria.andPlatformEqualTo(platform);
    }

    if (!StringUtils.isEmpty(trainType)) {
      criteria.andTraintypeEqualTo(trainType);
    }
    return (int)zcProductMapper.countByExample(example);
  }

  public List<ZcProducttype> queryProductType(String productName, String platform, String trainType) {
    ZcProductExample example = new ZcProductExample();
    ZcProductExample.Criteria criteria = example.createCriteria();
    if (!StringUtils.isEmpty(productName)) {
      criteria.andProductnameLike("%" + productName + "%");
    }

    if (!StringUtils.isEmpty(platform)) {
      criteria.andPlatformEqualTo(platform);
    }

    if (!StringUtils.isEmpty(trainType)) {
      criteria.andTraintypeEqualTo(trainType);
    }
    return zcProductMapper.selectProductTypeByExample(example);
  }

  public void add(ZcProduct product) {
    zcProductMapper.insertSelective(product);
  }

  public ZcProduct findById(Integer id) {
    return zcProductMapper.selectByPrimaryKey(id);
  }

  public void updateById(ZcProduct product) {
    zcProductMapper.updateByPrimaryKeySelective(product);
  }

  public void deleteById(Integer id) {
    zcProductMapper.deleteByPrimaryKey(id);
  }

  @Transactional(rollbackFor = Exception.class)
  public Map batchImport(String fileName, MultipartFile file) throws Exception {
    Map<String, Object> data = new HashMap<>();
    if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
      data.put("code", "0");
      data.put("message", "上传文件格式不正确");
      return data;
    }

    Map<String, Object> platformMap = new HashMap<>();
    List<ZcCategory> platformList = zcCategoryService.queryL1();
    for (ZcCategory platform : platformList) {
      platformMap.put(platform.getName(), platform.getCode());
    }

    Map<String, Object> trainTypeMap = new HashMap<>();
    List<ZcCategory> trainTypeList = zcCategoryService.queryL2();
    for (ZcCategory trainType : trainTypeList) {
      trainTypeMap.put(trainType.getName(), trainType.getCode());
    }

    boolean isExcel2003 = true;
    if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
      isExcel2003 = false;
    }
    InputStream is = file.getInputStream();
    Workbook wb = null;
    if (isExcel2003) {
      wb = new HSSFWorkbook(is);
    } else {
      wb = new XSSFWorkbook(is);
    }
    int sheetNum = wb.getNumberOfSheets();
    if (sheetNum > 0) {
      List<ZcProduct> productList = new ArrayList<>();
      // 遍历所有页签
      for (int i = 0; i < sheetNum; i ++) {
        Sheet sheet = wb.getSheetAt(i);
        if (sheet != null) {
        }
        String sheetName = sheet.getSheetName();
        boolean isImportSheet = false;
        for (String sname : SHEET_NAME_LIST) {
          if (sheetName.contains(sname)) {
            isImportSheet = true;
            break;
          }
        }
        if (isImportSheet && sheet.getLastRowNum() > 2) {
          // 跳过第一第二行
          for (int r = 2; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
              continue;
            }
            ZcProduct product = new ZcProduct();
            product.setProducttype(getProductTypeBySheetname(sheetName));
            String producyType = product.getProducttype();
            int end = row.getLastCellNum();
            // 跳过第一列
            for (int j = 1; j < end; j++) {
              Cell cell = row.getCell(j);
              String value = getValue(cell);
              if (j == 1) {
                // 物资编号
                product.setProductnum(value);
              } else if (j == 2) {
                // 产品名称
                product.setProductname(value);
              } else if (j == 5) {
                // 结构形式
                product.setStructtype(value);
              } else if (j == 6) {
                if (KQTH_TYPE.equals(producyType) || QJGX_TYPE.equals(producyType) || XJDUI_TYPE.equals(producyType)
                    || XJDIAN_TYPE.equals(producyType)) {
                  product.setWj(value);
                } else if (KCGNG_TYPE.equals(producyType)) {
                  product.setLgjzdjj(value);
                } else if (LGZJ_TYPE.equals(producyType)) {
                  product.setLgzxj(value);
                } else if (QYZZ_TYPE.equals(producyType) || VXH_TYPE.equals(producyType)
                    || QT_TYPE.equals(producyType)) {
                  product.setCd(value);
                } else if (ZXH_TYPE.equals(producyType)) {
                  product.setZhij(value);
                } else if (ZHID_TYPE.equals(producyType)) {
                  product.setZyg(value);
                }
              } else if (j == 7) {
                if (KQTH_TYPE.equals(producyType)) {
                  product.setBzgd(value);
                } else if (KCGNG_TYPE.equals(producyType)) {
                  product.setZczjj(value);
                } else if (LGZJ_TYPE.equals(producyType)) {
                  product.setGtqtwj(value);
                } else if (QYZZ_TYPE.equals(producyType)) {
                  product.setKd(value);
                } else if (QJGX_TYPE.equals(producyType)) {
                  product.setCd(value);
                } else if (ZXH_TYPE.equals(producyType)) {
                  product.setGd(value);
                } else if (XJDUI_TYPE.equals(producyType)) {
                  product.setCd(value);
                } else if (XJDIAN_TYPE.equals(producyType)) {
                  product.setGd(value);
                } else if (VXH_TYPE.equals(producyType)) {
                  product.setKd(value);
                } else if (ZHID_TYPE.equals(producyType)) {
                  product.setZhouxgd(value);
                } else if (QT_TYPE.equals(producyType)) {
                  product.setKd(value);
                }
              } else if (j == 8) {
                if (KQTH_TYPE.equals(producyType)) {
                  product.setKz(value);
                } else if (KCGNG_TYPE.equals(producyType)) {
                  product.setNgbcd(value);
                } else if (LGZJ_TYPE.equals(producyType)) {
                  product.setKlx(value);
                } else if (QYZZ_TYPE.equals(producyType)) {
                  product.setGd(value);
                } else if (QJGX_TYPE.equals(producyType)) {
                  product.setZpcd(value);
                } else if (ZXH_TYPE.equals(producyType)) {
                  product.setKz(value);
                } else if (XJDUI_TYPE.equals(producyType)) {
                  product.setKd(value);
                } else if (XJDIAN_TYPE.equals(producyType)) {
                  product.setSyhz(value);
                } else if (VXH_TYPE.equals(producyType)) {
                  product.setGd(value);
                } else if (ZHID_TYPE.equals(producyType)) {
                  product.setTraintype(trainTypeMap.get(value) == null ? value : trainTypeMap.get(value).toString());
                } else if (QT_TYPE.equals(producyType)) {
                  product.setGd(value);
                }
              } else if (j == 9) {
                if (KQTH_TYPE.equals(producyType)) {
                  product.setCz(value);
                } else if (KCGNG_TYPE.equals(producyType)) {
                  product.setJxhz(value);
                } else if (LGZJ_TYPE.equals(producyType)) {
                  product.setJxhz(value);
                } else if (QYZZ_TYPE.equals(producyType)) {
                  product.setZpcc(value);
                } else if (QJGX_TYPE.equals(producyType)) {
                  product.setJxgd(value);
                } else if (ZXH_TYPE.equals(producyType)) {
                  product.setZdhz(value);
                } else if (XJDUI_TYPE.equals(producyType)) {
                  product.setGd(value);
                } else if (XJDIAN_TYPE.equals(producyType)) {
                  product.setGangd(value);
                } else if (VXH_TYPE.equals(producyType)) {
                  product.setVxjd(value);
                } else if (ZHID_TYPE.equals(producyType)) {
                  product.setPlatform(platformMap.get(value) == null ? value : platformMap.get(value).toString());
                } else if (QT_TYPE.equals(producyType)) {
                  product.setTraintype(trainTypeMap.get(value) == null ? value : trainTypeMap.get(value).toString());
                }
              } else if (j == 10) {
                if (KQTH_TYPE.equals(producyType)) {
                  product.setWeight(value);
                } else if (KCGNG_TYPE.equals(producyType)) {
                  product.setPlhz(value);
                } else if (LGZJ_TYPE.equals(producyType)) {
                  product.setYzl(value);
                } else if (QYZZ_TYPE.equals(producyType)) {
                  product.setJxhz(value);
                } else if (QJGX_TYPE.equals(producyType)) {
                  product.setZhouxgd(value);
                } else if (ZXH_TYPE.equals(producyType)) {
                  product.setCxgd(value);
                } else if (XJDUI_TYPE.equals(producyType)) {
                  product.setCxhz(value);
                } else if (XJDIAN_TYPE.equals(producyType)) {
                  product.setTraintype(trainTypeMap.get(value) == null ? value : trainTypeMap.get(value).toString());
                } else if (VXH_TYPE.equals(producyType)) {
                  product.setKz(value);
                } else if (ZHID_TYPE.equals(producyType)) {
                  product.setProvider(value);
                } else if (QT_TYPE.equals(producyType)) {
                  product.setPlatform(platformMap.get(value) == null ? value : platformMap.get(value).toString());
                }
              } else if (j == 11) {
                if (KQTH_TYPE.equals(producyType)) {
                  product.setTraintype(trainTypeMap.get(value) == null ? value : trainTypeMap.get(value).toString());
                } else if (KCGNG_TYPE.equals(producyType)) {
                  product.setXtgd(value);
                } else if (LGZJ_TYPE.equals(producyType)) {
                  product.setJxgd(value);
                } else if (QYZZ_TYPE.equals(producyType)) {
                  product.setZongxgd(value);
                } else if (QJGX_TYPE.equals(producyType)) {
                  product.setTraintype(trainTypeMap.get(value) == null ? value : trainTypeMap.get(value).toString());
                } else if (ZXH_TYPE.equals(producyType)) {
                  product.setYsg(value);
                } else if (XJDUI_TYPE.equals(producyType)) {
                  product.setCxgd(value);
                } else if (XJDIAN_TYPE.equals(producyType)) {
                  product.setPlatform(platformMap.get(value) == null ? value : platformMap.get(value).toString());
                } else if (VXH_TYPE.equals(producyType)) {
                  product.setZdhz(value);
                } else if (ZHID_TYPE.equals(producyType)) {
                } else if (QT_TYPE.equals(producyType)) {
                }
              } else if (j == 12) {
                if (KQTH_TYPE.equals(producyType)) {
                  product.setPlatform(platformMap.get(value) == null ? value : platformMap.get(value).toString());
                } else if (KCGNG_TYPE.equals(producyType)) {
                  product.setWeight(value);
                } else if (LGZJ_TYPE.equals(producyType)) {
                  product.setWeight(value);
                } else if (QYZZ_TYPE.equals(producyType)) {
                  product.setWeight(value);
                } else if (QJGX_TYPE.equals(producyType)) {
                  product.setPlatform(platformMap.get(value) == null ? value : platformMap.get(value).toString());
                } else if (ZXH_TYPE.equals(producyType)) {
                  product.setTraintype(trainTypeMap.get(value) == null ? value : trainTypeMap.get(value).toString());
                } else if (XJDUI_TYPE.equals(producyType)) {
                } else if (XJDIAN_TYPE.equals(producyType)) {
                  product.setProvider(value);
                } else if (VXH_TYPE.equals(producyType)) {
                  product.setCxgd(value);
                } else if (ZHID_TYPE.equals(producyType)) {
                } else if (QT_TYPE.equals(producyType)) {
                  product.setProvider(value);
                }
              } else if (j == 13) {
                if (KQTH_TYPE.equals(producyType)) {
                  product.setProvider(value);
                } else if (KCGNG_TYPE.equals(producyType)) {
                  product.setTraintype(trainTypeMap.get(value) == null ? value : trainTypeMap.get(value).toString());
                } else if (LGZJ_TYPE.equals(producyType)) {
                  product.setTraintype(trainTypeMap.get(value) == null ? value : trainTypeMap.get(value).toString());
                } else if (QYZZ_TYPE.equals(producyType)) {
                  product.setTraintype(trainTypeMap.get(value) == null ? value : trainTypeMap.get(value).toString());
                } else if (QJGX_TYPE.equals(producyType)) {
                  product.setProvider(value);
                } else if (ZXH_TYPE.equals(producyType)) {
                  product.setPlatform(platformMap.get(value) == null ? value : platformMap.get(value).toString());
                } else if (XJDUI_TYPE.equals(producyType)) {
                } else if (XJDIAN_TYPE.equals(producyType)) {
                } else if (VXH_TYPE.equals(producyType)) {
                  product.setHxgd(value);
                } else if (ZHID_TYPE.equals(producyType)) {
                } else if (QT_TYPE.equals(producyType)) {
                }
              } else if (j == 14) {
                if (KQTH_TYPE.equals(producyType)) {
                } else if (KCGNG_TYPE.equals(producyType)) {
                  product.setPlatform(platformMap.get(value) == null ? value : platformMap.get(value).toString());
                } else if (LGZJ_TYPE.equals(producyType)) {
                  product.setPlatform(platformMap.get(value) == null ? value : platformMap.get(value).toString());
                } else if (QYZZ_TYPE.equals(producyType)) {
                  product.setPlatform(platformMap.get(value) == null ? value : platformMap.get(value).toString());
                } else if (QJGX_TYPE.equals(producyType)) {
                } else if (ZXH_TYPE.equals(producyType)) {
                  product.setProvider(value);
                } else if (XJDUI_TYPE.equals(producyType)) {
                  product.setTraintype(trainTypeMap.get(value) == null ? value : trainTypeMap.get(value).toString());
                } else if (XJDIAN_TYPE.equals(producyType)) {
                } else if (VXH_TYPE.equals(producyType)) {
                  product.setZongxgd(value);
                } else if (ZHID_TYPE.equals(producyType)) {
                } else if (QT_TYPE.equals(producyType)) {
                }
              } else if (j == 15) {
                if (KQTH_TYPE.equals(producyType)) {
                } else if (KCGNG_TYPE.equals(producyType)) {
                  product.setProvider(value);
                } else if (LGZJ_TYPE.equals(producyType)) {
                  product.setProvider(value);
                } else if (QYZZ_TYPE.equals(producyType)) {
                  product.setProvider(value);
                } else if (QJGX_TYPE.equals(producyType)) {
                } else if (ZXH_TYPE.equals(producyType)) {
                } else if (XJDUI_TYPE.equals(producyType)) {
                  product.setPlatform(platformMap.get(value) == null ? value : platformMap.get(value).toString());
                } else if (XJDIAN_TYPE.equals(producyType)) {
                } else if (VXH_TYPE.equals(producyType)) {
                  product.setYsg(value);
                } else if (ZHID_TYPE.equals(producyType)) {
                } else if (QT_TYPE.equals(producyType)) {
                }
              } else if (j == 16) {
                if (VXH_TYPE.equals(producyType)) {
                  product.setWeight(value);
                } else if (XJDUI_TYPE.equals(producyType)) {
                  product.setProvider(value);
                }
              } else if (j == 17) {
                if (VXH_TYPE.equals(producyType)) {
                  product.setTraintype(trainTypeMap.get(value) == null ? value : trainTypeMap.get(value).toString());
                }
              } else if (j == 18) {
                if (VXH_TYPE.equals(producyType)) {
                  product.setPlatform(platformMap.get(value) == null ? value : platformMap.get(value).toString());
                }
              } else if (j == 19) {
                if (VXH_TYPE.equals(producyType)) {
                  product.setProvider(value);
                }
              }
            }
            if (!StringUtils.isEmpty(product.getProductnum()) && !StringUtils.isEmpty(product.getProductname())) {
              productList.add(product);
            }
          }
        }
      }

      if (!CollectionUtils.isEmpty(productList)) {
        // 查询物资编号列表
        List<String> productNumList = zcProductMapper.queryProductNumList();
        // 得到插入列表
        List<ZcProduct> insertProductList = new ArrayList<>();
        // 得到更新列表
        List<ZcProduct> updateProductList = new ArrayList<>();
        for (ZcProduct product : productList) {
          if (productNumList.contains(product.getProductnum())) {
            updateProductList.add(product);
          } else {
            insertProductList.add(product);
          }
        }
        if (!CollectionUtils.isEmpty(insertProductList)) {
          zcProductMapper.batchInsert(insertProductList);
        }
        if (!CollectionUtils.isEmpty(updateProductList)) {
          zcProductMapper.batchUpdate(updateProductList);
        }
      }
    }
    return data;
  }

  private String getProductTypeBySheetname(String sheetName) {
    String productType = "11";
    for (Map.Entry<String, String> entry : PRODUCT_TYPE_MAP.entrySet()) {
      if (sheetName.contains(entry.getValue())) {
        productType = entry.getKey();
      }
    }

    return productType;
  }

  private static String getValue(Cell cell) {
    String obj = null;
    if (null != cell) {
      switch (cell.getCellType()) {
        case Cell.CELL_TYPE_BOOLEAN:
          obj = String.valueOf(cell.getBooleanCellValue());
          break;
        case Cell.CELL_TYPE_NUMERIC:
          obj = String.valueOf(cell.getNumericCellValue());
          break;
        case Cell.CELL_TYPE_STRING:
          obj = cell.getStringCellValue();
          break;
        default:
          break;
      }
    }
    return obj;
  }
}
