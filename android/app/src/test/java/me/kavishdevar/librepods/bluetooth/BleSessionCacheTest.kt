package me.kavishdevar.librepods.bluetooth

import org.junit.Assert.*
import org.junit.Test

class BleSessionCacheTest {
    @Test fun switchingMaxToProClearsAllAdvertisementState() {
        val cache = BleSessionCache<String>()
        cache.select("max", "max-irk", "max-encryption")
        cache.statuses["max-rpa"] = "Disconnected"
        cache.processedAddresses.add("max-rpa")
        cache.caseBatteries["max-rpa"] = 80

        assertTrue(cache.select("pro", "pro-irk", "pro-encryption"))
        assertTrue(cache.statuses.isEmpty())
        assertTrue(cache.processedAddresses.isEmpty())
        assertTrue(cache.caseBatteries.isEmpty())
        assertTrue(cache.select("max", "max-irk", "max-encryption"))
        assertTrue(cache.statuses.isEmpty())
    }

    @Test fun repeatedStatusReadPreservesCurrentHeadset() {
        val cache = BleSessionCache<String>()
        cache.select("max", "irk", "key")
        cache.statuses["rpa"] = "Music"
        assertFalse(cache.select("max", "irk", "key"))
        assertEquals("Music", cache.statuses["rpa"])
    }

    @Test fun keyRotationAndIdentityRemovalInvalidateCachedData() {
        val cache = BleSessionCache<String>()
        cache.select("pro", "irk", "old-key")
        cache.statuses["rpa"] = "Music"
        assertTrue(cache.select("pro", "irk", "new-key"))
        assertTrue(cache.statuses.isEmpty())
        cache.statuses["rpa"] = "Idle"
        assertTrue(cache.select(null, null, null))
        assertTrue(cache.statuses.isEmpty())
    }
}
