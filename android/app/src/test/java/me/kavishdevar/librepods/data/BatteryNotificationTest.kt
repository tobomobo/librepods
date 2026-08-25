package me.kavishdevar.librepods.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BatteryNotificationTest {
    @Test
    fun parsesSingleHeadsetBattery() {
        val packet = byteArrayOf(
            0x04, 0x00, 0x04, 0x00, 0x04, 0x00, 0x01,
            0x01, 0x01, 83, 0x02, 0x01
        )
        val notification = AirPodsNotifications.BatteryNotification()

        assertTrue(notification.isBatteryData(packet))
        notification.setBattery(packet)

        val batteries = notification.getBattery()
        assertEquals(83, batteries[0].level)
        assertEquals(83, batteries[1].level)
        assertEquals(BatteryStatus.DISCONNECTED, batteries[2].status)
    }
}
