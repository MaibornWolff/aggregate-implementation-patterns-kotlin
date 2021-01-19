package domain.shared.value

class EmailAddress private constructor(val value: String) {

    companion object {
        fun build(emailAddress: String): EmailAddress {
            return EmailAddress(emailAddress)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmailAddress

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

}