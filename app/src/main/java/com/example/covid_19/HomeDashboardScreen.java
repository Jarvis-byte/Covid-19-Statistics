package com.example.covid_19;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.covid_19.AlertDialouge.ListItem;
import com.example.covid_19.AlertDialouge.MyAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.github.ybq.android.spinkit.style.WanderingCubes;

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
    TextView confirmed_number, active_number, recovered_number, death_number, NewRecovered_number, NewDeaths_number, btn_change, infected_number, recover_county_number, death_number_country, Welcome_User, changeGraphs, CityWiseList;
    String number, formatted;
    double amount;
    DecimalFormat formatter;
    EditText country;
    ImageView Country_flag, toolbar_image, pushDown, pushDown_Grid, Close;
    String welcomemessage;
    MyAdapter adapter;
    ProgressBar Progress_Login_in, spin_kit_infected_number, spin_kit_recover_num, spin_kit_death_num, spin_kit_flag, spin_kit_alert;
    boolean onCardClick = true;
    GridLayout Grid_Layout_second;
    PieChart PieChart;
    RecyclerView recycler;

    SearchView search_city;
    private List<ListItem> listItems;
    private Toolbar toolbar;
    private LineChart mChart;
    private CardView CardView_Line_Graph, first_card, second_card, card_under_first_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dashboard_screen);

        toolbar = findViewById(R.id.toolbar);
        toolbar_image = findViewById(R.id.toolbar_image);
        ImageSlider imageSlider = findViewById(R.id.slider);
        mChart = findViewById(R.id.lineChart);

        CityWiseList = findViewById(R.id.CityWiseList);
        CityWiseList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGatePassDetailDialog();
            }
        });

        PieChart = findViewById(R.id.PieChart);
        CardView_Line_Graph = findViewById(R.id.CardView_Line_Graph);
        first_card = findViewById(R.id.first_card);
        second_card = findViewById(R.id.second_card);
        Grid_Layout_second = findViewById(R.id.Grid_Layout_second);
        pushDown_Grid = findViewById(R.id.pushDown_Grid);
        changeGraphs = findViewById(R.id.changeGraphs);
        card_under_first_card = findViewById(R.id.card_under_first_card);

        //============================ Progress bar ============================================

        spin_kit_infected_number = findViewById(R.id.spin_kit_infected_number);
        Sprite doubleBounce = new ThreeBounce();
        spin_kit_infected_number.setIndeterminateDrawable(doubleBounce);

        spin_kit_recover_num = findViewById(R.id.spin_kit_recover_num);
        Sprite doubleBouncerecover_num = new ThreeBounce();
        spin_kit_recover_num.setIndeterminateDrawable(doubleBouncerecover_num);

        spin_kit_death_num = findViewById(R.id.spin_kit_death_num);
        Sprite doubleBouncerDeath_num = new ThreeBounce();
        spin_kit_death_num.setIndeterminateDrawable(doubleBouncerDeath_num);

        spin_kit_flag = findViewById(R.id.spin_kit_flag);
        Sprite doubleBouncerflag = new DoubleBounce();
        spin_kit_flag.setIndeterminateDrawable(doubleBouncerflag);

        Progress_Login_in = findViewById(R.id.Progress_Login_in);
        Sprite doubleBouncerbtn_changer = new FadingCircle();
        Progress_Login_in.setIndeterminateDrawable(doubleBouncerbtn_changer);
        //==========================================================================================================
        changeGraphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PieChart.getVisibility() == View.GONE) {
                    AutoTransition autoTransition = new AutoTransition();
                    // autoTransition.excludeChildren(R.id.second_card, true);
                    TransitionManager.beginDelayedTransition(CardView_Line_Graph, autoTransition);
                    PieChart.setVisibility(View.VISIBLE);
                    mChart.setVisibility(View.GONE);
                    changeGraphs.setText("Show Line Chart");
                } else {
                    AutoTransition autoTransition = new AutoTransition();
                    // autoTransition.excludeChildren(R.id.second_card, true);
                    TransitionManager.beginDelayedTransition(CardView_Line_Graph, autoTransition);
                    PieChart.setVisibility(View.GONE);
                    mChart.setVisibility(View.VISIBLE);
                    changeGraphs.setText("Show Pie Chart");
                }
            }
        });
        second_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Grid_Layout_second.getVisibility() == View.GONE) {
                    AutoTransition autoTransition = new AutoTransition();
                    autoTransition.excludeChildren(R.id.second_card, true);
                    TransitionManager.beginDelayedTransition(second_card, autoTransition);
                    Grid_Layout_second.setVisibility(View.VISIBLE);
                    pushDown_Grid.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    AutoTransition autoTransition = new AutoTransition();
                    autoTransition.excludeChildren(R.id.second_card, true);
                    TransitionManager.beginDelayedTransition(second_card, autoTransition);
                    Grid_Layout_second.setVisibility(View.GONE);
                    pushDown_Grid.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }

            }
        });
        pushDown = findViewById(R.id.pushDown);
        first_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CardView_Line_Graph.getVisibility() == View.GONE) {
                    onCardClick = false;
                    AutoTransition autoTransition = new AutoTransition();
                    autoTransition.excludeChildren(R.id.first_card, true);
                    TransitionManager.beginDelayedTransition(first_card, autoTransition);
                    CardView_Line_Graph.setVisibility(View.VISIBLE);
                    pushDown.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);

                } else {
                    AutoTransition autoTransition = new AutoTransition();
                    autoTransition.excludeChildren(R.id.first_card, true);
                    TransitionManager.beginDelayedTransition(first_card, autoTransition);
                    CardView_Line_Graph.setVisibility(View.GONE);
                    pushDown.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    onCardClick = true;


                }
            }
        });


        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.covid_poster, "Welcome To Covid-19 Statistic App"));
        slideModels.add(new SlideModel(R.drawable.thanks_dr, "Thank You Heroes"));
        slideModels.add(new SlideModel(R.drawable.wear_mask, "Wear a mask.Save lives"));
        slideModels.add(new SlideModel(R.drawable.no_contact, "Please Do Not Touch"));
        slideModels.add(new SlideModel(R.drawable.washhand, "Clean your hands often"));
        slideModels.add(new SlideModel(R.drawable.social_distance_new, "Keep a safe distance"));
        slideModels.add(new SlideModel(R.drawable.stop_covid, "Together we will fight with Corona Virus"));


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

                        //  HomeDashboardScreen.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                        InputMethodManager imm = (InputMethodManager) HomeDashboardScreen.this.getSystemService(Context.INPUT_METHOD_SERVICE);

                        imm.showSoftInput(country, 0);

                        country.setText("");
                        CardView_Line_Graph.setVisibility(View.GONE);
                        pushDown.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);

                        AutoTransition autoTransition = new AutoTransition();
                        //   autoTransition.excludeChildren(R.id.first_card, true);
                        TransitionManager.beginDelayedTransition(first_card, autoTransition);
                        CityWiseList.setVisibility(View.GONE);

                    } else if (btn_change.getText().toString().equalsIgnoreCase("Search")) {
                        btn_change.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_repeat_24, 0, 0, 0);
                        btn_change.setText("Change");
                        country.setEnabled(false);
                        HomeDashboardScreen.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        Progress_Login_in.setVisibility(View.VISIBLE);
                        btn_change.setVisibility(View.INVISIBLE);
                        getCovidCountryWiseApi(country.getText().toString().trim());
                        getFlagCovidApi(country.getText().toString().trim());
                        getCovidCountrywiseAllCases(country.getText().toString().trim());
                        AutoTransition autoTransition = new AutoTransition();
                        TransitionManager.beginDelayedTransition(first_card, autoTransition);
                        Country_flag.setVisibility(View.GONE);
                        spin_kit_flag.setVisibility(View.VISIBLE);
                        infected_number.setText("");
                        recover_county_number.setText("");
                        death_number_country.setText("");

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


        AutoTransition autoTransition = new AutoTransition();
        TransitionManager.beginDelayedTransition(card_under_first_card, autoTransition);
        infected_number.setVisibility(View.GONE);
        spin_kit_infected_number.setVisibility(View.VISIBLE);

        AutoTransition autoTransition2 = new AutoTransition();
        TransitionManager.beginDelayedTransition(card_under_first_card, autoTransition2);
        recover_county_number.setVisibility(View.GONE);
        spin_kit_recover_num.setVisibility(View.VISIBLE);

        AutoTransition autoTransition3 = new AutoTransition();
        TransitionManager.beginDelayedTransition(card_under_first_card, autoTransition3);
        death_number_country.setVisibility(View.GONE);
        spin_kit_death_num.setVisibility(View.VISIBLE);

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
                    final ArrayList<PieEntry> TotalConfirmed = new ArrayList<>();

                    Log.i("Response", response.toString());
                    myResponse = response.body().string();
                    String countryjson = Country;
                    if (countryjson.equalsIgnoreCase("india")) {

                        HomeDashboardScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CityWiseList.setVisibility(View.VISIBLE);
                            }
                        });

                    } else {
                        HomeDashboardScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CityWiseList.setVisibility(View.GONE);
                            }
                        });
                    }
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
                                            int totalconfirmedint = Integer.parseInt(O.getString("TotalConfirmed"));

                                            number = infected_number.getText().toString();
                                            amount = Double.parseDouble(number);
                                            formatter = new DecimalFormat("#,###");
                                            formatted = formatter.format(amount);
                                            infected_number.setText(formatted);

                                            recover_county_number.setText(O.getString("TotalRecovered"));
                                            int totalrecoveredint = Integer.parseInt(O.getString("TotalRecovered"));
                                            number = recover_county_number.getText().toString();
                                            amount = Double.parseDouble(number);
                                            formatter = new DecimalFormat("#,###");
                                            formatted = formatter.format(amount);
                                            recover_county_number.setText(formatted);

                                            death_number_country.setText(O.getString("TotalDeaths"));
                                            int totaldeathint = Integer.parseInt(O.getString("TotalDeaths"));
                                            number = death_number_country.getText().toString();
                                            amount = Double.parseDouble(number);
                                            formatter = new DecimalFormat("#,###");
                                            formatted = formatter.format(amount);
                                            death_number_country.setText(formatted);

                                            AutoTransition autoTransition = new AutoTransition();
                                            TransitionManager.beginDelayedTransition(card_under_first_card, autoTransition);
                                            infected_number.setVisibility(View.VISIBLE);
                                            spin_kit_infected_number.setVisibility(View.GONE);

                                            AutoTransition autoTransition2 = new AutoTransition();
                                            TransitionManager.beginDelayedTransition(card_under_first_card, autoTransition2);
                                            recover_county_number.setVisibility(View.VISIBLE);
                                            spin_kit_recover_num.setVisibility(View.GONE);

                                            AutoTransition autoTransition3 = new AutoTransition();
                                            TransitionManager.beginDelayedTransition(card_under_first_card, autoTransition3);
                                            death_number_country.setVisibility(View.VISIBLE);
                                            spin_kit_death_num.setVisibility(View.GONE);
                                            TotalConfirmed.add(new PieEntry(totalconfirmedint, "Infected"));
                                            TotalConfirmed.add(new PieEntry(totalrecoveredint, "Recovered"));
                                            TotalConfirmed.add(new PieEntry(totaldeathint, "Death"));

                                            PieChart(TotalConfirmed);
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

        AutoTransition autoTransition = new AutoTransition();
        TransitionManager.beginDelayedTransition(first_card, autoTransition);
        Country_flag.setVisibility(View.GONE);
        spin_kit_flag.setVisibility(View.VISIBLE);

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
                                            AutoTransition autoTransition = new AutoTransition();
                                            TransitionManager.beginDelayedTransition(first_card, autoTransition);
                                            Country_flag.setVisibility(View.VISIBLE);
                                            spin_kit_flag.setVisibility(View.GONE);
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
                                    AutoTransition autoTransition = new AutoTransition();
                                    TransitionManager.beginDelayedTransition(first_card, autoTransition);
                                    Country_flag.setVisibility(View.VISIBLE);
                                    spin_kit_flag.setVisibility(View.GONE);
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
                                            AutoTransition autoTransition = new AutoTransition();
                                            TransitionManager.beginDelayedTransition(card_under_first_card, autoTransition);
                                            Country_flag.setVisibility(View.VISIBLE);
                                            spin_kit_flag.setVisibility(View.GONE);
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
                                    AutoTransition autoTransition = new AutoTransition();
                                    TransitionManager.beginDelayedTransition(card_under_first_card, autoTransition);
                                    Country_flag.setVisibility(View.VISIBLE);
                                    spin_kit_flag.setVisibility(View.GONE);
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
                    final ArrayList<PieEntry> TotalConfirmed = new ArrayList<>();
                    final ArrayList<PieEntry> TotalRecovered = new ArrayList<>();
                    final ArrayList<PieEntry> TotalDeath = new ArrayList<>();
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
                                            AutoTransition autoTransition = new AutoTransition();
                                            TransitionManager.beginDelayedTransition(card_under_first_card, autoTransition);
                                            infected_number.setVisibility(View.VISIBLE);
                                            spin_kit_infected_number.setVisibility(View.GONE);

                                            AutoTransition autoTransition2 = new AutoTransition();
                                            TransitionManager.beginDelayedTransition(card_under_first_card, autoTransition2);
                                            recover_county_number.setVisibility(View.VISIBLE);
                                            spin_kit_recover_num.setVisibility(View.GONE);


                                            AutoTransition autoTransition3 = new AutoTransition();
                                            TransitionManager.beginDelayedTransition(card_under_first_card, autoTransition3);
                                            death_number_country.setVisibility(View.VISIBLE);
                                            spin_kit_death_num.setVisibility(View.GONE);


                                            infected_number.setText(O.getString("TotalConfirmed"));
                                            int totalconfirmedint = Integer.parseInt(O.getString("TotalConfirmed"));
                                            number = infected_number.getText().toString();
                                            amount = Double.parseDouble(number);
                                            formatter = new DecimalFormat("#,###");
                                            formatted = formatter.format(amount);
                                            infected_number.setText(formatted);

                                            recover_county_number.setText(O.getString("TotalRecovered"));
                                            int totalrecoveredint = Integer.parseInt(O.getString("TotalRecovered"));
                                            number = recover_county_number.getText().toString();
                                            amount = Double.parseDouble(number);
                                            formatter = new DecimalFormat("#,###");
                                            formatted = formatter.format(amount);
                                            recover_county_number.setText(formatted);

                                            death_number_country.setText(O.getString("TotalDeaths"));
                                            int totaldeathint = Integer.parseInt(O.getString("TotalDeaths"));
                                            number = death_number_country.getText().toString();
                                            amount = Double.parseDouble(number);
                                            formatter = new DecimalFormat("#,###");
                                            formatted = formatter.format(amount);
                                            death_number_country.setText(formatted);
                                            TotalConfirmed.add(new PieEntry(totalconfirmedint, "Infected"));
                                            TotalConfirmed.add(new PieEntry(totalrecoveredint, "Recovered"));
                                            TotalConfirmed.add(new PieEntry(totaldeathint, "Death"));

                                            PieChart(TotalConfirmed);

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
        set1.setColor(Color.parseColor("#DF9628"));
        set1.setLineWidth(3f);
        set1.setDrawFilled(true);
        set1.setDrawValues(false);
        set1.setDrawCircles(false);


        set2.setColor(Color.parseColor("#13BD86"));
        set2.setFillAlpha(110);
        set2.setLineWidth(3f);
        set2.setCircleColors(Color.BLACK);
        set2.setDrawValues(false);
        set2.setDrawCircles(false);

        set3.setColor(Color.parseColor("#F65051"));
        set3.setCircleColors(Color.BLACK);
        set3.setFillAlpha(110);
        set3.setLineWidth(3f);
        set3.setDrawValues(false);
        set3.setDrawCircles(false);

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
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject O = jsonArray.getJSONObject(i);
                            Log.i("Date", O.getString("Date"));
                            int confirmed = Integer.parseInt(O.getString("Confirmed"));
                            int recovered = Integer.parseInt(O.getString("Recovered"));
                            int death = Integer.parseInt(O.getString("Deaths"));
                            yValues.add(new Entry(i, confirmed));
                            yRecovered.add((new Entry(i, recovered)));
                            yDeaths.add(new Entry(i, death));

