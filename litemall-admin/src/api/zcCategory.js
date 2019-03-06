import request from '@/utils/request'

export function listCategory(query) {
  return request({
    url: '/zcCategory/list',
    method: 'get',
    params: query
  })
}

export function createCategory(data) {
  return request({
    url: '/zcCategory/create',
    method: 'post',
    data
  })
}

export function listCatL1() {
  return request({
    url: '/zcCategory/l1',
    method: 'get'
  })
}

export function listCatL2() {
  return request({
    url: '/zcCategory/l2',
    method: 'get'
  })
}

export function listProductTypes() {
  return request({
    url: '/product/getProductTypes',
    method: 'get'
  })
}

export function deleteCategory(data) {
  return request({
    url: '/zcCategory/delete',
    method: 'post',
    data
  })
}

export function updateCategory(data) {
  return request({
    url: '/zcCategory/update',
    method: 'post',
    data
  })
}
