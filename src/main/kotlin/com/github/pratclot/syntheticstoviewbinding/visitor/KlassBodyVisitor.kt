package com.github.pratclot.syntheticstoviewbinding.visitor

import com.github.pratclot.syntheticstoviewbinding.util.MigrateActionCallbacks
import com.github.pratclot.syntheticstoviewbinding.util.allTrue
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.structuralsearch.visitor.KotlinRecursiveElementVisitor
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression

private const val LAYOUT_REFERENCE_MARKER = "R.layout"

class KlassBodyVisitor(
    private val migrateActionCallbacks: MigrateActionCallbacks
) : KotlinRecursiveElementVisitor() {
    override fun visitElement(element: PsiElement) {
        super.visitElement(element)
        checkForSyntheticReference(element)
    }

    /**
     * Finds instances of inflater.inflate(R.layout.fragment, container, false).
     */
    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)
        expression.checkForLayoutReference()
    }

    private fun checkForSyntheticReference(element: PsiElement) {
        if (element.text in migrateActionCallbacks.synthNames) migrateActionCallbacks.onSynthRefFound(
            element
        )
    }

    private fun KtCallExpression.checkForLayoutReference() {
        val valueArguments = children[1]
        val firstArgumentExpression = try {
            valueArguments.children[0].children[0]
        } catch (_: IndexOutOfBoundsException) {
            return
        }
        if (allTrue(
                firstArgumentExpression is KtDotQualifiedExpression,
                firstArgumentExpression.text.startsWith(LAYOUT_REFERENCE_MARKER),
            )
        ) migrateActionCallbacks.layout = firstArgumentExpression
    }
}
