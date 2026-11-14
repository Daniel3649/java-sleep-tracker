package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.domain.SleepingSession;
import ru.yandex.practicum.sleeptracker.domain.SleepingState;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class SleepingSessionCounterByState
        implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    private String contextInfo = "Количество сессий с плохим качеством сна";
    private SleepingState sleepingState = SleepingState.BAD;

    public SleepingSessionCounterByState() {}

    public SleepingSessionCounterByState(String contextInfo, SleepingState sleepingState) {
        this.contextInfo = contextInfo;
        this.sleepingState = sleepingState;
    }

    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sleepingSessions) {
        Objects.requireNonNull(sleepingSessions, "sleepingSessions не должен быть null");

        Long sessionCountByState = sleepingSessions.stream()
                .filter(sleepingSession -> sleepingSession.state().equals(sleepingState))
                .count();

        return new SleepAnalysisResult<>(
                contextInfo,
                sessionCountByState
        );
    }

    public void setContextInfo(String contextInfo) {
        this.contextInfo = contextInfo;
    }

    public void setSleepingState(SleepingState sleepingState) {
        this.sleepingState = sleepingState;
    }
}
