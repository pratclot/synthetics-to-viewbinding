package com.github.pratclot.syntheticstoviewbinding.visitor

import com.github.pratclot.syntheticstoviewbinding.util.ActionDataStore
import com.github.pratclot.syntheticstoviewbinding.util.with
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.structuralsearch.visitor.KotlinRecursiveElementVisitor

class InitialFileVisitor(private val actionDataStore: ActionDataStore) :
    KotlinRecursiveElementVisitor() {
    override fun visitElement(element: PsiElement) {
        with(actionDataStore, element) {
            pkg = it.children[0]
            imports = it.children[2]
            klass = it.children[4]
        }
    }
}
