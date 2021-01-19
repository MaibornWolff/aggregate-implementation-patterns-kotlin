package domain.functional.es.customer

import domain.THelper.eventIsNull
import domain.THelper.eventOfWrongTypeWasRecorded
import domain.THelper.noEventShouldHaveBeenRecorded
import domain.THelper.noEventWasRecorded
import domain.THelper.propertyIsNull
import domain.THelper.propertyIsWrong
import domain.THelper.typeOfFirst
import domain.shared.command.ChangeCustomerEmailAddress
import domain.shared.command.ConfirmCustomerEmailAddress
import domain.shared.command.RegisterCustomer.Companion.build
import domain.shared.event.*
import domain.shared.event.CustomerEmailAddressChanged.Companion.build
import domain.shared.event.CustomerEmailAddressConfirmed.Companion.build
import domain.shared.event.CustomerRegistered.Companion.build
import domain.shared.value.EmailAddress
import domain.shared.value.EmailAddress.Companion.build
import domain.shared.value.Hash
import domain.shared.value.ID
import domain.shared.value.PersonName
import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import java.util.*

@TestMethodOrder(OrderAnnotation::class)
class Customer5Test {
    private var customerID: ID? = null
    private var emailAddress: EmailAddress? = null
    private var changedEmailAddress: EmailAddress? = null
    private var confirmationHash: Hash? = null
    private var wrongConfirmationHash: Hash? = null
    private var changedConfirmationHash: Hash? = null
    private var name: PersonName? = null
    private var eventStream: MutableList<Event>? = null
    private var customerRegistered: CustomerRegistered? = null
    private var recordedEvents: List<Event>? = null

    @BeforeEach
    fun beforeEach() {
        customerID = ID.generate()
        emailAddress = build("john@doe.com")
        changedEmailAddress = build("john+changed@doe.com")
        confirmationHash = Hash.generate()
        wrongConfirmationHash = Hash.generate()
        changedConfirmationHash = Hash.generate()
        name = PersonName.build("John", "Doe")
        eventStream = ArrayList()
        recordedEvents = ArrayList()
    }

    @Test
    @Order(1)
    fun registerCustomer() {
        WHEN_RegisterCustomer()
        THEN_CustomerRegistered()
    }

    @Test
    @Order(2)
    fun confirmEmailAddress() {
        GIVEN_CustomerRegistered()
        WHEN_ConfirmEmailAddress_With(confirmationHash)
        THEN_EmailAddressConfirmed()
    }

    @Test
    @Order(3)
    fun confirmEmailAddress_withWrongConfirmationHash() {
        GIVEN_CustomerRegistered()
        WHEN_ConfirmEmailAddress_With(wrongConfirmationHash)
        THEN_EmailAddressConfirmationFailed()
    }

    @Test
    @Order(4)
    fun confirmEmailAddress_whenItWasAlreadyConfirmed() {
        GIVEN_CustomerRegistered()
        __and_EmailAddressWasConfirmed()
        WHEN_ConfirmEmailAddress_With(confirmationHash)
        THEN_NothingShouldHappen()
    }

    @Test
    @Order(5)
    fun confirmEmailAddress_withWrongConfirmationHash_whenItWasAlreadyConfirmed() {
        GIVEN_CustomerRegistered()
        __and_EmailAddressWasConfirmed()
        WHEN_ConfirmEmailAddress_With(wrongConfirmationHash)
        THEN_EmailAddressConfirmationFailed()
    }

    @Test
    @Order(6)
    fun changeEmailAddress() {
        GIVEN_CustomerRegistered()
        WHEN_ChangeEmailAddress_With(changedEmailAddress)
        THEN_EmailAddressChanged()
    }

    @Test
    @Order(7)
    fun changeEmailAddress_withUnchangedEmailAddress() { // Given
        GIVEN_CustomerRegistered()
        WHEN_ChangeEmailAddress_With(emailAddress)
        THEN_NothingShouldHappen()
    }

    @Test
    @Order(8)
    fun changeEmailAddress_whenItWasAlreadyChanged() {
        GIVEN_CustomerRegistered()
        __and_EmailAddressWasChanged()
        WHEN_ChangeEmailAddress_With(changedEmailAddress)
        THEN_NothingShouldHappen()
    }

    @Test
    @Order(9)
    fun confirmEmailAddress_whenItWasPreviouslyConfirmedAndThenChanged() { // Given
        GIVEN_CustomerRegistered()
        __and_EmailAddressWasConfirmed()
        __and_EmailAddressWasChanged()
        WHEN_ConfirmEmailAddress_With(changedConfirmationHash)
        THEN_EmailAddressConfirmed()
    }

    /**
     * Methods for GIVEN
     */
    private fun GIVEN_CustomerRegistered() {
        eventStream!!.add(build(customerID!!, emailAddress!!, confirmationHash!!, name!!))
    }

