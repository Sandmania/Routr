package fi.routr.fragment;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

/**
 * <p>
 * A dialog fragment for displaying a time picker.
 * </p>
 * 
 * @author Jouni Latvatalo<jouni.latvatalo@gmail.com>
 * 
 */
public abstract class TimeDialogFragment extends DialogFragment {

	private Calendar sTime = Calendar.getInstance();

	private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			Calendar newTime = Calendar.getInstance();
			newTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			newTime.set(Calendar.MINUTE, minute);
			timeDialogFragmentTimeSet(newTime);
		}
	};

	/**
	 * Override to set time
	 * 
	 * @param time
	 */
	public abstract void timeDialogFragmentTimeSet(Calendar time);

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int hour = sTime.get(Calendar.HOUR_OF_DAY);
		int minute = sTime.get(Calendar.MINUTE);
		return new TimePickerDialog(getActivity(), timeSetListener, hour,
				minute, DateFormat.is24HourFormat(getActivity()));
	}

	public interface TimeDialogFragmentListener {
		public void timeDialogFragmentTimeSet(Calendar time);
	}

	public interface TimeDetailFragmentButtonListener {
		public void onSetTimeButtonClicked(Calendar time);
	}

}
