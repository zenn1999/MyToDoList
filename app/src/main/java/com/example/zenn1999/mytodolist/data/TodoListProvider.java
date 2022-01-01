package com.example.zenn1999.mytodolist.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


/**
 * Created by Home on 9/27/2017.
 */

public class TodoListProvider extends ContentProvider {
    private static final int TODO = 100;
    private static final int TODO_ID = 101;

    //the URI matcher used by this content provider
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private TodoListDBHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TodoListContract.CONTENT_AUTHORITY;

        //for each type of URI you want to add create the coresponding code.

        matcher.addURI(authority, TodoListContract.PATH_TODO, TODO);
        matcher.addURI(authority, TodoListContract.PATH_TODO + "/#", TODO_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new TodoListDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case TODO: {
                retCursor = mOpenHelper.getReadableDatabase().query(TodoListContract.TodoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case  TODO_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TodoListContract.TodoEntry.TABLE_NAME,
                        projection,
                        TodoListContract.TodoEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TODO:
                return TodoListContract.TodoEntry.CONTENT_TYPE;
            case TODO_ID:
                return TodoListContract.TodoEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TODO: {
                long _id = db.insert(TodoListContract.TodoEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = TodoListContract.TodoEntry.buildTodoUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case TODO:
                rowsDeleted = db.delete(TodoListContract.TodoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);


        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case TODO:
                rowsUpdated = db.update(TodoListContract.TodoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TODO:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TodoListContract.TodoEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }

    }
}
