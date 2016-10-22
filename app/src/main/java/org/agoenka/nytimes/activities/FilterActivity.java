package org.agoenka.nytimes.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.agoenka.nytimes.R;
import org.agoenka.nytimes.fragments.DatePickerFragment;
import org.agoenka.nytimes.models.Filter;
import org.agoenka.nytimes.utils.DateUtils;

import java.util.Calendar;
import java.util.List;

import static org.agoenka.nytimes.utils.DateUtils.FORMAT_MMDDYY;
import static org.agoenka.nytimes.utils.DateUtils.getCalendar;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText etBeginDate;
    Spinner spnSortOrder;
    CheckBox cbArts;
    CheckBox cbFashionStyle;
    CheckBox cbSports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.action_settings);
        }

        setupViews();
    }

    private void setupViews () {
        etBeginDate = (EditText) findViewById(R.id.etBeginDate);
        spnSortOrder = (Spinner) findViewById(R.id.spnSortOrder);
        cbArts = (CheckBox) findViewById(R.id.cbArts);
        cbFashionStyle = (CheckBox) findViewById(R.id.cbFashionStyle);
        cbSports = (CheckBox) findViewById(R.id.cbSports);
    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar calendar = getCalendar(year, monthOfYear, dayOfMonth);
        // format the date for view
        String formattedDate = DateUtils.formatDate(calendar, FORMAT_MMDDYY);
        // ge the date field view
        etBeginDate = (EditText) findViewById(R.id.etBeginDate);
        // set the selected date string into the date view
        etBeginDate.setText(formattedDate);
    }

    public void onSave(View view) {
        String beginDate = etBeginDate.getText().toString();
        String sortOrder = spnSortOrder.getSelectedItem().toString();
        List<String> newsDesks = Filter.getNewsDesks(this, cbArts, cbFashionStyle, cbSports);
        Filter filter = new Filter(beginDate, sortOrder, newsDesks);
        Intent intent = new Intent();
        intent.putExtra("filter", filter);
        setResult(RESULT_OK, intent);
        finish();
    }
}
