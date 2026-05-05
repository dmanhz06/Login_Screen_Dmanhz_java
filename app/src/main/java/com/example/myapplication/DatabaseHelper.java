package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PostsManager";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_POSTS = "posts";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_DATE = "date";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_AVATAR_URL = "avatarUrl";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_POSTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_NAME + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_CONTENT + " TEXT,"
                + KEY_AVATAR_URL + " TEXT" + ")";
        db.execSQL(CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        onCreate(db);
    }

    // Add new post
    public long addPost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, post.getUserName());
        values.put(KEY_DATE, post.getDate());
        values.put(KEY_CONTENT, post.getContent());
        values.put(KEY_AVATAR_URL, post.getAvatarUrl());

        long id = db.insert(TABLE_POSTS, null, values);
        db.close();
        return id;
    }

    // Get all posts
    public List<Post> getAllPosts() {
        List<Post> postList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_POSTS + " ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Post post = new Post(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                postList.add(post);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return postList;
    }

    // Update post
    public int updatePost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CONTENT, post.getContent());

        return db.update(TABLE_POSTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(post.getId())});
    }

    // Delete post
    public void deletePost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POSTS, KEY_ID + " = ?",
                new String[]{String.valueOf(post.getId())});
        db.close();
    }
}