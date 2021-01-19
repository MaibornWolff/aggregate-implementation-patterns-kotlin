package domain.shared.command

import domain.shared.value.Hash
import domain.shared.value.ID

class ConfirmCustomerEmailAddress private constructor(customerID: String, confirmationHash: String) {

    val customerID: ID = ID.build(customerID)
    val confirmationHash: Hash = Hash.build(confirmationHash)

    companion object {
        fun build(customerID: String, confirmationHash: String): ConfirmCustomerEmailAddress {
            return ConfirmCustomerEmailAddress(customerID, confirmationHash)
        }
    }

}