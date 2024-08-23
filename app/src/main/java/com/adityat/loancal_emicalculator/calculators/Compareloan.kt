package com.adityat.loancal_emicalculator.calculators

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlin.math.pow

@Composable
fun LoanComparisonComposable() {
    var principal1 by remember { mutableStateOf("") }
    var interestRate1 by remember { mutableStateOf("") }
    var years1 by remember { mutableStateOf("") }
    var months1 by remember { mutableStateOf("") }

    var principal2 by remember { mutableStateOf("") }
    var interestRate2 by remember { mutableStateOf("") }
    var years2 by remember { mutableStateOf("") }
    var months2 by remember { mutableStateOf("") }

    val emi1 = calculateEMI(
        principal1.toDoubleOrNull(),
        interestRate1.toDoubleOrNull(),
        years1.toIntOrNull(),
        months1.toIntOrNull()
    )
    val emi2 = calculateEMI(
        principal2.toDoubleOrNull(),
        interestRate2.toDoubleOrNull(),
        years2.toIntOrNull(),
        months2.toIntOrNull()
    )

    val totalPayment1 =
        emi1?.let { it * getTotalMonths(years1.toIntOrNull(), months1.toIntOrNull()) }
    val totalPayment2 =
        emi2?.let { it * getTotalMonths(years2.toIntOrNull(), months2.toIntOrNull()) }

    val interestPayable1 = totalPayment1?.minus(principal1.toDoubleOrNull() ?: 0.0)
    val interestPayable2 = totalPayment2?.minus(principal2.toDoubleOrNull() ?: 0.0)

    val verticalscrollstate = rememberScrollState()


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(verticalscrollstate)
    ) {

        Text(
            "Loan 1",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xff0174A3)
        )
        LoanInputFields(
            principal1,
            interestRate1,
            years1,
            months1,
            onPrincipalChange = { principal1 = it },
            onInterestRateChange = { interestRate1 = it },
            onYearsChange = { years1 = it },
            onMonthsChange = { months1 = it })

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Loan 2",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xff0174A3)
        )
        LoanInputFields(
            principal2,
            interestRate2,
            years2,
            months2,
            onPrincipalChange = { principal2 = it },
            onInterestRateChange = { interestRate2 = it },
            onYearsChange = { years2 = it },
            onMonthsChange = { months2 = it })

        Spacer(modifier = Modifier.height(32.dp))
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = {
                AdView(it).apply {
                    setAdSize(AdSize.SMART_BANNER)
                    adUnitId = "ca-app-pub-1838194983985161/1165697539"
                    loadAd(AdRequest.Builder().build())
                }
            })

        if (emi1 != null && emi2 != null) {
            Table4x3(
                cell1 = "",
                cell2 ="Loan 1" ,
                cell3 = "Loan 2",
                cell4 = "EMI",
                cell5 = " ₹%.2f".format(emi1) ,
                cell6 = " ₹%.2f".format(emi2),
                cell7 = "Interest Payable",
                cell8 = " ₹%.2f".format(interestPayable1),
                cell9 = " ₹%.2f".format(interestPayable2),
                cell10 = "Total Payment",
                cell11 = " ₹%.2f".format(totalPayment1),
                cell12 = " ₹%.2f".format(totalPayment2)
            )


        }

    }
}

