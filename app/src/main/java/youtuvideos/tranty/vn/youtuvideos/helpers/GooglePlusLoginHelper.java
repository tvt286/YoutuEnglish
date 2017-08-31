package youtuvideos.tranty.vn.youtuvideos.helpers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;

import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.activities.MainActivity;
import youtuvideos.tranty.vn.youtuvideos.dao.users.UserLoginVO;
import youtuvideos.tranty.vn.youtuvideos.interfaces.SocialConnectListener;
import youtuvideos.tranty.vn.youtuvideos.mics.ShowToast;
import youtuvideos.tranty.vn.youtuvideos.mics.Util;


/**
 * Created by Wasim on 06-Dec-15.
 */
public class GooglePlusLoginHelper implements GoogleApiClient.OnConnectionFailedListener {


	private AppCompatActivity activity;
	private UserLoginVO userData;

	//private Person person;
	private SocialConnectListener userCallbackListener;

	/* Client for accessing Google APIs */
	private static GoogleApiClient mGoogleApiClient;

	private int requestIdentifier = 0;

	private static final String TAG = "GooglePlusLoginHelper";

	public void createConnection(AppCompatActivity mActivity)
	{

		this.activity = mActivity;
		userData = new UserLoginVO();

		if (Util.checkPlayServices(mActivity)) {

			GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
					.requestScopes(new Scope(Scopes.PROFILE))
					.requestScopes(new Scope(Scopes.PLUS_LOGIN))
					.requestProfile()
					.requestEmail()
					.build();

			if (mGoogleApiClient == null) {
				// [START create_google_api_client]
				// Build GoogleApiClient with access to basic profile
				mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
						.enableAutoManage(mActivity,this)
						.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
						//.addApi(Plus.API)
						.build();
			}
		}
	}

	public void onActivityResult(int resultCode,Intent data)
	{
		GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
		handleSignInResult(result);
	}

	// [START signIn]
	public void signIn(int identifier) {
		requestIdentifier = identifier;
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		activity.startActivityForResult(signInIntent, requestIdentifier);
	}
	// [END signIn]


	public void signOut() {
		if (mGoogleApiClient != null) {

			if(!mGoogleApiClient.isConnecting()){
				mGoogleApiClient.connect();
			}
			mGoogleApiClient.disconnect();
		}
		/**/
	}

	public void setUserCallbackListener(SocialConnectListener userCallbackListener) {
		this.userCallbackListener = userCallbackListener;
	}


	public void handleSignInResult(GoogleSignInResult result) {

		Log.d(TAG, "handleSignInResult:" + result.isSuccess());
		if (result.isSuccess()) {
			// Signed in successfully, show authenticated UI.
			GoogleSignInAccount acct = result.getSignInAccount();

			if (acct != null) {
				userData.setFullName(acct.getDisplayName());

				userData.setGoogleID(acct.getId());
				userData.setEmail(acct.getEmail());
				if (acct.getPhotoUrl() != null) {
					userData.setUserImageUri(acct.getPhotoUrl().toString());
				}
				userData.setGplusLogin(true);
				userData.setSocial(true);

				if (userCallbackListener != null) {
					userCallbackListener.onUserConnected(requestIdentifier,userData);
				}
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.d(TAG, "onConnectionFailed:" + connectionResult);
		if (userCallbackListener != null)
		{
			userCallbackListener.onConnectionError(requestIdentifier,connectionResult.getErrorMessage());
		}
	}
}
