package com.example.covid_19.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid_19.Adapters.CentreDetailsAdapter;
import com.example.covid_19.AlertDialouge.DatePickerFragment;
import com.example.covid_19.ModelClass.CentreDetails;
import com.example.covid_19.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.View.VISIBLE;


public class SymptomsFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    //private CentreDetailsAdapter mAdapter;
    public String pincode, mDate;
    TextView txt_know;
    ScrollView scrollView;
    int searchclicked = 0;
    boolean check_ScrollingUp = false;
  //  RelativeLayout pincodeLayout;
    TextView txt;
    private TextInputEditText mPincode;
    private Button mNext, cancel;
    private RecyclerView mRecyclerview;
    private ArrayList<CentreDetails> centres;
    private ProgressBar mProgressBar;
    private String api_url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?";

    public static boolean isValidPinCode(String pinCode) {

        // Regex to check valid pin code of India.
        String regex
                = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the pin code is empty
        // return false
        if (pinCode == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given pin code
        // and regular expression.
        Matcher m = p.matcher(pinCode);

        // Return if the pin code
        // matched the ReGex
        return m.matches();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vaccination, container, false);
        scrollView = view.findViewById(R.id.scrollView);

        mProgressBar = view.findViewById(R.id.progress_circular);
        mPincode = view.findViewById(R.id.inputPin);
        mNext = view.findViewById(R.id.submitBtn);
        mRecyclerview = view.findViewById(R.id.recyclerView);
        mRecyclerview.setHasFixedSize(true);
//        pincodeLayout = view.findViewById(R.id.pincodeLayout);
        txt = view.findViewById(R.id.txt);
//        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {

//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) {
//                    // Scrolling up
//                    if (check_ScrollingUp) {
////                        cancel.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.trans_downwards));
////                        txt.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.trans_downwards));
////                        mPincode.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.trans_downwards));
//                        pincodeLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.trans_downwards));
//                        check_ScrollingUp = false;
//                    }
//
//                } else {
//                    // User scrolls down
//                    if (!check_ScrollingUp) {
//                        pincodeLayout.startAnimation(AnimationUtils
//                                .loadAnimation(getContext(), R.anim.trans_upwards));
//                        check_ScrollingUp = true;
//
//                    }
//                }
//            }


//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//            }
    //    });

        centres = new ArrayList<CentreDetails>();
        cancel = view.findViewById(R.id.cancel);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mPincode.getText().toString())) {
                    mPincode.setError("Please enter some Pincode");
                } else if (!isValidPinCode(mPincode.getText().toString())) {
                    mPincode.setError("Please enter valid Pincode");
                } else {
                    DatePickerFragment datePicker = new DatePickerFragment();
                    datePicker.setListeningActivity(SymptomsFragment.this);
                    datePicker.show(getActivity().getSupportFragmentManager(), "date picker");
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerview.setVisibility(View.GONE);
                scrollView.setVisibility(VISIBLE);
                cancel.setVisibility(View.INVISIBLE);
                mNext.setVisibility(VISIBLE);
            }
        });

        return view;
    }

    private void sendRequest() {
        mProgressBar.setVisibility(VISIBLE);
        //mRecyclerview.invalidate();
        mRecyclerview.setVisibility(View.VISIBLE);
        mNext.setVisibility(View.INVISIBLE);
        cancel.setVisibility(VISIBLE);
        mPincode.clearFocus();
        centres.clear();
        pincode = mPincode.getText().toString();
        String request_url = api_url + "pincode=" + pincode + "&date=" + mDate;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, request_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("RESPONSE", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray sessionsArray = obj.getJSONArray("sessions");
                    for (int i = 0; i < sessionsArray.length(); i++) {
                        JSONObject sessionObject = sessionsArray.getJSONObject(i);
                        CentreDetails cd = new CentreDetails();
                        cd.setCenterName(sessionObject.getString("name"));
                        cd.setCenterAddress(sessionObject.getString("address"));
                        cd.setCenterFromTime(sessionObject.getString("from"));
                        cd.setCenterToTime(sessionObject.getString("to"));
                        cd.setVaccineName(sessionObject.getString("vaccine"));
                        cd.setFee_type(sessionObject.getString("fee_type"));
                        cd.setAgeLimit(String.valueOf(sessionObject.getInt("min_age_limit")));
                        cd.setAvaiableCapacity(sessionObject.getString("available_capacity"));
                        centres.add(cd);
                    }
                    Log.i("SIZE", String.valueOf(centres.size()));
                    if (centres.isEmpty()) {
                        scrollView.setVisibility(VISIBLE);
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mRecyclerview.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Sorry !!! No Data Available", Toast.LENGTH_SHORT).show();
                    } else {

                        CentreDetailsAdapter centreDetailsAdapter = new CentreDetailsAdapter(getActivity(), centres);
                        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mRecyclerview.setAdapter(centreDetailsAdapter);
                        mProgressBar.setVisibility(View.INVISIBLE);
                        scrollView.setVisibility(View.INVISIBLE);

                    }

                } catch (JSONException e) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar z = Calendar.getInstance();
        z.set(Calendar.YEAR, year);
        z.set(Calendar.MONTH, month);
        z.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-YYYY");
        dateformat.setTimeZone(z.getTimeZone());
        String date = dateformat.format(z.getTime());
        utility(date);
    }

    private void utility(String date) {
        mDate = date;
        sendRequest();
    }
}