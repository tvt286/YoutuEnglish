package youtuvideos.tranty.vn.youtuvideos.requests;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import youtuvideos.tranty.vn.youtuvideos.mics.Constants;

public class HandlerRequest {
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES);
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(Constants.API.URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

}
