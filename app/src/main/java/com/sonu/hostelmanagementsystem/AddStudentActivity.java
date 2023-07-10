package com.sonu.hostelmanagementsystem;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class AddStudentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    Button btnDOB, btnSubmit;
    EditText edtAadhaar, edtFullName, edtRoomNumber, edtFatherName, edtMotherName, edtAddress, edtContact,edtFatherContact, edtRelativeContact;
    Spinner spnBranch, spnCurrentYear;
    RadioButton radMale, radFemale;
    private DatePickerDialog datePickerDialog;
    int Year, Month, Day;
    private Calendar calendar;
    private RelativeLayout rlAddStudent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        getSupportActionBar().setTitle("Add Student");

        //Initializations
        rlAddStudent = (RelativeLayout) findViewById(R.id.rl_add_student);
        spnCurrentYear = (Spinner) findViewById(R.id.spnCurrentYear);
        spnCurrentYear.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"1st Year", "2nd Year", "3rd Year"}));
        spnCurrentYear.setSelection(0);
        edtAadhaar = (EditText) findViewById(R.id.edtAadhaarNumber);
        edtFullName = (EditText) findViewById(R.id.edtFullname);
        spnBranch = (Spinner) findViewById(R.id.spn_branch);
        spnBranch.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"CSE", "IT", "CIVIL", "ELEX", "PHARMACY", "PGDCA", "MOMSP"}));
        spnBranch.setSelection(0);
        edtRoomNumber = (EditText) findViewById(R.id.edtRoomNumber);
        edtFatherName = (EditText) findViewById(R.id.edtFatherName);
        edtMotherName = (EditText) findViewById(R.id.edtMotherName);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        radMale = (RadioButton) findViewById(R.id.radMale);
        radFemale = (RadioButton) findViewById(R.id.radFemale);
        edtContact = (EditText) findViewById(R.id.edtContact);
        edtFatherContact = (EditText) findViewById(R.id.edtFatherContact);
        edtRelativeContact = (EditText) findViewById(R.id.edtRelativeContact);

        btnDOB = (Button) findViewById(R.id.btnDOB);
        btnDOB.setBackgroundColor(Color.RED);
        btnDOB.setTextColor(Color.WHITE);
        btnDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                Year = calendar.get(Calendar.YEAR) - 18;
                Month = calendar.get(Calendar.MONTH);
                Day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = DatePickerDialog.newInstance(AddStudentActivity.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(getResources().getColor(R.color.colorMyGreen));
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show(getFragmentManager(), "Date Picker");
            }
        });

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!performValidation()) return;

                String aadhaar = edtAadhaar.getText().toString().trim();
                String fullName = edtFullName.getText().toString().trim();
                String branch = getBranch();
                String year = getCurrentYear();
                String room = edtRoomNumber.getText().toString().trim();
                String gender = getGender();
                String dob = btnDOB.getText().toString().equalsIgnoreCase("DATE OF BIRTH") ? "-" : btnDOB.getText().toString();
                String father = edtFatherName.getText().toString().trim();
                String mother = edtMotherName.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();
                String contact = edtContact.getText().toString().trim();
                String fatherContact = edtFatherContact.getText().toString().trim();
                String relContact = edtRelativeContact.getText().toString().trim();

                boolean isTableCreated = DBUtils.createStudentsTable(AddStudentActivity.this);
                if (isTableCreated) {
                    boolean isStudentInserted = DBUtils.insertStudent(AddStudentActivity.this, new String[]{aadhaar, fullName, branch, year, room, gender, dob, father, mother, address, contact, fatherContact, relContact});
                    if (isStudentInserted) {
                        Toast.makeText(AddStudentActivity.this, "Student data inserted Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Snackbar.make(rlAddStudent,"Student data NOT inserted!",Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(rlAddStudent,"Error in Creating Database",Snackbar.LENGTH_LONG).show();
                }
            }
        });


    }

    private boolean performValidation() {

        if(edtAadhaar.getText().toString().trim().length()<12){
            edtAadhaar.setError("Enter valid Aadhaar ID");
            edtAadhaar.requestFocus();
            return false;
        }
        if(edtFullName.getText().toString().trim().length()<1)
        {
            edtFullName.setError("Name Required");
            edtFullName.requestFocus();
            return false;
        }
        if(edtFatherName.getText().toString().trim().length()<1){
            edtFatherName.setError("Father Name Required");
            edtFatherName.requestFocus();
            return false;
        }
        if(edtMotherName.getText().toString().trim().length()<1){
            edtMotherName.setError("Mother Name Required");
            edtMotherName.requestFocus();
            return false;
        }
        if(edtAddress.getText().toString().trim().length()<1){
            edtAddress.setError("Address Required");
            edtAddress.requestFocus();
            return false;
        }
        if(btnDOB.getText().toString().trim().equalsIgnoreCase("DATE OF BIRTH")){
            Snackbar.make(rlAddStudent,"Date of Birth Required",Snackbar.LENGTH_LONG).show();
            btnDOB.requestFocus();
            return false;
        }
        if(edtContact.getText().toString().trim().length()<10){
            edtContact.setError("Enter valid Contact");
            edtContact.requestFocus();
            return false;
        }
        if(edtFatherContact.getText().toString().trim().length()<10){
            edtFatherContact.setError("Enter valid Father Contact");
            edtFatherContact.requestFocus();
            return false;
        }
        if(edtRelativeContact.getText().toString().trim().length()<10){
            edtRelativeContact.setError("Enter valid Relative Contact");
            edtRelativeContact.requestFocus();
            return false;
        }
        return true;
    }

    private String getBranch() {
        switch (spnBranch.getSelectedItemPosition()){
            case 0:
                return "CSE";
            case 1:
                return "IT";
            case 2:
                return "CIVIL";
            case 3:
                return "ELEX";
            case 4:
                return "PHARMACY";
            case 5:
                return "PGDCA";
            case 6:
                return "MOMSP";
        }
        return null;
    }

    private String getCurrentYear() {
        switch (spnCurrentYear.getSelectedItemPosition()) {
            case 0:
                return "1";
            case 1:
                return "2";
            case 2:
                return "3";
        }
        return "0";
    }

    private String getGender() {
        if (radMale.isChecked() && !radFemale.isChecked()) {
            return MALE;
        } else if (!radMale.isChecked() && radFemale.isChecked()) {
            return FEMALE;
        } else return "-";
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        btnDOB.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        btnDOB.setBackgroundColor(getResources().getColor(R.color.colorMyGreen));
    }
}
