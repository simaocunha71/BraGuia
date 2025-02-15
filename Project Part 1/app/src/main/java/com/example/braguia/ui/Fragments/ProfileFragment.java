package com.example.braguia.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.braguia.R;
import com.example.braguia.model.user.User;
import com.example.braguia.viewmodel.UserViewModel;

import java.io.IOException;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    private UserViewModel userViewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(){
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        try {
            userViewModel.getUser().observe(getViewLifecycleOwner(), x -> loadView(view, x));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return view;
    }

    private void loadView(View view, User user){
        TextView username = view.findViewById(R.id.profile_username);
        username.setText(user.getUsername());

        TextView user_type = view.findViewById(R.id.profile_usertype);
        user_type.setText(user.getUser_type());

        TextView fullName = view.findViewById(R.id.profile_fullname);
        String fullname = user.getFirst_name()+user.getLast_name();
        fullName.setText(fullname);

        TextView email = view.findViewById(R.id.profile_email);
        String emailString = user.getEmail();
        if(Objects.equals(emailString, ""))
            emailString="No Email";
        email.setText(emailString);

        Button trailHist = view.findViewById(R.id.profile_trailsHist_button);
        trailHist.setOnClickListener(e->replaceFragment());
    }

    private void replaceFragment() {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navHostFragment.getNavController().navigate(R.id.trailHistoryFragment);
    }
}