package common.channels

import protocol.Packet
import protocol.channel.Channel
import protocol.session.Session

/** Mainly keepalive pings, but also some other things like ping-pong testing. */
object HeartbeatChannel : Channel("heartbeat") {
    override fun handle(source: Session, packet: Packet) {
    }
}
