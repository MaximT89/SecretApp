package com.second.world.secretapp.core.bases

interface Mapper<T, R> {

    fun map(data : T) : R
}