package dev.alpas

import dev.alpas.console.Command
import kotlin.reflect.KClass

interface Kernel {
    fun accepts(it: KClass<out ServiceProvider>) = true
    fun boot(app: Application) {}
    fun serviceProviders(app: Application): Iterable<KClass<out ServiceProvider>> = emptyList()
    fun stop(app: Application) {}
    fun commands(app: Application) = emptyList<Command>()
}
