package beccalee.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import beccalee.nytimessearch.R;

public class SearchFilter extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText date;
    Spinner spinner;
    CheckBox arts;
    CheckBox fashion;
    CheckBox health;
    CheckBox sports;

    Filters filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        filters = (Filters) getIntent().getSerializableExtra("filters");

        setupViews();
    }

    public void setupViews(){
        date = (EditText) findViewById(R.id.etDate);
        spinner = (Spinner) findViewById(R.id.spinner);
        arts = (CheckBox) findViewById(R.id.cbArts);
        fashion = (CheckBox) findViewById(R.id.cbFashion);
        health = (CheckBox) findViewById(R.id.cbHealth);
        sports = (CheckBox) findViewById(R.id.cbSports);

        arts.setChecked(filters.arts);
        fashion.setChecked(filters.fashion);
        health.setChecked(filters.health);
        sports.setChecked(filters.sports);

        date.setText(filters.beginDate);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.sort_order, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        setupDate();
    }

    public void setupDate(){
        final Calendar c = Calendar.getInstance();

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Show current date in the date picker
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog picker = new DatePickerDialog(SearchFilter.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // store the values selected into a Calendar instance
                        c.set(Calendar.YEAR, selectedYear);
                        c.set(Calendar.MONTH, selectedMonth);
                        c.set(Calendar.DAY_OF_MONTH, selectedDay);

                        String dateFormat = "MM/dd/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
                        date.setText(sdf.format(c.getTime()));

                        String dateFormat2 = "yyyyMMdd";
                        SimpleDateFormat urlDate = new SimpleDateFormat(dateFormat2, Locale.US);
                        filters.beginDate = urlDate.format(c.getTime());
                    }
                },year, month, day);
                picker.setTitle("Select date");
                picker.show();
            }
        });
    }

    public void onSubmit(View view){
        Intent info = new Intent();
        info.putExtra("filters", filters);
        setResult(RESULT_OK, info);
        finish();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        filters.sort = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.cbArts:
                if (checked) { filters.arts = true; }
                else { filters.arts = false; }
                break;
            case R.id.cbFashion:
                if (checked) { filters.fashion = true; }
                else { filters.fashion = false; }
                break;
            case R.id.cbHealth:
                if (checked) { filters.health = true; }
                else { filters.health = false; }
                break;
            case R.id.cbSports:
                if (checked) { filters.sports = true; }
                else { filters.sports = false; }
                break;        }
    }

}
