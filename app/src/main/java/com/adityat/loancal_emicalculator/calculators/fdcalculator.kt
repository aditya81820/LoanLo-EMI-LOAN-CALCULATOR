package com.adityat.loancal_emicalculator.calculators

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
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

@Composable
fun FDCalculator() {
    var depositAmount by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }
    var periodYears by remember { mutableStateOf("") }
    var periodMonths by remember { mutableStateOf("") }
    var periodDays by remember { mutableStateOf("") }
    var depositType by remember { mutableStateOf("Reinvestment") }

    val deposit = listOf(
        "Quarterly Payout",
        "Monthly Payout",
        "Short Term"
    )
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.padding(16.dp).verticalScroll(scrollState)) {
        OutlinedTextField(
            value = depositAmount,
            onValueChange = { depositAmount = it },
            label = { Text("Deposit Amount") },
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
                value = periodYears,
                onValueChange = { periodYears = it },
                label = { Text("Years") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = periodMonths,
                onValueChange = { periodMonths = it },
                label = { Text("Months") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = periodDays,
                onValueChange = { periodDays = it },
                label = { Text("Days") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            backgroundColor = Color(0xff0174A3)
        ) {
            DropdownMenu(
                selectedItem = depositType,
                items = deposit,
                onItemSelected = { depositType = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val (maturityAmount, totalInvestment, maturityDate) = calculateMaturityAmount(
            depositAmount.toDoubleOrNull() ?: 0.0,
            interestRate.toDoubleOrNull() ?: 0.0,
            periodYears.toIntOrNull() ?: 0,
            periodMonths.toIntOrNull() ?: 0,
            periodDays.toIntOrNull() ?: 0,
            depositType
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
            Text("Maturity Amount: $maturityAmount" , fontFamily = FontFamily.Monospace , fontSize = 16.sp)

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
            Text("Total Investment: $totalInvestment", fontFamily = FontFamily.Monospace , fontSize = 16.sp)

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
            Text("Maturity Date: $maturityDate", fontFamily = FontFamily.Monospace , fontSize = 16.sp)

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

        val depositAmountValue = depositAmount.toDoubleOrNull() ?: 0.0
        val totalInterest = maturityAmount - depositAmountValue

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CustomPieChart(
                depositAmount = depositAmountValue,
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
    }
}

@Composable
fun DropdownMenu(selectedItem: String, items: List<String>, onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier.clickable {
                expanded = true
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(selectedItem, color = Color.White)

                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "" , tint = Color.White)
            }
            androidx.compose.material.DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onItemSelected(item)
                    }) {
                        Text(item)
                    }
                }
            }
        }
    }

}

fun calculateMaturityAmount(
    depositAmount: Double,
    interestRate: Double,
    years: Int,
    months: Int,
    days: Int,
    depositType: String
): Triple<Double, Double, String> {
    // Calculate total period in days
    val totalDays = years * 365 + months * 30 + days

    // Calculate maturity amount based on deposit type
    val maturityAmount = when (depositType) {
        "Reinvestment", "Cumulative" -> {
            val rate = interestRate / 100 / 365
            depositAmount * Math.pow(1 + rate, totalDays.toDouble())
        }

        "Quarterly Payout" -> {
            // Assuming quarterly compounding for simplicity
            val quarters = totalDays / 90
            val rate = interestRate / 100 / 4
            depositAmount * Math.pow(1 + rate, quarters.toDouble())
        }

        "Monthly Payout" -> {
            // Assuming monthly compounding for simplicity
            val months = totalDays / 30
            val rate = interestRate / 100 / 12
            depositAmount * Math.pow(1 + rate, months.toDouble())
        }

        "Short Term" -> {
            // Assuming simple interest for short term
            depositAmount * (1 + interestRate / 100 * totalDays / 365)
        }

        else -> depositAmount
    }

    val totalInvestment = depositAmount
    val maturityDate = calculateMaturityDate(years, months, days)

    return Triple(maturityAmount, totalInvestment, maturityDate)
}

fun calculateMaturityDate(years: Int, months: Int, days: Int): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.YEAR, years)
    calendar.add(Calendar.MONTH, months)
    calendar.add(Calendar.DAY_OF_YEAR, days)

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(calendar.time)
}


@Composable
fun CustomPieChart(depositAmount: Double, totalInterest: Double) {
    val hasDeposit = depositAmount > 0.0 // Check if deposit amount is entered

    if (hasDeposit) { // Only draw chart if deposit amount is entered
        val total = depositAmount + totalInterest
        val depositPercentage = if (total > 0) (depositAmount / total) * 100 else 0.0
        val interestPercentage = if (total > 0) (totalInterest / total) * 100 else 0.0

        Canvas(
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
        ) {
            // Calculate angles for the arc
            val depositAngle = (depositAmount / total) * 360f
            val interestAngle = (totalInterest / total) * 360f

            // Draw deposit arc
            drawArc(
                color = Color(0xff0174A3),
                startAngle = 0f,
                sweepAngle = depositAngle.toFloat(),
                useCenter = true
            )


            // Draw interest arc
            drawArc(
                color = Color(0xffF3BC00),
                startAngle = depositAngle.toFloat(),
                sweepAngle = interestAngle.toFloat(),
                useCenter = true
            )


            // Draw percentage labels
            drawPercentageLabel(
                label = "${depositPercentage.toInt()}%",
                startAngle = 0.0,
                sweepAngle = depositAngle,
                c = android.graphics.Color.WHITE
            )

            drawPercentageLabel(
                label = "${interestPercentage.toInt()}%",
                startAngle = depositAngle,
                sweepAngle = interestAngle,
                c = android.graphics.Color.BLACK
            )
        }
    } else {
        // Optionally display a message or placeholder when no deposit is entered
        Text("Enter a deposit amount to see the pie chart.")
    }
}


fun DrawScope.drawPercentageLabel(label: String, startAngle: Double, sweepAngle: Double, c: Int) {
    val radius = size.minDimension / 2
    val labelRadius = radius * 0.6 // Adjust this value for label positioning

    // Calculate angle for the center of the slice
    val midAngleRad = Math.toRadians((startAngle + sweepAngle / 2.0).toDouble())

    val labelX = (center.x + labelRadius * Math.cos(midAngleRad)).toFloat()
    val labelY = (center.y + labelRadius * Math.sin(midAngleRad)).toFloat()

    drawContext.canvas.nativeCanvas.drawText(
        label,
        labelX,
        labelY,
        android.graphics.Paint().apply {
            color = c
            textSize = 30f
            textAlign = android.graphics.Paint.Align.CENTER
        }
    )
}

