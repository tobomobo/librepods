package me.kavishdevar.librepods.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BatteryNotificationTest {
    @Test
    fun keepsLegacyLeftRightAndCaseBattery() {
        val packet = byteArrayOf(
            0x04, 0x00, 0x04, 0x00, 0x04, 0x00, 0x03,
            0x04, 0x01, 80, 0x02, 0x01,
            0x02, 0x01, 75, 0x01, 0x01,
            0x08, 0x01, 60, 0x02, 0x01
        )
        val notification = AirPodsNotifications.BatteryNotification()

        assertTrue(notification.isBatteryData(packet))
        notification.setBattery(packet)

        val batteries = notification.getBattery()
        assertEquals(80, batteries.find { it.component == BatteryComponent.LEFT }?.level)
        assertEquals(75, batteries.find { it.component == BatteryComponent.RIGHT }?.level)
        assertEquals(60, batteries.find { it.component == BatteryComponent.CASE }?.level)
        assertEquals("L: 80%  R: ⚡ 75%  Case: 60%", batteryNotificationText(batteries))
    }

    @Test
    fun parsesCapturedSingleHeadsetBattery() {
        // Captured from AirPods Max: https://github.com/librepods-org/librepods/issues/30#issuecomment-3540231232
        val packet = byteArrayOf(
            0x04, 0x00, 0x04, 0x00, 0x04, 0x00, 0x01,
            0x01, 0x01, 100, 0x02, 0x01
        )
        val notification = AirPodsNotifications.BatteryNotification()

        assertTrue(notification.isBatteryData(packet))
        notification.setBattery(packet)

        val batteries = notification.getBattery()
        assertEquals(100, batteries[0].level)
        assertEquals(100, batteries[1].level)
        assertEquals(BatteryStatus.DISCONNECTED, batteries[2].status)
        assertEquals(BatteryComponent.HEADSET, batteries[3].component)
        assertEquals("100%", batteryNotificationText(batteries))
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
