package domain.functional.es.customer

import domain.shared.command.ChangeCustomerEmailAddress
import domain.shared.command.ConfirmCustomerEmailAddress
import domain.shared.command.RegisterCustomer
import domain.shared.event.*
import domain.shared.event.CustomerRegistered.Companion.build
import domain.shared.value.EmailAddress
import domain.shared.value.Hash

object Customer5 {

    fun register(command: RegisterCustomer): CustomerRegistered {
        return build(
                command.customerID,
                command.emailAddress,
                command.confirmationHash,
                command.name
        )
    }

    fun confirmEmailAddress(eventStream: List<Event?>, command: ConfirmCustomerEmailAddress): List<Event> {
        var isEmailAddressConfirmed = false
        var confirmationHash: Hash? = null
        for (event in eventStream) {
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

        // TODO

        return emptyList() // TODO
    }

    fun changeEmailAddress(eventStream: List<Event>, command: ChangeCustomerEmailAddress): List<Event> {
        var emailAddress: EmailAddress? = null
        for (event in eventStream) {
            when (event) {
                is CustomerRegistered -> {
                    // TODO
                }
                is CustomerEmailAddressChanged -> {
                    // TODO
                }
            }
        }

        // TODO

        return emptyList()
    }
}