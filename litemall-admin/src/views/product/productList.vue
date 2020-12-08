<template>
  <div class="app-container calendar-list-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入物资编号" v-model="listQuery.productNum"></el-input>
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入产品名称" v-model="listQuery.productName"></el-input>
      <el-select style="width: 200px" class="filter-item" placeholder="请选择产品类型" v-model="listQuery.productType">
        <el-option label="请选择产品类型" value=""></el-option>
        <el-option v-for="(item, index) in productTypeMap" :key="index" :label="item.typename" :value="item.producttype"></el-option>
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate" icon="el-icon-edit">添加</el-button>
      <el-upload class="filter-item" :action="importExcel" :show-file-list="false" :headers="headers"
                 accept=".xls,.xlsx" :before-upload="handlePreview" :on-success="handleSuccess">
        <el-button type="primary" :loading="importing" icon="el-icon-download">导入产品</el-button>
      </el-upload>
      <el-button class="filter-item" type="primary" icon="el-icon-download" @click="handleUploadPics">上传产品图片</el-button>
      <el-upload class="filter-item" :action="importRemarkExcel" :show-file-list="false" :headers="headers"
                 accept=".xls,.xlsx" :before-upload="handleNumPreview" :on-success="handleNumSuccess">
        <el-button type="primary" :loading="importingNum" icon="el-icon-download">更新产品备注</el-button>
      </el-upload>
    </div>
    <!-- 查询结果 -->
    <el-dialog :visible.sync="dialogVisible"><img width="100%" :src="dialogImageUrl" alt=""></el-dialog>
    <el-dialog :visible.sync="uploadVisible" :before-close="handleClose">
      <el-upload class="filter-item" :action="importPics" :show-file-list="true" :headers="headers" multiple
                 accept=".jpg,.png" :before-upload="handleImportingPics" :on-success="handleImportingPicsSuccess">
        <el-tooltip class="item" effect="dark" content="可多选,只处理jpg/png文件,且不超过5MB,'物资编号_1'匹配实物图,'物资编号_2'匹配示意图" placement="bottom">
          <el-button type="primary" :loading="importingPics" icon="el-icon-download">上传产品图片</el-button>
        </el-tooltip>
      </el-upload>
    </el-dialog>
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form v-if="props.row.producttype == '01'" label-position="left" class="table-expand">
            <el-form-item label="外径A/mm"><span>{{ props.row.wj }}</span></el-form-item>
            <el-form-item label="长度B/mm"><span>{{ props.row.cd }}</span></el-form-item>
            <el-form-item label="装配长度C/mm"><span>{{ props.row.zpcd }}</span></el-form-item>
            <el-form-item label="径向刚度/kN/mm"><span>{{ props.row.jxgd }}</span></el-form-item>
            <el-form-item label="轴向刚度/kN/mm"><span>{{ props.row.zhouxgd }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '02'" label-position="left" class="table-expand">
            <el-form-item label="外径A/mm"><span>{{ props.row.wj }}</span></el-form-item>
            <el-form-item label="高度B/mm"><span>{{ props.row.gd }}</span></el-form-item>
            <el-form-item label="受压荷载/kN"><span>{{ props.row.syhz }}</span></el-form-item>
            <el-form-item label="刚度/kN/mm"><span>{{ props.row.gangd }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '03'" label-position="left" class="table-expand">
            <el-form-item label="自由高/mm"><span>{{ props.row.zyg }}</span></el-form-item>
            <el-form-item label="轴向刚度/kN/mm"><span>{{ props.row.zhouxgd }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '04'" label-position="left" class="table-expand">
            <el-form-item label="外径/mm"><span>{{ props.row.wj }}</span></el-form-item>
            <el-form-item label="标准高度/mm"><span>{{ props.row.bzgd }}</span></el-form-item>
            <el-form-item label="空载/kN"><span>{{ props.row.kz }}</span></el-form-item>
            <el-form-item label="超载/kN"><span>{{ props.row.cz }}</span></el-form-item>
            <el-form-item label="重量/kg"><span>{{ props.row.weight }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '05'" label-position="left" class="table-expand">
            <el-form-item label="连杆加载点间距L1/mm"><span>{{ props.row.lgjzdjj }}</span></el-form-item>
            <el-form-item label="支撑座间距L2/mm"><span>{{ props.row.zczjj }}</span></el-form-item>
            <el-form-item label="扭杆臂长度L3/mm"><span>{{ props.row.ngbcd }}</span></el-form-item>
            <el-form-item label="极限荷载/kN"><span>{{ props.row.jxhz }}</span></el-form-item>
            <el-form-item label="疲劳荷载/kN"><span>{{ props.row.plhz }}</span></el-form-item>
            <el-form-item label="系统刚度MN.m/rad"><span>{{ props.row.xtgd }}</span></el-form-item>
            <el-form-item label="重量/kg"><span>{{ props.row.weight }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '06'" label-position="left" class="table-expand">
            <el-form-item label="连杆中心距L1/mm"><span>{{ props.row.lgzxj }}</span></el-form-item>
            <el-form-item label="杆体球头外径D1/mm"><span>{{ props.row.gtqtwj }}</span></el-form-item>
            <el-form-item label="孔类型"><span>{{ props.row.klx }}</span></el-form-item>
            <el-form-item label="极限荷载/kN"><span>{{ props.row.jxhz }}</span></el-form-item>
            <el-form-item label="压装力/kN"><span>{{ props.row.yzl }}</span></el-form-item>
            <el-form-item label="径向刚度/kN/mm"><span>{{ props.row.jxgd }}</span></el-form-item>
            <el-form-item label="重量/kg"><span>{{ props.row.weight }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '07'" label-position="left" class="table-expand">
            <el-form-item label="长度L1/mm"><span>{{ props.row.cd }}</span></el-form-item>
            <el-form-item label="宽度L2/mm"><span>{{ props.row.kd }}</span></el-form-item>
            <el-form-item label="高度L3/mm"><span>{{ props.row.gd }}</span></el-form-item>
            <el-form-item label="装配尺寸L4/mm"><span>{{ props.row.zpcc }}</span></el-form-item>
            <el-form-item label="极限荷载/kN"><span>{{ props.row.jxhz }}</span></el-form-item>
            <el-form-item label="纵向刚度/kN/mm"><span>{{ props.row.zongxgd }}</span></el-form-item>
            <el-form-item label="重量/kg"><span>{{ props.row.weight }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '08'" label-position="left" class="table-expand">
            <el-form-item label="外径A/mm"><span>{{ props.row.wj }}</span></el-form-item>
            <el-form-item label="高度C/mm"><span>{{ props.row.gd }}</span></el-form-item>
            <el-form-item label="垂向荷载/kN"><span>{{ props.row.cxhz }}</span></el-form-item>
            <el-form-item label="垂向刚度/kN/mm"><span>{{ props.row.cxgd }}</span></el-form-item>
            <el-form-item label="剪切X向刚度/kN/mm"><span>{{ props.row.jqxxgd }}</span></el-form-item>
            <el-form-item label="剪切Y向刚度/kN/mm"><span>{{ props.row.jqyxgd }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '09'" label-position="left" class="table-expand">
            <el-form-item label="外径A/mm"><span>{{ props.row.wj }}</span></el-form-item>
            <el-form-item label="高度B/mm"><span>{{ props.row.gd }}</span></el-form-item>
            <el-form-item label="空载/kN"><span>{{ props.row.kz }}</span></el-form-item>
            <el-form-item label="最大荷载/kN"><span>{{ props.row.zdhz }}</span></el-form-item>
            <el-form-item label="垂向刚度/kN/mm"><span>{{ props.row.cxgd }}</span></el-form-item>
            <el-form-item label="压缩高/mm"><span>{{ props.row.ysg }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '10'" label-position="left" class="table-expand">
            <el-form-item label="A/mm"><span>{{ props.row.cd }}</span></el-form-item>
            <el-form-item label="B/mm"><span>{{ props.row.kd }}</span></el-form-item>
            <el-form-item label="C/mm"><span>{{ props.row.gd }}</span></el-form-item>
            <el-form-item label="D/mm"><span>{{ props.row.vd }}</span></el-form-item>
            <el-form-item label="E/mm"><span>{{ props.row.ve }}</span></el-form-item>
            <el-form-item label="F/mm"><span>{{ props.row.vf }}</span></el-form-item>
            <el-form-item label="V形角度/°"><span>{{ props.row.vxjd }}</span></el-form-item>
            <el-form-item label="安装角度α/°"><span>{{ props.row.azjd }}</span></el-form-item>
            <el-form-item label="空载/kN"><span>{{ props.row.kz }}</span></el-form-item>
            <el-form-item label="最大荷载/kN"><span>{{ props.row.zdhz }}</span></el-form-item>
            <el-form-item label="垂向刚度/kN/mm"><span>{{ props.row.cxgd }}</span></el-form-item>
            <el-form-item label="横向刚度/kN/mm"><span>{{ props.row.hxgd }}</span></el-form-item>
            <el-form-item label="纵向刚度/kN/mm"><span>{{ props.row.zongxgd }}</span></el-form-item>
            <el-form-item label="压缩高（AWO）/mm"><span>{{ props.row.ysg }}</span></el-form-item>
            <el-form-item label="重量/kg"><span>{{ props.row.weight }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '11'" label-position="left" class="table-expand">
            <el-form-item label="长度A/mm"><span>{{ props.row.cd }}</span></el-form-item>
            <el-form-item label="宽度B/mm"><span>{{ props.row.kd }}</span></el-form-item>
            <el-form-item label="高度C/mm"><span>{{ props.row.gd }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '12'" label-position="left" class="table-expand">
            <el-form-item label="外径A/mm"><span>{{ props.row.wj }}</span></el-form-item>
            <el-form-item label="高度B/mm"><span>{{ props.row.gd }}</span></el-form-item>
            <el-form-item label="垂向刚度/kN/mm"><span>{{ props.row.cxgd }}</span></el-form-item>
            <el-form-item label="垂向荷载/kN"><span>{{ props.row.cxhz }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '13' || props.row.producttype == '14'" label-position="left" class="table-expand">
            <el-form-item label="自由高/mm"><span>{{ props.row.zyg }}</span></el-form-item>
            <el-form-item label="匹配钢轨型号"><span>{{ props.row.ppggxh }}</span></el-form-item>
            <el-form-item label="安装孔距L/mm"><span>{{ props.row.azkj }}</span></el-form-item>
            <el-form-item label="静刚度/kN/mm"><span>{{ props.row.jgd }}</span></el-form-item>
            <el-form-item label="动静刚度比"><span>{{ props.row.djgdb }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '15'" label-position="left" class="table-expand">
            <el-form-item label="长度A/mm"><span>{{ props.row.cd }}</span></el-form-item>
            <el-form-item label="齿梳宽度B/mm"><span>{{ props.row.cskd }}</span></el-form-item>
            <el-form-item label="安装孔位L/mm"><span>{{ props.row.azkw }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '16' || props.row.producttype == '17'" label-position="left" class="table-expand">
            <el-form-item label="长度A/mm"><span>{{ props.row.cd }}</span></el-form-item>
            <el-form-item label="宽度B/mm"><span>{{ props.row.kd }}</span></el-form-item>
            <el-form-item label="高度C/mm"><span>{{ props.row.gd }}</span></el-form-item>
            <el-form-item label="静刚度/kN/mm"><span>{{ props.row.jgd }}</span></el-form-item>
            <el-form-item label="动静刚度比"><span>{{ props.row.djgdb }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '18'" label-position="left" class="table-expand">
            <el-form-item label="长度A/mm"><span>{{ props.row.cd }}</span></el-form-item>
            <el-form-item label="宽度B/mm"><span>{{ props.row.kd }}</span></el-form-item>
            <el-form-item label="高度C/mm"><span>{{ props.row.gd }}</span></el-form-item>
            <el-form-item label="直径D/mm"><span>{{ props.row.zhij }}</span></el-form-item>
            <el-form-item label="等效阻尼比"><span>{{ props.row.dxznb }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '19'" label-position="left" class="table-expand">
            <el-form-item label="竖向承载力/kN"><span>{{ props.row.sxczl }}</span></el-form-item>
            <el-form-item label="水平承载力/kN"><span>{{ props.row.spczl }}</span></el-form-item>
            <el-form-item label="动峰值"><span>{{ props.row.dfz }}</span></el-form-item>
            <el-form-item label="温度位移/mm"><span>{{ props.row.wdwy }}</span></el-form-item>
            <el-form-item label="适用坡度/度"><span>{{ props.row.sypd }}</span></el-form-item>
            <el-form-item label="适用温度/℃"><span>{{ props.row.sywd }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '20'" label-position="left" class="table-expand">
            <el-form-item label="屈服力/kN"><span>{{ props.row.qfl }}</span></el-form-item>
            <el-form-item label="阻尼系数"><span>{{ props.row.znxs }}</span></el-form-item>
            <el-form-item label="速度指数"><span>{{ props.row.sdzs }}</span></el-form-item>
            <el-form-item label="阻尼力/kN"><span>{{ props.row.znl }}</span></el-form-item>
            <el-form-item label="行程/mm"><span>{{ props.row.xc }}</span></el-form-item>
            <el-form-item label="屈服位移/mm"><span>{{ props.row.qfwy }}</span></el-form-item>
            <el-form-item label="阻尼位移/mm"><span>{{ props.row.znwy }}</span></el-form-item>
            <el-form-item label="锁定力/kN"><span>{{ props.row.sdl }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '21'" label-position="left" class="table-expand">
            <el-form-item label="长度A/mm"><span>{{ props.row.cd }}</span></el-form-item>
            <el-form-item label="宽度B/mm"><span>{{ props.row.kd }}</span></el-form-item>
            <el-form-item label="高度C/mm"><span>{{ props.row.gd }}</span></el-form-item>
            <el-form-item label="半径/mm"><span>{{ props.row.bj }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '22'" label-position="left" class="table-expand">
            <el-form-item label="高度A/mm"><span>{{ props.row.gd }}</span></el-form-item>
            <el-form-item label="垂向刚度/kN/mm"><span>{{ props.row.cxgd }}</span></el-form-item>
            <el-form-item label="安装螺纹规格"><span>{{ props.row.azlwgg }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '23'" label-position="left" class="table-expand">
            <el-form-item label="长度A/mm"><span>{{ props.row.cd }}</span></el-form-item>
            <el-form-item label="宽度B/mm"><span>{{ props.row.kd }}</span></el-form-item>
            <el-form-item label="高度C/mm"><span>{{ props.row.gd }}</span></el-form-item>
            <el-form-item label="垂向刚度/kN/mm"><span>{{ props.row.cxgd }}</span></el-form-item>
            <el-form-item label="叠簧直径F/mm"><span>{{ props.row.dhzj }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '24'" label-position="left" class="table-expand">
            <el-form-item label="外径D/mm"><span>{{ props.row.wj }}</span></el-form-item>
            <el-form-item label="内径d/mm"><span>{{ props.row.nj }}</span></el-form-item>
            <el-form-item label="垂向刚度/kN/mm"><span>{{ props.row.cxgd }}</span></el-form-item>
            <el-form-item label="长度L/mm"><span>{{ props.row.cd }}</span></el-form-item>
          </el-form>
          <el-form v-else-if="props.row.producttype == '25'" label-position="left" class="table-expand">
            <el-form-item label="长度A/mm"><span>{{ props.row.cd }}</span></el-form-item>
            <el-form-item label="直径B/mm"><span>{{ props.row.zhij }}</span></el-form-item>
            <el-form-item label="安装螺栓C/mm"><span>{{ props.row.azls }}</span></el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column align="center" label="物资编号" prop="productnum"></el-table-column>
      <el-table-column align="center" min-width="100" label="产品名称" prop="productname"></el-table-column>
      <el-table-column align="center" property="iconUrl" label="实物图">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handlePicturePreview(scope.row.realpic)">查看</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" property="iconUrl" label="示意图">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handlePicturePreview(scope.row.snapshot)">查看</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="结构形式" prop="structtype">
      </el-table-column>
      <el-table-column align="center" label="产品应用车型" prop="traintypename">
      </el-table-column>
      <el-table-column align="center" label="产品应用平台" prop="platformname">
      </el-table-column>
      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button type="danger" size="mini"  @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.page"
                     :page-sizes="[10,20,30,50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
    <el-tooltip placement="top" content="返回顶部"><back-to-top :visibilityHeight="100" ></back-to-top></el-tooltip>
  </div>
</template>

<style>
  .table-expand {
    font-size: 0;
  }
  .table-expand label {
    width: 200px;
    color: #99a9bf;
  }
  .table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
  }
  .gallery {
    width: 80px;
    margin-right: 10px;
  }
</style>

<script>
  import { listProducts, deleteProduct, importExcel, importPics, getProductTypes, importRemarkExcel } from '@/api/product'
  import BackToTop from '@/components/BackToTop'
  import { getToken } from '@/utils/auth'

  const productTypeMap = [
    { key: '01', value: '球铰关节' },
    { key: '02', value: '橡胶垫' },
    { key: '03', value: '止挡' },
    { key: '04', value: '空气弹簧' },
    { key: '05', value: '抗侧滚扭杆' },
    { key: '06', value: '连杆组件' },
    { key: '07', value: '牵引装置' },
    { key: '08', value: '橡胶堆' },
    { key: '09', value: '锥形簧' },
    { key: '10', value: 'V形簧' },
    { key: '11', value: '其他' }
  ]

  export default {
    name: 'ProductList',
    components: { BackToTop },
    computed: {
      headers() {
        return {
          'Admin-Token': getToken()
        }
      }
    },
    data() {
      return {
        importExcel,
        importPics,
        importRemarkExcel,
        list: [],
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          productNum: undefined,
          productName: undefined,
          productType: undefined,
          sort: 'productnum',
          order: 'asc'
        },
        dialogImageUrl: '',
        dialogVisible: false,
        uploadVisible: false,
        productTypeMap,
        goodsDetail: '',
        detailDialogVisible: false,
        importing: false,
        importingNum: false,
        importingPics: false
      }
    },
    created() {
      this.getList()
      this.getProductTypes()
    },
    methods: {
      getList() {
        this.listLoading = true
        listProducts(this.listQuery).then(response => {
          this.list = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.list = []
          this.total = 0
          this.listLoading = false
        })
      },
      getProductTypes() {
        getProductTypes().then(response => {
          this.productTypeMap = response.data.data
        }).catch(() => {
        })
      },
      handleFilter() {
        this.listQuery.page = 1
        this.getList()
      },
      handleSizeChange(val) {
        this.listQuery.limit = val
        this.getList()
      },
      handleCurrentChange(val) {
        this.listQuery.page = val
        this.getList()
      },
      handleCreate() {
        this.$router.push({ path: '/product/create' })
      },
      handleUpdate(row) {
        this.$router.push({ path: '/product/create', query: { id: row.id }})
      },
      showDetail(detail) {
        this.goodsDetail = detail
        this.detailDialogVisible = true
      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.currentTarget.currentSrc
        this.dialogVisible = true
      },
      handlePicturePreview(picSrc) {
        this.dialogImageUrl = picSrc
        this.dialogVisible = true
      },
      handleUploadPics() {
        this.uploadVisible = true
      },
      handlePreview() {
        this.importing = true
      },
      handleNumPreview() {
        this.importingNum = true
      },
      handleImportingPics() {
        this.importingPics = true
      },
      handleSuccess() {
        this.importing = false
        this.getList()
        this.$notify({
          title: '成功',
          message: '导入成功',
          type: 'success',
          duration: 2000
        })
      },
      handleNumSuccess(response) {
        this.importingNum = false

        this.$alert(response.data.message, '成功', {
          confirmButtonText: '确定'
        });
      },
      handleImportingPicsSuccess(response, file, fileList) {
        this.importingPics = false
        if (response.errno === -1) {
          file.status = 'fail'
          this.$notify.error({
            title: '错误',
            message: '导入失败',
            duration: 2000
          })
        } else {
          this.$notify({
            title: '成功',
            message: '导入成功',
            type: 'success',
            duration: 2000
          })
        }
      },
      handleClose() {
        this.uploadVisible = false
        this.getList()
      },
      handleDelete(row) {
        deleteProduct(row).then(response => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          const index = this.list.indexOf(row)
          this.list.splice(index, 1)
        })
      }
    }
  }
</script>
