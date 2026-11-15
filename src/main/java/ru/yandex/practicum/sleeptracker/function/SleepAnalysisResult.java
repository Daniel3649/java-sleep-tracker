package ru.yandex.practicum.sleeptracker.function;

public record SleepAnalysisResult<T>(
        String context,
        T value
) {
}
