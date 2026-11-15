package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.domain.SleepingSession;
import ru.yandex.practicum.sleeptracker.domain.SleepingState;

import java.time.LocalDateTime;

final class SleepingSessionTestData {
    private SleepingSessionTestData() {
    }

    static SleepingSession session(String start, String end) {
        return session(start, end, SleepingState.NORMAL);
    }

    static SleepingSession session(String start, String end, SleepingState state) {
        return new SleepingSession(
                LocalDateTime.parse(start),
                LocalDateTime.parse(end),
                state
        );
    }
}
