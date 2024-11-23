package be.tomcools;

import ai.timefold.solver.core.api.solver.*;
import be.tomcools.domain.Location;
import be.tomcools.domain.Santa;
import be.tomcools.domain.SantaPlan;
import be.tomcools.domain.Visit;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("santa")
public class SantaResource {

    private final SolverFactory<SantaPlan> solverFactory;

    public SantaResource(SolverFactory<SantaPlan> solverFactory) {
        this.solverFactory = solverFactory;
    }

    /**
     * Uses a solver to optimally sort a list of visits
     * @param visits the locations santa needs to visit
     * @return an optimized list of visits ordered
     */
    @POST
    @Path("plan")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SantaPlan solve(List<Visit> visits) {
        Santa santa = new Santa(new Location(66.5039, 25.7294));
        SantaPlan santaPlan = new SantaPlan(santa, visits);

        Solver<SantaPlan> solver = solverFactory.buildSolver();
        SantaPlan solution = solver.solve(santaPlan);
        return solution;
    }
}
