package com.example.finanzas.ui.util

import android.app.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@Composable
fun TimePickerDialog(
    onCancel: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _, selectedHour: Int, selectedMinute: Int ->
                onConfirm(selectedHour, selectedMinute)
            },
            hour,
            minute,
            true // 24 hour format
        ).apply {
            setOnDismissListener {
                onCancel()
            }
        }
    }

    DisposableEffect(Unit) {
        timePickerDialog.show()
        onDispose {
            timePickerDialog.dismiss()
        }
    }
}
