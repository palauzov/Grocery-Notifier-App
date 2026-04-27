package com.example.grocerynotifier.ui.products;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grocerynotifier.daos.AppDatabase;
import com.example.grocerynotifier.databinding.MyProductsBinding;
import com.example.grocerynotifier.model.Product;
import com.example.grocerynotifier.utils.ProductAdapter;

import java.util.List;
import java.util.concurrent.Executors;

public class ProductsFragment extends Fragment {

    private MyProductsBinding binding;
    private AppDatabase db;
    private ProductAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // 1. Initialize Binding
        binding = MyProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 2. Setup RecyclerView
        binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext()));

        // 3. Initialize Database
        db = AppDatabase.getInstance(requireContext());

        // 4. Load the data
        loadProducts();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProducts(); // Refresh list when user switches back to this fragment
    }

    private void loadProducts() {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Get accountId from SharedPreferences
            SharedPreferences prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            String accountId = prefs.getString("accountId", null);

            if (accountId != null) {
                List<Product> productList = db.productDao().getProductsByAccount(accountId);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Handle Empty State
                        if (productList.isEmpty()) {
                            binding.tvEmptyMessage.setVisibility(View.VISIBLE);
                        } else {
                            binding.tvEmptyMessage.setVisibility(View.GONE);
                        }

                        // Setup Adapter
                        adapter = new ProductAdapter(productList, product -> deleteProduct(product));
                        binding.recyclerViewProducts.setAdapter(adapter);
                    });
                }
            }
        });
    }

    private void deleteProduct(Product product) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.productDao().delete(product);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Product removed", Toast.LENGTH_SHORT).show();
                    loadProducts(); // Refresh list
                });
            }
        });
    }
}