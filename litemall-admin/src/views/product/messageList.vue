<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入客户姓名" v-model="listQuery.name">
      </el-input>
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入电话号码" v-model="listQuery.phone">
      </el-input>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column align="center" type="index" label="序号">
      </el-table-column>

      <el-table-column align="center" label="单位名称" prop="org">
      </el-table-column>

      <el-table-column align="center" label="客户姓名" prop="name">
      </el-table-column>

      <el-table-column align="center" label="电话号码" prop="phone">
      </el-table-column>

      <el-table-column align="center" label="邮箱" prop="email">
      </el-table-column>

      <el-table-column align="center" label="留言内容" prop="content">
      </el-table-column>

      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
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
  </div>
</template>

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #20a0ff;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 120px;
    height: 120px;
    line-height: 120px;
    text-align: center;
  }
  .avatar {
    width: 120px;
    height: 120px;
    display: block;
  }
</style>

<script>
  import { listMessage, deleteMessage } from '@/api/zcMessage'
  import { getToken } from '@/utils/auth'

  export default {
    name: 'Category',
    computed: {
      headers() {
        return {
          'Admin-Token': getToken()
        }
      }
    },
    data() {
      return {
        list: undefined,
        total: undefined,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          id: undefined,
          name: undefined,
          sort: 'createtime',
          order: 'desc'
        },
        catL1: {},
        dataForm: {
          id: undefined,
          name: '',
          keyword: '',
          level: 'L2',
          pid: undefined,
          desc: '',
          iconUrl: undefined,
          picUrl: undefined
        },
        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: '编辑',
          create: '创建'
        },
        rules: {
          name: [{ required: true, message: '类目名不能为空', trigger: 'blur' }]
        },
        downloadLoading: false
      }
    },
    created() {
      this.getList()
    },
    methods: {
      getList() {
        this.listLoading = true
        listMessage(this.listQuery).then(response => {
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
      handleDelete(row) {
        deleteMessage(row).then(response => {
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
