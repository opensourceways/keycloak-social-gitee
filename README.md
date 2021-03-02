# keycloak-social-gitee

# Build
```
mvn clean install
```
# Install
```
cp ./target/keycloak-social-gitee-1.0.0.jar <keycloak-home>/providers
cp templates/realm-identity-provider-gitee.html <keycloak-home>/themes/base/admin/resources/partials
```
