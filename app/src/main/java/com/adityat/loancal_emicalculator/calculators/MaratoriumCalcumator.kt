package com.adityat.loancal_emicalculator.calculators

import androidx.compose.foundation.border
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun MoratoriumCalculator() {
    var loanAmount by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }
    var period by remember { mutableStateOf("") }
    var installmentPaid by remember { mutableStateOf("") }
    var moratoriumPeriod by remember { mutableStateOf("") }
    var noMoratoriumResults by remember { mutableStateOf(CalculationResults()) }
    var moratoriumResults by remember { mutableStateOf(CalculationResults()) }
    var selectedOption by remember { mutableStateOf("NoChange") }

    // Scroll state
    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(scrollState) // Enable vertical scrolling
    ) {
        // Input fields
        OutlinedTextField(
            value = loanAmount,
            onValueChange = { loanAmount = it },
            label = { Text("Loan Amount") },
            modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )

        )

        OutlinedTextField(
            value = interestRate,
            onValueChange = { interestRate = it },
            label = { Text("Interest Rate") },
            modifier = Modifier.fillMaxWidth()
            , keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        OutlinedTextField(
            value = period,
            onValueChange = { period = it },
            label = { Text("Period (Months)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        OutlinedTextField(
            value = installmentPaid,
            onValueChange = { installmentPaid = it },
            label = { Text("Installment Paid") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        OutlinedTextField(
            value = moratoriumPeriod,
            onValueChange = { moratoriumPeriod = it },
            label = { Text("Moratorium Period (Months)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Radio Buttons for options
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedOption == "NoChange",
                onClick = { selectedOption = "NoChange" }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("No Change in Monthly EMI")

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = selectedOption == "TenureChange",
                onClick = { selectedOption = "TenureChange" }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("No Change in Loan Tenure")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Calculate results based on input values
                noMoratoriumResults = calculateNoMoratoriumResults(
                    loanAmount.toDoubleOrNull() ?: 0.0,
                    interestRate.toDoubleOrNull() ?: 0.0,
                    period.toIntOrNull() ?: 0,
                    installmentPaid.toDoubleOrNull() ?: 0.0,
                    moratoriumPeriod.toIntOrNull() ?: 0
                )
                moratoriumResults = calculateMoratoriumResults(
                    loanAmount.toDoubleOrNull() ?: 0.0,
                    interestRate.toDoubleOrNull() ?: 0.0,
                    period.toIntOrNull() ?: 0,
                    installmentPaid.toDoubleOrNull() ?: 0.0,
                    moratoriumPeriod.toIntOrNull() ?: 0,
                    selectedOption
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary, // Background color
                contentColor = Color.White // Text color
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Results Grid
        ResultsGrid(
            noMoratoriumResults = noMoratoriumResults,
            moratoriumResults = moratoriumResults
        )
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
fun ResultsGrid(noMoratoriumResults: CalculationResults, moratoriumResults: CalculationResults) {
    val headers = listOf("Total Principal", "Monthly EMI", "Tenure (Months)", "Total Interest", "Total Payment")
    val noMoratoriumValues = listOf(
        noMoratoriumResults.principal,
        noMoratoriumResults.emi,
        noMoratoriumResults.tenure,
        noMoratoriumResults.interest,
        noMoratoriumResults.total
    )
    val moratoriumValues = listOf(
        moratoriumResults.principal,
        moratoriumResults.emi,
        moratoriumResults.tenure,
        moratoriumResults.interest,
        moratoriumResults.total
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.border(1.dp, Color.Gray).padding(8.dp)) {
            Text(" ", modifier = Modifier.width(160.dp).padding(8.dp))
            Text("No Moratorium", fontSize = 14.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f).padding(4.dp))
            Text("Moratorium Revised EMI",fontSize = 14.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f).padding(4.dp))
        }
        headers.forEachIndexed { index, header ->
            Row(Modifier.border(1.dp, Color.Gray).padding(8.dp)) {
                Text(header, modifier = Modifier.width(200.dp).padding(8.dp), fontSize = 14.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                Text(noMoratoriumValues[index].toString(), modifier = Modifier.weight(1f).padding(8.dp))
                Text(moratoriumValues[index].toString(), modifier = Modifier.weight(1f).padding(8.dp))
            }
        }
    }
}

data class CalculationResults(
    val principal: Double = 0.0,
    val emi: Double = 0.0,
    val tenure: Int = 0,
    val interest: Double = 0.0,
    val total: Double = 0.0
)

// Example calculation logic
fun calculateNoMoratoriumResults(loanAmount: Double, interestRate: Double, period: Int, installmentPaid: Double, moratoriumPeriod: Int): CalculationResults {
    // Dummy implementation: Replace with actual calculation
    return CalculationResults(
        principal = loanAmount,
        emi = installmentPaid,
        tenure = period,
        interest = (loanAmount * interestRate / 100),
        total = loanAmount + (loanAmount * interestRate / 100)
    )
}

// Example calculation logic
fun calculateMoratoriumResults(loanAmount: Double, interestRate: Double, period: Int, installmentPaid: Double, moratoriumPeriod: Int, selectedOption: String): CalculationResults {
    // Dummy implementation: Replace with actual calculation
    return CalculationResults(
        principal = loanAmount,
        emi = installmentPaid, // This should be recalculated based on moratorium logic
        tenure = period, // This should be adjusted based on moratorium logic
        interest = (loanAmount * interestRate / 100),
        total = loanAmount + (loanAmount * interestRate / 100)
    )
}
