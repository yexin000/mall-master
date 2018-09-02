import request from '@/utils/request'

export function listProducts(query) {
  return request({
    url: '/product/list',
    method: 'get',
    params: query
  })
}

export function createProduct(data) {
  return request({
    url: '/product/create',
    method: 'post',
    data
  })
}

export function detailProduct(id) {
  return request({
    url: '/product/detail',
    method: 'get',
    params: { id }
  })
}

export function editProduct(data) {
  return request({
    url: '/product/update',
    method: 'post',
    data
  })
}

export function deleteProduct(data) {
  return request({
    url: '/product/delete',
    method: 'post',
    data
  })
}

const importExcel = process.env.BASE_API + '/product/import'
export { importExcel }
