package com.cs.baselib.bus

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(RetentionPolicy.RUNTIME)
annotation class BindBus
