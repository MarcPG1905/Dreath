package com.marcpg.dreath.event.events

import com.marcpg.dreath.ModInfo
import com.marcpg.dreath.event.Event

/**
 * Called after a mod is loaded and enabled/initialized.
 */
class ModEnabledEvent(val info: ModInfo) : Event()

/**
 * Called after a mod is disabled and unloaded.
 */
class ModDisabledEvent(val info: ModInfo) : Event()

/**
 * Called after a mod is reloaded.
 */
class ModReloadedEvent(val info: ModInfo) : Event()
