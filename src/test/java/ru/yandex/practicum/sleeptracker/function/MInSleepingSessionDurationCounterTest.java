package ru.yandex.practicum.sleeptracker.function;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.sleeptracker.function.SleepingSessionTestData.session;

class MInSleepingSessionDurationCounterTest {
    private final MInSleepingSessionDurationCounter counter = new MInSleepingSessionDurationCounter();

    @Test
    void shouldFindShortestSessionInMinutes() {
        var sessions = List.of(
                session("2025-10-01T23:00", "2025-10-02T00:30"),
                session("2025-10-02T14:00", "2025-10-02T14:30"),
                session("2025-10-02T23:00", "2025-10-03T00:00")
        );

        assertEquals(30L, counter.apply(sessions).value());
    }

    @Test
    void shouldReturnZeroForEmptyList() {
        assertEquals(0L, counter.apply(List.of()).value());
    }
}
