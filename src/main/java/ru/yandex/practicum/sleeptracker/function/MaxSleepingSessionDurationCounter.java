package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.domain.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class MaxSleepingSessionDurationCounter
        implements Function<List<SleepingSession>, SleepAnalysisResult<Long>> {
    private static final String CONTEXT_INFO = "Максимальная продолжительность сессии";

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sleepingSessionList) {
        Objects.requireNonNull(sleepingSessionList, "sleepingSessionList не должен быть null");

        Long maxDuration = sleepingSessionList.stream().
                mapToLong(sleepingSession -> Duration.between(
                                sleepingSession.startTime(), sleepingSession.endTime()
                        ).toMinutes()
                ).
                max()
                .orElse(0L);

        return new SleepAnalysisResult<>(
                CONTEXT_INFO,
                maxDuration
        );

    }
}
