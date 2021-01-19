package domain.shared.event

import domain.shared.value.ID

class CustomerEmailAddressConfirmed private constructor(val customerID: ID) : Event {

    companion object {
        fun build(customerID: ID): CustomerEmailAddressConfirmed {
            return CustomerEmailAddressConfirmed(customerID)
        }
    }

}