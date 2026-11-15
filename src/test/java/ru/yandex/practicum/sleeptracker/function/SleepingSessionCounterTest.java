package ru.yandex.practicum.sleeptracker.function;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.sleeptracker.function.SleepingSessionTestData.session;

class SleepingSessionCounterTest {
    private final SleepingSessionCounter counter = new SleepingSessionCounter();

    @Test
    void shouldCountAllSessions() {
        var sessions = List.of(
                session("2025-10-01T01:00", "2025-10-01T08:00"),
                session("2025-10-01T14:00", "2025-10-01T15:00")
        );

        assertEquals(2L, counter.apply(sessions).value());
    }

    @Test
    void shouldReturnZeroForEmptyList() {
        assertEquals(0L, counter.apply(List.of()).value());
    }
}
