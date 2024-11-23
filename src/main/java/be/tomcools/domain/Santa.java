package be.tomcools.domain;

import java.util.ArrayList;
import java.util.List;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.PlanningListVariable;

@PlanningEntity
public class Santa {

    private Location homeLocation;

    @PlanningListVariable
    private List<Visit> visits;

    public Santa() {
    }

    public Santa(Location homeLocation) {
        this.homeLocation = homeLocation;
        this.visits = new ArrayList<>();}

    public Location getHomeLocation() {
        return homeLocation;
    }

    public void setHomeLocation(Location homeLocation) {
        this.homeLocation = homeLocation;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }
}