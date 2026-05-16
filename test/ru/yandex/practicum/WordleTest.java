package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private WordleDictionary dictionary;
    private PrintWriter log;

    @BeforeEach
    void setUp() {
        // Для тестов направляем лог в консоль, чтобы не создавать файлы
        log = new PrintWriter(System.out);
        List<String> testWords = List.of("арбуз", "книга", "трава", "слово", "экран", "буква");
        dictionary = new WordleDictionary(testWords);
    }

    @Test
    void testDictionaryNormalization() {
        assertEquals("арбуз", WordleDictionary.normalize(" Арбуз "));
        assertEquals("елка", WordleDictionary.normalize("Ёлка"));
        assertEquals("", WordleDictionary.normalize(null));
    }

    @Test
    void testDictionaryContains() {
        assertTrue(dictionary.contains("книга"));
        assertFalse(dictionary.contains("дом"));
    }

    @Test
    void testDictionaryFilter() {
        Map<Integer, Character> exact = Map.of(0, 'к', 4, 'а');
        Set<Character> present = Set.of('н');
        Set<Character> absent = Set.of('р', 'б');

        List<String> filtered = dictionary.filter(exact, present, absent);
        assertTrue(filtered.contains("книга"));
        assertFalse(filtered.contains("арбуз")); // есть 'р' и 'б' в absent
        assertFalse(filtered.contains("трава")); // не подходит по exact
    }

    @Test
    void testGameMakeMoveValid() throws Exception {
        // Подменяем ответ на известный для теста
        WordleGame game = new WordleGame(dictionary, log) {
            // Hack для теста: фиксируем ответ (в реальном коде лучше использовать конструктор с ответом)
        };
        // Тестирование через рефлексию или открытый конструктор предпочтительнее,
        // но для краткости проверим валидацию ввода:
        assertThrows(InvalidWordException.class, () -> game.makeMove("кот"));
        assertThrows(WordNotFoundException.class, () -> game.makeMove("домик"));
    }

    @Test
    void testGameFeedbackLogic() throws Exception {
        // Создаём игру с фиксированным ответом через тестовый словарь из 1 слова
        WordleDictionary singleDict = new WordleDictionary(List.of("слово"));
        WordleGame game = new WordleGame(singleDict, log);

        // Проверяем, что попытка уменьшилась
        String feedback = game.makeMove("слово");
        assertEquals("+++++", feedback);
        assertEquals(5, game.getStepsLeft());
        assertTrue(game.getAnswer().equals("слово"));
    }

    @Test
    void testHintGeneration() {
        WordleGame game = new WordleGame(dictionary, log);
        String hint = game.getHint();
        assertNotNull(hint);
        assertTrue(dictionary.contains(hint));
        assertTrue(hint.length() == 5);
    }
}