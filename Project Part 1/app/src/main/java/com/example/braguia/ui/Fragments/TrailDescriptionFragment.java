package com.example.braguia.ui.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.braguia.R;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.ui.Activitys.NavigationActivity;
import com.example.braguia.viewmodel.TrailViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrailDescriptionFragment extends Fragment {
    private TrailViewModel trailViewModel;
    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            this.id = getArguments().getInt("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!MapsFragment.meetsPreRequisites(getContext())) {
            Toast toast = Toast.makeText(getContext(), "Install Google Maps to navigate", Toast.LENGTH_SHORT);
            toast.show();
        }
        View view = inflater.inflate(R.layout.fragment_trail_description, container, false);

        trailViewModel = new ViewModelProvider(requireActivity()).get(TrailViewModel.class);
        trailViewModel.getTrailById(id).observe(getViewLifecycleOwner(), x -> loadView(view, x));
        return view;
    }

    private void loadView(View view, Trail trail) {
        TextView titulo = view.findViewById(R.id.trail_description_title);
        titulo.setText(trail.getTrail_name());

        ImageView imagem = view.findViewById(R.id.trailImageDescription);
        Picasso.get().load(trail.getTrail_img()
                        .replace("http", "https"))
                .into(imagem);
        Button intro = view.findViewById(R.id.start_trip_button);

        intro.setOnClickListener(v -> {
            if (NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()) {
                Intent intent = new Intent(requireActivity(), NavigationActivity.class);
                intent.putExtra("id", trail.getId()); // add a string extra
                startActivity(intent);
            } else {
                // Create an intent to open app notification settings
                Intent settingsIntent = new Intent();
                settingsIntent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                settingsIntent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().getPackageName());

                // Check if the intent can be resolved
                if (settingsIntent.resolveActivity(requireContext().getPackageManager()) != null) {
                    startActivity(settingsIntent);
                } else {
                    // Provide an alternative action or message if the intent cannot be resolved
                    Toast.makeText(requireContext(), "Please enable notifications in your device settings", Toast.LENGTH_SHORT).show();
                }
            }
        });



        FragmentManager childFragmentManager = getChildFragmentManager();
        EdgeListFragment childFragment = EdgeListFragment.newInstanceByTrails(new ArrayList<>(List.of(id)));
        FragmentTransaction transaction1 = childFragmentManager.beginTransaction();
        transaction1.add(R.id.pin_list_content, childFragment);
        transaction1.commit();


        MapsFragment mapsFragment = MapsFragment.newInstance(trail.getRoute());
        FragmentTransaction transaction2 = childFragmentManager.beginTransaction();
        transaction2.replace(R.id.maps_trail_overview, mapsFragment);
        transaction2.commit();
    }
}