package fi.routr.dto;

import fi.sandman.navici.Location;
import fi.sandman.navici.client.NaviciClient;
import fi.sandman.navici.dto.response.locationResponse.JsonLocationResponse;

/**
 * Just a POJO that holds the required information for doing a location search
 * with {@link NaviciClient}.
 * 
 * @author Jouni Latvatalo<jouni.latvatalo@gmail.com>
 * 
 */
public class LocationSearchDTO {

	private JsonLocationResponse response;
	private Location searchLocation;

	private String searchTerm;

	public LocationSearchDTO(Location searchLocation, String searchTerm) {
		this.searchLocation = searchLocation;
		this.searchTerm = searchTerm;
	}

	public JsonLocationResponse getResponse() {
		return response;
	}

	/**
	 * Whether the location is "From" or "To"
	 * 
	 * @return
	 */
	public Location getSearchLocation() {
		return searchLocation;
	}

	/**
	 * 
	 * @return
	 */
	public String getSearchTerm() {
		return searchTerm;
	}

	public void setResponse(JsonLocationResponse response) {
		this.response = response;
	}

	/**
	 * Whether the location is "From" or "To"
	 * 
	 * @param searchLocation
	 */
	public void setSearchLocation(Location searchLocation) {
		this.searchLocation = searchLocation;
	}

	/**
	 * 
	 * @param searchTerm
	 */
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

}
