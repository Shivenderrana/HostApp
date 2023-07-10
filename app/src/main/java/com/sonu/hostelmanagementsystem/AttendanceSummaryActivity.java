package com.sonu.hostelmanagementsystem;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;

public class AttendanceSummaryActivity extends AppCompatActivity {

    String month;
    String year;

    public static final int ATTENDANCE_COLUMN_COUNT = 33;
    private String[] headers = new String[ATTENDANCE_COLUMN_COUNT];
    String[][] studentDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_summary);

        if(getIntent() != null) {
            month = getIntent().getStringExtra("month");
            year = getIntent().getStringExtra("year");
        }
        else finish();

        studentDetails = DBUtils.getStudentAttendance(this,month,year);
        if(studentDetails == null) return;

        final TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.tableView);
        tableView.setColumnCount(ATTENDANCE_COLUMN_COUNT);
        TableColumnWeightModel columnWeightModel = new TableColumnWeightModel(ATTENDANCE_COLUMN_COUNT);
        columnWeightModel.setColumnWeight(0,4);
        columnWeightModel.setColumnWeight(1,2);
        tableView.setColumnModel(columnWeightModel);

        headers[0] = "Name";
        headers[1] = "Year";
        for (int i = 2; i < ATTENDANCE_COLUMN_COUNT; i++) headers[i] = String.valueOf(i-1);

        tableView.setHeaderAdapter(new MyTableHeaderAdapter(this,headers));
        tableView.setDataAdapter(new MyTableDataAdapter(this,studentDetails));

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(studentDetails == null) {
            Toast.makeText(this, "No data for found" , Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private class MyTableDataAdapter extends TableDataAdapter<String[]>{

        private int paddingLeft = 10;
        private int paddingTop = 10;
        private int paddingRight = 10;
        private int paddingBottom = 10;
        private int textSize = 12;
        private int typeface = Typeface.NORMAL;
        private int textColor = 0x99000000;

        public MyTableDataAdapter(Context context, String[][] data) {
            super(context, data);
        }

        @Override
        public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {

            final TextView textView = new TextView(getContext());
            textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            textView.setTypeface(textView.getTypeface(), typeface);
            textView.setTextSize(textSize);
            textView.setTextColor(textColor);
            textView.setSingleLine();
            textView.setEllipsize(TextUtils.TruncateAt.END);

            try {
                final String textToShow = getItem(rowIndex)[columnIndex];
                textView.setText(textToShow);
            } catch (final IndexOutOfBoundsException e) {
            }

            return textView;
        }
    }

    private class MyTableHeaderAdapter extends TableHeaderAdapter{

        private final String[] headers;
        private int paddingLeft = 15;
        private int paddingTop = 20;
        private int paddingRight = 15;
        private int paddingBottom = 20;
        private int textSize = 14;
        private int typeface = Typeface.BOLD;
        private int textColor = Color.BLACK;

        public MyTableHeaderAdapter(Context context, String[] headers) {
            super(context);
            this.headers = headers;
        }

        @Override
        public View getHeaderView(int columnIndex, ViewGroup parentView) {
            final TextView textView = new TextView(getContext());

            if (columnIndex < headers.length) {
                textView.setText(headers[columnIndex]);
            }

            textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            textView.setTypeface(textView.getTypeface(), typeface);
            textView.setTextSize(textSize);
            textView.setTextColor(textColor);
            textView.setSingleLine();
            //textView.setEllipsize(TextUtils.TruncateAt.END);

            return textView;
        }
    }

}
