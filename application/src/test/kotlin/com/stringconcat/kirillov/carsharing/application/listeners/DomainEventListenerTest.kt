package com.stringconcat.kirillov.carsharing.application.listeners

import com.stringconcat.kirillov.carsharing.commons.types.base.DomainEvent
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class DomainEventListenerTest {
    private class TestDomainEvent : DomainEvent()
    private class TestEventListener : DomainEventListener<TestDomainEvent> {
        override fun handle(event: TestDomainEvent) = Unit
    }

    @Test
    fun `eventType by default should return generic parameter from implementation class`() {
        val eventType = TestEventListener().eventType()

        eventType shouldBe TestDomainEvent::class
    }
}