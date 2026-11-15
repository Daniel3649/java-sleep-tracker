package ru.yandex.practicum.sleeptracker.function;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.domain.SleepingState;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.sleeptracker.function.SleepingSessionTestData.session;

class SleepingSessionCounterByStateTest {
    private final SleepingSessionCounterByState counter = new SleepingSessionCounterByState();

    @Test
    void shouldCountOnlyBadQualitySessionsByDefault() {
        var sessions = List.of(
                session("2025-10-01T23:00", "2025-10-02T07:00", SleepingState.BAD),
                session("2025-10-02T23:00", "2025-10-03T07:00", SleepingState.GOOD),
                session("2025-10-03T14:00", "2025-10-03T15:00", SleepingState.BAD),
                session("2025-10-03T23:00", "2025-10-04T07:00", SleepingState.NORMAL)
        );

        assertEquals(2L, counter.apply(sessions).value());
    }

    @Test
    void shouldReturnZeroWhenNoBadQualitySessionsExist() {
        var sessions = List.of(
                session("2025-10-01T23:00", "2025-10-02T07:00", SleepingState.GOOD),
                session("2025-10-02T23:00", "2025-10-03T07:00", SleepingState.NORMAL)
        );

        assertEquals(0L, counter.apply(sessions).value());
    }
}
