package domain.functional.es.customer

import domain.shared.command.ChangeCustomerEmailAddress
import domain.shared.command.ConfirmCustomerEmailAddress
import domain.shared.command.RegisterCustomer
import domain.shared.event.CustomerRegistered
import domain.shared.event.CustomerRegistered.Companion.build
import domain.shared.event.Event

object Customer7 {

    fun register(command: RegisterCustomer): CustomerRegistered {
        return build(
                command.customerID,
                command.emailAddress,
                command.confirmationHash,
                command.name
        )
    }

    fun confirmEmailAddress(current: CustomerState, command: ConfirmCustomerEmailAddress): List<Event> {

        // TODO

        return emptyList() // TODO
    }

    fun changeEmailAddress(current: CustomerState, command: ChangeCustomerEmailAddress): List<Event> {
        // TODO

        return emptyList() // TODO
    }
}