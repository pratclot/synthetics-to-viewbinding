package com.github.pratclot.syntheticstoviewbinding.action

import com.github.pratclot.syntheticstoviewbinding.util.MigrateActionCallbacks
import com.github.pratclot.syntheticstoviewbinding.util.mapToSetAs
import com.github.pratclot.syntheticstoviewbinding.util.toCamelCase
import com.github.pratclot.syntheticstoviewbinding.visitor.KlassBodyVisitor
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.PsiElement
import com.intellij.psi.search.FilenameIndex
import org.jetbrains.kotlin.idea.search.getKotlinFqName
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.resolve.ImportPath

private const val SYNTHETIC_IMPORT_MARKER = "import kotlinx.android.synthetic"

class MigrateFragmentAction : BaseAction(), MigrateActionCallbacks {

    override var synthImports: List<PsiElement> = mutableListOf()
    override var synthNames: Set<String> = mutableSetOf()
    override var layout: PsiElement? = null

    var synthUses: List<PsiElement> = mutableListOf()
    val layoutName: String
        get() = layout?.children?.run { if (size > 1) get(1)?.text else null } ?: ""
    val bindingClass: String get() = layoutName.toCamelCase() + "Binding"

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        collectSynthElements()

        try {
            convertSynthUses()
            addPropertyForBinding()
            addImportForBinding()
            cleanSynthImports()
        } catch (ex: Throwable) {
            println(ex)
        }

        clearActionState()
    }

    private fun collectSynthElements() {
        imports?.run {
            synthImports =
                synthImports + children
                    .filter { it.text.startsWith(SYNTHETIC_IMPORT_MARKER) } as List<PsiElement>
        }

        synthNames =
            synthImports.mapToSetAs<KtImportDirective, String> { it.importedName.toString() }

        klass?.accept(KlassBodyVisitor(this))
    }

    /**
     * TODO
     *
     * [allScope] includes `app/build/generated/source` dir, but not `app/build/generated/data_binding_base_class_source_out`.
     * Once the binding files could be located this step may work better.
     */
    private fun addImportForBinding() = try {
        val bindingFile = FilenameIndex.getAllFilesByExt(project, "java", allScope)
//            .filter { it.path.contains("BuildConfig") }
            .filter { it.path.contains(bindingClass) }
//            .forEach { println(it) }
            .first()
        val classesByName = psiShortNamesCache.getClassesByName(bindingClass, allScope)
        val first = classesByName.first()
        first.getKotlinFqName()?.let {
            writeToFile {
                ktPsiFactory.createImportDirective(
                    ImportPath(it, false)
                )
            }
        }
    } catch (_: Throwable) {
    }

    private fun clearActionState() {
        synthImports = mutableListOf()
        synthNames = mutableSetOf()
        synthUses = mutableListOf()
    }

    private fun addPropertyForBinding() {
        firstFun?.let {
            if (bindingProperty == null) {
                writeToFile {
                    (ktPsiFactory.createProperty("$BINDING_PROPERTY_START $bindingClass") as PsiElement)
                        .also { klassBody?.addBefore(it, firstFun) }
                }
            }
        }
    }

    private fun convertSynthUses() {
        synthUses.forEach {
            if (it.startOffsetInParent == 0) {
                val replacement = ktPsiFactory.createExpression("$BINDING_PROPERTY_NAME.${it.text}")
                writeToFile { it.replace(replacement) }
            }
        }
    }

    private fun cleanSynthImports() {
        synthImports.forEach { writeToFile { it.delete() } }
    }

    override fun onSynthRefFound(element: PsiElement) {
        synthUses = synthUses + element
    }
}
