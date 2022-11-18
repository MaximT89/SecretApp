package com.second.world.secretapp.core.constants

object Constants {

    /**
     * Базовый URL для запросов на сервер ( используется в клиенте @MainRetrofitClient)
     */
    const val BASE_URL = "https://dev.ah-info.ru:8443/"

    /**
     * Урл сформированный каждым объектом в MainFragment ( используется в клиенте @ConnRetrofitClient)
     *
     * protocol :// ip : port / action - данные берем из модели [ResponseMainScreen]
     */
    var CONNECTION_BASE_URL = "http://156.67.53.74:8083/"
//    var CONNECTION_BASE_URL = ""

    const val ADMIN_PIN = "11998899"
}