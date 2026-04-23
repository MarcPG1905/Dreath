package engine

import com.marcpg.dreath.event.Event
import com.marcpg.dreath.util.registry.Registrar
import com.marcpg.dreath.util.registry.RegistrarType

object EventRegistrar : Registrar<Event> {
    override val type: RegistrarType<Event> = RegistrarType.EVENTS

    private val loaded = mutableListOf<Event>()

    override fun register(instance: Event) {
        require(instance !in loaded) { "Event '${instance::class.qualifiedName}' is already registered" }

        loaded += instance
    }

    override fun loaded(): List<Event> = loaded.toList() // Only return a copy, never the actual mutable list.
}
