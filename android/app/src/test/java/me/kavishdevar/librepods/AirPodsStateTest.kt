package me.kavishdevar.librepods

import me.kavishdevar.librepods.bluetooth.AACPManager
import me.kavishdevar.librepods.bluetooth.AACPManager.Companion.ControlCommandIdentifiers
import me.kavishdevar.librepods.bluetooth.BLEManager
import me.kavishdevar.librepods.data.airPodsMaxArtworkRes
import me.kavishdevar.librepods.presentation.components.availableSerialNumbers
import me.kavishdevar.librepods.services.matchesBleBatteryModel
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class AirPodsStateTest {
    @Test
    fun omitsMissingBudSerialNumbers() {
        assertEquals(listOf(0 to "H123"), availableSerialNumbers(listOf("H123", "0", "0")))
    }

    @Test
    fun keepsOnlyLatestControlState() {
        val manager = AACPManager()
        val identifier = ControlCommandIdentifiers.ADAPTIVE_VOLUME_CONFIG

        manager.setControlCommandStatusValue(identifier, byteArrayOf(0x02))
        manager.setControlCommandStatusValue(identifier, byteArrayOf(0x01))

        assertEquals(1, manager.controlCommandStatusList.size)
        assertArrayEquals(byteArrayOf(0x01), manager.getControlCommandStatus(identifier)?.value)
    }

    @Test
    fun resolvesAirPodsMaxColorsPerModel() {
        assertEquals("Sky Blue", BLEManager.colorName(0x0A20, 0x03))
        assertEquals("Midnight", BLEManager.colorName(0x1F20, 0x12))
        assertEquals("Midnight", BLEManager.colorName(0x2D20, 0x12))
        assertEquals("Unknown (0x02)", BLEManager.colorName(0x1F20, 0x02))
        assertEquals("Unknown (0x03)", BLEManager.colorName(0x2D20, 0x03))
        assertEquals("Blue", BLEManager.colorName(0x1420, 0x03))
    }

    @Test
    fun acceptsBleBatteryOnlyForActiveModelType() {
        assertEquals(true, matchesBleBatteryModel(null, "AirPods Pro"))
        assertEquals(true, matchesBleBatteryModel(false, "AirPods Max 2"))
        assertEquals(true, matchesBleBatteryModel(true, "AirPods Pro"))
        assertEquals(false, matchesBleBatteryModel(false, "AirPods Pro"))
        assertEquals(false, matchesBleBatteryModel(true, "AirPods Max 2"))
    }

    @Test
    fun usesExactAirPodsMaxArtwork() {
        assertEquals(R.drawable.airpods_max, airPodsMaxArtworkRes("Silver", 0))
        assertEquals(R.drawable.airpods_max_spacegray, airPodsMaxArtworkRes("Midnight", 0))
        assertEquals(R.drawable.airpods_max_spacegray, airPodsMaxArtworkRes("Space Gray", 0))
        assertEquals(R.drawable.airpods_max_skyblue, airPodsMaxArtworkRes("Sky Blue", 0))
        assertEquals(R.drawable.airpods_max_pink, airPodsMaxArtworkRes("Pink", 0))
        assertEquals(R.drawable.airpods_max_green, airPodsMaxArtworkRes("Green", 0))
        assertEquals(123, airPodsMaxArtworkRes(null, 123))
    }

    @Test
    fun ignoresBleTimestampOnlyChanges() {
        val status = BLEManager.AirPodsStatus(address = "00:00:00:00:00:00", lastSeen = 1)

        assertEquals(
            false,
            BLEManager.hasMeaningfulStatusChange(status, status.copy(lastSeen = 2))
        )
        assertEquals(
            true,
            BLEManager.hasMeaningfulStatusChange(status, status.copy(color = "Midnight"))
        )
    }
}
