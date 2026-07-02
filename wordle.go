// wordle.go
package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"math/rand"
	"os"
	"path/filepath"
	"strings"
	"time"
)

const (
	reset  = "\033[0m"
	green  = "\033[92m"
	yellow = "\033[93m"
	gray   = "\033[90m"
	bold   = "\033[1m"
)

func colorize(text, color string) string {
	return color + text + reset
}

var WORDS_RU = []string{
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
	"январь", "яркость", "яхта",
}

var WORDS_EN = []string{
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
	"fruit",
}

type Stats struct {
	Played        int `json:"played"`
	Wins          int `json:"wins"`
	CurrentStreak int `json:"current_streak"`
	MaxStreak     int `json:"max_streak"`
}

type Wordle struct {
	word       string
	words      []string
	attempts   int
	guesses    []string
	results    [][]string
	hintUsed   bool
}

func NewWordle(lang string) *Wordle {
	var words []string
	if lang == "ru" {
		words = WORDS_RU
	} else {
		words = WORDS_EN
	}
	rand.Seed(time.Now().UnixNano())
	word := words[rand.Intn(len(words))]
	w := &Wordle{
		word:     strings.ToUpper(word),
		words:    words,
		attempts: 6,
		hintUsed: false,
	}
	return w
}

func (w *Wordle) Guess(input string) (bool, string) {
	guess := strings.ToUpper(input)
	if len(guess) != 5 {
		return false, "Слово должно быть из 5 букв"
	}
	found := false
	for _, wrd := range w.words {
		if strings.ToUpper(wrd) == guess {
			found = true
			break
		}
	}
	if !found {
		return false, "Слова нет в словаре"
	}
	w.guesses = append(w.guesses, guess)
	colors := w.check(guess)
	w.results = append(w.results, colors)
	w.attempts--
	return true, ""
}

func (w *Wordle) check(guess string) []string {
	result := make([]string, 5)
	for i := 0; i < 5; i++ {
		result[i] = "gray"
	}
	target := []rune(w.word)
	g := []rune(guess)
	// Зеленые
	for i := 0; i < 5; i++ {
		if g[i] == target[i] {
			result[i] = "green"
			target[i] = ' '
			g[i] = ' '
		}
	}
	// Желтые
	for i := 0; i < 5; i++ {
		if g[i] != ' ' {
			for j := 0; j < 5; j++ {
				if target[j] != ' ' && g[i] == target[j] {
					result[i] = "yellow"
					target[j] = ' '
					break
				}
			}
		}
	}
	return result
}

func (w *Wordle) IsWin() bool {
	return len(w.guesses) > 0 && w.guesses[len(w.guesses)-1] == w.word
}

func (w *Wordle) IsLoss() bool {
	return w.attempts == 0 && !w.IsWin()
}

func (w *Wordle) Display() {
	fmt.Println(colorize(fmt.Sprintf("Угадай слово (5 букв, %d попыток)", w.attempts), bold))
	for i, guess := range w.guesses {
		for j, ch := range guess {
			var col string
			switch w.results[i][j] {
			case "green":
				col = green
			case "yellow":
				col = yellow
			default:
				col = gray
			}
			fmt.Print(colorize(string(ch), col) + " ")
		}
		fmt.Println()
	}
	if w.IsWin() {
		fmt.Println(colorize("Поздравляем! Вы угадали слово!", green))
	} else if w.IsLoss() {
		fmt.Println(colorize("Вы проиграли. Загаданное слово: "+w.word, gray))
	}
}

func main() {
	lang := "ru"
	showStats := false
	resetStats := false
	args := os.Args[1:]
	if len(args) > 0 {
		if args[0] == "ru" || args[0] == "en" {
			lang = args[0]
		} else if args[0] == "-s" || args[0] == "--stats" {
			showStats = true
		} else if args[0] == "-r" || args[0] == "--reset" {
			resetStats = true
		} else if args[0] == "-h" || args[0] == "--help" {
			fmt.Println("Использование: wordle [ru|en] [-s] [-r]")
			return
		}
	}

	home := os.Getenv("HOME")
	statsPath := filepath.Join(home, ".wordle_stats.json")
	var stats Stats
	if data, err := os.ReadFile(statsPath); err == nil {
		json.Unmarshal(data, &stats)
	}
	if resetStats {
		stats = Stats{}
		data, _ := json.MarshalIndent(stats, "", "  ")
		os.WriteFile(statsPath, data, 0644)
		fmt.Println("Статистика сброшена.")
		return
	}
	if showStats {
		fmt.Printf("Сыграно игр: %d\n", stats.Played)
		fmt.Printf("Побед: %d\n", stats.Wins)
		fmt.Printf("Процент побед: %.1f%%\n", float64(stats.Wins)/float64(stats.Played)*100)
		fmt.Printf("Текущая серия: %d\n", stats.CurrentStreak)
		fmt.Printf("Максимальная серия: %d\n", stats.MaxStreak)
		return
	}

	game := NewWordle(lang)
	fmt.Println(colorize("Слово загадано. У вас 6 попыток. Вводите 5-буквенные слова.", bold))
	reader := bufio.NewReader(os.Stdin)
	for game.attempts > 0 && !game.IsWin() {
		fmt.Print("Ваше слово: ")
		input, _ := reader.ReadString('\n')
		input = strings.TrimSpace(input)
		if input == "q" {
			fmt.Println("Выход.")
			break
		}
		if input == "?" {
			if !game.hintUsed {
				fmt.Printf("Подсказка: первая буква '%c'\n", game.word[0])
				game.hintUsed = true
			} else {
				fmt.Println("Вы уже использовали подсказку.")
			}
			continue
		}
		if ok, msg := game.Guess(input); ok {
			game.Display()
		} else {
			fmt.Println(msg)
		}
	}
	stats.Played++
	if game.IsWin() {
		stats.Wins++
		stats.CurrentStreak++
		if stats.CurrentStreak > stats.MaxStreak {
			stats.MaxStreak = stats.CurrentStreak
		}
	} else {
		stats.CurrentStreak = 0
	}
	data, _ := json.MarshalIndent(stats, "", "  ")
	os.WriteFile(statsPath, data, 0644)
}
