package com.github.pratclot.syntheticstoviewbinding.util

@Suppress("UNCHECKED_CAST")
fun <T> Iterable<Any>.forEachAs(block: (T) -> Unit) = forEach { block(it as T) }

@Suppress("UNCHECKED_CAST")
fun <T> Iterable<Any>.mapAs(block: (T) -> Unit) = map { block(it as T) }

@Suppress("UNCHECKED_CAST")
fun <T, R> Iterable<Any>.mapToSetAs(block: (T) -> R): Set<R> =
    mapTo(mutableSetOf()) { block(it as T) }

fun Iterable<Boolean>.allTrue() = all { it }

fun allTrue(vararg conditions: Boolean) = conditions.toList().allTrue()

fun String.toCamelCase() = split('_').joinToString("", transform = String::capitalize)

fun <R, S> with(one: R, two: S, block: R.(S) -> Unit) = block(one, two)
