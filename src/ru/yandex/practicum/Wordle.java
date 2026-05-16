package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {
        try (PrintWriter log = new PrintWriter(new FileWriter("wordle.log", StandardCharsets.UTF_8))) {
            log.println("=== Wordle Session Started ===");

            WordleDictionaryLoader loader = new WordleDictionaryLoader();
            WordleDictionary dictionary = loader.load("words_ru.txt");
            WordleGame game = new WordleGame(dictionary, log);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Добро пожаловать в Wordle!");
            System.out.println("Угадайте слово из 5 букв. У вас 6 попыток.");
            System.out.println("Введите слово, нажмите Enter для подсказки или 'exit' для выхода.\n");

            boolean won = false;
            while (!game.isGameOver()) {
                System.out.printf("Попытка %d/%d. Введите слово: ", 7 - game.getStepsLeft(), 6);
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Игра завершена пользователем.");
                    log.println("Game exited by user");
                    break;
                }

                if (input.isEmpty()) {
                    System.out.println(" Подсказка: " + game.getHint());
                    continue;
                }

                try {
                    String feedback = game.makeMove(input);
                    System.out.println(feedback);

                    if (feedback.equals("+++++")) {
                        System.out.println("🎉 Поздравляем! Вы угадали слово: " + game.getAnswer());
                        won = true;
                        break;
                    }
                } catch (WordNotFoundException e) {
                    log.println("WordNotFoundException: " + e.getMessage());
                    System.out.println(" " + e.getMessage());
                } catch (InvalidWordException e) {
                    log.println("InvalidWordException: " + e.getMessage());
                    System.out.println("⚠️ " + e.getMessage());
                }
            }

            if (!won && game.isGameOver()) {
                System.out.println("💀 Попытки закончились. Загаданное слово: " + game.getAnswer());
                log.println("Game over. Answer: " + game.getAnswer());
            }
            log.println("=== Session Finished. Won: " + won + " ===");

        } catch (DictionaryLoadException e) {
            logError("DictionaryLoadException", e);
            System.err.println("Ошибка загрузки словаря");
        } catch (IOException e) {
            logError("IOException", e);
            System.err.println("Ошибка инициализации");
        }
    }

    private static void logError(String type, Throwable e) {
        try (PrintWriter log = new PrintWriter(new FileWriter("wordle.log", StandardCharsets.UTF_8, true))) {
            log.println(type + ": " + e.getMessage());
            e.printStackTrace(log);
        } catch (IOException ex) {
            System.err.println("Failed to write to log file: " + ex.getMessage());
        }
    }
}