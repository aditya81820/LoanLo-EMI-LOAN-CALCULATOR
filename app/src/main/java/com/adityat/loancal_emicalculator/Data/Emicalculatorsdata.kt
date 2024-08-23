package com.adityat.loancal_emicalculator.Data

import com.adityat.loancal_emicalculator.R
import com.adityat.loancal_emicalculator.Screen

data class Entities (
    val cal : String,
    val icon : Int,
    val route : String

)
val emicalculators  = listOf(
    Entities("EMI Calculator" ,  R.drawable.emicalculator, Screen.entityScreen.Emicalculator.eroute),
    Entities("Advance Calculator" ,  R.drawable.advance, Screen.entityScreen.Advanceemical.eroute),
    Entities("Compare Calculator" ,  R.drawable.compareloan, Screen.entityScreen.Compareloan.eroute),
)
val loans  = listOf(
    Entities("Create Loan Profile" ,  R.drawable.loanprofile, Screen.entityScreen.CreateLoanprofile.eroute),
    Entities("View Loan " +
            "Profile" ,  R.drawable.viewloanprofile, Screen.entityScreen.ViewLoanProfile.eroute),
    Entities("Check Eligibility" ,  R.drawable.checkeligibility, Screen.entityScreen.LoanEligiblity.eroute),
    Entities("Moratorium " +
            "Calculator" ,  R.drawable.maratorium, Screen.entityScreen.MaratoriumCal.eroute),

)
val gstandvats  = listOf(
    Entities("GST Calculator" ,  R.drawable.gstcalculate, Screen.entityScreen.GSTCalculator.eroute),
    Entities("Vat Calculator" ,  R.drawable.vatcalculate, Screen.entityScreen.VATCalculator.eroute)
)
val bankingcalculators = listOf(
    Entities("FD Calculator" ,  R.drawable.fd, Screen.entityScreen.FDCalculator.eroute),
    Entities("RD Calculator" ,  R.drawable.rd, Screen.entityScreen.RDCalculator.eroute),
    Entities("PPF Calculator" ,  R.drawable.ppf, Screen.entityScreen.PPFCalculator.eroute),
    Entities("Simple & Com.." ,  R.drawable.sici, Screen.entityScreen.Simpleandcompound.eroute)
)
val othercalculators = listOf(
    Entities("Cash Note Counter" , R.drawable.cashnote, Screen.entityScreen.CashCounter.eroute),
    Entities("Amount to word" ,  R.drawable.amounttoword, Screen.entityScreen.Amounttoword.eroute),
    Entities("Discount Calculator" ,  R.drawable.discount, Screen.entityScreen.Discountcalculator.eroute),
    Entities("SIP Calculator" ,  R.drawable.sipcal, Screen.entityScreen.SIPcalculator.eroute)
)
