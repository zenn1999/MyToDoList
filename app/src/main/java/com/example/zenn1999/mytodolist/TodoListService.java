package com.example.zenn1999.mytodolist;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.zenn1999.mytodolist.data.TodoListContract;

/**
 * Created by Home on 9/28/2017.
 */

public class TodoListService extends IntentService {
    public static final String EXTRA_TASK_DESCRIPTION = "EXTRA_TASK_DESCRIPTION";
    private final String LOG_TAG = TodoListService.class.getSimpleName();

    public TodoListService() {
        super("TodoListService");
    }

    @Override
    protected void onHandleIntent( Intent intent) {
        String taskdescription = intent.getStringExtra(EXTRA_TASK_DESCRIPTION);

        ContentValues contentValues = new ContentValues();
        contentValues.put(TodoListContract.TodoEntry.COLUMN_DESCRIPTION, taskdescription);

        getContentResolver().insert(TodoListContract.TodoEntry.CONTENT_URI, contentValues);

    }
}
