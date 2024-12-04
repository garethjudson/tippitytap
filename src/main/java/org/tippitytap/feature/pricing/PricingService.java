package org.tippitytap.feature.pricing;

import java.util.HashMap;
import java.util.Map;

// could be an interface if there was different pricing models
// could then use a pattern to choose the right pricing strategy
// only one implementation, no need for an interface or elegant abstraction
// note: if you call a class "...Impl" you have failed at naming.
public class PricingService {
  private final Map<String, Map<String, Integer>> pricingSchedule;

  public PricingService() {
    // A very naive way to determine pricing
    // just link each to stop to every other stop, as there are only 3
    // I wouldn't do this in a prod app, but for the sake of this we just needed something simple

    // Trips between Stop 1 and Stop 2 cost $3.25
    // Trips between Stop 2 and Stop 3 cost $5.50
    // Trips between Stop 1 and Stop 3 cost $7.30
    // Cancelled (where both tap on and off are the same) cost 0
    // Incomplete (where 'to' stop is empty) -- enter max cost separately
    pricingSchedule = new HashMap<>();
    pricingSchedule.put("Stop1", Map.of("Stop2", 325, "Stop3", 730, "Stop1", 0, "", 730));
    pricingSchedule.put("Stop2", Map.of("Stop1", 325, "Stop3", 550, "Stop2", 0, "", 550));
    pricingSchedule.put("Stop3", Map.of("Stop3", 0, "Stop2", 550, "Stop1", 730, "", 730));
  }

  // I don't like the exceptions but will leave for now
  public int getCharge(String fromStopId, String toStopId) {
    if (fromStopId == null) {
      throw new IllegalArgumentException("fromStopId cannot be null");
    }

    var scheduleForStop = pricingSchedule.get(fromStopId);
    if (scheduleForStop == null) {
      throw new IllegalArgumentException(String.format("fromStopId %s is invalid", fromStopId));
    }

    // use empty string to store the price of an incomplete stop
    var safeToStopId = toStopId == null ? "" : toStopId;
    var result = scheduleForStop.get(safeToStopId);
    if (result == null) {
      throw new IllegalArgumentException(String.format("toStopId %s is invalid", toStopId));
    }
    return result;
  }
}
