package protocol.channel

import com.marcpg.dreath.util.CallOriginCheck

interface ChannelManager {
    companion object {
        lateinit var INSTANCE: ChannelManager

        fun initialize(instance: ChannelManager) {
            CallOriginCheck.require(setOf("common.Game")) // TODO: Actually initialize it here.
            require(!::INSTANCE.isInitialized) { "ChannelManager has already been initialized" }

            INSTANCE = instance
        }
    }

    operator fun get(id: UByte): Channel?
    fun add(channel: Channel): UByte?
    fun getId(channel: Channel): UByte?
}
