package uk.co.tezk.mymarvelapplication.retrofit;

import io.reactivex.Flowable;
import uk.co.tezk.mymarvelapplication.model.Constant;
import uk.co.tezk.mymarvelapplication.model.retrofit.MarvelResponse;
import uk.co.tezk.mymarvelapplication.utility.MD5;

import static uk.co.tezk.mymarvelapplication.model.Constant.NUM_COMICS;
import static uk.co.tezk.mymarvelapplication.model.Constant.PUBLIC_API_KEY;
import static uk.co.tezk.mymarvelapplication.model.Constant.TIME_STAMP;

/**
 * Created by Terry Kay
 *
 * Facade class to interact with the retrofit API
 */

public class MarvelInteractor {
    IMarvelApi mApi;

    public MarvelInteractor(IMarvelApi api) {
        mApi = api;
    }

    public Flowable<MarvelResponse> getComicList() {
        String hash = MD5.getMd5Hash(TIME_STAMP + Constant.PRIVATE_API_KEY + PUBLIC_API_KEY);

        final Flowable<MarvelResponse> response = mApi.getComics(PUBLIC_API_KEY, TIME_STAMP, hash, NUM_COMICS);

        return response;
    }

}
