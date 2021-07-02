package com.stringconcat.kirillov.carsharing.commons.types.base

import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class DomainEntityTest {
    @Test
    fun `domain entity should has typed id`() {
        val expectedId = 123L
        val entity = TestEntity(expectedId)

        entity.id shouldBe expectedId
    }

    @Test
    fun `domain entity should accumulate added domain events`() {
        val entity = TestEntity(id = 123L)
        val expectedEvents1 = listOf(domainEvent())
        val expectedEvents2 = listOf(domainEvent())

        entity.apply {
            produceEvents(expectedEvents1)
            produceEvents(expectedEvents2)
        }

        entity.popEvents() shouldContainAll expectedEvents1 + expectedEvents2
    }

    @Test
    fun `domain entity should clean all events after pop`() {
        val events = listOf<DomainEvent>(domainEvent())

        val entity = TestEntity(id = 123L).apply {
            produceEvents(events)
            popEvents()
        }

        entity.popEvents() shouldBe emptyList()
    }
}

private class TestEntity(id: Long) : DomainEntity<Long>(id) {
    fun produceEvents(events: List<DomainEvent>) {
        addEvents(events)
    }
}

private fun domainEvent() = object : DomainEvent() {}
