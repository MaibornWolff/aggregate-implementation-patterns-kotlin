package domain.shared.event

import domain.shared.value.ID
import domain.shared.value.PersonName

class CustomerNameChanged private constructor(val customerID: ID, val name: PersonName) : Event {

    companion object {
        fun build(customerID: ID, name: PersonName): CustomerNameChanged {
            return CustomerNameChanged(customerID, name)
        }
    }

}