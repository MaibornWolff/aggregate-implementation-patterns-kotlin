package domain.functional.traditional.customer

import domain.shared.command.ChangeCustomerEmailAddress
import domain.shared.command.ConfirmCustomerEmailAddress
import domain.shared.command.RegisterCustomer
import domain.shared.exception.WrongConfirmationHashException

object Customer2 {

    fun register(command: RegisterCustomer): CustomerState {
        return CustomerState(
                command.customerID,
                command.emailAddress,
                command.confirmationHash,
                command.name
        )
    }

    fun confirmEmailAddress(current: CustomerState, command: ConfirmCustomerEmailAddress): CustomerState {
        if (command.confirmationHash != current.confirmationHash) {
            throw WrongConfirmationHashException()
        }
        return CustomerState(
                current.id,
                current.emailAddress,
                current.confirmationHash,
                current.name,
                true
        )
    }

    fun changeEmailAddress(current: CustomerState, command: ChangeCustomerEmailAddress): CustomerState {
        return CustomerState(
                current.id,
                command.emailAddress,
                command.confirmationHash,
                current.name,
                false
        )
    }
}