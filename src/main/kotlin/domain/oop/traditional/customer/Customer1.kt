package domain.oop.traditional.customer

import domain.shared.command.ChangeCustomerEmailAddress
import domain.shared.command.ConfirmCustomerEmailAddress
import domain.shared.command.RegisterCustomer
import domain.shared.exception.WrongConfirmationHashException
import domain.shared.value.EmailAddress
import domain.shared.value.Hash
import domain.shared.value.ID
import domain.shared.value.PersonName

class Customer1 private constructor(val id: ID, var emailAddress: EmailAddress, var confirmationHash: Hash, var name: PersonName) {

    var isEmailAddressConfirmed = false

    fun confirmEmailAddress(command: ConfirmCustomerEmailAddress) {
        if (command.confirmationHash != confirmationHash) {
            throw WrongConfirmationHashException()
        }
        isEmailAddressConfirmed = true
    }

    fun changeEmailAddress(command: ChangeCustomerEmailAddress) {
        emailAddress = command.emailAddress
        confirmationHash = command.confirmationHash
        isEmailAddressConfirmed = false
    }

    companion object {
        fun register(command: RegisterCustomer): Customer1 {
            return Customer1(
                    command.customerID,
                    command.emailAddress,
                    command.confirmationHash,
                    command.name
            )
        }
    }

}