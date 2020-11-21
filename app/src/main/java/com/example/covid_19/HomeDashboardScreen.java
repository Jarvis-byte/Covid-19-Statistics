package com.example.covid_19;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class HomeDashboardScreen extends AppCompatActivity {
    String myResponse;
    TextView confirmed_number, active_number, recovered_number, death_number, NewRecovered_number, NewDeaths_number, btn_change, infected_number, recover_county_number, death_number_country, Welcome_User;
    String number, formatted;
    double amount;
    DecimalFormat formatter;
    EditText country;
    ImageView Country_flag, toolbar_image;
    String welcomemessage;
    ProgressBar Progress_Login_in;
    boolean onCardClick = true;
    private Toolbar toolbar;
    private LineChart mChart;
    private CardView CardView_Line_Graph, first_card;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dashboard_screen);
        toolbar = findViewById(R.id.toolbar);
        toolbar_image = findViewById(R.id.toolbar_image);
        ImageSlider imageSlider = findViewById(R.id.slider);
        Progress_Login_in = findViewById(R.id.Progress_Login_in);
        mChart = findViewById(R.id.lineChart);
        CardView_Line_Graph = findViewById(R.id.CardView_Line_Graph);
        first_card = findViewById(R.id.first_card);
        first_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCardClick) {
                    onCardClick = false;
                    CardView_Line_Graph.setVisibility(View.VISIBLE);

                } else {
                    CardView_Line_Graph.setVisibility(View.GONE);
                    onCardClick = true;
                }
            }
        });


        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.wear_mask, "Wear a mask.Save lives"));
        slideModels.add(new SlideModel(R.drawable.no_contact, "No Contact"));
        slideModels.add(new SlideModel(R.drawable.washhand, "Wash Your Hand Properly"));
        slideModels.add(new SlideModel(R.drawable.hand_sanitizer, "Use Sanitizer"));
        slideModels.add(new SlideModel(R.drawable.fight_corona, "Together we will fight with Corona Virus"));
        imageSlider.setImageList(slideModels, true);
        Country_flag = findViewById(R.id.Country_flag);
        Welcome_User = findViewById(R.id.Welcome_User);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {

            Welcome_User.setText(" Good Morning");
            welcomemessage = "Good Morning";
            Welcome_User.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sunrise, 0, 0, 0);
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            Welcome_User.setText(" Good Afternoon");
            welcomemessage = " Good Afternoon";

            Welcome_User.setCompoundDrawablesWithIntrinsicBounds(R.drawable.afternoon, 0, 0, 0);
        } else if (timeOfDay >= 16 && timeOfDay < 21) {

            Welcome_User.setText(" Good Evening");
            welcomemessage = " Good Evening";

            Welcome_User.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sunset, 0, 0, 0);

        } else if (timeOfDay >= 21 && timeOfDay < 24) {

            Welcome_User.setText(" Good Night");
            welcomemessage = " Good Night";
            Welcome_User.setCompoundDrawablesWithIntrinsicBounds(R.drawable.night, 0, 0, 0);

        }
        configureToolbar(toolbar);

