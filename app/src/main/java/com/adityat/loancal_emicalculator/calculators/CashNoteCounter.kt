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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CashNoteCounter() {
    val denominations = listOf(1, 2, 5, 10, 20, 50, 100, 500)
    val noteCounts = remember { mutableStateListOf(*Array(denominations.size) { "" }) }
    var totalAmount by remember { mutableStateOf(0) }
    var totalNotes by remember { mutableStateOf(0) }
    var amountInWords by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = {
                AdView(it).apply {
                    setAdSize(AdSize.SMART_BANNER)
                    adUnitId = "ca-app-pub-1838194983985161/1165697539"
                    loadAd(AdRequest.Builder().build())
                }
            })
        Row(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .border(3.dp, color = Color.Black)
                .background(Color(0xffEDF7F6)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total Amount: ₹${totalAmount.formatAsCurrency()}",
                fontFamily = FontFamily.Monospace,
                fontSize = 24.sp,
                modifier = Modifier.padding(vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0174A3)

            )
        }
        Row(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .border(3.dp, color = Color.Black)
                .background(Color(0xffEDF7F6)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total Notes Used: $totalNotes",
                fontFamily = FontFamily.Monospace,
                fontSize = 24.sp,

                modifier = Modifier.padding(vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0174A3)


            )
        }



        Text(
            text = "Amount in Words: $amountInWords",
            fontFamily = FontFamily.Monospace,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp),
            fontWeight = FontWeight.Bold,
        )




        denominations.forEachIndexed { index, denomination ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = denomination.toString() + " ->",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = noteCounts[index],
                    onValueChange = { noteCounts[index] = it },
                    label = { Text("Number of ₹$denomination notes") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }


        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                totalAmount = calculateTotalAmount(denominations, noteCounts)
                totalNotes = noteCounts.sumOf { it.toIntOrNull() ?: 0 }
                amountInWords = convertNumberToWords(totalAmount)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Calculate", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))


    }
}

fun calculateTotalAmount(denominations: List<Int>, noteCounts: List<String>): Int {
    return denominations.zip(noteCounts) { denomination, count ->
        denomination * (count.toIntOrNull() ?: 0)
    }.sum()
}

fun Int.formatAsCurrency(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    return formatter.format(this).replace("₹", "")
}

fun convertNumberToWords(number: Int): String {
    if (number == 0) return "zero"

    val ones = arrayOf(
        "",
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine",
        "ten",
        "eleven",
        "twelve",
        "thirteen",
        "fourteen",
        "fifteen",
        "sixteen",
        "seventeen",
        "eighteen",
        "nineteen"
    )
    val tens = arrayOf(
        "",
        "",
        "twenty",
        "thirty",
        "forty",
        "fifty",
        "sixty",
        "seventy",
        "eighty",
        "ninety"
    )

    fun numberToWords(n: Int): String {
        return when {
            n < 20 -> ones[n]
            n < 100 -> tens[n / 10] + if (n % 10 != 0) " " + ones[n % 10] else ""
            n < 1000 -> ones[n / 100] + " hundred" + if (n % 100 != 0) " and " + numberToWords(n % 100) else ""
            else -> numberToWords(n / 1000) + " thousand" + if (n % 1000 != 0) " " + numberToWords(n % 1000) else ""
        }
    }

    return numberToWords(number).capitalize()
}
