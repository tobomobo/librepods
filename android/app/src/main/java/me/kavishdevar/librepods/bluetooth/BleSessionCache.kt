package me.kavishdevar.librepods.bluetooth

/** Cached advertisements belong to one selected headset and one proximity key pair. */
internal class BleSessionCache<T> {
    private var identity: List<String?> = emptyList()
    val statuses = mutableMapOf<String, T>()
    val processedAddresses = mutableSetOf<String>()
    val caseBatteries = mutableMapOf<String, Int>()

    fun select(address: String?, irk: String?, encryptionKey: String?): Boolean {
        val next = listOf(address, irk, encryptionKey)
        if (identity == next) return false
        identity = next
        statuses.clear()
        processedAddresses.clear()
        caseBatteries.clear()
        return true
    }
}
