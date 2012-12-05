package fi.routr;

import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import fi.routr.dto.LocationSearchDTO;
import fi.routr.fragment.ChooseLocationDialogFragment;
import fi.routr.fragment.DateDialogFragment;
import fi.routr.fragment.TimeDialogFragment;
import fi.routr.tasks.async.LocationRequestTask;
import fi.sandman.navici.dto.request.routeRequest.Location;

/**
 * Main activity window that displays the search screen.
 * 
 * @author Jouni Latvatalo<jouni.latvatalo@gmail.com>
 * 
 */
@EActivity(R.layout.activity_main)
public class RoutrActivity extends FragmentActivity {

	@App
	RoutrApplication app;

	@ViewById(R.id.date)
	TextView dateText;

	/*
	 * The "From" location received from navici server after doing a location
	 * request
	 */
	private Location fromLocation;

	LocationRequestTask fromLocationRequestTask;

	@ViewById
	EditText fromSearchTerm;

	@ViewById(R.id.progressBar)
	ProgressBar progressBar;

	@ViewById(R.id.time)
	TextView timeText;

	/*
	 * The "to" location received from navici server after doing a location
	 * request
	 */
	private Location toLocation;

	LocationRequestTask toLocationRequestTask;

	@ViewById
	EditText toSearchTerm;

	@AfterViews
	void afterViews() {
	}

	/**
	 * 
	 * @param result
	 */
	public void chooseLocation(LocationSearchDTO result) {
		ChooseLocationDialogFragment cldf = new ChooseLocationDialogFragment() {

			@Override
			public void selectLocation(Location chosenLocation,
					LocationSearchDTO locationSearchDto) {
				if (fi.sandman.navici.Location.FROM.equals(locationSearchDto
						.getSearchLocation())) {
					getFromSearchTerm().setText(chosenLocation.toString());
				} else if (fi.sandman.navici.Location.TO
						.equals(locationSearchDto.getSearchLocation())) {
					getToSearchTerm().setText(chosenLocation.toString());
				}
			}
		};
		cldf.setLocationSearchDto(result);
		cldf.show(getSupportFragmentManager(), "Pick location");
	}

	/**
	 * Does "From" location search
	 */
	private void doFromLocationSearch() {
		// Check that we really have a value in textfield
		if (fromLocation == null && fromSearchTerm != null
				&& fromSearchTerm.getText().length() > 0) {

			// Initialize search criteria
			LocationSearchDTO fromLocationSearchDTO = new LocationSearchDTO(
					fi.sandman.navici.Location.FROM, fromSearchTerm.getText()
							.toString().trim());

			fromLocationRequestTask.connect(this);
			// Do search
			if (fromLocationRequestTask.isPending()) {
				fromLocationRequestTask.execute(fromLocationSearchDTO);
			}
		}
	}

	/**
	 * Does "To" location search
	 */
	private void doToLocationSearch() {
		// Check that we really have a value in textfield
		if (toLocation == null && toSearchTerm != null
				&& toSearchTerm.getText().length() > 0) {

			// Initialize search criteria
			LocationSearchDTO toLocationSearchDTO = new LocationSearchDTO(
					fi.sandman.navici.Location.TO, toSearchTerm.getText()
							.toString().trim());

			toLocationRequestTask.connect(this);
			// Do search
			if (toLocationRequestTask.isPending()) {
				toLocationRequestTask.execute(toLocationSearchDTO);
			}
		}
	}

	public Location getFromLocation() {
		return fromLocation;
	}

	public EditText getFromSearchTerm() {
		return fromSearchTerm;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public Location getToLocation() {
		return toLocation;
	}

	public EditText getToSearchTerm() {
		return toSearchTerm;
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		fromLocationRequestTask = new LocationRequestTask();
		fromLocationRequestTask.setRoutrApplication(app);
		toLocationRequestTask = new LocationRequestTask();
		toLocationRequestTask.setRoutrApplication(app);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// always disconnect the activity from the task here, in order to not
		// risk leaking a context reference
		fromLocationRequestTask.disconnect();
	}

	/**
	 * Resets the {@link LocationRequestTask}
	 * 
	 * @param location
	 */
	public void resetTask(fi.sandman.navici.Location location) {
		Log.d(getClass().getCanonicalName(), "Reset task " + location);
		if (fi.sandman.navici.Location.FROM.equals(location)) {
			fromLocationRequestTask = new LocationRequestTask();
			fromLocationRequestTask.setRoutrApplication(app);
		} else if (fi.sandman.navici.Location.TO.equals(location)) {
			toLocationRequestTask = new LocationRequestTask();
			toLocationRequestTask.setRoutrApplication(app);
		}
	}

	public void setFromLocation(Location fromLocation) {
		this.fromLocation = fromLocation;
	}

	public void setFromSearchTerm(EditText fromSearchTerm) {
		this.fromSearchTerm = fromSearchTerm;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public void setToLocation(Location toLocation) {
		this.toLocation = toLocation;
	}

	public void setToSearchTerm(EditText toSearchTerm) {
		this.toSearchTerm = toSearchTerm;
	}

	/**
	 * Shows the date picker dialog when pick date button is clicked
	 */
	@Click(R.id.pickDate)
	public void showDatePickerDialog() {
		DateDialogFragment ddf = new DateDialogFragment() {

			/**
			 * Updates UI date TextView with the given date
			 * 
			 * @param date
			 */
			@Override
			public void dateDialogFragmentDateSet(Calendar date) {
				dateText.setText(date.get(Calendar.DAY_OF_MONTH) + "."
						+ (date.get(Calendar.MONTH) + 1) + "."
						+ date.get(Calendar.YEAR));
			}
		};

		ddf.show(getSupportFragmentManager(), "Pick date");
	}

	/**
	 * Shows the time picker dialog when pick time button is clicked
	 */
	@Click(R.id.pickTime)
	public void showTimePickerDialog() {
		TimeDialogFragment tdf = new TimeDialogFragment() {

			/**
			 * Updates UI time TextView with the given time
			 */
			@Override
			public void timeDialogFragmentTimeSet(Calendar time) {
				timeText.setText(time.get(Calendar.HOUR_OF_DAY) + "."
						+ time.get(Calendar.MINUTE));
			}
		};

		tdf.show(getSupportFragmentManager(), "Pick time");
	}

	/**
	 * Starts a location search in background.
	 */
	@Click(R.id.searchButton)
	public void startLocationSearch() {
		doFromLocationSearch();
		doToLocationSearch();
	}
}
