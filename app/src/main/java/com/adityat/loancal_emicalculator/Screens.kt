package com.adityat.loancal_emicalculator

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val title: String, val route: String){

    sealed class DrawerScreens(val dTitle: String, val dRoute: String, val icon: ImageVector)
        : Screen(dTitle , dRoute){
        object Dashboard : DrawerScreens(
            "Dashboard",
            "dashboard",
            Icons.Default.Home,


        )
        object Sharethisapp : DrawerScreens(
            "Share This App",
            "sharethisapp",
            Icons.Default.Share
        )
        object Ratethisapp : DrawerScreens(
            "Rate This App",
            "ratethisapp",
            Icons.Default.Star
        )

        object Feedbackreport : DrawerScreens(
            "Feedback / Report",
            "feedbackreport",
            Icons.Default.Warning
        )
        object Privacypolicies : DrawerScreens(
            "Privacy Policies",
            "privacypolicis",
            Icons.Default.Info
        )
    }
    sealed class entityScreen(val etitile: String , val eroute: String): Screen(etitile , eroute){
        object Emicalculator: entityScreen("EMI-CALCULATOR","emi-calculator")
        object Compareloan: entityScreen("COMPARE-LOANS","compare-loan")
        object LoanEligiblity: entityScreen("LOAN-ELIGIBILITY","loan-Eligibility")
        object MaratoriumCal: entityScreen("MORATORIUM-CALCULATOR","maratorium-cal")
        object Advanceemical: entityScreen("ADVANCE-EMI-CALCULATOR","advanceemi-cal")
        object QuickCalculator: entityScreen("QUICK-CALCULATOR","quick-cal")
        object FDCalculator: entityScreen("FIXED-DEPOSIT-CALCULATOR","fd-cal")
        object RDCalculator: entityScreen("RECCURING-DEPOSIT-CALC.","rd-cal")
        object PPFCalculator: entityScreen("PPF-CALCULATOR.","ppf-cal")
        object Simpleandcompound: entityScreen("SIMPLE&COMPOUND-CAL.","si-ci-cal")
        object GSTCalculator: entityScreen("GST-CALCULATOR","gst-cal")
        object VATCalculator: entityScreen("VAT-CALCULATOR","vat-cal")
        object CashCounter: entityScreen("Cash-Counter","cash-counter")
        object Amounttoword: entityScreen("Amount-To-Word","amt-to-word")
        object Discountcalculator: entityScreen("Discount-Calculator","discount-cal")
        object SIPcalculator: entityScreen("SIP-Calculator","sip-cal")
        object CreateLoanprofile: entityScreen("Create-Loan-Profile","create-loanp")
        object ViewLoanProfile : entityScreen("VIEW-LOAN-PROFILE", "view-loanp")
        object LoanDetails : entityScreen("LOAN-DETAILS", "loan-details")



    }



}



val screenInDrawer = listOf(
    Screen.DrawerScreens.Dashboard,
    Screen.DrawerScreens.Sharethisapp,
    Screen.DrawerScreens.Ratethisapp,

    Screen.DrawerScreens.Feedbackreport,
    Screen.DrawerScreens.Privacypolicies,
)
val entityScreens = listOf(
    Screen.entityScreen.Emicalculator,
    Screen.entityScreen.Compareloan,
    Screen.entityScreen.LoanEligiblity,
    Screen.entityScreen.MaratoriumCal,
    Screen.entityScreen.Advanceemical,
    Screen.entityScreen.QuickCalculator,
    Screen.entityScreen.FDCalculator,
    Screen.entityScreen.RDCalculator,
    Screen.entityScreen.PPFCalculator,
    Screen.entityScreen.Simpleandcompound,
    Screen.entityScreen.GSTCalculator,
    Screen.entityScreen.VATCalculator,
    Screen.entityScreen.CashCounter,
    Screen.entityScreen.Amounttoword,
    Screen.entityScreen.Discountcalculator,
    Screen.entityScreen.SIPcalculator,
    Screen.entityScreen.CreateLoanprofile,
    Screen.entityScreen.ViewLoanProfile,
    Screen.entityScreen.LoanDetails


)