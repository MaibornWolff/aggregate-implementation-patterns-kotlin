package domain.functional.es.customer

import domain.functional.es.customer.CustomerState.Companion.reconstitute
import domain.shared.command.ChangeCustomerEmailAddress
import domain.shared.command.ConfirmCustomerEmailAddress
import domain.shared.command.RegisterCustomer
import domain.shared.event.CustomerRegistered
import domain.shared.event.CustomerRegistered.Companion.build
import domain.shared.event.Event

object Customer6 {

    fun register(command: RegisterCustomer): CustomerRegistered? {
        return null // TODO
    }

    fun confirmEmailAddress(eventStream: List<Event>, command: ConfirmCustomerEmailAddress): List<Event> {
        val current = reconstitute(eventStream)

        // TODO

        return emptyList() // TODO
    }

    fun changeEmailAddress(eventStream: List<Event>, command: ChangeCustomerEmailAddress): List<Event> {
        val current = reconstitute(eventStream)

        // TODO

        return emptyList() // TODO
    }
}