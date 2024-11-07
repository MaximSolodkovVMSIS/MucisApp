package com.example.musicapp

object NativeUtils {
    init {
        // Загружаем нативную библиотеку
        System.loadLibrary("native-lib") // Имя должно совпадать с именем библиотеки в CMakeLists.txt
    }

    // Объявление нативного метода
    @JvmStatic
    external fun formatTimeJNI(milliseconds: Int): String
}
