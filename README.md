
## Описание
**Практикум SDET: задание UI**

**Объект тестирования:** https://automationteststore.com/

## Технологии
- Java 25
- Gradle
- Selenium WebDriver
- JUnit 5
- Allure Report

## Тест-кейсы

Подробное описание — в файле [TEST_CASES.md](TEST_CASES.md).

1. **TC-01** — Сортировка товаров в любой категории по имени (возрастание)
2. **TC-02** — Сортировка товаров в любой категории по имени (убывание)
3. **TC-03** — Сортировка товаров в любой категории по цене (возрастание)
4. **TC-04** — Сортировка товаров в любой категории по цене (убывание)
5. **TC-05** — Поиск товаров и добавление в корзину
6. **TC-06** — Добавление случайных товаров и удаление четных позиций

## Запуск тестов

### Запустить все тесты
```bash
./gradlew test
```

### Запустить конкретный класс
```bash
./gradlew test --tests "tests.FilterTest"
./gradlew test --tests "tests.SearchTest"
./gradlew test --tests "tests.RandomAddToCartTest"
```

### Сгенерировать Allure-отчёт (Доп. задание №1)
```bash
./gradlew test allureReport
# Открыть отчёт в браузере:
./gradlew allureServe
```

## Параллельный запуск (Доп. задание №2)

```bash
./gradlew testParallel
```

