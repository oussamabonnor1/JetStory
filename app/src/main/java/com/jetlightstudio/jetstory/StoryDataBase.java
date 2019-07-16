package com.jetlightstudio.jetstory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by oussama on 12/03/2018.
 */

public class StoryDataBase extends SQLiteOpenHelper {

    private static final String dataBaseName = "savedStories.db";
    private static final int dataBaseVersion = 1;
    private static String tableName = "stories";
    private static String culomnID = "id";
    private static String culomnTitle = "title";
    private static String culomnAuthor = "author";
    private static String culomnDate = "date";
    private static String culomnContent = "content";
    private static String culomnTime = "time";
    private static String culomncategory = "category";


    public StoryDataBase(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, dataBaseName, factory, dataBaseVersion);
    }

    public void saveStory(Story story, Context context) {
        if (!isStorySaved(story.getId())) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues content = new ContentValues();
            content.put(culomnID, story.getId());
            content.put(culomnTitle, story.getTitle());
            content.put(culomnAuthor, story.getAuthor());
            content.put(culomnDate, story.getDate());
            content.put(culomnContent, story.getContent());
            content.put(culomnTime, story.getTime());
            content.put(culomncategory, story.getCategory().name());
            db.insert(tableName, null, content);

            Toast.makeText(context, "Story SAVED successfully!", Toast.LENGTH_SHORT).show();
        } else {
            removeStory(story.getId(), context);
        }
    }

    public boolean isStorySaved(int id) {
        String query = "Select " + culomnID + " From " + tableName + " where " + culomnID + " = " + id;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        return c.moveToFirst();
    }

    public void removeStory(int id, Context context) {
        String query = "DELETE FROM " + tableName + " WHERE " + culomnID + "=" + id;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        Toast.makeText(context, "Story DELETED successfully!", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Story> loadStories() {
        ArrayList<Story> stories = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "Select * FROM " + tableName;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            stories.add(new Story(c.getString(c.getColumnIndex(culomnTitle)),
                    c.getString(c.getColumnIndex(culomnAuthor)),
                    c.getString(c.getColumnIndex(culomnDate)),
                    c.getString(c.getColumnIndex(culomnContent)),
                    c.getInt(c.getColumnIndex(culomnID)),
                    c.getInt(c.getColumnIndex(culomnTime)),
                    getCategory(c.getString(c.getColumnIndex(culomncategory))).toString()));
            c.moveToNext();
        }
        c.close();
        db.close();
        return stories;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "Create table " + tableName + " (" +
                culomnID + " INTEGER," +
                culomnTitle + " varchar(500)," +
                culomnAuthor + " varchar(500)," +
                culomnDate + " date," +
                culomnContent + " text," +
                culomnTime + " INTEGER," +
                culomncategory + " varchar(50)," +
                "PRIMARY KEY(" + culomnID + ")"
                + ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(sqLiteDatabase);
    }

    protected Story.Category getCategory(String name) {
        if (name.equals("action")) return Story.Category.action;
        else if (name.equals("comedy")) return Story.Category.comedy;
        else if (name.equals("romance")) return Story.Category.romance;
        else if (name.equals("moral")) return Story.Category.moral;
        else if (name.equals("sad")) return Story.Category.sad;
        else return Story.Category.action;
    }
}
