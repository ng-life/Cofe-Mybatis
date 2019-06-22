package tk.cofe.plugin.mybatis.service.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import org.eclipse.lsp4j.jsonrpc.validation.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tk.cofe.plugin.mybatis.dom.description.model.tag.ClassElement;
import tk.cofe.plugin.mybatis.dom.description.model.tag.Mapper;
import tk.cofe.plugin.mybatis.service.JavaPsiService;
import tk.cofe.plugin.mybatis.service.MapperService;
import tk.cofe.plugin.mybatis.util.PsiJavaUtils;

import java.util.Optional;

/**
 * @author : zhengrf
 * @date : 2019-01-07
 */
public class JavaPsiServiceImpl implements JavaPsiService {
    private final Project project;
    private final MapperService mapperService;
    private final JavaPsiFacade javaPsiFacade;

    public JavaPsiServiceImpl(Project project) {
        this.project = project;
        this.mapperService = MapperService.getInstance(project);
        this.javaPsiFacade = JavaPsiFacade.getInstance(project);
    }

    // todo 调整
    @Override
    @SuppressWarnings("unchecked")
    public void process(@NotNull PsiElement target, @NotNull Processor processor) {
        if (target instanceof PsiMethod) {
            process((PsiMethod) target, processor);
        } else if (target instanceof PsiClass) {
            process((PsiClass) target, processor);
        }
    }

    @Override
    public void importClass(PsiJavaFile file, String qualifiedName) {
        if (!PsiJavaUtils.hasImportClass(file, qualifiedName)) {
            findPsiClass(qualifiedName).ifPresent(psiClass -> PsiJavaUtils.importClass(file, psiClass));
        }
    }

    @Override
    public void addAnnotation(PsiModifierListOwner psiModifierListOwner, String annotationText) {
        PsiModifierList modifierList = psiModifierListOwner.getModifierList();
        if (modifierList != null) {
            modifierList.add(javaPsiFacade.getElementFactory().createAnnotationFromText(annotationText, psiModifierListOwner));
        }
    }

    public void process(@NotNull PsiMethod psiMethod, @NotNull Processor<ClassElement> processor) {
        PsiClass psiClass = psiMethod.getContainingClass();
        if (psiClass == null) {
            return;
        }
        mapperService.findMapperXmls(psiClass).forEach(mapperXml -> mapperXml.getClassElements().forEach(classElement -> {
            classElement.getIdValue().ifPresent(id -> {
                if (id.equals(psiMethod.getName())) {
                    processor.process(classElement);
                }
            });
        }));
    }

    public void process(@NotNull PsiClass psiClass, @NotNull Processor<Mapper> processor) {
        mapperService.findMapperXmls(psiClass).forEach(mapperXml -> mapperXml.getNamespaceValue().ifPresent(qualifiedName -> {
            if (qualifiedName.equals(psiClass.getQualifiedName())) {
                processor.process(mapperXml);
            }
        }));
    }

    @NonNull
    @Override
    public Optional<PsiClass> findPsiClass(@NotNull String qualifiedName) {
        PsiClass aClass = javaPsiFacade.findClass(qualifiedName, GlobalSearchScope.projectScope(project));
        return Optional.ofNullable(aClass == null ? javaPsiFacade.findClass(qualifiedName, GlobalSearchScope.allScope(project)) : aClass);
    }

    @NotNull
    @Override
    public Optional<PsiMethod> findPsiMethod(@Nullable ClassElement element) {
        return findPsiMethods(element).flatMap(psiMethods -> psiMethods.length > 0 ? Optional.of(psiMethods[0]) : Optional.empty());
    }

    @NotNull
    @Override
    public Optional<PsiMethod[]> findPsiMethods(@Nullable ClassElement element) {
        if (element == null || !element.getIdValue().isPresent()) {
            return Optional.empty();
        }
        return element.getNamespaceValue().flatMap(qualifiedName ->
                findPsiClass(qualifiedName).flatMap(psiClass ->
                        element.getIdValue().flatMap(id -> Optional.of(psiClass.findMethodsByName(id, false)))));
    }

    @NotNull
    @Override
    public Optional<PsiMethod[]> findPsiMethod(@NotNull Mapper mapper, String methodName) {
        return mapper.getNamespaceValue().flatMap(namespace -> findPsiClass(namespace).flatMap(psiClass -> Optional.of(psiClass.findMethodsByName(methodName, false))));
    }

    @NotNull
    @Override
    public Optional<PsiClass> findPsiClass(@NotNull ClassElement element) {
        return element.getNamespaceValue().flatMap(this::findPsiClass);
    }
}
