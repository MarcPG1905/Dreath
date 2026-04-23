package common.channels

import protocol.Packet
import protocol.channel.Channel
import protocol.session.Session

/** Voice chat transmission — unreliable and unordered. */
object VoiceChatChannel : Channel("vc", encrypted = true) {
    override fun handle(source: Session, packet: Packet) {
        TODO("Not yet implemented")
    }
}
