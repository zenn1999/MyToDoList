package com.example.zenn1999.mytodolist;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.example.zenn1999.mytodolist.data.TodoListContract;
import com.example.zenn1999.mytodolist.TodoCursorAdapter.ToggleTodoCheckListener;
import com.example.zenn1999.mytodolist.AddTaskDialogFragment.AddTaskDialogListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AddTaskDialogListener, ToggleTodoCheckListener{
    private TodoCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setHasFixedSize(true);

        cursorAdapter = new TodoCursorAdapter(null, this);
        mRecyclerView.setAdapter(cursorAdapter);

        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.floating_action_btn);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTaskDialogFragment dialogFragment = new AddTaskDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "AddTask");
            }
        });

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, TodoListContract.TodoEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);

    }


    @Override
    public void onDialogPositiveClick(String stringValue) {
        if (!TextUtils.isEmpty(stringValue)) {
            getContentResolver().insert(TodoListContract.TodoEntry.CONTENT_URI, getTodoListContentValues(stringValue));
        }

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onTodoItemChange(int todoID, boolean done) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TodoListContract.TodoEntry.COLUMN_DONE, done ? 1 : 0);
        String[] mSelectionArgs = {Integer.toString(todoID)};
        getContentResolver().update(TodoListContract.TodoEntry.CONTENT_URI, contentValues, TodoListContract.TodoEntry.WHERE_TODO_ID, mSelectionArgs);
    }


    private ContentValues getTodoListContentValues(String stringValue) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TodoListContract.TodoEntry.COLUMN_DESCRIPTION, stringValue);
        contentValues.put(TodoListContract.TodoEntry.COLUMN_DATETEXT, Calendar.getInstance().getTimeInMillis());
        return contentValues;
    }
}
