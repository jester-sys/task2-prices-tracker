package com.jaixlabs.pricestracker.fragment;


import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.jaixlabs.pricestracker.Adapter.PriceAdapter;
import com.jaixlabs.pricestracker.R;
import com.jaixlabs.pricestracker.Repository.PriceRepository;
import com.jaixlabs.pricestracker.Util.BiometricUtil;
import com.jaixlabs.pricestracker.Util.NetworkUtils;
import com.jaixlabs.pricestracker.ViewModel.PriceViewModel;
import com.jaixlabs.pricestracker.ViewModel.PriceViewModelFactory;
import com.jaixlabs.pricestracker.databinding.FragmentHomeBinding;
import com.jaixlabs.pricestracker.model.Price;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private PriceViewModel viewModel;
    private SharedPreferences sharedPreferences;
    private PriceAdapter adapter;
    private List<Price> allPrice = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        initSharedPrefs();

        PriceRepository repository = new PriceRepository(requireActivity());
        PriceViewModelFactory factory = new PriceViewModelFactory(repository);
        viewModel = new ViewModelProvider(requireActivity(), factory).get(PriceViewModel.class);

        notificationPermison();

        setupRecyclerView();
        setupSearchListener();
        observePrices();
        binding.settingsBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.action_homeFragment_to_settingsFragment);
        });
        binding.analyticsBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.action_homeFragment_to_analyticsFragment);
        });





        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (sharedPreferences != null) {
                String token = sharedPreferences.getString("auth_token", "");
                Log.d("HomeFragment", "Token: " + token);

                if (token == null || token.isEmpty()) {
                    Log.d("HomeFragment", "Token is empty or null. Navigating to Login.");
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_homeFragment_to_loginFragment);
                } else {
                    Log.d("HomeFragment", "Token is valid. Proceeding to biometric prompt.");
                    showBiometricPrompt(token);
                }
            } else {
                Log.e("HomeFragment", "sharedPreferences is null!");
            }
        }, 300); // Delay added
    }


    private void observePrices() {
        viewModel.getTransactions().observe(getViewLifecycleOwner(), prices -> {
            if (prices != null) {
                allPrice = prices;
                adapter.submitList(new ArrayList<>(prices)); // Use new list to trigger diff
            }
        });

        String token = sharedPreferences.getString("auth_token", "");
        viewModel.startRealTimeUpdates(token); // Poll every 5 seconds
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.stopRealTimeUpdates(); // 👈 Prevent memory leak or useless calls
    }

    public void notificationPermison(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        requireActivity(),
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1001
                );
            }
        }

    }


    private void initSharedPrefs() {
        try {
            sharedPreferences = EncryptedSharedPreferences.create(
                    "secure_prefs",
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    requireContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupRecyclerView() {
        adapter = new PriceAdapter(); // using ListAdapter
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupSearchListener() {
        binding.search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().toLowerCase().trim();
                String selectedCategory = binding.categorySpinner.getSelectedItem().toString();
                filterBySearchAndCategory(query, selectedCategory);

                List<Price> filtered = new ArrayList<>();
                for (Price t : allPrice) {
                    if (
                            String.valueOf(t.getName()).toLowerCase().contains(query)
                    ) {
                        filtered.add(t);
                    }
                }
                adapter.submitList(filtered);
            }
        });
    }


    private void showBiometricPrompt(String token) {
        BiometricUtil.showBiometricPrompt(requireActivity(), new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
             //   fetchTransactions(token);
                fetchDataBasedOnNetwork(token);
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                if (errorCode != BiometricPrompt.ERROR_USER_CANCELED &&
                        errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON &&
                        errorCode != BiometricPrompt.ERROR_CANCELED) {
                    Toast.makeText(getContext(), "Biometric Error: " + errString, Toast.LENGTH_SHORT).show();
                }
                showBiometricFallbackDialog(token);
            }

            @Override
            public void onAuthenticationFailed() {
                Toast.makeText(getContext(), "Biometric authentication failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDataBasedOnNetwork(String token) {
        if (NetworkUtils.isInternetAvailable(requireContext())) {
            fetchTransactions(token);
        } else {
            fetchFromRoom(); // Internet off
        }
    }

    private void fetchFromRoom() {
        viewModel.getLocalTransactions().observe(getViewLifecycleOwner(), transactions -> {
            if (transactions != null) {
                allPrice.clear();
                allPrice.addAll(transactions);
                adapter.setPriceList(allPrice);
                setupCategorySpinner();
            }
        });
    }

    private void fetchTransactions(String token) {
        viewModel.loadTransactions(token);
        viewModel.getTransactions().observe(getViewLifecycleOwner(), transactions -> {
            if (transactions != null) {
                allPrice = transactions;
                adapter.submitList(transactions);
                setupCategorySpinner();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void showBiometricFallbackDialog(String token) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Biometric Authentication")
                .setMessage("Biometric authentication was not completed. Do you want to continue without it?")
                .setPositiveButton("Continue", (dialog, which) -> {
                //    fetchTransactions(token);
                    fetchDataBasedOnNetwork(token);
                })
                .setNegativeButton("Retry", (dialog, which) -> {
                    showBiometricPrompt(token);
                })
                .setCancelable(false)
                .show();
    }
    private void setupCategorySpinner() {
        List<String> categories = new ArrayList<>();
        categories.add("All");

        for (Price t : allPrice) {
            String name = t.getName();
            if (name != null && !categories.contains(name)) {
                categories.add(name);
            }
        }

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorySpinner.setAdapter(adapterSpinner);

        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                filterBySearchAndCategory(binding.search.getText().toString(), selected); // 🔥
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finishAffinity(); // ✅ Close the app completely
            }
        });
    }
    private void filterBySearchAndCategory(String query, String categoryName) {
        List<Price> filtered = new ArrayList<>();
        query = query.toLowerCase();

        for (Price t : allPrice) {
            // Category match check karega by name
            boolean matchCategory = categoryName.equals("All") || t.getName().equalsIgnoreCase(categoryName);

            // Search match: name, price, or change
            boolean matchSearch =
                    t.getName().toLowerCase().contains(query) ||
                            String.valueOf(t.getPrice()).toLowerCase().contains(query) ||
                            String.valueOf(t.getChange()).toLowerCase().contains(query);

            if (matchCategory && matchSearch) {
                filtered.add(t);
            }
        }

        adapter.submitList(filtered);
    }



}


