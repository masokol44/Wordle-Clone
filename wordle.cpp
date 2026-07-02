// wordle.cpp
#include <iostream>
#include <string>
#include <vector>
#include <random>
#include <fstream>
#include <map>
#include <algorithm>
#include <cctype>
#include <filesystem>

using namespace std;
namespace fs = std::filesystem;

const string RESET = "\033[0m";
const string GREEN = "\033[92m";
const string YELLOW = "\033[93m";
const string GRAY = "\033[90m";
const string BOLD = "\033[1m";

string colorize(const string& text, const string& color) {
    return color + text + RESET;
}

vector<string> WORDS_RU = {
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

vector<string> WORDS_EN = {
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

struct Stats {
    int played = 0;
    int wins = 0;
    int current_streak = 0;
    int max_streak = 0;
};

class Wordle {
public:
    Wordle(const string& lang) {
        words = (lang == "ru") ? WORDS_RU : WORDS_EN;
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dis(0, words.size()-1);
        word = words[dis(gen)];
        transform(word.begin(), word.end(), word.begin(), ::toupper);
        attempts = 6;
        hint_used = false;
    }

    string getWord() const { return word; }
    int getAttempts() const { return attempts; }
    bool isWin() const { return guesses.size() > 0 && guesses.back() == word; }
    bool isLoss() const { return attempts == 0 && !isWin(); }

    bool guess(const string& input) {
        string guess = input;
        transform(guess.begin(), guess.end(), guess.begin(), ::toupper);
        if (guess.size() != 5) {
            cout << "Слово должно быть из 5 букв" << endl;
            return false;
        }
        // Проверка, что слово есть в словаре (неэффективно, но для демонстрации)
        bool found = false;
        for (auto& w : words) {
            string wu = w;
            transform(wu.begin(), wu.end(), wu.begin(), ::toupper);
            if (wu == guess) { found = true; break; }
        }
        if (!found) {
            cout << "Слова нет в словаре" << endl;
            return false;
        }
        guesses.push_back(guess);
        vector<string> colors = check(guess);
        results.push_back(colors);
        attempts--;
        return true;
    }

    vector<string> check(const string& guess) {
        vector<string> result(5, "gray");
        string target = word;
        string g = guess;
        for (int i=0; i<5; ++i) {
            if (g[i] == target[i]) {
                result[i] = "green";
                target[i] = ' ';
                g[i] = ' ';
            }
        }
        for (int i=0; i<5; ++i) {
            if (g[i] != ' ' && target.find(g[i]) != string::npos) {
                result[i] = "yellow";
                target[target.find(g[i])] = ' ';
            }
        }
        return result;
    }

    void display() {
        cout << colorize("Угадай слово (5 букв, " + to_string(attempts) + " попыток)", BOLD) << endl;
        for (size_t i=0; i<guesses.size(); ++i) {
            for (int j=0; j<5; ++j) {
                string ch = string(1, guesses[i][j]);
                if (results[i][j] == "green") cout << colorize(ch, GREEN) << " ";
                else if (results[i][j] == "yellow") cout << colorize(ch, YELLOW) << " ";
                else cout << colorize(ch, GRAY) << " ";
            }
            cout << endl;
        }
        if (isWin()) {
            cout << colorize("Поздравляем! Вы угадали слово!", GREEN) << endl;
        } else if (isLoss()) {
            cout << colorize("Вы проиграли. Загаданное слово: " + word, GRAY) << endl;
        }
    }

    bool hint_used;
    int attempts;
    string word;
    vector<string> words;
    vector<string> guesses;
    vector<vector<string>> results;
};

int main(int argc, char* argv[]) {
    string lang = "ru";
    bool show_stats = false, reset_stats = false;
    if (argc > 1) {
        string arg = argv[1];
        if (arg == "ru" || arg == "en") lang = arg;
        else if (arg == "-s" || arg == "--stats") show_stats = true;
        else if (arg == "-r" || arg == "--reset") reset_stats = true;
        else if (arg == "-h" || arg == "--help") {
            cout << "Использование: wordle [ru|en] [-s] [-r]" << endl;
            return 0;
        }
    }

    string home = getenv("HOME") ? getenv("HOME") : "";
    string stats_path = home + "/.wordle_stats.json";
    Stats stats;
    if (fs::exists(stats_path)) {
        ifstream f(stats_path);
        if (f) {
            string content((istreambuf_iterator<char>(f)), istreambuf_iterator<char>());
            // Простой JSON-парсинг (для демонстрации)
            auto parse_int = [&](const string& key) {
                size_t pos = content.find("\"" + key + "\":");
                if (pos != string::npos) {
                    pos += key.size() + 3;
                    size_t end = content.find_first_of(",}", pos);
                    return stoi(content.substr(pos, end-pos));
                }
                return 0;
            };
            stats.played = parse_int("played");
            stats.wins = parse_int("wins");
            stats.current_streak = parse_int("current_streak");
            stats.max_streak = parse_int("max_streak");
        }
    }
    if (reset_stats) {
        stats = Stats();
        ofstream f(stats_path);
        if (f) f << "{\"played\":0,\"wins\":0,\"current_streak\":0,\"max_streak\":0}";
        cout << "Статистика сброшена." << endl;
        return 0;
    }
    if (show_stats) {
        cout << "Сыграно игр: " << stats.played << endl;
        cout << "Побед: " << stats.wins << endl;
        cout << "Процент побед: " << (stats.played ? (stats.wins*100.0/stats.played) : 0) << "%" << endl;
        cout << "Текущая серия: " << stats.current_streak << endl;
        cout << "Максимальная серия: " << stats.max_streak << endl;
        return 0;
    }

    Wordle game(lang);
    cout << colorize("Слово загадано. У вас 6 попыток. Вводите 5-буквенные слова.", BOLD) << endl;
    while (game.attempts > 0 && !game.isWin()) {
        cout << "Ваше слово: ";
        string guess;
        cin >> guess;
        if (guess == "q") { cout << "Выход." << endl; break; }
        if (guess == "?") {
            if (!game.hint_used) {
                cout << "Подсказка: первая буква '" << game.word[0] << "'" << endl;
                game.hint_used = true;
            } else {
                cout << "Вы уже использовали подсказку." << endl;
            }
            continue;
        }
        if (game.guess(guess)) {
            game.display();
        }
    }
    // Обновление статистики
    stats.played++;
    if (game.isWin()) {
        stats.wins++;
        stats.current_streak++;
        if (stats.current_streak > stats.max_streak) stats.max_streak = stats.current_streak;
    } else {
        stats.current_streak = 0;
    }
    ofstream f(stats_path);
    if (f) f << "{\"played\":" << stats.played << ",\"wins\":" << stats.wins
             << ",\"current_streak\":" << stats.current_streak
             << ",\"max_streak\":" << stats.max_streak << "}";
    return 0;
}
