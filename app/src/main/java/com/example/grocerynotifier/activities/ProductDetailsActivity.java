package com.example.grocerynotifier.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grocerynotifier.R;
import com.example.grocerynotifier.daos.AppDatabase;
import com.example.grocerynotifier.daos.ProductDao;
import com.example.grocerynotifier.model.Product;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView tvName, tvBarcode, tvPrice, tvWeight, tvBought, tvExpiry;

    private AppDatabase db;
    private ProductDao productDao;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        tvName = findViewById(R.id.tvName);
        tvBarcode = findViewById(R.id.tvBarcode);
        tvPrice = findViewById(R.id.tvPrice);
        tvWeight = findViewById(R.id.tvWeight);
        tvBought = findViewById(R.id.tvBought);
        tvExpiry = findViewById(R.id.tvExpiry);

        db = AppDatabase.getInstance(getApplicationContext());
        productDao = db.productDao();

        String barcode = getIntent().getStringExtra("barcode");

        if (barcode != null) {
            loadProduct(barcode);
        }
    }

    private void loadProduct(String barcode) {
        executor.execute(() -> {
            Product p = productDao.getByBarcode(barcode);

            if (p != null) {
                runOnUiThread(() -> {
                    tvName.setText(p.getName());
                    tvBarcode.setText("Barcode: " + p.getBarcode());
                    tvPrice.setText("Price: €" + p.getPrice());
                    tvWeight.setText("Weight: " + p.getWeight() + "g");
                    tvBought.setText("Bought: " + p.getBoughtDate());
                    tvExpiry.setText("Expires: " + p.getExpirationDate());
                });
            }
        });
    }
}