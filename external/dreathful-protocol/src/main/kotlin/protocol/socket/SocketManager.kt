package protocol.socket

import protocol.socket.SocketManager.current

/**
 * Manages the currently running socket and handles the implementation selection.
 *
 * This exists to not expose the implementation selection to public API and ensure only one socket will ever exist at once.
 *
 * @author MarcPG
 * @since 0.1.0
 */
object SocketManager {
    lateinit var current: Socket
        private set

    /**
     * Selects the best socket implementation on this system and calls its [Socket.open] method.
     *
     * Cannot be called twice.
     */
    fun open(server: Boolean) {
        require(!::current.isInitialized) { "SocketManager has already been opened" }
        current = selectSocket(server)

        current.open()
    }

    /**
     * Closes the [current] socket via [Socket.close].
     *
     * Cannot be called twice.
     */
    fun close() {
        require(::current.isInitialized) { "SocketManager is not yet opened" }

        current.close()
    }

    private fun selectSocket(server: Boolean): Socket = JavaNioSocket(server, 42069) // TODO: Replace with configured value instead of always 42069.
}
