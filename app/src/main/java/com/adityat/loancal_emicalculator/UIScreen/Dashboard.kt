package com.adityat.loancal_emicalculator.UIScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.adityat.loancal_emicalculator.Data.Entities
import com.adityat.loancal_emicalculator.Data.bankingcalculators
import com.adityat.loancal_emicalculator.Data.emicalculators
import com.adityat.loancal_emicalculator.Data.gstandvats
import com.adityat.loancal_emicalculator.Data.loans
import com.adityat.loancal_emicalculator.Data.othercalculators
import com.adityat.loancal_emicalculator.loadInterstitial
import com.adityat.loancal_emicalculator.showInterstitial
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Dashboard(navController: NavHostController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        loadInterstitial(context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffEDF7F6))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(scrollState)
        ) {
            card(
                head = "Loans",
                calculators = loans,
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .padding(top = 16.dp)
            )

            card(
                head = "Banking Calculators",
                calculators = bankingcalculators,
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .padding(top = 16.dp)
            )

            card(
                head = "EMI Calculators",
                calculators = emicalculators,
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .padding(top = 16.dp)
            )

            card(
                head = "GST & VAT",
                calculators = gstandvats,
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(top = 16.dp)
            )

            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = {
                    AdView(it).apply {
                        setAdSize(AdSize.SMART_BANNER)
                        adUnitId = "ca-app-pub-1838194983985161/1165697539"
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )

            card(
                head = "Other Calculators",
                calculators = othercalculators,
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun card(
    head: String,
    calculators: List<Entities>,
    navController: NavHostController,
    modifier: Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(text = head, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize()
            ) {
                items(calculators) { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(8.dp)
                            .border(
                                border = BorderStroke(0.1.dp, color = Color.LightGray),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                CoroutineScope(Dispatchers.Main).launch {
                                    showInterstitial(context) {

                                    }
                                }
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                }
                            }
                    ) {
                        Image(
                            painter = painterResource(id = item.icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(8.dp)
                        )
                        Text(
                            text = item.cal,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
