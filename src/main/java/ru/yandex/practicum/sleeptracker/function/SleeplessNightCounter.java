package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.domain.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class SleeplessNightCounter
        implements Function<List<SleepingSession>, SleepAnalysisResult<Long>> {

    private static final String CONTEXT_INFO = "Количество бессонных ночей";

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sleepingSessionList) {
        Objects.requireNonNull(
                sleepingSessionList,
                "sleepingSessionList не должен быть null"
        );

        if (sleepingSessionList.isEmpty()) {
            return new SleepAnalysisResult<>(CONTEXT_INFO, 0L);
        }

        LocalDateTime firstStart = sleepingSessionList.getFirst().startTime();
        LocalDateTime lastEnd = sleepingSessionList.getLast().endTime();

        LocalDate firstNight = firstStart.toLocalTime().isBefore(LocalTime.NOON)
                ? firstStart.toLocalDate()
                : firstStart.toLocalDate().plusDays(1);

        LocalDate endExclusive = lastEnd.toLocalDate().plusDays(1);

        long totalNights = Math.max(
                0,
                ChronoUnit.DAYS.between(firstNight, endExclusive)
        );

        if (totalNights == 0) {
            return new SleepAnalysisResult<>(CONTEXT_INFO, 0L);
        }

        long nightsWithSleep = sleepingSessionList.stream()
                .flatMap(session ->
                        session.startTime().toLocalDate()
                                .datesUntil(session.endTime().toLocalDate().plusDays(1))
                                .filter(date ->
                                        !date.isBefore(firstNight)
                                                && date.isBefore(endExclusive)
                                )
                                .filter(date ->
                                        session.startTime().isBefore(date.atTime(6, 0))
                                                && session.endTime().isAfter(date.atStartOfDay())
                                )
                )
                .distinct()
                .count();

        return new SleepAnalysisResult<>(
                CONTEXT_INFO,
                totalNights - nightsWithSleep
        );
    }
}
