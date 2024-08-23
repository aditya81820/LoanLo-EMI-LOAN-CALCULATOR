package com.adityat.loancal_emicalculator.calculators



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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

@Composable
fun AdvanceEMICalculator() {
    var loanAmount by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }
    var periods by remember { mutableStateOf("12") } // Default to 12 months
    var processingFeesPercentage by remember { mutableStateOf("") }
    var gstOnInterest by remember { mutableStateOf("") }

    var monthlyEMI by remember { mutableStateOf("0") }
    var totalInterest by remember { mutableStateOf("0") }
    var gstOnProcessingFees by remember { mutableStateOf("0") }
    var totalPayment by remember { mutableStateOf("0") }

    val verticalscrollstate = rememberScrollState()

    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(verticalscrollstate)) {
        // Input Fields
        OutlinedTextField(
            value = loanAmount,
            onValueChange = { loanAmount = it },
            label = { Text("Loan Amount") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = interestRate,
            onValueChange = { interestRate = it },
            label = { Text("Interest Rate %") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Period Type Selection
        var periodType by remember { mutableStateOf("Months") }
        Row(modifier = Modifier.fillMaxWidth()) {
            RadioButton(
                selected = periodType == "Months",
                onClick = { periodType = "Months" }
            )
            Text("Months")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = periodType == "Years",
                onClick = { periodType = "Years" }
            )
            Text("Years")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = periods,
            onValueChange = { periods = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Periods (${if (periodType == "Months") "Months" else "Years"})") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = processingFeesPercentage,
            onValueChange = { processingFeesPercentage = it },
            label = { Text("Processing Fees (%)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = gstOnInterest,
            onValueChange = { gstOnInterest = it },
            label = { Text("GST on Interest %") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Calculate Button
        val P = loanAmount.toDoubleOrNull() ?: 0.0
        Button(onClick = {

            val r = (interestRate.toDoubleOrNull() ?: 0.0) / 12 / 100
            val n =
                periods.toDoubleOrNull()?.let { if (periodType == "Years") it * 12 else it } ?: 0.0
            val feePercentage = processingFeesPercentage.toDoubleOrNull() ?: 0.0
            val gstInterestRate = (gstOnInterest.toDoubleOrNull() ?: 0.0) / 100

            // Monthly EMI Calculation
            val emi = (P * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1)
            monthlyEMI = String.format("%.2f", emi)

            // Total Interest Calculation
            val totalInterestValue = (emi * n) - P
            totalInterest = String.format("%.2f", totalInterestValue)

            // Processing Fees Calculation
            val processingFees = P * feePercentage / 100
            val gstProcessingFees = processingFees * gstInterestRate
            gstOnProcessingFees = String.format("%.2f", gstProcessingFees)

            // Total Payment Calculation
            val totalPaymentValue = (emi * n) + gstProcessingFees + processingFees
            totalPayment = String.format("%.2f", totalPaymentValue)
        }) {
            Text("Calculate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Output Table
        Column(modifier = Modifier.fillMaxWidth()) {
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
                Text("Monthly EMI : ", fontFamily = FontFamily.Monospace, fontSize = 16.sp)
                Text(monthlyEMI, fontFamily = FontFamily.Monospace, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))

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
                Text("Total Interest : " , fontFamily = FontFamily.Monospace, fontSize = 16.sp)
                Text(totalInterest, fontFamily = FontFamily.Monospace, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))

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
                Text("Processing Fees : ", fontFamily = FontFamily.Monospace, fontSize = 16.sp)
                Text(
                    String.format(
                        "%.2f",
                        P * (processingFeesPercentage.toDoubleOrNull() ?: 0.0) / 100
                    ), fontFamily = FontFamily.Monospace, fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

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
                Text("GST on Processing Fees : ", fontFamily = FontFamily.Monospace, fontSize = 16.sp)
                Text(gstOnProcessingFees, fontFamily = FontFamily.Monospace, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))

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
                Text("Total Payment : ", fontFamily = FontFamily.Monospace, fontSize = 16.sp)
                Text(totalPayment, fontFamily = FontFamily.Monospace, fontSize = 16.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
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
