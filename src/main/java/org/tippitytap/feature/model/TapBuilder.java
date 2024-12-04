package org.tippitytap.feature.model;

import java.time.Instant;

public class TapBuilder {
  // ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN
  private String id;

  private Instant timestamp;

  private TapType type;

  private String stopId;

  private String companyId;

  private String busId;

  private String pan;

  public TapBuilder withId(String id) {
    this.id = id;
    return this;
  }

  public TapBuilder withTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public TapBuilder withType(TapType type) {
    this.type = type;
    return this;
  }

  public TapBuilder withStopId(String stopId) {
    this.stopId = stopId;
    return this;
  }

  public TapBuilder withCompanyId(String companyId) {
    this.companyId = companyId;
    return this;
  }

  public TapBuilder withBusId(String busId) {
    this.busId = busId;
    return this;
  }

  public TapBuilder withPan(String pan) {
    this.pan = pan;
    return this;
  }

  public Tap build() {
    Tap tap = new Tap();
    tap.setId(id);
    tap.setTimestamp(timestamp);
    tap.setType(type);
    tap.setStopId(stopId);
    tap.setCompanyId(companyId);
    tap.setBusId(busId);
    tap.setPan(pan);
    return tap;
  }
}
