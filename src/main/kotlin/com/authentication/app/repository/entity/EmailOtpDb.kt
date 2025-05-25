package com.authentication.app.repository.entity

import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "email_otp")
data class EmailOtpDb(
    @Id
    val email:String,
    @Column(name = "otp")
    val otp:String,
    @Column(name = "created_date")
    val createdDate:Date
)