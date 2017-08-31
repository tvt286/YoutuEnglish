package youtuvideos.tranty.vn.youtuvideos.requests;

import retrofit2.Call;
import retrofit2.Callback;
import youtuvideos.tranty.vn.youtuvideos.abtracts.AbstractResponse;
import youtuvideos.tranty.vn.youtuvideos.dao.modules.ResponseModuleVO;
import youtuvideos.tranty.vn.youtuvideos.dao.modules.logs.ResponseModuleLogVO;
import youtuvideos.tranty.vn.youtuvideos.dao.parrents.ResponseVO;
import youtuvideos.tranty.vn.youtuvideos.interfaces.Requests;

/**
 * Created by PC on 3/29/2017.
 */
public class ModulesRequest {
    public static void gets(int knowledge_id, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseModuleVO> call = client.getModules(knowledge_id);
        call.enqueue(new Callback<ResponseModuleVO>() {
            @Override
            public void onResponse(Call<ResponseModuleVO> call, retrofit2.Response<ResponseModuleVO> response) {
                if (response.body().error_code == 0)
                    resp.onSuccess(response.body().error_code, response.body().message, response.body().data);
                else
                    resp.onFailure();
            }
            @Override
            public void onFailure(Call<ResponseModuleVO> call, Throwable t) {
                resp.onFailure();

            }
        });
    }

    public static void completeModuleUser(int knowledge_user_id, int module_id, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseVO> call = client.completeModuleUser(knowledge_user_id,module_id);
        call.enqueue(new Callback<ResponseVO>() {
            @Override
            public void onResponse(Call<ResponseVO> call, retrofit2.Response<ResponseVO> response) {
                if(response.body().error_code == 0)
                    resp.onSuccess(response.body().error_code, response.body().message, response.body());
                else
                    resp.onFailure();
            }
            @Override
            public void onFailure(Call<ResponseVO> call, Throwable t) {
                resp.onFailure();

            }
        });
    }

    public static void getModulesLogs(int knowledge_user_id, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseModuleLogVO> call = client.getModulesLogs(knowledge_user_id);
        call.enqueue(new Callback<ResponseModuleLogVO>() {
            @Override
            public void onResponse(Call<ResponseModuleLogVO> call, retrofit2.Response<ResponseModuleLogVO> response) {
                resp.onSuccess(response.body().error_code, response.body().message, response.body().data);
            }
            @Override
            public void onFailure(Call<ResponseModuleLogVO> call, Throwable t) {
                resp.onFailure();
            }
        });
    }
}
