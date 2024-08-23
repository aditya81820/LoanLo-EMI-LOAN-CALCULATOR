package com.adityat.loancal_emicalculator

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.adityat.loancal_emicalculator.UIScreen.Dashboard
import com.adityat.loancal_emicalculator.UIScreen.HomeScreen
import com.adityat.loancal_emicalculator.UIScreen.LoanDetailsScreen
import com.adityat.loancal_emicalculator.UIScreen.LoanProfileForm
import com.adityat.loancal_emicalculator.UIScreen.ViewProfiles
import com.adityat.loancal_emicalculator.calculators.AdvanceEMICalculator
import com.adityat.loancal_emicalculator.calculators.AmountToWordsCalculator
import com.adityat.loancal_emicalculator.calculators.CashNoteCounter
import com.adityat.loancal_emicalculator.calculators.DiscountCalculator
import com.adityat.loancal_emicalculator.calculators.EmiCalculator
import com.adityat.loancal_emicalculator.calculators.FDCalculator
import com.adityat.loancal_emicalculator.calculators.GSTCalculator
import com.adityat.loancal_emicalculator.calculators.LoanComparisonComposable
import com.adityat.loancal_emicalculator.calculators.LoanEligibilityComposable
import com.adityat.loancal_emicalculator.calculators.MoratoriumCalculator
import com.adityat.loancal_emicalculator.calculators.PPFCalculator
import com.adityat.loancal_emicalculator.calculators.QuickCalculatot
import com.adityat.loancal_emicalculator.calculators.RDCalculator
import com.adityat.loancal_emicalculator.calculators.SIPCalculator
import com.adityat.loancal_emicalculator.calculators.SimpleCompoundInterestCalculator
import com.adityat.loancal_emicalculator.calculators.VATCalculator
import com.adityat.loancal_emicalculator.ui.theme.LoanCalEMICalculatorTheme
import com.adityat.loancal_emicalculator.viewModels.MainViewModel
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        MobileAds.initialize(this) {}
        setContent {

            LoanCalEMICalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    HomeScreen()
                }
            }
        }
    }
    override fun onDestroy() {
        removeInterstitial()
        super.onDestroy()
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavController, viewModel: MainViewModel, pd: PaddingValues) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.DrawerScreens.Dashboard.route,


    ) {
        composable(Screen.DrawerScreens.Dashboard.route) {
            Dashboard(navController)
        }
        composable(Screen.DrawerScreens.Sharethisapp.route) {

        }
        composable(Screen.DrawerScreens.Ratethisapp.route) {

        }

        composable(Screen.DrawerScreens.Feedbackreport.route) {

        }
        composable(Screen.DrawerScreens.Privacypolicies.route) {

        }
        composable(Screen.entityScreen.Emicalculator.eroute) {
            EmiCalculator()
        }
        composable(Screen.entityScreen.Compareloan.eroute) {
            LoanComparisonComposable()
        }
        composable(Screen.entityScreen.LoanEligiblity.eroute) {
            LoanEligibilityComposable()
        }
        composable(Screen.entityScreen.MaratoriumCal.eroute) {
            MoratoriumCalculator()
        }
        composable(Screen.entityScreen.Advanceemical.eroute) {
            AdvanceEMICalculator()
        }
        composable(Screen.entityScreen.QuickCalculator.eroute) {
            QuickCalculatot()
        }
        composable(Screen.entityScreen.FDCalculator.eroute,) {
            FDCalculator()
        }
        composable(Screen.entityScreen.RDCalculator.eroute) {
            RDCalculator()
        }
        composable(Screen.entityScreen.PPFCalculator.eroute) {
            PPFCalculator()
        }
        composable(Screen.entityScreen.Simpleandcompound.eroute) {
            SimpleCompoundInterestCalculator()
        }
        composable(Screen.entityScreen.GSTCalculator.eroute) {
            GSTCalculator()
        }
        composable(Screen.entityScreen.VATCalculator.eroute) {
            VATCalculator()
        }
        composable(Screen.entityScreen.CashCounter.eroute) {
            CashNoteCounter()
        }
        composable(Screen.entityScreen.Amounttoword.eroute) {
            AmountToWordsCalculator()
        }
        composable(Screen.entityScreen.Discountcalculator.eroute) {
            DiscountCalculator()
        }
        composable(Screen.entityScreen.SIPcalculator.eroute) {
            SIPCalculator()
        }
        composable(Screen.entityScreen.CreateLoanprofile.eroute) {
            LoanProfileForm()
        }
        composable(Screen.entityScreen.ViewLoanProfile.eroute) {
            ViewProfiles(navController)
        }
        composable(Screen.entityScreen.LoanDetails.eroute + "/{profilId}",
            arguments = listOf(
                navArgument("profilId") {
                    type = NavType.IntType
                    defaultValue = 0
                    nullable = false
                }

            )
        ) { entry ->
            val id = entry.arguments?.getInt("profilId") ?: 0
            LoanDetailsScreen(navController = navController, profileId = id)
        }


    }
}




