package com.adityat.loancal_emicalculator.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loan_profiles")
data class LoanProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val profileName: String,
    val principalAmount: Double,
    val annualInterestRate: Double,
    val loanTenureMonths: Int,
    val processingFees: Double,
    val startDate: Long,
    val loanType: String
)
