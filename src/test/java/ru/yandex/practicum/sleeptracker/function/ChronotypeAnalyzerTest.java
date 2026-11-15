package ru.yandex.practicum.sleeptracker.function;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.sleeptracker.function.SleepingSessionTestData.session;

class ChronotypeAnalyzerTest {
    private final ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

    @Test
    void shouldClassifyLateBedtimeAndLateWakeupAsOwl() {
        var sessions = List.of(session("2025-10-01T23:30", "2025-10-02T09:30"));

        assertEquals("Сова", analyzer.apply(sessions).value());
    }

    @Test
    void shouldClassifyEarlyBedtimeAndEarlyWakeupAsLark() {
        var sessions = List.of(session("2025-10-01T21:30", "2025-10-02T06:30"));

        assertEquals("Жаворонок", analyzer.apply(sessions).value());
    }

    @Test
    void shouldClassifyIntermediateScheduleAsPigeon() {
        var sessions = List.of(session("2025-10-01T22:30", "2025-10-02T08:00"));

        assertEquals("Голубь", analyzer.apply(sessions).value());
    }

    @Test
    void shouldChooseMostFrequentType() {
        var sessions = List.of(
                session("2025-10-01T23:30", "2025-10-02T09:30"),
                session("2025-10-02T21:30", "2025-10-03T06:30"),
                session("2025-10-04T23:30", "2025-10-05T09:30")
        );

        assertEquals("Сова", analyzer.apply(sessions).value());
    }

    @Test
    void shouldChoosePigeonWhenOwlAndLarkCountsAreEqual() {
        var sessions = List.of(
                session("2025-10-01T23:30", "2025-10-02T09:30"),
                session("2025-10-02T21:30", "2025-10-03T06:30")
        );

        assertEquals("Голубь", analyzer.apply(sessions).value());
    }

    @Test
    void shouldIgnoreDaytimeSessionsAndSleeplessNights() {
        var sessions = List.of(
                session("2025-10-01T23:30", "2025-10-02T09:30"),
                session("2025-10-03T07:00", "2025-10-03T11:00"),
                session("2025-10-04T14:00", "2025-10-04T16:00")
        );

        assertEquals("Сова", analyzer.apply(sessions).value());
    }
}
