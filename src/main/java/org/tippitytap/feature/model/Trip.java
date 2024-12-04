package org.tippitytap.feature.model;

import java.time.Instant;

public class Trip implements Comparable<Trip> {
  // Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN,
  // Status
  private Instant started;

  private Instant ended;

  private long durationSeconds;

  private String fromStopId;

  private String toStopId;

  private int chargeAmountCents;

  private String companyId;

  private String busId;

  public TripStatus getStatus() {
    return status;
  }

  public void setStatus(TripStatus status) {
    this.status = status;
  }

  public String getPan() {
    return pan;
  }

  public void setPan(String pan) {
    this.pan = pan;
  }

  private TripStatus status;

  private String pan;

  public Instant getStarted() {
    return started;
  }

  public void setStarted(Instant started) {
    this.started = started;
  }

  public Instant getEnded() {
    return ended;
  }

  public void setEnded(Instant ended) {
    this.ended = ended;
  }

  public long getDurationSeconds() {
    return durationSeconds;
  }

  public void setDurationSeconds(long durationSeconds) {
    this.durationSeconds = durationSeconds;
  }

  public String getFromStopId() {
    return fromStopId;
  }

  public void setFromStopId(String fromStopId) {
    this.fromStopId = fromStopId;
  }

  public String getToStopId() {
    return toStopId;
  }

  public void setToStopId(String toStopId) {
    this.toStopId = toStopId;
  }

  public int getChargeAmountCents() {
    return chargeAmountCents;
  }

  public void setChargeAmountCents(int chargeAmountCents) {
    this.chargeAmountCents = chargeAmountCents;
  }

  public String getCompanyId() {
    return companyId;
  }

  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

  public String getBusId() {
    return busId;
  }

  public void setBusId(String busId) {
    this.busId = busId;
  }

  @Override
  // order is determined by start date and the from stop id
  public int compareTo(Trip o) {
    var dateCompare = this.started.compareTo(o.started);
    if (dateCompare == 0) {
      return getFromStopId().compareTo(o.fromStopId);
    }
    return dateCompare;
  }
}
