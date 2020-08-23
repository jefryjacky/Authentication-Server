package com.authentication.app.repository.mapper

import org.springframework.stereotype.Component

/**
 * Created by Jefry Jacky on 23/08/20.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Component
annotation class Mapper(
        /**
         * The value may indicate a suggestion for a logical component name,
         * to be turned into a Spring bean in case of an autodetected component.
         * @return the suggested component name, if any (or empty String otherwise)
         */
        val value: String = "")
