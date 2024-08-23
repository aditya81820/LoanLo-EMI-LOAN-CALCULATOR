package com.adityat.loancal_emicalculator.calculators

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlin.math.pow

@Composable
fun EmiCalculator(){

    var principalText by remember { mutableStateOf(TextFieldValue("")) }
    var interestRateText by remember { mutableStateOf(TextFieldValue("")) }
    var tenureText by remember { mutableStateOf(TextFieldValue("")) }

    var monthlyEMI by remember { mutableStateOf(0.0) }
    var totalInterest by remember { mutableStateOf(0.0) }
    var totalPayment by remember { mutableStateOf(0.0) }
    var depositAmount by remember { mutableStateOf(0.0) }

    val verticalscrollstate = rememberScrollState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(verticalscrollstate),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        OutlinedTextField(
            value = principalText,
            onValueChange = { principalText = it },
            label = { Text("Principal Amount") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            value = interestRateText,
            onValueChange = { interestRateText = it },
            label = { Text("Annual Interest Rate (%)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            value = tenureText,
            onValueChange = { tenureText = it },
            label = { Text("Tenure (Years)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Button(
            onClick = {
                val principal = principalText.text.toDoubleOrNull() ?: 0.0
                val annualInterestRate = interestRateText.text.toDoubleOrNull() ?: 0.0
                val tenureInYears = tenureText.text.toIntOrNull() ?: 0

                depositAmount = principal
                val result = calculateEMI(principal, annualInterestRate, tenureInYears)
                monthlyEMI = result.first
                totalInterest = result.second
                totalPayment = principal + totalInterest
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate EMI")
        }

        Row(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color(0xffEDF7F6))
                .border(3.dp, color = Color.Black),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Monthly EMI: ₹${"%.2f".format(monthlyEMI)}",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp
            )

        }
        Row(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color(0xffEDF7F6))
                .border(3.dp, color = Color.Black),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Total Interest: ₹${"%.2f".format(totalInterest)}",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp
            )

        }
        Row(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color(0xffEDF7F6))
                .border(3.dp, color = Color.Black),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Total Payment: ₹${"%.2f".format(totalPayment)}",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp
            )

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CustomPieChart(
                depositAmount = depositAmount,
                totalInterest = totalInterest
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp) // Adjust the size as needed
                    .background(Color(0xff0174A3))

            )
            Text(
                text = "Deposit Amount", modifier = Modifier
                    .padding(8.dp)
            )
            Box(
                modifier = Modifier
                    .size(10.dp) // Adjust the size as needed
                    .background(Color(0xffF3BC00))
                    .padding(8.dp)
            )
            Text(
                text = "Total Interest", modifier = Modifier
                    .padding(8.dp)
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

fun calculateEMI(principal: Double, annualInterestRate: Double, tenureInYears: Int): Pair<Double, Double> {
    val monthlyInterestRate = annualInterestRate / 12 / 100
    val tenureInMonths = tenureInYears * 12

    val emi = if (monthlyInterestRate > 0) {
        (principal * monthlyInterestRate * (1 + monthlyInterestRate).pow(tenureInMonths.toDouble())) /
                ((1 + monthlyInterestRate).pow(tenureInMonths.toDouble()) - 1)
    } else {
        principal / tenureInMonths
    }

    val totalPayment = emi * tenureInMonths
    val totalInterest = totalPayment - principal

    return Pair(emi, totalInterest)
}