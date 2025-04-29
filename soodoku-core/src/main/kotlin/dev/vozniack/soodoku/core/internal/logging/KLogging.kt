package dev.vozniack.soodoku.core.internal.logging

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.reflect.full.companionObject

abstract class KLogging {
    val logger = KotlinLogging.logger(unwrapCompanionClass(this::class.java).name)
}

fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> = ofClass.enclosingClass?.takeIf {
    ofClass.enclosingClass.kotlin.companionObject?.java == ofClass
} ?: ofClass
