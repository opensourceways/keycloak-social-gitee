package cn.osinfra.keycloak.social.gitee;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.keycloak.broker.oidc.AbstractOAuth2IdentityProvider;
import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;
import org.keycloak.broker.oidc.mappers.AbstractJsonUserAttributeMapper;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityBrokerException;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.broker.social.SocialIdentityProvider;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.KeycloakSession;

import java.util.Iterator;

/**
 * @author <a href="mailto:tommylikehu@gmail.com">TommyLike</a>
 */

public class GiteeIdentityProvider extends AbstractOAuth2IdentityProvider<OAuth2IdentityProviderConfig>  implements SocialIdentityProvider<OAuth2IdentityProviderConfig> {
    public static final String AUTH_URL = "https://gitee.com/oauth/authorize";
    public static final String TOKEN_URL = "https://gitee.com/oauth/token";
    public static final String PROFILE_URL = "https://gitee.com/api/v5/user";
    public static final String EMAIL_URL = "https://gitee.com/api/v5/emails";
    public static final String DEFAULT_SCOPE = "user_info emails";
    public static final String PRIMARY_EMAIL_SCOPE = "primary";

    public GiteeIdentityProvider(KeycloakSession session, OAuth2IdentityProviderConfig config) {
        super(session, config);
        config.setAuthorizationUrl(AUTH_URL);
        config.setTokenUrl(TOKEN_URL);
        config.setUserInfoUrl(PROFILE_URL);
    }

    @Override
    protected boolean supportsExternalExchange() {
        return true;
    }

    @Override
    protected String getProfileEndpointForValidation(EventBuilder event) {
        return PROFILE_URL;
    }

    @Override
    protected BrokeredIdentityContext extractIdentityFromProfile(EventBuilder event, JsonNode profile) {
        BrokeredIdentityContext user = new BrokeredIdentityContext(getJsonProperty(profile, "id"));

        String username = getJsonProperty(profile, "login");
        user.setUsername(username);
        user.setName(getJsonProperty(profile, "name"));
        user.setEmail(getJsonProperty(profile, "email"));
        user.setIdpConfig(getConfig());
        user.setIdp(this);
        AbstractJsonUserAttributeMapper.storeUserProfileForMapper(user, profile, getConfig().getAlias());

        return user;

    }


    @Override
    protected BrokeredIdentityContext doGetFederatedIdentity(String accessToken) {
        try {
            JsonNode profile = SimpleHttp.doGet(PROFILE_URL, session).header("Authorization", "Bearer " + accessToken).asJson();

            BrokeredIdentityContext user = extractIdentityFromProfile(null, profile);

            if (user.getEmail() == null) {
                user.setEmail(searchEmail(accessToken));
            }

            return user;
        } catch (Exception e) {
            throw new IdentityBrokerException("Could not obtain user profile from gitee. error:" + e.getMessage(), e);
        }
    }

    private String searchEmail(String accessToken) {

        try {
            ArrayNode emails = (ArrayNode) SimpleHttp.doGet(EMAIL_URL, session).header("Authorization", "Bearer " + accessToken).asJson();
            Iterator<JsonNode> loop = emails.elements();
            while (loop.hasNext()) {
                JsonNode mail = loop.next();
                JsonNode scopes = mail.get("scope");
                if (scopes != null && scopes.isArray()) {
                    for (JsonNode s : scopes) {
                        if (s.asText().equals(PRIMARY_EMAIL_SCOPE)) {
                            return getJsonProperty(mail, "email");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new IdentityBrokerException("Could not obtain user email from gitee. error:" + e.getMessage(), e);
        }
        throw new IdentityBrokerException("Primary email from gitee is not found.");
    }

    @Override
    protected String getDefaultScopes() {
        return DEFAULT_SCOPE;
    }
}
