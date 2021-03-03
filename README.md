# Gitee Identity Provider for Keycloak
Keycloak Social Login extension for [Gitee](https://gitee.com/).

# Build
```
mvn clean package
```
# Install
Download jar file from github release or build it from source and copy it to Keycloak home folder. as below.
```
cp ./target/keycloak-social-gitee-1.0.0.jar <keycloak-home>/providers
cp templates/realm-identity-provider-gitee.html <keycloak-home>/themes/base/admin/resources/partials
```
alternative, you can overwrite the default keycloak image with `Dockerfile.cp_relase` file.

# Configure
The default request scopes of this plugin are `userinfo` and `emails`, you can change scope on Gitee IDP's configuration
page, for more configure detail, please refer to [Gitee document](https://gitee.com/api/v5/oauth_doc#/)
