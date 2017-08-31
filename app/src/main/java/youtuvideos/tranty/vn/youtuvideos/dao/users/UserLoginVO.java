package youtuvideos.tranty.vn.youtuvideos.dao.users;

import java.io.Serializable;

/**
 * Created by PC on 3/9/2017.
 */
public class UserLoginVO implements Serializable{
    private int UserID;

    private String FbID;
    private String GoogleID;
    private String TwitterID;
    private String fullName;
    private String LastName;
    private String Email;
    private String City;
    private String Mobile;
    private String Password;
    private String UserImageUrl;
    private String UserImageUri;
    private String State;


    private boolean isFbLogin;
    private boolean isAndroid;
    private boolean isGplusLogin;
    private boolean isTwittersLogin;
    private boolean isBasicLogin;
    private boolean isSocial;

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getFbID() {
        return FbID;
    }

    public void setFbID(String fbID) {
        FbID = fbID;
    }

    public String getGoogleID() {
        return GoogleID;
    }

    public void setGoogleID(String googleID) {
        GoogleID = googleID;
    }

    public String getTwitterID() {
        return TwitterID;
    }

    public void setTwitterID(String twitterID) {
        TwitterID = twitterID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserImageUrl() {
        return UserImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        UserImageUrl = userImageUrl;
    }

    public String getUserImageUri() {
        return UserImageUri;
    }

    public void setUserImageUri(String userImageUri) {
        UserImageUri = userImageUri;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public boolean isFbLogin() {
        return isFbLogin;
    }

    public void setFbLogin(boolean fbLogin) {
        isFbLogin = fbLogin;
    }

    public boolean isAndroid() {
        return isAndroid;
    }

    public void setAndroid(boolean android) {
        isAndroid = android;
    }

    public boolean isGplusLogin() {
        return isGplusLogin;
    }

    public void setGplusLogin(boolean gplusLogin) {
        isGplusLogin = gplusLogin;
    }

    public boolean isTwittersLogin() {
        return isTwittersLogin;
    }

    public void setTwittersLogin(boolean twittersLogin) {
        isTwittersLogin = twittersLogin;
    }

    public boolean isBasicLogin() {
        return isBasicLogin;
    }

    public void setBasicLogin(boolean basicLogin) {
        isBasicLogin = basicLogin;
    }

    public boolean isSocial() {
        return isSocial;
    }

    public void setSocial(boolean social) {
        isSocial = social;
    }
}
