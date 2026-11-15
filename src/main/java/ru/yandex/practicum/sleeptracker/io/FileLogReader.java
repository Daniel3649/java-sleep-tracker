package ru.yandex.practicum.sleeptracker.io;

import ru.yandex.practicum.sleeptracker.domain.SleepingSession;
import ru.yandex.practicum.sleeptracker.domain.SleepingState;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class FileLogReader {
    private static final Logger logger = Logger.getLogger(
            FileLogReader.class.getName()
    );

    private static final Map<String, SleepingState> sleepingStatesByString = Map.of(
            "GOOD", SleepingState.GOOD,
            "NORMAL", SleepingState.NORMAL,
            "BAD", SleepingState.BAD
    );

    private static final String LOG_SEPARATOR = ";";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private FileLogReader() {
    }

    public static List<SleepingSession> readAllSleepingSessions(Path path) throws IOException {
        Objects.requireNonNull(path, "path не может быть null");

        List<String> sleepingSessionsAsString = Files.readAllLines(
                path,
                StandardCharsets.UTF_8
        );

        return sleepingSessionsAsString.stream()
                .<SleepingSession>mapMulti((line, consumer) -> {
                    Optional<String[]> partsAsOptional = splitString(line);
                    if (partsAsOptional.isEmpty()) {
                        return;
                    }
                    String[] parts = partsAsOptional.get();

                    Optional<LocalDateTime> startTime = parseDateTime(parts[0]);
                    if (startTime.isEmpty()) {
                        return;
                    }

                    Optional<LocalDateTime> endTime = parseDateTime(parts[1]);
                    if (endTime.isEmpty()) {
                        return;
                    }

                    Optional<SleepingState> sleepingState = parseSleepingState(parts[2]);
                    if (sleepingState.isEmpty()) {
                        return;
                    }

                    SleepingSession sleepingSession = new SleepingSession(
                            startTime.get(),
                            endTime.get(),
                            sleepingState.get()
                    );
                    consumer.accept(sleepingSession);

                }).collect(Collectors.toList());
    }

    private static Optional<String[]> splitString(String text) {
        String[] parts = text.split(LOG_SEPARATOR);

        if (parts.length != 3) {
            String message = String.format("Некорректный формат лога: %s. Правильный формат: " +
                    "<время начала сна>;<время окончания сна>;<состояние сна>", text);
            logger.log(Level.WARNING, message);
            return Optional.empty();
        }

        return Optional.of(parts);
    }

    private static Optional<LocalDateTime> parseDateTime(String text) {
        try {
            return Optional.of(
                    LocalDateTime.parse(text, formatter)
            );
        } catch (DateTimeParseException e) {
            logger.log(Level.WARNING, e.getMessage());
            return Optional.empty();
        }
    }

    private static Optional<SleepingState> parseSleepingState(String text) {
        SleepingState sleepingState = sleepingStatesByString.get(text);
        if (sleepingState == null) {
            String message = String.format(
                    "Некорректное состояние сна: %s. Возможные варианты: %s",
                    text,
                    String.join(", ", sleepingStatesByString.keySet())
            );
            logger.log(Level.WARNING, message);
            return Optional.empty();
        }
        return Optional.of(sleepingState);
    }
}
