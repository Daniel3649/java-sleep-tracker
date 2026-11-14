package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.domain.SleepingSession;
import ru.yandex.practicum.sleeptracker.io.FileLogReader;

import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SleepTrackerApp {
    private static final Logger logger = Logger.getLogger(
            SleepTrackerApp.class.getName()
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



    }
}