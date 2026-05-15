package protocol.data

import com.marcpg.dreath.util.CallOriginCheck

interface PacketDataManager {
    companion object {
        lateinit var INSTANCE: PacketDataManager

        fun initialize(instance: PacketDataManager) {
            CallOriginCheck.require(setOf("common.Game"))
            require(!::INSTANCE.isInitialized) { "PacketDataManager has already been initialized" }

            INSTANCE = instance
        }
    }

    fun getUnknown(id: UShort): PacketDataCompanion<*>?

    fun add(channel: PacketDataCompanion<*>): UShort?
    fun getId(channel: PacketDataCompanion<*>): UShort?

    fun getOrAdd(channel: PacketDataCompanion<*>): UShort = getId(channel) ?: add(channel)!!
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : PacketData<T>> PacketDataManager.get(id: UShort): PacketDataCompanion<T>? = getUnknown(id) as? PacketDataCompanion<T>?
