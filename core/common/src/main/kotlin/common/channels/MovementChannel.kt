package common.channels

import protocol.Packet
import protocol.channel.Channel
import protocol.session.Session

/** Movement-related packets — reliable but unordered. */
object MovementChannel : Channel("movement", reliable = true) {
    override fun handle(source: Session, packet: Packet) {
        TODO("Not yet implemented")
    }
}
