package org.haruni.domain.oauth.common.entity;

import org.haruni.domain.oauth.common.utils.OAuth2Provider;

import java.util.Map;

public interface OAuth2UserInfo {

    OAuth2Provider getProvider();

    String getAccessToken();

    Map<String, Object> getAttributes();

    String getId();

    String getName();

    String getEmail();

    String getFirstName();

    String getLastName();

    String getNickname();

    String getProfileImageUrl();
}
