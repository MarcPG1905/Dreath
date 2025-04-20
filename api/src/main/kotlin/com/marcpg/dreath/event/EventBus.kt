package com.marcpg.dreath.event

/**
 * The event bus to use to register and post events.
 * Usually, a mod will only register events, and only custom features will require posting events.
 * @author MarcPG
 * @since 0.1.0
 */
object EventBus {
    @PublishedApi
    internal val handlers = mutableMapOf<Class<out Event>, MutableList<(Event) -> Unit>>()

    /**
     * Registers a new event handler/listener to this event bus,
     * which will then be called when the event is posted.
     */
    inline fun <reified T : Event> register(noinline handler: (T) -> Unit) {
        val clazz = T::class.java
        val list = handlers.getOrPut(clazz) { mutableListOf() }
        @Suppress("UNCHECKED_CAST")
        list.add(handler as (Event) -> Unit)
    }

    /**
     * Posts an event to this event bus and calls all its registered handlers/listeners.
     */
    fun post(event: Event) {
        handlers[event.javaClass]?.forEach { it(event) }
    }
}
