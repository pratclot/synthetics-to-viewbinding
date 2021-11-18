package com.github.pratclot.syntheticstoviewbinding.visitor

import com.github.pratclot.syntheticstoviewbinding.util.MigrateActionCallbacks
import com.github.pratclot.syntheticstoviewbinding.util.allTrue
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtReferenceExpression
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid

private const val LAYOUT_REFERENCE_MARKER = "R.layout"

class KlassBodyVisitor(
    private val migrateActionCallbacks: MigrateActionCallbacks
) : KtTreeVisitorVoid() {
    override fun visitReferenceExpression(expression: KtReferenceExpression) {
        super.visitReferenceExpression(expression)
        expression.checkForSyntheticReference()
    }

    override fun visitDotQualifiedExpression(expression: KtDotQualifiedExpression) {
        super.visitDotQualifiedExpression(expression)
        expression.checkForLayoutReference()
    }

    private fun KtReferenceExpression.checkForSyntheticReference() {
        if (allTrue(
                text in migrateActionCallbacks.synthNames,
            )
        ) migrateActionCallbacks.onSynthRefFound(
            this
        )
    }

    private fun KtDotQualifiedExpression.checkForLayoutReference() {
        if (allTrue(
                text.startsWith(LAYOUT_REFERENCE_MARKER)
            )
        ) migrateActionCallbacks.layout = this
    }
}
