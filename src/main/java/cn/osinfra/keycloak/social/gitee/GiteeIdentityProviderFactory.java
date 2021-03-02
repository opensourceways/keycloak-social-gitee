package cn.osinfra.keycloak.social.gitee;

import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;
import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.broker.social.SocialIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;

/**
 * @author jacky.yong
 */
public class GiteeIdentityProviderFactory extends AbstractIdentityProviderFactory<GiteeIdentityProvider>
        implements SocialIdentityProviderFactory<GiteeIdentityProvider> {

    public static final String PROVIDER_ID = "gitee";

    @Override
    public String getName() {
        return "Gitee";
    }

    @Override
    public GiteeIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
        return new GiteeIdentityProvider(session, new OAuth2IdentityProviderConfig(model));
    }

    @Override
    public OAuth2IdentityProviderConfig createConfig() {
        return new OAuth2IdentityProviderConfig();
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
