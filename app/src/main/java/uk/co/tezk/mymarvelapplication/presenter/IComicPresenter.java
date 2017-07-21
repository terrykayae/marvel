package uk.co.tezk.mymarvelapplication.presenter;

import java.util.List;

/**
 * Created by tezk on 19/07/17.
 */

public interface IComicPresenter {
    interface IView {
        public void onStartLoading() ;
        public void onFinishedLoading() ;
        public void onShowComics(List comicList) ;
        public void showError(Throwable e) ;
    }

    interface IPresenter {
        public void bind(IView view) ;
        public void unbind() ;
        public void fetchComics();
    }
}
