# keycloak-social-gitee

* 安装步骤:
* 添加jar包到Keycloak服务:
  * `$ cp release/${version}/keycloak-social-gitee-${version}.jar _KEYCLOAK_HOME_/providers/`

* 添加模板文件到Keycloak服务:
  1. `$ cp templates/realm-identity-provider-gitee.html _KEYCLOAK_HOME_/themes/base/admin/resources/partials`
  1. `$ cp templates/realm-identity-provider-gitee-ext.html _KEYCLOAK_HOME_/themes/base/admin/resources/partials`

* 如果使用docker, 可使用
```
docker cp keycloak-social-gitee-${version}.jar ${keycloak-container-id}:/opt/jboss/keycloak/providers/
```
  
