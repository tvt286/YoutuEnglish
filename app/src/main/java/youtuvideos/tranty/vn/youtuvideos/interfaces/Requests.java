package youtuvideos.tranty.vn.youtuvideos.interfaces;


import java.util.Map;

import retrofit2.*;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import youtuvideos.tranty.vn.youtuvideos.dao.comments.ResponseCommentVO;
import youtuvideos.tranty.vn.youtuvideos.dao.comments.ResponseCommentsVO;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.ResponseKnowledgeVO;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.ResponseKnowledgesVO;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.favorites.ResponseKnowledgesFavoritesVO;
import youtuvideos.tranty.vn.youtuvideos.dao.modules.ResponseModuleVO;
import youtuvideos.tranty.vn.youtuvideos.dao.modules.logs.ResponseModuleLogVO;
import youtuvideos.tranty.vn.youtuvideos.dao.parrents.ResponseVO;
import youtuvideos.tranty.vn.youtuvideos.dao.users.ResponseUserVO;
import youtuvideos.tranty.vn.youtuvideos.dao.users.knowledges.ResponseKnowledgesUserVO;

public interface Requests {

    // lay danh sách khóa học theo ngôn ngữ
    @FormUrlEncoded
    @POST("knowledges")
    Call<ResponseKnowledgesVO> getKnowledges(@Field("language_id") int language_id, @Field("teacher_id") int teacher_id);

    // Thong tin khóa học theo id
    @GET("knowledge/{knowledge_id}")
    Call<ResponseKnowledgeVO> getKnowledgesById(@Path("knowledge_id") int knowledge_id);

    // danh sách môn học theo khoa hoc id
    @GET("modules/{knowledge_id}")
    Call<ResponseModuleVO> getModules(@Path("knowledge_id") int knowledge_id);

    // Login User
    @FormUrlEncoded
    @POST("user/login")
    Call<ResponseVO> loginUser(@FieldMap Map<String, String> fields);

    // Logout User
    @FormUrlEncoded
    @POST("user/logout")
    Call<ResponseVO> logoutUser(@Field("user_id") String user_id);

    // Register knowledge
    @FormUrlEncoded
    @POST("user/knowledge/register")
    Call<ResponseVO> registerKnowledge(@FieldMap Map<String, String> fields);

    // Register knowledge
    @FormUrlEncoded
    @POST("user/update/setting")
    Call<ResponseVO> updateKnowledge(@FieldMap Map<String, String> fields);

    // thông tin của user
    @GET("user/{user_id}")
    Call<ResponseUserVO> getUserInfo(@Path("user_id") String user_id);

    // danh sách khóa học user
    @GET("user/knowledges/{knowledge_user_id}")
    Call<ResponseKnowledgesUserVO> getKnowledgesUser(@Path("knowledge_user_id") int knowledge_user_id);

    // get danh sach knowledges favorites cua user
    @GET("user/knowledges/favorites/{user_id}")
    Call<ResponseKnowledgesFavoritesVO> getKnowledgesFavorites(@Path("user_id") String user_id);

    // add knowledge favorites cua user
    @FormUrlEncoded
    @POST("user/knowledges/favorites")
    Call<ResponseVO> addKnowledgesFavorites(@Field("user_id") String user_id, @Field("knowledge_id") int knowledge_id);

    // remove knowledge favorites cua user
    @FormUrlEncoded
    @POST("user/knowledges/favorites/remove")
    Call<ResponseVO> removeKnowledgesFavorites(@Field("user_id") String user_id, @Field("knowledge_id") int knowledge_id);

    //add comments courses
    @FormUrlEncoded
    @POST("knowledge/comments")
    Call<ResponseCommentVO> addCommentsKnowledge(@Field("knowledge_id") int knowledge_id, @Field("user_id") String user_id, @Field("content") String content);

    // get danh sach comments knowledge
    @GET("knowledge/comments/{knowledge_id}")
    Call<ResponseCommentsVO> getCommentsKnowledge(@Path("knowledge_id") int course_id);

    //update language
    @FormUrlEncoded
    @POST("user/update/language")
    Call<ResponseVO> updateLanguageUser(@Field("user_id") String user_id, @Field("language_id") int language_id, @Field("teacher_id") int teacher_id);

    //update status knowledge user
    @FormUrlEncoded
    @POST("user/knowledges/status")
    Call<ResponseVO> updateStatus(@Field("knowledge_user_id") int knowledge_user_id);

    //xoa khoa hoc của user
    @FormUrlEncoded
    @POST("user/knowledges/delete")
    Call<ResponseVO> deleteKnowledgeUser(@Field("knowledge_user_id") int knowledge_user_id);

    //set complete  knowledge user
    @FormUrlEncoded
    @POST("user/knowledges/module_completed")
    Call<ResponseVO> completeModuleUser(@Field("knowledge_user_id") int knowledge_user_id, @Field("module_id") int module_id);

    // danh sách module logs
    @GET("user/knowledges/modules_logs/{knowledge_user_id}")
    Call<ResponseModuleLogVO> getModulesLogs(@Path("knowledge_user_id") int knowledge_user_id);

    //cập nhật lại reg_id của user
    @FormUrlEncoded
    @POST("user/reg_id")
    Call<ResponseVO> updateRegId(@Field("user_id") String user_id, @Field("reg_id") String reg_id);

    //kiem tra xem user da dang ky khoa hoc do chua
    @FormUrlEncoded
    @POST("user/hasregister")
    Call<ResponseVO> checkUserRegisterKnowledge(@Field("user_id") String user_id, @Field("knowledge_id") int knowledge_id);
}
