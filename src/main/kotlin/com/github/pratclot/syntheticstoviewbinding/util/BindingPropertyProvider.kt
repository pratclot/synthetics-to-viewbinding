package com.github.pratclot.syntheticstoviewbinding.util

import org.jetbrains.kotlin.psi.KtProperty


interface BindingPropertyProvider {
    val bindingProperty: KtProperty?
    val bindingPropertyClass: String
}
