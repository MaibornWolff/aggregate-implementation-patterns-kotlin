package domain.shared.event

import domain.shared.value.ID

class CustomerEmailAddressConfirmationFailed private constructor(val customerID: ID) : Event {

    companion object {
        fun build(customerID: ID): CustomerEmailAddressConfirmationFailed {
            return CustomerEmailAddressConfirmationFailed(customerID)
        }
    }

}