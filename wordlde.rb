#!/usr/bin/env ruby
# wordle.rb
# encoding: UTF-8

require 'json'
require 'fileutils'

COLORS = {
  reset: "\e[0m",
  green: "\e[92m",
  yellow: "\e[93m",
  gray: "\e[90m",
  bold: "\e[1m"
}

def colorize(text, color)
  "#{COLORS[color]}#{text}#{COLORS[:reset]}"
end

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
  "fruit"
]

class Wordle
  attr_reader :word, :attempts, :hint_used

  def initialize(lang)
    @words = lang == 'ru' ? WORDS_RU : WORDS_EN
    @word = @words.sample.upcase
    @attempts = 6
    @guesses = []
    @results = []
    @hint_used = false
  end

  def guess(input)
    guess = input.upcase
    return [false, "Слово должно быть из 5 букв"] if guess.size != 5
    return [false, "Слова нет в словаре"] unless @words.any? { |w| w.upcase == guess }
    @guesses << guess
    @results << check(guess)
    @attempts -= 1
    [true, ""]
  end

  def check(guess)
    result = ['gray'] * 5
    target = @word.chars
    g = guess.chars
    # зеленые
    (0...5).each do |i|
      if g[i] == target[i]
        result[i] = 'green'
        target[i] = ' '
        g[i] = ' '
      end
    end
    # желтые
    (0...5).each do |i|
      next if g[i] == ' '
      idx = target.index(g[i])
      if idx
        result[i] = 'yellow'
        target[idx] = ' '
      end
    end
    result
  end

  def win?
    @guesses.any? && @guesses.last == @word
  end

  def loss?
    @attempts == 0 && !win?
  end

  def display
    puts colorize("Угадай слово (5 букв, #{@attempts} попыток)", :bold)
    @guesses.each_with_index do |guess, i|
      row = ''
      guess.chars.each_with_index do |ch, j|
        col = @results[i][j] == 'green' ? :green : @results[i][j] == 'yellow' ? :yellow : :gray
        row += colorize(ch, col) + ' '
      end
      puts row
    end
    if win?
      puts colorize("Поздравляем! Вы угадали слово!", :green)
    elsif loss?
      puts colorize("Вы проиграли. Загаданное слово: #{@word}", :gray)
    end
  end

  def use_hint
    @hint_used = true
  end
end

def main
  lang = 'ru'
  show_stats = false
  reset_stats = false
  if ARGV.size > 0
    if ARGV[0] == 'ru' || ARGV[0] == 'en'
      lang = ARGV[0]
    elsif ARGV[0] == '-s' || ARGV[0] == '--stats'
      show_stats = true
    elsif ARGV[0] == '-r' || ARGV[0] == '--reset'
      reset_stats = true
    elsif ARGV[0] == '-h' || ARGV[0] == '--help'
      puts "Использование: ruby wordle.rb [ru|en] [-s] [-r]"
      exit
    end
  end

  stats_file = File.join(Dir.home, '.wordle_stats.json')
  stats = { 'played' => 0, 'wins' => 0, 'current_streak' => 0, 'max_streak' => 0 }
  if File.exist?(stats_file)
    stats = JSON.parse(File.read(stats_file))
  end
  if reset_stats
    stats = { 'played' => 0, 'wins' => 0, 'current_streak' => 0, 'max_streak' => 0 }
    File.write(stats_file, JSON.pretty_generate(stats))
    puts "Статистика сброшена."
    return
  end
  if show_stats
    puts "Сыграно игр: #{stats['played']}"
    puts "Побед: #{stats['wins']}"
    puts "Процент побед: #{stats['played'] > 0 ? (stats['wins']*100.0/stats['played']).round(1) : 0}%"
    puts "Текущая серия: #{stats['current_streak']}"
    puts "Максимальная серия: #{stats['max_streak']}"
    return
  end

  game = Wordle.new(lang)
  puts colorize("Слово загадано. У вас 6 попыток. Вводите 5-буквенные слова.", :bold)
  while game.attempts > 0 && !game.win?
    print "Ваше слово: "
    input = gets.chomp.strip
    if input == 'q'
      puts "Выход."
      break
    end
    if input == '?'
      if !game.hint_used
        puts "Подсказка: первая буква '#{game.word[0]}'"
        game.use_hint
      else
        puts "Вы уже использовали подсказку."
      end
      next
    end
    ok, msg = game.guess(input)
    if ok
      game.display
    else
      puts msg
    end
  end
  stats['played'] += 1
  if game.win?
    stats['wins'] += 1
    stats['current_streak'] += 1
    stats['max_streak'] = [stats['max_streak'], stats['current_streak']].max
  else
    stats['current_streak'] = 0
  end
  File.write(stats_file, JSON.pretty_generate(stats))
end

main if __FILE__ == $0
