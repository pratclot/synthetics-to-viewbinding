package com.github.pratclot.syntheticstoviewbinding.action

import com.github.pratclot.syntheticstoviewbinding.util.ActionDataStore
import com.github.pratclot.syntheticstoviewbinding.util.BindingPropertyProvider
import com.github.pratclot.syntheticstoviewbinding.util.allTrue
import com.github.pratclot.syntheticstoviewbinding.visitor.InitialFileVisitor
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.ProjectScope
import com.intellij.psi.search.PsiShortNamesCache
import org.jetbrains.kotlin.idea.structuralsearch.visitor.KotlinRecursiveElementVisitor
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.KtTypeReference


const val BINDING_CLASS_PLACEHOLDER = "BINDING_PLACEHOLDER"

/**
 * TODO make these user-configurable.
 */
const val BINDING_PROPERTY_NAME = "binding"
const val BINDING_PROPERTY_START = "val $BINDING_PROPERTY_NAME get() = getViewBinding() as "

open class BaseAction : AnAction(), ActionDataStore, BindingPropertyProvider {

    lateinit var project: Project
    var file: PsiFile? = null

    override var pkg: PsiElement? = null
    override var imports: PsiElement? = null
    override var klass: PsiElement? = null

    override val bindingPropertyClass: String
        //        get() = bindingProperty?.run { children[0].text } ?: ""
        get() {
            var newValue = ""
            bindingProperty?.accept(object : KotlinRecursiveElementVisitor() {
                override fun visitElement(element: PsiElement) {
                    super.visitElement(element)

                    if (element is KtTypeReference) newValue = element.text
                }
            })
            return newValue
        }
    override val bindingProperty: KtProperty?
        get() = klassBody?.children?.find {
            allTrue(
                it is KtProperty,
                it.text.startsWith(BINDING_PROPERTY_START)
            )
        } as KtProperty?

    val klassBody: PsiElement? get() = klass?.run { children.get(1) }
    val firstFun: PsiElement? get() = klassBody?.children?.find { it is KtNamedFunction }

    val ktPsiFactory: KtPsiFactory by lazy { KtPsiFactory(project) }
    val psiShortNamesCache: PsiShortNamesCache by lazy { PsiShortNamesCache.getInstance(project) }
    val everythingScope: GlobalSearchScope by lazy { GlobalSearchScope.everythingScope(project) }
    val allScope: GlobalSearchScope by lazy { ProjectScope.getAllScope(project) }

    override fun actionPerformed(e: AnActionEvent) {
        setup(e)
        parseFile()
    }

    private fun setup(e: AnActionEvent) {
        project = e.project!!
        file = e.getData(CommonDataKeys.PSI_FILE)
    }

    private fun parseFile() {
        file?.accept(InitialFileVisitor(this))
    }

    protected fun writeToFile(block: () -> Any?) {
        WriteCommandAction.runWriteCommandAction(project) { block() }
    }
}
