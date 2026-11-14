package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.domain.SleepingSession;
import ru.yandex.practicum.sleeptracker.function.*;
import ru.yandex.practicum.sleeptracker.io.FileLogReader;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SleepTrackerApp {
    private static final Logger logger = Logger.getLogger(
            SleepTrackerApp.class.getName()
    );

    private static final List<
            Function<List<SleepingSession>, ? extends SleepAnalysisResult<?>>
            > functions = List.of(
            new SleepingSessionCounter(),
            new MInSleepingSessionDurationCounter(),
            new MaxSleepingSessionDurationCounter(),
            new AvgSleepingSessionCounter(),
            new SleepingSessionCounterByState()
    );

    public static void main(String[] args) {
        if (args.length != 1) {
            logger.log(
                    Level.SEVERE,
                    "Для работы программы требуется 1 аргумент - путь к файлу с логами"
            );
            return;
        }

        List<SleepingSession> sleepingSessions;
        try {
            sleepingSessions = FileLogReader.readAllSleepingSessions(
                    Path.of(args[0])
            );
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return;
        }

        try {
            functions.forEach(function -> {
                    SleepAnalysisResult<?> result = function.apply(sleepingSessions);
                    String message = String.format(
                            "%s: %s",
                            result.context(),
                            result.value()
                    );
                    System.out.println(message);
                }
            );
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
