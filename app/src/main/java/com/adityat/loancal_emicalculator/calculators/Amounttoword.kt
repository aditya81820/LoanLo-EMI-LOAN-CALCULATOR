package com.adityat.loancal_emicalculator.calculators

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.adityat.loancal_emicalculator.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AmountToWordsCalculator() {
    var amount by remember { mutableStateOf("") }
    var wordsInIndianFormat by remember { mutableStateOf("") }
    var wordsInUSFormat by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Enter Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val amountInt = amount.toIntOrNull() ?: 0
                wordsInIndianFormat = convertNumberToIndianWords(amountInt)
                wordsInUSFormat = convertNumberToUSWords(amountInt)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Convert to Words", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Indian Format: ",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)

            )
            Text(
                text = "$wordsInIndianFormat",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = Color(0xFF0174A3)

            )
            IconButton(
                onClick = { copyToClipboard(context, wordsInIndianFormat) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.contentcopy),
                    contentDescription = "Copy Indian Format"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "US Format: ",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$wordsInUSFormat",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0174A3),
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { copyToClipboard(context, wordsInUSFormat) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.contentcopy),
                    contentDescription = "Copy US Format"
                )
            }
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

fun convertNumberToIndianWords(number: Int): String {
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
            n < 100000 -> numberToWords(n / 1000) + " thousand" + if (n % 1000 != 0) " " + numberToWords(
                n % 1000
            ) else ""

            n < 10000000 -> numberToWords(n / 100000) + " lakh" + if (n % 100000 != 0) " " + numberToWords(
                n % 100000
            ) else ""

            else -> numberToWords(n / 10000000) + " crore" + if (n % 10000000 != 0) " " + numberToWords(
                n % 10000000
            ) else ""
        }
    }

    return numberToWords(number).capitalize()
}

fun convertNumberToUSWords(number: Int): String {
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
            n < 1000000 -> numberToWords(n / 1000) + " thousand" + if (n % 1000 != 0) " " + numberToWords(
                n % 1000
            ) else ""

            n < 1000000000 -> numberToWords(n / 1000000) + " million" + if (n % 1000000 != 0) " " + numberToWords(
                n % 1000000
            ) else ""

            else -> numberToWords(n / 1000000000) + " billion" + if (n % 1000000000 != 0) " " + numberToWords(
                n % 1000000000
            ) else ""
        }
    }

    return numberToWords(number).capitalize()
}

fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Amount in Words", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
}
