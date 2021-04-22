package com.example.covid_19.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import com.example.covid_19.Adapters.CardStackAdapter;
import com.example.covid_19.Adapters.CardStackCallback;
import com.example.covid_19.ModelClass.NewsItems;
import com.example.covid_19.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;


public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";
    CardStackView cardStackView;
    List<NewsItems> items = new ArrayList<>();
    FloatingActionButton rewind_btn;
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;
    private String myResponse;
    ProgressBar spin_kit_splash_Screen;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        cardStackView = view.findViewById(R.id.card_stack_view);
        rewind_btn = view.findViewById(R.id.rewind_btn);
        spin_kit_splash_Screen = view.findViewById(R.id.spin_kit_splash_Screen);
        spin_kit_splash_Screen.setVisibility(View.VISIBLE);
        manager = new CardStackLayoutManager(getActivity(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
                if (direction == Direction.Top) {
                    Toast.makeText(getActivity(), "Direction Top", Toast.LENGTH_SHORT).show();
                }

                if (direction == Direction.Bottom) {
                    Toast.makeText(getActivity(), "Direction Bottom", Toast.LENGTH_SHORT).show();
                }

                // Paginating
                if (manager.getTopPosition() == adapter.getItemCount() - 5) {
                    Toast.makeText(getActivity(), "No More News", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {

            }

            @Override
            public void onCardDisappeared(View view, int position) {
//                TextView tv = view.findViewById(R.id.item_name);
//                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }

        });

        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.FREEDOM);
        manager.setCanScrollVertical(false);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        cardStackView.setLayoutManager(manager);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CardStackAdapter(getContext(), addList());
        cardStackView.setAdapter(adapter);

        rewind_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewindAnimationSetting settings = new RewindAnimationSetting.Builder()
                        .setDirection(Direction.Bottom)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();

                CardStackLayoutManager cardStackLayoutManager2 = new CardStackLayoutManager(getContext());
                cardStackLayoutManager2.setStackFrom(StackFrom.None);
                cardStackLayoutManager2.setVisibleCount(3);
                cardStackLayoutManager2.setTranslationInterval(8.0f);
                cardStackLayoutManager2.setScaleInterval(0.95f);
                cardStackLayoutManager2.setSwipeThreshold(0.3f);
                cardStackLayoutManager2.setMaxDegree(20.0f);
                cardStackLayoutManager2.setDirections(Direction.FREEDOM);
                cardStackLayoutManager2.setCanScrollVertical(false);
                cardStackLayoutManager2.setCanScrollHorizontal(true);
                cardStackLayoutManager2.setSwipeableMethod(SwipeableMethod.Manual);
                cardStackLayoutManager2.setOverlayInterpolator(new LinearInterpolator());
                cardStackLayoutManager2.setRewindAnimationSetting(settings);
                cardStackView.setLayoutManager(cardStackLayoutManager2);
                cardStackView.rewind();
                Log.i("REWIND", "Rewinding");
            }
        });

        return view;
    }

    private void paginate() {
        List<NewsItems> old = adapter.getItems();
        List<NewsItems> baru = new ArrayList<>(addList());
        CardStackCallback callback = new CardStackCallback(old, baru);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
        adapter.setItems(baru);
        hasil.dispatchUpdatesTo(adapter);
    }

    private List<NewsItems> addList() {
        final List<NewsItems> items = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com/api/news/get-coronavirus-news/0")
                .get()
                .addHeader("x-rapidapi-key", "611dcfc3c9mshc5674db9b1ee25ep19792fjsn981e83738e46")
                .addHeader("x-rapidapi-host", "vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("Failed to call", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                myResponse = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(myResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("news");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Log.i("Image", jsonObject1.getString("urlToImage"));
                        items.add(new NewsItems(jsonObject1.getString("title"), jsonObject1.getString("content")+"\n\n....Click to Know More ", jsonObject1.getString("link"),jsonObject1.getString("urlToImage"),"Source: "+jsonObject1.getString("reference"),jsonObject1.getString("pubDate")));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                spin_kit_splash_Screen.setVisibility(View.INVISIBLE);
                                rewind_btn.setEnabled(true);
                                adapter = new CardStackAdapter(getContext(), items);
                                cardStackView.setAdapter(adapter);

                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return items;
    }


}