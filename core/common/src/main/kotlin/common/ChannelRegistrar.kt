package common

import com.marcpg.dreath.util.firstNotInOrNull
import com.marcpg.dreath.util.registry.Registrar
import com.marcpg.dreath.util.registry.RegistrarType
import protocol.channel.Channel
import protocol.channel.ChannelManager
import kotlin.math.pow

object ChannelRegistrar : Registrar<Channel>, ChannelManager {
    const val CHANNEL_BITS = 4

    override val type: RegistrarType<Channel> = RegistrarType.PROTOCOL_CHANNELS

    private val loaded = mutableListOf<Channel>()
    private val idsToChannels = mutableMapOf<UByte, Channel>()

    override fun get(id: UByte): Channel? = idsToChannels[id]

    override fun add(channel: Channel): UByte {
        val channelLimit = 2.0.pow(CHANNEL_BITS).toUInt()

        // Start at 1 / remove 1, as 0 is reserved for internal protocol stuff.
        val availableId = (1u..<channelLimit).firstNotInOrNull(idsToChannels.map { it.key.toUInt() })?.toUByte()
        check(availableId != null) { "Max. amount of channels (${channelLimit - 1u}) has been reached" }

        loaded += channel
        idsToChannels[availableId] = channel

        return availableId
    }

    override fun getId(channel: Channel): UByte? {
        for (entry in idsToChannels) {
            if (entry.value == channel)
                return entry.key
        }
        return null
    }

    override fun register(instance: Channel) { add(instance) }

    override fun loaded(): List<Channel> = loaded.toList()
}
