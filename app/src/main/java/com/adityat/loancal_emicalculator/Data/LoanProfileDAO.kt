package com.adityat.loancal_emicalculator.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LoanProfileDao {
    @Insert
    suspend fun insert(loanProfile: LoanProfile)

    @Query("SELECT * FROM loan_profiles")
    suspend fun getAllProfiles(): List<LoanProfile>

    @Query("SELECT * FROM loan_profiles WHERE id = :profileId")
    suspend fun getProfileById(profileId: Int): LoanProfile?

    @Delete
    suspend fun deleteProfile(loanProfile: LoanProfile)


}
