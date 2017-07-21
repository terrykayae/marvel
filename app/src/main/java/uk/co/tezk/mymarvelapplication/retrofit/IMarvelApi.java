package uk.co.tezk.mymarvelapplication.retrofit;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import uk.co.tezk.mymarvelapplication.model.Constant;
import uk.co.tezk.mymarvelapplication.model.retrofit.MarvelResponse;

/**
 * Created by tezk on 19/07/17.
 */

public interface IMarvelApi {
    @GET(Constant.COMIC_API)
    Flowable<MarvelResponse> getComics(@Query("apikey") String apiKey, @Query("ts") String timeStamp, @Query("hash") String hash, @Query("limit") int limit) ;
}
