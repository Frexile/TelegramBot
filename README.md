***TelegramBot***
===
**_Описание_**
---
Данный Телеграм-бот создан для вывода информации о курсе валют согласно [ЦБ РФ](http://www.cbr.ru/currency/). а также для предоставления информации относительно курса валют в 5 самых выгодных банках в городах РФ с населением больше 500.000 человек.

**_Список валют:_**
---
1. Американский доллар **_(USD)_**
2. Евро **_(EUR)_**
3. Фунт стерлингов **_(GBP)_**
4. Китайский юань **_(CNY)_**
5. Японская йена **_(JPY)_**

**_Список городов:_**
---
* Москва
* Санкт-Петербург
* Екатеринбург
* Казань
* Нижний Новгород
* Самара
* Красноярск
* Краснодар
* Ижевск
* Хабаровск
* Томск
* Челябинск
* Пермь
* Саратов
* Барнаул
* Ярославль
* Оренбург
* Новосибирск
* Ростов на Дону
* Воронеж
* Тюмень
* Иркутск 
* Владивосток
* Омск
* Уфа
* Волгоград
* Тольятти
* Ульяновск
* Махачкала
* Кемерово
* Новокузнецк
* Рязань
* Астрахань
* Набережные челны
* Пенза
* Киров
* Липецк

_Информация относительно курса валют по регионам была взята с данного [сайта](https://ru.myfin.by/currency/)._

**_Создание бота_**
---
1. Пишите Телеграм-боту [@BotFather](https://t.me/botfather) и в поле для сообщений отправляйте команду **`/start`**.
2. Напишите команду  **`/newbot`** для создания нового бота.
3. Следуйте инструкциям [@BotFather](https://t.me/botfather) и в конечном итоге он вам выдаст уникальный токен для вашего бота.
4. Чтобы добавить бота в групповой чат напишите боту **`/setjoingroups`**.
5. Чтобы бот мог реагировать не только на команды, начинающиеся с **`/`** прописываете команду **`/setprivacy`** и устанавливаете статус **`DISABLED`**

**_Запуск бота_**
---
1. Если вы НЕ собираетесь использовать [Intellij IDEA](https://www.jetbrains.com/ru-ru/idea/) убедитесь, что у вас локально установлен [Java JDK8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html). 
1. [Скачайте](https://github.com/Gocers/TelegramBot/archive/master.zip) данный репозиторий.
2. Откройте файл `Bot.java`, и в поле `botToken` пропишите токен для вашего бота полученный во время _**создания бота в п.3**_.
3. Для запуска, введите в командную строку следующее `java src.main.java.Bot` или же запустите через [Intellij IDEA](https://www.jetbrains.com/ru-ru/idea/) метод **`Bot.main`** в классе **`Bot.java`**.

**_Библиотеки и Maven-зависимости_**
---
* [telegrambots](https://github.com/rubenlagus/TelegramBots)

```html
  <dependency>
            <groupId>org.telegram</groupId>
                <artifactId>telegrambots</artifactId>
            <version>4.9</version>
  </dependency>
```

* [junit](https://github.com/junit-team/junit4/)

```html
  <dependency>
            <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                  <version>4.12</version>
            <scope>test</scope>
   </dependency>
```

* [jsoup](https://github.com/jhy/jsoup)

```html
  <dependency>
            <groupId>org.jsoup</groupId>
                <artifactId>jsoup</artifactId>
            <version>1.13.1</version>
  </dependency>
```

* [gson](https://github.com/google/gson)

```html
  <dependency>
            <groupId>com.google.code.gson</groupId>
                <artifactId>gson</artifactId>
            <version>2.8.6</version>
  </dependency>
```
