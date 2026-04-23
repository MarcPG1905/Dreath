package protocol.channel

import protocol.Packet
import protocol.session.Session
import java.net.SocketAddress

abstract class Channel(
    val name: String,

    val reliable: Boolean = false,
    val ordered: Boolean = false,
    val encrypted: Boolean = false,
    val compression: Boolean = false,
) {
    abstract fun handle(source: Session, packet: Packet)
    open fun handleSessionless(sourceAddress: SocketAddress, packet: Packet) {
        error("Unknown packet origin session")
    }
}
