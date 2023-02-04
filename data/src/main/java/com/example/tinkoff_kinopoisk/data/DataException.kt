package com.example.tinkoff_kinopoisk.data

/**
 * Class for custom data exceptions
 */
sealed class DataException(message: String) : Exception(message){
    class InternetException : DataException("Что-то пошло не так. Пожалуйста, проверьте соедиение с сетью Интернет.")

    class Response400 : DataException("Ошибка запроса.")
    class Response401 : DataException("Ошибка авторизации.")
    class Response404 : DataException("Не найдено.")
    class Response429 : DataException("Слишком много запросов.")
    class Response500 : DataException("Сервер сломан.")
    class UnknownServerError(code: Int) : DataException(code.toString())

    internal companion object {
        // converts server exception code into custom data exception
        fun responseCodeToException(code: Int) = when (code) {
            400 -> Response400()
            401 -> Response401()
            404 -> Response404()
            429 -> Response429()
            500 -> Response500()
            else -> UnknownServerError(code)
        }
    }
}

