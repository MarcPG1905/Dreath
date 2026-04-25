package protocol

import com.marcpg.dreath.util.DreathVersion
import com.marcpg.dreath.util.ReleaseType

object Protocol {
    /** The protocol's current version. */
    val VERSION = DreathVersion(
        verId = 0u,
        moduleName = "Protocol",
        ver = Triple(0u, 1u, 0u),
        verType = ReleaseType.ALPHA,
    )
}
