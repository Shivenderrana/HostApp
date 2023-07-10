package com.sonu.hostelmanagementsystem;

import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import static com.sonu.hostelmanagementsystem.DBUtils.HOSTEL_DATABASE;
import static com.sonu.hostelmanagementsystem.DBUtils.STUDENTS_TABLE;

public class ImportExportActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnExportStudentsCSV, btnExportAttendanceCSV, btnExportDatabase;
    Button btnImportDatabase;
    Spinner spnMonth;
    EditText edtYear;
    private RelativeLayout rlImportExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_export);

        getSupportActionBar().setTitle("Import/Export");
        rlImportExport = (RelativeLayout) findViewById(R.id.rl_import_export);
        DBUtils.createImportDirectory(this);

        edtYear = (EditText) findViewById(R.id.edtYear);

        spnMonth = (Spinner) findViewById(R.id.spnMonth);
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        spnMonth.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, months));
        spnMonth.setSelection(0);

        btnExportStudentsCSV = (Button) findViewById(R.id.btnExportStudentsCSV);
        btnExportAttendanceCSV = (Button) findViewById(R.id.btnExportAttendanceCSV);
        btnExportDatabase = (Button) findViewById(R.id.btnExportDatabase);

        btnImportDatabase = (Button) findViewById(R.id.btnImportDatabase);

        btnExportStudentsCSV.setOnClickListener(this);
        btnExportAttendanceCSV.setOnClickListener(this);
        btnExportDatabase.setOnClickListener(this);

        btnImportDatabase.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnExportStudentsCSV){
            try{
                SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(HOSTEL_DATABASE, MODE_PRIVATE, null);
                boolean isExportedAsCSV = SqliteExporter.export(sqLiteDatabase, STUDENTS_TABLE, "Students CSV");
                if(isExportedAsCSV)
                    Snackbar.make(rlImportExport,"Students CSV Exported",Snackbar.LENGTH_LONG).show();
                else
                    Snackbar.make(rlImportExport,"CSV Not Exported",Snackbar.LENGTH_LONG).show();

            }catch (Exception e){
                Snackbar.make(rlImportExport,"CSV Not Exported",Snackbar.LENGTH_LONG).show();
            }

        } else if(view.getId() == R.id.btnExportAttendanceCSV){
            try{
                String month = getStringMonth(spnMonth.getSelectedItemPosition());

                if(edtYear.getText().length()<4){
                    edtYear.setError("Enter valid Year");
                    edtYear.requestFocus();
                    return;
                }
                String year = edtYear.getText().toString().trim();
                String tableName = month + "_" + year;

                SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(HOSTEL_DATABASE, MODE_PRIVATE, null);

                boolean isExportedAsCSV = SqliteExporter.export(sqLiteDatabase, tableName, "Attendance CSV");
                if(isExportedAsCSV)
                    Snackbar.make(rlImportExport,"Attendance CSV Exported",Snackbar.LENGTH_LONG).show();
                else
                    Snackbar.make(rlImportExport,"CSV Not Exported",Snackbar.LENGTH_LONG).show();

            }catch (Exception e){
                Snackbar.make(rlImportExport,"CSV Not Exported",Snackbar.LENGTH_LONG).show();
            }
        } else if(view.getId() == R.id.btnExportDatabase){
            boolean isDatabaseExported = DBUtils.exportDatabase(this, "Databases");
            if(isDatabaseExported)
                Snackbar.make(rlImportExport,"Database Exported",Snackbar.LENGTH_LONG).show();
            else
                Snackbar.make(rlImportExport,"Database NOT Exported",Snackbar.LENGTH_LONG).show();
        } else if(view.getId() == R.id.btnImportDatabase){
            boolean isDatabaseImported = DBUtils.importDatabase(this);
            if(isDatabaseImported)
                Snackbar.make(rlImportExport,"Database Imported",Snackbar.LENGTH_LONG).show();
            else
                Snackbar.make(rlImportExport,"Database NOT Imported",Snackbar.LENGTH_LONG).show();
        }
    }

    private String getStringMonth(int month) {
        switch (month) {
            case 0:
                return "january";
            case 1:
                return "february";
            case 2:
                return "march";
            case 3:
                return "april";
            case 4:
                return "may";
            case 5:
                return "june";
            case 6:
                return "july";
            case 7:
                return "august";
            case 8:
                return "september";
            case 9:
                return "october";
            case 10:
                return "november";
            case 11:
                return "december";
        }
        return null;
    }
}
