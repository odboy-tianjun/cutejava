/**
 * 在modules中定义后，需要到这注册
 * @type {any}
 */
const getters = {
  deployUploadApi: state => state.api.deployUploadApi,
  databaseUploadApi: state => state.api.databaseUploadApi,
  size: state => state.app.size,
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  roles: state => state.user.roles,
  user: state => state.user.user,
  loadMenus: state => state.user.loadMenus,
  addRouters: state => state.permission.addRouters,
  imagesUploadApi: state => state.api.imagesUploadApi,
  baseApi: state => state.api.baseApi,
  fileUploadApi: state => state.api.fileUploadApi,
  ossUploadApi: state => state.api.ossUploadApi,
  updateAvatarApi: state => state.api.updateAvatarApi,
  druidSqlApi: state => state.api.druidSqlApi,
  swaggerApi: state => state.api.swaggerApi,
  websocketApi: state => state.api.websocketApi,
  sidebarRouters: state => state.permission.sidebarRouters,
  statusConst: state => state.pipeline.statusConst
}
export default getters
