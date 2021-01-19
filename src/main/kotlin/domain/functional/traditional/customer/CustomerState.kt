package domain.functional.traditional.customer

import domain.shared.value.EmailAddress
import domain.shared.value.Hash
import domain.shared.value.ID
import domain.shared.value.PersonName

class CustomerState constructor(val id: ID, val emailAddress: EmailAddress, val confirmationHash: Hash, val name: PersonName, val isEmailAddressConfirmed: Boolean = false)