package com.adityat.loancal_emicalculator.calculators

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.pow

data class RDResult(
    val totalInvestment: Double,
    val totalInterest: Double,
    val maturityAmount: Double,
    val maturityDate: String
)

@Composable
fun RDCalculator() {
    var monthlyAmount by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }
    var period by remember { mutableStateOf("") }
    var periodType by remember { mutableStateOf("Months") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = monthlyAmount,
            onValueChange = { monthlyAmount = it },
            label = { Text("Monthly Amount") },
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
                value = period,
                onValueChange = { period = it },
                label = { Text("Period") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            RadioButtonWithLabel(
                selected = periodType == "Months",
                onClick = { periodType = "Months" },
                label = "Months"
            )

            Spacer(modifier = Modifier.width(8.dp))

            RadioButtonWithLabel(
                selected = periodType == "Years",
                onClick = { periodType = "Years" },
                label = "Years"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val rdResult = calculateRD(
            monthlyAmount.toDoubleOrNull() ?: 0.0,
            interestRate.toDoubleOrNull() ?: 0.0,
            period.toIntOrNull() ?: 0,
            periodType
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
            Text("Maturity Amount: ₹${"%.2f".format(rdResult.maturityAmount)}", fontFamily = FontFamily.Monospace, fontSize = 16.sp)
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
            Text("Total Investment: ₹${"%.2f".format(rdResult.totalInvestment)}", fontFamily = FontFamily.Monospace, fontSize = 16.sp)
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
            Text("Total Interest: ₹${"%.2f".format(rdResult.totalInterest)}", fontFamily = FontFamily.Monospace, fontSize = 16.sp)
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
            Text("Maturity Date: ${rdResult.maturityDate}", fontFamily = FontFamily.Monospace, fontSize = 16.sp)
        }



        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CustomPieChart(
                depositAmount = rdResult.totalInvestment,
                totalInterest = rdResult.totalInterest

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
            androidx.compose.material.Text(
                text = "Total Investment", modifier = Modifier
                    .padding(8.dp)
            )
            Box(
                modifier = Modifier
                    .size(10.dp) // Adjust the size as needed
                    .background(Color(0xffF3BC00))
                    .padding(8.dp)
            )
            androidx.compose.material.Text(
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

@Composable
fun RadioButtonWithLabel(selected: Boolean, onClick: () -> Unit, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label)
    }
}

fun calculateRD(
    monthlyAmount: Double,
    interestRate: Double,
    period: Int,
    periodType: String
): RDResult {
    val months = if (periodType == "Years") period * 12 else period

    // Using compound interest formula for RD
    val rate = interestRate / 100 / 12
    val maturityAmount = monthlyAmount * ((1 + rate).pow(months) - 1) * (1 + rate) / rate
    val totalInvestment = monthlyAmount * months
    val totalInterest = maturityAmount - totalInvestment

    val maturityDate = calculateMaturityDate(months)

    return RDResult(totalInvestment, totalInterest, maturityAmount, maturityDate)
}

fun calculateMaturityDate(months: Int): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MONTH, months)

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(calendar.time)
}
