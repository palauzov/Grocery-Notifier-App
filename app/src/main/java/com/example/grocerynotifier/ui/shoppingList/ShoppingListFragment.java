package com.example.grocerynotifier.ui.shoppingList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grocerynotifier.R;
import com.example.grocerynotifier.adapters.ShopListAdapter;
import com.example.grocerynotifier.daos.AppDatabase;
import com.example.grocerynotifier.daos.ShopDao;
import com.example.grocerynotifier.databinding.FragmentShoppingListBinding;
import com.example.grocerynotifier.model.ShopList;
import com.example.grocerynotifier.utils.IdGenerator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShoppingListFragment extends Fragment {

    private FragmentShoppingListBinding binding;
    private ShopDao shopDao;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shopDao = AppDatabase.getInstance(requireContext()).shopDao();

        binding.recyclerLists.setLayoutManager(new LinearLayoutManager(requireContext()));

        setupClicks();
        loadLists();
    }

    private void setupClicks() {

        binding.btnCreateList.setOnClickListener(v -> {

            executor.execute(() -> {

                String listId = IdGenerator.generateId(8);

                String accountId = requireActivity()
                        .getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                        .getString("accountId", null);

                ShopList list = new ShopList(
                        listId,
                        accountId,
                        System.currentTimeMillis()
                );

                shopDao.insertList(list);

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "List created 🛒", Toast.LENGTH_SHORT).show();
                    loadLists();
                });
            });
        });
    }

    private void loadLists() {

        executor.execute(() -> {

            List<ShopList> lists = shopDao.getAllLists();

            requireActivity().runOnUiThread(() -> {

                ShopListAdapter adapter = new ShopListAdapter(lists, list -> {

                    Bundle bundle = new Bundle();
                    bundle.putString("listId", list.id);

                    NavController navController =
                            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);

                    navController.navigate(R.id.shoppingListItemsFragment, bundle);
                });

                binding.recyclerLists.setAdapter(adapter);
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}