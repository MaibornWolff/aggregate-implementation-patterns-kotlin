package domain.shared.value

import java.util.*

class Hash private constructor(val value: String) {

    companion object {
        fun generate(): Hash {
            return Hash(UUID.randomUUID().toString())
        }

        fun build(hash: String): Hash {
            return Hash(hash)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Hash

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

}