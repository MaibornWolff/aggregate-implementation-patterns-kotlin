package domain.shared.value

import java.util.*

class ID private constructor(val value: String) {

    companion object {
        fun generate(): ID {
            return ID(UUID.randomUUID().toString())
        }

        fun build(id: String): ID {
            return ID(id)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ID

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

}