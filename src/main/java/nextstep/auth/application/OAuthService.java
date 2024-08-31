package nextstep.auth.application;

import nextstep.auth.domain.OAuthProvider;
import nextstep.auth.domain.OAuthUser;

public interface OAuthService {
    OAuthUser loadUserProfile(String code);
    OAuthProvider getImplementsProvider();
}
