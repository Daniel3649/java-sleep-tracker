package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.domain.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class AvgSleepingSessionCounter
        implements Function<List<SleepingSession>, SleepAnalysisResult<Double>> {
    private static final String CONTEXT_INFO = "Средняя продолжительность сессии";

    @Override
    public SleepAnalysisResult<Double> apply(List<SleepingSession> sleepingSessionList) {
        Objects.requireNonNull(sleepingSessionList, "sleepingSessionList не должен быть null");

        Double averageDuration = sleepingSessionList.stream()
                .mapToLong(sleepingSession -> Duration.between(
                                sleepingSession.startTime(), sleepingSession.endTime()
                        ).toMinutes()
                )
                .average()
                .orElse(0L);

        return new SleepAnalysisResult<>(
                CONTEXT_INFO,
                averageDuration
        );

    }
}
