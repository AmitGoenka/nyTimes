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
import org.agoenka.nytimes.utils.AppUtils;
import org.agoenka.nytimes.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static org.agoenka.nytimes.utils.AppUtils.setSpinnerToValue;
import static org.agoenka.nytimes.utils.DateUtils.FORMAT_MMDDYY;
import static org.agoenka.nytimes.utils.DateUtils.formatDate;
import static org.agoenka.nytimes.utils.DateUtils.getCalendar;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.etBeginDate) EditText etBeginDate;
    @BindView(R.id.spnSortOrder) Spinner spnSortOrder;
    @BindView(R.id.cbArts) CheckBox cbArts;
    @BindView(R.id.cbFashionStyle) CheckBox cbFashionStyle;
    @BindView(R.id.cbSports) CheckBox cbSports;

    Calendar selectedCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.action_settings);
        }

        setupViews();
    }

    private void setupViews() {
        if (getIntent().getSerializableExtra("filter") != null) {
            Filter filter = (Filter) getIntent().getSerializableExtra("filter");

            Date beginDate = filter.getBeginDate();
            String sortOrder = filter.getSortOrder();
            List<String> newsDesks = filter.getNewsDesks();

            if (beginDate != null) {
                etBeginDate.setText(formatDate(beginDate, FORMAT_MMDDYY));
                selectedCalendar = getCalendar(beginDate);
            }
            if (!isEmpty(sortOrder)) {
                setSpinnerToValue(spnSortOrder, sortOrder);
            }
            if (!AppUtils.isEmpty(newsDesks)) {
                Filter.setNewsDesks(this, newsDesks, cbArts, cbFashionStyle, cbSports);
            }
        }
    }

    // attach to an onclick handler to show the date picker
    @OnClick(R.id.etBeginDate)
    public void showDatePickerDialog(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        if (selectedCalendar != null) {
            Bundle args = new Bundle();
            args.putSerializable("selectedCalendar", selectedCalendar);
            newFragment.setArguments(args);
        }
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        selectedCalendar = getCalendar(year, monthOfYear, dayOfMonth);
        // format the date for view
        String formattedDate = DateUtils.formatDate(selectedCalendar, FORMAT_MMDDYY);
        // set the selected date string into the date view
        etBeginDate.setText(formattedDate);
    }

    @OnClick(R.id.btnSave)
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
