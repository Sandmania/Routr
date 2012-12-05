
package fi.routr;

import android.app.Application;
import android.util.Log;

import com.googlecode.androidannotations.annotations.EApplication;

import fi.sandman.navici.client.City;
import fi.sandman.navici.client.NaviciClient;

/**
 * 
 * Application base class.
 * 
 * @author Jouni Latvatalo<jouni.latvatalo@gmail.com>
 *
 */
//@ReportsCrashes(formKey = "YOUR_FORM_KEY")
@EApplication
public class RoutrApplication
    extends Application
{

	private NaviciClient naviciClient;

	/**
	 * 
	 * @return {@link NaviciClient}
	 */
    public NaviciClient getNaviciClient() {
		return naviciClient;
	}

    /**
     * 
     * @param {@link NaviciClient} naviciClient
     */
	public void setNaviciClient(NaviciClient naviciClient) {
		this.naviciClient = naviciClient;
	}

	/**
	 * Initializes {@link NaviciClient}
	 */
	@Override
    public void onCreate() {
		Log.d("*******************", "creating application");
//        ACRA.init(this);
    	setNaviciClient(NaviciClient.getInstance(City.JYVASKYLA));
    	Log.d("*******************", "navici client instance " + getNaviciClient());
        super.onCreate();
    }

}
