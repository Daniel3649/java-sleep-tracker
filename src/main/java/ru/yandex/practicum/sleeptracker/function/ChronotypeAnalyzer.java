package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.domain.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ChronotypeAnalyzer
        implements Function<List<SleepingSession>, SleepAnalysisResult<String>> {

    private static final String CONTEXT_INFO = "Хронотип пользователя";

    private static final String OWL_STRING = "Сова";
    private static final String LARK_STRING = "Жаворонок";
    private static final String PIGEON_STRING = "Голубь";

    private enum Chronotype {
        OWL, LARK, PIGEON
    }

    private record SleepBounds(LocalDateTime start, LocalDateTime end) {

        private SleepBounds merge(SleepBounds other) {
            return new SleepBounds(
                    start.isBefore(other.start) ? start : other.start,
                    end.isAfter(other.end) ? end : other.end
            );
        }
    }

    @Override
    public SleepAnalysisResult<String> apply(List<SleepingSession> sleepingSessionList) {
        Objects.requireNonNull(
                sleepingSessionList,
                "sleepingSessionList не должен быть null"
        );

        Map<LocalDate, SleepBounds> nights = sleepingSessionList.stream()
                .flatMap(session ->
                        session.startTime().toLocalDate()
                                .datesUntil(session.endTime().toLocalDate().plusDays(1))
                                .filter(date ->
                                        session.startTime().isBefore(date.atTime(6, 0))
                                                && session.endTime().isAfter(date.atStartOfDay())
                                )
                                .map(date -> Map.entry(
                                        date,
                                        new SleepBounds(session.startTime(), session.endTime())
                                ))
                )
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        SleepBounds::merge
                ));

        Map<Chronotype, Long> counts = nights.entrySet().stream()
                .map(entry -> classify(entry.getKey(), entry.getValue()))
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        long owls = counts.getOrDefault(Chronotype.OWL, 0L);
        long larks = counts.getOrDefault(Chronotype.LARK, 0L);
        long pigeons = counts.getOrDefault(Chronotype.PIGEON, 0L);

        if (owls > larks && owls > pigeons) {
            return new SleepAnalysisResult<>(CONTEXT_INFO, OWL_STRING);
        } else if (larks > owls && larks > pigeons) {
            return new SleepAnalysisResult<>(CONTEXT_INFO, LARK_STRING);
        }

        return new SleepAnalysisResult<>(CONTEXT_INFO, PIGEON_STRING);
    }

    private static Chronotype classify(LocalDate date, SleepBounds sleep) {
        if (sleep.start().isAfter(date.minusDays(1).atTime(23, 0))
                && sleep.end().isAfter(date.atTime(9, 0))) {
            return Chronotype.OWL;
        }

        if (sleep.start().isBefore(date.minusDays(1).atTime(22, 0))
                && sleep.end().isBefore(date.atTime(7, 0))) {
            return Chronotype.LARK;
        }

        return Chronotype.PIGEON;
    }
}
