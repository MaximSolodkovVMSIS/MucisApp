# Требования к проекту
---

# Содержание
1 [Введение](#intro)  
1.1 [Назначение](#appointment)  
1.2 [Бизнес-требования](#business_requirements)  
1.2.1 [Исходные данные](#initial_data)  
1.2.2 [Возможности бизнеса](#business_opportunities)  
1.2.3 [Границы проекта](#project_boundary)  
1.3 [Аналоги](#analogues)  
2 [Требования пользователя](#user_requirements)  
2.1 [Программные интерфейсы](#software_interfaces)  
2.2 [Интерфейс пользователя](#user_interface)  
2.3 [Характеристики пользователей](#user_specifications)  
2.3.1 [Классы пользователей](#user_classes)  
2.3.2 [Аудитория приложения](#application_audience)  
2.3.2.1 [Целевая аудитория](#target_audience)  
2.3.2.1 [Побочная аудитория](#collateral_audience)  
2.4 [Предположения и зависимости](#assumptions_and_dependencies)  
3 [Системные требования](#system_requirements)  
3.1 [Функциональные требования](#functional_requirements)  
3.1.1 [Основные функции](#main_functions)  
3.1.1.1 [Вход пользователя в приложение](#user_logon_to_the_application)  
3.1.1.2 [Настройка профиля активного пользователя](#setting_up_the_profile_of_the_active_user)  
3.1.1.3 [Загрузка списка музыки(Кнопка "поиск")](#download_news)  
3.1.1.4 [Просмотр списка музыки(Кнопка "моя музыка")](#view_information_about_an_individual_newsletter)  
3.1.1.5 [Управление воспроизведением(кнопка "плеер")](#active_user_change)  
3.1.1.6 [Регистрация нового пользователя после входа в приложение](#add_new_user)  
3.1.2 [Ограничения и исключения](#restrictions_and_exclusions)  
3.2 [Нефункциональные требования](#non-functional_requirements)  
3.2.1 [Атрибуты качества](#quality_attributes)  
3.2.1.1 [Требования к удобству использования](#requirements_for_ease_of_use)    
3.2.1.2 [Требования к безопасности](#security_requirements)  
3.2.1.3 [Требования к доступности](#access_requirements)  
3.2.2 [Внешние интерфейсы](#external_interfaces)  
3.2.3 [Ограничения](#restrictions)  

<a name="intro"/>

# 1 Введение

<a name="appointment"/>

## 1.1 Назначение
В этом документе описаны функциональные и нефункциональные требования к приложению «Музыкальный плеер» для ОС Android. Этот документ предназначен для команды, которая будет реализовывать и проверять корректность работы приложения.

<a name="business_requirements"/>

## 1.2 Бизнес-требования

<a name="initial_data"/>

### 1.2.1 Исходные данные
Большое количество людей слушает музыку с помощью мобильных устройств. Каждый из них имеет свои предпочтения в жанрах и исполнителях. Современные музыкальные приложения предоставляют возможность искать музыку, сохранять её в плейлисты и управлять воспроизведением через удобный интерфейс. Пользователи стремятся иметь доступ к музыке в любом месте и в любое время, а также возможность просматривать свою музыкальную библиотеку и управлять ею через удобные функции поиска и каталогизации.

<a name="business_opportunities"/>

### 1.2.2 Возможности бизнеса
Многие пользователи хотят иметь простое и интуитивное приложение, которое позволит не только искать и воспроизводить музыку, но и сохранять её в персональные коллекции. Приложение должно предоставлять базовые функции управления музыкой, такие как воспроизведение, пауза, добавление в избранное, а также возможность регистрации для персонализации музыкального опыта. Оно также должно поддерживать поиск музыки через интерфейс, который сможет удовлетворить требования различных категорий пользователей.

<a name="project_boundary"/>

### 1.2.3 Границы проекта
Приложение «Музыкальный плеер» позволит пользователям регистрироваться, искать музыку, сохранять любимые треки в плейлисты и управлять воспроизведением. Основной функционал приложения включает регистрацию, поиск музыки, отображение сохранённой музыки и плеер для воспроизведения треков.

<a name="analogues"/>

## 1.3 Аналоги
Аналогами в сфере музыкальных приложений являются Spotify, Яндекс Музыка, Apple Music и Deezer. Эти сервисы предлагают обширные библиотеки треков, персонализированные рекомендации, создание плейлистов и оффлайн-прослушивание. Spotify и Яндекс Музыка выделяются функциями рекомендаций и интеграцией с соцсетями, но Spotify ограничен в некоторых странах. Яндекс Музыка также позволяет слушать подкасты и интегрируется с другими сервисами Яндекса. Главный недостаток таких приложений — зависимость от интернета для стриминга.

<a name="user_requirements"/>

# 2 Требования пользователя

<a name="software_interfaces"/>

## 2.1 Программные интерфейсы
Приложение будет просматривать все файлы формата mp3 из вашего проводника, т.е у вас может быть музыка из разных источников и вы сможете ее добавить в ваше приложение для дальнейшего прослушивания. Файлы можно редактировать(изменять название или добавлять группу).

<a name="user_interface"/>

## 2.2 Интерфейс пользователя
Окно входа в приложение.  

![Окно входа в приложение](https://github.com/MaximSolodkovVMSIS/MucisApp/blob/master/Requirements/Mockups/mainwindow.JPG)  

Окно регистрации или авторизации. 

![Окно регистрации или авторизации](https://github.com/MaximSolodkovVMSIS/MucisApp/blob/master/Requirements/Mockups/regwindow.JPG)

Окно поиска mp3 файлов в проводнике

![Кнопка поиска mp3 файлов в проводнике](https://github.com/MaximSolodkovVMSIS/MucisApp/blob/master/Requirements/Mockups/searchbut.JPG) 

Окно вашей музыки. 

![Окно вашей музыки](https://github.com/MaximSolodkovVMSIS/MucisApp/blob/master/Requirements/Mockups/youmusicbut.JPG)      

Окно плеера.

![Окно плеера](https://github.com/MaximSolodkovVMSIS/MucisApp/blob/master/Requirements/Mockups/playerbut.JPG)    

<a name="user_specifications"/>

## 2.3 Характеристики пользователей

<a name="user_classes"/>

### 2.3.1 Классы пользователей

| Класс пользователей | Описание |
|:---|:---|
| Гости | Пользователи, которые не хотят регистрироваться в приложении. Имеют доступ к частичному функционалу |
| Зарегистрированные пользователи | Пользователи, которые вошли в приложение, могут иметь возможность синхронизации между одинм аккаунтом |

<a name="application_audience"/>

### 2.3.2 Аудитория приложения

<a name="target_audience"/>

#### 2.3.2.1 Целевая аудитория
Пользователи проводящие свой досуг в основном за прослушиванием музыки.

<a name="collateral_audience"/>

#### 2.3.2.2 Побочная аудитория
Пользователи проводящие свой досуг не только за прослушиванием музыки.

<a name="assumptions_and_dependencies"/>

## 2.4 Предположения и зависимости
1. Приложение не будет работать без предоставления доступа к файловой системе;

<a name="system_requirements"/>

# 3 Системные требования

<a name="functional_requirements"/>

## 3.1 Функциональные требования

<a name="main_functions"/>

### 3.1.1 Основные функции

<a name="user_logon_to_the_application"/>

#### 3.1.1.1 Вход пользователя в приложение
**Описание.** Пользователь имеет возможность использовать приложение без создания собственного профиля либо войдя в свою учётную запись.

| Функция | Требования | 
|:---|:---|
| Вход в приложение без создания собственного профиля | Приложение должно предоставить пользователю возможность войти в приложение анонимно |
| <a name="registration_requirements"/>Регистрация нового пользователя | Приложение должно запросить у пользователя ввести имя для создания учётной записи. Пользователь должен либо ввести имя, либо отменить действие |
| *Пользователь с таким именем существует* | *Приложение должно известить пользователя об ошибке регистрации и запросить ввод имени. Пользователь должен либо ввести имя, либо отменить действие* |
| Вход зарегистрированного пользователя в приложение | Приложение должно предоставить пользователю список имён зарегистрированных пользователей. Пользователь должен либо выбрать из списка своё имя, либо отменить действие |

<a name="setting_up_the_profile_of_the_active_user"/>

#### 3.1.1.2 Настройка профиля активного пользователя
**Описание.** Зарегистрированный пользователь имеет возможность прослушивать свою музыку и добавлять новую.
 
| Функция | Требования | 
|:---|:---|
| Добавление новой песни | Приложение должно предоставить зарегистрированному пользователю поле для ввода названия песни. Пользователь должен либо ввести новое название, либо оставить первоначальное |
| Удаление фильма из списка | Пользователь имеет возможножность удалить музыку как из проводника, так и из приложения |

<a name="download_news"/>

#### 3.1.1.3 Загрузка списка музыки(Кнопка "поиск")
**Описание.** После входа пользователя в приложение, оно должно отправить уведомление на разрешение доступа к файловой систме, после чего прилодение просмотривает раздел "Музыка" или "Загрузки" и выводит все mp3 файлы находящиеся в соответсвующих каталогах.


<a name="view_information_about_an_individual_newsletter"/>

#### 3.1.1.4 Просмотр списка музыки(Кнопка "моя музыка")
**Описание.** Пользователь имеет возможность просмотра добавленной музыки с возможностью редактивания и воспроизведения выбранной композиции

<a name="active_user_change"/>

#### 3.1.1.5 Управление воспроизведением(кнопка "плеер")
**Описание.** Пользователь может перематывать, остановить, зациклить или перемешать прослушивание, так же вернуться к треку назад или вперед

<a name="add_new_user"/>

#### 3.1.1.6 Регистрация нового пользователя после входа в приложение
**Описание.** Посетитель имеет возможность зарегистрироваться в приложении.

**Требование.** Приложение должно предоставить посетителю возможность [зарегистрироваться в приложении](#registration_requirements). 

<a name="restrictions_and_exclusions"/>

### 3.1.2 Ограничения и исключения
1. Приложение не может добавить mp3 файл большого размера.

<a name="non-functional_requirements"/>

## 3.2 Нефункциональные требования

<a name="quality_attributes"/>

### 3.2.1 Атрибуты качества

<a name="requirements_for_ease_of_use"/>

#### 3.2.1.1 Требования к удобству использования
1. Приложение реализует все основные функции;
2. Все функциональные элементы пользовательского интерфейса имеют названия, описывающие действие, которое произойдет при выборе элемента.
3. Интерфейс не содержит лишних элементов.

<a name="security_requirements"/>

#### 3.2.1.2 Требования к безопасности
1. Приложение запрашивает доступ к файловой системе.
2. Приложение предоставляет возможность синхронизации между несколькими устройствами.

<a name="access_requirements"/>

#### 3.2.1.3 Требования к доступности
Время реакции на действия пользователя минимально.

<a name="external_interfaces"/>

### 3.2.2 Внешние интерфейсы
Окна приложения удобны для использования пользователями:
  * размер шрифта не должен быть слишком маленьким; 
  * интерфейс оформлен в соответствии с [Android стандартами](https://developer.android.com/guide/practices/ui_guidelines/index.html).

<a name="restrictions"/>

### 3.2.3 Ограничения
1. Приложение реализовано для ОС Android;
2. Язык, на котором реализована программа, - Kotlin.
