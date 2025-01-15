package org.haruni.domain.oauth.domain.google.entity;

import org.haruni.domain.oauth.common.entity.OAuth2UserInfo;
import org.haruni.domain.oauth.common.utils.OAuth2Provider;

import java.util.Map;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    /**
     * CONCEPT : Google 에서 받아온 정보를 담아 놓을 객체
     * Google 에서는 Json 형식으로 데이터를 넘겨줌
     * 해당 정보를 담아 접근에 용이하게 할 수 있는 객체 생성
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

    public GoogleOAuth2UserInfo(Map<String, Object> attributes, String accessToken){
        this.attributes = attributes;
        this.accessToken = accessToken;
        this.id = (String) attributes.get("sub");
        this.email = (String) attributes.get("email");
        this.name = (String) attributes.get("name");
        this.firstName = (String) attributes.get("given_name");
        this.lastName = (String) attributes.get("family_name");
        this.nickName = null;
        this.profileImageUrl = (String) attributes.get("picture");
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.GOOGLE;
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
