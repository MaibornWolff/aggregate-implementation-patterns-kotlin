package domain.oop.es.customer

import domain.shared.command.ChangeCustomerEmailAddress
import domain.shared.command.ConfirmCustomerEmailAddress
import domain.shared.command.RegisterCustomer
import domain.shared.event.*
import domain.shared.event.CustomerEmailAddressChanged.Companion.build
import domain.shared.event.CustomerRegistered.Companion.build
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
        if (confirmationHash != command.confirmationHash) {
            recordThat(
                    CustomerEmailAddressConfirmationFailed.build(command.customerID)
            )
            return
        }
        if (!isEmailAddressConfirmed) {
            recordThat(
                    CustomerEmailAddressConfirmed.build(command.customerID)
            )
        }
    }

    fun changeEmailAddress(command: ChangeCustomerEmailAddress) {
        if (emailAddress != command.emailAddress) {
            recordThat(
                    build(command.customerID, command.emailAddress, command.confirmationHash)
            )
        }
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
                emailAddress = event.emailAddress
                confirmationHash = event.confirmationHash
                name = event.name
            }
            is CustomerEmailAddressConfirmed -> {
                isEmailAddressConfirmed = true
            }
            is CustomerEmailAddressChanged -> {
                emailAddress = event.emailAddress
                confirmationHash = event.confirmationHash
                isEmailAddressConfirmed = false
            }
        }
    }

    companion object {
        fun register(command: RegisterCustomer): Customer4 {
            val customer = Customer4()
            customer.recordThat(
                    build(command.customerID, command.emailAddress, command.confirmationHash, command.name)
            )
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