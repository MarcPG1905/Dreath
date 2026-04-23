package com.marcpg.dreath.util

object Constants {
    val VERSION = DreathVersion(
        verId = 0u,
        moduleName = "Dreath",
        ver = Triple(0u, 1u, 0u),
        verType = ReleaseType.ALPHA,
    )

    val PROTOCOL_VERSION = DreathVersion(
        verId = 0u,
        moduleName = "Peg-Protocol",
        ver = Triple(0u, 1u, 0u),
        verType = ReleaseType.ALPHA,
    )

    const val TPS = 64

    // TODO: Make these all configurable.
    const val MAX_PAYLOAD_BYTES = 1024
    const val TIMEOUT = 5000
    const val PORT = 42069
}
