package ru.yandex.practicum.sleeptracker.function;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.sleeptracker.function.SleepingSessionTestData.session;

class AvgSleepingSessionCounterTest {
    private final AvgSleepingSessionCounter counter = new AvgSleepingSessionCounter();

    @Test
    void shouldCalculateFractionalAverageInMinutes() {
        var sessions = List.of(
                session("2025-10-01T23:00", "2025-10-02T00:00"),
                session("2025-10-02T23:00", "2025-10-03T00:31")
        );

        assertEquals(75.5, counter.apply(sessions).value(), 0.0001);
    }

    @Test
    void shouldReturnZeroForEmptyList() {
        assertEquals(0.0, counter.apply(List.of()).value(), 0.0001);
    }
}
