package domain.shared.command

import domain.shared.value.EmailAddress
import domain.shared.value.EmailAddress.Companion.build
import domain.shared.value.Hash
import domain.shared.value.ID
import domain.shared.value.PersonName
import domain.shared.value.PersonName.Companion.build

class RegisterCustomer private constructor(emailAddress: String, givenName: String, familyName: String) {

    val customerID: ID = ID.generate()
    val emailAddress: EmailAddress = build(emailAddress)
    val confirmationHash: Hash = Hash.generate()
    val name: PersonName = build(givenName, familyName)

    companion object {
        fun build(emailAddress: String, givenName: String, familyName: String): RegisterCustomer {
            return RegisterCustomer(emailAddress, givenName, familyName)
        }
    }

}