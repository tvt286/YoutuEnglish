package youtuvideos.tranty.vn.youtuvideos.abtracts;


import youtuvideos.tranty.vn.youtuvideos.interfaces.Responses;

public abstract class AbstractResponse implements Responses {
    public void onStart(){
    }

    public void onSuccess(int error_code, String message, Object obj){
    }

    public void onFailure(){
    }
}
