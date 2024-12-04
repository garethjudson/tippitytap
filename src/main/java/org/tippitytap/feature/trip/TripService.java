package org.tippitytap.feature.trip;

import java.util.*;
import org.tippitytap.feature.model.Tap;
import org.tippitytap.feature.model.TapType;
import org.tippitytap.feature.model.Trip;
import org.tippitytap.feature.model.TripBuilder;
import org.tippitytap.feature.pricing.PricingService;

public class TripService {
  private final PricingService pricingService;

  public TripService(PricingService pricingService) {
    this.pricingService = pricingService;
  }

  public List<Trip> processTaps(List<Tap> tapList) {
    Map<String, Tap> startedTrips = new HashMap<>();
    List<Trip> finalResult = new ArrayList<>();

    // order taps by timestamp
    // the next tap for a pan should be in order
    // assumption is no two cards have the same pan
    // e.g. not possible for two people to use the same card on different trips at the same time
    // we could use a composite of company/bus/pan to make this more realistic
    // but for simplicity I've just used pan
    tapList.stream()
        .sorted(Comparator.comparing(Tap::getTimestamp))
        .forEach(
            (tap) -> {
              // could consider grouping and then doing a reduce
              // but generally a simple loop is broadly easier to understand
              var currentIncomplete = startedTrips.get(tap.getPan());
              if (tap.getType() == TapType.ON) {
                // tap on means the start of a new trip
                // if there is an already started trip, it will not be completed
                if (currentIncomplete != null) {
                  // the limit of nesting...
                  finalResult.add(createTrip(currentIncomplete, null));
                  startedTrips.remove(tap.getPan());
                }
                startedTrips.put(tap.getPan(), tap);
              } else {
                // else tap off
                if (currentIncomplete != null) {
                  finalResult.add(createTrip(currentIncomplete, tap));
                  startedTrips.remove(tap.getPan());
                } else {
                  // oh dear tap off, then tap off, this is a system failure :-(
                  throw new IllegalArgumentException(
                      "supplied taps are invalid, tap off for PAN before tap ON");
                }
              }
            });
    if (!startedTrips.isEmpty()) {
      startedTrips.values().stream().map((tap) -> createTrip(tap, null)).forEach(finalResult::add);
    }
    return finalResult;
  }

  private Trip createTrip(Tap tapOn, Tap tapOff) {
    return TripBuilder.withFrom(tapOn).withTo(tapOff).withPricingService(pricingService).build();
  }
}
