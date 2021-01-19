package domain

import domain.shared.event.Event

object THelper {
    fun typeOfFirst(recordedEvents: List<Event>): String {
        return if (recordedEvents.isEmpty()) {
            "???"
        } else recordedEvents[0].javaClass.simpleName
    }

    fun propertyIsNull(property: String?): String {
        return String.format(
                "PROBLEM: The %s is null!\n" +
                        "HINT: Maybe you didn't apply the previous events properly!?\n",
                property
        )
    }

    fun eventIsNull(method: String?, expectedEvent: String?): String {
        return String.format(
                "PROBLEM in %s(): The recorded/returned event is NULL!\n" +
                        "HINT: Make sure you record/return a %s event\n\n",
                method,
                expectedEvent
        )
    }

    fun propertyIsWrong(method: String?, property: String?): String {
        return String.format(
                "PROBLEM in %s(): The event contains a wrong %s!\n" +
                        "HINT: The %s in the event should be taken from the command!\n\n",
                method, property, property
        )
    }

    fun noEventWasRecorded(method: String?, expectedEvent: String?): String {
        return String.format(
                "PROBLEM in %s(): No event was recorded/returned!\n" +
                        "HINTS: Build a %s event and record/return it!\n" +
                        "       Did you apply all previous events properly?\n" +
                        "       Check your business logic :-)!\n\n",
                method, expectedEvent
        )
    }

    fun eventOfWrongTypeWasRecorded(method: String?): String {
        return String.format(
                "PROBLEM in %s(): An event of the wrong type was recorded/returned!\n" +
                        "HINTS: Did you apply all previous events properly?\n" +
                        "       Check your business logic :-)!\n\n",
                method
        )
    }

    fun noEventShouldHaveBeenRecorded(recordedEventType: String?): String {
        return String.format(
                "PROBLEM: No event should have been recorded/returned!\n" +
                        "HINTS: Check your business logic - this command should be ignored (idempotency)!\n" +
                        "       Did you apply all previous events properly?\n" +
                        "       The recorded/returned event is of type %s.\n\n",
                recordedEventType
        )
    }
}