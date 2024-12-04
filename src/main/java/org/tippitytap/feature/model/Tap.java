package org.tippitytap.feature.model;

import java.time.Instant;

public class Tap {
  // ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN
  private String id;

  private Instant timestamp;

  private TapType type;

  private String stopId;

  private String companyId;

  private String busId;

  private String pan;

  public void setId(String id) {
    this.id = id;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }

  public void setType(TapType type) {
    this.type = type;
  }

  public void setStopId(String stopId) {
    this.stopId = stopId;
  }

  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

  public void setBusId(String busId) {
    this.busId = busId;
  }

  public void setPan(String pan) {
    this.pan = pan;
  }

  public String getId() {
    return id;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public TapType getType() {
    return type;
  }

  public String getStopId() {
    return stopId;
  }

  public String getCompanyId() {
    return companyId;
  }

  public String getBusId() {
    return busId;
  }

  public String getPan() {
    return pan;
  }
}
