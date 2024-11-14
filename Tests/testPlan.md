# План тестирования

---

## Содержание
1. [Введение](#introduction)  
2. [Объект тестирования](#items)  
3. [Атрибуты качества](#quality)  
4. [Риски](#risk)  
5. [Аспекты тестирования](#features)  
6. [Подходы к тестированию](#approach)  
7. [Представление результатов](#pass)  
8. [Выводы](#conclusion)

<a name="introduction"/>

## Введение

Данный документ описывает план тестирования приложения "MusicApp". Документ предназначен для людей, выполняющих тестирование данного проекта. Цель тестирования — проверка соответствия реального поведения программы проекта и ее ожидаемого поведения, которое описано в требованиях.

<a name="items"/>

## Объект тестирования

В качестве объектов тестирования можно выделить следующие функциональные требования:

- Вход в аккаунт;
- Создание нового аккаунта;
- Поиск музыкальных файлов;
- Добавление треков в раздел "Моя музыка";
- Прослушивание треков перед добавлением;
- Управление воспроизведением в разделе "Моя музыка";
- Удаление треков из списка;
- Добавление треков в очередь.

<a name="quality"/>

## Атрибуты качества

1. **Функциональность**:
    - Функциональная полнота: приложение должно выполнять все заявленные функции;
    - Функциональная корректность: приложение должно выполнять все заявленные функции корректно.

2. **Удобство использования**:
    - Все функциональные элементы пользовательского интерфейса имеют названия, описывающие действие, которое произойдет при выборе элемента.

<a name="risk"/>

## Риски

К рискам можно отнести:
- Проблемы с доступом к файловой системе устройства, что может повлиять на поиск музыкальных файлов;
- Ошибки при добавлении или удалении треков, что может привести к неполной функциональности;
- Возможные сбои при воспроизведении треков.

<a name="features"/>

## Аспекты тестирования

В ходе тестирования планируется проверить реализацию основных функций приложения. Аспекты, подвергаемые тестированию:  
- Запуск приложения;  
- Создание нового пользователя;  
- Вход в существующий аккаунт;  
- Поиск музыкальных файлов;  
- Добавление треков в раздел "Моя музыка";  
- Прослушивание треков перед добавлением;  
- Управление воспроизведением треков;  
- Удаление треков из раздела "Моя музыка";  
- Добавление треков в очередь.

### Запуск приложения
Необходимо протестировать:
- Запуск приложения;
- Отображение окна приложения.

### Создание нового пользователя
Необходимо протестировать:
- Добавление данных о пользователе в базу данных;
- Сообщение об успешной регистрации.

### Вход в существующий аккаунт
Необходимо протестировать:
- Ввод корректных данных для входа;
- Проверка сообщений при некорректных данных.

### Поиск музыкальных файлов
Необходимо протестировать:
- Отображение всех mp3 файлов на устройстве;
- Предварительное прослушивание треков.

### Добавление треков в "Моя музыка"
Необходимо протестировать:
- Добавление треков в список;
- Проверка отображения добавленных треков.

### Управление воспроизведением
Необходимо протестировать:
- Воспроизведение треков;
- Пауза, пропуск и повтор треков.

### Удаление треков
Необходимо протестировать:
- Удаление треков из списка;
- Проверка сообщения о подтверждении удаления.

### Добавление треков в очередь
Необходимо протестировать:
- Добавление треков в очередь воспроизведения;
- Проверка порядка треков в очереди.

<a name="approach"/>

## Подходы к тестированию

Тестирование будет проводиться вручную, с использованием позитивных и негативных сценариев. Будет проверяться функциональность и удобство использования.

<a name="pass"/>

## Представление результатов

Результаты тестирования будут представлены в виде отчетов, содержащих описание сценариев тестирования, результаты и возможные ошибки.

<a name="conclusion"/>

## Выводы

В случае успешного прохождения всех тестов приложение будет считаться готовым для использования. В случае выявления ошибок, они будут проанализированы и исправлены перед повторным тестированием.
