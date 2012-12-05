package fi.routr.fragment;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

/**
 * A dialog fragment for displaying a date picker.
 * 
 * @author Jouni Latvatalo<jouni.latvatalo@gmail.com>
 * 
 */
public abstract class DateDialogFragment extends DialogFragment {

	private Calendar sDate = Calendar.getInstance();

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		return new DatePickerDialog(getActivity(), dateSetListener,
				sDate.get(Calendar.YEAR), sDate.get(Calendar.MONTH),
				sDate.get(Calendar.DAY_OF_MONTH));

	}

	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			Calendar newDate = Calendar.getInstance();
			newDate.set(year, monthOfYear, dayOfMonth);
			dateDialogFragmentDateSet(newDate);

		}
	};

	public abstract void dateDialogFragmentDateSet(Calendar date);

}
