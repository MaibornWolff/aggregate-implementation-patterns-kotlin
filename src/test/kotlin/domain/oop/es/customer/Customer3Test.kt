package domain.oop.es.customer

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

@TestMethodOrder(OrderAnnotation::class)
internal class Customer3Test {
    private var customerID: ID? = null
    private var emailAddress: EmailAddress? = null
    private var changedEmailAddress: EmailAddress? = null
    private var confirmationHash: Hash? = null
    private var wrongConfirmationHash: Hash? = null
    private var changedConfirmationHash: Hash? = null
    private var name: PersonName? = null
    private var customerRegistered: CustomerRegistered? = null
    private var recordedEvents: List<Event>? = null
    private var registeredCustomer: Customer3? = null

    @BeforeEach
    fun beforeEach() {
        customerID = ID.generate()
        emailAddress = build("john@doe.com")
        changedEmailAddress = build("john+changed@doe.com")
        confirmationHash = Hash.generate()
        wrongConfirmationHash = Hash.generate()
        changedConfirmationHash = Hash.generate()
        name = PersonName.build("John", "Doe")
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
        GIVEN(customerIsRegistered())
        WHEN_ConfirmEmailAddress_With(confirmationHash)
        THEN_EmailAddressConfirmed()
    }

    @Test
    @Order(3)
    fun confirmEmailAddress_withWrongConfirmationHash() {
        GIVEN(customerIsRegistered())
        WHEN_ConfirmEmailAddress_With(wrongConfirmationHash)
        THEN_EmailAddressConfirmationFailed()
    }

    @Test
    @Order(4)
    fun confirmEmailAddress_whenItWasAlreadyConfirmed() {
        GIVEN(
            customerIsRegistered(),
            __and_EmailAddressWasConfirmed()
        )
        WHEN_ConfirmEmailAddress_With(confirmationHash)
        THEN_NothingShouldHappen()
    }

    @Test
    @Order(5)
    fun confirmEmailAddress_withWrongConfirmationHash_whenItWasAlreadyConfirmed() {
        GIVEN(
            customerIsRegistered(),
            __and_EmailAddressWasConfirmed()
        )
        WHEN_ConfirmEmailAddress_With(wrongConfirmationHash)
        THEN_EmailAddressConfirmationFailed()
    }

    @Test
    @Order(6)
    fun changeEmailAddress() {
        GIVEN(customerIsRegistered())
        WHEN_ChangeEmailAddress_With(changedEmailAddress)
        THEN_EmailAddressChanged()
    }

    @Test
    @Order(7)
    fun changeEmailAddress_withUnchangedEmailAddress() {
        GIVEN(customerIsRegistered())
        WHEN_ChangeEmailAddress_With(emailAddress)
        THEN_NothingShouldHappen()
    }

    @Test
    @Order(8)
    fun changeEmailAddress_whenItWasAlreadyChanged() {
        GIVEN(
            customerIsRegistered(),
            __and_EmailAddressWasChanged()
        )
        WHEN_ChangeEmailAddress_With(changedEmailAddress)
        THEN_NothingShouldHappen()
    }

    @Test
    @Order(9)
    fun confirmEmailAddress_whenItWasPreviouslyConfirmedAndThenChanged() {
        GIVEN(
            customerIsRegistered(),
            __and_EmailAddressWasConfirmed(),
            __and_EmailAddressWasChanged()
        )
        WHEN_ConfirmEmailAddress_With(changedConfirmationHash)
        THEN_EmailAddressConfirmed()
    }

    /**
     * Methods for GIVEN
     */
    private fun GIVEN(vararg events: Event) {
        registeredCustomer = Customer3.reconstitute(events.toList())
    }

    private fun customerIsRegistered(): CustomerRegistered {
        return build(customerID!!, emailAddress!!, confirmationHash!!, name!!)
    }

    private fun __and_EmailAddressWasConfirmed(): CustomerEmailAddressConfirmed {
        return build(customerID!!)
    }

    private fun __and_EmailAddressWasChanged(): CustomerEmailAddressChanged {
        return build(customerID!!, changedEmailAddress!!, changedConfirmationHash!!)
    }

