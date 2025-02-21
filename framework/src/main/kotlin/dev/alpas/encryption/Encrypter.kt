package dev.alpas.encryption

import dev.alpas.JsonSerializer
import dev.alpas.base64Decoded
import dev.alpas.base64DecodedBytes
import dev.alpas.base64Encoded
import dev.alpas.secureByteArray
import dev.alpas.secureRandomString
import se.simbio.encryption.Encryption

private data class EncryptedData(val iv: String, val salt: String, val value: String?)

class Encrypter(private val key: String) {
    fun encrypt(value: String): String? {
        val iv = makeIV()
        val salt = makeSalt()
        val encryptedValue = Encryption.getDefault(key, salt, iv).encryptOrNull(value)?.trim()

        return serialize(iv.base64Encoded(), salt, encryptedValue).base64Encoded()
    }

    fun decrypt(value: String): String? {
        deserialize(value.base64Decoded()).let {
            val iv = it.iv.base64DecodedBytes()
            val salt = it.salt

            return Encryption.getDefault(key, salt, iv).decryptOrNull(it.value)
        }
    }

    private fun serialize(iv: String, salt: String, value: String?): String {
        return JsonSerializer.serialize(EncryptedData(iv, salt, value))
    }

    private fun deserialize(str: String): EncryptedData {
        return JsonSerializer.deserialize(str)
    }

    private fun makeSalt() = secureRandomString()

    private fun makeIV() = secureByteArray(16)
}
