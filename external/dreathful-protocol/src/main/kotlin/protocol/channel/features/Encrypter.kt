package protocol.channel.features

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class Encrypter(private val key: SecretKey) {
    companion object {
        private const val NONCE_SIZE = 12
    }

    // Not sure if having them separate has any benefit, but yeah.
    private val encrypter: Cipher = Cipher.getInstance("ChaCha20-Poly1305")
    private val decrypter: Cipher = Cipher.getInstance("ChaCha20-Poly1305")

    /**
     * Encrypts this piece of data using ChaCha20-Poly1305 with a nonce and AAD.
     * @param data The unencrypted input data.
     * @param nonce The nonce to use for the encryption.
     * @param aad Optionally, additional data for the encryption.
     * @return The encrypted data.
     */
    fun encrypt(data: ByteArray, nonce: ByteArray, aad: ByteArray? = null): ByteArray {
        require(nonce.size == NONCE_SIZE) { "Nonce must be $NONCE_SIZE bytes long" }

        encrypter.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(nonce))
        if (aad != null)
            encrypter.updateAAD(aad)

        return encrypter.doFinal(data)
    }

    /**
     * Decrypts this piece of data using ChaCha20-Poly1305 with a nonce and AAD.
     * @param data The encrypted input data.
     * @param nonce The nonce to use for the decryption.
     * @param aad Optionally, additional data for the decryption.
     * @return The decrypted data.
     */
    fun decrypt(data: ByteArray, nonce: ByteArray, aad: ByteArray? = null): ByteArray {
        require(nonce.size == NONCE_SIZE) { "Nonce must be $NONCE_SIZE bytes long" }

        decrypter.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(nonce))
        if (aad != null)
            decrypter.updateAAD(aad)

        return decrypter.doFinal(data)
    }
}
