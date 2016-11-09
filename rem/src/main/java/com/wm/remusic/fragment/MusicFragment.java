package com.wm.remusic.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wm.remusic.R;
import com.wm.remusic.activity.SelectActivity;
import com.wm.remusic.info.MusicInfo;
import com.wm.remusic.service.MusicPlayer;
import com.wm.remusic.uitl.DividerItemDecoration;
import com.wm.remusic.uitl.IConstants;
import com.wm.remusic.uitl.MusicUtils;
import com.wm.remusic.uitl.PreferencesUtility;
import com.wm.remusic.uitl.SortOrder;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by wm on 2016/1/19.
 */
public class MusicFragment extends BaseFragment {
    private int currentlyPlayingPosition = 0;
    private Adapter mAdapter;
    private ArrayList<MusicInfo> musicInfos;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PreferencesUtility mPreferences;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = PreferencesUtility.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recylerview, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new Adapter(null);
        recyclerView.setAdapter(mAdapter);
        //fastScroller = (FastScroller) view.findViewById(R.id.fastscroller);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        reloadAdapter();
        // new loadSongs().execute("");

        return view;
    }


    //去除界面重叠
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            //参数是固定写法
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    //刷新列表
    public void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                ArrayList<MusicInfo> songList = (ArrayList) MusicUtils.queryMusic(getActivity(), IConstants.START_FROM_LOCAL);
                mAdapter.updateDataSet(songList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.song_sort_by, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.menu_sort_by_az) {

                mPreferences.setSongSortOrder(SortOrder.SongSortOrder.SONG_A_Z);
                reloadAdapter();
                return true;
        }
        if (id== R.id.menu_sort_by_date) {
            mPreferences.setSongSortOrder(SortOrder.SongSortOrder.SONG_DATE);
            reloadAdapter();
            return true;
        }
        if (id== R.id.menu_sort_by_artist) {
            mPreferences.setSongSortOrder(SortOrder.SongSortOrder.SONG_ARTIST);
            reloadAdapter();
            return true;
        }
        if (id== R.id.menu_sort_by_album) {
            mPreferences.setSongSortOrder(SortOrder.SongSortOrder.SONG_ALBUM);
            reloadAdapter();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //异步加载recyclerview界面
    private class loadSongs extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (getActivity() != null) {
                musicInfos = (ArrayList<MusicInfo>) MusicUtils.queryMusic(getActivity(), IConstants.START_FROM_LOCAL);
                if (musicInfos != null)
                    mAdapter = new Adapter(musicInfos);
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            recyclerView.setAdapter(mAdapter);
            if (getActivity() != null)
                recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        }

        @Override
        protected void onPreExecute() {

        }
    }

    public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        final static int FIRST_ITEM = 0;
        final static int ITEM = 1;
        private ArrayList<MusicInfo> mList;

        public Adapter(ArrayList<MusicInfo> list) {
//            if (list == null) {
//                throw new IllegalArgumentException("model Data must not be null");
//            }
            mList = list;
        }

        //更新adpter的数据
        public void updateDataSet(ArrayList<MusicInfo> list) {
            this.mList = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            if (viewType == FIRST_ITEM)
                return new CommonItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_item, viewGroup, false));

            else {
                return new ListItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.frament_musci_common_item, viewGroup, false));
            }
        }

        //判断布局类型
        @Override
        public int getItemViewType(int position) {
            return position == FIRST_ITEM ? FIRST_ITEM : ITEM;

        }

        //将数据与界面进行绑定
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MusicInfo model = null;
            if (position > 0) {
                model = mList.get(position - 1);
            }
            if (holder instanceof ListItemViewHolder) {

                ((ListItemViewHolder) holder).mainTitle.setText(model.musicName.toString());
                ((ListItemViewHolder) holder).title.setText(model.artist.toString());

                //判断该条目音乐是否在播放
                if (MusicPlayer.getCurrentAudioId() == model.songId) {
                    ((ListItemViewHolder) holder).playState.setVisibility(View.VISIBLE);
                    ((ListItemViewHolder) holder).playState.setImageResource(R.drawable.song_play_icon);
                    currentlyPlayingPosition = position;
                } else {
                    ((ListItemViewHolder) holder).playState.setVisibility(View.GONE);
                }

            } else if (holder instanceof CommonItemViewHolder) {
                ((CommonItemViewHolder) holder).textView.setText("(共" + mList.size() + "首)");

                ((CommonItemViewHolder) holder).select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SelectActivity.class);
                        intent.putParcelableArrayListExtra("ids", mList);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().startActivity(intent);
                    }
                });

            }
        }

        @Override
        public int getItemCount() {
            return (null != mList ? mList.size() + 1 : 0);
        }


        public class CommonItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textView;
            ImageView select;

            CommonItemViewHolder(View view) {
                super(view);
                this.textView = (TextView) view.findViewById(R.id.play_all_number);
                this.select = (ImageView) view.findViewById(R.id.select);
                view.setOnClickListener(this);
            }

            public void onClick(View v) {
                //// TODO: 2016/1/20
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        long[] list = new long[mList.size()];
                        for (int i = 0; i < mList.size(); i++) {
                            list[i] = mList.get(i).songId;
                        }
                        MusicPlayer.playAll(getContext(), list, 0, false);
                    }
                }, 100);

            }

        }


        public class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            //ViewHolder
            ImageView moreOverflow, playState;
            TextView mainTitle, title;

            ListItemViewHolder(View view) {
                super(view);
                this.mainTitle = (TextView) view.findViewById(R.id.viewpager_list_toptext);
                this.title = (TextView) view.findViewById(R.id.viewpager_list_bottom_text);
                this.playState = (ImageView) view.findViewById(R.id.play_state);
                this.moreOverflow = (ImageView) view.findViewById(R.id.viewpager_list_button);

                moreOverflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MoreFragment morefragment = MoreFragment.newInstance(mList.get(getAdapterPosition() - 1).songId + "", IConstants.MUSICOVERFLOW);
                        morefragment.show(getFragmentManager(), "music");
                    }
                });
                view.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        long[] list = new long[mList.size()];
                        for (int i = 0; i < mList.size(); i++) {
                            list[i] = mList.get(i).songId;
                        }
                        MusicPlayer.playAll(getContext(), list, getAdapterPosition() - 1, false);
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notifyItemChanged(currentlyPlayingPosition);
                                notifyItemChanged(getAdapterPosition());
                            }
                        }, 50);
                    }
                }, 100);
            }

        }
    }

}