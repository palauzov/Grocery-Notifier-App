package com.example.grocerynotifier.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.grocerynotifier.activities.LoginActivity;
import com.example.grocerynotifier.activities.PrivacyPolicyActivity;
import com.example.grocerynotifier.activities.TermsAndConditionsActivity;
import com.example.grocerynotifier.daos.AccountDao;
import com.example.grocerynotifier.daos.AppDatabase;
import com.example.grocerynotifier.daos.UserDao;
import com.example.grocerynotifier.databinding.ProfileBinding;
import com.example.grocerynotifier.model.Account;
import com.example.grocerynotifier.model.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    private ProfileBinding binding;

    private AppDatabase db;
    private UserDao userDao;
    private AccountDao accountDao;

    private User currentUser;
    private Account currentAccount;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = ProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getInstance(requireContext());
        userDao = db.userDao();
        accountDao = db.accountDao();

        String email = requireActivity()
                .getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                .getString("email", null);

        if (email == null) {
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
            return;
        }

        executorService.execute(() -> {
            currentUser = userDao.getUserByEmail(email);

            if (currentUser != null) {
                currentAccount = accountDao.getAccountById(currentUser.getAccountId());
            }

            requireActivity().runOnUiThread(this::populateUserInfo);
        });

        setupClicks();
    }

    private void populateUserInfo() {
        if (currentUser == null || currentAccount == null) return;

        binding.textViewName.setText("Name: " + currentUser.getName());
        binding.textViewEmail.setText("Email: " + currentUser.getEmail());
        binding.textViewPhone.setText("Phone: " + currentUser.getPhone());
        binding.textViewBirthDate.setText("Birth Date: " + currentUser.getDateOfBirth());
        binding.textViewGender.setText("Gender: " + currentUser.getGender());

        // READ ONLY ACCOUNT INFO
        binding.textViewAccountType.setText(
                "Account Type: " + currentAccount.getAccountType().name()
        );

        binding.textViewAccountCode.setText(
                "Account Code: " + currentAccount.getId()
        );
    }

    private void setupClicks() {

        binding.textViewShowMembers.setOnClickListener(v -> {
            if (binding.layoutMembersContainer.getVisibility() == View.GONE) {
                showMembers();
                binding.layoutMembersContainer.setVisibility(View.VISIBLE);
                binding.textViewShowMembers.setText("Hide Members ▲");
            } else {
                binding.layoutMembersContainer.setVisibility(View.GONE);
                binding.textViewShowMembers.setText("Show Members ▼");
            }
        });

        binding.textViewTerms.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), TermsAndConditionsActivity.class))
        );

        binding.textViewPrivacy.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), PrivacyPolicyActivity.class))
        );

        binding.buttonLogout.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        });
    }

    private void showMembers() {
        if (currentUser == null) return;

        binding.layoutMembersContainer.removeAllViews();

        executorService.execute(() -> {
            List<com.example.grocerynotifier.model.User> members =
                    userDao.getUsersByAccountId(currentUser.getAccountId());

            requireActivity().runOnUiThread(() -> {
                for (com.example.grocerynotifier.model.User member : members) {
                    android.widget.TextView tv = new android.widget.TextView(requireContext());
                    tv.setText(member.getName());
                    tv.setPadding(8, 8, 8, 8);
                    binding.layoutMembersContainer.addView(tv);
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}