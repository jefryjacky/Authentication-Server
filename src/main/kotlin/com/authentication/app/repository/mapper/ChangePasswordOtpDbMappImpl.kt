package com.authentication.app.repository.mapper

import com.authentication.app.domain.entity.ChangePasswordOtp
import com.authentication.app.repository.entity.ChangePasswordOtpDb

@Mapper
class ChangePasswordOtpDbMappImpl:AbstractMapper<ChangePasswordOtpDb, ChangePasswordOtp>() {
    override fun mapToEntity(db: ChangePasswordOtpDb): ChangePasswordOtp {
        return ChangePasswordOtp(
            email = db.email,
            otp = db.otp,
            createdDate = db.createdDate)
    }

    override fun mapFromEntity(entity: ChangePasswordOtp): ChangePasswordOtpDb {
        return ChangePasswordOtpDb(
            email = entity.email,
            otp = entity.otp,
            createdDate = entity.createdDate
        )
    }
}