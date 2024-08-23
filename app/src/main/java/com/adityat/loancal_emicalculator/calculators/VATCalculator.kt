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
fun VATCalculator() {
    var initialAmount by remember { mutableStateOf("") }
    var vatRate by remember { mutableStateOf("") }
    var vatOperation by remember { mutableStateOf("Add VAT") }

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
            value = vatRate,
            onValueChange = { vatRate = it },
            label = { Text("VAT Rate (%)") },
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
                selected = vatOperation == "Add VAT",
                onClick = { vatOperation = "Add VAT" }
            )
            Text(
                text = "Add VAT",
                modifier = Modifier.clickable { vatOperation = "Add VAT" }
            )
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF0174A3),
                    unselectedColor = Color.Gray,
                    disabledColor = Color.LightGray
                ),
                selected = vatOperation == "Remove VAT",
                onClick = { vatOperation = "Remove VAT" }
            )
            Text(
                text = "Remove VAT",
                modifier = Modifier.clickable { vatOperation = "Remove VAT" }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val vatResult = calculateVAT(
            initialAmount = initialAmount.toDoubleOrNull() ?: 0.0,
            vatRate = vatRate.toDoubleOrNull() ?: 0.0,
            vatOperation = vatOperation
        )

        Row(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .border(3.dp, color = Color.Black)
                .background(Color(0xffEDF7F6)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Original Cost: ₹${"%.2f".format(vatResult.originalCost)}",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .border(3.dp, color = Color.Black)
                .background(Color(0xffEDF7F6)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "VAT Price: ₹${"%.2f".format(vatResult.vatPrice)}",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .border(3.dp, color = Color.Black)
                .background(Color(0xffEDF7F6)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Net Price: ₹${"%.2f".format(vatResult.netPrice)}",
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

data class VATResult(
    val originalCost: Double,
    val vatPrice: Double,
    val netPrice: Double
)

fun calculateVAT(
    initialAmount: Double,
    vatRate: Double,
    vatOperation: String
): VATResult {
    return if (vatOperation == "Add VAT") {
        val vatPrice = initialAmount * vatRate / 100
        val netPrice = initialAmount + vatPrice
        VATResult(initialAmount, vatPrice, netPrice)
    } else {
        val netPrice = initialAmount / (1 + vatRate / 100)
        val vatPrice = initialAmount - netPrice
        VATResult(netPrice, vatPrice, initialAmount)
    }
}
