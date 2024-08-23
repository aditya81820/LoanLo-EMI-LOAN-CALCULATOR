package com.adityat.loancal_emicalculator.calculators

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun GSTCalculator() {
    var initialAmount by remember { mutableStateOf("") }
    var gstRate by remember { mutableStateOf("") }
    var gstOperation by remember { mutableStateOf("Add GST") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = initialAmount,
            onValueChange = { initialAmount = it },
            label = { Text("Initial Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = gstRate,
            onValueChange = { gstRate = it },
            label = { Text("GST Rate (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF0174A3),
                    unselectedColor = Color.Gray,
                    disabledColor = Color.LightGray
                ),
                selected = gstOperation == "Add GST",
                onClick = { gstOperation = "Add GST" }
            )
            Text(
                text = "Add GST",
                modifier = Modifier.clickable { gstOperation = "Add GST" }
            )
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF0174A3),
                    unselectedColor = Color.Gray,
                    disabledColor = Color.LightGray
                ),
                selected = gstOperation == "Remove GST",
                onClick = { gstOperation = "Remove GST" }
            )
            Text(
                text = "Remove GST",
                modifier = Modifier.clickable { gstOperation = "Remove GST" }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val gstResult = calculateGST(
            initialAmount = initialAmount.toDoubleOrNull() ?: 0.0,
            gstRate = gstRate.toDoubleOrNull() ?: 0.0,
            gstOperation = gstOperation
        )

        Row(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(8.dp).border(3.dp, color = Color.Black)
                .background(Color(0xffEDF7F6)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Initial Amount: ₹${"%.2f".format(gstResult.initialAmount)}",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(8.dp).border(3.dp, color = Color.Black)
                .background(Color(0xffEDF7F6)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "GST Amount: ₹${"%.2f".format(gstResult.gstAmount)}",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(8.dp).border(3.dp, color = Color.Black)
                .background(Color(0xffEDF7F6)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Total Amount: ₹${"%.2f".format(gstResult.totalAmount)}",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp
            )
        }
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = {
                AdView(it).apply {
                    setAdSize(AdSize.SMART_BANNER)
                    adUnitId = "ca-app-pub-1838194983985161/1165697539"
                    loadAd(AdRequest.Builder().build())
                }
            })
    }
}

data class GSTResult(
    val initialAmount: Double,
    val gstAmount: Double,
    val totalAmount: Double
)

fun calculateGST(
    initialAmount: Double,
    gstRate: Double,
    gstOperation: String
): GSTResult {
    return if (gstOperation == "Add GST") {
        val gstAmount = initialAmount * gstRate / 100
        val totalAmount = initialAmount + gstAmount
        GSTResult(initialAmount, gstAmount, totalAmount)
    } else {
        val gstAmount = initialAmount - initialAmount / (1 + gstRate / 100)
        val totalAmount = initialAmount / (1 + gstRate / 100)
        GSTResult(totalAmount, gstAmount, initialAmount)
    }
}
