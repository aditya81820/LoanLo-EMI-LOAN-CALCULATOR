package com.adityat.loancal_emicalculator.UIScreen

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.adityat.loancal_emicalculator.Data.DatabaseBuilder
import com.adityat.loancal_emicalculator.Data.LoanProfile
import com.adityat.loancal_emicalculator.calculators.DropdownMenu
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoanProfileForm() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { DatabaseBuilder.getInstance(context) }
    val loanProfileDao = remember { db.loanProfileDao() }

    var profileName by remember { mutableStateOf("") }
    var principalAmount by remember { mutableStateOf("") }
    var annualInterestRate by remember { mutableStateOf("") }
    var loanTenureMonths by remember { mutableStateOf("") }
    var processingFees by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(Date().time) }
    var loanType by remember { mutableStateOf("Home Loan") }

    val loanTypes =
        listOf("Home Loan", "Vehicle Loan", "Personal Loan", "Gold Loan", "Education Loan", "Other")


    // DatePickerDialog to choose a start date
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            startDate = calendar.timeInMillis
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = profileName,
            onValueChange = { profileName = it },
            label = { Text("Profile Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = principalAmount,
            onValueChange = { principalAmount = it },
            label = { Text("Principal Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = annualInterestRate,
            onValueChange = { annualInterestRate = it },
            label = { Text("Annual Interest Rate (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = loanTenureMonths,
            onValueChange = { loanTenureMonths = it },
            label = { Text("Loan Tenure (Months)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = processingFees,
            onValueChange = { processingFees = it },
            label = { Text("Processing Fees") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), horizontalArrangement = Arrangement.Center
        ) {

            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .height(43.dp),
                onClick = { datePickerDialog.show() },
                backgroundColor = Color(0xff0174A3)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Choose Start Date", color = Color.White)
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }

            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .height(43.dp),
                backgroundColor = Color(0xff0174A3),
            ) {

                    DropdownMenu(
                        selectedItem = loanType,
                        items = loanTypes,
                        onItemSelected = { selectedType -> loanType = selectedType }
                    )


            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val loanProfile = LoanProfile(
                    profileName = profileName,
                    principalAmount = principalAmount.toDoubleOrNull() ?: 0.0,
                    annualInterestRate = annualInterestRate.toDoubleOrNull() ?: 0.0,
                    loanTenureMonths = loanTenureMonths.toIntOrNull() ?: 0,
                    processingFees = processingFees.toDoubleOrNull() ?: 0.0,
                    startDate = startDate,
                    loanType = loanType
                )

                scope.launch {
                    loanProfileDao.insert(loanProfile)
                    Toast.makeText(context, "Loan profile saved", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Loan Profile")
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
