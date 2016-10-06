package com.b2infosoft.addley.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.b2infosoft.addley.model.Category;
import com.b2infosoft.addley.model.OfferItem;
import com.b2infosoft.addley.model.TopStoryItem;
import com.b2infosoft.addley.model.CouponItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajesh on 5/18/2016.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "addley.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String offer_all = "CREATE TABLE " + TableSchema.OFFER_ALL + " ( " + TableSchema.OFFER_ALL_NAME + " text," + TableSchema.OFFER_ALL_LOGO + " text," + TableSchema.OFFER_ALL_DISCOUNT + " text," + TableSchema.OFFER_ALL_LINK + " text," + TableSchema.LAST_POSITION + " text);";
        db.execSQL(offer_all);
        String top_story = "CREATE TABLE " + TableSchema.TOP_STORY + " ( " + TableSchema.TOP_STORY_NAME + " text,"+ TableSchema.TOP_STORY_C_ID+" text,"+ TableSchema.TOP_STORY_COUNT + " text," + TableSchema.TOP_STORY_IMG + " text," + TableSchema.LAST_POSITION + " text);";
        db.execSQL(top_story);
        String category = "CREATE TABLE " + TableSchema.CATEGORY + " ( " + TableSchema.CATEGORY_ID + " text," + TableSchema.CATEGORY_NAME + " text," + TableSchema.CATEGORY_COUNT + " text," + TableSchema.CATEGORY_IMAGE + " text);";
        db.execSQL(category);
        String level1 = "CREATE TABLE " + TableSchema.LEVEL1 + " ( " + TableSchema.LEVEL1_CODE + " text," + TableSchema.LEVEL1_DATE + " text," + TableSchema.LEVEL1_NAME + " text," + TableSchema.LAST_POSITION + " text);";
        db.execSQL(level1);
        String level2 = "CREATE TABLE " + TableSchema.LEVEL2 + " ( " + TableSchema.LEVEL2_CODE + " text," + TableSchema.LEVEL2_DATE + " text," + TableSchema.LEVEL2_NAME + " text," + TableSchema.LAST_POSITION + " text);";
        db.execSQL(level2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableSchema.OFFER_ALL);
        db.execSQL("DROP TABLE IF EXISTS " + TableSchema.TOP_STORY);
        db.execSQL("DROP TABLE IF EXISTS " + TableSchema.CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TableSchema.LEVEL1);
        db.execSQL("DROP TABLE IF EXISTS " + TableSchema.LEVEL2);
        onCreate(db);
    }

    public boolean insertOfferAll(OfferItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableSchema.OFFER_ALL_NAME, item.getName());
        contentValues.put(TableSchema.OFFER_ALL_LOGO, item.getLogo());
        contentValues.put(TableSchema.OFFER_ALL_DISCOUNT, item.getDiscount());
        contentValues.put(TableSchema.OFFER_ALL_LINK, item.getOfferLink());
        contentValues.put(TableSchema.LAST_POSITION, item.getLastPosition());
        db.insert(TableSchema.OFFER_ALL, null, contentValues);
        return true;
    }

    public boolean insertTopStory(TopStoryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableSchema.TOP_STORY_NAME, item.getName());
        contentValues.put(TableSchema.TOP_STORY_COUNT, item.getOfferCount());
        contentValues.put(TableSchema.TOP_STORY_IMG, item.getImg());
        db.insert(TableSchema.TOP_STORY, null, contentValues);
        return true;
    }

    public boolean insertTopStory(CouponItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableSchema.TOP_STORY_NAME, item.getCompanyName());
        contentValues.put(TableSchema.TOP_STORY_C_ID, item.getCompanyId());
        contentValues.put(TableSchema.TOP_STORY_COUNT, item.getCompanyCount());
        contentValues.put(TableSchema.TOP_STORY_IMG, item.getCompanyImage());
        contentValues.put(TableSchema.LAST_POSITION, item.getLastPosition());
        db.insert(TableSchema.TOP_STORY, null, contentValues);
        return true;
    }

    public boolean insertCategory(Category item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableSchema.CATEGORY_ID, item.getId());
        contentValues.put(TableSchema.CATEGORY_NAME, item.getName());
        contentValues.put(TableSchema.CATEGORY_COUNT, item.getCount());
        contentValues.put(TableSchema.CATEGORY_IMAGE, item.getImg());
        db.insert(TableSchema.CATEGORY, null, contentValues);
        return true;
    }

    public boolean insertLevel1(String date, String code, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableSchema.LEVEL1_CODE, code);
        contentValues.put(TableSchema.LEVEL1_DATE, date);
        contentValues.put(TableSchema.LEVEL1_NAME, name);
        db.insert(TableSchema.LEVEL1, null, contentValues);
        return true;
    }

    public boolean insertLevel2(String date, String code, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableSchema.LEVEL2_CODE, code);
        contentValues.put(TableSchema.LEVEL2_DATE, date);
        contentValues.put(TableSchema.LEVEL2_NAME, name);
        db.insert(TableSchema.LEVEL2, null, contentValues);
        return true;
    }

    public List<OfferItem> getOfferAll() {
        List<OfferItem> offerItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("SELECT * FROM " + TableSchema.OFFER_ALL, null);
        while (rs.moveToNext()) {
            OfferItem item = new OfferItem();
            item.setName(rs.getString(rs.getColumnIndex(TableSchema.OFFER_ALL_NAME)));
            item.setLogo(rs.getString(rs.getColumnIndex(TableSchema.OFFER_ALL_LOGO)));
            item.setDiscount(rs.getString(rs.getColumnIndex(TableSchema.OFFER_ALL_DISCOUNT)));
            item.setOfferLink(rs.getString(rs.getColumnIndex(TableSchema.OFFER_ALL_LINK)));
            item.setLastPosition(rs.getString(rs.getColumnIndex(TableSchema.LAST_POSITION)));
            offerItems.add(item);
        }
        if (!rs.isClosed()) {
            rs.close();
        }
        return offerItems;
    }

    public int getLastPositionOffer() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("SELECT * FROM " + TableSchema.OFFER_ALL, null);
        int last = 0;
        if (rs.moveToNext()) {
            rs.moveToLast();
            last = Integer.parseInt(rs.getString(rs.getColumnIndex(TableSchema.LAST_POSITION)).trim());
        }
        if (!rs.isClosed()) {
            rs.close();
        }
        return last;
    }

    public List<TopStoryItem> getTopStory() {
        List<TopStoryItem> offerItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("SELECT * FROM " + TableSchema.TOP_STORY, null);
        while (rs.moveToNext()) {
            TopStoryItem item = new TopStoryItem();
            item.setName(rs.getString(rs.getColumnIndex(TableSchema.TOP_STORY_NAME)));
            int count = Integer.parseInt(rs.getString(rs.getColumnIndex(TableSchema.TOP_STORY_COUNT)).trim());
            item.setOfferCount(count);
            item.setImg(rs.getString(rs.getColumnIndex(TableSchema.TOP_STORY_IMG)));
            offerItems.add(item);
        }
        if (!rs.isClosed()) {
            rs.close();
        }
        return offerItems;
    }

    public List<CouponItem> getCoupons() {
        List<CouponItem> offerItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("SELECT * FROM " + TableSchema.TOP_STORY, null);
        while (rs.moveToNext()) {
            CouponItem item = new CouponItem();
            item.setCompanyName(rs.getString(rs.getColumnIndex(TableSchema.TOP_STORY_NAME)));
            int count = Integer.parseInt(rs.getString(rs.getColumnIndex(TableSchema.TOP_STORY_COUNT)).trim());
            item.setCompanyCount(count);
            item.setCompanyId(rs.getString(rs.getColumnIndex(TableSchema.TOP_STORY_C_ID)));
            item.setCompanyImage(rs.getString(rs.getColumnIndex(TableSchema.TOP_STORY_IMG)));
            offerItems.add(item);
        }
        if (!rs.isClosed()) {
            rs.close();
        }
        return offerItems;
    }
    public int getLastPositionCoupon() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("SELECT * FROM " + TableSchema.TOP_STORY, null);
        int last = 0;
        if (rs.moveToNext()) {
            rs.moveToLast();
            last = Integer.parseInt(rs.getString(rs.getColumnIndex(TableSchema.LAST_POSITION)).trim());
        }
        if (!rs.isClosed()) {
            rs.close();
        }
        return last;
    }

    public List<Category> getCategory() {
        List<Category> offerItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("SELECT * FROM " + TableSchema.CATEGORY, null);
        while (rs.moveToNext()) {
            Category item = new Category();
            item.setName(rs.getString(rs.getColumnIndex(TableSchema.CATEGORY_NAME)));
            int count = Integer.parseInt(rs.getString(rs.getColumnIndex(TableSchema.CATEGORY_COUNT)).trim());
            item.setCount(count);
            int id = Integer.parseInt(rs.getString(rs.getColumnIndex(TableSchema.CATEGORY_ID)).trim());
            item.setId(id);
            item.setImg(rs.getString(rs.getColumnIndex(TableSchema.CATEGORY_IMAGE)));
            offerItems.add(item);
        }
        if (!rs.isClosed()) {
            rs.close();
        }
        return offerItems;
    }

    public boolean deleteOfferAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TableSchema.OFFER_ALL);
        return true;
    }

    public boolean deleteTopStory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TableSchema.TOP_STORY);
        return true;
    }

    public boolean deleteCategory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TableSchema.CATEGORY);
        return true;
    }

    public boolean deleteLevel1() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TableSchema.LEVEL1);
        return true;
    }

    public boolean deleteLevel2() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TableSchema.LEVEL2);
        return true;
    }
}