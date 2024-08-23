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
import kotlin.math.pow

@Composable
fun SimpleCompoundInterestCalculator() {
    var amount by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }
    var years by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }
    var days by remember { mutableStateOf("") }
    var interestType by remember { mutableStateOf("Simple Interest") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = interestRate,
            onValueChange = { interestRate = it },
            label = { Text("Interest Rate (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                value = years,
                onValueChange = { years = it },
                label = { Text("Years") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = months,
                onValueChange = { months = it },
                label = { Text("Months") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = days,
                onValueChange = { days = it },
                label = { Text("Days") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF0174A3), // Set the color for the selected state
                    unselectedColor = Color.Gray,     // Set the color for the unselected state
                    disabledColor = Color.LightGray   // Set the color for the disabled state
                ),
                selected = interestType == "Simple Interest",
                onClick = { interestType = "Simple Interest" }
            )
            Text(
                text = "Simple Interest",
                modifier = Modifier.clickable { interestType = "Simple Interest" }
            )
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF0174A3), // Set the color for the selected state
                    unselectedColor = Color.Gray,     // Set the color for the unselected state
                    disabledColor = Color.LightGray   // Set the color for the disabled state
                ),
                selected = interestType == "Compound Interest",
                onClick = { interestType = "Compound Interest" }
            )
            Text(
                text = "Compound Interest",
                modifier = Modifier.clickable { interestType = "Compound Interest" }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val result = calculateInterest(
            principal = amount.toDoubleOrNull() ?: 0.0,
            interestRate = interestRate.toDoubleOrNull() ?: 0.0,
            years = years.toIntOrNull() ?: 0,
            months = months.toIntOrNull() ?: 0,
            days = days.toIntOrNull() ?: 0,
            interestType = interestType
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
                "Principal Amount: ₹${"%.2f".format(result.principal)}",
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
                "Interest Amount: ₹${"%.2f".format(result.interest)}",
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
                "Total Amount: ₹${"%.2f".format(result.total)}",
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

data class InterestResult(
    val principal: Double,
    val interest: Double,
    val total: Double
)

fun calculateInterest(
    principal: Double,
    interestRate: Double,
    years: Int,
    months: Int,
    days: Int,
    interestType: String
): InterestResult {
    val totalYears = years + months / 12.0 + days / 365.0
    return if (interestType == "Simple Interest") {
        val interest = principal * interestRate * totalYears / 100
        InterestResult(principal, interest, principal + interest)
    } else {
        val total = principal * (1 + interestRate / 100).pow(totalYears)
        InterestResult(principal, total - principal, total)
    }
}
