package youtuvideos.tranty.vn.youtuvideos.services;

/**
 * Created by TRUC-SIDA on 2/27/2017.
 */

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import youtuvideos.tranty.vn.youtuvideos.abtracts.AbstractResponse;
import youtuvideos.tranty.vn.youtuvideos.dao.users.UserLoginVO;
import youtuvideos.tranty.vn.youtuvideos.preferences.UserShared;
import youtuvideos.tranty.vn.youtuvideos.requests.UsersRequest;

/**
 * Created by cafe on 11/08/2016.
 */

public class MyFirebaseIDService extends FirebaseInstanceIdService {
    private UserLoginVO userLoginVO;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token= FirebaseInstanceId.getInstance().getToken();
        saveToken(token);
    }

    private void saveToken(String token) {
        boolean isLogin = UserShared.ins(this).getIsLogin();
        if (isLogin) {
            userLoginVO = UserShared.ins(this).getUserLoginVO();

            UsersRequest.updateRegId(userLoginVO.getGoogleID(), token, new AbstractResponse() {
                @Override
                public void onSuccess(int error_code, String message, Object obj) {
                    super.onSuccess(error_code, message, obj);
                }
            });
        }

    }
}