@Composable
fun Table4x3(
    cell1: String, cell2: String, cell3: String,
    cell4: String, cell5: String, cell6: String,
    cell7: String, cell8: String, cell9: String,
    cell10: String, cell11: String, cell12: String
) {
    // Define common styles for cells and lines
    val cellModifier = Modifier

        .padding(8.dp)
        .size(80.dp)

        .aspectRatio(1f)

    val lineModifier = Modifier
        .background(Color.White)
        .fillMaxWidth()
        .height(1.dp)

    val fontWeightB = FontWeight.Bold
    val fontWeightN = FontWeight.Normal

    val fontsize1 = 20.sp
    val fontsize2 = 16.sp


    Box(
        modifier = Modifier.border(2.dp, color = Color.Black).background(Color(0xff0174A3))
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // First Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                TableCell(text = cell1, modifier = cellModifier,fontWeightB, fontsize1)
                TableCell(
                    text = cell2,
                    modifier = cellModifier,
                    fontWeightB = fontWeightB,
                    fontsize1 = fontsize1
                )
                TableCell(
                    text = cell3,
                    modifier = cellModifier,
                    fontWeightB = fontWeightB,
                    fontsize1 = fontsize1
                )
            }
            LineSeparator(modifier = lineModifier)

            // Second Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                TableCell(
                    text = cell4,
                    modifier = cellModifier,
                    fontWeightB = fontWeightB,
                    fontsize1 = fontsize1
                )
                TableCell(
                    text = cell5,
                    modifier = cellModifier,
                    fontWeightB = fontWeightN,
                    fontsize1 = fontsize2
                )
                TableCell(
                    text = cell6,
                    modifier = cellModifier,
                    fontWeightB = fontWeightN,
                    fontsize1 = fontsize2
                )
            }
            LineSeparator(modifier = lineModifier)

            // Third Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                TableCell(
                    text = cell7,
                    modifier = cellModifier,
                    fontWeightB = fontWeightB,
                    fontsize1 = fontsize1
                )
                TableCell(
                    text = cell8,
                    modifier = cellModifier,
                    fontWeightB = fontWeightN,
                    fontsize1 = fontsize2
                )
                TableCell(
                    text = cell9,
                    modifier = cellModifier,
                    fontWeightB = fontWeightN,
                    fontsize1 = fontsize2
                )
            }
            LineSeparator(modifier = lineModifier)

            // Fourth Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                TableCell(
                    text = cell10,
                    modifier = cellModifier,
                    fontWeightB = fontWeightB,
                    fontsize1 = fontsize1
                )
                TableCell(
                    text = cell11,
                    modifier = cellModifier,
                    fontWeightB = fontWeightN,
                    fontsize1 = fontsize2
                )
                TableCell(
                    text = cell12,
                    modifier = cellModifier,
                    fontWeightB = fontWeightN,
                    fontsize1 = fontsize2
                )
            }
        }
    }
}

@Composable
fun TableCell(text: String, modifier: Modifier, fontWeightB: FontWeight, fontsize1: TextUnit) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = fontsize1,
            color = Color.White,
            fontWeight = fontWeightB,

        )
    }
}

@Composable
fun LineSeparator(modifier: Modifier) {
    Box(modifier = modifier)
}

@Composable
fun LoanInputFields(
    principal: String,
    interestRate: String,
    years: String,
    months: String,
    onPrincipalChange: (String) -> Unit,
    onInterestRateChange: (String) -> Unit,
    onYearsChange: (String) -> Unit,
    onMonthsChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = principal,
            onValueChange = onPrincipalChange,
            label = { Text("Principal Amount") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

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
fun LoanComparisonResult(
    loanLabel: String,
    emi: Double,
    interestPayable: Double,
    totalPayment: Double
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("$loanLabel:", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text("EMI: ₹%.2f".format(emi), style = MaterialTheme.typography.bodyLarge)
        Text(
            "Interest Payable: ₹%.2f".format(interestPayable),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            "Total Payment: ₹%.2f".format(totalPayment),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

fun calculateEMI(principal: Double?, interestRate: Double?, years: Int?, months: Int?): Double? {
    if (principal == null || interestRate == null || (years == null && months == null)) return null

    val totalMonths = getTotalMonths(years, months)
    val monthlyInterestRate = interestRate / 12 / 100
    return (principal * monthlyInterestRate * (1 + monthlyInterestRate).pow(totalMonths)) /
            ((1 + monthlyInterestRate).pow(totalMonths) - 1)
}

fun getTotalMonths(years: Int?, months: Int?): Int {
    return (years ?: 0) * 12 + (months ?: 0)
}
