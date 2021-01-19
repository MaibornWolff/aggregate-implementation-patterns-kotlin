package domain.oop.es.customer

import domain.shared.command.ChangeCustomerEmailAddress
import domain.shared.command.ConfirmCustomerEmailAddress
import domain.shared.command.RegisterCustomer
import domain.shared.event.CustomerEmailAddressChanged
import domain.shared.event.CustomerEmailAddressConfirmed
import domain.shared.event.CustomerRegistered
import domain.shared.event.Event
import domain.shared.value.EmailAddress
import domain.shared.value.Hash
import domain.shared.value.PersonName
import java.util.*

class Customer4 private constructor() {
    private var emailAddress: EmailAddress? = null
    private var confirmationHash: Hash? = null
    private var isEmailAddressConfirmed = false
    private var name: PersonName? = null
    private val recordedEvents: MutableList<Event>

    fun confirmEmailAddress(command: ConfirmCustomerEmailAddress) {

        // TODO

    }

    fun changeEmailAddress(command: ChangeCustomerEmailAddress) {

        // TODO

    }

    fun getRecordedEvents(): List<Event> {
        return recordedEvents
    }

    private fun recordThat(event: Event) {
        recordedEvents.add(event)
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
        fun register(command: RegisterCustomer): Customer4 {
            val customer = Customer4()

            // TODO

            return customer
        }

        fun reconstitute(events: List<Event>): Customer4 {
            val customer = Customer4()
            customer.apply(events)
            return customer
        }
    }

    init {
        recordedEvents = ArrayList()
    }
}