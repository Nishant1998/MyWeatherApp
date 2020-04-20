package com.example.myweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView cityName, temperature, weather;
    EditText editText;
    Button button;

    private String baseUrl = "https://api.openweathermap.org/data/2.5/weather?q=";
    private String API = "&appid=175614bfd0bad82d8067444f735fa9ea";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.textview_city_name);
        temperature = findViewById(R.id.textview_temp);
        weather = findViewById(R.id.textview_weather);
        editText = findViewById(R.id.city_name);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals(new String(""))) {
                    editText.setError("Please Enter City Name");
                } else {
                    String mURL = baseUrl + editText.getText().toString() + API;
                    Log.i("URL","URL" +mURL);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, mURL, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("JSON", "JSON" + response);

                                    try {
                                        String info = response.getString("weather");
                                        Log.d("INFO", "Info : " + info);

                                        JSONArray array = new JSONArray(info);

                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject parObj = array.getJSONObject(i);

                                            String w =  parObj.getString("main");
                                            weather.setText(w);
                                            Log.i("WEATHER : ",w);
                                        }

                                        JSONObject main = response.getJSONObject("main");
                                        Double temp = main.getDouble("temp");

                                        Log.i("TEMP : ", ""+(temp-273.15));
                                        String s = (temp-273.15)+"";
                                        temperature.setText(s.substring(0,2) + "°C");

                                        cityName.setText(editText.getText().toString());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("ERROR : " , "SOMETHING WENT WRONG : " + error);
                        }
                    });

                    MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);


                }


            }
        });
    }
}
