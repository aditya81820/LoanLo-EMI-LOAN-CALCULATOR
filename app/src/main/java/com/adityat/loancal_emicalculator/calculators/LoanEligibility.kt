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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
fun LoanEligibilityComposable() {
    var grossMonthlyIncome by remember { mutableStateOf("") }
    var totalMonthlyObligations by remember { mutableStateOf("") }
    var foir by remember { mutableStateOf(35) }  // Default FOIR set to 35
    var interestRate by remember { mutableStateOf("") }
    var years by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }

    val eligibleLoanAmount = calculateEligibleLoanAmount(
        grossMonthlyIncome.toDoubleOrNull(),
        totalMonthlyObligations.toDoubleOrNull(),
        foir.toDouble(),
        interestRate.toDoubleOrNull(),
        years.toIntOrNull(),
        months.toIntOrNull()
    )

    val emi = eligibleLoanAmount?.let {
        calculateEMI(
            it,
            interestRate.toDoubleOrNull(),
            years.toIntOrNull(),
            months.toIntOrNull()
        )
    }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        LoanEligibilityInputFields(
            grossMonthlyIncome,
            totalMonthlyObligations,
            foir,
            interestRate,
            years,
            months,
            onGrossMonthlyIncomeChange = { grossMonthlyIncome = it },
            onTotalMonthlyObligationsChange = { totalMonthlyObligations = it },
            onFoirChange = { foir = it },
            onInterestRateChange = { interestRate = it },
            onYearsChange = { years = it },
            onMonthsChange = { months = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (eligibleLoanAmount != null && emi != null) {
            LoanEligibilityResult(eligibleLoanAmount, emi)
        }
    }
}

@Composable
fun LoanEligibilityInputFields(
    grossMonthlyIncome: String,
    totalMonthlyObligations: String,
    foir: Int,
    interestRate: String,
    years: String,
    months: String,
    onGrossMonthlyIncomeChange: (String) -> Unit,
    onTotalMonthlyObligationsChange: (String) -> Unit,
    onFoirChange: (Int) -> Unit,
    onInterestRateChange: (String) -> Unit,
    onYearsChange: (String) -> Unit,
    onMonthsChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val foirOptions = (35..90 step 5).toList()

    Column {
        OutlinedTextField(
            value = grossMonthlyIncome,
            onValueChange = onGrossMonthlyIncomeChange,
            label = { Text("Gross Monthly Income") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

            )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = totalMonthlyObligations,
            onValueChange = onTotalMonthlyObligationsChange,
            label = { Text("Total Monthly Obligations") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

            )

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = foir.toString(),
                onValueChange = {},
                label = { Text("FOIR (%)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                enabled = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )

            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                foirOptions.forEach { option ->
                    DropdownMenuItem(text = { Text(text = option.toString())}, onClick = { onFoirChange(option)
                        expanded = false })

                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = interestRate,
            onValueChange = onInterestRateChange,
            label = { Text("Interest Rate (%)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = years,
                onValueChange = onYearsChange,
                label = { Text("Years") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = months,
                onValueChange = onMonthsChange,
                label = { Text("Months") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }
    }
}

@Composable
fun LoanEligibilityResult(eligibleLoanAmount: Double, emi: Double) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
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
                "Eligible Loan Amount: ₹%.2f".format(eligibleLoanAmount),
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
                "EMI: ₹%.2f".format(emi),
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

fun calculateEligibleLoanAmount(
    grossMonthlyIncome: Double?,
    totalMonthlyObligations: Double?,
    foir: Double?,
    interestRate: Double?,
    years: Int?,
    months: Int?
): Double? {
    if (grossMonthlyIncome == null || totalMonthlyObligations == null || foir == null || interestRate == null || (years == null && months == null)) return null

    val disposableIncome = grossMonthlyIncome * (foir / 100)
    val availableIncomeForLoan = disposableIncome - totalMonthlyObligations
    val totalMonths = getTotalMonths(years, months)
    val monthlyInterestRate = interestRate / 12 / 100

    // Using EMI formula rearranged to solve for Principal (P)
    return (availableIncomeForLoan * ((1 + monthlyInterestRate).pow(totalMonths) - 1)) /
            (monthlyInterestRate * (1 + monthlyInterestRate).pow(totalMonths))
}

