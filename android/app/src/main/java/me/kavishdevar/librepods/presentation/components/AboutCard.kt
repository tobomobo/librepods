/*
    LibrePods - AirPods liberated from Apple’s ecosystem
    Copyright (C) 2025 LibrePods contributors

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

@file:OptIn(ExperimentalEncodingApi::class)

package me.kavishdevar.librepods.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.presentation.theme.DesignSystem
import me.kavishdevar.librepods.presentation.theme.LocalDesignSystem
import kotlin.io.encoding.ExperimentalEncodingApi

@Composable
fun AboutCard(
    modelName: String,
    actualModel: String,
    serialNumbers: List<String>,
    version: String?,
    navigateToVersion: () -> Unit
) {
    val displayedSerialNumbers = availableSerialNumbers(serialNumbers).map { (index, serialNumber) ->
        when (LocalDesignSystem.current) {
            DesignSystem.Apple -> when (index) {
                1 -> "􀀛 $serialNumber"
                2 -> "􀀧 $serialNumber"
                else -> serialNumber
            }

            DesignSystem.Material -> when (index) {
                1 -> "${stringResource(R.string.left)} $serialNumber"
                2 -> "${stringResource(R.string.right)} $serialNumber"
                else -> serialNumber
            }
        }
    }

    val serialNumber = remember(displayedSerialNumbers) { mutableIntStateOf(0) }

    StyledList (title = stringResource(R.string.about)) {
        StyledListItem(
            name = stringResource(R.string.model_name),
            description = modelName
        )

        StyledListItem(
            name = stringResource(R.string.model_number),
            description = actualModel
        )

        StyledListItem (
            name = stringResource(R.string.serial_number),
            description = displayedSerialNumbers.getOrNull(serialNumber.intValue),
            onClick = if (displayedSerialNumbers.size > 1) {
                { serialNumber.intValue = (serialNumber.intValue + 1) % displayedSerialNumbers.size }
            } else null
        )

        StyledListItem(
            name = stringResource(R.string.version),
            description = version,
            onClick = navigateToVersion,
        )
    }
}

internal fun availableSerialNumbers(serialNumbers: List<String>) =
    serialNumbers.mapIndexedNotNull { index, serialNumber ->
        (index to serialNumber).takeIf {
            index == 0 || serialNumber.isNotBlank() && serialNumber != "0"
        }
    }
