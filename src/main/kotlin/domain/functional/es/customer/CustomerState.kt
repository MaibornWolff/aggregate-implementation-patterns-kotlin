package domain.functional.es.customer

import domain.shared.event.CustomerEmailAddressChanged
import domain.shared.event.CustomerEmailAddressConfirmed
import domain.shared.event.CustomerRegistered
import domain.shared.event.Event
import domain.shared.value.EmailAddress
import domain.shared.value.Hash
import domain.shared.value.PersonName

class CustomerState private constructor() {
    var emailAddress: EmailAddress? = null
    var confirmationHash: Hash? = null
    var name: PersonName? = null
    var isEmailAddressConfirmed = false

    fun apply(events: List<Event>) {
        for (event in events) {
            if (event.javaClass == CustomerRegistered::class.java) {
                // TODO
                continue
            }
            if (event.javaClass == CustomerEmailAddressConfirmed::class.java) {
                // TODO
                continue
            }
            if (event.javaClass == CustomerEmailAddressChanged::class.java) {
                // TODO
            }
        }
    }

    companion object {
        fun reconstitute(events: List<Event>): CustomerState {
            val customer = CustomerState()
            customer.apply(events)
            return customer
        }
    }
}