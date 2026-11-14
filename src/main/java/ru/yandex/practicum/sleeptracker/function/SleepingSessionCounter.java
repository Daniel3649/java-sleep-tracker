package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.domain.SleepingSession;

import java.util.Objects;
import java.util.function.Function;
import java.util.List;

public final class SleepingSessionCounter implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final String CONTEXT_INFO = "Количество сессий сна";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        Objects.requireNonNull(sleepingSessions, "sleepingSessions не может быть null");

        return new SleepAnalysisResult(
                CONTEXT_INFO,
                sleepingSessions.size()
        );
    }
}
