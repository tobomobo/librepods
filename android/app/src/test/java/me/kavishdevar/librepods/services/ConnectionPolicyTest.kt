package me.kavishdevar.librepods.services

import org.junit.Assert.*
import org.junit.Test

class ConnectionPolicyTest {
    @Test fun manualDisconnectSurvivesAutomaticEventsUntilExplicitReconnect() {
        assertTrue(mayAutomaticallyConnect(true, "max", "max", null))
        repeat(5) { assertFalse(mayAutomaticallyConnect(true, "max", "max", "max")) }
        assertTrue(mayAutomaticallyConnect(true, "max", "max", null))
    }

    @Test fun oldHeadsetCannotReclaimAudioAfterSwitchingToPro() {
        assertFalse(mayAutomaticallyConnect(true, "max", "pro", null))
        assertTrue(mayAutomaticallyConnect(true, "pro", "pro", "max"))
    }

    @Test fun automaticConnectionOffBlocksBothModels() {
        for (device in listOf("max", "pro")) {
            assertFalse(mayAutomaticallyConnect(false, device, device, null))
        }
        assertFalse(mayAutomaticallyConnect(true, null, "pro", null))
        assertFalse(mayAutomaticallyConnect(true, "", "", null))
    }

    @Test fun deviceSnapshotsIncludeKeysAndModelButExcludeGlobalPhoneConfiguration() {
        for (key in listOf("IRK", "ENC_KEY", "name", "airpods_model_number", "airpods_max_color")) {
            assertTrue(key, isDeviceIdentityPreference(key))
        }
        for (key in listOf("self_mac_address", "mac_address", "head_gestures", "manually_disconnected_device")) {
            assertFalse(key, isDeviceIdentityPreference(key))
        }
    }
}
