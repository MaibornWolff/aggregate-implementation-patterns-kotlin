package domain.shared.value

class PersonName private constructor(val givenName: String, val familyName: String) {

    companion object {
        fun build(givenName: String, familyName: String): PersonName {
            return PersonName(givenName, familyName)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PersonName

        if (givenName != other.givenName) return false
        if (familyName != other.familyName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = givenName.hashCode()
        result = 31 * result + familyName.hashCode()
        return result
    }

}