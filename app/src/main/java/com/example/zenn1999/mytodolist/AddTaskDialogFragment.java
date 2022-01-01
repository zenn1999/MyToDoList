package com.example.zenn1999.mytodolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Home on 9/26/2017.
 */

public class AddTaskDialogFragment extends DialogFragment {

    // Use this instance of the interface to deliver action events
    AddTaskDialogListener mListener;

    /*public static AddTaskDialogFragment getInstance(String idTask) {
        Bundle bundle = new Bundle();
        bundle.putString("ID_TASK", idTask);
        AddTaskDialogFragment fragment = new AddTaskDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AddTaskDialogListener) context;
        }catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString() + " must implement AddTaskDialogListener");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)    //added for builder.setView()
    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.task_create_dialog, null, false);
        final EditText textView = (EditText) view.findViewById(R.id.edit_add_dialog);
        builder.setView(view);

        builder.setTitle("Add A new Task");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                // Send the positive button event back to the host activity
                mListener.onDialogPositiveClick(textView.getText().toString());
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(AddTaskDialogFragment.this);
                    }
                });

        return builder.create();
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface AddTaskDialogListener {
        public void onDialogPositiveClick(String inputValue);

        public void onDialogNegativeClick(DialogFragment dialog);
    }

}
