<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入物资编号" v-model="listQuery.productNum">
      </el-input>
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入产品名称" v-model="listQuery.productName">
      </el-input>
      <el-select style="width: 200px" class="filter-item" placeholder="请选择产品类型" v-model="listQuery.productType">
        <el-option v-for="(item, index) in productTypeMap" :key="index" :label="item.value" :value="item.key">
        </el-option>
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate" icon="el-icon-edit">添加</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" class="table-expand">
            <el-form-item label="宣传画廊">
              <img class="gallery" v-for="pic in props.row.gallery" :key="pic" :src="pic"/>
            </el-form-item>
            <el-form-item label="商品介绍">
              <span>{{ props.row.brief }}</span>
            </el-form-item>
            <el-form-item label="商品单位">
              <span>{{ props.row.unit }}</span>
            </el-form-item>
            <el-form-item label="关键字">
              <span>{{ props.row.keyword }}</span>
            </el-form-item>
            <el-form-item label="类目ID">
              <span>{{ props.row.categoryId }}</span>
            </el-form-item>
            <el-form-item label="品牌商ID">
              <span>{{ props.row.brandId }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>

      <el-table-column align="center" label="物资编号" prop="productnum">
      </el-table-column>

      <el-table-column align="center" min-width="100" label="产品名称" prop="productname">
      </el-table-column>

      <el-table-column align="center" property="iconUrl" label="实物图">
        <template slot-scope="scope">
          <img :src="scope.row.picUrl" width="40"/>
        </template>
      </el-table-column>

      <el-table-column align="center" property="iconUrl" label="剖面图">
        <template slot-scope="scope">
          <img :src="scope.row.shareUrl" width="40"/>
        </template>
      </el-table-column>

      <el-table-column align="center" label="结构形式" prop="structtype">
      </el-table-column>

      <el-table-column align="center" label="产品应用车型" prop="traintype">
      </el-table-column>

      <el-table-column align="center" label="产品应用平台" prop="platform">
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

    <el-tooltip placement="top" content="返回顶部">
      <back-to-top :visibilityHeight="100" ></back-to-top>
    </el-tooltip>

  </div>
</template>

<style>
  .table-expand {
    font-size: 0;
  }
  .table-expand label {
    width: 100px;
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
  import { listProducts } from '@/api/product'
  import BackToTop from '@/components/BackToTop'

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
    data() {
      return {
        list: [],
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          productNum: undefined,
          productName: undefined,
          productType: undefined,
          sort: 'add_time',
          order: 'desc'
        },
        productTypeMap,
        goodsDetail: '',
        detailDialogVisible: false,
        downloadLoading: false
      }
    },
    created() {
      this.getList()
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
        this.$router.push({ path: '/goods/create' })
      },
      handleUpdate(row) {
        this.$router.push({ path: '/goods/edit', query: { id: row.id }})
      },
      showDetail(detail) {
        this.goodsDetail = detail
        this.detailDialogVisible = true
      },
      handleDelete(row) {
        /*
        deleteGoods(row).then(response => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          const index = this.list.indexOf(row)
          this.list.splice(index, 1)
        })
        */
      }
    }
  }
</script>
