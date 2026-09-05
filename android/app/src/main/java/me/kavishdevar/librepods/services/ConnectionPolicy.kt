package me.kavishdevar.librepods.services

internal fun mayAutomaticallyConnect(
    enabled: Boolean,
    targetAddress: String?,
    selectedAddress: String,
    manuallyDisconnectedAddress: String?
): Boolean = enabled && !targetAddress.isNullOrEmpty() &&
    targetAddress == selectedAddress && targetAddress != manuallyDisconnectedAddress

internal fun isDeviceIdentityPreference(key: String): Boolean =
    key == "name" || key == "IRK" || key == "ENC_KEY" || key.startsWith("airpods_")

internal fun mayHandleEarPlayback(
    selectedAddress: String,
    manuallyDisconnectedAddress: String?,
    automaticConnectionAllowed: Boolean,
    audioConnected: Boolean
): Boolean = selectedAddress.isNotEmpty() && selectedAddress != manuallyDisconnectedAddress &&
    (automaticConnectionAllowed || audioConnected)
