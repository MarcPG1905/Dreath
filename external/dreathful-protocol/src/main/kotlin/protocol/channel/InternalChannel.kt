package protocol.channel

import com.marcpg.dreath.util.toByteArray
import com.marcpg.dreath.util.toUInt
import protocol.Packet
import protocol.header.TypeId
import protocol.session.*
import protocol.socket.SocketManager
import protocol.util.createPacket
import protocol.util.createPing
import java.net.SocketAddress

internal object InternalChannel : Channel("internal", reliable = true, ordered = true, encrypted = true) {
    override fun handle(source: Session, packet: Packet) {
        SocketManager.current.log.fine("Received packet from $source, containing: $packet")

        when (packet.header.type) {
            TypeId.CONNECT -> {
                SessionManager.localSession = LocalSession(packet.data!!.toUInt())

                source.state = SessionState.CONNECTED
                SocketManager.current.log.success("Connected to server ${source.address}.")

                SocketManager.current.send(source, createPing(source, true))
            }
            TypeId.PING -> {
                source.touch()

                if (source.state == SessionState.HANDSHAKING)
                    source.state = SessionState.CONNECTED

                if (!packet.header.variant)
                    SocketManager.current.send(source, createPing(source, true))
            }
            else -> {}
        }
    }

    override fun handleSessionless(sourceAddress: SocketAddress, packet: Packet) {
        SocketManager.current.log.fine("Received sessionless packet from $sourceAddress, containing: $packet")

        // type = CONNECT & variant = 0
        if (packet.header.type == TypeId.CONNECT && !packet.header.variant) {
            val clientSession = SessionManager.getOrAddNormal(sourceAddress)

            // Send back confirmation packet with assigned ID.
            SocketManager.current.sendToAddress(sourceAddress, createPacket(
                receiver = clientSession,
                type = TypeId.CONNECT,
                channel = InternalChannel,
                variant = true, // Server->Client Variant (Confirmation)
                data = clientSession.id.toByteArray()
            ))

            SocketManager.current.log.info("Confirmed connection request of $sourceAddress.")
        }
    }

    fun connectToServer(serverAddress: SocketAddress) {
        SessionManager.forceAdd(NormalSession(serverAddress, 0u))

        SocketManager.current.sendToAddress(serverAddress, createPacket(
            receiver = DummySession(serverAddress),
            type = TypeId.CONNECT,
            channel = InternalChannel,
            variant = false, // Client->Server Variant (Initial Connect)
            // TODO: Attach login data and some other stuff like compression format, encryption keys, etc.
        ))
    }
}
