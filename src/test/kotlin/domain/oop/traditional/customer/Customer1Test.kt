package domain.oop.traditional.customer

import domain.oop.traditional.customer.Customer1.Companion.register
import domain.shared.command.ChangeCustomerEmailAddress
import domain.shared.command.ConfirmCustomerEmailAddress
import domain.shared.command.RegisterCustomer.Companion.build
import domain.shared.exception.WrongConfirmationHashException
import domain.shared.value.EmailAddress
import domain.shared.value.EmailAddress.Companion.build
import domain.shared.value.Hash
import domain.shared.value.Hash.Companion.generate
import domain.shared.value.ID
import domain.shared.value.PersonName
import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation

@TestMethodOrder(OrderAnnotation::class)
internal class Customer1Test {
    private var customerID: ID? = null
    private var confirmationHash: Hash? = null
    private var name: PersonName? = null
    private var emailAddress: EmailAddress? = null
    private var changedEmailAddress: EmailAddress? = null
    private var wrongConfirmationHash: Hash? = null
    private var changedConfirmationHash: Hash? = null
    private var registeredCustomer: Customer1? = null

    @BeforeEach
    fun beforeEach() {
        emailAddress = build("john@doe.com")
        changedEmailAddress = build("john+changed@doe.com")
        wrongConfirmationHash = generate()
        changedConfirmationHash = generate()
        name = PersonName.build("John", "Doe")
    }

    @Test
    @Order(1)
    fun registerCustomer() { // When registerCustomer
        val command = build(emailAddress!!.value, name!!.givenName, name!!.familyName)
        val customer = register(command)
        // Then it should succeed
        // and should have the expected state
        Assertions.assertNotNull(customer)
        Assertions.assertEquals(customer.id, command.customerID)
        Assertions.assertEquals(customer.name, command.name)
        Assertions.assertEquals(customer.emailAddress, command.emailAddress)
        Assertions.assertEquals(customer.confirmationHash, command.confirmationHash)
        Assertions.assertFalse(customer.isEmailAddressConfirmed)
    }

    @Test
    @Order(2)
    fun confirmEmailAddress() { // Given
        givenARegisteredCustomer()
        // When confirmCustomerEmailAddress
        // Then it should succeed
        val command = ConfirmCustomerEmailAddress.build(customerID!!.value, confirmationHash!!.value)
        Assertions.assertDoesNotThrow { registeredCustomer!!.confirmEmailAddress(command) }
        // and the emailAddress should be confirmed
        Assertions.assertTrue(registeredCustomer!!.isEmailAddressConfirmed)
    }

    @Test
    @Order(3)
    fun confirmEmailAddress_withWrongConfirmationHash() { // Given
        givenARegisteredCustomer()
        // When confirmCustomerEmailAddress
// Then it should throw WrongConfirmationHashException
        val command = ConfirmCustomerEmailAddress.build(customerID!!.value, wrongConfirmationHash!!.value)
        Assertions.assertThrows(WrongConfirmationHashException::class.java) { registeredCustomer!!.confirmEmailAddress(command) }
        // and the emailAddress should not be confirmed
        Assertions.assertFalse(registeredCustomer!!.isEmailAddressConfirmed)
    }

    @Test
    @Order(6)
    fun changeEmailAddress() { // Given
        givenARegisteredCustomer()
        // When changeCustomerEmailAddress
        val command = ChangeCustomerEmailAddress.build(customerID!!.value, changedEmailAddress!!.value)
        registeredCustomer!!.changeEmailAddress(command)
        // Then the emailAddress and confirmationHash should be changed and the emailAddress should be unconfirmed
        Assertions.assertEquals(registeredCustomer!!.emailAddress, command.emailAddress)
        Assertions.assertEquals(registeredCustomer!!.confirmationHash, command.confirmationHash)
        Assertions.assertFalse(registeredCustomer!!.isEmailAddressConfirmed)
    }

    @Test
    @Order(9)
    fun confirmEmailAddress_whenItWasPreviouslyConfirmedAndThenChanged() { // Given
        givenARegisteredCustomer()
        givenEmailAddressWasConfirmed()
        givenEmailAddressWasChanged()
        // When confirmCustomerEmailAddress
// Then it should succeed
        val command = ConfirmCustomerEmailAddress.build(customerID!!.value, changedConfirmationHash!!.value)
        Assertions.assertDoesNotThrow { registeredCustomer!!.confirmEmailAddress(command) }
        // and the emailAddress should be confirmed
        Assertions.assertTrue(registeredCustomer!!.isEmailAddressConfirmed)
    }

    /**
     * Helper methods to set up the Given state
     */
    private fun givenARegisteredCustomer() {
        val register = build(emailAddress!!.value, name!!.givenName, name!!.familyName)
        customerID = register.customerID
        confirmationHash = register.confirmationHash
        registeredCustomer = register(register)
    }

    private fun givenEmailAddressWasConfirmed() {
        val command = ConfirmCustomerEmailAddress.build(customerID!!.value, confirmationHash!!.value)
        try {
            registeredCustomer!!.confirmEmailAddress(command)
        } catch (e: WrongConfirmationHashException) {
            Assertions.fail<Any>("unexpected error in givenEmailAddressWasConfirmed: " + e.message)
        }
    }

    private fun givenEmailAddressWasChanged() {
        val command = ChangeCustomerEmailAddress.build(customerID!!.value, changedEmailAddress!!.value)
        changedConfirmationHash = command.confirmationHash
        registeredCustomer!!.changeEmailAddress(command)
    }
}