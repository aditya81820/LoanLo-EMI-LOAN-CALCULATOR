package com.adityat.loancal_emicalculator.Data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LoanProfile::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun loanProfileDao(): LoanProfileDao
}
