FROM quay.io/keycloak/keycloak:12.0.1
MAINTAINER tommylikehu<tommylikehu@gmail.com>

RUN mkdir -p /opt/jboss/keycloak/providers
COPY ./target/keycloak-social-wechat-v12.0.1.jar /opt/jboss/keycloak/providers
COPY templates/realm-identity-provider-gitee.html /opt/jboss/keycloak/themes/base/admin/resources/partials

ENTRYPOINT [ "/opt/jboss/tools/docker-entrypoint.sh" ]
CMD ["-b", "0.0.0.0"]
