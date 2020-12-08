package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.linlinjava.litemall.db.dao.ZcProductMapper;
import org.linlinjava.litemall.db.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 中车产品查询service
 */
@Service
public class ZcProductService {
    /**
     * 安全装置
     */
    public static final String AQZZ_TYPE = "15";
    public static final String[] CONDITION_KEY_LIST = {"wj", "bzgd", "lgjzdjj", "zczjj", "ngbcd", "lgzxj", "gtqtwj", "klx", "zongxgd", "jxgd",
            "gd", "gangd", "zyg", "zhouxgd", "cxgd", "vxjd", "cd", "kd"};
    /**
     * 层压产品
     */
    public static final String CYCP_TYPE = "28";
    /**
     * 挡板座
     */
    public static final String DBZ_TYPE = "32";
    /**
     * 道岔
     */
    public static final String DC_TYPE = "14";
    /**
     * 叠簧式齿轮箱弹性支撑总成
     */
    public static final String DHSCLX_TYPE = "23";
    /**
     * 发电机弹性支撑
     */
    public static final String FDJTXZC_TYPE = "22";
    /**
     * 轨道减振器
     */
    public static final String GDJZQ_TYPE = "13";
    /**
     * 轨距挡板
     */
    public static final String GJDB_TYPE = "34";
    /**
     * 轨距块
     */
    public static final String GJK_TYPE = "33";
    /**
     * 机舱罩弹性支撑
     */
    public static final String JCZTXZC_TYPE = "27";
    /**
     * 检修产品
     */
    public static final String JXCP_TYPE = "26";
    /**
     * 抗侧滚扭杆
     */
    public static final String KCGNG_TYPE = "05";
    /**
     * 空气弹簧
     */
    public static final String KQTH_TYPE = "04";
    /**
     * 连杆组件
     */
    public static final String LGZJ_TYPE = "06";
    /**
     * 耐磨板
     */
    public static final String NMB_TYPE = "30";
    /**
     * 内装
     */
    public static final String NZ_TYPE = "31";
    public static final Map<String, String> PRODUCT_TYPE_MAP = new HashMap<String, String>() {
        {
            put("01", "球铰关节");
            put("02", "橡胶垫");
            put("03", "止挡");
            put("04", "空气弹簧");
            put("05", "抗侧滚扭杆");
            put("06", "连杆组件");
            put("07", "牵引装置");
            put("08", "橡胶堆");
            put("09", "锥形簧");
            put("10", "V形簧");
            put("11", "其他悬挂");
            put("12", "沙漏簧");
            put("13", "轨道减振器");
            put("14", "道岔");
            put("15", "安全装置");
            put("16", "弹性垫板");
            put("17", "套靴");
            put("18", "橡胶支座");
            put("19", "桥梁支座");
            put("20", "阻尼器");
            put("21", "止水产品");
            put("22", "发电机弹性支撑");
            put("23", "叠簧式齿轮箱弹性支撑总成");
            put("24", "轴瓦式齿轮箱弹性支撑");
            put("25", "其它风电");
            put("26", "检修产品");
            put("27", "机舱罩弹性支撑");

            put("28", "层压产品");
            put("29", "轨枕");
            put("30", "耐磨板");
            put("31", "内装");
            put("32", "挡板座");
            put("33", "轨距块");
            put("34", "轨距挡板");
            put("35", "套管");
            put("36", "调高垫板");
        }
    };
    /**
     * 球铰关节
     */
    public static final String QJGX_TYPE = "01";
    /**
     * 桥梁支座
     */
    public static final String QLZZ_TYPE = "19";
    /**
     * 其它风电
     */
    public static final String QTFD_TYPE = "25";
    /**
     * 其他
     */
    public static final String QT_TYPE = "11";
    /**
     * 牵引装置
     */
    public static final String QYZZ_TYPE = "07";
    public static final List<String> SHEET_NAME_LIST = new ArrayList<String>() {{
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
        add("其他悬挂");
        add("沙漏簧");
        add("轨道减振器");
        add("道岔");
        add("安全装置");
        add("弹性垫板");
        add("套靴");
        add("橡胶支座");
        add("桥梁支座");
        add("阻尼器");
        add("止水产品");
        add("发电机弹性支撑");
        add("叠簧式齿轮箱弹性支撑总成");
        add("轴瓦式齿轮箱弹性支撑");
        add("其它风电");
        add("检修产品");
        add("机舱罩弹性支撑");

        add("层压产品");
        add("轨枕");
        add("耐磨板");
        add("内装");
        add("挡板座");
        add("轨距块");
        add("轨距挡板");
        add("套管");
        add("调高垫板");
    }};
    /**
     * 沙漏簧
     */
    public static final String SLH_TYPE = "12";
    /**
     * 调高垫板
     */
    public static final String TGDB_TYPE = "36";
    /**
     * 套管
     */
    public static final String TG_TYPE = "35";
    /**
     * 弹性垫板
     */
    public static final String TXDB_TYPE = "16";
    /**
     * 套靴
     */
    public static final String TX_TYPE = "17";
    /**
     * V形簧
     */
    public static final String VXH_TYPE = "10";
    /**
     * 橡胶垫
     */
    public static final String XJDIAN_TYPE = "02";
    /**
     * 橡胶堆
     */
    public static final String XJDUI_TYPE = "08";
    /**
     * 橡胶支座
     */
    public static final String XJZZ_TYPE = "18";
    /**
     * 轨枕
     */
    public static final String ZG_TYPE = "29";
    /**
     * 止挡
     */
    public static final String ZHID_TYPE = "03";
    /**
     * 阻尼器
     */
    public static final String ZNQ_TYPE = "20";
    /**
     * 止水产品
     */
    public static final String ZSCP_TYPE = "21";
    /**
     * 轴瓦式齿轮箱弹性支撑
     */
    public static final String ZWSCLX_TYPE = "24";
    /**
     * 锥形簧
     */
    public static final String ZXH_TYPE = "09";
    @Resource
    private ZcCategoryService zcCategoryService;
    @Resource
    private ZcProductMapper zcProductMapper;

