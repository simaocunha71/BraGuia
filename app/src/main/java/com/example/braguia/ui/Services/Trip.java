package com.example.braguia.ui.Services;

import android.content.Context;
import android.location.Location;

import com.example.braguia.model.TrailMetrics.TrailMetrics;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Trip implements Serializable {
    private long timeStart;
    private long distance;
    private Trail trail;
    private List<EdgeTip> percorridos;
    private List<EdgeTip> previstos;

    public Trip(Trail trail) {
        this.timeStart = new Date().getTime();
        this.distance = 0;
        this.trail=trail;
        this.percorridos= new ArrayList<>();
        this.previstos=trail.getRoute();
    }

    public TrailMetrics finish(String username) {
        float percentageCompletion = (float) percorridos.size()/(previstos.size()+percorridos.size())*100;
        float timeTaken = (float) (new Date().getTime() - timeStart)/1000; //returns in seconds
        List<Integer> p= percorridos.stream().map(EdgeTip::getId).collect(Collectors.toList());
        return new TrailMetrics(username,trail.getId(),percentageCompletion,timeTaken,p);
    }

    public EdgeTip verifyPins(Location location) {
        /*
        for (EdgeTip pin:previstos){
                percorridos.add(pin);
                previstos.remove(pin);
                return pin;
        }*/
        for (EdgeTip pin:previstos){
            float distance = pin.getLocation().distanceTo(location);
            if(distance <= 50){
                percorridos.add(pin);
                previstos.remove(pin);
                return pin;
            }
        }
        return null;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public long getDistance() {
        return distance;
    }


    public List<EdgeTip> getPercorridos() {
        return percorridos;
    }

    public List<EdgeTip> getPrevistos() {
        return previstos;
    }

    public Trail getTrail() {
        return trail;
    }
}
