package tk.cofe.plugin.mybatis.dom.mapper.converter.attribute;

import com.intellij.psi.PsiElement;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.ResolvingConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tk.cofe.plugin.mybatis.dom.mapper.model.tag.ClassElement;
import tk.cofe.plugin.mybatis.service.JavaPsiService;
import tk.cofe.plugin.mybatis.util.DomUtils;
import tk.cofe.plugin.mybatis.util.StringUtils;

import java.util.Collection;
import java.util.Collections;

/**
 * @author : zhengrf
 * @date : 2019-01-23
 */
public class ClassElementConvert {

    public static class Id extends ResolvingConverter.StringConverter {
        @NotNull
        @Override
        public Collection<? extends String> getVariants(ConvertContext context) {
            //DomElement invocationElement = context.getInvocationElement();
            //ClassElement classElement = DomUtils.getParentOfType(invocationElement, ClassElement.class, true);
            //if (classElement == null) {
            //    return Collections.emptyList();
            //}
            //GenericAttributeValue<String> classElementId = classElement.getId();
            // todo ID值提示
            return Collections.emptyList();
        }

        @Nullable
        @Override
        public PsiElement resolve(String methodName, ConvertContext context) {
            if (StringUtils.isBlank(methodName)) {
                return null;
            }
            ClassElement classElement = DomUtils.getParentOfType(context.getInvocationElement(), ClassElement.class, true);
            return JavaPsiService.getInstance(context.getProject()).findMethod(classElement).map(psiMethod -> {
                if (methodName.equals(psiMethod.getName())) {
                    return psiMethod;
                }
                return null;
            }).orElse(null);
        }
    }
}