    /**
     * Methods for WHEN
     */
    private fun WHEN_RegisterCustomer() {
        val registerCustomer = build(emailAddress!!.value, name!!.givenName, name!!.familyName)
        customerRegistered = Customer3.register(registerCustomer)
        customerID = registerCustomer.customerID
        confirmationHash = registerCustomer.confirmationHash
    }

    private fun WHEN_ConfirmEmailAddress_With(confirmationHash: Hash?) {
        val command = ConfirmCustomerEmailAddress.build(customerID!!.value, confirmationHash!!.value)
        try {
            recordedEvents = registeredCustomer!!.confirmEmailAddress(command)
        } catch (e: NullPointerException) {
            Assertions.fail<Any>(propertyIsNull("confirmationHash"))
        }
    }

    private fun WHEN_ChangeEmailAddress_With(emailAddress: EmailAddress?) {
        val command = ChangeCustomerEmailAddress.build(customerID!!.value, emailAddress!!.value)
        try {
            recordedEvents = registeredCustomer!!.changeEmailAddress(command)
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
        Assertions.assertNotNull(customerRegistered, eventIsNull("register", eventName))
        Assertions.assertEquals(customerID, customerRegistered!!.customerID, propertyIsWrong(method, "customerID"))
        Assertions.assertEquals(
            emailAddress,
            customerRegistered!!.emailAddress,
            propertyIsWrong(method, "emailAddress")
        )
        Assertions.assertEquals(
            confirmationHash,
            customerRegistered!!.confirmationHash,
            propertyIsWrong(method, "confirmationHash")
        )
        Assertions.assertEquals(name, customerRegistered!!.name, propertyIsWrong(method, "name"))
    }

    private fun THEN_EmailAddressConfirmed() {
        val method = "confirmEmailAddress"
        val eventName = "CustomerEmailAddressConfirmed"
        Assertions.assertEquals(1, recordedEvents!!.size, noEventWasRecorded(method, eventName))
        val event = recordedEvents!![0]
        Assertions.assertNotNull(event, eventIsNull(method, eventName))
        Assertions.assertEquals(
            CustomerEmailAddressConfirmed::class.java,
            event.javaClass,
            eventOfWrongTypeWasRecorded(method)
        )
        Assertions.assertEquals(
            customerID,
            (event as CustomerEmailAddressConfirmed).customerID,
            propertyIsWrong(method, "customerID")
        )
    }

    private fun THEN_EmailAddressConfirmationFailed() {
        val method = "confirmEmailAddress"
        val eventName = "CustomerEmailAddressConfirmationFailed"
        Assertions.assertEquals(1, recordedEvents!!.size, noEventWasRecorded(method, eventName))
        val event = recordedEvents!![0]
        Assertions.assertNotNull(event, eventIsNull(method, eventName))
        Assertions.assertEquals(
            CustomerEmailAddressConfirmationFailed::class.java,
            event.javaClass,
            eventOfWrongTypeWasRecorded(method)
        )
        Assertions.assertEquals(
            customerID,
            (event as CustomerEmailAddressConfirmationFailed).customerID,
            propertyIsWrong(method, "customerID")
        )
    }

    private fun THEN_EmailAddressChanged() {
        val method = "changeEmailAddress"
        val eventName = "CustomerEmailAddressChanged"
        Assertions.assertEquals(1, recordedEvents!!.size, noEventWasRecorded(method, eventName))
        val event = recordedEvents!![0]
        Assertions.assertNotNull(event, eventIsNull(method, eventName))
        Assertions.assertEquals(
            CustomerEmailAddressChanged::class.java,
            event.javaClass,
            eventOfWrongTypeWasRecorded(method)
        )
        Assertions.assertEquals(
            customerID,
            (event as CustomerEmailAddressChanged).customerID,
            propertyIsWrong(method, "customerID")
        )
        Assertions.assertEquals(changedEmailAddress, event.emailAddress, propertyIsWrong(method, "emailAddress"))
    }

    private fun THEN_NothingShouldHappen() {
        Assertions.assertEquals(0, recordedEvents!!.size, noEventShouldHaveBeenRecorded(typeOfFirst(recordedEvents!!)))
    }
}