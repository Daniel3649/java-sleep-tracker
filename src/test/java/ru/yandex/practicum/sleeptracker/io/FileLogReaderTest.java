package ru.yandex.practicum.sleeptracker.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.yandex.practicum.sleeptracker.domain.SleepingSession;
import ru.yandex.practicum.sleeptracker.domain.SleepingState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileLogReaderTest {
    @TempDir
    Path tempDirectory;

    @Test
    void shouldReadSessionsInAssignmentFormatAndPreserveOrder() throws IOException {
        Path file = tempDirectory.resolve("sleep_log.txt");
        Files.writeString(file, """
                01.10.25 22:15;02.10.25 08:00;GOOD
                02.10.25 23:00;03.10.25 08:00;NORMAL
                03.10.25 14:30;03.10.25 15:20;NORMAL
                03.10.25 23:30;04.10.25 06:20;BAD
                """);

        var expected = List.of(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 8, 0),
                        SleepingState.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 8, 0),
                        SleepingState.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 14, 30),
                        LocalDateTime.of(2025, 10, 3, 15, 20),
                        SleepingState.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 23, 30),
                        LocalDateTime.of(2025, 10, 4, 6, 20),
                        SleepingState.BAD
                )
        );

        assertEquals(expected, FileLogReader.readAllSleepingSessions(file));
    }

    @Test
    void shouldReturnEmptyListForEmptyFile() throws IOException {
        Path file = tempDirectory.resolve("empty.txt");
        Files.writeString(file, "");

        assertEquals(List.of(), FileLogReader.readAllSleepingSessions(file));
    }

    @Test
    void shouldSkipInvalidLinesAndReadValidSession() throws IOException {
        Path file = tempDirectory.resolve("mixed.txt");
        Files.writeString(file, """
                invalid line
                invalid date;02.10.25 08:00;GOOD
                01.10.25 22:15;invalid date;GOOD
                01.10.25 22:15;02.10.25 08:00;UNKNOWN
                01.10.25 22:15;02.10.25 08:00

                02.10.25 23:00;03.10.25 08:00;NORMAL
                """);
        var expected = new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 23, 0),
                LocalDateTime.of(2025, 10, 3, 8, 0),
                SleepingState.NORMAL
        );

        assertEquals(List.of(expected), FileLogReader.readAllSleepingSessions(file));
    }

    @Test
    void shouldThrowIOExceptionWhenFileDoesNotExist() {
        Path file = tempDirectory.resolve("missing.txt");

        assertThrows(IOException.class, () -> FileLogReader.readAllSleepingSessions(file));
    }

    @Test
    void shouldRejectNullPath() {
        assertThrows(NullPointerException.class, () -> FileLogReader.readAllSleepingSessions(null));
    }
}
