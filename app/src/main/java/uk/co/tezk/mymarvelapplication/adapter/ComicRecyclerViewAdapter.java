package uk.co.tezk.mymarvelapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.tezk.mymarvelapplication.R;
import uk.co.tezk.mymarvelapplication.model.Comic;

/**
 * Created by tezk on 19/07/17.
 */

public class ComicRecyclerViewAdapter extends RecyclerView.Adapter <ComicRecyclerViewAdapter.ComicViewHolder> {

    private OnClickHandler onClickHandler;

    List <Comic> mComicList;
    Context mContext;

    public ComicRecyclerViewAdapter(List<Comic> comicList, Context context, OnClickHandler onClickHandler) {
        mComicList = comicList;
        mContext = context;
        this.onClickHandler = onClickHandler;
    }

    public void setOnClickHandler(OnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
    }

    @Override
    public ComicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.comic_card_item, parent, false);
        ComicViewHolder comicViewHolder = new ComicViewHolder(view);
//        ButterKnife.bind(comicViewHolder, parent);

        return comicViewHolder;
    }

    @Override
    public void onBindViewHolder(ComicViewHolder holder, int position) {
        Comic comic = mComicList.get(position);

        Glide.with(mContext).load(comic.getThumbnail()).into(holder.ivComic);
        holder.tvComicTitle.setText(comic.getTitle());
        holder.comic = comic;
    }

    @Override
    public int getItemCount() {
        return mComicList.size();
    }

    class ComicViewHolder extends RecyclerView.ViewHolder {
        Comic comic;

        @BindView(R.id.ivComicItem) ImageView ivComic;
        @BindView(R.id.tvComicItem) TextView tvComicTitle;

        public ComicViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickHandler!=null)
                        onClickHandler.onClick(comic);
                }
            });
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickHandler {
        void onClick(Comic comic) ;
    }
}