//                            while (count != 10) {
//                                yValues.add(new Entry(count, confirmed));
//                                yRecovered.add((new Entry(count, recovered)));
//                                yDeaths.add(new Entry(count, death));
//                                break;
//                            }
//
//                            count++;

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
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject O = jsonArray.getJSONObject(i);

                            Log.i("Date", O.getString("Date"));
                            int confirmed = Integer.parseInt(O.getString("Confirmed"));
                            int recovered = Integer.parseInt(O.getString("Recovered"));
                            int death = Integer.parseInt(O.getString("Deaths"));
                            yValues.add(new Entry(i, confirmed));
                            yRecovered.add((new Entry(i, recovered)));
                            yDeaths.add(new Entry(i, death));


//                            while (count != 10) {
//                                yValues.add(new Entry(count, confirmed));
//                                yRecovered.add((new Entry(count, recovered)));
//                                yDeaths.add(new Entry(count, death));
//                                break;
//                            }
//
//                            count++;

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

    public void PieChart(ArrayList<PieEntry> totalRecovered) {
        PieChart.setUsePercentValues(true);
        PieChart.getDescription().setEnabled(true);
        Description description = new Description();
        description.setText("Today's Report");
        description.setTextSize(15);
        PieChart.setDescription(description);
        PieChart.setExtraOffsets(5, 10, 5, 5);
        PieChart.setDrawHoleEnabled(true);
        PieChart.setHoleColor(Color.WHITE);
        PieChart.setTransparentCircleRadius(60f);
        PieChart.animateY(1000, Easing.EaseInOutCubic);

        PieDataSet dataSet = new PieDataSet(totalRecovered, "");

        dataSet.setSelectionShift(5f);
        dataSet.setColors(Color.parseColor("#DF9628"), Color.parseColor("#13BD86"), Color.parseColor("#F65051"));
        dataSet.setDrawValues(false);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        PieChart.setData(data);

    }

    public void showGatePassDetailDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(HomeDashboardScreen.this, R.style.CustomAlertDialog);

        View alertView = (HomeDashboardScreen.this).getLayoutInflater().inflate(R.layout.alert_dialouge_box_state, null);
        alert.setView(alertView);
        final AlertDialog alertDialog = alert.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        Close = alertView.findViewById(R.id.Close);

        search_city = alertView.findViewById(R.id.search_city);
        search_city.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });


        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        spin_kit_alert = alertView.findViewById(R.id.spin_kit_alert);
        Sprite doubleBouncerflag = new WanderingCubes();
        spin_kit_alert.setIndeterminateDrawable(doubleBouncerflag);

        recycler = alertView.findViewById(R.id.recycler);
        recycler.setVisibility(View.GONE);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(HomeDashboardScreen.this));
        listItems = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://api.covidindiatracker.com/state_data.json")
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
                    Log.i("JSON DATA", myResponse);
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject O = jsonArray.getJSONObject(i);


                            ListItem listItem = new ListItem(O.getString("state"), O.getString("confirmed"), O.getString("recovered"), O.getString("deaths"));
                            listItems.add(listItem);
                        }
                        HomeDashboardScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recycler.setVisibility(View.VISIBLE);
                                spin_kit_alert.setVisibility(View.GONE);
                                adapter = new MyAdapter(listItems, HomeDashboardScreen.this);
                                recycler.setAdapter(adapter);
                            }
                        });

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