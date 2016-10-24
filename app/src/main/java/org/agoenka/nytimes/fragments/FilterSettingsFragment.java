package org.agoenka.nytimes.fragments;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import org.agoenka.nytimes.R;
import org.agoenka.nytimes.databinding.FragmentFilterSettingsBinding;
import org.agoenka.nytimes.models.Filter;
import org.agoenka.nytimes.utils.AppUtils;
import org.agoenka.nytimes.utils.DateUtils;
import org.parceler.Parcels;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.text.TextUtils.isEmpty;
import static org.agoenka.nytimes.utils.AppUtils.setSpinnerToValue;
import static org.agoenka.nytimes.utils.DateUtils.FORMAT_MMDDYY;
import static org.agoenka.nytimes.utils.DateUtils.formatDate;
import static org.agoenka.nytimes.utils.DateUtils.getCalendar;

public class FilterSettingsFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private FragmentFilterSettingsBinding binding;

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

    public static FilterSettingsFragment newInstance(String title, Filter filter) {
        FilterSettingsFragment fragment = new FilterSettingsFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        if (filter != null) args.putParcelable(KEY_FILTER, Parcels.wrap(filter));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter_settings, container, false);
        binding.setHandlers(new Handlers());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String title = getArguments().getString(KEY_TITLE);
        getDialog().setTitle(title);

        if (getArguments().getParcelable(KEY_FILTER) != null) {
            filter = Parcels.unwrap(getArguments().getParcelable(KEY_FILTER));
            assert filter != null;

            Date beginDate = filter.getBeginDate();
            String sortOrder = filter.getSortOrder();
            List<String> newsDesks = filter.getNewsDesks();

            if (beginDate != null) {
                binding.etBeginDate.setText(formatDate(beginDate, FORMAT_MMDDYY));
                selectedCalendar = getCalendar(beginDate);
            }
            if (!isEmpty(sortOrder)) {
                setSpinnerToValue(binding.spnSortOrder, sortOrder);
            }
            if (!AppUtils.isEmpty(newsDesks)) {
                Filter.setNewsDesks(getContext(), newsDesks, binding.cbArts, binding.cbFashionStyle, binding.cbSports);
            }
        }
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        selectedCalendar = getCalendar(year, monthOfYear, dayOfMonth);
        // format the date for view
        String formattedDate = DateUtils.formatDate(selectedCalendar, FORMAT_MMDDYY);
        // set the selected date string into the date view
        binding.etBeginDate.setText(formattedDate);
    }

    public class Handlers {

        // attach to an onclick handler to show the date picker
        public void showDatePickerDialog(@SuppressWarnings("unused") View view) {
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

        public void onSave(@SuppressWarnings("unused") View view) {
            String beginDate = binding.etBeginDate.getText().toString();
            String sortOrder = binding.spnSortOrder.getSelectedItem().toString();
            List<String> newsDesks = Filter.getNewsDesks(getContext(), binding.cbArts, binding.cbFashionStyle, binding.cbSports);
            filter = new Filter(beginDate, sortOrder, newsDesks);

            if (listener != null) {
                listener.onSave(filter);
            }

            filter = null;
            selectedCalendar = null;
            dismiss();
        }

    }

}
