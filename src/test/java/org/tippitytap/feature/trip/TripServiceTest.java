package org.tippitytap.feature.trip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.tippitytap.feature.model.*;
import org.tippitytap.feature.pricing.PricingService;

class TripServiceTest {
  // We could use a mock here, for now the actual service is fine as its really simple
  private final TripService tripService = new TripService(new PricingService());

  @Test
  public void processTaps_handles_simple_case() {
    // Test to cover the simplest scenarios
    var result = tripService.processTaps(testTaps).stream().sorted().toList();

    assertEquals(3, result.size());

    // I could do this better... become used to kotlin data classes (automatic equality...spoilt,
    // java is so verbose)
    assertEquals(TripStatus.INCOMPLETE, result.get(0).getStatus());
    assertEquals("Stop3", result.get(0).getFromStopId());
    assertEquals(730, result.get(0).getChargeAmountCents());
    assertNull(result.get(0).getToStopId());
    assertEquals("Bus36", result.get(0).getBusId());
    assertEquals("Company1", result.get(0).getCompanyId());
    assertEquals(0, result.get(0).getDurationSeconds());

    assertEquals(TripStatus.COMPLETED, result.get(1).getStatus());
    assertEquals("Stop1", result.get(1).getFromStopId());
    assertEquals("Stop2", result.get(1).getToStopId());
    assertEquals(325, result.get(1).getChargeAmountCents());
    assertEquals("Bus37", result.get(1).getBusId());
    assertEquals("Company1", result.get(1).getCompanyId());
    assertEquals(300, result.get(1).getDurationSeconds());

    assertEquals(TripStatus.CANCELLED, result.get(2).getStatus());
    assertEquals("Stop1", result.get(2).getFromStopId());
    assertEquals("Stop1", result.get(2).getToStopId());
    assertEquals(0, result.get(2).getChargeAmountCents());
    assertEquals("Bus37", result.get(2).getBusId());
    assertEquals("Company1", result.get(2).getCompanyId());
    assertEquals(120, result.get(2).getDurationSeconds());
  }

  // this is very ugly :'(
  private final List<Tap> testTaps = // handle simple case
      List.of(
          new TapBuilder()
              .withId("1")
              .withTimestamp(Instant.parse("2023-01-22T13:00:00Z"))
              .withType(TapType.ON)
              .withStopId("Stop1")
              .withCompanyId("Company1")
              .withBusId("Bus37")
              .withPan("5500005555555559")
              .build(),
          new TapBuilder()
              .withId("2")
              .withTimestamp(Instant.parse("2023-01-22T13:05:00Z"))
              .withType(TapType.OFF)
              .withStopId("Stop2")
              .withCompanyId("Company1")
              .withBusId("Bus37")
              .withPan("5500005555555559")
              .build(),
          new TapBuilder()
              .withId("3")
              .withTimestamp(Instant.parse("2023-01-22T09:20:00Z"))
              .withType(TapType.ON)
              .withStopId("Stop3")
              .withCompanyId("Company1")
              .withBusId("Bus36")
              .withPan("4111111111111111")
              .build(),
          new TapBuilder()
              .withId("4")
              .withTimestamp(Instant.parse("2023-01-23T08:00:00Z"))
              .withType(TapType.ON)
              .withStopId("Stop1")
              .withCompanyId("Company1")
              .withBusId("Bus37")
              .withPan("4111111111111111")
              .build(),
          new TapBuilder()
              .withId("5")
              .withTimestamp(Instant.parse("2023-01-23T08:02:00Z"))
              .withType(TapType.OFF)
              .withStopId("Stop1")
              .withCompanyId("Company1")
              .withBusId("Bus37")
              .withPan("4111111111111111")
              .build());
}
