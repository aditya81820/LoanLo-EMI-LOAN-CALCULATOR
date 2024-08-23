package com.adityat.loancal_emicalculator.calculators

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ln
import kotlin.math.pow

@Composable
fun QuickCalculatot() {
    var calculationType by remember { mutableStateOf("EMI") }
    var principal by remember { mutableStateOf(50000.0) }
    var interestRate by remember { mutableStateOf(7.0) }
    var tenureInYears by remember { mutableStateOf(5) }
    var monthlyEMI by remember { mutableStateOf(0.0) }
    var totalInterest by remember { mutableStateOf(0.0) }
    var totalPayment by remember { mutableStateOf(0.0) }

    val verticalScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(verticalScrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Select Calculation Type", fontSize = 18.sp, color = Color.Black)

        RadioGroup(calculationType) { type ->
            calculationType = type
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (calculationType) {
            "EMI" -> {
                SliderWithLabels("Principal Amount", principal, 10000f..1000000f) { value ->
                    principal = value.toDouble()
                }
                SliderWithLabels("Annual Interest Rate (%)", interestRate, 0f..20f) { value ->
                    interestRate = value.toDouble()
                }
                SliderWithLabels("Tenure (Years)", tenureInYears.toDouble(), 1f..30f) { value ->
                    tenureInYears = value.toInt()
                }
            }
            "Amount" -> {
                SliderWithLabels("Monthly EMI", monthlyEMI, 1000f..100000f) { value ->
                    monthlyEMI = value.toDouble()
                }
                SliderWithLabels("Annual Interest Rate (%)", interestRate, 0f..20f) { value ->
                    interestRate = value.toDouble()
                }
                SliderWithLabels("Tenure (Years)", tenureInYears.toDouble(), 1f..30f) { value ->
                    tenureInYears = value.toInt()
                }
            }
            "Period" -> {
                SliderWithLabels("Principal Amount", principal, 10000f..1000000f) { value ->
                    principal = value.toDouble()
                }
                SliderWithLabels("Monthly EMI", monthlyEMI, 1000f..100000f) { value ->
                    monthlyEMI = value.toDouble()
                }
                SliderWithLabels("Annual Interest Rate (%)", interestRate, 0f..20f) { value ->
                    interestRate = value.toDouble()
                }
            }
            "Interest" -> {
                SliderWithLabels("Principal Amount", principal, 10000f..1000000f) { value ->
                    principal = value.toDouble()
                }
                SliderWithLabels("Monthly EMI", monthlyEMI, 1000f..100000f) { value ->
                    monthlyEMI = value.toDouble()
                }
                SliderWithLabels("Tenure (Years)", tenureInYears.toDouble(), 1f..30f) { value ->
                    tenureInYears = value.toInt()
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val result = when (calculationType) {
                    "EMI" -> calculateEMI(principal, interestRate, tenureInYears)
                    "Amount" -> calculateAmount(interestRate, tenureInYears, monthlyEMI)
                    "Period" -> calculatePeriod(principal, interestRate, monthlyEMI)
                    "Interest" -> calculateInterest(principal, interestRate, tenureInYears, monthlyEMI)
                    else -> Pair(0.0, 0.0)
                }
                monthlyEMI = result.first
                totalInterest = result.second
                totalPayment = principal + totalInterest
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate")
        }

        ResultsRow("Monthly EMI", monthlyEMI)
        ResultsRow("Total Interest", totalInterest)
        ResultsRow("Total Payment", totalPayment)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CustomPieChart(
                depositAmount = principal,
                totalInterest = totalInterest
            )
        }
    }
}

@Composable
fun RadioGroup(selectedType: String, onTypeChange: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        RadioButton(selected = selectedType == "EMI", onClick = { onTypeChange("EMI") })
        Text("Calculate EMI", modifier = Modifier.padding(start = 8.dp))

        RadioButton(selected = selectedType == "Amount", onClick = { onTypeChange("Amount") })
        Text("Calculate Amount", modifier = Modifier.padding(start = 8.dp))


    }
    Row(modifier = Modifier.fillMaxWidth()) {
        RadioButton(selected = selectedType == "Period", onClick = { onTypeChange("Period") })
        Text("Calculate Period", modifier = Modifier.padding(start = 8.dp))

        RadioButton(selected = selectedType == "Interest", onClick = { onTypeChange("Interest") })
        Text("Calculate Interest", modifier = Modifier.padding(start = 8.dp))
    }

}

@Composable
fun SliderWithLabels(label: String, value: Double, range: ClosedFloatingPointRange<Float>, onValueChange: (Float) -> Unit) {
    Text("$label: ${"%.2f".format(value)}", fontSize = 16.sp)
    Slider(
        value = value.toFloat(),
        onValueChange = { onValueChange(it) },
        valueRange = range,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ResultsRow(label: String, value: Double) {
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
            "$label: ₹${"%.2f".format(value)}",
            fontSize = 16.sp
        )
    }
}



fun calculateAmount(interestRate: Double, tenureInYears: Int, monthlyEMI: Double): Pair<Double, Double> {
    val monthlyInterestRate = interestRate / 12 / 100
    val tenureInMonths = tenureInYears * 12

    val principal = if (monthlyInterestRate > 0) {
        (monthlyEMI * ((1 + monthlyInterestRate).pow(tenureInMonths.toDouble()) - 1)) /
                (monthlyInterestRate * (1 + monthlyInterestRate).pow(tenureInMonths.toDouble()))
    } else {
        monthlyEMI * tenureInMonths
    }

    val totalPayment = monthlyEMI * tenureInMonths
    val totalInterest = totalPayment - principal

    return Pair(principal, totalInterest)
}


fun calculatePeriod(principal: Double, interestRate: Double, monthlyEMI: Double): Pair<Double, Double> {
    val monthlyInterestRate = interestRate / 12 / 100

    val tenureInMonths = if (monthlyInterestRate > 0) {
        (ln(monthlyEMI / (monthlyEMI - principal * monthlyInterestRate)) / ln(1 + monthlyInterestRate)).toInt()
    } else {
        (principal / monthlyEMI).toInt()
    }

    val tenureInYears = tenureInMonths / 12
    val totalPayment = monthlyEMI * tenureInMonths
    val totalInterest = totalPayment - principal

    return Pair(tenureInYears.toDouble(), totalInterest)
}

fun calculateInterest(principal: Double, annualInterestRate: Double, tenureInYears: Int, monthlyEMI: Double): Pair<Double, Double> {
    val monthlyInterestRate = annualInterestRate / 12 / 100
    val tenureInMonths = tenureInYears * 12

    val totalPayment = monthlyEMI * tenureInMonths
    val totalInterest = totalPayment - principal

    return Pair(totalInterest, totalPayment)
}
