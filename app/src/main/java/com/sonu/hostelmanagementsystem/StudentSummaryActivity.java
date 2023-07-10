package com.sonu.hostelmanagementsystem;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StudentSummaryActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spnMonth;
    EditText edtAadhaar, edtYear;
    Button btnShow;
    TextView txtName, txtAcademicYear, txtTotalPresent, txtTotalClasses, txtAttendancePercentage;
    String[] months;
    private RelativeLayout rlStudentSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_summary);

        getSupportActionBar().setTitle("Student Summary");
        rlStudentSummary = (RelativeLayout) findViewById(R.id.rl_student_summary);

        //Initializations
        edtYear = (EditText) findViewById(R.id.edtYear);
        spnMonth = (Spinner) findViewById(R.id.spnMonth);
        months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        spnMonth.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, months));
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM", Locale.getDefault());
        int month = Integer.parseInt(dateFormat.format(date)) - 1;
        spnMonth.setSelection(month);
        dateFormat = new SimpleDateFormat("yyyy",Locale.getDefault());
        String year = dateFormat.format(date);
        edtYear.setText(year);

        edtAadhaar = (EditText) findViewById(R.id.edt_aadhaar_no);
        btnShow = (Button) findViewById(R.id.btn_show);
        btnShow.setOnClickListener(this);

        txtName = (TextView) findViewById(R.id.txt_name);
        txtAcademicYear = (TextView) findViewById(R.id.txt_academic_year);
        txtTotalPresent = (TextView) findViewById(R.id.txt_total_days_present);
        txtTotalClasses = (TextView) findViewById(R.id.txt_total_classes);
        txtAttendancePercentage = (TextView) findViewById(R.id.txt_attendance_percentage);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_show) {
            resetFields();
            String month = months[spnMonth.getSelectedItemPosition()].toLowerCase();
            String year = edtYear.getText().toString().trim();
            if(year.length()<4){
                edtYear.setError("Enter valid Year");
                edtYear.requestFocus();
                return;
            }
            String aadhaar = edtAadhaar.getText().toString().trim();
            if(aadhaar.length()<12){
                edtAadhaar.setError("Enter valid Aadhaar");
                edtAadhaar.requestFocus();
                return;
            }
            MyUtils.hideKeyBoard(this);
            StudentSummary summary = DBUtils.getStudentSummary(this, month, year, aadhaar);
            if (summary == null) {
                Snackbar.make(rlStudentSummary,"No data found",Snackbar.LENGTH_LONG).show();
                return;
            }
            txtName.setText("Name: " + summary.getStudentName());
            txtAcademicYear.setText("Academic Year: " + summary.getStudentAcademicYear());
            txtTotalPresent.setText("Days Present: " + summary.getTotal_days_present());
            txtTotalClasses.setText("Total Days: " + summary.getTotal_days());
            txtAttendancePercentage.setText("Atendance (%): " + summary.getStudentAttendancePercentage());
        }
    }

    private void resetFields() {
        txtName.setText("Name: -");
        txtAcademicYear.setText("Academic Year: -");
        txtTotalPresent.setText("Total Days Present: -");
        txtTotalClasses.setText("Total Classes: - ");
        txtAttendancePercentage.setText("Attendance (%): -");
    }
}
