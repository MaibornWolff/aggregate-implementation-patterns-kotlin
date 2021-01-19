package domain.shared.command

import domain.shared.value.EmailAddress
import domain.shared.value.Hash
import domain.shared.value.Hash.Companion.generate
import domain.shared.value.ID

class ChangeCustomerEmailAddress private constructor(customerID: String, emailAddress: String) {

    val customerID: ID = ID.build(customerID)
    val emailAddress: EmailAddress = EmailAddress.build(emailAddress)
    val confirmationHash: Hash = generate()

    companion object {
        fun build(customerID: String, emailAddress: String): ChangeCustomerEmailAddress {
            return ChangeCustomerEmailAddress(customerID, emailAddress)
        }
    }

}