<template>
  <div class="app-container calendar-list-container">
    <el-card class="box-card">
      <h3>产品信息</h3>
      <el-form :rules="rules" ref="zcProduct" :model="zcProduct" label-width="150px">
        <el-form-item label="物资编号" prop="productnum">
          <el-input v-model="zcProduct.productnum"></el-input>
        </el-form-item>
        <el-form-item label="产品名称" prop="productname">
          <el-input v-model="zcProduct.productname"></el-input>
        </el-form-item>
        <el-form-item label="产品类型">
          <el-select v-model="zcProduct.producttype">
            <el-option v-for="item in productTypeMap" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <div v-if="zcProduct.producttype == '01'">
          <el-form-item label="外径A/mm" prop="wj"><el-input v-model="zcProduct.wj"></el-input></el-form-item>
          <el-form-item label="长度B/mm" prop="cd"><el-input v-model="zcProduct.cd"></el-input></el-form-item>
          <el-form-item label="装配长度C/mm" prop="zpcd"><el-input v-model="zcProduct.zpcd"></el-input></el-form-item>
          <el-form-item label="径向刚度/kN/mm" prop="jxgd"><el-input v-model="zcProduct.jxgd"></el-input></el-form-item>
          <el-form-item label="轴向刚度/kN/mm" prop="zhouxgd"><el-input v-model="zcProduct.zhouxgd"></el-input></el-form-item>
        </div>
        <div v-if="zcProduct.producttype == '02'">
          <el-form-item label="外径A/mm" prop="wj"><el-input v-model="zcProduct.wj"></el-input></el-form-item>
          <el-form-item label="高度B/mm" prop="gd"><el-input v-model="zcProduct.gd"></el-input></el-form-item>
          <el-form-item label="受压荷载/kN" prop="syhz"><el-input v-model="zcProduct.syhz"></el-input></el-form-item>
          <el-form-item label="刚度/kN/mm" prop="gangd"><el-input v-model="zcProduct.gangd"></el-input></el-form-item>
        </div>
        <div v-if="zcProduct.producttype == '03'">
          <el-form-item label="自由高/mm" prop="zyg"><el-input v-model="zcProduct.zyg"></el-input></el-form-item>
          <el-form-item label="轴向刚度/kN/mm" prop="zhouxgd"><el-input v-model="zcProduct.zhouxgd"></el-input></el-form-item>
        </div>
        <div v-if="zcProduct.producttype == '04'">
          <el-form-item label="外径A/mm" prop="wj"><el-input v-model="zcProduct.wj"></el-input></el-form-item>
          <el-form-item label="标准高度/mm" prop="bzgd"><el-input v-model="zcProduct.bzgd"></el-input></el-form-item>
          <el-form-item label="空载/kN" prop="kz"><el-input v-model="zcProduct.kz"></el-input></el-form-item>
          <el-form-item label="超载/kN" prop="cz"><el-input v-model="zcProduct.cz"></el-input></el-form-item>
          <el-form-item label="重量/kg" prop="weight"><el-input v-model="zcProduct.weight"></el-input></el-form-item>
        </div>
        <div v-if="zcProduct.producttype == '05'">
          <el-form-item label="连杆加载点间距L1/mm" prop="lgjzdjj"><el-input v-model="zcProduct.lgjzdjj"></el-input></el-form-item>
          <el-form-item label="支撑座间距L2/mm" prop="zczjj"><el-input v-model="zcProduct.zczjj"></el-input></el-form-item>
          <el-form-item label="扭杆臂长度L3/mm" prop="ngbcd"><el-input v-model="zcProduct.ngbcd"></el-input></el-form-item>
          <el-form-item label="极限荷载/kN" prop="jxhz"><el-input v-model="zcProduct.jxhz"></el-input></el-form-item>
          <el-form-item label="疲劳荷载/kN" prop="plhz"><el-input v-model="zcProduct.plhz"></el-input></el-form-item>
          <el-form-item label="系统刚度/kN/mm" prop="xtgd"><el-input v-model="zcProduct.xtgd"></el-input></el-form-item>
          <el-form-item label="重量/kg" prop="weight"><el-input v-model="zcProduct.weight"></el-input></el-form-item>
        </div>
        <div v-if="zcProduct.producttype == '06'">
          <el-form-item label="连杆中心距L1/mm" prop="lgzxj"><el-input v-model="zcProduct.lgzxj"></el-input></el-form-item>
          <el-form-item label="杆体球头外径L2/mm" prop="gtqtwj"><el-input v-model="zcProduct.gtqtwj"></el-input></el-form-item>
          <el-form-item label="孔类型L3/mm" prop="klx"><el-input v-model="zcProduct.klx"></el-input></el-form-item>
          <el-form-item label="极限荷载/kN" prop="jxhz"><el-input v-model="zcProduct.jxhz"></el-input></el-form-item>
          <el-form-item label="压装力/kN" prop="yzl"><el-input v-model="zcProduct.yzl"></el-input></el-form-item>
          <el-form-item label="径向刚度/kN/mm" prop="jxgd"><el-input v-model="zcProduct.jxgd"></el-input></el-form-item>
          <el-form-item label="重量/kg" prop="weight"><el-input v-model="zcProduct.weight"></el-input></el-form-item>
        </div>
        <div v-if="zcProduct.producttype == '07'">
          <el-form-item label="长度L1/mm" prop="cd"><el-input v-model="zcProduct.cd"></el-input></el-form-item>
          <el-form-item label="宽度L2/mm" prop="kd"><el-input v-model="zcProduct.kd"></el-input></el-form-item>
          <el-form-item label="高度L3/mm" prop="gd"><el-input v-model="zcProduct.gd"></el-input></el-form-item>
          <el-form-item label="装配尺寸L4/mm" prop="zpcc"><el-input v-model="zcProduct.zpcc"></el-input></el-form-item>
          <el-form-item label="极限荷载/kN" prop="jxhz"><el-input v-model="zcProduct.jxhz"></el-input></el-form-item>
          <el-form-item label="纵向刚度/kN/mm" prop="zongxgd"><el-input v-model="zcProduct.zongxgd"></el-input></el-form-item>
          <el-form-item label="重量/kg" prop="weight"><el-input v-model="zcProduct.weight"></el-input></el-form-item>
        </div>
        <div v-if="zcProduct.producttype == '08'">
          <el-form-item label="外径A/mm" prop="wj"><el-input v-model="zcProduct.wj"></el-input></el-form-item>
          <el-form-item label="长度L1/mm" prop="cd"><el-input v-model="zcProduct.cd"></el-input></el-form-item>
          <el-form-item label="宽度L2/mm" prop="kd"><el-input v-model="zcProduct.kd"></el-input></el-form-item>
          <el-form-item label="高度L3/mm" prop="gd"><el-input v-model="zcProduct.gd"></el-input></el-form-item>
          <el-form-item label="垂向荷载/kN" prop="cxhz"><el-input v-model="zcProduct.cxhz"></el-input></el-form-item>
          <el-form-item label="垂向刚度/kN/mm" prop="cxgd"><el-input v-model="zcProduct.cxgd"></el-input></el-form-item>
        </div>
        <div v-if="zcProduct.producttype == '09'">
          <el-form-item label="长度L1/mm" prop="cd"><el-input v-model="zcProduct.cd"></el-input></el-form-item>
          <el-form-item label="宽度L2/mm" prop="kd"><el-input v-model="zcProduct.kd"></el-input></el-form-item>
          <el-form-item label="高度L3/mm" prop="gd"><el-input v-model="zcProduct.gd"></el-input></el-form-item>
          <el-form-item label="空载/kN" prop="kz"><el-input v-model="zcProduct.kz"></el-input></el-form-item>
          <el-form-item label="最大荷载/kN" prop="zdhz"><el-input v-model="zcProduct.zdhz"></el-input></el-form-item>
          <el-form-item label="垂向刚度/kN/mm" prop="cxgd"><el-input v-model="zcProduct.cxgd"></el-input></el-form-item>
          <el-form-item label="压缩高（AWO）/mm" prop="ysg"><el-input v-model="zcProduct.ysg"></el-input></el-form-item>
        </div>
        <div v-if="zcProduct.producttype == '10'">
          <el-form-item label="长度L1/mm" prop="cd"><el-input v-model="zcProduct.cd"></el-input></el-form-item>
          <el-form-item label="宽度L2/mm" prop="kd"><el-input v-model="zcProduct.kd"></el-input></el-form-item>
          <el-form-item label="高度L3/mm" prop="gd"><el-input v-model="zcProduct.gd"></el-input></el-form-item>
          <el-form-item label="V形角度/°" prop="vxjd"><el-input v-model="zcProduct.vxjd"></el-input></el-form-item>
          <el-form-item label="空载/kN" prop="kz"><el-input v-model="zcProduct.kz"></el-input></el-form-item>
          <el-form-item label="最大荷载/kN" prop="zdhz"><el-input v-model="zcProduct.zdhz"></el-input></el-form-item>
          <el-form-item label="垂向刚度/kN/mm" prop="cxgd"><el-input v-model="zcProduct.cxgd"></el-input></el-form-item>
          <el-form-item label="横向刚度/kN/mm" prop="hxgd"><el-input v-model="zcProduct.hxgd"></el-input></el-form-item>
          <el-form-item label="纵向刚度/kN/mm" prop="zongxgd"><el-input v-model="zcProduct.zongxgd"></el-input></el-form-item>
          <el-form-item label="压缩高（AWO）/mm" prop="ysg"><el-input v-model="zcProduct.ysg"></el-input></el-form-item>
          <el-form-item label="重量/kg" prop="weight"><el-input v-model="zcProduct.weight"></el-input></el-form-item>
        </div>
        <div v-if="zcProduct.producttype == '11'">
          <el-form-item label="长度L1/mm" prop="cd"><el-input v-model="zcProduct.cd"></el-input></el-form-item>
          <el-form-item label="宽度L2/mm" prop="kd"><el-input v-model="zcProduct.kd"></el-input></el-form-item>
          <el-form-item label="高度L3/mm" prop="gd"><el-input v-model="zcProduct.gd"></el-input></el-form-item>
        </div>

        <el-form-item label="实物图">
          <el-upload class="avatar-uploader" :action="uploadPath" list-type="picture-card" :show-file-list="false" :headers="headers"
                     accept=".jpg,.jpeg,.png,.gif" :on-success="uploadSnapshot">
            <img v-if="zcProduct.snapshot" :src="zcProduct.snapshot" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>

        <el-form-item label="剖面图">
          <el-upload class="avatar-uploader" :action="uploadPath" list-type="picture-card" :show-file-list="false" :headers="headers"
                     accept=".jpg,.jpeg,.png,.gif" :on-success="uploadRealPic">
            <img v-if="zcProduct.realpic" :src="zcProduct.realpic" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>

        <el-form-item label="结构形式" prop="structtype">
          <el-input v-model="zcProduct.structtype"></el-input>
        </el-form-item>

        <el-form-item label="产品应用平台">
          <el-cascader expand-trigger="hover" :options="platformMap" @change="handlePlatformChange"></el-cascader>
        </el-form-item>

        <el-form-item label="产品应用车型">
          <el-cascader expand-trigger="hover" :options="trainTypeList" @change="handleTrainTypeChange"></el-cascader>
        </el-form-item>

      </el-form>
    </el-card>

    <div class="op-container">
      <el-button @click="handleCancel">取消</el-button>
      <el-button v-if="isCreate == true" @click="handlePublish" type="primary">创建</el-button>
      <el-button v-else @click="handleUpdate" type="primary">保存</el-button>
    </div>

  </div>
