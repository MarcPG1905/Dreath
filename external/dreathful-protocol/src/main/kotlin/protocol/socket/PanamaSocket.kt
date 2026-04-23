package protocol.socket

import java.lang.foreign.*
import java.lang.invoke.MethodHandle
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer

internal class PanamaSocket(server: Boolean, port: Int) : Socket(server, port) {
    val batchSize = 32L

    private val recvmmsg: MethodHandle
    private val sendmmsg: MethodHandle

    private var socketFd = -1

    init {
        val linker = Linker.nativeLinker()
        val lookup = linker.defaultLookup()

        recvmmsg = linker.downcallHandle(
            lookup.find("recvmmsg").get(),
            FunctionDescriptor.of(
                ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, // int sockfd
                ValueLayout.ADDRESS, // struct mmsghdr msgvec[n]
                ValueLayout.JAVA_INT, // unsigned int n
                ValueLayout.JAVA_INT, // int flags
                ValueLayout.ADDRESS, // struct timespec *timeout
            )
        )

        sendmmsg = linker.downcallHandle(
            lookup.find("sendmmsg").get(),
            FunctionDescriptor.of(
                ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, // int sockfd
                ValueLayout.ADDRESS, // struct mmsghdr msgvec[n]
                ValueLayout.JAVA_INT, // unsigned int n
                ValueLayout.JAVA_INT, // int flags
            )
        )

//        socketFd = createSocket(port)
    }

    override fun listenOnVThread() {
        Arena.ofConfined().use { arena ->
            val iovecLayout = MemoryLayout.structLayout(
                ValueLayout.ADDRESS.withName("iov_base"),
                ValueLayout.JAVA_LONG.withName("iov_len"),
            )

            val msghdrLayout = MemoryLayout.structLayout(
                ValueLayout.ADDRESS,
                ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS,
                ValueLayout.JAVA_LONG,
                ValueLayout.ADDRESS,
                ValueLayout.JAVA_LONG,
                ValueLayout.JAVA_INT,
            )

            val mmsghdrLayout = MemoryLayout.structLayout(
                msghdrLayout,
                ValueLayout.JAVA_INT,
            )

            val mmsghdrArray = arena.allocate(mmsghdrLayout, batchSize)
            val iovecArray = arena.allocate(iovecLayout, batchSize)

            val buffers = Array(batchSize.toInt()) {
                arena.allocate(2048)
            }

            for (i in 0 until batchSize) {
                val iovec = iovecArray.asSlice(i * iovecLayout.byteSize(), iovecLayout.byteSize())
                iovec.set(ValueLayout.ADDRESS, 0, buffers[i.toInt()])
                iovec.set(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS.byteSize(), 2048)

                val msg = mmsghdrArray.asSlice(i * mmsghdrLayout.byteSize(), mmsghdrLayout.byteSize())

                val msgHdr = msg.asSlice(0, msghdrLayout.byteSize())
                msgHdr.set(ValueLayout.ADDRESS, 0, MemorySegment.NULL)
                msgHdr.set(ValueLayout.JAVA_INT, ValueLayout.ADDRESS.byteSize(), 0)

                msgHdr.set(ValueLayout.ADDRESS, ValueLayout.ADDRESS.byteSize() + ValueLayout.JAVA_INT.byteSize(), iovec)
                msgHdr.set(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS.byteSize() + ValueLayout.JAVA_INT.byteSize() + ValueLayout.ADDRESS.byteSize(), 1L)
            }

            while (running) {
                val received = recvmmsg(
                    socketFd,
                    mmsghdrArray,
                    batchSize,
                    0,
                    MemorySegment.NULL
                ) as Int

                if (received <= 0) continue

                for (i in 0 until received) {
                    val msg = mmsghdrArray.asSlice(i * mmsghdrLayout.byteSize(), mmsghdrLayout.byteSize())
                    val length = msg.get(ValueLayout.JAVA_INT, msghdrLayout.byteSize())
                    val buffer = buffers[i].asSlice(0, length.toLong()).asByteBuffer()

                    val dummyAddress = InetSocketAddress("0.0.0.0", 0)
                    handlePacket(dummyAddress, buffer)
                }
            }
        }
    }

    override fun closeChannel() {
        TODO("Not yet implemented")
    }

    override fun sendRawToAddress(address: SocketAddress, rawData: ByteBuffer) {
        TODO("Not yet implemented")
    }
}
