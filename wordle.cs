// wordle.cs
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.Json;

class WordleGame
{
    static string Colorize(string text, string color)
    {
        string col = color switch
        {
            "green" => "\x1b[92m",
            "yellow" => "\x1b[93m",
            "gray" => "\x1b[90m",
            "bold" => "\x1b[1m",
            _ => "\x1b[0m"
        };
        return col + text + "\x1b[0m";
    }

    static List<string> WORDS_RU = new List<string> {
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
    };

    static List<string> WORDS_EN = new List<string> {
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
    };

    class Wordle
    {
        private List<string> words;
        private string word;
        public int attempts;
        private List<string> guesses = new List<string>();
        private List<string[]> results = new List<string[]>();
        private bool hintUsed;

        public Wordle(string lang)
        {
            words = lang == "ru" ? WORDS_RU : WORDS_EN;
            var rand = new Random();
            word = words[rand.Next(words.Count)].ToUpper();
            attempts = 6;
            hintUsed = false;
        }

        public bool Guess(string input)
        {
            string guess = input.ToUpper();
            if (guess.Length != 5) { Console.WriteLine("Слово должно быть из 5 букв"); return false; }
            if (!words.Any(w => w.ToUpper() == guess)) { Console.WriteLine("Слова нет в словаре"); return false; }
            guesses.Add(guess);
            results.Add(Check(guess));
            attempts--;
            return true;
        }

        private string[] Check(string guess)
        {
            string[] result = new string[5] { "gray", "gray", "gray", "gray", "gray" };
            char[] target = word.ToCharArray();
            char[] g = guess.ToCharArray();
            // зеленые
            for (int i = 0; i < 5; i++)
                if (g[i] == target[i]) { result[i] = "green"; target[i] = ' '; g[i] = ' '; }
            // желтые
            for (int i = 0; i < 5; i++)
                if (g[i] != ' ')
                {
                    int idx = Array.IndexOf(target, g[i]);
                    if (idx != -1) { result[i] = "yellow"; target[idx] = ' '; }
                }
            return result;
        }

        public bool IsWin() => guesses.Count > 0 && guesses[guesses.Count-1] == word;
        public bool IsLoss() => attempts == 0 && !IsWin();

        public void Display()
        {
            Console.WriteLine(Colorize($"Угадай слово (5 букв, {attempts} попыток)", "bold"));
            for (int i = 0; i < guesses.Count; i++)
            {
                for (int j = 0; j < 5; j++)
                {
                    string col = results[i][j] == "green" ? "green" :
                                 results[i][j] == "yellow" ? "yellow" : "gray";
                    Console.Write(Colorize(guesses[i][j].ToString(), col) + " ");
                }
                Console.WriteLine();
            }
            if (IsWin()) Console.WriteLine(Colorize("Поздравляем! Вы угадали слово!", "green"));
            else if (IsLoss()) Console.WriteLine(Colorize($"Вы проиграли. Загаданное слово: {word}", "gray"));
        }

        public string Word => word;
        public bool HintUsed => hintUsed;
        public void UseHint() { hintUsed = true; }
    }

    class Stats
    {
        public int played { get; set; }
        public int wins { get; set; }
        public int current_streak { get; set; }
        public int max_streak { get; set; }
    }

    static void Main(string[] args)
    {
        string lang = "ru";
        bool showStats = false, resetStats = false;
        if (args.Length > 0)
        {
            if (args[0] == "ru" || args[0] == "en") lang = args[0];
            else if (args[0] == "-s" || args[0] == "--stats") showStats = true;
            else if (args[0] == "-r" || args[0] == "--reset") resetStats = true;
            else if (args[0] == "-h" || args[0] == "--help")
            {
                Console.WriteLine("Использование: wordle [ru|en] [-s] [-r]");
                return;
            }
        }

        string statsPath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.UserProfile), ".wordle_stats.json");
        Stats stats = new Stats();
        if (File.Exists(statsPath))
        {
            string json = File.ReadAllText(statsPath);
            stats = JsonSerializer.Deserialize<Stats>(json) ?? new Stats();
        }
        if (resetStats)
        {
            stats = new Stats();
            File.WriteAllText(statsPath, JsonSerializer.Serialize(stats));
            Console.WriteLine("Статистика сброшена.");
            return;
        }
        if (showStats)
        {
            Console.WriteLine($"Сыграно игр: {stats.played}");
            Console.WriteLine($"Побед: {stats.wins}");
            Console.WriteLine($"Процент побед: {(stats.played > 0 ? stats.wins*100.0/stats.played : 0):F1}%");
            Console.WriteLine($"Текущая серия: {stats.current_streak}");
            Console.WriteLine($"Максимальная серия: {stats.max_streak}");
            return;
        }

        Wordle game = new Wordle(lang);
        Console.WriteLine(Colorize("Слово загадано. У вас 6 попыток. Вводите 5-буквенные слова.", "bold"));
        while (game.attempts > 0 && !game.IsWin())
        {
            Console.Write("Ваше слово: ");
            string input = Console.ReadLine().Trim();
            if (input == "q") { Console.WriteLine("Выход."); break; }
            if (input == "?")
            {
                if (!game.HintUsed)
                {
                    Console.WriteLine($"Подсказка: первая буква '{game.Word[0]}'");
                    game.UseHint();
                }
                else Console.WriteLine("Вы уже использовали подсказку.");
                continue;
            }
            if (game.Guess(input)) game.Display();
        }
        stats.played++;
        if (game.IsWin())
        {
            stats.wins++;
            stats.current_streak++;
            if (stats.current_streak > stats.max_streak) stats.max_streak = stats.current_streak;
        }
        else stats.current_streak = 0;
        File.WriteAllText(statsPath, JsonSerializer.Serialize(stats));
    }
}
