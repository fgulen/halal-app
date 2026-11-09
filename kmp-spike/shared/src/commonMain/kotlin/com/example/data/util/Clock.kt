package com.example.data.util

import kotlin.time.ExperimentalTime

/** Şu anın epoch milisaniyesi. JVM'de System.currentTimeMillis ile aynı değeri verir. */
@OptIn(ExperimentalTime::class)
fun nowEpochMillis(): Long = kotlin.time.Clock.System.now().toEpochMilliseconds()
