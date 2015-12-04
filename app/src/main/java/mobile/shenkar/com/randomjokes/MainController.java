package mobile.shenkar.com.randomjokes;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

public class MainController {
	private final String usrlString = "http://api.icndb.com/jokes/random";
	private final String baseUrlString = "http://api.icndb.com";
	private Context context;
	private Application application;
	
	public MainController(Context context) {
		this.context = context;
	}
	/**
	 *
	 * @param callback
	 */
	public void getJokeInBackground(final AppCallback<String> callback) {
		
		//Anonymous async task.
		//Note: we create an instance every call, its possible to create the 
		//async task as private class and create setCallback function inside the async task, 
		//this way we can prevent us from creating several instances.
		new AsyncTask<String, Integer, String>() {

			@Override
			protected String doInBackground(String... params) {
				return getFromWeb(params[0]); // BG thread
			}

			@Override
			protected void onPostExecute(String result) {
				if (result == null) //UI thread
				{
					callback.done(null, new Exception("failed to fetch data from the web."));
					return;
				}
				try {
					JSONObject jsonResponse = new JSONObject(result);
					JSONObject value = jsonResponse.getJSONObject("value");
					String joke = "";
					if(value!=null)
					{
						joke = value.getString("joke");
					}
					if (callback != null) {
						callback.done(joke, null);
					}
				} catch (JSONException e) {
					callback.done(null, e);
				}
			}

		}.execute(usrlString);
	}

	private String getFromWeb(String urlString) {
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urlString);
			//get the url connection.
			urlConnection = (HttpURLConnection) url.openConnection();
			//get the input stream.
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(inReader);
			StringBuilder responseBuilder = new StringBuilder();
			for (String line = bufferedReader.readLine(); line != null; line = bufferedReader
					.readLine()) {
				responseBuilder.append(line);
			}
			return responseBuilder.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			urlConnection.disconnect();
		}
	}
}
