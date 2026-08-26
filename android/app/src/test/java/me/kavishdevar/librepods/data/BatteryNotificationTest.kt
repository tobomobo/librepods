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
        assertEquals(BatteryComponent.HEADSET, batteries[3].component)
        assertEquals("83%", batteryNotificationText(batteries))
    }

    @Test
    fun keepsBleHeadsetBatteryUnified() {
        val notification = AirPodsNotifications.BatteryNotification()

        notification.setHeadsetBatteryDirect(64, true)

        val batteries = notification.getBattery()
        assertEquals(BatteryStatus.DISCONNECTED, batteries[2].status)
        assertEquals(BatteryComponent.HEADSET, batteries[3].component)
        assertEquals(64, batteries[3].level)
        assertEquals(BatteryStatus.CHARGING, batteries[3].status)
        assertEquals("⚡ 64%", batteryNotificationText(batteries))
    }

    @Test
    fun hidesMissingBleHeadsetBattery() {
        val notification = AirPodsNotifications.BatteryNotification()

        notification.setHeadsetBatteryDirect(null, false)

        assertEquals("", batteryNotificationText(notification.getBattery()))
    }
}
