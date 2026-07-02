// wordle.js
#!/usr/bin/env node
'use strict';

const fs = require('fs');
const path = require('path');
const os = require('os');
const readline = require('readline');

const COLORS = {
    reset: '\x1b[0m',
    green: '\x1b[92m',
    yellow: '\x1b[93m',
    gray: '\x1b[90m',
    bold: '\x1b[1m'
};

function colorize(text, color) {
    return COLORS[color] + text + COLORS.reset;
}

const WORDS_RU = [
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
];

const WORDS_EN = [
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
];

class Wordle {
    constructor(lang) {
        this.words = lang === 'ru' ? WORDS_RU : WORDS_EN;
        this.word = this.words[Math.floor(Math.random() * this.words.length)].toUpperCase();
        this.attempts = 6;
        this.guesses = [];
        this.results = [];
        this.hintUsed = false;
    }

    guess(input) {
        const guess = input.toUpperCase();
        if (guess.length !== 5) return { ok: false, msg: 'Слово должно быть из 5 букв' };
        if (!this.words.some(w => w.toUpperCase() === guess)) {
            return { ok: false, msg: 'Слова нет в словаре' };
        }
        this.guesses.push(guess);
        const colors = this.check(guess);
        this.results.push(colors);
        this.attempts--;
        return { ok: true };
    }

    check(guess) {
        const result = ['gray', 'gray', 'gray', 'gray', 'gray'];
        const target = this.word.split('');
        const g = guess.split('');
        // зеленые
        for (let i = 0; i < 5; i++) {
            if (g[i] === target[i]) {
                result[i] = 'green';
                target[i] = ' ';
                g[i] = ' ';
            }
        }
        // желтые
        for (let i = 0; i < 5; i++) {
            if (g[i] !== ' ') {
                const idx = target.indexOf(g[i]);
                if (idx !== -1) {
                    result[i] = 'yellow';
                    target[idx] = ' ';
                }
            }
        }
        return result;
    }

    isWin() {
        return this.guesses.length > 0 && this.guesses[this.guesses.length-1] === this.word;
    }

    isLoss() {
        return this.attempts === 0 && !this.isWin();
    }

    display() {
        console.log(colorize(`Угадай слово (5 букв, ${this.attempts} попыток)`, 'bold'));
        for (let i = 0; i < this.guesses.length; i++) {
            let line = '';
            for (let j = 0; j < 5; j++) {
                const ch = this.guesses[i][j];
                const col = this.results[i][j] === 'green' ? 'green' :
                           this.results[i][j] === 'yellow' ? 'yellow' : 'gray';
                line += colorize(ch, col) + ' ';
            }
            console.log(line);
        }
        if (this.isWin()) {
            console.log(colorize('Поздравляем! Вы угадали слово!', 'green'));
        } else if (this.isLoss()) {
            console.log(colorize('Вы проиграли. Загаданное слово: ' + this.word, 'gray'));
        }
    }
}

async function main() {
    const args = process.argv.slice(2);
    let lang = 'ru';
    let showStats = false, resetStats = false;
    if (args.length) {
        if (args[0] === 'ru' || args[0] === 'en') lang = args[0];
        else if (args[0] === '-s' || args[0] === '--stats') showStats = true;
        else if (args[0] === '-r' || args[0] === '--reset') resetStats = true;
        else if (args[0] === '-h' || args[0] === '--help') {
            console.log('Использование: node wordle.js [ru|en] [-s] [-r]');
            process.exit(0);
        }
    }

    const statsFile = path.join(os.homedir(), '.wordle_stats.json');
    let stats = { played: 0, wins: 0, current_streak: 0, max_streak: 0 };
    if (fs.existsSync(statsFile)) {
        stats = JSON.parse(fs.readFileSync(statsFile, 'utf8'));
    }
    if (resetStats) {
        stats = { played: 0, wins: 0, current_streak: 0, max_streak: 0 };
        fs.writeFileSync(statsFile, JSON.stringify(stats, null, 2));
        console.log('Статистика сброшена.');
        return;
    }
    if (showStats) {
        console.log(`Сыграно игр: ${stats.played}`);
        console.log(`Побед: ${stats.wins}`);
        console.log(`Процент побед: ${stats.played ? (stats.wins/stats.played*100).toFixed(1) : 0}%`);
        console.log(`Текущая серия: ${stats.current_streak}`);
        console.log(`Максимальная серия: ${stats.max_streak}`);
        return;
    }

    const game = new Wordle(lang);
    const rl = readline.createInterface({
        input: process.stdin,
        output: process.stdout
    });
    const question = (q) => new Promise(resolve => rl.question(q, resolve));

    console.log(colorize('Слово загадано. У вас 6 попыток. Вводите 5-буквенные слова.', 'bold'));
    while (game.attempts > 0 && !game.isWin()) {
        const input = await question('Ваше слово: ');
        const cmd = input.trim().toLowerCase();
        if (cmd === 'q') { console.log('Выход.'); break; }
        if (cmd === '?') {
            if (!game.hintUsed) {
                console.log(`Подсказка: первая буква '${game.word[0]}'`);
                game.hintUsed = true;
            } else {
                console.log('Вы уже использовали подсказку.');
            }
            continue;
        }
        const result = game.guess(cmd);
        if (!result.ok) {
            console.log(result.msg);
            continue;
        }
        game.display();
    }
    stats.played++;
    if (game.isWin()) {
        stats.wins++;
        stats.current_streak++;
        if (stats.current_streak > stats.max_streak) stats.max_streak = stats.current_streak;
    } else {
        stats.current_streak = 0;
    }
    fs.writeFileSync(statsFile, JSON.stringify(stats, null, 2));
    rl.close();
}

main().catch(console.error);
