package uk.co.tezk.mymarvelapplication.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.tezk.mymarvelapplication.model.Constant;

/**
 * Created by Terry Kay
 *
 * provides Retrofit instance to interact with API
 */

public class MarvelRetrofit {
    public IMarvelApi getRetrofit() {
        /**
         *  get the API, built from the components
         */
        HttpLoggingInterceptor httpLoggingInterceptor = provideInterceptor();
        OkHttpClient okHttpClient = provideOkHttpclient(httpLoggingInterceptor);
        Retrofit retrofit = provideRetrofit(okHttpClient);

        return retrofit.create(IMarvelApi.class);
    }

    public HttpLoggingInterceptor provideInterceptor() {
        /**
            Create an HttpLoggingInterceptor
         @return an interceptor that provides logging of the HTTP requests
         */
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public OkHttpClient provideOkHttpclient(HttpLoggingInterceptor interceptor) {
        /**
         * Create an OkHttpClient
         * @param interceptor Logging interceptor
         * @return an OkHttpInstance
         *
         */
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        return okHttpClient;
    }

    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        /**
         * Get an instance of Retrofit to build the API request
         * @param okHttpClient the OkHttpClient to use
         * @return the instance of Retrofit
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }
}
