package protocol.util

private val emptyByteArray = ByteArray(0)

/**
 * Returns a completely empty [ByteArray] with a size of 0.
 *
 * This does not create a new instance on each call and instead re-uses the same instance.
 */
fun emptyByteArray() = emptyByteArray
