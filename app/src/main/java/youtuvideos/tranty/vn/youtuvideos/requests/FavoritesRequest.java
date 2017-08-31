package youtuvideos.tranty.vn.youtuvideos.requests;

import retrofit2.Call;
import retrofit2.Callback;
import youtuvideos.tranty.vn.youtuvideos.abtracts.AbstractResponse;
import youtuvideos.tranty.vn.youtuvideos.dao.comments.ResponseCommentsVO;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.favorites.ResponseKnowledgesFavoritesVO;
import youtuvideos.tranty.vn.youtuvideos.dao.parrents.ResponseVO;
import youtuvideos.tranty.vn.youtuvideos.interfaces.Requests;

/**
 * Created by TRUC-SIDA on 3/27/2017.
 */

public class FavoritesRequest {

    public static void addKnowledgesFavorites(String user_id, int knowledge_id, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseVO> call = client.addKnowledgesFavorites(user_id,knowledge_id);
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


    public static void removeKnowledgesFavorites(String user_id, int knowledge_id, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseVO> call = client.removeKnowledgesFavorites(user_id,knowledge_id);
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

    public static void getKnowledgesFavorites(String user_id, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseKnowledgesFavoritesVO> call = client.getKnowledgesFavorites(user_id);
        call.enqueue(new Callback<ResponseKnowledgesFavoritesVO>() {
            @Override
            public void onResponse(Call<ResponseKnowledgesFavoritesVO> call, retrofit2.Response<ResponseKnowledgesFavoritesVO> response) {
                    resp.onSuccess(response.body().error_code, response.body().message, response.body().data);
            }
            @Override
            public void onFailure(Call<ResponseKnowledgesFavoritesVO> call, Throwable t) {
                resp.onFailure();

            }
        });
    }
}
