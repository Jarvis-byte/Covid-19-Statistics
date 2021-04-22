package com.example.covid_19.Fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.covid_19.R;


public class SymptomsFragment extends Fragment {
    TextView txt_know;

    //"What you need to know\n
//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vaccination, container, false);


        return view;
    }
}