    public static void main(String[] args) {
        Map<String, Object> trainTypeMap = new HashMap<>();
        trainTypeMap.put("铁路桥梁", "08001");
        trainTypeMap.put("公路桥梁", "08002");

        ZcProductService service = new ZcProductService();
        System.out.println(service.getTrainTypeCode("铁路桥梁;公路桥梁", trainTypeMap));

        Map<String, Object> platformMap = new HashMap<>();
        platformMap.put("桥建", "08");
        platformMap.put("其他", "09");
        System.out.println(service.getTrainTypeCode("其他;桥建", platformMap));
    }

    private String getTrainTypeCode(String trainType, Map<String, Object> trainTypeMap) {
        if (!StringUtils.isEmpty(trainType)) {
            StringBuilder trainTypeCode = new StringBuilder();
            String[] trainTypes = trainType.replaceAll("；", ";").split(";");
            for (String type : trainTypes) {
                if (trainTypeMap.get(type) != null) {
                    trainTypeCode.append(trainTypeMap.get(type) + ";");
                }
            }

            if (trainTypeCode.length() > 0) {
                return trainTypeCode.substring(0, trainTypeCode.length() - 1);
            }
        }

        return trainType;
    }

    public void add(ZcProduct product) {
        zcProductMapper.insertSelective(product);
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
            for (int i = 0; i < sheetNum; i++) {
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
                if (isImportSheet && sheet.getLastRowNum() > 1) {
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
                                        || XJDIAN_TYPE.equals(producyType) || SLH_TYPE.equals(producyType)) {
                                    product.setWj(value);
                                } else if (KCGNG_TYPE.equals(producyType)) {
                                    product.setLgjzdjj(value);
                                } else if (LGZJ_TYPE.equals(producyType)) {
                                    product.setLgzxj(value);
                                } else if (QYZZ_TYPE.equals(producyType) || VXH_TYPE.equals(producyType)
                                        || QT_TYPE.equals(producyType) || AQZZ_TYPE.equals(producyType) || TXDB_TYPE.equals(producyType)
                                        || TX_TYPE.equals(producyType) || XJZZ_TYPE.equals(producyType)
                                        || ZSCP_TYPE.equals(producyType) || DHSCLX_TYPE.equals(producyType) || QTFD_TYPE.equals(producyType)
                                        || CYCP_TYPE.equals(producyType) || ZG_TYPE.equals(producyType) || NMB_TYPE.equals(producyType)
                                        || TG_TYPE.equals(producyType) || TGDB_TYPE.equals(producyType)) {
                                    product.setCd(value);
                                } else if (ZXH_TYPE.equals(producyType)) {
                                    product.setWj(value);
                                } else if (QLZZ_TYPE.equals(producyType)) {
                                    product.setSxczl(value);
                                } else if (ZHID_TYPE.equals(producyType)) {
                                    product.setZyg(value);
                                } else if (GDJZQ_TYPE.equals(producyType) || DC_TYPE.equals(producyType)) {
                                    product.setPpggxh(value);
                                } else if (ZNQ_TYPE.equals(producyType)) {
                                    product.setZnxs(value);
                                } else if (FDJTXZC_TYPE.equals(producyType) || JCZTXZC_TYPE.equals(producyType)) {
                                    product.setGd(value);
                                } else if (ZWSCLX_TYPE.equals(producyType)) {
                                    product.setNj(value);
                                } else if (JXCP_TYPE.equals(producyType)) {
                                    product.setTraintype(getTrainTypeCode(value, trainTypeMap));
                                } else if (DBZ_TYPE.equals(producyType) || GJDB_TYPE.equals(producyType)) {
                                    product.setXingh(value);
                                } else if (GJK_TYPE.equals(producyType)) {
                                    product.setGjbhd(value);
                                } else if (NZ_TYPE.equals(producyType)) {
                                    product.setCail(value);
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
                                } else if (XJDUI_TYPE.equals(producyType) || NZ_TYPE.equals(producyType)) {
                                    product.setCd(value);
                                } else if (XJDIAN_TYPE.equals(producyType) || SLH_TYPE.equals(producyType)) {
                                    product.setGd(value);
                                } else if (VXH_TYPE.equals(producyType)) {
                                    product.setKd(value);
                                } else if (ZHID_TYPE.equals(producyType)) {
                                    product.setZhouxgd(value);
                                } else if (QT_TYPE.equals(producyType) || TXDB_TYPE.equals(producyType) || TX_TYPE.equals(producyType)
                                        || XJZZ_TYPE.equals(producyType) || ZSCP_TYPE.equals(producyType) || NMB_TYPE.equals(producyType)
                                        || DHSCLX_TYPE.equals(producyType) || CYCP_TYPE.equals(producyType) || ZG_TYPE.equals(producyType)
                                        || TGDB_TYPE.equals(producyType)) {
                                    product.setKd(value);
                                } else if (GDJZQ_TYPE.equals(producyType) || DC_TYPE.equals(producyType)) {
                                    product.setZyg(value);
                                } else if (AQZZ_TYPE.equals(producyType)) {
                                    product.setCskd(value);
                                } else if (QLZZ_TYPE.equals(producyType)) {
                                    product.setSpczl(value);
                                } else if (ZNQ_TYPE.equals(producyType)) {
                                    product.setSdzs(value);
                                } else if (FDJTXZC_TYPE.equals(producyType) || JCZTXZC_TYPE.equals(producyType)) {
                                    product.setAzlwgg(value);
                                } else if (ZWSCLX_TYPE.equals(producyType)) {
                                    product.setWj(value);
                                } else if (QTFD_TYPE.equals(producyType)) {
                                    product.setZhij(value);
                                } else if (JXCP_TYPE.equals(producyType)) {
                                    product.setPlatform(getPlatformCode(value, platformMap));
                                } else if (DBZ_TYPE.equals(producyType)) {
                                    product.setHda(value);
                                } else if (GJK_TYPE.equals(producyType)) {
                                    product.setKybhd(value);
                                } else if (GJDB_TYPE.equals(producyType)) {
                                    product.setTraintype(getTrainTypeCode(value, trainTypeMap));
                                } else if (TG_TYPE.equals(producyType)) {
                                    product.setNj(value);
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
                                } else if (XJDUI_TYPE.equals(producyType) || NZ_TYPE.equals(producyType)) {
                                    product.setKd(value);
                                } else if (XJDIAN_TYPE.equals(producyType)) {
                                    product.setSyhz(value);
                                } else if (VXH_TYPE.equals(producyType) || TXDB_TYPE.equals(producyType) || TX_TYPE.equals(producyType)
                                        || XJZZ_TYPE.equals(producyType) || ZSCP_TYPE.equals(producyType)
                                        || DHSCLX_TYPE.equals(producyType) || CYCP_TYPE.equals(producyType) || ZG_TYPE.equals(producyType)) {
                                    product.setGd(value);
                                } else if (ZHID_TYPE.equals(producyType)) {
                                    product.setTraintype(getTrainTypeCode(value, trainTypeMap));
                                } else if (QT_TYPE.equals(producyType)) {
                                    product.setGd(value);
                                } else if (QLZZ_TYPE.equals(producyType)) {
                                    product.setDfz(value);
                                } else if (GDJZQ_TYPE.equals(producyType) || DC_TYPE.equals(producyType)) {
                                    product.setAzkj(value);
                                } else if (AQZZ_TYPE.equals(producyType)) {
                                    product.setAzkw(value);
                                } else if (ZNQ_TYPE.equals(producyType)) {
                                    product.setZnl(value);
                                } else if (FDJTXZC_TYPE.equals(producyType) || JCZTXZC_TYPE.equals(producyType)) {
                                    product.setCxgd(value);
                                } else if (ZWSCLX_TYPE.equals(producyType)) {
                                    product.setCd(value);
                                } else if (QTFD_TYPE.equals(producyType)) {
                                    product.setAzls(value);
                                } else if (SLH_TYPE.equals(producyType)) {
                                    product.setCxhz(value);
                                } else if (JXCP_TYPE.equals(producyType)) {
                                    product.setProvider(value);
                                } else if (NMB_TYPE.equals(producyType) || TGDB_TYPE.equals(producyType)) {
                                    product.setHd(value);
                                } else if (DBZ_TYPE.equals(producyType)) {
                                    product.setHdb(value);
                                } else if (GJK_TYPE.equals(producyType)) {
                                    product.setYingd(value);
                                } else if (GJDB_TYPE.equals(producyType)) {
                                    product.setPlatform(getPlatformCode(value, platformMap));
                                } else if (TG_TYPE.equals(producyType)) {
                                    product.setWj(value);
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
                                } else if (XJDUI_TYPE.equals(producyType) || NZ_TYPE.equals(producyType)) {
                                    product.setGd(value);
                                } else if (XJDIAN_TYPE.equals(producyType)) {
                                    product.setGangd(value);
                                } else if (VXH_TYPE.equals(producyType)) {
                                    product.setVxjd(value);
                                } else if (ZHID_TYPE.equals(producyType)) {
                                    product.setPlatform(getPlatformCode(value, platformMap));
                                } else if (QT_TYPE.equals(producyType) || AQZZ_TYPE.equals(producyType) || FDJTXZC_TYPE.equals(producyType)
                                        || QTFD_TYPE.equals(producyType) || JCZTXZC_TYPE.equals(producyType) || ZG_TYPE.equals(producyType)
                                        || GJK_TYPE.equals(producyType)) {
                                    product.setTraintype(getTrainTypeCode(value, trainTypeMap));
                                } else if (GDJZQ_TYPE.equals(producyType) || DC_TYPE.equals(producyType) || TXDB_TYPE.equals(producyType)
                                        || TX_TYPE.equals(producyType)) {
                                    product.setJgd(value);
                                } else if (ZWSCLX_TYPE.equals(producyType) || SLH_TYPE.equals(producyType)) {
                                    product.setCxgd(value);
                                } else if (XJZZ_TYPE.equals(producyType)) {
                                    product.setZhij(value);
                                } else if (QLZZ_TYPE.equals(producyType)) {
                                    product.setWdwy(value);
                                } else if (ZNQ_TYPE.equals(producyType)) {
                                    product.setXc(value);
                                } else if (ZSCP_TYPE.equals(producyType) || CYCP_TYPE.equals(producyType)) {
                                    product.setBj(value);
                                } else if (DHSCLX_TYPE.equals(producyType)) {
                                    product.setDhzj(value);
                                } else if (JXCP_TYPE.equals(producyType)) {
                                    product.setRemark(value);
                                } else if (NMB_TYPE.equals(producyType)) {
                                    product.setMd(value);
                                } else if (DBZ_TYPE.equals(producyType)) {
                                    product.setCanb(value);
                                } else if (GJDB_TYPE.equals(producyType)) {
                                    product.setProvider(value);
                                } else if (TG_TYPE.equals(producyType)) {
                                    product.setKbl(value);
                                } else if (TGDB_TYPE.equals(producyType)) {
                                    product.setYingd(value);
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
                                } else if (ZXH_TYPE.equals(producyType) || DHSCLX_TYPE.equals(producyType)) {
                                    product.setCxgd(value);
                                } else if (XJDUI_TYPE.equals(producyType)) {
                                    product.setCxhz(value);
                                } else if (XJDIAN_TYPE.equals(producyType) || ZSCP_TYPE.equals(producyType) || ZSCP_TYPE.equals(producyType)
                                        || ZWSCLX_TYPE.equals(producyType) || SLH_TYPE.equals(producyType) || CYCP_TYPE.equals(producyType)
                                        || DBZ_TYPE.equals(producyType) || TG_TYPE.equals(producyType) || TGDB_TYPE.equals(producyType)) {
                                    product.setTraintype(getTrainTypeCode(value, trainTypeMap));
                                } else if (VXH_TYPE.equals(producyType)) {
                                    product.setKz(value);
                                } else if (ZHID_TYPE.equals(producyType)) {
                                    product.setProvider(value);
                                } else if (QT_TYPE.equals(producyType) || AQZZ_TYPE.equals(producyType) || FDJTXZC_TYPE.equals(producyType)
                                        || QTFD_TYPE.equals(producyType) || JCZTXZC_TYPE.equals(producyType) || ZG_TYPE.equals(producyType)
                                        || GJK_TYPE.equals(producyType)) {
                                    product.setPlatform(getPlatformCode(value, platformMap));
                                } else if (GDJZQ_TYPE.equals(producyType) || DC_TYPE.equals(producyType) || TXDB_TYPE.equals(producyType)
                                        || TX_TYPE.equals(producyType)) {
                                    product.setDjgdb(value);
                                } else if (XJZZ_TYPE.equals(producyType)) {
                                    product.setDxznb(value);
                                } else if (QLZZ_TYPE.equals(producyType)) {
                                    product.setSypd(value);
                                } else if (ZNQ_TYPE.equals(producyType)) {
                                    product.setQfl(value);
                                } else if (NMB_TYPE.equals(producyType)) {
                                    product.setLsqd(value);
                                } else if (NZ_TYPE.equals(producyType)) {
                                    product.setFhgf(value);
                                } else if (GJDB_TYPE.equals(producyType)) {
                                    product.setRemark(value);
                                }
                            } else if (j == 11) {
                                if (KQTH_TYPE.equals(producyType)) {
                                    product.setTraintype(getTrainTypeCode(value, trainTypeMap));
                                } else if (KCGNG_TYPE.equals(producyType)) {
                                    product.setXtgd(value);
                                } else if (LGZJ_TYPE.equals(producyType)) {
                                    product.setJxgd(value);
                                } else if (QYZZ_TYPE.equals(producyType)) {
                                    product.setZongxgd(value);
                                } else if (QJGX_TYPE.equals(producyType) || GDJZQ_TYPE.equals(producyType) || DC_TYPE.equals(producyType)
                                        || TXDB_TYPE.equals(producyType) || TX_TYPE.equals(producyType) || DHSCLX_TYPE.equals(producyType)
                                        || NZ_TYPE.equals(producyType)) {
                                    product.setTraintype(getTrainTypeCode(value, trainTypeMap));
                                } else if (ZXH_TYPE.equals(producyType)) {
                                    product.setYsg(value);
                                } else if (XJDUI_TYPE.equals(producyType)) {
                                    product.setCxgd(value);
                                } else if (XJDIAN_TYPE.equals(producyType) || ZSCP_TYPE.equals(producyType) || ZWSCLX_TYPE.equals(producyType)
                                        || SLH_TYPE.equals(producyType) || CYCP_TYPE.equals(producyType) || DBZ_TYPE.equals(producyType)
                                        || TG_TYPE.equals(producyType) || TGDB_TYPE.equals(producyType)) {
                                    product.setPlatform(getPlatformCode(value, platformMap));
                                } else if (VXH_TYPE.equals(producyType)) {
                                    product.setZdhz(value);
                                } else if (ZHID_TYPE.equals(producyType)) {
                                } else if (QT_TYPE.equals(producyType)) {
                                } else if (AQZZ_TYPE.equals(producyType) || FDJTXZC_TYPE.equals(producyType) || QTFD_TYPE.equals(producyType)
                                        || JCZTXZC_TYPE.equals(producyType) || ZG_TYPE.equals(producyType) || GJK_TYPE.equals(producyType)) {
                                    product.setProvider(value);
                                } else if (XJZZ_TYPE.equals(producyType)) {
                                    product.setTraintype(getTrainTypeCode(value, trainTypeMap));
                                } else if (QLZZ_TYPE.equals(producyType)) {
                                    product.setSywd(value);
                                } else if (ZNQ_TYPE.equals(producyType)) {
                                    product.setQfwy(value);
                                } else if (NMB_TYPE.equals(producyType)) {
                                    product.setDllsyb(value);
                                }
                            } else if (j == 12) {
                                if (KQTH_TYPE.equals(producyType) || GDJZQ_TYPE.equals(producyType) || DC_TYPE.equals(producyType)
                                        || TXDB_TYPE.equals(producyType) || TX_TYPE.equals(producyType) || DHSCLX_TYPE.equals(producyType)) {
                                    product.setPlatform(getPlatformCode(value, platformMap));
                                } else if (KCGNG_TYPE.equals(producyType)) {
                                    product.setWeight(value);
                                } else if (LGZJ_TYPE.equals(producyType)) {
                                    product.setWeight(value);
                                } else if (QYZZ_TYPE.equals(producyType)) {
                                    product.setWeight(value);
                                } else if (QJGX_TYPE.equals(producyType) || NZ_TYPE.equals(producyType)) {
                                    product.setPlatform(getPlatformCode(value, platformMap));
                                } else if (ZXH_TYPE.equals(producyType) || DC_TYPE.equals(producyType) || QLZZ_TYPE.equals(producyType)) {
                                    product.setTraintype(getTrainTypeCode(value, trainTypeMap));
                                } else if (XJDUI_TYPE.equals(producyType)) {
                                    product.setJqxxgd(value);
                                } else if (XJDIAN_TYPE.equals(producyType) || ZSCP_TYPE.equals(producyType) || ZWSCLX_TYPE.equals(producyType)
                                        || SLH_TYPE.equals(producyType) || CYCP_TYPE.equals(producyType) || DBZ_TYPE.equals(producyType)
                                        || TG_TYPE.equals(producyType) || TGDB_TYPE.equals(producyType)) {
                                    product.setProvider(value);
                                } else if (VXH_TYPE.equals(producyType)) {
                                    product.setCxgd(value);
                                } else if (ZHID_TYPE.equals(producyType)) {
                                } else if (QT_TYPE.equals(producyType)) {

                                } else if (AQZZ_TYPE.equals(producyType) || FDJTXZC_TYPE.equals(producyType) || QTFD_TYPE.equals(producyType)
                                        || JCZTXZC_TYPE.equals(producyType) || ZG_TYPE.equals(producyType) || GJK_TYPE.equals(producyType)) {
                                    product.setRemark(value);
                                } else if (XJZZ_TYPE.equals(producyType)) {
                                    product.setPlatform(getPlatformCode(value, platformMap));
                                } else if (ZNQ_TYPE.equals(producyType)) {
                                    product.setZnwy(value);
                                } else if (NMB_TYPE.equals(producyType)) {
                                    product.setLstxml(value);
                                }
                            } else if (j == 13) {
                                if (KQTH_TYPE.equals(producyType) || GDJZQ_TYPE.equals(producyType) || DC_TYPE.equals(producyType)
                                        || TXDB_TYPE.equals(producyType) || TX_TYPE.equals(producyType) || DHSCLX_TYPE.equals(producyType)) {
                                    product.setProvider(value);
                                } else if (KCGNG_TYPE.equals(producyType)) {
                                    product.setTraintype(getTrainTypeCode(value, trainTypeMap));
                                } else if (LGZJ_TYPE.equals(producyType)) {
                                    product.setTraintype(getTrainTypeCode(value, trainTypeMap));
                                } else if (QYZZ_TYPE.equals(producyType)) {
                                    product.setTraintype(getTrainTypeCode(value, trainTypeMap));
                                } else if (QJGX_TYPE.equals(producyType) || NZ_TYPE.equals(producyType)) {
                                    product.setProvider(value);
                                } else if (ZXH_TYPE.equals(producyType)) {
                                    product.setPlatform(getPlatformCode(value, platformMap));
                                } else if (XJDUI_TYPE.equals(producyType)) {
                                    product.setJqyxgd(value);
                                } else if (XJDIAN_TYPE.equals(producyType)) {
                                } else if (VXH_TYPE.equals(producyType)) {
                                    product.setHxgd(value);
                                } else if (ZHID_TYPE.equals(producyType)) {
                                } else if (QT_TYPE.equals(producyType)) {
                                    product.setProvider(value);
                                } else if (XJZZ_TYPE.equals(producyType)) {
                                    product.setProvider(value);
                                } else if (QLZZ_TYPE.equals(producyType)) {
                                    product.setPlatform(getPlatformCode(value, platformMap));
                                } else if (ZNQ_TYPE.equals(producyType)) {
                                    product.setSdl(value);
                                } else if (ZSCP_TYPE.equals(producyType) || ZWSCLX_TYPE.equals(producyType) || CYCP_TYPE.equals(producyType)
                                        || DBZ_TYPE.equals(producyType) || TG_TYPE.equals(producyType) || TGDB_TYPE.equals(producyType)) {
                                    product.setRemark(value);
                                } else if (NMB_TYPE.equals(producyType)) {
                                    product.setQyhyd(value);
                                }
                            } else if (j == 14) {
                                if (KQTH_TYPE.equals(producyType)) {
                                } else if (KCGNG_TYPE.equals(producyType)) {
                                    product.setPlatform(getPlatformCode(value, platformMap));
                                } else if (LGZJ_TYPE.equals(producyType)) {
                                    product.setPlatform(getPlatformCode(value, platformMap));
                                } else if (QYZZ_TYPE.equals(producyType)) {
                                    product.setPlatform(getPlatformCode(value, platformMap));
                                } else if (QJGX_TYPE.equals(producyType)) {
                                } else if (ZXH_TYPE.equals(producyType)) {
                                    product.setProvider(value);
                                } else if (XJDUI_TYPE.equals(producyType) || ZNQ_TYPE.equals(producyType)) {
                                    product.setTraintype(getTrainTypeCode(value, trainTypeMap));
                                } else if (XJDIAN_TYPE.equals(producyType)) {
                                } else if (VXH_TYPE.equals(producyType)) {
                                    product.setZongxgd(value);
                                } else if (ZHID_TYPE.equals(producyType)) {
                                } else if (XJZZ_TYPE.equals(producyType) || NZ_TYPE.equals(producyType)) {
                                    product.setRemark(value);
                                } else if (QT_TYPE.equals(producyType)) {
                                } else if (QLZZ_TYPE.equals(producyType)) {
                                    product.setProvider(value);
                                } else if (NMB_TYPE.equals(producyType)) {
                                    product.setCsjmcxs(value);
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
                                } else if (XJDUI_TYPE.equals(producyType) || ZNQ_TYPE.equals(producyType)) {
                                    product.setPlatform(getPlatformCode(value, platformMap));
                                } else if (XJDIAN_TYPE.equals(producyType)) {
                                } else if (VXH_TYPE.equals(producyType)) {
                                    product.setYsg(value);
                                } else if (ZHID_TYPE.equals(producyType)) {
                                } else if (QT_TYPE.equals(producyType)) {
                                } else if (QLZZ_TYPE.equals(producyType)) {
                                    product.setRemark(value);
                                } else if (NMB_TYPE.equals(producyType)) {
                                    product.setXmhl(value);
                                }
                            } else if (j == 16) {
                                if (VXH_TYPE.equals(producyType)) {
                                    product.setWeight(value);
                                } else if (XJDUI_TYPE.equals(producyType) || ZNQ_TYPE.equals(producyType)) {
                                    product.setProvider(value);
                                } else if (NMB_TYPE.equals(producyType)) {
                                    product.setTraintype(getTrainTypeCode(value, trainTypeMap));
                                }
                            } else if (j == 17) {
                                if (VXH_TYPE.equals(producyType)) {
                                    product.setTraintype(getTrainTypeCode(value, trainTypeMap));
                                } else if (NMB_TYPE.equals(producyType)) {
                                    product.setPlatform(getPlatformCode(value, platformMap));
                                }
                            } else if (j == 18) {
                                if (VXH_TYPE.equals(producyType)) {
                                    product.setPlatform(getPlatformCode(value, platformMap));
                                } else if (NMB_TYPE.equals(producyType)) {
                                    product.setProvider(value);
                                }
                            } else if (j == 19) {
                                if (VXH_TYPE.equals(producyType)) {
                                    product.setProvider(value);
                                } else if (NMB_TYPE.equals(producyType)) {
                                    product.setRemark(value);
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

    private String getPlatformCode(String platformName, Map<String, Object> platformMap) {
        if (!StringUtils.isEmpty(platformName)) {
            StringBuilder platformCode = new StringBuilder();
            String[] platformNames = platformName.replaceAll("；", ";").split(";");
            for (String name : platformNames) {
                if (platformMap.get(name) != null) {
                    platformCode.append(platformMap.get(name) + ";");
                }

            }

            if (platformCode.length() > 0) {
                return platformCode.substring(0, platformCode.length() - 1);
            }
        }

        return platformName;
    }

    public int countSelective(String productNum, String productName, String productType,
                              String platform, String trainType, List<ZcProductSearchCondition> conditionList,
                              String nameOrNum) {
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

        if (!StringUtils.isEmpty(nameOrNum)) {
            criteria.andOrNameOrNum("%" + nameOrNum + "%");
        }

        if (!StringUtils.isEmpty(platform)) {
            String[] platforms = platform.split(",");
            if (platforms != null && platforms.length > 0) {
                List<String> platformList = Arrays.asList(platforms);
                criteria.andPlatformIn(platformList);
            }
        }

        if (!StringUtils.isEmpty(trainType)) {
            criteria.andTraintypeLike("%" + trainType + "%");
        }

        setSearchCondition(criteria, conditionList);
        return (int) zcProductMapper.countByExample(example);
    }

    private void setSearchCondition(ZcProductExample.Criteria criteria, List<ZcProductSearchCondition> conditionList) {
        if (!CollectionUtils.isEmpty(conditionList)) {
            for (ZcProductSearchCondition condition : conditionList) {
                if ("wj".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andWjGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andWjLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("bzgd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andBzgdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andBzgdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("lgjzdjj".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andLgjzdjjGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andLgjzdjjLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("zczjj".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andZczjjGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andZczjjLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("ngbcd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andNgbcdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andNgbcdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("lgzxj".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andLgzxjGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andLgzxjLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("gtqtwj".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andGtqtwjGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andGtqtwjLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("zongxgd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andZongxgdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andZongxgdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("jxgd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andJxgdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andJxgdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("gd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andGdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andGdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("gangd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andGangdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andGangdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("zyg".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andZygGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andZygLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("zhouxgd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andZhouxgdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andZhouxgdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("cxgd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andCxgdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andCxgdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("vxjd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andVxjdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andVxjdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("cd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andCdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andCdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("kd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andKdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andKdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("dhzj".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andDhzjGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andDhzjLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("nj".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andNjGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andNjLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("jgd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andJgdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andJgdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("cskd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andCskdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andCskdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("sxczl".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andSxczlGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andSxczlLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("znl".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andZnlGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andZnlLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("znxs".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andZnxsGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andZnxslLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("sdzs".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andSdzsGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andSdzsLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("zhij".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andZhijGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andZhijLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("md".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andMdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andMdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("hda".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andHdaGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andHdaLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("hdb".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andHdbGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andHdbLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("canb".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andCanbGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andCanbLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("gjbhd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andGjbhdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andGjbhdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("kybhd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andKybhdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andKybhdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("yingd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andYingdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andYingdLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("kbl".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andKblGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andKblLessThanOrEqualTo(condition.getHigh());
                    }
                } else if ("hd".equals(condition.getKey())) {
                    if (!StringUtils.isEmpty(condition.getLow())) {
                        criteria.andhdGreaterThanOrEqualTo(condition.getLow());
                    }
                    if (!StringUtils.isEmpty(condition.getHigh())) {
                        criteria.andhdLessThanOrEqualTo(condition.getHigh());
                    }
                }
            }
        }
    }

    public void deleteById(Integer id) {
        zcProductMapper.deleteByPrimaryKey(id);
    }

    public ZcProduct findById(Integer id) {
        return zcProductMapper.selectByPrimaryKey(id);
    }

    public List<ZcAttrRange> queryAttrsRange() {
        return zcProductMapper.queryAttrsRangeList();
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

    public List<ZcProduct> queryByTrainType(String trainType, String sort, String order) {
        ZcProductExample example = new ZcProductExample();
        ZcProductExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(trainType)) {
            criteria.andTraintypeLike("%" + trainType + "%");
        }

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        return zcProductMapper.selectByExample(example);
    }

    public List<String> queryPlatformsByType(String productType) {
        return zcProductMapper.queryPlatformsByType(productType);
    }

    public List<ZcProducttype> queryProductType(String productName, String platform, String trainType, String productType) {
        ZcProductExample example = new ZcProductExample();
        ZcProductExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(productName)) {
            criteria.andProductnameLike("%" + productName + "%");
        }

        if (!StringUtils.isEmpty(platform)) {
            String[] platforms = platform.split(",");
            if (platforms != null && platforms.length > 0) {
                List<String> platformList = Arrays.asList(platforms);
                criteria.andPlatformIn(platformList);
            }
        }

        if (!StringUtils.isEmpty(trainType)) {
            criteria.andTraintypeLike("%" + trainType + "%");
        }

        if (!StringUtils.isEmpty(productType)) {
            criteria.andProducttypeEqualToWithTable(productType);
        }
        return zcProductMapper.selectProductTypeByExample(example);
    }

    public List<ZcProduct> querySelective(String productNum, String productName, String productType,
                                          String platform, String trainType,
                                          Integer page, Integer limit, String sort, String order,
                                          List<ZcProductSearchCondition> conditionList, String nameOrNum) {
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

        if (!StringUtils.isEmpty(nameOrNum)) {
            criteria.andOrNameOrNum("%" + nameOrNum + "%");
        }

        if (!StringUtils.isEmpty(platform)) {
            String[] platforms = platform.split(",");
            if (platforms != null && platforms.length > 0) {
                List<String> platformList = Arrays.asList(platforms);
                criteria.andPlatformIn(platformList);
                //example.setPlatformList(Arrays.asList(platforms));
            }
        }

        if (!StringUtils.isEmpty(trainType)) {
            criteria.andTraintypeLike("%" + trainType + "%");
        }

        setSearchCondition(criteria, conditionList);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + "*1" + " " + order);
        }

        PageHelper.startPage(page, limit);
        return zcProductMapper.selectByExample(example);
    }

    public void updateById(ZcProduct product) {
        zcProductMapper.updateByPrimaryKeySelective(product);
    }

    public void updateRealpicByProductNum(String productNum, String url) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("productNum", productNum);
        params.put("url", url);
        zcProductMapper.updateRealpicByProductNum(params);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map updateRemark(String fileName, MultipartFile file) throws Exception {
        Map<String, Object> data = new HashMap<>();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            data.put("code", "0");
            data.put("message", "上传文件格式不正确");
            return data;
        }

        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }

        int sheetNum = wb.getNumberOfSheets();
        if (sheetNum > 0) {
            // 读取第一个页签
            Sheet sheet = wb.getSheetAt(0);
            if (sheet != null) {
                if (sheet.getLastRowNum() > 0) {
                    List<ZcProduct> productList = new ArrayList<>();
                    // 跳过第一行
                    for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                        Row row = sheet.getRow(r);
                        if (row == null) {
                            continue;
                        }
                        ZcProduct product = new ZcProduct();
                        product.setProductnum(getValue(row.getCell(0)));
                        product.setRemark(getValue(row.getCell(1)));
                        if (!StringUtils.isEmpty(product.getProductnum())) {
                            productList.add(product);
                        }
                    }

                    if (!CollectionUtils.isEmpty(productList)) {
                        // 查询物资编号列表
                        List<String> productNumList = zcProductMapper.queryProductNumList();
                        // 得到更新列表
                        List<ZcProduct> updateProductList = new ArrayList<>();
                        for (ZcProduct product : productList) {
                            if (productNumList.contains(product.getProductnum())) {
                                updateProductList.add(product);
                            }
                        }
                        if (!CollectionUtils.isEmpty(updateProductList)) {
                            zcProductMapper.batchUpdate(updateProductList);
                        }

                        List<String> updatedNumList = updateProductList.stream().map(ZcProduct::getProductnum).collect(Collectors.toList());
                        data.put("code", "0");
                        data.put("message", "已更新下列产品备注：" +  String.join(",", updatedNumList));
                    }
                }
            }
        }

        return data;
    }

    public void updateSnapshotByProductNum(String productNum, String url) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("productNum", productNum);
        params.put("url", url);
        zcProductMapper.updateSnapshotByProductNum(params);
    }
}
