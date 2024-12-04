package org.tippitytap.feature.model;

public enum TripStatus {
  COMPLETED,
  INCOMPLETE,
  CANCELLED;

  // could extract this if it becomes complicated
  public static TripStatus fromStops(String fromStop, String toStop) {
    if (toStop == null) {
      return INCOMPLETE;
    } else if (toStop.equals(fromStop)) {
      return CANCELLED;
    } else {
      return COMPLETED;
    }
  }
}
