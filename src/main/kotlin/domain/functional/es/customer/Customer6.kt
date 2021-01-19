package domain.functional.es.customer

import domain.functional.es.customer.CustomerState.Companion.reconstitute
import domain.shared.command.ChangeCustomerEmailAddress
import domain.shared.command.ConfirmCustomerEmailAddress
import domain.shared.command.RegisterCustomer
import domain.shared.event.CustomerEmailAddressChanged.Companion.build
import domain.shared.event.CustomerEmailAddressConfirmationFailed
import domain.shared.event.CustomerEmailAddressConfirmed
import domain.shared.event.CustomerRegistered
import domain.shared.event.CustomerRegistered.Companion.build
import domain.shared.event.Event

object Customer6 {

    fun register(command: RegisterCustomer): CustomerRegistered {
        return build(
                command.customerID,
                command.emailAddress,
                command.confirmationHash,
                command.name
        )
    }

    fun confirmEmailAddress(eventStream: List<Event>, command: ConfirmCustomerEmailAddress): List<Event> {
        val current = reconstitute(eventStream)
        if (current.confirmationHash != command.confirmationHash) {
            return listOf(CustomerEmailAddressConfirmationFailed.build(command.customerID))
        }
        return if (current.isEmailAddressConfirmed) {
            listOf()
        } else listOf(CustomerEmailAddressConfirmed.build(command.customerID))
    }

    fun changeEmailAddress(eventStream: List<Event>, command: ChangeCustomerEmailAddress): List<Event> {
        val current = reconstitute(eventStream)
        return if (current.emailAddress == command.emailAddress) {
            listOf()
        } else listOf(build(command.customerID, command.emailAddress, command.confirmationHash))
    }
}