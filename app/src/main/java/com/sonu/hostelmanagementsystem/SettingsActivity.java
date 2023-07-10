package com.sonu.hostelmanagementsystem;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    Button btnDeleteStudents, btnDeleteAttendance, btnDeleteDatabase;
    Spinner spnMonth;
    EditText edtYear;
    String[] months;
    private RelativeLayout rlSettings;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Delete");
        rlSettings = (RelativeLayout) findViewById(R.id.rl_settings);

        months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        spnMonth = (Spinner) findViewById(R.id.spnMonth);
        spnMonth.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, months));
        spnMonth.setSelection(0);
        btnDeleteStudents = (Button) findViewById(R.id.btn_delete_students);
        btnDeleteAttendance = (Button) findViewById(R.id.btn_delete_attendance);
        btnDeleteDatabase = (Button) findViewById(R.id.btn_delete_database);
        edtYear = (EditText) findViewById(R.id.edtYear);

        btnDeleteStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("Delete Alert", "Delete all Student records ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean isDeleted = DBUtils.deleteStudentsTable(SettingsActivity.this);
                        if(isDeleted)
                            Snackbar.make(rlSettings,"Students table deleted!",Snackbar.LENGTH_LONG).show();
                        else
                            Snackbar.make(rlSettings,"Table not found!",Snackbar.LENGTH_LONG).show();
                    }
                }, null);

            }
        });

        btnDeleteAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtYear.getText().length()<4){
                    edtYear.setError("Enter valid Year");
                    edtYear.requestFocus();
                    return;
                }

                showAlert("Delete Alert", "Delete all Attendance records ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String month = months[spnMonth.getSelectedItemPosition()].toLowerCase();
                        String year = edtYear.getText().toString().trim();
                        String TableName = month + "_" + year;
                        boolean isDeleted = DBUtils.deleteStudentsAttendanceTable(SettingsActivity.this,TableName);
                        if(isDeleted)
                            Snackbar.make(rlSettings,"Attendance table deleted!",Snackbar.LENGTH_LONG).show();
                        else
                            Snackbar.make(rlSettings,"Table not found!",Snackbar.LENGTH_LONG).show();
                    }
                }, null);

            }
        });

        btnDeleteDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("Delete Alert", "Delete complete Database ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean isDeleted = DBUtils.deleteDatabase(SettingsActivity.this);
                        if(isDeleted)
                            Snackbar.make(rlSettings,"Database deleted",Snackbar.LENGTH_LONG).show();
                        else
                            Snackbar.make(rlSettings,"There was an error in deleting database",Snackbar.LENGTH_LONG).show();
                    }
                }, null);
            }
        });

    }

    private void showAlert(String title, String message, DialogInterface.OnClickListener yes_listener, DialogInterface.OnClickListener no_listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setMessage(message)
                .setTitle(title)
                .setCancelable(true)
                .setPositiveButton("Yes", yes_listener)
                .setNegativeButton("No", no_listener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
