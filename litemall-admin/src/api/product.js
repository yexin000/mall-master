import request from '@/utils/request'

export function listProducts(query) {
  return request({
    url: '/product/list',
    method: 'get',
    params: query
  })
}
