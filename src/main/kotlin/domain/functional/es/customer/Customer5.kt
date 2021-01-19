package domain.functional.es.customer

import domain.shared.command.ChangeCustomerEmailAddress
import domain.shared.command.ConfirmCustomerEmailAddress
import domain.shared.command.RegisterCustomer
import domain.shared.event.*
import domain.shared.event.CustomerEmailAddressChanged.Companion.build
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
                    confirmationHash = event.confirmationHash
                }
                is CustomerEmailAddressConfirmed -> {
                    isEmailAddressConfirmed = true
                }
                is CustomerEmailAddressChanged -> {
                    isEmailAddressConfirmed = false
                    confirmationHash = event.confirmationHash
                }
            }
        }
        if (confirmationHash != command.confirmationHash) {
            return listOf(CustomerEmailAddressConfirmationFailed.build(command.customerID))
        }
        return if (isEmailAddressConfirmed) {
            listOf()
        } else listOf(CustomerEmailAddressConfirmed.build(command.customerID))
    }

    fun changeEmailAddress(eventStream: List<Event>, command: ChangeCustomerEmailAddress): List<Event> {
        var emailAddress: EmailAddress? = null
        for (event in eventStream) {
            if (event is CustomerRegistered) {
                emailAddress = event.emailAddress
            } else if (event is CustomerEmailAddressChanged) {
                emailAddress = event.emailAddress
            }
        }
        return if (emailAddress == command.emailAddress) {
            listOf()
        } else listOf(build(command.customerID, command.emailAddress, command.confirmationHash))
    }
}