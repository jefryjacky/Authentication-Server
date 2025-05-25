package com.authentication.app.repository.emailotp

import com.authentication.app.repository.entity.EmailOtpDb
import org.springframework.data.jpa.repository.JpaRepository


interface JpaEmailOtpRepository: JpaRepository<EmailOtpDb, String> {
}