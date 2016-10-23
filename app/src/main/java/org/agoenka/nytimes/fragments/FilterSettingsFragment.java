package org.agoenka.nytimes.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.agoenka.nytimes.R;
import org.agoenka.nytimes.models.Filter;
import org.agoenka.nytimes.utils.AppUtils;
import org.agoenka.nytimes.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.text.TextUtils.isEmpty;
import static org.agoenka.nytimes.utils.AppUtils.setSpinnerToValue;
import static org.agoenka.nytimes.utils.DateUtils.FORMAT_MMDDYY;
import static org.agoenka.nytimes.utils.DateUtils.formatDate;
import static org.agoenka.nytimes.utils.DateUtils.getCalendar;

public class FilterSettingsFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.etBeginDate) EditText etBeginDate;
    @BindView(R.id.spnSortOrder) Spinner spnSortOrder;
    @BindView(R.id.cbArts) CheckBox cbArts;
    @BindView(R.id.cbFashionStyle) CheckBox cbFashionStyle;
    @BindView(R.id.cbSports) CheckBox cbSports;
    private Unbinder unbinder;

    Filter filter;
    Calendar selectedCalendar;

    public static final String KEY_TITLE = "title";
    public static final String KEY_FILTER = "filter";

    public interface FilterSettingsDialogListener {
        void onSave(Filter filter);
    }

    private FilterSettingsDialogListener listener;

    public void setFilterSettingsDialogListener(FilterSettingsDialogListener listener) {
        this.listener = listener;
    }

    public FilterSettingsFragment() {
        // Required empty public constructor
    }

    public static FilterSettingsFragment newInstance(String title, Filter filter) {
        FilterSettingsFragment fragment = new FilterSettingsFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        if (filter != null) args.putSerializable(KEY_FILTER, filter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String title = getArguments().getString(KEY_TITLE);
        getDialog().setTitle(title);

        if (getArguments().getSerializable(KEY_FILTER) != null) {
            filter = (Filter) getArguments().getSerializable(KEY_FILTER);
            assert filter != null;

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
                Filter.setNewsDesks(getContext(), newsDesks, cbArts, cbFashionStyle, cbSports);
            }
        }
    }

    // attach to an onclick handler to show the date picker
    @OnClick(R.id.etBeginDate)
    public void showDatePickerDialog() {
        FragmentManager fm = getFragmentManager();
        DatePickerFragment pickerFragment = new DatePickerFragment();
        if (selectedCalendar != null) {
            Bundle args = new Bundle();
            args.putSerializable("selectedCalendar", selectedCalendar);
            pickerFragment.setArguments(args);
        }
        pickerFragment.setTargetFragment(FilterSettingsFragment.this, 100);
        pickerFragment.show(fm, "Date Picker");
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
    public void onSave() {
        String beginDate = etBeginDate.getText().toString();
        String sortOrder = spnSortOrder.getSelectedItem().toString();
        List<String> newsDesks = Filter.getNewsDesks(getContext(), cbArts, cbFashionStyle, cbSports);
        filter = new Filter(beginDate, sortOrder, newsDesks);

        if (listener != null) {
            listener.onSave(filter);
        }

        filter = null;
        selectedCalendar = null;
        dismiss();
    }

    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
