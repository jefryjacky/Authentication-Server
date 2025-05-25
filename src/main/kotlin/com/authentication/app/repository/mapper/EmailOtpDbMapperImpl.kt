package com.authentication.app.repository.mapper

import com.authentication.app.domain.entity.EmailOtp
import com.authentication.app.repository.entity.EmailOtpDb

@Mapper
class EmailOtpDbMapperImpl: AbstractMapper<EmailOtpDb, EmailOtp>() {
    override fun mapToEntity(db: EmailOtpDb): EmailOtp {
        return EmailOtp(
            email = db.email,
            otp = db.otp,
            createdDate = db.createdDate
        )
    }

    override fun mapFromEntity(entity: EmailOtp): EmailOtpDb {
        return EmailOtpDb(
            email = entity.email,
            otp = entity.otp,
            createdDate = entity.createdDate
        )
    }


}