package ru.yandex.practicum.sleeptracker.domain;

import java.time.LocalDateTime;

public record SleepingSession(
        LocalDateTime startTime,
        LocalDateTime endTime,
        SleepingState state
) {
}