//declare
        confirmed_number = findViewById(R.id.confirmed_number);
        active_number = findViewById(R.id.active_number);
        recovered_number = findViewById(R.id.recovered_number);
        death_number = findViewById(R.id.death_number);
        NewRecovered_number = findViewById(R.id.NewRecovered_number);
        NewDeaths_number = findViewById(R.id.NewDeaths_number);
        btn_change = findViewById(R.id.btn_change);
        country = findViewById(R.id.country);
        country.setEnabled(false);
        infected_number = findViewById(R.id.infected_number);
        recover_county_number = findViewById(R.id.recover_county_number);
        death_number_country = findViewById(R.id.death_number_country);

        NoInternetDialog noInternetDialog = new NoInternetDialog.Builder(this).build();
        noInternetDialog.setCancelable(false);
        getCovidGlobalApi();
        getCovidIndiaApi();
        getFlagIndiaApi();
        getCovidIndiaCasesStartupGraph();
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(country.getText())) {
                    country.setError("Please Enter Country Name");
                    country.requestFocus();
                } else {
                    if (btn_change.getText().toString().equalsIgnoreCase("Change")) {
                        btn_change.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_search_24, 0, 0, 0);
                        btn_change.setText("Search");
                        country.setEnabled(true);
                        country.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        country.setText("");
                    } else if (btn_change.getText().toString().equalsIgnoreCase("Search")) {

                        btn_change.setText("Change");
                        country.setEnabled(false);
                        country.clearFocus();
                        HomeDashboardScreen.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        Progress_Login_in.setVisibility(View.VISIBLE);
                        btn_change.setVisibility(View.INVISIBLE);
                        getCovidCountryWiseApi(country.getText().toString().trim());
                        getFlagCovidApi(country.getText().toString().trim());
                        getCovidCountrywiseAllCases(country.getText().toString().trim());
                    }
                }

            }
        });


    }

    private void configureToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Covid-19 Statistics");
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public void getCovidCountryWiseApi(final String Country) {
        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://api.covid19api.com/summary")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("Failled to call", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("Response", response.toString());
                    myResponse = response.body().string();
                    String countryjson = Country;
                    try {
                        Boolean cityFound = false;

                        JSONObject jsonObject = new JSONObject(myResponse);
                        JSONArray jsonArray = jsonObject.getJSONArray("Countries");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject O = jsonArray.getJSONObject(i);


                            if (O.getString("Country").equalsIgnoreCase(countryjson) || O.getString("Slug").equalsIgnoreCase(countryjson)) {
                                cityFound = true;
                                HomeDashboardScreen.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                            infected_number.setText(O.getString("TotalConfirmed"));
                                            number = infected_number.getText().toString();
                                            amount = Double.parseDouble(number);
                                            formatter = new DecimalFormat("#,###");
                                            formatted = formatter.format(amount);
                                            infected_number.setText(formatted);

                                            recover_county_number.setText(O.getString("TotalRecovered"));
                                            number = recover_county_number.getText().toString();
                                            amount = Double.parseDouble(number);
                                            formatter = new DecimalFormat("#,###");
                                            formatted = formatter.format(amount);
                                            recover_county_number.setText(formatted);

                                            death_number_country.setText(O.getString("TotalDeaths"));
                                            number = death_number_country.getText().toString();
                                            amount = Double.parseDouble(number);
                                            formatter = new DecimalFormat("#,###");
                                            formatted = formatter.format(amount);
                                            death_number_country.setText(formatted);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                break;
                            }
                        }

                        if (!cityFound) {
                            HomeDashboardScreen.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    country.setError("No Country Found By This Name");
                                    infected_number.setText("0");
                                    recover_county_number.setText("0");
                                    death_number_country.setText("0");
                                }
                            });
                        }

                    } catch (Exception e) {
                        Log.i("JSON ERROR", e.getMessage());
                    }


                } else {
                    Log.i("OkHTTP", "Call is NOT-successfull");
                }
            }
        });


    }

    public void getFlagCovidApi(final String Country) {
        Log.i("Flag Function", "Called");

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://restcountries.eu/rest/v2/name/" + Country)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("Failled to call", e.getMessage());
                Progress_Login_in.setVisibility(View.INVISIBLE);
                btn_change.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseFlag = response.body().string();
                    try {

                        JSONArray jsonArray = new JSONArray(responseFlag);
                        if (jsonArray.length() > 1) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                if (jsonObject.getString("name").equalsIgnoreCase(Country)) {
                                    final String flag_png = jsonObject.getString("flag");

                                    HomeDashboardScreen.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            GlideToVectorYou.justLoadImage(HomeDashboardScreen.this, Uri.parse(flag_png), Country_flag);
                                            Progress_Login_in.setVisibility(View.INVISIBLE);
                                            btn_change.setVisibility(View.VISIBLE);
                                            country.setEnabled(false);
                                            country.setFocusable(false);

                                        }
                                    });


                                    Log.i("Flag link", flag_png);
                                    break;
                                }

                            }
                        } else {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            final String flag_png = jsonObject.getString("flag");
                            HomeDashboardScreen.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Progress_Login_in.setVisibility(View.INVISIBLE);
                                    btn_change.setVisibility(View.VISIBLE);
                                    GlideToVectorYou.justLoadImage(HomeDashboardScreen.this, Uri.parse(flag_png), Country_flag);
//                                    Glide.with(HomeDashboardScreen.this)
//                                            .load(flag_png)
//                                            .apply(RequestOptions.centerInsideTransform())
//                                            .fitCenter()
//                                            .into(Country_flag);
                                }
                            });
                            Log.i("Flag link", flag_png);
                        }


                    } catch (Exception e) {
                        HomeDashboardScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Progress_Login_in.setVisibility(View.INVISIBLE);
                                btn_change.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                } else {
                    HomeDashboardScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Progress_Login_in.setVisibility(View.INVISIBLE);
                            btn_change.setVisibility(View.VISIBLE);
                        }
                    });
                    Log.i("OkHTTP", "Call is NOT-successfull");
                }
            }
        });
    }

    public void getFlagIndiaApi() {
        Log.i("Flag Function", "Called");

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://restcountries.eu/rest/v2/name/india")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("Failled to call", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseFlag = response.body().string();
                    try {

                        JSONArray jsonArray = new JSONArray(responseFlag);
                        if (jsonArray.length() > 1) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                if (jsonObject.getString("name").equalsIgnoreCase("india")) {
                                    final String flag_png = jsonObject.getString("flag");

                                    HomeDashboardScreen.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            GlideToVectorYou.justLoadImage(HomeDashboardScreen.this, Uri.parse(flag_png), Country_flag);
//                                            Glide.with(HomeDashboardScreen.this)
//                                                    .load(flag_png)
//                                                    .apply(RequestOptions.centerInsideTransform())
//                                                    .fitCenter()
//                                                    .into(Country_flag);
                                        }
                                    });


                                    Log.i("Flag link", flag_png);
                                    break;
                                }

                            }
                        } else {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            final String flag_png = jsonObject.getString("flag");
                            HomeDashboardScreen.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    GlideToVectorYou.justLoadImage(HomeDashboardScreen.this, Uri.parse(flag_png), Country_flag);
//                                    Glide.with(HomeDashboardScreen.this)
//                                            .load(flag_png)
//                                            .apply(RequestOptions.centerInsideTransform())
//                                            .fitCenter()
//                                            .into(Country_flag);
                                }
                            });
                            Log.i("Flag link", flag_png);
                        }


                    } catch (Exception e) {

                    }

                } else {
                    Log.i("OkHTTP", "Call is NOT-successfull");
                }
            }
        });
    }

    public void getCovidIndiaApi() {
        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://api.covid19api.com/summary")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("Failled to call", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("Response", response.toString());
                    myResponse = response.body().string();

                    try {
                        Boolean cityFound = false;

                        JSONObject jsonObject = new JSONObject(myResponse);
                        JSONArray jsonArray = jsonObject.getJSONArray("Countries");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject O = jsonArray.getJSONObject(i);


                            if (O.getString("Country").equalsIgnoreCase("india") || O.getString("Slug").equalsIgnoreCase("india")) {
                                cityFound = true;
                                HomeDashboardScreen.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                            infected_number.setText(O.getString("TotalConfirmed"));
                                            number = infected_number.getText().toString();
                                            amount = Double.parseDouble(number);
                                            formatter = new DecimalFormat("#,###");
                                            formatted = formatter.format(amount);
                                            infected_number.setText(formatted);

                                            recover_county_number.setText(O.getString("TotalRecovered"));
                                            number = recover_county_number.getText().toString();
                                            amount = Double.parseDouble(number);
                                            formatter = new DecimalFormat("#,###");
                                            formatted = formatter.format(amount);
                                            recover_county_number.setText(formatted);

                                            death_number_country.setText(O.getString("TotalDeaths"));
                                            number = death_number_country.getText().toString();
                                            amount = Double.parseDouble(number);
                                            formatter = new DecimalFormat("#,###");
                                            formatted = formatter.format(amount);
                                            death_number_country.setText(formatted);


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                break;
                            }
                        }

                        if (!cityFound) {
                            HomeDashboardScreen.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    country.setError("No Country Found By This Name");
                                    infected_number.setText("0");
                                    recover_county_number.setText("0");
                                    death_number_country.setText("0");
                                }
                            });
                        }

                    } catch (Exception e) {
                        Log.i("JSON ERROR", e.getMessage());
                    }


                } else {
                    Log.i("OkHTTP", "Call is NOT-successfull");
                }
            }
        });


    }

    public void getCovidGlobalApi() {
        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://api.covid19api.com/summary")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("Failled to call", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    myResponse = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(myResponse);
                        final JSONObject Global = jsonObject.getJSONObject("Global");
                        final String Total_Confiremed = Global.getString("TotalConfirmed");
                        final String New_confirmed = Global.getString("NewConfirmed");
                        final String Total_Recovered = Global.getString("TotalRecovered");
                        final String Total_death = Global.getString("TotalDeaths");
                        final String Newrecovered_number = Global.getString("NewRecovered");
                        final String Newdeaths_number = Global.getString("NewDeaths");
                        HomeDashboardScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                confirmed_number.setText(Total_Confiremed);
                                number = confirmed_number.getText().toString();
                                amount = Double.parseDouble(number);
                                formatter = new DecimalFormat("#,###");
                                formatted = formatter.format(amount);
                                confirmed_number.setText(formatted);

                                active_number.setText(New_confirmed);
                                number = active_number.getText().toString();
                                amount = Double.parseDouble(number);
                                formatter = new DecimalFormat("#,###");
                                formatted = formatter.format(amount);
                                active_number.setText(formatted);

                                recovered_number.setText(Total_Recovered);
                                number = recovered_number.getText().toString();
                                amount = Double.parseDouble(number);
                                formatter = new DecimalFormat("#,###");
                                formatted = formatter.format(amount);
                                recovered_number.setText(formatted);

                                death_number.setText(Total_death);
                                number = death_number.getText().toString();
                                amount = Double.parseDouble(number);
                                formatter = new DecimalFormat("#,###");
                                formatted = formatter.format(amount);
                                death_number.setText(formatted);

                                NewRecovered_number.setText(Newrecovered_number);
                                number = NewRecovered_number.getText().toString();
                                amount = Double.parseDouble(number);
                                formatter = new DecimalFormat("#,###");
                                formatted = formatter.format(amount);
                                NewRecovered_number.setText(formatted);

                                NewDeaths_number.setText(Newdeaths_number);
                                number = NewDeaths_number.getText().toString();
                                amount = Double.parseDouble(number);
                                formatter = new DecimalFormat("#,###");
                                formatted = formatter.format(amount);
                                NewDeaths_number.setText(formatted);


                            }
                        });

                        Log.i("NewConfirmed", Global.getString("NewConfirmed"));
                        Log.i("TotalConfirmed", Global.getString("TotalConfirmed"));
                    } catch (Exception e) {

                    }


                } else {
                    Log.i("OkHTTP", "Call is NOT-successfull");

                }
            }
        });


    }

    public void LineChart(ArrayList<Entry> yValues, ArrayList<Entry> yRecovered, ArrayList<Entry> yDeaths) {

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        Description description = new Description();
        description.setText("Months Wise");
        mChart.setDescription(description);
        mChart.getAxisRight().setEnabled(false);
        mChart.setDrawGridBackground(true);
        //mChart.setGridBackgroundColor(Color.CYAN);
        mChart.setDrawBorders(true);
        mChart.getDescription().setEnabled(false);

        LineDataSet set1 = new LineDataSet(yValues, "Infected");
        LineDataSet set2 = new LineDataSet(yRecovered, "Recovered");
        LineDataSet set3 = new LineDataSet(yDeaths, "Deaths");
        set1.setCircleColors(Color.BLACK);
        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setDrawFilled(true);
        set1.setDrawValues(false);


        set2.setColor(Color.BLACK);
        set2.setFillAlpha(110);
        set2.setLineWidth(3f);
        set2.setCircleColors(Color.BLACK);
        set2.setDrawValues(false);

        set3.setColor(Color.BLUE);
        set3.setCircleColors(Color.BLACK);
        set3.setFillAlpha(110);
        set3.setLineWidth(3f);
        set3.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        LineData data = new LineData(dataSets);
        mChart.setData(data);

        String[] values = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov"};

        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormater(values));
        // xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

    }

    public void getCovidCountrywiseAllCases(String Country) {
        final ArrayList<Entry> yValues = new ArrayList<>();
        final ArrayList<Entry> yRecovered = new ArrayList<>();
        final ArrayList<Entry> yDeaths = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://api.covid19api.com/dayone/country/" + Country)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("Failled to call", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    int count = 0;
                    myResponse = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse);
                        for (int i = 0; i < jsonArray.length(); i = i + 30) {
                            JSONObject O = jsonArray.getJSONObject(i);

                            Log.i("Date", O.getString("Date"));
                            int confirmed = Integer.parseInt(O.getString("Confirmed"));
                            int recovered = Integer.parseInt(O.getString("Recovered"));
                            int death = Integer.parseInt(O.getString("Deaths"));
                            while (count != 10) {
                                yValues.add(new Entry(count, confirmed));
                                yRecovered.add((new Entry(count, recovered)));
                                yDeaths.add(new Entry(count, death));
                                break;
                            }

                            count++;

                        }
                        LineChart(yValues, yRecovered, yDeaths);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.i("OkHTTP", "Call is NOT-successfull");
                }
            }
        });


    }

    public void getCovidIndiaCasesStartupGraph() {
        final ArrayList<Entry> yValues = new ArrayList<>();
        final ArrayList<Entry> yRecovered = new ArrayList<>();
        final ArrayList<Entry> yDeaths = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://api.covid19api.com/dayone/country/india")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("Failled to call", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    int count = 0;
                    myResponse = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse);
                        for (int i = 0; i < jsonArray.length(); i = i + 30) {
                            JSONObject O = jsonArray.getJSONObject(i);

                            Log.i("Date", O.getString("Date"));
                            int confirmed = Integer.parseInt(O.getString("Confirmed"));
                            int recovered = Integer.parseInt(O.getString("Recovered"));
                            int death = Integer.parseInt(O.getString("Deaths"));
                            while (count != 10) {
                                yValues.add(new Entry(count, confirmed));
                                yRecovered.add((new Entry(count, recovered)));
                                yDeaths.add(new Entry(count, death));
                                break;
                            }

                            count++;

                        }
                        LineChart(yValues, yRecovered, yDeaths);
//


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.i("OkHTTP", "Call is NOT-successfull");
                }
            }
        });


    }

    public class MyXAxisValueFormater extends ValueFormatter implements IValueFormatter {
        private String[] mValues;

        public MyXAxisValueFormater(String[] mValues) {
            Log.i("MYXAXisvalueFormater", "Called");
            this.mValues = mValues;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mValues[(int) value];
            // mValues[(int) value];
        }
    }
}