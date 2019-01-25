package dpm.project.b.b_project.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    public static Retrofit client() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .header("User-Agent", "Your-App-Name")
                    .header("Accept", "application/vnd.yourapi.v1.full+json")
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        });

        return new Retrofit.Builder()
                .baseUrl("http://seong0428.iptime.org:11000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }


}
