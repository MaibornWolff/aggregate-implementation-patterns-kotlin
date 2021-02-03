package domain.oop.es.customer

import domain.shared.command.ChangeCustomerEmailAddress
import domain.shared.command.ConfirmCustomerEmailAddress
import domain.shared.command.RegisterCustomer
import domain.shared.event.CustomerEmailAddressChanged
import domain.shared.event.CustomerEmailAddressConfirmed
import domain.shared.event.CustomerRegistered
import domain.shared.event.CustomerRegistered.Companion.build
import domain.shared.event.Event
import domain.shared.value.EmailAddress
import domain.shared.value.Hash
import domain.shared.value.PersonName

class Customer3 private constructor() {
    private var emailAddress: EmailAddress? = null
    private var confirmationHash: Hash? = null
    private var isEmailAddressConfirmed = false
    private var name: PersonName? = null

    fun confirmEmailAddress(command: ConfirmCustomerEmailAddress): List<Event> {

        // TODO

        return emptyList() // TODO
    }

    fun changeEmailAddress(command: ChangeCustomerEmailAddress): List<Event> {

        // TODO

        return emptyList() // TODO
    }

    fun apply(events: List<Event>) {
        for (event in events) {
            apply(event)
        }
    }

    fun apply(event: Event) {
        when (event) {
            is CustomerRegistered -> {
                // TODO
            }
            is CustomerEmailAddressConfirmed -> {
                // TODO
            }
            is CustomerEmailAddressChanged -> {
                // TODO
            }
        }
    }

    companion object {
        fun register(command: RegisterCustomer): CustomerRegistered? {
            return null // TODO
        }

        fun reconstitute(events: List<Event>): Customer3 {
            val customer = Customer3()
            customer.apply(events)
            return customer
        }
    }
}