package common.channels

import protocol.Packet
import protocol.channel.Channel
import protocol.session.Session

/** General gameplay-related packets — reliable and ordered. */
object GameplayChannel : Channel("gameplay", reliable = true, ordered = true) {
    override fun handle(source: Session, packet: Packet) {
    }
}
