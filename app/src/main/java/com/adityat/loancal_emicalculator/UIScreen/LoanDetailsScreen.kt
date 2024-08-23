package com.adityat.loancal_emicalculator.UIScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.adityat.loancal_emicalculator.Data.DatabaseBuilder
import com.adityat.loancal_emicalculator.Data.LoanProfile
import com.adityat.loancal_emicalculator.calculators.CustomPieChart
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoanDetailsScreen(navController: NavController, profileId: Int?) {
    val context = LocalContext.current
    var profile by remember { mutableStateOf<LoanProfile?>(null) }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(profileId) {
        if (profileId != null) {
            try {
                val db = DatabaseBuilder.getInstance(context)
                withContext(Dispatchers.IO) {
                    profile = db.loanProfileDao().getProfileById(profileId)
                }
            } catch (e: Exception) {
                errorMessage = "Error fetching profile: ${e.message}"
            } finally {
                loading = false
            }
        } else {
            errorMessage = "Invalid profile ID"
            loading = false
        }
    }

    if (loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Loading...")
        }
    } else if (errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = errorMessage ?: "Unknown error")
        }
    } else {
        profile?.let { profile ->
            val formattedDate = try {
                val instant = Instant.ofEpochMilli(profile.startDate)
                val zoneId = ZoneId.systemDefault()
                val zonedDateTime = instant.atZone(zoneId)
                val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
                zonedDateTime.format(formatter)
            } catch (e: Exception) {
                "Invalid Date" // Handle conversion errors
            }

            // Calculate EMI
            val emi = calculateEmi(profile)

            // Calculate total interest
            val totalInterest = emi * profile.loanTenureMonths - profile.principalAmount

            // Total Amount = Principal + Total Interest
            val totalAmount = profile.principalAmount + totalInterest

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                LoanDetailRow(label = "Loan Type", value = profile.loanType)
                LoanDetailRow(label = "Profile Name", value = profile.profileName)
                LoanDetailRow(label = "Principal Amount", value = profile.principalAmount.toString())
                LoanDetailRow(label = "Annual Interest Rate", value = "${profile.annualInterestRate}%")
                LoanDetailRow(label = "Loan Tenure", value = "${profile.loanTenureMonths} months")
                LoanDetailRow(label = "Processing Fees", value = "${profile.processingFees}")
                LoanDetailRow(label = "Start Date", value = formattedDate)
                LoanDetailRow(label = "EMI", value = "%.2f".format(emi))
                LoanDetailRow(label = "Total Interest", value = "%.2f".format(totalInterest))
                LoanDetailRow(label = "Total Amount", value = "%.2f".format(totalAmount))

                // Donut Chart
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalArrangement = Arrangement.Center) {
                    CustomPieChart(
                        depositAmount = profile.principalAmount,
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
                        text = "Principal", modifier = Modifier
                            .padding(8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(10.dp) // Adjust the size as needed
                            .background(Color(0xffF3BC00))
                            .padding(8.dp)
                    )
                    Text(
                        text = "Interest", modifier = Modifier
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
    }
}

@Composable
fun LoanDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontFamily = FontFamily.Default,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = value,
            fontFamily = FontFamily.Monospace,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0174A3)
        )
    }
}

@Composable
fun DonutChart(principal: Double, interest: Float, modifier: Modifier = Modifier) {
    val total = principal + interest
    val principalAngle = 360f * (principal / total)
    val interestAngle = 360f * (interest / total)

    Canvas(modifier = modifier) {
        drawArc(
            color = Color(0xFF0174A3),
            startAngle = -90f,
            sweepAngle = principalAngle.toFloat(),
            useCenter = false,
            style = Stroke(width = 40f)
        )
        drawArc(
            color = Color(0xFFEC407A),
            startAngle = (-90f + principalAngle).toFloat(),
            sweepAngle = interestAngle.toFloat(),
            useCenter = false,
            style = Stroke(width = 40f)
        )
    }
}


