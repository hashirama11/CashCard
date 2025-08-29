package com.example.finanzas.ui.util

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import java.util.Calendar

@Composable
fun TimePickerDialog(
    context: Context,
    onCancel: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            onConfirm(selectedHour, selectedMinute)
        },
        hour,
        minute,
        true // 24 hour format
    )

    timePickerDialog.setOnCancelListener {
        onCancel()
    }

    timePickerDialog.show()
}