    private fun __and_EmailAddressWasConfirmed() {
        eventStream!!.add(build(customerID!!))
    }

    private fun __and_EmailAddressWasChanged() {
        eventStream!!.add(build(customerID!!, changedEmailAddress!!, changedConfirmationHash!!))
        emailAddress = changedEmailAddress
        confirmationHash = changedConfirmationHash
    }

    /**
     * Methods for WHEN
     */
    private fun WHEN_RegisterCustomer() {
        val registerCustomer = build(emailAddress!!.value, name!!.givenName, name!!.familyName)
        customerRegistered = Customer5.register(registerCustomer)
        customerID = registerCustomer.customerID
        confirmationHash = registerCustomer.confirmationHash
    }

    private fun WHEN_ConfirmEmailAddress_With(confirmationHash: Hash?) {
        val command = ConfirmCustomerEmailAddress.build(customerID!!.value, confirmationHash!!.value)
        try {
            recordedEvents = Customer5.confirmEmailAddress(eventStream!!, command)
        } catch (e: NullPointerException) {
            Assertions.fail<Any>(propertyIsNull("confirmationHash"))
        }
    }

    private fun WHEN_ChangeEmailAddress_With(emailAddress: EmailAddress?) {
        val command = ChangeCustomerEmailAddress.build(customerID!!.value, emailAddress!!.value)
        try {
            recordedEvents = Customer5.changeEmailAddress(eventStream!!, command)
            changedConfirmationHash = command.confirmationHash
        } catch (e: NullPointerException) {
            Assertions.fail<Any>(propertyIsNull("emailAddress"))
        }
    }

    /**
     * Methods for THEN
     */
    private fun THEN_CustomerRegistered() {
        val method = "register"
        val eventName = "CustomerRegistered"
        Assertions.assertNotNull(customerRegistered, eventIsNull(method, eventName))
        Assertions.assertEquals(customerID, customerRegistered!!.customerID, propertyIsWrong(method, "customerID"))
        Assertions.assertEquals(emailAddress, customerRegistered!!.emailAddress, propertyIsWrong(method, "emailAddress"))
        Assertions.assertEquals(confirmationHash, customerRegistered!!.confirmationHash, propertyIsWrong(method, "confirmationHash"))
        Assertions.assertEquals(name, customerRegistered!!.name, propertyIsWrong(method, "name"))
    }

    private fun THEN_EmailAddressConfirmed() {
        val method = "confirmEmailAddress"
        val eventName = "CustomerEmailAddressConfirmed"
        Assertions.assertEquals(1, recordedEvents!!.size, noEventWasRecorded(method, eventName))
        Assertions.assertNotNull(recordedEvents!![0], eventIsNull(method, eventName))
        Assertions.assertEquals(CustomerEmailAddressConfirmed::class.java, recordedEvents!![0].javaClass, eventOfWrongTypeWasRecorded(method))
        val event = recordedEvents!![0] as CustomerEmailAddressConfirmed
        Assertions.assertEquals(customerID, event.customerID, propertyIsWrong(method, "customerID"))
    }

    private fun THEN_EmailAddressConfirmationFailed() {
        val method = "confirmEmailAddress"
        val eventName = "CustomerEmailAddressConfirmationFailed"
        Assertions.assertEquals(1, recordedEvents!!.size, noEventWasRecorded(method, eventName))
        Assertions.assertNotNull(recordedEvents!![0], eventIsNull(method, eventName))
        Assertions.assertEquals(CustomerEmailAddressConfirmationFailed::class.java, recordedEvents!![0].javaClass, eventOfWrongTypeWasRecorded(method))
        val event = recordedEvents!![0] as CustomerEmailAddressConfirmationFailed
        Assertions.assertEquals(customerID, event.customerID, propertyIsWrong(method, "customerID"))
    }

    private fun THEN_EmailAddressChanged() {
        val method = "changeEmailAddress"
        val eventName = "CustomerEmailAddressChanged"
        Assertions.assertEquals(1, recordedEvents!!.size, noEventWasRecorded(method, eventName))
        Assertions.assertNotNull(recordedEvents!![0], eventIsNull(method, eventName))
        Assertions.assertEquals(CustomerEmailAddressChanged::class.java, recordedEvents!![0].javaClass, eventOfWrongTypeWasRecorded(method))
        val event = recordedEvents!![0] as CustomerEmailAddressChanged
        Assertions.assertEquals(customerID, event.customerID, propertyIsWrong(method, "customerID"))
        Assertions.assertEquals(changedEmailAddress, event.emailAddress, propertyIsWrong(method, "emailAddress"))
        Assertions.assertEquals(changedConfirmationHash, event.confirmationHash, propertyIsWrong(method, "confirmationHash"))
    }

    private fun THEN_NothingShouldHappen() {
        Assertions.assertEquals(0, recordedEvents!!.size,
                noEventShouldHaveBeenRecorded(typeOfFirst(recordedEvents!!)))
    }
}