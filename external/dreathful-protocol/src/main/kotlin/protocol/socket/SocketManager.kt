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
     *
     * @param server If this socket manager should be opened in the context of a server, meaning
     * @param port The port to run the main socket on.
     *             If set to 0, this will be randomly assigned by the OS, which is recommended for clients.
     */
    fun open(server: Boolean, port: Int = 0) {
        require(!::current.isInitialized) { "SocketManager has already been opened" }
        current = selectSocket(server, port)

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

    private fun selectSocket(server: Boolean, port: Int): Socket = JavaNioSocket(server, port)
}
