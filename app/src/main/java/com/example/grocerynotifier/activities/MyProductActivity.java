package com.example.grocerynotifier.activities;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grocerynotifier.daos.AppDatabase;
import com.example.grocerynotifier.databinding.MyProductsBinding;
import com.example.grocerynotifier.model.Product;
import com.example.grocerynotifier.utils.ProductAdapter;

import java.util.List;
import java.util.concurrent.Executors;

public class MyProductActivity extends AppCompatActivity {

    private MyProductsBinding binding;
    private AppDatabase db;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MyProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerViewProducts.setLayoutManager(
                new LinearLayoutManager(this)
        );

        db = AppDatabase.getInstance(this);

        loadProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    private void loadProducts() {

        Executors.newSingleThreadExecutor().execute(() -> {

            SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            String accountId = prefs.getString("accountId", null);

            if (accountId == null) return;

            List<Product> productList =
                    db.productDao().getProductsByAccount(accountId);

            runOnUiThread(() -> {

                if (productList.isEmpty()) {
                    binding.tvEmptyMessage.setVisibility(View.VISIBLE);
                } else {
                    binding.tvEmptyMessage.setVisibility(View.GONE);
                }

                adapter = new ProductAdapter(

                        productList,

                        // DELETE
                        this::deleteProduct,

                        // CLICK → OPEN DETAILS
                        product -> {
                            Intent i = new Intent(
                                    MyProductActivity.this,
                                    ProductDetailsActivity.class
                            );
                            i.putExtra("barcode", product.getBarcode());
                            startActivity(i);
                        }
                );

                binding.recyclerViewProducts.setAdapter(adapter);
            });
        });
    }

    private void deleteProduct(Product product) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.productDao().delete(product);

            runOnUiThread(() -> {
                Toast.makeText(this,
                        "Product removed",
                        Toast.LENGTH_SHORT).show();

                loadProducts();
            });
        });
    }
}