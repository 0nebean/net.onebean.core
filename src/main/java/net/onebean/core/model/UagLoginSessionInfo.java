package net.onebean.core.model;


public class UagLoginSessionInfo {

    private String loginStatus;
    private String uagUserId;
    private String loginStatusComment;
    private String oauthBaseUrl;
    private String uagUserNickName;
    private String uagUsername;
    private String uagDeviceToken;
    private String uagAppId;
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getUagUserId() {
        return uagUserId;
    }

    public void setUagUserId(String uagUserId) {
        this.uagUserId = uagUserId;
    }

    public String getLoginStatusComment() {
        return loginStatusComment;
    }

    public void setLoginStatusComment(String loginStatusComment) {
        this.loginStatusComment = loginStatusComment;
    }

    public String getOauthBaseUrl() {
        return oauthBaseUrl;
    }

    public void setOauthBaseUrl(String oauthBaseUrl) {
        this.oauthBaseUrl = oauthBaseUrl;
    }

    public String getUagUserNickName() {
        return uagUserNickName;
    }

    public void setUagUserNickName(String uagUserNickName) {
        this.uagUserNickName = uagUserNickName;
    }

    public String getUagUsername() {
        return uagUsername;
    }

    public void setUagUsername(String uagUsername) {
        this.uagUsername = uagUsername;
    }

    public String getUagDeviceToken() {
        return uagDeviceToken;
    }

    public void setUagDeviceToken(String uagDeviceToken) {
        this.uagDeviceToken = uagDeviceToken;
    }

    public String getUagAppId() {
        return uagAppId;
    }

    public void setUagAppId(String uagAppId) {
        this.uagAppId = uagAppId;
    }
}
