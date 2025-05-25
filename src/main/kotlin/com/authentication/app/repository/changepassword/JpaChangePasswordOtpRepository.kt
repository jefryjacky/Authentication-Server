package com.authentication.app.repository.changepassword

import com.authentication.app.repository.entity.ChangePasswordOtpDb
import org.springframework.data.jpa.repository.JpaRepository

interface JpaChangePasswordOtpRepository:JpaRepository<ChangePasswordOtpDb, String> {
}