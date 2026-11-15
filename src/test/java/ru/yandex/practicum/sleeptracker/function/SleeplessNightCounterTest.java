package ru.yandex.practicum.sleeptracker.function;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.sleeptracker.function.SleepingSessionTestData.session;

class SleeplessNightCounterTest {
    private final SleeplessNightCounter counter = new SleeplessNightCounter();

    @Test
    void shouldNotCountNightsWithSleepAsSleepless() {
        var sessions = List.of(
                session("2025-10-01T23:00", "2025-10-02T03:00"),
                session("2025-10-03T02:00", "2025-10-03T07:00")
        );

        assertEquals(0L, counter.apply(sessions).value());
    }

    @Test
    void shouldCountPreviousNightAsSleeplessWhenSleepStartsAtSeven() {
        var sessions = List.of(session("2025-10-02T07:00", "2025-10-02T11:00"));

        assertEquals(1L, counter.apply(sessions).value());
    }

    @Test
    void shouldStartCountingFromNextNightWhenFirstSessionStartsAfterNoon() {
        var sessions = List.of(
                session("2025-10-01T13:00", "2025-10-01T14:00"),
                session("2025-10-02T07:00", "2025-10-02T11:00")
        );

        assertEquals(1L, counter.apply(sessions).value());
    }

    @Test
    void shouldCountSleeplessNightsAcrossMonthBoundary() {
        var sessions = List.of(
                session("2025-01-30T23:00", "2025-01-31T08:00"),
                session("2025-02-02T23:00", "2025-02-03T08:00")
        );

        assertEquals(2L, counter.apply(sessions).value());
    }
}
