package org.tippitytap.feature.model;

import org.tippitytap.feature.pricing.PricingService;

public class TripBuilder {
  private Tap from;
  private Tap to;
  private PricingService pricingService;

  public static TripBuilder withFrom(Tap tap) {
    TripBuilder builder = new TripBuilder();
    builder.from = tap;
    return builder;
  }

  public TripBuilder withTo(Tap tap) {
    this.to = tap;
    return this;
  }

  public TripBuilder withPricingService(PricingService pricingService) {
    this.pricingService = pricingService;
    return this;
  }

  public Trip build() {
    Trip trip = new Trip();
    trip.setFromStopId(from.getStopId());
    trip.setBusId(from.getBusId());
    trip.setCompanyId(from.getCompanyId());
    trip.setStarted(from.getTimestamp());
    trip.setPan(from.getPan());

    String toStopId = null;
    if (to != null) {
      trip.setEnded(to.getTimestamp());
      trip.setDurationSeconds(
          to.getTimestamp().getEpochSecond() - from.getTimestamp().getEpochSecond());
      trip.setToStopId(to.getStopId());
      toStopId = to.getStopId();
    }
    trip.setStatus(TripStatus.fromStops(from.getStopId(), toStopId));
    trip.setChargeAmountCents(pricingService.getCharge(from.getStopId(), toStopId));

    return trip;
  }
}
