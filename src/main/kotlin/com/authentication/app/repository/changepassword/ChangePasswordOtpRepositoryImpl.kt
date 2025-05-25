package com.authentication.app.repository.changepassword

import com.authentication.app.domain.entity.ChangePasswordOtp
import com.authentication.app.domain.repository.ChangePasswordOtpRepository
import com.authentication.app.repository.entity.ChangePasswordOtpDb
import com.authentication.app.repository.mapper.AbstractMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ChangePasswordOtpRepositoryImpl:ChangePasswordOtpRepository {

    @Autowired
    private lateinit var mapper:AbstractMapper<ChangePasswordOtpDb, ChangePasswordOtp>
    @Autowired
    private lateinit var jpaChangePasswordOtpRepository: JpaChangePasswordOtpRepository
    override fun save(changePasswordOtp: ChangePasswordOtp) {
        val modelDb = mapper.mapFromEntity(changePasswordOtp)
        jpaChangePasswordOtpRepository.save(modelDb)
    }

    override fun get(email:String): ChangePasswordOtp? {
        val modelDb = jpaChangePasswordOtpRepository.findByIdOrNull(email)
        return modelDb?.let {
            mapper.mapToEntity(modelDb)
        }
    }
}