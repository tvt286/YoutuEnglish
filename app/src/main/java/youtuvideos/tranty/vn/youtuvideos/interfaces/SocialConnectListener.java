package youtuvideos.tranty.vn.youtuvideos.interfaces;

import youtuvideos.tranty.vn.youtuvideos.dao.users.UserLoginVO;

/**
 * Created by Wasim on 06-Dec-15.
 */
public interface SocialConnectListener {

	public void onUserConnected(int requestIdentifier, UserLoginVO userData);
	public void onConnectionError(int requestIdentifier, String message);
	public void onCancelled(int requestIdentifier, String message);
}
