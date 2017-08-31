package youtuvideos.tranty.vn.youtuvideos.requests;

import retrofit2.Call;
import retrofit2.Callback;
import youtuvideos.tranty.vn.youtuvideos.abtracts.AbstractResponse;
import youtuvideos.tranty.vn.youtuvideos.dao.comments.ResponseCommentVO;
import youtuvideos.tranty.vn.youtuvideos.dao.comments.ResponseCommentsVO;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.ResponseKnowledgeVO;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.ResponseKnowledgesVO;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.favorites.ResponseKnowledgesFavoritesVO;
import youtuvideos.tranty.vn.youtuvideos.dao.parrents.ResponseVO;
import youtuvideos.tranty.vn.youtuvideos.interfaces.Requests;

/**
 * Created by TRUC-SIDA on 3/28/2017.
 */

public class KnowledgesRequest {

    public static void gets(int language_id,int teacher_id, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseKnowledgesVO> call = client.getKnowledges(language_id,teacher_id);
        call.enqueue(new Callback<ResponseKnowledgesVO>() {
            @Override
            public void onResponse(Call<ResponseKnowledgesVO> call, retrofit2.Response<ResponseKnowledgesVO> response) {
                if(response.body().error_code == 0)
                    resp.onSuccess(response.body().error_code, response.body().message, response.body().data);
                else
                    resp.onFailure();
            }
            @Override
            public void onFailure(Call<ResponseKnowledgesVO> call, Throwable t) {
                resp.onFailure();

            }
        });
    }

    // get courses language
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

    public static void getKnowledgeComments(int knowledge_id, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseCommentsVO> call = client.getCommentsKnowledge(knowledge_id);
        call.enqueue(new Callback<ResponseCommentsVO>() {
            @Override
            public void onResponse(Call<ResponseCommentsVO> call, retrofit2.Response<ResponseCommentsVO> response) {
                if(response.body().error_code == 0 || response.body().error_code == 3)
                    resp.onSuccess(response.body().error_code, response.body().message, response.body().data);
                else
                    resp.onFailure();
            }
            @Override
            public void onFailure(Call<ResponseCommentsVO> call, Throwable t) {
                resp.onFailure();

            }
        });
    }

    public static void updateStatus(int knowledge_user_id, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseVO> call = client.updateStatus(knowledge_user_id);
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

    public static void deleteKnowledgeUser(int knowledge_user_id, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseVO> call = client.deleteKnowledgeUser(knowledge_user_id);
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

    public static void addKnowledgesComments(int knowledge_id,String user_id, String content, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseCommentVO> call = client.addCommentsKnowledge(knowledge_id,user_id,content);
        call.enqueue(new Callback<ResponseCommentVO>() {
            @Override
            public void onResponse(Call<ResponseCommentVO> call, retrofit2.Response<ResponseCommentVO> response) {
                if(response.body().error_code == 0)
                    resp.onSuccess(response.body().error_code, response.body().message, response.body().data);
                else
                    resp.onFailure();
            }
            @Override
            public void onFailure(Call<ResponseCommentVO> call, Throwable t) {
                resp.onFailure();

            }
        });
    }

    public static void getKnowledgeById(int knowledge_id, final AbstractResponse resp) {
        resp.onStart();
        Requests client = HandlerRequest.createService(Requests.class);
        Call<ResponseKnowledgeVO> call = client.getKnowledgesById(knowledge_id);
        call.enqueue(new Callback<ResponseKnowledgeVO>() {
            @Override
            public void onResponse(Call<ResponseKnowledgeVO> call, retrofit2.Response<ResponseKnowledgeVO> response) {
                if(response.body().error_code == 0)
                    resp.onSuccess(response.body().error_code, response.body().message, response.body().data);
                else
                    resp.onFailure();
            }
            @Override
            public void onFailure(Call<ResponseKnowledgeVO> call, Throwable t) {
                resp.onFailure();

            }
        });
    }



}
