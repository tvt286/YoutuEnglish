package youtuvideos.tranty.vn.youtuvideos.preferences;

import android.content.Context;

import youtuvideos.tranty.vn.youtuvideos.dao.users.UserLoginVO;

public class UserShared extends SharedPre{
    private static UserShared instance = null;
    private UserLoginVO userLoginVO;
    private boolean isLogin;

    private UserShared(Context context) {
        super(context, "user");
        userLoginVO = new UserLoginVO();
        restoringPreferences();
    }

    public static UserShared ins(Context context) {
        if (instance == null) {
            instance = new UserShared(context);
        }
        return instance;
    }

    public void restoringPreferences() {
        editor = pre.edit();
        userLoginVO.setGoogleID(getId());
        userLoginVO.setEmail(getEmail());
        userLoginVO.setFullName(getName());
        userLoginVO.setUserImageUri(getImage());
        isLogin = getIsLogin();
    }


    public UserLoginVO getUserLoginVO(){
        return userLoginVO;
    }

    public void setUser(UserLoginVO userLoginVO) {
        editor = pre.edit();
        editor.putString("id", userLoginVO.getGoogleID());
        editor.putString("name", userLoginVO.getFullName());
        editor.putString("email", userLoginVO.getEmail());
        editor.putString("image", userLoginVO.getUserImageUri());
        editor.commit();
        this.userLoginVO = userLoginVO;
    }

    public void setIsLogin(boolean islogin)
    {
        editor = pre.edit();
        editor.putBoolean("isLogin", islogin);
        editor.commit();
        this.isLogin = isLogin;
    }


    public String getId() {
        return pre.getString("id", "");
    }
    public String getEmail() {
        return pre.getString("email", "");
    }
    public String getImage() {
        return pre.getString("image", "");
    }
    public String getName() {
        return pre.getString("name", "");
    }
    public boolean getIsLogin() {
        return pre.getBoolean("isLogin", false);
    }

}
