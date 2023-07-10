package com.sonu.hostelmanagementsystem;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DeleteStudentActivity extends AppCompatActivity {

    EditText edtAadhaar;
    Button btnDelete;
    private RelativeLayout rlDeleteStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_student);

        getSupportActionBar().setTitle("Delete Student");
        rlDeleteStudent = (RelativeLayout) findViewById(R.id.rl_delete_student);

        edtAadhaar = (EditText) findViewById(R.id.edtAadhaarNumber);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtAadhaar.getText().toString().trim().length()<12){
                    edtAadhaar.setError("Enter valid Aadhaar");
                    edtAadhaar.requestFocus();
                    return;
                }

                String aadhaar = edtAadhaar.getText().toString().trim();
                boolean isStudentDeleted = DBUtils.deleteStudent(DeleteStudentActivity.this, aadhaar);
                if(isStudentDeleted) {
                    Toast.makeText(DeleteStudentActivity.this, "Student record deleted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Snackbar.make(rlDeleteStudent,"Cannot find student info",Snackbar.LENGTH_LONG).show();
            }
        });

    }
}
