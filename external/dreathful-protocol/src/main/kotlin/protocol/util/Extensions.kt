package protocol.util

import protocol.Packet
import protocol.data.PacketData
import protocol.session.Session
import protocol.socket.SocketManager
import protocol.util.growable.GrowableByteBufferPool
import java.nio.ByteBuffer

/** @see protocol.socket.Socket.send */
fun Session.send(packet: Packet<*>) = SocketManager.current.send(this, packet)

/** @see protocol.socket.Socket.sendRaw */
fun Session.sendRaw(rawData: ByteBuffer) = SocketManager.current.sendRaw(this, rawData)

fun <T : PacketData<T>> T.encodeToByteArray(): ByteArray = GrowableByteBufferPool.Default_MTU.asByteArray { encode(it) }

fun ByteBuffer.getUByte(): UByte = get().toUByte()
fun ByteBuffer.getUShort(): UShort = getShort().toUShort()
fun ByteBuffer.getUInt(): UInt = getInt().toUInt()
fun ByteBuffer.getULong(): ULong = getLong().toULong()

fun Byte.toBinary(bits: Int): String = toString(2).padStart(bits, '0')
fun Short.toBinary(bits: Int): String = toString(2).padStart(bits, '0')
fun Int.toBinary(bits: Int): String = toString(2).padStart(bits, '0')
fun Long.toBinary(bits: Int): String = toString(2).padStart(bits, '0')

fun UByte.toBinary(bits: Int): String = toString(2).padStart(bits, '0')
fun UShort.toBinary(bits: Int): String = toString(2).padStart(bits, '0')
fun UInt.toBinary(bits: Int): String = toString(2).padStart(bits, '0')
fun ULong.toBinary(bits: Int): String = toString(2).padStart(bits, '0')
