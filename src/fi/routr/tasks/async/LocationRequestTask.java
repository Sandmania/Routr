package fi.routr.tasks.async;

import java.security.InvalidParameterException;
import java.util.Arrays;

import android.util.Log;
import android.view.View;

import com.github.ignition.core.tasks.IgnitedAsyncTask;
import com.googlecode.androidannotations.annotations.App;

import fi.routr.RoutrActivity;
import fi.routr.RoutrApplication;
import fi.routr.dto.LocationSearchDTO;
import fi.sandman.navici.Location;
import fi.sandman.navici.client.NaviciClient;
import fi.sandman.navici.dto.response.locationResponse.JsonLocationResponse;

/**
 * An asynchronous task for doing location requests with {@link NaviciClient}.
 * 
 * 
 * @author Jouni Latvatalo<jouni.latvatalo@gmail.com>
 * 
 */
public class LocationRequestTask
		extends
		IgnitedAsyncTask<RoutrActivity, LocationSearchDTO, Void, LocationSearchDTO> {

	Location location;

	private RoutrApplication routrApplication;

	/**
	 * Returns true if given {@link JsonLocationResponse} parameter contains
	 * locations.
	 * 
	 * @param {@link JsonLocationResponse} result
	 * @return
	 */
	public boolean foundLocations(JsonLocationResponse result) {
		return (result != null && result.getData() != null && result.getData()
				.getLocations() != null);
	}

	public RoutrApplication getRoutrApplication() {
		return routrApplication;
	}

	@Override
	public boolean onTaskCompleted(RoutrActivity context,
			LocationSearchDTO result) {
		Log.d(getClass().getName(), "onTaskCompleted " + location);
		context.getProgressBar().setVisibility(View.GONE);
		context.resetTask(location);
		return true;
	}

	@Override
	public boolean onTaskFailed(RoutrActivity context, Exception error) {
		Log.d(getClass().getName(), "onTaskFailed " + error);
		error.printStackTrace();
		return true;
	}

	@Override
	public boolean onTaskStarted(RoutrActivity context) {
		Log.d(getClass().getName(), "onTaskStarted");
		context.getProgressBar().setVisibility(View.VISIBLE);
		return true;
	}

	@Override
	public boolean onTaskSuccess(RoutrActivity context, LocationSearchDTO result) {
		Log.d(getClass().getName(), "onTaskSuccess " + result
				+ " found locations? " + foundLocations(result.getResponse()));
		context.chooseLocation(result);
		return true;
	}

	/**
	 * <p>
	 * {@link NaviciClient} needs to be in the first index of parameter list.
	 * </p>
	 * <p>
	 * As Android annotations is unable to inject {@link RoutrApplication} with
	 * {@link App}, we need to give it as a parameter.
	 * </p>
	 */
	@Override
	public LocationSearchDTO run(LocationSearchDTO... params) throws Exception {

		if (params == null || params.length == 0
				|| !(params[0] instanceof LocationSearchDTO)) {
			throw new InvalidParameterException(
					"LocationSearchDTO not given as parameter. Expected instance of LocationSearchDTO at params[0]");

		}

		LocationSearchDTO locationSearchDto = params[0];
		this.location = locationSearchDto.getSearchLocation();

		Log.d(getClass().getName(), "run " + Arrays.toString(params)
				+ " naviciClient instance "
				+ getRoutrApplication().getNaviciClient() + " search term "
				+ locationSearchDto.getSearchTerm());

		JsonLocationResponse response = getRoutrApplication().getNaviciClient()
				.sendRequest(locationSearchDto.getSearchTerm());
		locationSearchDto.setResponse(response);

		return locationSearchDto;
	}

	public void setRoutrApplication(RoutrApplication routrApplication) {
		this.routrApplication = routrApplication;
	}
}
