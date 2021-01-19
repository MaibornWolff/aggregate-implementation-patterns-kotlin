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

class Customer3 private constructor() {
    private var emailAddress: EmailAddress? = null
    private var confirmationHash: Hash? = null
    private var isEmailAddressConfirmed = false
    private var name: PersonName? = null

    fun confirmEmailAddress(command: ConfirmCustomerEmailAddress): List<Event> {
        if (confirmationHash != command.confirmationHash) {
            return listOf(
                    CustomerEmailAddressConfirmationFailed.build(command.customerID)
            )
        }
        return if (isEmailAddressConfirmed) {
            listOf()
        } else listOf(
                CustomerEmailAddressConfirmed.build(command.customerID)
        )
    }

    fun changeEmailAddress(command: ChangeCustomerEmailAddress): List<Event> {
        return if (emailAddress == command.emailAddress) {
            listOf()
        } else listOf(
                build(command.customerID, command.emailAddress, command.confirmationHash)
        )
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
        fun register(command: RegisterCustomer): CustomerRegistered {
            return build(
                    command.customerID,
                    command.emailAddress,
                    command.confirmationHash,
                    command.name
            )
        }

        fun reconstitute(events: List<Event>): Customer3 {
            val customer = Customer3()
            customer.apply(events)
            return customer
        }
    }
}