package com.github.pratclot.syntheticstoviewbinding.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtNamedFunction

private const val BINDING_GETTER_NAME = "getViewBinding"
private const val BINDING_SETTER_NAME = "setViewBinding"
private const val BINDING_GETTER_BODY =
    "protected fun $BINDING_GETTER_NAME() = requireView().getTag(R.string.view_binding_tag)"
private const val BINDING_SETTER_BODY =
    "private fun $BINDING_SETTER_NAME() = with(requireView()) { setTag(R.string.view_binding_tag, $BINDING_CLASS_PLACEHOLDER.bind(this)) }"

class AddViewBindingMethodsAction : BaseAction() {

    var bindingGetter: KtNamedFunction? = null
    var bindingSetter: KtNamedFunction? = null

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        searchForExistingBindingMethods()
        addViewBindingGetter()
        addViewBindingSetter()

        bindingGetter = null
        bindingSetter = null
    }

    private fun searchForExistingBindingMethods() {
        klassBody?.run {
            children
                .filterIsInstance(KtNamedFunction::class.java)
                .forEach {
                    when (it.name) {
                        BINDING_GETTER_NAME -> bindingGetter = it
                        BINDING_SETTER_NAME -> bindingSetter = it
                    }
                }
        }
    }

    private fun addViewBindingGetter() = if (bindingGetter == null) {
        addViewBindingMethod(
            BINDING_GETTER_BODY.replace(
                BINDING_CLASS_PLACEHOLDER,
                bindingPropertyClass
            )
        )
    } else Unit

    private fun addViewBindingSetter() = if (bindingSetter == null) {
        addViewBindingMethod(
            BINDING_SETTER_BODY.replace(
                BINDING_CLASS_PLACEHOLDER,
                bindingPropertyClass
            )
        )
    } else Unit

    private fun addViewBindingMethod(functionBody: String) {
        firstFun?.let {
            writeToFile {
                (ktPsiFactory.createFunction(functionBody) as PsiElement)
                    .also { klassBody?.addBefore(it, firstFun) }
            }
        }
    }
}
