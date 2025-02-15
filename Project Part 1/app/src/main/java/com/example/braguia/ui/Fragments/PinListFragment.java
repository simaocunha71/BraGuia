package com.example.braguia.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.ui.viewAdapters.PinsRecyclerViewAdapter;
import com.example.braguia.viewmodel.TrailViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PinListFragment extends Fragment {
    private TrailViewModel trailViewModel;
    private static final String ARG_TRAIL_LIST = "TRAIL_LIST";
    private static final String ARG_PIN_LIST = "PIN_LIST";
    private String type;
    private List<Integer> ids;



    public static PinListFragment newInstanceByTrails(List<Integer> ids) {
        PinListFragment fragment = new PinListFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_TRAIL_LIST, new ArrayList<>(ids));
        fragment.setArguments(args);

        return fragment;
    }

    public static PinListFragment newInstanceByPins(List<Integer> ids) {
        PinListFragment fragment = new PinListFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_PIN_LIST, new ArrayList<>(ids));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            List<Integer> idsList = getArguments().getIntegerArrayList(ARG_TRAIL_LIST);
            if(idsList != null) {
                ids = idsList;
                type= "trails";
            } else {
                ids = getArguments().getIntegerArrayList(ARG_PIN_LIST);
                type= "pins";
                if(ids == null) {
                    throw new IllegalArgumentException("Both trail ID list and pin ID list are null");
                }
            }
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edge_list, container, false);

        trailViewModel = new ViewModelProvider(requireActivity()).get(TrailViewModel.class);
        if(Objects.equals(type, "trails")){
            trailViewModel.getTrailsById(ids).observe(getViewLifecycleOwner(), trails -> {
                List<EdgeTip> edgeTips = trails.stream()
                        .map(e->e.getEdges()
                                .stream()
                                .flatMap(es -> Stream.of(es.getEdge_start(), es.getEdge_end()))
                                .collect(Collectors.toList()))
                        .flatMap(List::stream)
                        .distinct()
                        .collect(Collectors.toList());
                loadView(view, edgeTips);
            });
        } else if (type=="pins") {
            trailViewModel.getPinsById(ids).observe(getViewLifecycleOwner(), x -> loadView(view, x));
        }
        return view;
    }

    private void loadView(View view, List<EdgeTip> edgeTips){
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);

            PinsRecyclerViewAdapter adapter = new PinsRecyclerViewAdapter(edgeTips);
            recyclerView.setAdapter(adapter);


            adapter.setOnItemClickListener(this::replaceFragment);
        }
    }

    private void replaceFragment(EdgeTip edgeTip) { //TODO maybe adicionar um backtrace a partir da main activity para tornar o fragmento mais fléxivel
        Bundle bundle = new Bundle();
        bundle.putInt("id", edgeTip.getId());

        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navHostFragment.getNavController().navigate(R.id.pinFragment,bundle);
    }
}

