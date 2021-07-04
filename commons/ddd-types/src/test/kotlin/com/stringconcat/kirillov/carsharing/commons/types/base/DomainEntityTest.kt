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
        val expectedEvent1 = domainEvent()
        val expectedEvent2 = domainEvent()

        entity.apply {
            produceEvent(expectedEvent1)
            produceEvent(expectedEvent2)
        }

        entity.popEvents() shouldContainAll listOf(expectedEvent1, expectedEvent2)
    }

    @Test
    fun `domain entity should clean events after pop`() {
        val event = domainEvent()

        val entity = TestEntity(id = 123L).apply {
            produceEvent(event)
            produceEvent(event)

            popEvents()
        }

        entity.popEvents() shouldBe emptyList()
    }
}

private class TestEntity(id: Long) : DomainEntity<Long>(id) {
    fun produceEvent(event: DomainEvent) {
        addEvent(event)
    }
}

private fun domainEvent() = object : DomainEvent() {}
