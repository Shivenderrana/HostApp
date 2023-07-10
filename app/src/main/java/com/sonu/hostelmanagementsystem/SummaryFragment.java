package com.sonu.hostelmanagementsystem;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryFragment extends Fragment {


    public SummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnStudentSummary = (Button) view.findViewById(R.id.btn_stu_summary);
        Button btnViewAttendanceSummary = (Button) view.findViewById(R.id.btn_view_attendance_summary);
        final Spinner spnMonth = (Spinner) view.findViewById(R.id.spnMonth);
        final EditText edtYear = (EditText) view.findViewById(R.id.edtYear);

        final String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        spnMonth.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, months));
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM", Locale.getDefault());
        int month = Integer.parseInt(dateFormat.format(date)) - 1;
        spnMonth.setSelection(month);
        dateFormat = new SimpleDateFormat("yyyy",Locale.getDefault());
        String year = dateFormat.format(date);
        edtYear.setText(year);

        btnStudentSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),StudentSummaryActivity.class));
            }
        });

        btnViewAttendanceSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtYear.getText().length()<4){
                    edtYear.setError("Enter valid Year");
                    edtYear.requestFocus();
                    return;
                }

                Intent intent = new Intent(view.getContext(),AttendanceSummaryActivity.class);
                intent.putExtra("month",months[spnMonth.getSelectedItemPosition()].toLowerCase());
                intent.putExtra("year", edtYear.getText().toString().trim());
                startActivity(intent);
            }
        });
    }

}
