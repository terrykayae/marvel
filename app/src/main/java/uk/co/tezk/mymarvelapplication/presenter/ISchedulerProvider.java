package uk.co.tezk.mymarvelapplication.presenter;

import io.reactivex.Scheduler;

/**
 * Created by tezk on 20/07/17.
 */

public interface ISchedulerProvider {
    Scheduler provideSubscribeScheduler() ;
    Scheduler provideObserveScheduler() ;
}
