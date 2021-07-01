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
            addEvents(expectedEvents1)
            addEvents(expectedEvents2)
        }

        entity.popEvents() shouldContainAll expectedEvents1 + expectedEvents2
    }

    @Test
    fun `domain entity should clean all events after pop`() {
        val events = listOf<DomainEvent>(domainEvent())

        val entity = TestEntity(id = 123L).apply {
            addEvents(events)
            popEvents()
        }

        entity.popEvents() shouldBe emptyList()
    }
}

private class TestEntity(id: Long) : DomainEntity<Long>(id)

private fun domainEvent() = object : DomainEvent() {}
