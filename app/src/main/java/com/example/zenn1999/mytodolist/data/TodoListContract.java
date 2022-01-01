package com.example.zenn1999.mytodolist.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Home on 9/26/2017.
 */

public class TodoListContract {

    public static final String CONTENT_AUTHORITY = "com.zenn1999.todolist";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TODO = "todo";
    public static final String DATE_FORMAT = "yyyyMMdd";
    public static final String QUERY_EQUAL = "=?";

    public static String getDbDateString(Date date) {
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * Converts a dateText to a long Unix time representation
     *
     * @param dateText the input date string
     * @return the Date object
     */
    public static Date getDateFromDb(String dateText) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return dbDateFormat.parse(dateText);
        }catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "todo";
        // Column with the foreign key into the location table.
        public static final String COLUMN_LOC_KEY = "location_id";
        // Due Date, stored as Text with format yyyy-MM-dd
        public static final String COLUMN_DESCRIPTION = "short_desc";
        public static final String WHERE_TODO_ID = _ID + QUERY_EQUAL;

        public static final String COLUMN_DONE = "done";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TODO).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_TODO;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_TODO;
        // Date, stored as Text with format yyyy-MM-dd
        public static final String COLUMN_DATETEXT = "date";
        // Due Date, stored as Text with format yyyy-MM-dd
        public static final String COLUMN_DUE_DATE_TEXT = "due_date";

        public static Uri buildTodoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildTodoWithStartDate(String locationSetting, String startDate) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting).appendQueryParameter(COLUMN_DATETEXT, startDate).build();
        }

        public static String getDateFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getStartDateFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_DATETEXT);
        }
    }

}
