package org.haruni.domain.oauth.domain.naver.entity;

import org.haruni.domain.oauth.common.entity.OAuth2UserInfo;
import org.haruni.domain.oauth.common.utils.OAuth2Provider;

import java.util.Map;

public class NaverOAuth2UserInfo implements OAuth2UserInfo {
    /**
     * CONCEPT : Naver 에서 받아온 정보를 담아 놓을 객체
     */

    private final Map<String, Object> attributes;

    private final String accessToken;
    private final String id;
    private final String email;
    private final String name;
    private final String firstName;
    private final String lastName;
    private final String nickName;
    private final String profileImageUrl;

    public NaverOAuth2UserInfo(Map<String, Object> attributes, String accessToken){
        this.attributes = (Map<String, Object>) attributes.get("response");
        this.accessToken = accessToken;

        this.id = (String) this.attributes.get("id");
        this.email = (String) this.attributes.get("email");
        this.name = (String) this.attributes.get("name");
        this.nickName = (String) attributes.get("nickname");
        this.profileImageUrl = (String) attributes.get("profile_image");

        this.firstName = null;
        this.lastName = null;
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.NAVER;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getNickname() {
        return nickName;
    }

    @Override
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
