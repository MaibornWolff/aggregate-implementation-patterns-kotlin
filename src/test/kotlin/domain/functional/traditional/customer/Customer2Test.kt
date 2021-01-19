package domain.functional.traditional.customer

import domain.functional.traditional.customer.Customer2.changeEmailAddress
import domain.functional.traditional.customer.Customer2.confirmEmailAddress
import domain.shared.command.ChangeCustomerEmailAddress
import domain.shared.command.ConfirmCustomerEmailAddress
import domain.shared.command.RegisterCustomer.Companion.build
import domain.shared.exception.WrongConfirmationHashException
import domain.shared.value.EmailAddress
import domain.shared.value.EmailAddress.Companion.build
import domain.shared.value.Hash
import domain.shared.value.ID
import domain.shared.value.PersonName
import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation

@TestMethodOrder(OrderAnnotation::class)
internal class Customer2Test {
    private var customerID: ID? = null
    private var confirmationHash: Hash? = null
    private var name: PersonName? = null
    private var emailAddress: EmailAddress? = null
    private var changedEmailAddress: EmailAddress? = null
    private var wrongConfirmationHash: Hash? = null
    private var changedConfirmationHash: Hash? = null
    private var registeredCustomer: CustomerState? = null

    @BeforeEach
    fun beforeEach() {
        emailAddress = build("john@doe.com")
        changedEmailAddress = build("john+changed@doe.com")
        wrongConfirmationHash = Hash.generate()
        changedConfirmationHash = Hash.generate()
        name = PersonName.build("John", "Doe")
    }

    @Test
    @Order(1)
    fun registerCustomer() { // When
        val command = build(emailAddress!!.value, name!!.givenName, name!!.familyName)
        val customer = Customer2.register(command)
        // Then it should succeed
// and it should expose the expected state
        Assertions.assertNotNull(customer)
        Assertions.assertEquals(command.customerID, customer.id)
        Assertions.assertEquals(command.name, customer.name)
        Assertions.assertEquals(command.emailAddress, customer.emailAddress)
        Assertions.assertEquals(command.confirmationHash, customer.confirmationHash)
        Assertions.assertFalse(customer.isEmailAddressConfirmed)
    }

    @Test
    @Order(2)
    fun confirmEmailAddress() { // Given
        givenARegisteredCustomer()
        // When confirmCustomerEmailAddress
// Then it should succeed
        val command = ConfirmCustomerEmailAddress.build(customerID!!.value, confirmationHash!!.value)
        val changedCustomer = Assertions.assertDoesNotThrow<CustomerState> { confirmEmailAddress(registeredCustomer!!, command) }
        // and the emailAddress of the changed Customer should be confirmed
        Assertions.assertTrue(changedCustomer.isEmailAddressConfirmed)
    }

    @Test
    @Order(3)
    fun confirmEmailAddress_withWrongConfirmationHash() { // Given
        givenARegisteredCustomer()
        // When confirmCustomerEmailAddress
// Then it should throw WrongConfirmationHashException
        val command = ConfirmCustomerEmailAddress.build(customerID!!.value, wrongConfirmationHash!!.value)
        Assertions.assertThrows(WrongConfirmationHashException::class.java) { confirmEmailAddress(registeredCustomer!!, command) }
    }

    @Test
    @Order(6)
    fun changeEmailAddress() { // Given
        givenARegisteredCustomer()
        // When changeCustomerEmailAddress
        val command = ChangeCustomerEmailAddress.build(customerID!!.value, changedEmailAddress!!.value)
        val changedCustomer = changeEmailAddress(registeredCustomer!!, command)
        // Then the emailAddress and confirmationHash should be changed and the emailAddress should be unconfirmed
        Assertions.assertEquals(command.emailAddress, changedCustomer.emailAddress)
        Assertions.assertEquals(command.confirmationHash, changedCustomer.confirmationHash)
        Assertions.assertFalse(changedCustomer.isEmailAddressConfirmed)
    }

    @Test
    @Order(9)
    fun confirmEmailAddress_whenItWasPreviouslyConfirmedAndThenChanged() { // Given
        givenARegisteredCustomer()
        givenEmailAddressWasConfirmed()
        givenEmailAddressWasChanged()
        // When confirmEmailAddress
// Then it should throw WrongConfirmationHashException
        val command = ConfirmCustomerEmailAddress.build(customerID!!.value, changedConfirmationHash!!.value)
        val changedCustomer = Assertions.assertDoesNotThrow<CustomerState> { confirmEmailAddress(registeredCustomer!!, command) }
        // and the emailAddress of the changed Customer should be confirmed
        Assertions.assertTrue(changedCustomer.isEmailAddressConfirmed)
    }

    /**
     * Helper methods to set up the Given state
     */
    private fun givenARegisteredCustomer() {
        val register = build(emailAddress!!.value, name!!.givenName, name!!.familyName)
        customerID = register.customerID
        confirmationHash = register.confirmationHash
        registeredCustomer = Customer2.register(register)
    }

    private fun givenEmailAddressWasConfirmed() {
        val command = ConfirmCustomerEmailAddress.build(customerID!!.value, confirmationHash!!.value)
        try {
            registeredCustomer = confirmEmailAddress(registeredCustomer!!, command)
        } catch (e: WrongConfirmationHashException) {
            Assertions.fail<Any>("unexpected error in givenEmailAddressWasConfirmed: " + e.message)
        }
    }

    private fun givenEmailAddressWasChanged() {
        val command = ChangeCustomerEmailAddress.build(customerID!!.value, changedEmailAddress!!.value)
        changedConfirmationHash = command.confirmationHash
        registeredCustomer = changeEmailAddress(registeredCustomer!!, command)
    }
}