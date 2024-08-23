package com.adityat.loancal_emicalculator.UIScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.adityat.loancal_emicalculator.Data.DatabaseBuilder
import com.adityat.loancal_emicalculator.Data.LoanProfile
import com.adityat.loancal_emicalculator.Screen
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId.systemDefault
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewProfiles(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { DatabaseBuilder.getInstance(context) }
    val loanProfileDao = remember { db.loanProfileDao() }

    var loanProfiles by remember { mutableStateOf(listOf<LoanProfile>()) }

    LaunchedEffect(Unit) {
        scope.launch {
            loanProfiles = loanProfileDao.getAllProfiles()
        }
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {

        items(loanProfiles) { profile ->
            LoanProfileItem(
                profile = profile,
                navController = navController,
                onDelete = {
                    // Remove the profile from the list
                    loanProfiles = loanProfiles.filter { it.id != profile.id }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoanProfileItem(profile: LoanProfile, navController: NavController, onDelete: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val loanProfileDao = remember { DatabaseBuilder.getInstance(context).loanProfileDao() }
    val emi = calculateEmi(profile)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffEDF7F6)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()


            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(25.dp)
                )

                Text(
                    text = profile.profileName,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff222E50),


                    )

            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Principal Amount",
                    fontFamily = FontFamily.Default,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
                Text(
                    text = "${profile.principalAmount}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0174A3),
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text(
                    text = "EMI",
                    fontFamily = FontFamily.Default,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
                Text(
                    text = "%.2f".format(emi),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0174A3),
                )

            }


            // Convert Long to formatted date string
            val formattedDate = try {
                val instant = Instant.ofEpochMilli(profile.startDate)
                val zoneId = systemDefault()
                val zonedDateTime = instant.atZone(zoneId)
                val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
                zonedDateTime.format(formatter)
            } catch (e: Exception) {
                "Invalid Date" // Handle conversion errors
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text(
                    text = "Start Date:",
                    fontFamily = FontFamily.Default,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,

                    )
                Text(
                    text = "$formattedDate",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0174A3),
                )

            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        navController.navigate(Screen.entityScreen.LoanDetails.eroute + "/${profile.id}")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0174A3))
                ) {
                    Text(text = "Loan Details")
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(
                            0xFF0174A3
                        )
                    ), onClick = {
                        scope.launch {
                            loanProfileDao.deleteProfile(profile)
                            onDelete() // Update the list after deletion
                        }
                    }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.LightGray
                    )
                }
            }
        }
    }
}


fun calculateEmi(profile: LoanProfile): Double {
    val principal = profile.principalAmount
    val annualInterestRate = profile.annualInterestRate / 100
    val monthlyInterestRate = annualInterestRate / 12
    val months = profile.loanTenureMonths.toDouble()
    return if (monthlyInterestRate > 0) {
        principal * monthlyInterestRate * Math.pow(
            1 + monthlyInterestRate,
            months
        ) / (Math.pow(1 + monthlyInterestRate, months) - 1)
    } else {
        principal / months
    }
}
