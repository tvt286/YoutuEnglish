package youtuvideos.tranty.vn.youtuvideos.interfaces;

public interface Responses {
    public void onStart();
    public void onSuccess(int error_code, String message, Object obj);
    public void onFailure();
}
