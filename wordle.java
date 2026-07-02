// wordle.java
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class wordle {
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[92m";
    private static final String YELLOW = "\u001B[93m";
    private static final String GRAY = "\u001B[90m";
    private static final String BOLD = "\u001B[1m";

    private static String colorize(String text, String color) {
        return color + text + RESET;
    }

    private static final List<String> WORDS_RU = Arrays.asList(
        "абзац", "абонемент", "автобус", "агрегат", "аквариум", "аккумулятор",
        "алгоритм", "амплитуда", "ананас", "анекдот", "антенна", "аппарат",
        "арбуз", "аромат", "артист", "архив", "аспект", "астроном", "атмосфера",
        "атом", "аудитория", "аэропорт", "базар", "баланс", "барабан", "бассейн",
        "батарея", "безопасность", "библиотека", "билет", "биология", "благодарность",
        "блокнот", "богатство", "болезнь", "бонус", "борщ", "ботинки", "брак",
        "бригада", "бронза", "буква", "бульвар", "бумага", "буржуазия", "бутерброд",
        "быт", "бюджет", "вагон", "вариант", "вдохновение", "вектор", "вершина",
        "весна", "взаимодействие", "взгляд", "взрыв", "внимание", "воздух",
        "возраст", "война", "волонтер", "воображение", "воспитание", "впечатление",
        "время", "выбор", "выпуск", "выражение", "высота", "выступление", "газета",
        "галактика", "гарантия", "гармония", "гениальность", "география", "герой",
        "гитара", "глобус", "голос", "гора", "город", "государство", "грамота",
        "граница", "гриб", "груз", "гуманизм", "дар", "движение", "дворец",
        "дебют", "декада", "декорация", "делегат", "демократия", "деревня",
        "деталь", "диалог", "диплом", "директор", "дисциплина", "доброта",
        "договор", "дождь", "документ", "долг", "долина", "дом", "достижение",
        "достоинство", "драма", "друг", "дубль", "душа", "дым", "европа",
        "единство", "ежедневник", "желание", "железо", "жизнь", "журнал",
        "забота", "завод", "загадка", "закон", "зал", "запас", "запись",
        "защита", "звезда", "звук", "здание", "здоровье", "зеркало", "знак",
        "знание", "золото", "зона", "игра", "идея", "издание", "изображение",
        "изобретение", "интерес", "информация", "искусство", "история", "кабинет",
        "календарь", "камень", "канал", "капитал", "карьера", "катастрофа",
        "качество", "квартира", "кино", "класс", "климат", "книга", "ковер",
        "код", "количество", "коллектив", "команда", "комитет", "комната",
        "конкурс", "конструкция", "контакт", "контракт", "концерт", "копейка",
        "корень", "корзина", "корпус", "космос", "костюм", "кофе", "кран",
        "красота", "кредит", "кризис", "кристалл", "критерий", "круг", "крыша",
        "кубок", "культура", "курорт", "лаборатория", "лагерь", "ладонь",
        "лампа", "ландшафт", "лауреат", "лед", "лекция", "лес", "лето",
        "лечение", "лидер", "линия", "листок", "литература", "личность", "лоб",
        "ловушка", "логика", "локоть", "луч", "льгота", "любовь", "магазин",
        "магия", "макет", "максимум", "мальчик", "манеж", "маршрут", "масса",
        "математика", "материал", "матрица", "машина", "медицина", "мел",
        "мемориал", "меньшинство", "мера", "механизм", "микрофон", "миллион",
        "минута", "мир", "миссия", "мнение", "модель", "модернизация", "молоко",
        "момент", "монитор", "монумент", "море", "мост", "мотивация", "мощность",
        "музей", "музыка", "мышление", "навык", "нагрузка", "надежда", "название",
        "наличие", "народ", "наука", "находка", "нация", "небо", "неделя",
        "необходимость", "нефть", "низ", "новаторство", "норма", "ночь", "объект",
        "объем", "обучение", "общество", "объектив", "одежда", "озеро", "океан",
        "окно", "олимпиада", "операция", "опыт", "организация", "орден", "орел",
        "оригинал", "оркестр", "оружие", "осень", "основа", "ответ", "открытие",
        "отрасль", "отчет", "оценка", "память", "панорама", "парад", "парк",
        "пароль", "партия", "паспорт", "патриот", "пауза", "певец", "перемена",
        "период", "песня", "пианино", "письмо", "питание", "план", "планета",
        "пластик", "платформа", "племя", "пленум", "плоскость", "победа",
        "повод", "погода", "поддержка", "подход", "позиция", "познание",
        "показатель", "поколение", "поле", "полет", "политика", "половина",
        "пользователь", "помощь", "понятие", "порт", "портрет", "последствие",
        "постановка", "поток", "поэзия", "пояс", "правило", "практика", "предмет",
        "президент", "премия", "прибор", "приз", "приказ", "природа", "причина",
        "провинция", "прогноз", "программа", "продукт", "проект", "промышленность",
        "пропаганда", "проспект", "процесс", "процент", "профессия", "психология",
        "птица", "публика", "путь", "пьеса", "работа", "равновесие", "радио",
        "развитие", "размер", "разум", "район", "ранг", "расход", "реализация",
        "революция", "регион", "режиссер", "результат", "реклама", "рекомендация",
        "религия", "ремонт", "ресурс", "реформа", "рисунок", "ритм", "род",
        "роль", "роман", "рост", "рынок", "сад", "санкция", "сборник", "свет",
        "свобода", "связь", "сезон", "секрет", "сектор", "сельское хозяйство",
        "семья", "сервис", "серия", "сигнал", "сила", "символ", "система",
        "ситуация", "сказка", "скорость", "слава", "слово", "служба", "случай",
        "смысл", "событие", "совет", "сознание", "создание", "сок", "солнце",
        "соревнование", "состав", "состояние", "сотрудник", "сохранение",
        "союз", "спасение", "спектакль", "список", "спорт", "способ", "справедливость",
        "средство", "стабильность", "стандарт", "статья", "стекло", "стена",
        "степень", "стиль", "стол", "столица", "стоимость", "страна", "стратегия",
        "стремление", "строительство", "студент", "стул", "субъект", "судьба",
        "сумма", "сутки", "сцена", "счастье", "тайна", "талант", "танец",
        "театр", "текст", "телефон", "температура", "тенденция", "теория",
        "терапия", "термин", "территория", "техника", "технология", "тип",
        "тишина", "товар", "творчество", "темперамент", "темп", "течение",
        "транспорт", "требование", "третий", "труд", "туризм", "убеждение",
        "уважение", "уверенность", "удар", "удача", "узел", "указ", "украшение",
        "улица", "улучшение", "ум", "управление", "уровень", "урок", "успех",
        "установка", "устойчивость", "ученик", "учет", "фабрика", "факультет",
        "фигура", "физика", "философия", "фильм", "финал", "фирма", "флаг",
        "фокус", "фонд", "форма", "формула", "фотография", "фрагмент", "фронт",
        "функция", "характер", "химия", "хлеб", "хозяин", "холод", "хороший",
        "художник", "цвет", "цель", "центр", "цирк", "цифра", "часть", "человек",
        "черта", "чистота", "чувство", "шаг", "шанс", "школа", "шум", "экран",
        "эксперт", "экспорт", "элемент", "энергия", "эпизод", "эпоха", "эскиз",
        "этап", "эфир", "юбилей", "юмор", "юность", "яблоко", "явление", "язык",
        "январь", "яркость", "яхта"
    );

    private static final List<String> WORDS_EN = Arrays.asList(
        "about", "above", "abuse", "actor", "acute", "admit", "adopt", "adult",
        "after", "again", "agent", "agree", "ahead", "alarm", "album", "alien",
        "align", "alike", "alive", "alley", "allow", "alone", "along", "alter",
        "among", "ample", "angel", "anger", "angle", "angry", "anime", "ankle",
        "annoy", "apart", "apple", "apply", "arena", "argue", "arise", "armor",
        "army", "aroma", "array", "arrow", "aside", "asset", "atlas", "attic",
        "audio", "audit", "avoid", "awake", "award", "aware", "awful", "baker",
        "basic", "basis", "batch", "beach", "begin", "being", "below", "bench",
        "bible", "birth", "black", "blade", "blame", "bland", "blank", "blast",
        "blaze", "bleed", "blend", "bless", "blind", "blink", "bliss", "block",
        "blood", "bloom", "blown", "blue", "bluff", "blunt", "blurt", "board",
        "boost", "booth", "bound", "brain", "brand", "brass", "brave", "bread",
        "break", "breed", "brick", "bride", "brief", "bring", "broad", "broke",
        "brook", "brown", "brush", "buck", "buddy", "build", "built", "bunch",
        "burden", "bureau", "butter", "button", "cabin", "cable", "cache",
        "camel", "candy", "cargo", "carry", "catch", "cater", "cause", "cease",
        "chain", "chair", "chalk", "champ", "charm", "chart", "chase", "cheap",
        "check", "cheek", "cheer", "chess", "chest", "chief", "child", "chill",
        "china", "chord", "chunk", "civic", "civil", "clash", "clean", "clear",
        "click", "cliff", "climb", "cling", "clock", "clone", "close", "cloth",
        "cloud", "coach", "coast", "color", "comet", "comic", "coral", "count",
        "crack", "craft", "crane", "crash", "crawl", "crazy", "cream", "crest",
        "crime", "crisp", "cross", "crowd", "crown", "cruel", "crush", "cubic",
        "daily", "dance", "death", "debug", "debut", "decay", "decor", "delay",
        "delta", "demon", "dense", "depot", "depth", "derby", "desk", "devil",
        "diary", "dirty", "disco", "ditch", "dodge", "dollar", "dome", "donor",
        "doubt", "dough", "draft", "drain", "drake", "drama", "drank", "drape",
        "drawn", "dread", "dream", "dress", "dried", "drift", "drill", "drink",
        "drive", "drone", "drove", "dwarf", "eager", "eagle", "early", "earth",
        "eight", "elite", "elope", "empty", "endow", "enemy", "enjoy", "enter",
        "entry", "equal", "equip", "erode", "error", "erupt", "essay", "ether",
        "event", "every", "exact", "exert", "exile", "exist", "extra", "fable",
        "facet", "faith", "fancy", "fatal", "fault", "feast", "fence", "ferry",
        "fetch", "fever", "fewer", "final", "first", "fixed", "flame", "flash",
        "fleet", "flesh", "float", "flock", "flood", "floor", "flora", "flour",
        "fluid", "flush", "flute", "focus", "force", "forge", "forth", "forum",
        "found", "frame", "frank", "fraud", "fresh", "front", "frost", "froze",
        "fruit"
    );

    static class Wordle {
        List<String> words;
        String word;
        int attempts;
        List<String> guesses = new ArrayList<>();
        List<String[]> results = new ArrayList<>();
        boolean hintUsed = false;

        public Wordle(String lang) {
            words = lang.equals("ru") ? WORDS_RU : WORDS_EN;
            Random rand = new Random();
            word = words.get(rand.nextInt(words.size())).toUpperCase();
            attempts = 6;
        }

        public boolean guess(String input) {
            String guess = input.toUpperCase();
            if (guess.length() != 5) {
                System.out.println("Слово должно быть из 5 букв");
                return false;
            }
            if (!words.stream().anyMatch(w -> w.toUpperCase().equals(guess))) {
                System.out.println("Слова нет в словаре");
                return false;
            }
            guesses.add(guess);
            results.add(check(guess));
            attempts--;
            return true;
        }

        private String[] check(String guess) {
            String[] result = new String[]{"gray", "gray", "gray", "gray", "gray"};
            char[] target = word.toCharArray();
            char[] g = guess.toCharArray();
            // зеленые
            for (int i=0; i<5; i++) {
                if (g[i] == target[i]) {
                    result[i] = "green";
                    target[i] = ' ';
                    g[i] = ' ';
                }
            }
            // желтые
            for (int i=0; i<5; i++) {
                if (g[i] != ' ') {
                    for (int j=0; j<5; j++) {
                        if (target[j] != ' ' && g[i] == target[j]) {
                            result[i] = "yellow";
                            target[j] = ' ';
                            break;
                        }
                    }
                }
            }
            return result;
        }

        public boolean isWin() {
            return guesses.size() > 0 && guesses.get(guesses.size()-1).equals(word);
        }

        public boolean isLoss() {
            return attempts == 0 && !isWin();
        }

        public void display() {
            System.out.println(colorize("Угадай слово (5 букв, " + attempts + " попыток)", BOLD));
            for (int i=0; i<guesses.size(); i++) {
                for (int j=0; j<5; j++) {
                    String col = results.get(i)[j].equals("green") ? GREEN :
                                 results.get(i)[j].equals("yellow") ? YELLOW : GRAY;
                    System.out.print(colorize(String.valueOf(guesses.get(i).charAt(j)), col) + " ");
                }
                System.out.println();
            }
            if (isWin()) System.out.println(colorize("Поздравляем! Вы угадали слово!", GREEN));
            else if (isLoss()) System.out.println(colorize("Вы проиграли. Загаданное слово: " + word, GRAY));
        }

        public String getWord() { return word; }
        public boolean isHintUsed() { return hintUsed; }
        public void useHint() { hintUsed = true; }
    }

    static class Stats {
        int played, wins, current_streak, max_streak;
    }

    public static void main(String[] args) throws IOException {
        String lang = "ru";
        boolean showStats = false, resetStats = false;
        if (args.length > 0) {
            if (args[0].equals("ru") || args[0].equals("en")) lang = args[0];
            else if (args[0].equals("-s") || args[0].equals("--stats")) showStats = true;
            else if (args[0].equals("-r") || args[0].equals("--reset")) resetStats = true;
            else if (args[0].equals("-h") || args[0].equals("--help")) {
                System.out.println("Использование: java wordle [ru|en] [-s] [-r]");
                return;
            }
        }

        String statsPath = System.getProperty("user.home") + "/.wordle_stats.json";
        Stats stats = new Stats();
        if (Files.exists(Paths.get(statsPath))) {
            String json = new String(Files.readAllBytes(Paths.get(statsPath)));
            // Упрощённый парсинг (можно использовать библиотеку, но для демонстрации оставим так)
            // Для простоты используем ручной разбор
            try {
                // Заглушка, чтобы не тащить зависимость
                // В реальном проекте лучше использовать Gson или Jackson.
                // Здесь мы просто прочитаем числа.
                String[] parts = json.split(",");
                for (String p : parts) {
                    String[] kv = p.split(":");
                    if (kv.length == 2) {
                        String key = kv[0].replaceAll("\"", "").trim();
                        int val = Integer.parseInt(kv[1].replaceAll("[^0-9]", ""));
                        if (key.equals("played")) stats.played = val;
                        else if (key.equals("wins")) stats.wins = val;
                        else if (key.equals("current_streak")) stats.current_streak = val;
                        else if (key.equals("max_streak")) stats.max_streak = val;
                    }
                }
            } catch (Exception e) { /* ignore */ }
        }
        if (resetStats) {
            stats = new Stats();
            Files.write(Paths.get(statsPath), "{\"played\":0,\"wins\":0,\"current_streak\":0,\"max_streak\":0}".getBytes());
            System.out.println("Статистика сброшена.");
            return;
        }
        if (showStats) {
            System.out.println("Сыграно игр: " + stats.played);
            System.out.println("Побед: " + stats.wins);
            System.out.printf("Процент побед: %.1f%%\n", stats.played>0 ? stats.wins*100.0/stats.played : 0);
            System.out.println("Текущая серия: " + stats.current_streak);
            System.out.println("Максимальная серия: " + stats.max_streak);
            return;
        }

        Wordle game = new Wordle(lang);
        System.out.println(colorize("Слово загадано. У вас 6 попыток. Вводите 5-буквенные слова.", BOLD));
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (game.attempts > 0 && !game.isWin()) {
            System.out.print("Ваше слово: ");
            String input = reader.readLine().trim();
            if (input.equals("q")) { System.out.println("Выход."); break; }
            if (input.equals("?")) {
                if (!game.isHintUsed()) {
                    System.out.println("Подсказка: первая буква '" + game.getWord().charAt(0) + "'");
                    game.useHint();
                } else {
                    System.out.println("Вы уже использовали подсказку.");
                }
                continue;
            }
            if (game.guess(input)) {
                game.display();
            }
        }
        stats.played++;
        if (game.isWin()) {
            stats.wins++;
            stats.current_streak++;
            if (stats.current_streak > stats.max_streak) stats.max_streak = stats.current_streak;
        } else {
            stats.current_streak = 0;
        }
        // Сохраняем статистику
        String json = "{\"played\":" + stats.played + ",\"wins\":" + stats.wins +
                      ",\"current_streak\":" + stats.current_streak +
                      ",\"max_streak\":" + stats.max_streak + "}";
        Files.write(Paths.get(statsPath), json.getBytes());
    }
}
