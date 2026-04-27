package com.example.grocerynotifier.ui.addProduct;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.grocerynotifier.daos.AppDatabase;
import com.example.grocerynotifier.databinding.AddProductBinding;
import com.example.grocerynotifier.model.Product;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AddProductFragment extends Fragment {

    private AddProductBinding binding;
    private AppDatabase db;
    private String accountId;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = AddProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        db = AppDatabase.getInstance(requireContext());

        SharedPreferences prefs = requireContext()
                .getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

        accountId = prefs.getString("accountId", null);

        binding.expirationDate.setOnClickListener(v -> showDatePicker());

        binding.btnAddProduct.setOnClickListener(v -> addProduct());

        if (getArguments() != null) {
            String scannedBarcode = getArguments().getString("barcode");

            if (scannedBarcode != null) {
                binding.barcode.setText(scannedBarcode);
                fetchProductFromBarcode(scannedBarcode);
            }
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    binding.expirationDate.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void addProduct() {

        String barcodeStr = binding.barcode.getText().toString().trim();
        String name = binding.productName.getText().toString().trim();
        String expiryStr = binding.expirationDate.getText().toString().trim();

        if (TextUtils.isEmpty(barcodeStr) ||
                TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(expiryStr)) {

            Toast.makeText(getContext(),
                    "Fill required fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Product product = new Product();
                product.setBarcode(barcodeStr);
                product.setName(name);
                product.setExpirationDate(sdf.parse(expiryStr));
                product.setBoughtDate(new Date());
                product.setAccountId(accountId);

                db.productDao().insert(product);

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(),
                            "Product added",
                            Toast.LENGTH_SHORT).show();

                    clearFields();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void clearFields() {
        binding.barcode.setText("");
        binding.productName.setText("");
        binding.expirationDate.setText("");
        binding.weight.setText("");
        binding.price.setText("");
    }

    private void fetchProductFromBarcode(String code) {

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                URL url = new URL(
                        "https://world.openfoodfacts.org/api/v0/product/" +
                                code + ".json"
                );

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                JSONObject json = new JSONObject(sb.toString());

                if (json.optInt("status") == 1) {
                    String name = json.getJSONObject("product")
                            .optString("product_name", "");

                    requireActivity().runOnUiThread(() ->
                            binding.productName.setText(name)
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}