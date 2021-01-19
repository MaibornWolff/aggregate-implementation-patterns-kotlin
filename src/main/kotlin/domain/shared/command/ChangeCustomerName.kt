package domain.shared.command

import domain.shared.value.ID
import domain.shared.value.ID.Companion.build
import domain.shared.value.PersonName
import domain.shared.value.PersonName.Companion.build

class ChangeCustomerName private constructor(customerID: String, givenName: String, familyName: String) {

    val customerID: ID = build(customerID)
    val name: PersonName = build(givenName, familyName)

    companion object {
        fun build(customerID: String, givenName: String, familyName: String): ChangeCustomerName {
            return ChangeCustomerName(customerID, givenName, familyName)
        }
    }

}