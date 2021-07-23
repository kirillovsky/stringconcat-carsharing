package com.stringconcat.kirillov.carsharing.commons.types.base

import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldNotBe
import java.lang.Thread.sleep
import org.junit.jupiter.api.Test

internal class EventIdTest {
    @Test
    fun `generate should creates eventId with unique uuid`() {
        val oneEventId = EventId.generate()
        val anotherEventId = EventId.generate()

        oneEventId.uuid shouldNotBe anotherEventId.uuid
    }

    @Test
    fun `generate should creates eventId with non-decreasing createdTimestamps`() {
        val oneEventId = EventId.generate()
        sleep(500)
        val anotherEventId = EventId.generate()

        oneEventId.createdTimestamp shouldBeLessThan anotherEventId.createdTimestamp
    }
}