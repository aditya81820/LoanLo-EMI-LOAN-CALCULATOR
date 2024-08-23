package com.adityat.loancal_emicalculator.calculators


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlin.math.pow


@Composable
fun PPFCalculator() {
    var investmentAmount by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }
    var period by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("Yearly") }

    val frequencyOptions = listOf("Yearly", "Quarterly", "Monthly", "Half Yearly")

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = investmentAmount,
            onValueChange = { investmentAmount = it },
            label = { Text("Investment Amount") },
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

        OutlinedTextField(
            value = period,
            onValueChange = { period = it },
            label = { Text("Period (Years)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Card(
            backgroundColor = Color(0xff0174A3)
        ) {
            DropdownMenu(
                selectedItem = frequency,
                items = frequencyOptions,
                onItemSelected = { frequency = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val ppfResult = calculatePPF(
            investmentAmount.toDoubleOrNull() ?: 0.0,
            interestRate.toDoubleOrNull() ?: 0.0,
            period.toIntOrNull() ?: 0,
            frequency
        )

        Row(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color(0xffEDF7F6)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Maturity Amount: ₹${"%.2f".format(ppfResult.maturityAmount)}",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color(0xffEDF7F6)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Total Investment: ₹${"%.2f".format(ppfResult.totalInvestment)}",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color(0xffEDF7F6)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Total Interest: ₹${"%.2f".format(ppfResult.totalInterest)}",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CustomPieChart(
                depositAmount = ppfResult.totalInvestment,
                totalInterest = ppfResult.totalInterest
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

data class PPFResult(
    val totalInvestment: Double,
    val totalInterest: Double,
    val maturityAmount: Double
)

fun calculatePPF(
    investmentAmount: Double,
    interestRate: Double,
    period: Int,
    frequency: String
): PPFResult {
    val compoundingFrequency = when (frequency) {
        "Yearly" -> 1
        "Quarterly" -> 4
        "Monthly" -> 12
        "Half Yearly" -> 2
        else -> 1
    }

    val n = period * compoundingFrequency
    val r = interestRate / 100 / compoundingFrequency
    val maturityAmount = investmentAmount * ((1 + r).pow(n) - 1) / r
    val totalInvestment = investmentAmount * n
    val totalInterest = maturityAmount - totalInvestment

    return PPFResult(totalInvestment, totalInterest, maturityAmount)
}
