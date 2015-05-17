package fr.adrienbrault.idea.symfony2plugin.action.generator;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.CodeInsightAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import fr.adrienbrault.idea.symfony2plugin.Symfony2Icons;
import fr.adrienbrault.idea.symfony2plugin.Symfony2ProjectComponent;
import fr.adrienbrault.idea.symfony2plugin.action.ui.SymfonyCreateService;
import org.jetbrains.annotations.NotNull;

public class ServiceGenerateAction extends CodeInsightAction {

    @Override
    public void update(AnActionEvent event) {
        super.update(event);
        event.getPresentation().setVisible(Symfony2ProjectComponent.isEnabled(event.getProject()));
    }

    public static void invokeServiceGenerator(Project project, PsiFile file, PhpClass phpClass) {

        SymfonyCreateService symfonyCreateService = new SymfonyCreateService(project, file);
        String presentableFQN = phpClass.getPresentableFQN();
        if(presentableFQN != null) {
            symfonyCreateService.setClassName(presentableFQN);
        }

        symfonyCreateService.init();

        symfonyCreateService.setTitle("Symfony: Service Generator");
        symfonyCreateService.setIconImage(Symfony2Icons.getImage(Symfony2Icons.SYMFONY));
        symfonyCreateService.pack();
        symfonyCreateService.setLocationRelativeTo(null);
        symfonyCreateService.setVisible(true);
    }

    @Override
    protected boolean isValidForFile(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {

        if(!(file instanceof PhpFile)) {
            return false;
        }

        int offset = editor.getCaretModel().getOffset();
        if(offset <= 0) {
            return false;
        }

        PsiElement psiElement = file.findElementAt(offset);
        if(psiElement == null) {
            return false;
        }

        if(!PlatformPatterns.psiElement().inside(PhpClass.class).accepts(psiElement)) {
            return false;
        }

        return true;
    }

    @NotNull
    @Override
    protected CodeInsightActionHandler getHandler() {
        return new CodeInsightActionHandler() {
            @Override
            public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile psiFile) {

                PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
                if(file == null) {
                    return;
                }
                int offset = editor.getCaretModel().getOffset();
                if(offset <= 0) {
                    return;
                }

                PsiElement psiElement = file.findElementAt(offset);
                if(psiElement == null) {
                    return;
                }

                PhpClass phpClass = PsiTreeUtil.getParentOfType(psiElement, PhpClass.class);
                if(phpClass == null) {
                    return;
                }

                invokeServiceGenerator(project, file, phpClass);

            }

            @Override
            public boolean startInWriteAction() {
                return false;
            }
        };
    }

}
