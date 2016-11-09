/*
 * Copyright (C) 2015 Naman Dwivedi
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.wm.remusic.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wm.remusic.R;
import com.wm.remusic.fragment.MoreFragment;
import com.wm.remusic.fragment.SimpleMoreFragment;
import com.wm.remusic.info.MusicInfo;
import com.wm.remusic.service.MusicPlayer;
import com.wm.remusic.uitl.IConstants;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ItemHolder> {

    private Activity mContext;
    private List<MusicInfo> searchResults = new ArrayList<>();

    public SearchAdapter(Activity context) {
        this.mContext = context;

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v0 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.frament_musci_common_item, null);
        ItemHolder ml0 = new ItemHolder(v0);
        return ml0;
    }

    @Override
    public void onBindViewHolder(final ItemHolder itemHolder, int i) {

        MusicInfo song = searchResults.get(i);
        itemHolder.title.setText(song.musicName);
        itemHolder.songartist.setText(song.artist);
        setOnPopupMenuListener(itemHolder, i);
    }

    @Override
    public void onViewRecycled(ItemHolder itemHolder) {

    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    private void setOnPopupMenuListener(ItemHolder itemHolder, final int position) {

        itemHolder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleMoreFragment morefragment = SimpleMoreFragment.newInstance(searchResults.get(position).songId);
                morefragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "music");
            }
        });
    }


    public void updateSearchResults(List searchResults) {
        this.searchResults = searchResults;
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, songartist;
        ImageView menu, albumArt;

        public ItemHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.viewpager_list_toptext);
            this.songartist = (TextView) view.findViewById(R.id.viewpager_list_bottom_text);
            this.albumArt = (ImageView) view.findViewById(R.id.play_state);
            this.menu = (ImageView) view.findViewById(R.id.viewpager_list_button);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    long[] ret = new long[1];
                    ret[0] = ((MusicInfo) searchResults.get(getAdapterPosition())).songId;
                    MusicPlayer.playAll(mContext, ret, 0, false);
                }
            }, 100);

        }

    }
}





