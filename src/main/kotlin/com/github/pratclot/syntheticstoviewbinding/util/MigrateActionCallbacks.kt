package com.github.pratclot.syntheticstoviewbinding.util

import com.intellij.psi.PsiElement

interface MigrateActionCallbacks {
    var layout: PsiElement?
    var synthImports: List<PsiElement>
    var synthNames: Set<String>
    fun onSynthRefFound(element: PsiElement)
}
