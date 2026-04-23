package protocol.header

/**
 * A packet type attached to packages to specify how they are handled internally.
 * @author MarcPG
 * @since 0.1.0
 */
enum class TypeId(val id: UByte) {
    /** Connection initialization. */
    CONNECT(0u),

    /** Disconnection initialization. */
    DISCONNECT(1u),

    /** Keepalive pings. */
    PING(2u),

    /** Data-carrying packets. */
    DATA(3u);

    companion object {
        private val map = TypeId.entries.associateBy { it.id }

        /**
         * Gets the [TypeId] with the specified [UByte] ID.
         * @return The type or `null` if no type uses the specified ID.
         */
        fun ofId(id: UByte): TypeId? = map[id]
    }
}
