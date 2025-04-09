package com.jaixlabs.pricestracker.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jaixlabs.pricestracker.R;
import com.jaixlabs.pricestracker.Repository.PriceRepository;
import com.jaixlabs.pricestracker.ViewModel.PriceViewModel;
import com.jaixlabs.pricestracker.ViewModel.PriceViewModelFactory;
import com.jaixlabs.pricestracker.model.Price;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsFragment extends Fragment {

    private PriceViewModel viewModel;
    private SharedPreferences sharedPreferences;
    private LineChart lineChart;
    private ImageView  backBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        lineChart = view.findViewById(R.id.lineChart);
        backBtn = view.findViewById(R.id.backBtnn);



        PriceRepository repository = new PriceRepository(requireContext());
        PriceViewModelFactory factory = new PriceViewModelFactory(repository);
        viewModel = new ViewModelProvider(requireActivity(), factory).get(PriceViewModel.class);

        viewModel.getTransactions().observe(getViewLifecycleOwner(), prices -> {
            if (prices != null && !prices.isEmpty()) {
                showPriceChart(prices);
            }
        });

        backBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(AnalyticsFragment.this)
                    .navigate(R.id.action_analyticsFragment_to_homeFragment);
        });
        return view;
    }

    private void showPriceChart(List<Price> prices) {
        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < prices.size(); i++) {
            float change = (float) prices.get(i).getChange();
            entries.add(new Entry(i, change));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Price Change Trend");
        dataSet.setColor(Color.MAGENTA);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setDrawValues(true);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getDescription().setText("Price Change over Index");
        lineChart.animateX(1000);
        lineChart.invalidate();
    }
    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment.findNavController(AnalyticsFragment.this)
                        .navigate(R.id.action_analyticsFragment_to_homeFragment);
            }
        });
    }
}
