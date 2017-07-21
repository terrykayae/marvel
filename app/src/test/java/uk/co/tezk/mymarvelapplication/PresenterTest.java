package uk.co.tezk.mymarvelapplication;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import uk.co.tezk.mymarvelapplication.model.retrofit.MarvelResponse;
import uk.co.tezk.mymarvelapplication.presenter.ComicPresenterImpl;
import uk.co.tezk.mymarvelapplication.presenter.IComicPresenter;
import uk.co.tezk.mymarvelapplication.presenter.ISchedulerProvider;
import uk.co.tezk.mymarvelapplication.retrofit.MarvelInteractor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Terry Kay
 *
 * test of the Presenter
 */

public class PresenterTest {

    IComicPresenter.IPresenter mPresenter;
    ISchedulerProvider mSchedulerProvider;
    @Mock
    MarvelInteractor marvelInteractor;
    @Mock
    IComicPresenter.IView mView;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mSchedulerProvider = new ISchedulerProvider() {
            @Override
            public Scheduler provideSubscribeScheduler() {
                return Schedulers.trampoline();
            }

            @Override
            public Scheduler provideObserveScheduler() {
                return Schedulers.trampoline();
            }
        };
        mPresenter = new ComicPresenterImpl(marvelInteractor, mSchedulerProvider);

    }

    @Test(expected = RuntimeException.class)
    public void testCallingGetDataWithoutBindingThrowsException() {

        mPresenter.fetchComics();
    }

    @Test
    public void testBinding() {
        when(marvelInteractor.getComicList()).thenReturn(Flowable.<MarvelResponse>empty());

        mPresenter.bind(mView);
        mPresenter.fetchComics();

        verify(mView, times(1)).onStartLoading();

    }

    @Test
    public void testOnError() {
        when(marvelInteractor.getComicList()).thenReturn(Flowable.<MarvelResponse>error(new Throwable("test")));

        mPresenter.bind(mView);
        mPresenter.fetchComics();

        verify(mView, times(1)).onStartLoading();
        verify(mView, times(1)).showError(any(Throwable.class));
    }
}
