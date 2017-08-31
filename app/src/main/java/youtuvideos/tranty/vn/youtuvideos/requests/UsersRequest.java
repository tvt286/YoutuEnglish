package youtuvideos.tranty.vn.youtuvideos.requests;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import youtuvideos.tranty.vn.youtuvideos.abtracts.AbstractResponse;
import youtuvideos.tranty.vn.youtuvideos.dao.courses.ResponseCoursesVO;
import youtuvideos.tranty.vn.youtuvideos.dao.parrents.ResponseVO;
import youtuvideos.tranty.vn.youtuvideos.dao.users.ResponseUserVO;
import youtuvideos.tranty.vn.youtuvideos.dao.users.knowledges.ResponseKnowledgesUserVO;
import youtuvideos.tranty.vn.youtuvideos.interfaces.Requests;

/**
 * Created by PC on 3/25/2017.
 */
public class UsersRequest {
    // login
    public static void Login(Map<String, String> data, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseVO> call = client.loginUser(data);
        call.enqueue(new Callback<ResponseVO>() {
            @Override
            public void onResponse(Call<ResponseVO> call, retrofit2.Response<ResponseVO> response) {
                if (response.body().error_code == 0 || response.body().error_code == 4)
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

    //   Logout
    public static void Logout(String user_id, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseVO> call = client.logoutUser(user_id);
        call.enqueue(new Callback<ResponseVO>() {
            @Override
            public void onResponse(Call<ResponseVO> call, retrofit2.Response<ResponseVO> response) {
                if (response.body().error_code == 0)
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

    public static void get(String user_id, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseUserVO> call = client.getUserInfo(user_id);
        call.enqueue(new Callback<ResponseUserVO>() {
            @Override
            public void onResponse(Call<ResponseUserVO> call, retrofit2.Response<ResponseUserVO> response) {
                    resp.onSuccess(response.body().error_code, response.body().message, response.body().data);
            }

            @Override
            public void onFailure(Call<ResponseUserVO> call, Throwable t) {
                resp.onFailure();
            }
        });
    }

    public static void getKnowledges(int knowledge_user_id, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseKnowledgesUserVO> call = client.getKnowledgesUser(knowledge_user_id);
        call.enqueue(new Callback<ResponseKnowledgesUserVO>() {
            @Override
            public void onResponse(Call<ResponseKnowledgesUserVO> call, retrofit2.Response<ResponseKnowledgesUserVO> response) {
                if (response.body().error_code == 0)
                    resp.onSuccess(response.body().error_code, response.body().message, response.body().data);
                else
                    resp.onFailure();
            }

            @Override
            public void onFailure(Call<ResponseKnowledgesUserVO> call, Throwable t) {
                resp.onFailure();
            }
        });
    }

    // register
    public static void registerKnowledge(Map<String, String> data, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseVO> call = client.registerKnowledge(data);
        call.enqueue(new Callback<ResponseVO>() {
            @Override
            public void onResponse(Call<ResponseVO> call, retrofit2.Response<ResponseVO> response) {
                if (response.body().error_code == 0)
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

    // update
    public static void updateKnowledge(Map<String, String> data, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseVO> call = client.updateKnowledge(data);
        call.enqueue(new Callback<ResponseVO>() {
            @Override
            public void onResponse(Call<ResponseVO> call, retrofit2.Response<ResponseVO> response) {
                if (response.body().error_code == 0)
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

    public static void updateLanguage(String user_id, int language_id, int teacher_id, final AbstractResponse resp ) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseVO> call = client.updateLanguageUser(user_id, language_id, teacher_id);
        call.enqueue(new Callback<ResponseVO>() {
            @Override
            public void onResponse(Call<ResponseVO> call, retrofit2.Response<ResponseVO> response) {
                if (response.body().error_code == 0)
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

    public static void updateRegId(String user_id, String reg_id, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseVO> call = client.updateRegId(user_id, reg_id);
        call.enqueue(new Callback<ResponseVO>() {
            @Override
            public void onResponse(Call<ResponseVO> call, retrofit2.Response<ResponseVO> response) {
                if (response.body().error_code == 0)
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


}
