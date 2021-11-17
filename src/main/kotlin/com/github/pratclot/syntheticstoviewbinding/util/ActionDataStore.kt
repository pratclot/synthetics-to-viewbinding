package com.github.pratclot.syntheticstoviewbinding.util

import com.intellij.psi.PsiElement

interface ActionDataStore {
    var pkg: PsiElement?
    var imports: PsiElement?
    var klass: PsiElement?
}
