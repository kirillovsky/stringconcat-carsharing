package com.stringconcat.kirillov.carsharing.commons.types.base

import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

internal class DomainEventTest {
    @Test
    fun `domain event should be created with unique id`() {
        val oneEvent = TestEvent()
        val anotherEvent = TestEvent()

        oneEvent.id shouldNotBe anotherEvent.id
    }
}

private class TestEvent : DomainEvent()