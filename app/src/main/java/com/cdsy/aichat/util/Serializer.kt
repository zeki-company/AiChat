package com.cdsy.aichat.util

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * @author Zeki
 * @version 1.0
 * @date 2019/10/11 18:10
 */

/**
 * json Serializer
 */
val globalMoshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
