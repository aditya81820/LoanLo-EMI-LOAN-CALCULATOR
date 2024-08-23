package com.adityat.loancal_emicalculator.calculators

import android.app.DatePickerDialog
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import java.util.Date
import java.util.Locale
import kotlin.math.pow

@Composable
fun SIPCalculator() {
    var investmentAmount by remember { mutableStateOf("") }
    var expectedRate by remember { mutableStateOf("") }
    var tenureMonths by remember { mutableStateOf("") }
    var firstEmiDate by remember { mutableStateOf("Select Date") }
    var showDatePicker by remember { mutableStateOf(false) }
    var results by remember { mutableStateOf<SIPResults?>(null) }

    val context = LocalContext.current

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
            value = expectedRate,
            onValueChange = { expectedRate = it },
            label = { Text("Expected Rate of Interest (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = tenureMonths,
            onValueChange = { tenureMonths = it },
            label = { Text("Tenure (Months)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(160.dp)
                    .clickable {
                        showDatePicker = true

                    }
                    .background(Color(0xff0174A3))
                    .padding(8.dp),


                ) {
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Text(firstEmiDate, color = Color.Black, fontFamily = FontFamily.Monospace)
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")

                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            results = calculateSIP(
                investmentAmount.toDoubleOrNull() ?: 0.0,
                expectedRate.toDoubleOrNull() ?: 0.0,
                tenureMonths.toIntOrNull() ?: 0,
                firstEmiDate
            )
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Calculate", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        results?.let {
            ResultCard(title = "Total Investment Amount", value = it.totalInvestment)
            ResultCard(title = "Total Interest Value", value = it.totalInterest)
            ResultCard(title = "Maturity Date", value = it.maturityDate)
            ResultCard(title = "Maturity Value", value = it.maturityValue)
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

    // Show the DatePickerDialog if showDatePicker is true
    LaunchedEffect(showDatePicker) {
        if (showDatePicker) {
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val calendar = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    firstEmiDate =
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
                    showDatePicker = false
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}

data class SIPResults(
    val totalInvestment: String,
    val totalInterest: String,
    val maturityDate: String,
    val maturityValue: String
)

fun calculateSIP(
    investmentAmount: Double,
    expectedRate: Double,
    tenureMonths: Int,
    firstEmiDate: String
): SIPResults {
    val monthlyRate = expectedRate / 100 / 12
    val totalInvestment = investmentAmount * tenureMonths
    val maturityValue = investmentAmount * (((1 + monthlyRate).pow(tenureMonths) - 1) / monthlyRate)
    val totalInterest = maturityValue - totalInvestment

    val maturityDate = calculateMaturityDate(firstEmiDate, tenureMonths)

    return SIPResults(
        String.format("%.2f", totalInvestment),
        String.format("%.2f", totalInterest),
        maturityDate,
        String.format("%.2f", maturityValue)
    )
}

@Composable
fun ResultCard(title: String, value: String) {
    Card(
        backgroundColor = Color(0xffEDF7F6),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(3.dp, color = Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontSize = 16.sp, fontFamily = FontFamily.Monospace)
            Text(value, fontSize = 16.sp, fontFamily = FontFamily.Monospace)
        }
    }
}

fun calculateMaturityDate(startDate: String, months: Int): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    calendar.time = dateFormat.parse(startDate) ?: Date()
    calendar.add(Calendar.MONTH, months)
    return dateFormat.format(calendar.time)
}
