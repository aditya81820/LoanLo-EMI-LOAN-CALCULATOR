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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun DiscountCalculator() {
    var amount by remember { mutableStateOf("") }
    var discountPercentage by remember { mutableStateOf("") }
    var salesTaxPercentage by remember { mutableStateOf("") }
    var discountType by remember { mutableStateOf(DiscountType.AFTER_TAX) }
    var calculatedAmount by remember { mutableStateOf("") }
    var calculatedSalesTax by remember { mutableStateOf("") }
    var calculatedSavings by remember { mutableStateOf("") }
    var calculatedPayableAmount by remember { mutableStateOf("") }

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

        OutlinedTextField(
            value = discountPercentage,
            onValueChange = { discountPercentage = it },
            label = { Text("Enter Discount (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = salesTaxPercentage,
            onValueChange = { salesTaxPercentage = it },
            label = { Text("Enter Sales Tax (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            RadioButton(
                selected = discountType == DiscountType.AFTER_TAX,
                onClick = { discountType = DiscountType.AFTER_TAX }
            )
            Text(text = "Discount After Tax")

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = discountType == DiscountType.BEFORE_TAX,
                onClick = { discountType = DiscountType.BEFORE_TAX }
            )
            Text(text = "Discount Before Tax")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                calculateDiscount(
                    amount,
                    discountPercentage,
                    salesTaxPercentage,
                    discountType
                ).let { result ->
                    calculatedAmount = result.first.formatAsCurrency()
                    calculatedSalesTax = result.second.formatAsCurrency()
                    calculatedSavings = result.third.formatAsCurrency()
                    calculatedPayableAmount = result.fourth.formatAsCurrency()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Calculate", fontSize = 16.sp)
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

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(8.dp).border(3.dp, color = Color.Black)
                .background(Color(0xffEDF7F6)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Amount: $calculatedAmount",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp
            )

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
            Text(
                "Sales Tax: $calculatedSalesTax",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp
            )

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
            Text(
                "Savings: $calculatedSavings",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp
            )

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
            Text(
                "Payable Amount: $calculatedPayableAmount",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp
            )

        }

    }
}

fun calculateDiscount(
    amountStr: String,
    discountPercentageStr: String,
    salesTaxPercentageStr: String,
    discountType: DiscountType
): Quadruple<Int, Int, Int, Int> {
    val amount = amountStr.toDoubleOrNull() ?: 0.0
    val discountPercentage = discountPercentageStr.toDoubleOrNull() ?: 0.0
    val salesTaxPercentage = salesTaxPercentageStr.toDoubleOrNull() ?: 0.0

    val discount = (amount * discountPercentage) / 100
    val salesTax = (amount * salesTaxPercentage) / 100

    val finalAmount: Double
    val payableAmount: Double

    when (discountType) {
        DiscountType.AFTER_TAX -> {
            finalAmount = amount + salesTax
            payableAmount = finalAmount - discount
        }
        DiscountType.BEFORE_TAX -> {
            val amountAfterDiscount = amount - discount
            payableAmount = amountAfterDiscount + (amountAfterDiscount * salesTaxPercentage / 100)
            finalAmount = amountAfterDiscount
        }
    }

    return Quadruple(finalAmount.toInt(), salesTax.toInt(), discount.toInt(), payableAmount.toInt())
}

enum class DiscountType {
    BEFORE_TAX,
    AFTER_TAX
}

data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)


