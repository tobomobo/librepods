package me.kavishdevar.librepods

import me.kavishdevar.librepods.bluetooth.AACPManager
import me.kavishdevar.librepods.bluetooth.AACPManager.Companion.ControlCommandIdentifiers
import me.kavishdevar.librepods.bluetooth.BLEManager
import me.kavishdevar.librepods.presentation.components.availableSerialNumbers
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
        assertEquals("Midnight", BLEManager.colorName(0x2D20, 0x12))
        assertEquals("Blue", BLEManager.colorName(0x1420, 0x03))
    }
}
