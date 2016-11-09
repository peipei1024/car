/**
 * Copyright (c) www.longdw.com
 */
package com.wm.remusic.info;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class MusicInfo implements Parcelable {


    public static final String KEY_ID = "_id";
    public static final String KEY_SONG_ID = "songid";
    public static final String KEY_ALBUM_ID = "albumid";
    public static final String KEY_ALBUM_NAME = "albumname";
    public static final String KEY_ALBUM_DATA = "albumdata";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_MUSIC_NAME = "musicname";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_ARTIST_ID = "artist_id";
    public static final String KEY_DATA = "data";
    public static final String KEY_FOLDER = "folder";
    public static final String KEY_MUSIC_NAME_KEY = "musicnamekey";
    public static final String KEY_SIZE = "size";
    public static final String KEY_FAVORITE = "favorite";

    /**
     * 数据库中的_id
     */
    public int _id = -1;
    public int songId = -1;
    public int albumId = -1;
    public String albumName;
    public String albumData;
    public int duration;
    public String musicName;
    public String artist;
    public long artistId;
    public String data;
    public String folder;
    public String musicNameKey;

    public int size;
    /**
     * 0表示没有收藏 1表示收藏
     */
    public int favorite = 0;
    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {

        @Override
        public MusicInfo createFromParcel(Parcel source) {
            MusicInfo music = new MusicInfo();
            Bundle bundle = new Bundle();
            bundle = source.readBundle();
            music._id = bundle.getInt(KEY_ID);
            music.songId = bundle.getInt(KEY_SONG_ID);
            music.albumId = bundle.getInt(KEY_ALBUM_ID);
            music.albumName = bundle.getString(KEY_ALBUM_NAME);
            music.duration = bundle.getInt(KEY_DURATION);
            music.musicName = bundle.getString(KEY_MUSIC_NAME);
            music.artist = bundle.getString(KEY_ARTIST);
            music.artistId = bundle.getLong(KEY_ARTIST_ID);
            music.data = bundle.getString(KEY_DATA);
            music.folder = bundle.getString(KEY_FOLDER);
            music.musicNameKey = bundle.getString(KEY_MUSIC_NAME_KEY);
            music.albumData = bundle.getString(KEY_ALBUM_DATA);
            music.size = bundle.getInt(KEY_SIZE);
            music.favorite = bundle.getInt(KEY_FAVORITE);
            return music;
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ID, _id);
        bundle.putInt(KEY_SONG_ID, songId);
        bundle.putInt(KEY_ALBUM_ID, albumId);
        bundle.putString(KEY_ALBUM_NAME, albumName);
        bundle.putString(KEY_ALBUM_DATA, albumData);
        bundle.putInt(KEY_DURATION, duration);
        bundle.putString(KEY_MUSIC_NAME, musicName);
        bundle.putString(KEY_ARTIST, artist);
        bundle.putLong(KEY_ARTIST_ID, artistId);
        bundle.putString(KEY_DATA, data);
        bundle.putString(KEY_FOLDER, folder);
        bundle.putString(KEY_MUSIC_NAME_KEY, musicNameKey);
        bundle.putInt(KEY_SIZE, size);
        bundle.putInt(KEY_FAVORITE, favorite);
        dest.writeBundle(bundle);
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

}