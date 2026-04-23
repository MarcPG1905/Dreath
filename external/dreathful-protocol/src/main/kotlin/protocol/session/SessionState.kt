package protocol.session

/**
 * Represents the states a session can be in.
 *
 * In addition to the four states of a connection (handshake, connected, disconnecting, disconnected),
 * this also includes [SELF] for [LocalSession] and [DUMMY] for [DummySession].
 */
enum class SessionState {
    /** Set between the first handshake packet and the final connection-confirming packet. */
    HANDSHAKING,

    /** Set while the connection is actively running. */
    CONNECTED,

    /** Set between the first disconnection packet and the final disconnect-confirming packet. */
    DISCONNECTING,

    /** Set after the connection has already been disconnected. */
    DISCONNECTED,

    /** Reserved for [LocalSession]. */
    SELF,

    /** Reserved for [DummySession]. */
    DUMMY,
}
