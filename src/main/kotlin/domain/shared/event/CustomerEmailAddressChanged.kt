package domain.shared.event

import domain.shared.value.EmailAddress
import domain.shared.value.Hash
import domain.shared.value.ID

class CustomerEmailAddressChanged private constructor(val customerID: ID, val emailAddress: EmailAddress, val confirmationHash: Hash) : Event {

    companion object {
        fun build(customerID: ID, emailAddress: EmailAddress, confirmationHash: Hash): CustomerEmailAddressChanged {
            return CustomerEmailAddressChanged(customerID, emailAddress, confirmationHash)
        }
    }

}