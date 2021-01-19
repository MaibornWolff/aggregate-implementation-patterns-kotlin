package domain.shared.event

import domain.shared.value.EmailAddress
import domain.shared.value.Hash
import domain.shared.value.ID
import domain.shared.value.PersonName

class CustomerRegistered private constructor(val customerID: ID, val emailAddress: EmailAddress, val confirmationHash: Hash, val name: PersonName) : Event {

    companion object {
        fun build(
                id: ID,
                emailAddress: EmailAddress,
                confirmationHash: Hash,
                name: PersonName
        ): CustomerRegistered {
            return CustomerRegistered(id, emailAddress, confirmationHash, name)
        }
    }

}