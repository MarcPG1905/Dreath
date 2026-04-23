package com.marcpg.dreath.event.events

import com.marcpg.dreath.event.Event
import com.marcpg.dreath.util.Ticking

/**
 * Called before a server tick is processed.
 */
class ServerPreTickEvent(val tick: Ticking.Tick) : Event()

/**
 * Called before a server tick is processed.
 */
class ServerPostTickEvent(val tick: Ticking.Tick) : Event()
