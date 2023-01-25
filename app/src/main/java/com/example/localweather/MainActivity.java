package com.example.localweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText input;
    Button search;
    TextView address, temparature, status, windStatus;

    private final String appID = "eda1ef54ac6b95c3b2e8d2ef681e6569";
    private final String url = "https://api.openweathermap.org/data/2.5/weather";

    DecimalFormat dc = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.input);
        address = findViewById(R.id.address);
        temparature = findViewById(R.id.temp);
        status = findViewById(R.id.status);
        windStatus = findViewById(R.id.wind);
    }

    public void getData(View view){
        String tempurl = "";
        String city = input.getText().toString().trim();

        if(city.equals("")){
            address.setText("Empty Input");
        }else{
            tempurl = url + "?q="  + city + "&appid=" + appID;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.d("responce", response);

                String output = "";
                try{
                    JSONObject jsonResponce = new JSONObject(response);
                    JSONArray jsonArray = jsonResponce.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");


                    JSONObject jsonObjectMain = jsonResponce.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelslike = jsonObjectMain.getDouble("feels_like") - 273.15;


                    JSONObject jsonObjectWind = jsonResponce.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");


                    JSONObject jsonObjectSys = jsonResponce.getJSONObject("sys");
                    String cityname = jsonResponce.getString("name");
                    String countryName = jsonObjectSys.getString("country");

                    address.setText(cityname + " " + countryName);
                    temparature.setText(dc.format(temp) + " ℃");
                    status.setText(description.toUpperCase());
                    windStatus.setText(wind + " Km/h");

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }



}