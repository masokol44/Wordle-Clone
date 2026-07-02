# wordle.py
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import sys
import random
import json
import os
from pathlib import Path

# ANSI цвета
COLORS = {
    'reset': '\033[0m',
    'green': '\033[92m',
    'yellow': '\033[93m',
    'gray': '\033[90m',
    'bold': '\033[1m'
}

def colorize(text, color):
    return f"{COLORS.get(color, '')}{text}{COLORS['reset']}"

# Встроенные словари (5-буквенные слова)
WORDS_RU = [
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
]

WORDS_EN = [
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
    "fruit", "ghost", "giant", "given", "glad", "gleam", "glide", "globe",
    "gloom", "glory", "gloss", "glove", "glow", "gnome", "gold", "good",
    "grace", "grand", "grant", "grape", "graph", "grasp", "grass", "grate",
    "gravel", "great", "green", "greet", "grief", "grill", "grind", "grip",
    "groan", "groom", "gross", "ground", "group", "grove", "growl", "grown",
    "guard", "guess", "guest", "guide", "guild", "guilt", "gulf", "gush",
    "gust", "habit", "halo", "halt", "happy", "harsh", "haste", "hate",
    "haunt", "heart", "heavy", "hello", "honey", "honor", "horse", "hotel",
    "house", "human", "humor", "hurry", "hut", "ideal", "image", "imply",
    "inbox", "index", "indie", "infer", "inner", "input", "issue", "ivory",
    "jolly", "joust", "joyful", "jumpy", "jumbo", "jewel", "juice", "joker",
    "jolly", "joust", "judge", "juice", "jumbo", "jumpy", "jewel", "joker",
    "jolly", "joust", "judge", "juice", "jumbo", "jumpy", "jewel", "joker",
    "jolly", "joust", "judge", "juice", "jumbo", "jumpy", "jewel", "joker",
    "jolly", "joust", "judge", "juice", "jumbo", "jumpy", "jewel", "joker",
    "jolly", "joust", "judge", "juice", "jumbo", "jumpy", "jewel", "joker",
    "jolly", "joust", "judge", "juice", "jumbo", "jumpy", "jewel", "joker"
]

class Wordle:
    def __init__(self, lang='ru'):
        self.lang = lang
        self.words = WORDS_RU if lang == 'ru' else WORDS_EN
        self.word = random.choice(self.words).upper()
        self.attempts = 6
        self.guesses = []
        self.colors = []
        self.hint_used = False

    def guess(self, word):
        word = word.upper()
        if len(word) != 5:
            return False, "Слово должно быть из 5 букв"
        if word not in self.words:
            return False, "Слова нет в словаре"
        self.guesses.append(word)
        result = self.check(word)
        self.colors.append(result)
        self.attempts -= 1
        return True, result

    def check(self, word):
        result = ['gray'] * 5
        word_list = list(word)
        target_list = list(self.word)
        # Зелёные
        for i in range(5):
            if word_list[i] == target_list[i]:
                result[i] = 'green'
                target_list[i] = None
                word_list[i] = None
        # Жёлтые
        for i in range(5):
            if word_list[i] is not None and word_list[i] in target_list:
                result[i] = 'yellow'
                target_list[target_list.index(word_list[i])] = None
        return result

    def is_win(self):
        return self.guesses and self.guesses[-1] == self.word

    def is_loss(self):
        return self.attempts == 0 and not self.is_win()

    def display(self):
        print(colorize(f"Угадай слово (5 букв, {self.attempts} попыток)", 'bold'))
        for i, guess in enumerate(self.guesses):
            row = ''
            for j, ch in enumerate(guess):
                color = self.colors[i][j]
                if color == 'green':
                    row += colorize(ch, 'green') + ' '
                elif color == 'yellow':
                    row += colorize(ch, 'yellow') + ' '
                else:
                    row += colorize(ch, 'gray') + ' '
            print(row)
        if self.is_win():
            print(colorize("Поздравляем! Вы угадали слово!", 'green'))
        elif self.is_loss():
            print(colorize(f"Вы проиграли. Загаданное слово: {self.word}", 'red'))

def main():
    lang = 'ru'
    show_stats = False
    reset_stats = False
    if len(sys.argv) > 1:
        if sys.argv[1] in ['ru', 'en']:
            lang = sys.argv[1]
        elif sys.argv[1] in ['-s', '--stats']:
            show_stats = True
        elif sys.argv[1] in ['-r', '--reset']:
            reset_stats = True
        elif sys.argv[1] in ['-h', '--help']:
            print("Использование: python wordle.py [ru|en] [-s] [-r]")
            sys.exit(0)
    # Статистика
    stats_file = Path.home() / '.wordle_stats.json'
    stats = {'played': 0, 'wins': 0, 'current_streak': 0, 'max_streak': 0}
    if stats_file.exists():
        with open(stats_file, 'r') as f:
            stats = json.load(f)
    if reset_stats:
        stats = {'played': 0, 'wins': 0, 'current_streak': 0, 'max_streak': 0}
        with open(stats_file, 'w') as f:
            json.dump(stats, f, indent=2)
        print("Статистика сброшена.")
        sys.exit(0)
    if show_stats:
        print(f"Сыграно игр: {stats['played']}")
        print(f"Побед: {stats['wins']}")
        print(f"Процент побед: {stats['wins']/stats['played']*100 if stats['played'] > 0 else 0:.1f}%")
        print(f"Текущая серия: {stats['current_streak']}")
        print(f"Максимальная серия: {stats['max_streak']}")
        sys.exit(0)

    game = Wordle(lang)
    print(colorize(f"Слово загадано. У вас 6 попыток. Вводите 5-буквенные слова.", 'bold'))
    while game.attempts > 0 and not game.is_win():
        guess = input("Ваше слово: ").strip().lower()
        if guess == 'q':
            print("Выход.")
            break
        if guess == '?':
            if not game.hint_used:
                print(f"Подсказка: первая буква '{game.word[0]}'")
                game.hint_used = True
            else:
                print("Вы уже использовали подсказку.")
            continue
        valid, msg = game.guess(guess)
        if not valid:
            print(msg)
            continue
        game.display()
    # Обновление статистики
    stats['played'] += 1
    if game.is_win():
        stats['wins'] += 1
        stats['current_streak'] += 1
        if stats['current_streak'] > stats['max_streak']:
            stats['max_streak'] = stats['current_streak']
    else:
        stats['current_streak'] = 0
    with open(stats_file, 'w') as f:
        json.dump(stats, f, indent=2)

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("\nВыход.")
        sys.exit(0)
