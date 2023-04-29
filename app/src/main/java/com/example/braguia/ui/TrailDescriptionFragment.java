package com.example.braguia.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.braguia.R;
import com.example.braguia.model.app.AppInfo;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.viewmodel.TrailViewModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class TrailDescriptionFragment extends Fragment {
    private TrailViewModel trailViewModel;
    private final int id;
    public TrailDescriptionFragment(int id){
        this.id=id;
    }

    public static TrailDescriptionFragment newInstance(int id) {
        return new TrailDescriptionFragment(id);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trail_description, container, false);


        trailViewModel = new ViewModelProvider(requireActivity()).get(TrailViewModel.class);
        trailViewModel.getTrailById(id).observe(getViewLifecycleOwner(), x -> loadView(view, x));
        return view;
    }

    private void loadView(View view, Trail trail){
        ImageView imagem = view.findViewById(R.id.trailImageDescription);
        Picasso.get().load(trail.getTrail_img()
                        .replace("http", "https"))
                        .into(imagem);
        Button intro = view.findViewById(R.id.start_trip_button);
    }
}

