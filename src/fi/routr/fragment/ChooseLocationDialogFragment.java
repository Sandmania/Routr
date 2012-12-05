package fi.routr.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import fi.routr.dto.LocationSearchDTO;
import fi.routr.tasks.async.LocationRequestTask;
import fi.sandman.navici.dto.request.routeRequest.Location;

/**
 * A dialog for choosing a location from the {@link LocationRequestTask} search
 * results.
 * 
 * @author Jouni Latvatalo<jouni.latvatalo@gmail.com>
 * 
 */
public abstract class ChooseLocationDialogFragment extends DialogFragment {

	private LocationSearchDTO locationSearchDto;

	public LocationSearchDTO getLocationSearchDto() {
		return locationSearchDto;
	}

	private CharSequence[] getSelectableLocations() {
		List<CharSequence> items = new ArrayList<CharSequence>();
		for (Location loc : locationSearchDto.getResponse().getData()
				.getLocations()) {
			items.add(loc.toString());
		}
		return items.toArray(new CharSequence[0]);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setItems(getSelectableLocations(),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Location chosenLocation = locationSearchDto
								.getResponse().getData().getLocations()
								.get(which);
						selectLocation(chosenLocation, locationSearchDto);
					}
				});
		return builder.create();
	}

	/**
	 * What to do with the chosen location
	 * 
	 * @param chosenLocation
	 * @param locationSearchDto
	 */
	public abstract void selectLocation(Location chosenLocation,
			LocationSearchDTO locationSearchDto);

	public void setLocationSearchDto(LocationSearchDTO locationSearchDto) {
		this.locationSearchDto = locationSearchDto;
	}
}
