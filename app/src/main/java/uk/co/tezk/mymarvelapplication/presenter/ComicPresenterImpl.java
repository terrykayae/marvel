package uk.co.tezk.mymarvelapplication.presenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.tezk.mymarvelapplication.model.Comic;
import uk.co.tezk.mymarvelapplication.model.retrofit.Item;
import uk.co.tezk.mymarvelapplication.model.retrofit.MarvelResponse;
import uk.co.tezk.mymarvelapplication.model.retrofit.Result;
import uk.co.tezk.mymarvelapplication.retrofit.MarvelInteractor;

/**
 * Created by Terry Kay
 *
 * Presenter to format data from API suitable for the view
 */

public class ComicPresenterImpl implements IComicPresenter.IPresenter {

    private MarvelInteractor mMarvelInteractor;
    private IComicPresenter.IView mView;
    private CompositeDisposable mCompositeDisposable;
    private ISchedulerProvider mSchedulerProvider;

    public ComicPresenterImpl(MarvelInteractor marvelInteractor, ISchedulerProvider schedulerProvider) {
        /**
         * Constructor
         * @param IMarvelApi inject the interactor to use
         */
        mMarvelInteractor = marvelInteractor;
        mSchedulerProvider = schedulerProvider;
    }

    @Override
    public void fetchComics() {
        if (mView == null) {
            throw new RuntimeException("Presenter must be bound to a view before calling fetchComics");
        }
        mView.onStartLoading();
        mMarvelInteractor.getComicList()
                .subscribeOn(mSchedulerProvider.provideSubscribeScheduler())
                .observeOn(mSchedulerProvider.provideObserveScheduler())
                // Not the best way as this processes on the UI thread. Would prefer to flatMap to release the elements
                // then perform the mapping there
                .subscribe(new Consumer<MarvelResponse>() {
                               @Override
                               public void accept(MarvelResponse marvelResponse) throws Exception {
                                   List <Comic> comicList = new ArrayList();
                                   List<Result> resultList = marvelResponse.getData().getResults();
                                   for (Result eachResult : resultList) {
                                       Comic comic = new Comic();
                                       comic.setTitle(eachResult.getTitle());
                                       comic.setDescription(eachResult.getDescription());
                                       comic.setThumbnail(eachResult.getThumbnail().getPath()+"."+eachResult.getThumbnail().getExtension());
                                       comic.setPageCount(eachResult.getPageCount());
                                       comic.setPrice(eachResult.getPrices().size() == 0 ? 0 : eachResult.getPrices().get(0).getPrice());

                                       String authors = "";
                                       for (Item eachAuthor : eachResult.getCreators().getItems()) {
                                           authors += eachAuthor.getName()+", ";
                                       }
                                       comic.setAuthors(authors);

                                       comicList.add(comic);
                                   }
                                   mView.onShowComics(comicList);
                                   mView.onFinishedLoading();
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                mView.showError(throwable);
                                mView.onFinishedLoading();
                            }
                        });
    }

    @Override
    public void bind(IComicPresenter.IView view) {

        mView = view;
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed())
            mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void unbind() {
        if (mCompositeDisposable!=null)
            mCompositeDisposable.dispose();
        mView = null;
    }
}
