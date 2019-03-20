package com.example.app.ksugym.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.ksugym.Classes;
import com.example.app.ksugym.Customs.CustomAdminNewsGridView;
import com.example.app.ksugym.News;
import com.example.app.ksugym.R;
import com.example.app.ksugym.Students.StudentLogin;
import com.example.app.ksugym.Students.StudentSubscribe;
import com.example.app.ksugym.Students.Students;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminAddClassFragment  extends Fragment
{
    static View view;

    EditText className, trainerName, description, classNum;
    Button addClass;


    public static AdminAddClassFragment newInstance() {
        AdminAddClassFragment fragment = new AdminAddClassFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_admin_addclass, container, false);

        className = view.findViewById(R.id.addClassName);
        trainerName = view.findViewById(R.id.addClassTrainerName);
        description = view.findViewById(R.id.addClassDescription);
        classNum = view.findViewById(R.id.addClassNumStudents);
        addClass = view.findViewById(R.id.addClassBtn);

        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(checkIfAllMandatoryFieldsEntered())
                {
                    //Class object to write into my database
                    final Classes c = new Classes(className.getText().toString(), trainerName.getText().toString(),
                            description.getText().toString(),classNum.getText().toString());

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    final DatabaseReference ref = firebaseDatabase.getReference();

                    ref.child("Classes").orderByChild("className").equalTo(className.getText().toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot)
                                {
                                    if (dataSnapshot.exists()) {
                                        Toast.makeText(view.getContext(), "This Class Name already exists!", Toast.LENGTH_LONG).show();

                                    } else {

                                        FirebaseDatabase firebaseDatabase2 = FirebaseDatabase.getInstance();
                                        final DatabaseReference ref2 = firebaseDatabase2.getReference();
                                        ref2.child("Classes").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                ref2.child("Classes").push().setValue(c);
                                                Toast.makeText(view.getContext(), "Class successfully added", Toast.LENGTH_LONG).show();
                                                AdminClassesFragment.UpdateClassGridView();
                                                //go to gridview fragment
                                                AdminClassesFragment fragment = AdminClassesFragment.newInstance();
                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                                ft.replace(R.id.fragment_container, fragment,"Classes")
                                                        .commit();
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });


                } //end of check of all mando
            }
        });



        return view;
    }//End of OnCreate

    //Check if all fields are filled in
    private boolean checkIfAllMandatoryFieldsEntered() {
        // Reset errors.

        className.setError(null);
        trainerName.setError(null);
        description.setError(null);
        classNum.setError(null);


        // Store values at the time of the login attempt.
        String classname= className.getText().toString();
        String trainername = trainerName.getText().toString();
        String classdesc= description.getText().toString();
        String classnum = classNum.getText().toString();


        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(classname)) {
           className.setError(getString(R.string.error_field_required));
            focusView = className;
            cancel = true;
        }

        if (TextUtils.isEmpty(trainername)) {
            trainerName.setError(getString(R.string.error_field_required));
            focusView = trainerName;
            cancel = true;
        }
        if (TextUtils.isEmpty(classdesc)) {
            description.setError(getString(R.string.error_field_required));
            focusView = description;
            cancel = true;
        }
        if (TextUtils.isEmpty(classnum)) {
            classNum.setError(getString(R.string.error_field_required));
            focusView = classNum;
            cancel = true;
        }


        if (cancel) {

            focusView.requestFocus();
        } else {

            return true;
        }
        return false;
    }//end of check text



}//End of class