</template>

<style>
  .el-card {
    margin-bottom: 10px;
  }

  .el-tag + .el-tag {
    margin-left: 10px;
  }

  .input-new-keyword {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }

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
  import { createProduct, detailProduct, editProduct } from '@/api/product'
  import { createStorage, uploadPath } from '@/api/storage'
  import { listCatL1, listCatL2 } from '@/api/zcCategory'
  import Editor from '@tinymce/tinymce-vue'
  import { MessageBox } from 'element-ui'
  import { getToken } from '@/utils/auth'

  export default {
    name: 'ProductCreate',
    components: { Editor },
    computed: {
      headers() {
        return {
          'Admin-Token': getToken()
        }
      }
    },

    data() {
      return {
        uploadPath,
        newKeywordVisible: false,
        newKeyword: '',
        keywords: [],
        trainTypeList: [],
        brandList: [],
        goods: { picUrl: '', gallery: [] },
        zcProduct: { snapshot: '', realpic: '' },
        isCreate: true,
        specVisiable: false,
        specForm: { specification: '', value: '', picUrl: '' },
        multipleSpec: false,
        specifications: [{ specification: '规格', value: '标准', picUrl: '' }],
        productVisiable: false,
        productForm: { id: 0, specifications: [], price: 0.00, number: 0, url: '' },
        products: [{ id: 0, specifications: ['标准'], price: 0.00, number: 0, url: '' }],
        attributeVisiable: false,
        attributeForm: { attribute: '', value: '' },
        attributes: [],
        productTypeMap: [
          { value: '01', label: '球铰关节' },
          { value: '02', label: '橡胶垫' },
          { value: '03', label: '止挡' },
          { value: '04', label: '空气弹簧' },
          { value: '05', label: '抗侧滚扭杆' },
          { value: '06', label: '连杆组件' },
          { value: '07', label: '牵引装置' },
          { value: '08', label: '橡胶堆' },
          { value: '09', label: '锥形簧' },
          { value: '10', label: 'V形簧' },
          { value: '11', label: '其他' }
        ],
        trainTypeMap: [],
        platformMap: [],
        rules: {
          productnum: [{ required: true, message: '请输入物资编号', trigger: 'blur' }],
          productname: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
          producttype: [{ required: true, message: '请选择产品类型', trigger: 'blur' }]
        },
        editorInit: {
          language: 'zh_CN',
          plugins: ['advlist anchor autolink autoresize autosave emoticons fullscreen hr image imagetools importcss insertdatetime legacyoutput link lists media nonbreaking noneditable pagebreak paste preview print save searchreplace tabfocus table template textcolor textpattern visualblocks visualchars wordcount'],
          toolbar: ['bold italic underline strikethrough alignleft aligncenter alignright outdent indent  blockquote undo redo removeformat subscript superscript ', 'hr bullist numlist link image charmap preview anchor pagebreak fullscreen media table emoticons forecolor backcolor'],
          images_upload_handler: function(blobInfo, success, failure) {
            const formData = new FormData()
            formData.append('file', blobInfo.blob())
            createStorage(formData).then(res => {
              success(res.data.data.url)
            }).catch(() => {
              failure('上传失败，请重新上传')
            })
          }
        }
      }
    },
    created() {
      this.init()
    },

    methods: {
      init: function() {
        this.getCatL1()
        this.getCatL2()
        if (this.$route.query.id != null) {
          const productId = this.$route.query.id
          detailProduct(productId).then(response => {
            this.zcProduct = response.data.data.product
            this.isCreate = false
          })
        }
      },
      getCatL1() {
        listCatL1().then(response => {
          this.platformMap = response.data.data
        })
      },
      getCatL2() {
        listCatL2().then(response => {
          this.trainTypeMap = response.data.data
        })
      },
      handlePlatformChange(value) {
        for (var i = 0; i < this.trainTypeMap.length; i++) {
          const trainType = this.trainTypeMap[i]
          if (trainType.value === value.toString()) {
            this.trainTypeList = trainType.list
            this.zcProduct.platform = trainType.code
            break
          }
        }
      },
      handleTrainTypeChange(value) {
        for (var i = 0; i < this.trainTypeList.length; i++) {
          const trainType = this.trainTypeList[i]
          if (trainType.value.toString() === value.toString()) {
            this.zcProduct.traintype = trainType.code
            break
          }
        }
      },
      handleCancel: function() {
        this.$router.push({ path: '/goods/goods' })
      },
      handlePublish: function() {
        createProduct(this.zcProduct).then(response => {
          this.$notify({
            title: '成功',
            message: '创建成功',
            type: 'success',
            duration: 2000
          })
          this.$router.push({ path: '/product/productList' })
        }).catch(response => {
          MessageBox.alert('业务错误：' + response.data.errmsg, '警告', {
            confirmButtonText: '确定',
            type: 'error'
          })
        })
      },
      handleUpdate: function() {
        editProduct(this.zcProduct).then(response => {
          this.$notify({
            title: '成功',
            message: '修改成功',
            type: 'success',
            duration: 2000
          })
          this.$router.push({ path: '/product/productList' })
        }).catch(response => {
          MessageBox.alert('业务错误：' + response.data.errmsg, '警告', {
            confirmButtonText: '确定',
            type: 'error'
          })
        })
      },
      handleClose(tag) {
        this.keywords.splice(this.keywords.indexOf(tag), 1)
        this.goods.keywords = this.keywords.toString()
      },
      showInput() {
        this.newKeywordVisible = true
        this.$nextTick(_ => {
          this.$refs.newKeywordInput.$refs.input.focus()
        })
      },
      handleInputConfirm() {
        const newKeyword = this.newKeyword
        if (newKeyword) {
          this.keywords.push(newKeyword)
          this.goods.keywords = this.keywords.toString()
        }
        this.newKeywordVisible = false
        this.newKeyword = ''
      },
      uploadSnapshot: function(response) {
        this.zcProduct.snapshot = response.data.url
      },
      uploadRealPic: function(response) {
        this.zcProduct.realpic = response.data.url
      },
      uploadOverrun: function() {
        this.$message({
          type: 'error',
          message: '上传文件个数超出限制!最多上传1张图片!'
        })
      },
      handleGalleryUrl(response, file, fileList) {
        if (response.errno === 0) {
          this.goods.gallery.push(response.data.url)
        }
      },
      handleRemove: function(file, fileList) {
        for (var i = 0; i < this.goods.gallery.length; i++) {
          // 这里存在两种情况
          // 1. 如果所删除图片是刚刚上传的图片，那么图片地址是file.response.data.url
          //    此时的file.url虽然存在，但是是本机地址，而不是远程地址。
          // 2. 如果所删除图片是后台返回的已有图片，那么图片地址是file.url
          var url
          if (file.response === undefined) {
            url = file.url
          } else {
            url = file.response.data.url
          }

          if (this.goods.gallery[i] === url) {
            this.goods.gallery.splice(i, 1)
          }
        }
      },
      specChanged: function(label) {
        if (label === false) {
          this.specifications = [{ specification: '规格', value: '标准', picUrl: '' }]
          this.products = [{ id: 0, specifications: ['标准'], price: 0.00, number: 0, url: '' }]
        } else {
          this.specifications = []
          this.products = []
        }
      },
      uploadSpecPicUrl: function(response) {
        this.specForm.picUrl = response.data.url
      },
      handleSpecificationShow() {
        this.specForm = { specification: '', value: '', picUrl: '' }
        this.specVisiable = true
      },
      handleSpecificationAdd() {
        var index = this.specifications.length - 1
        for (var i = 0; i < this.specifications.length; i++) {
          const v = this.specifications[i]
          if (v.specification === this.specForm.specification) {
            index = i
          }
        }

        this.specifications.splice(index + 1, 0, this.specForm)
        this.specVisiable = false

        this.specToProduct()
      },
      handleSpecificationDelete(row) {
        const index = this.specifications.indexOf(row)
        this.specifications.splice(index, 1)
        this.specToProduct()
      },
      specToProduct() {
        if (this.specifications.length === 0) {
          return
        }
        // 根据specifications创建临时规格列表
        var specValues = []
        var spec = this.specifications[0].specification
        var values = []
        values.push(0)

        for (var i = 1; i < this.specifications.length; i++) {
          const aspec = this.specifications[i].specification

          if (aspec === spec) {
            values.push(i)
          } else {
            specValues.push(values)
            spec = aspec
            values = []
            values.push(i)
          }
        }
        specValues.push(values)

        // 根据临时规格列表生产货品规格
        // 算法基于 https://blog.csdn.net/tyhj_sf/article/details/53893125
        var productsIndex = 0
        var products = []
        var combination = []
        var n = specValues.length
        for (var s = 0; s < n; s++) {
          combination[s] = 0
        }
        var index = 0
        var isContinue = false
        do {
          var specifications = []
          for (var x = 0; x < n; x++) {
            var z = specValues[x][combination[x]]
            specifications.push(this.specifications[z].value)
          }
          products[productsIndex] = { id: productsIndex, specifications: specifications, price: 0.00, number: 0, url: '' }
          productsIndex++

          index++
          combination[n - 1] = index
          for (var j = n - 1; j >= 0; j--) {
            if (combination[j] >= specValues[j].length) {
              combination[j] = 0
              index = 0
              if (j - 1 >= 0) {
                combination[j - 1] = combination[j - 1] + 1
              }
            }
          }
          isContinue = false
          for (var p = 0; p < n; p++) {
            if (combination[p] !== 0) {
              isContinue = true
            }
          }
        } while (isContinue)

        this.products = products
      },
      handleProductShow(row) {
        this.productForm = Object.assign({}, row)
        this.productVisiable = true
      },
      uploadProductUrl: function(response) {
        this.productForm.url = response.data.url
      },
      handleProductEdit() {
        for (var i = 0; i < this.products.length; i++) {
          const v = this.products[i]
          if (v.id === this.productForm.id) {
            this.products.splice(i, 1, this.productForm)
            break
          }
        }
        this.productVisiable = false
      },
      handleAttributeShow() {
        this.attributeForm = {}
        this.attributeVisiable = true
      },
      handleAttributeAdd() {
        this.attributes.unshift(this.attributeForm)
        this.attributeVisiable = false
      },
      handleAttributeDelete(row) {
        const index = this.attributes.indexOf(row)
        this.attributes.splice(index, 1)
      }
    }
  }
</script>
