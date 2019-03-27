package com.horus;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class fetchData extends AsyncTask<Void, Void, Void> {
    String data = "";
    String dataParsed = "";
    String firstTempValue = "null";
    String secondTempValue = "null";
    String firstSmokeValue = "null";
    String secondSmokeValue = "null";
    Boolean firstTempWarn = false;
    Boolean secondTempWarn = false;
    Boolean firstSmokeWarn = false;
    Boolean secondSmokeWarn = false;

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("http://18.216.161.74/sensorData.txt");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            Log.i("OX_HORUS", String.valueOf(httpURLConnection));
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            try {
                JSONArray jsonArray = new JSONArray(data);
                firstTempValue = jsonArray.getJSONObject(0).getString("firstTemp");
                secondTempValue = jsonArray.getJSONObject(1).getString("secondTemp");
                firstSmokeValue = jsonArray.getJSONObject(2).getString("firstSmoke");
                secondSmokeValue = jsonArray.getJSONObject(3).getString("secondSmoke");
                firstTempWarn = Boolean.parseBoolean(jsonArray.getJSONObject(0).getString("warning"));
                secondTempWarn = Boolean.parseBoolean(jsonArray.getJSONObject(1).getString("warning"));
                firstSmokeWarn = Boolean.parseBoolean(jsonArray.getJSONObject(2).getString("warning"));
                secondSmokeWarn = Boolean.parseBoolean(jsonArray.getJSONObject(3).getString("warning"));

                Log.i("OX_HORUS", firstTempValue);

            } catch (Throwable t) {
                Log.i("OX_HORUS", "Could not parse malformed JSON: '" + data + "'");
            }
            Log.i("OX_HORUS", data);

        } catch (MalformedURLException e) {
            Log.i("OX_HORUS", String.valueOf(e));
        } catch (IOException e) {
            Log.i("OX_HORUS", String.valueOf(e));
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.i("OX_HORUS", this.dataParsed);

        MainActivity.firstTemp.setText(this.firstTempValue + "°C");
        MainActivity.secondTemp.setText(this.secondTempValue + "°C");
        MainActivity.firstSmoke.setText(this.firstSmokeValue);
        MainActivity.secondSmoke.setText(this.secondSmokeValue);

        MainActivity.firstTempWarn = this.firstTempWarn;
        MainActivity.secondTempWarn = this.secondTempWarn;
        MainActivity.firstSmokeWarn = this.firstSmokeWarn;
        MainActivity.secondSmokeWarn = this.secondSmokeWarn;

    }
}
