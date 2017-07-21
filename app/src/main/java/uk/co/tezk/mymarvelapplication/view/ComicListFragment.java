package uk.co.tezk.mymarvelapplication.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import uk.co.tezk.mymarvelapplication.R;
import uk.co.tezk.mymarvelapplication.adapter.ComicRecyclerViewAdapter;
import uk.co.tezk.mymarvelapplication.model.Comic;
import uk.co.tezk.mymarvelapplication.presenter.ComicPresenterImpl;
import uk.co.tezk.mymarvelapplication.presenter.IComicPresenter;
import uk.co.tezk.mymarvelapplication.presenter.ISchedulerProvider;
import uk.co.tezk.mymarvelapplication.retrofit.MarvelInteractor;
import uk.co.tezk.mymarvelapplication.retrofit.MarvelRetrofit;

/**
 * A simple {@link Fragment} subclass to display the comic list
 */
public class ComicListFragment extends Fragment implements IComicPresenter.IView, ComicRecyclerViewAdapter.OnClickHandler {
    // The view items, bound using Butterknife
    @BindView(R.id.rvComicList) RecyclerView rvComicList;
    // Our method attributes
    ComicRecyclerViewAdapter mComicListAdapter;
    IComicPresenter.IPresenter mPresenter;
    ProgressDialog mProgressDialog;

    List <Comic> mComicList;

    public ComicListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise and bind the presenter
        ISchedulerProvider schedulerProvider = new ISchedulerProvider() {
            @Override
            public Scheduler provideSubscribeScheduler() {
                return Schedulers.io();
            }

            @Override
            public Scheduler provideObserveScheduler() {
                return AndroidSchedulers.mainThread();
            }
        };
        mPresenter = new ComicPresenterImpl(new MarvelInteractor(new MarvelRetrofit().getRetrofit()), schedulerProvider);
        mPresenter.bind(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comic_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        // Initialise our Layout manager
        rvComicList.setLayoutManager(new LinearLayoutManager(getContext()));
        // Make the call to load the data
        if (mPresenter != null) {
            mPresenter.fetchComics();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unbind and release the presenter
        mPresenter.unbind();
        mPresenter = null;
    }

    @Override
    public void onStartLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("Please wait, fetching the list of comics");
            mProgressDialog.setTitle("Loading");
        }
        mProgressDialog.show();
    }

    @Override
    public void onFinishedLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onShowComics(List comicList) {
        mComicList = comicList;
        mComicListAdapter = new ComicRecyclerViewAdapter(comicList, getContext(), this);
        rvComicList.setAdapter(mComicListAdapter);

        //filterOnPrice(9.99);
    }

    @Override
    public void showError(Throwable e) {
        Toast.makeText(getContext(), "Error fetching comic list : "+e.getMessage(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(Comic comic) {
        /**
         * OnClickHandler for the RecyclerView adapter
         */
       // Toast.makeText(getContext(), "Pressed : "+comic.getTitle(), Toast.LENGTH_SHORT).show();

        StringBuilder message = new StringBuilder();
        message.append(comic.getTitle()+"\n\n");
        if (comic.getDescription()==null)
            comic.setDescription("No description available");
        message.append(comic.getDescription()+"\n\n");
        if (comic.getAuthors().length()==0)
            comic.setAuthors("no authors listed");
        message.append("Written by "+comic.getAuthors()+"\n\n");

        message.append("Price "+comic.getPrice()+"\n\n");
        message.append(comic.getPageCount()+" pages");

        new AlertDialog.Builder(getContext())
                .setTitle("Comic details")
                .setMessage(message.toString())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();

    }

    public void filterOnPrice(Double price) {
        /**
         * Set a price to filter for, if no price set on a comic, ignore it
         */

        Collections.sort(mComicList, new Comparator<Comic>() {
            @Override
            public int compare(Comic comic, Comic t1) {
                return (int)(comic.getPrice()*100 - t1.getPrice()*100);
            }
        });

        List <Comic> filteredList = new ArrayList<>();
        int pagesTotal = 0;

        for (Comic eachComic : mComicList) {
            double comicPrice = eachComic.getPrice();
            // If a price set
            if (comicPrice > 0) {
                // Do we have enough money left?
                if (price - comicPrice > 0) {
                    filteredList.add(eachComic);
                    price -= comicPrice;
                    pagesTotal += eachComic.getPageCount();
                }
            }
        }
        Toast.makeText(getContext(), "You can read "+pagesTotal+" pages for that amount", Toast.LENGTH_SHORT).show();
        mComicListAdapter = new ComicRecyclerViewAdapter(filteredList, getContext(), this);
        rvComicList.setAdapter(mComicListAdapter);

    }

    public void clearFilter() {
        mComicListAdapter = new ComicRecyclerViewAdapter(mComicList, getContext(), this);
        rvComicList.setAdapter(mComicListAdapter);
    }
}
