package com.sonu.hostelmanagementsystem;

import java.util.Calendar;
import java.util.List;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

public class SetAttendanceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    static final String PRESENT = "P";
    static final String ABSENT = "A";
    Button btnSetDate, btnFinalize;
    Spinner spnYear;
    RecyclerView rvStudents;
    StudentsAttendanceAdapter studentsAttendanceAdapter;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    int Year, Month, Day;
    private RelativeLayout rlSetAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_attendance);

        rlSetAttendance = (RelativeLayout) findViewById(R.id.rl_set_attendance);
        getSupportActionBar().setTitle("Set Attendance");

        //Initializations
        rvStudents = (RecyclerView) findViewById(R.id.rvStudents);
        spnYear = (Spinner) findViewById(R.id.spnYear);
        btnFinalize = (Button) findViewById(R.id.btnFinalize);
        String[] spinnerItems = {"1st Year", "2nd Year", "3rd Year"};
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        spnYear.setAdapter(spinnerAdapter);
        spnYear.setSelection(0);
        spnYear.setOnItemSelectedListener(this);

        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        btnSetDate = (Button) findViewById(R.id.btn_set_date);
        btnSetDate.setBackgroundColor(Color.RED);
        btnSetDate.setTextColor(Color.WHITE);
        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = DatePickerDialog.newInstance(SetAttendanceActivity.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(getResources().getColor(R.color.colorMyGreen));
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show(getFragmentManager(), "Date Picker");

            }
        });

        btnFinalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnSetDate.getText().toString().equalsIgnoreCase("SET DATE")){
                    Snackbar.make(rlSetAttendance,"Please Set Date",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < studentsAttendanceAdapter.getItemCount(); i++) {
                    String date = btnSetDate.getText().toString();
                    String aadhaar = studentsAttendanceAdapter.students.get(i).getAadhaar();
                    String fullname = studentsAttendanceAdapter.students.get(i).getFullName();
                    String currentYear = studentsAttendanceAdapter.students.get(i).getCurrentYear();
                    String Presentstate = studentsAttendanceAdapter.students.get(i).isPresent() ? PRESENT : ABSENT;
                    DBUtils.createStudentAttendanceTable(SetAttendanceActivity.this,date);
                    boolean inserted = DBUtils.insertStudentAttendance(SetAttendanceActivity.this, date, aadhaar, fullname, currentYear, Presentstate);
                }
                Snackbar.make(rlSetAttendance,"Attendance Complete!",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        btnSetDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        btnSetDate.setBackgroundColor(getResources().getColor(R.color.colorMyGreen));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                btnFinalize.setEnabled(populateStudentsOfYear(1));
                break;
            case 1:
                btnFinalize.setEnabled(populateStudentsOfYear(2));
                break;
            case 2:
                btnFinalize.setEnabled(populateStudentsOfYear(3));
                break;
        }
    }

    private boolean populateStudentsOfYear(int year) {
        rvStudents.setAdapter(null);
        List<Student> studentList = DBUtils.loadStudentsName(this, year);
        if (studentList == null) {
            return false;
        }
        rvStudents.setLayoutManager(new LinearLayoutManager(this));
        rvStudents.setHasFixedSize(true);
        studentsAttendanceAdapter = new StudentsAttendanceAdapter(studentList);
        rvStudents.setAdapter(studentsAttendanceAdapter);
        return true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
