package com.example.finanzas

import androidx.benchmark.macro.ExperimentalMacrobenchmarkApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalMacrobenchmarkApi
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun startup() = baselineProfileRule.collect(
        packageName = "com.example.finanzas",
        profileBlock = {
            startActivityAndWait()
            // TODO: Add more user journeys here, like navigating to other screens.
            // For now, we are just profiling the startup.
        }
    )
}
