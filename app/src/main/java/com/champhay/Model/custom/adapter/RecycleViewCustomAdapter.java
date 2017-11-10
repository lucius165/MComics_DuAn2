package com.champhay.Model.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.champhay.Model.handler.social.Comics;
import com.champhay.mcomics.R;
import com.champhay.mcomics.activity.ComicDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by HoangTP
 */

public class RecycleViewCustomAdapter extends RecyclerView.Adapter<RecycleViewCustomAdapter.RecyclerViewHolder> {

    private ArrayList<Comics> arrComics = new ArrayList<>();

    public RecycleViewCustomAdapter(ArrayList<Comics> arrComics) {
        this.arrComics = arrComics;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_top_list, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        final Context context = holder.comicImage.getContext();
        holder.comicName.setText(arrComics.get(position).comicsName);
        holder.viewNumber.setText(arrComics.get(position).getViews() + "");
        holder.chapterNumber.setText(arrComics.get(position).getEpisodes() + "");
        Picasso.with(context).load(arrComics.get(position).thumbnail).error(R.mipmap.bia).resize(300, 400).into(holder.comicImage);
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ComicDetailActivity.class);
                intent.putExtra("id", arrComics.get(position).getId() + "");
                intent.putExtra("comicsName", arrComics.get(position).getComicsName());
                intent.putExtra("view", String.valueOf(arrComics.get(position).getViews()));
                intent.putExtra("content", arrComics.get(position).getContent());
                intent.putExtra("thumbnail", arrComics.get(position).getThumbnail());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrComics.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView comicName, viewNumber, chapterNumber;
        ImageView comicImage;
        LinearLayout linear;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            linear = itemView.findViewById(R.id.linear);
            comicName = itemView.findViewById(R.id.txtHeader);
            comicImage = itemView.findViewById(R.id.imgItem);
            viewNumber = itemView.findViewById(R.id.view_number);
            chapterNumber = itemView.findViewById(R.id.chapter_number);

        }

    }
}
