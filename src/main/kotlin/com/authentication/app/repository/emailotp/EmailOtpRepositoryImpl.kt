package com.authentication.app.repository.emailotp

import com.authentication.app.domain.entity.EmailOtp
import com.authentication.app.domain.repository.EmailOtpRepository
import com.authentication.app.repository.entity.EmailOtpDb
import com.authentication.app.repository.mapper.AbstractMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class EmailOtpRepositoryImpl:EmailOtpRepository {

    @Autowired
    private lateinit var mapper: AbstractMapper<EmailOtpDb, EmailOtp>
    @Autowired
    private lateinit var jpaEmailOtpRepository: JpaEmailOtpRepository

    override fun saveEmailOtp(emailOtp: EmailOtp) {
        val modelDp = mapper.mapFromEntity(emailOtp)
        jpaEmailOtpRepository.save(modelDp)
    }

    override fun getEmailOtp(email: String): EmailOtp? {
        val modelDb = jpaEmailOtpRepository.findByIdOrNull(email)
        return modelDb?.let {
            mapper.mapToEntity(modelDb)
        }
    }

}