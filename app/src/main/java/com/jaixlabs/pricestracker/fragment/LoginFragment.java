package com.jaixlabs.pricestracker.fragment;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.jaixlabs.pricestracker.R;
import com.jaixlabs.pricestracker.Repository.PriceRepository;
import com.jaixlabs.pricestracker.ViewModel.PriceViewModel;
import com.jaixlabs.pricestracker.ViewModel.PriceViewModelFactory;
import com.jaixlabs.pricestracker.databinding.FragmentLoginBinding;


import java.io.IOException;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private PriceViewModel viewModel;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        PriceRepository repository = new PriceRepository(requireActivity());
        PriceViewModelFactory factory = new PriceViewModelFactory(repository);
        viewModel = new ViewModelProvider(requireActivity(), factory).get(PriceViewModel.class);

        initSharedPrefs();
        setListeners();

        return binding.getRoot();
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

    private void setListeners() {
        binding.loginButton.setOnClickListener(v -> {
            String username = binding.usernameEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Username & Password required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isInternetAvailable()) {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                return;
            }

            login(username, password);
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


    private void login(String username, String password) {
        // Show ProgressBar, Hide Button
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.loginButton.setEnabled(false);

        viewModel.login(username, password).observe(getViewLifecycleOwner(), result -> {
            // Hide ProgressBar, Enable Button
            binding.progressBar.setVisibility(View.GONE);
            binding.loginButton.setEnabled(true);

            if (result != null && result.isSuccessful() && result.body() != null) {
                String token = result.body().getToken();
                sharedPreferences.edit().putString("auth_token", token).apply();

                Toast.makeText(getContext(), "Login Success", Toast.LENGTH_SHORT).show();

                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_loginFragment_to_homeFragment);
            } else {
                String message = "Login Failed";

                if (result == null) {
                    message = "No response from server";
                } else if (result.errorBody() != null) {
                    try {
                        message = result.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @SuppressLint("ClickableViewAccessibility")
    private  void togglePassword(){
        binding.passwordEditText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (binding.passwordEditText.getRight() - binding.passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    int selection = binding.passwordEditText.getSelectionEnd();
                    if (binding.passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        // Show password
                        binding.passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                       binding.passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_off, 0);
                    } else {
                        // Hide password
                       binding.passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                       binding.passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye, 0);
                    }
                    binding.passwordEditText.setSelection(selection); // cursor same position
                    return true;
                }
            }
            return false;
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
