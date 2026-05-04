package com.example.grocerynotifier.ui.shoppingList;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grocerynotifier.adapters.ShopItemAdapter;
import com.example.grocerynotifier.daos.AppDatabase;
import com.example.grocerynotifier.daos.ShopDao;
import com.example.grocerynotifier.databinding.ListItemsFragmentBinding;
import com.example.grocerynotifier.model.ShopItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShopListItemsFragment extends Fragment {

    private ListItemsFragmentBinding binding;
    private ShopDao shopDao;
    private String listId;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = ListItemsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shopDao = AppDatabase.getInstance(requireContext()).shopDao();

        listId = getArguments() != null ? getArguments().getString("listId") : null;

        binding.recyclerViewItems.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        loadItems();

        binding.buttonAddItem.setOnClickListener(v -> {

            String name = binding.editTextItem.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(requireContext(), "Enter item name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (listId == null) {
                Toast.makeText(requireContext(), "List not found", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = 1;

            executor.execute(() -> {

                ShopItem item = new ShopItem(name, listId, quantity);

                shopDao.insertItem(item);

                loadItems();

                requireActivity().runOnUiThread(() ->
                        binding.editTextItem.setText("")
                );
            });
        });
    }

    private void loadItems() {

        executor.execute(() -> {

            List<ShopItem> items = shopDao.getItems(listId);

            requireActivity().runOnUiThread(() -> {

                ShopItemAdapter adapter =
                        new ShopItemAdapter(items, shopDao);

                binding.recyclerViewItems.setAdapter(adapter);
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}