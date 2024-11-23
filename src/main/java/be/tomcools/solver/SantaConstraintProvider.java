package be.tomcools.solver;

import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import be.tomcools.domain.Santa;
import be.tomcools.geo.HaversineDrivingTimeCalculator;
import org.jspecify.annotations.NonNull;

public class SantaConstraintProvider implements ConstraintProvider {
    HaversineDrivingTimeCalculator drivingTimeCalculator = HaversineDrivingTimeCalculator.getInstance();
    public static final String MINIMIZE_TRAVEL_TIME = "minimizeTravelTime";

    @Override
    public Constraint @NonNull [] defineConstraints(@NonNull ConstraintFactory factory) {
        return new Constraint[]{
                minimizeTravelTime(factory),
        };
    }

    Constraint minimizeTravelTime(ConstraintFactory factory) {
        return factory.forEach(Santa.class)
                .penalizeLong(HardSoftLongScore.ONE_SOFT,
                        santa -> drivingTimeCalculator.getTotalDrivingTimeSeconds(santa))
                .asConstraint(MINIMIZE_TRAVEL_TIME);
    }
}
