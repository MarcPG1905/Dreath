package common.channels

import protocol.Packet
import protocol.channel.Channel
import protocol.channel.features.CompressionLevel
import protocol.session.Session

/** Social functions like chat messages, titles, etc. — reliable and ordered. */
object SocialChannel : Channel("social", reliable = true, ordered = true, compression = CompressionLevel.NORMAL, encrypted = true) {
    override fun handle(source: Session, packet: Packet) {
    }
}
