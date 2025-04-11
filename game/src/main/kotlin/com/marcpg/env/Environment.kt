package com.marcpg.env

import com.marcpg.env.client.ClientEnv
import com.marcpg.env.server.ServerEnv

enum class Environment(val instances: () -> List<GameEnv>) {
    CLIENT({ listOf(ClientEnv) }),
    SERVER({ listOf(ServerEnv) }),
    MODULE_TESTING({ listOf(ModuleTestingEnv) }),
    UNKNOWN({ listOf() }),
}