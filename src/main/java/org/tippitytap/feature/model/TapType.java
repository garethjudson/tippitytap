package org.tippitytap.feature.model;

public enum TapType {
  ON,
  OFF;

  public static TapType parse(final String value) {
    if (value == null) {
      throw new IllegalArgumentException("TapType value cannot be null");
    }
    return valueOf(value.toUpperCase());
  }
}
