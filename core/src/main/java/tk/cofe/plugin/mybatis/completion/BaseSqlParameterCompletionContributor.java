/*
 * Copyright (C) 2019 cofe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package tk.cofe.plugin.mybatis.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.RowIcon;
import com.intellij.util.PlatformIcons;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tk.cofe.plugin.mybatis.annotation.Annotation;
import tk.cofe.plugin.mybatis.dom.model.dynamic.Bind;
import tk.cofe.plugin.mybatis.dom.model.include.BindInclude;
import tk.cofe.plugin.mybatis.dom.model.tag.ClassElement;
import tk.cofe.plugin.mybatis.provider.VariantsProvider;
import tk.cofe.plugin.mybatis.util.CompletionUtils;
import tk.cofe.plugin.mybatis.util.DomUtils;
import tk.cofe.plugin.mybatis.util.PsiJavaUtils;
import tk.cofe.plugin.mybatis.util.PsiTypeUtils;

import javax.swing.*;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 基础SQL 参数完成
 *
 * @author : zhengrf
 * @date : 2019-08-11
 */
abstract class BaseSqlParameterCompletionContributor extends CompletionContributor implements VariantsProvider<CompletionResultSet> {

    /**
     * 权重
     */
    private static final double PRIORITY = 100;
    private static final RowIcon PRIVATE_FIELD_ICON = new RowIcon(PlatformIcons.FIELD_ICON, PlatformIcons.PRIVATE_ICON);

    @Override
    public void fillCompletionVariants(@NotNull final CompletionParameters parameters, @NotNull final CompletionResultSet result) {
        if (parameters.getCompletionType() != CompletionType.BASIC) {
            return;
        }
        final PsiElement targetElement = getTargetElement(parameters, result);
        DomUtils.getDomElement(targetElement, ClassElement.class).flatMap(ClassElement::getIdMethod)
                .ifPresent(psiMethod -> {
                    DomUtils.getParents(targetElement, XmlTag.class, BindInclude.class).stream()
                            .flatMap(info -> info.getBinds().stream())
                            .map(Bind::getName)
                            .map(GenericAttributeValue::getXmlAttributeValue)
                            .filter(Objects::nonNull)
                            .map(XmlAttributeValue::getValue)
                            .filter(StringUtil::isNotEmpty)
                            .forEach(bind -> result.addElement(createLookupElement(bind, "", null)));
                    String prefixText = getPrefixText(parameters.getPosition(), result);
                    provider(prefixText, CompletionUtils.getPrefixArr(prefixText), psiMethod.getParameterList().getParameters(), result);
                });
    }

    @Override
    public void singleParam(final String prefixText, final String[] prefixArr, final PsiParameter firstParameter, final CompletionResultSet result) {
        Annotation.Value value = Annotation.PARAM.getValue(firstParameter);
        if (value == null) {
            // 如果是自定义类型,则读取类字段,如果不是则不做处理使用后续的 param1
            PsiTypeUtils.isCustomType(firstParameter.getType(), psiClassType -> addPsiClassTypeVariants(prefixText, psiClassType, result));
        } else {
            result.addElement(createLookupElement(value.getValue(), firstParameter.getType().getPresentableText(), AllIcons.Nodes.Parameter));
        }
    }

    @Override
    public void multiParam(final String prefixText, final String[] prefixArr, final PsiParameter[] parameters, final CompletionResultSet result) {
        for (PsiParameter psiParameter : parameters) {
            Optional.ofNullable(Annotation.PARAM.getValue(psiParameter)).ifPresent(value -> result.addElement(createLookupElement(value.getValue(), psiParameter.getType().getPresentableText(), AllIcons.Nodes.Parameter)));
        }
    }

    @Override
    public void emptyPrefix(final String prefixText, final String[] prefixArr, final PsiParameter[] parameters, final CompletionResultSet res) {
        PsiType type = CompletionUtils.getPrefixType(prefixArr[0], parameters);
        // 自定义类类型则取字段和方法
        PsiTypeUtils.isCustomType(type, psiClassType -> addPsiClassTypeVariants(prefixText, CompletionUtils.getPrefixPsiClass(prefixArr, type), res));
    }

    @Override
    public void beforeReturn(final String prefixText, final String[] prefixArr, final PsiParameter[] parameters, final CompletionResultSet result) {
        addParamsVariants(result, prefixArr, parameters);
        result.stopHere();
    }

    @Nullable
    abstract PsiElement getTargetElement(final CompletionParameters parameters, final CompletionResultSet result);

    /**
     * 获取前缀,用于值填充
     */
    abstract String getPrefixText(final PsiElement position, final CompletionResultSet result);

    /**
     * 通过类添加提示
     *
     * @param prefixText
     * @param psiType    Java类类型
     * @param result     结果集
     */
    private void addPsiClassTypeVariants(final String prefixText, @Nullable PsiClassType psiType, CompletionResultSet result) {
        if (psiType == null) {
            return;
        }
        PsiClass psiClass = psiType.resolve();
        if (psiClass == null) {
            return;
        }
        if (psiClass.isEnum()) {
            addMethodsVariants(prefixText, psiClass.getMethods(), result);
        } else {
            addFieldsVariants(prefixText, psiClass.getAllFields(), result);
            addMethodsVariants(prefixText, psiClass.getAllMethods(), result);
        }
    }

    private void addFieldsVariants(final String prefixText, final PsiField[] fields, final CompletionResultSet result) {
        for (PsiField field : fields) {
            if (PsiJavaUtils.notSerialField(field)) {
                createLookupElement(prefixText, field.getName(), field.getType().getPresentableText(), PsiTypeUtils.isCustomType(field.getType()) ? PlatformIcons.CLASS_ICON : PRIVATE_FIELD_ICON, result::addElement);
            }
        }
    }

    private void addMethodsVariants(final String prefixText, final PsiMethod[] methods, final CompletionResultSet result) {
        for (PsiMethod method : methods) {
            if (PsiJavaUtils.isGetMethod(method) && method.getReturnType() != null) {
                createLookupElement(prefixText, PsiJavaUtils.replaceGetPrefix(method), method.getReturnType().getPresentableText(), PlatformIcons.METHOD_ICON, result::addElement);
            }
        }
    }

    /**
     * 添加 param1-paramn 的提示
     *
     * @param result           结果集
     * @param prefixs          前缀
     * @param methodParameters 方法参数
     */
    private void addParamsVariants(CompletionResultSet result, String[] prefixs, PsiParameter[] methodParameters) {
        // 方法参数大于一个的时候才进行 param1->paramN的提示,否则仅提示类内部字段
        if (prefixs.length != 0 && methodParameters.length <= 1) {
            return;
        }
        for (int i = 0; i < methodParameters.length; i++) {
            result.addElement(createLookupElement("param" + (i + 1), methodParameters[i].getType().getPresentableText(), AllIcons.Nodes.Parameter, methodParameters.length - i));
        }
    }

    private void createLookupElement(final String preFix, @Nullable final String name, final String typeText, final Icon icon, final Consumer<LookupElement> consumer) {
        if (StringUtil.isEmpty(name) || StringUtil.isEmpty(typeText)) {
            return;
        }
        consumer.accept(PrioritizedLookupElement.withPriority(LookupElementBuilder.create(preFix + name).withPresentableText(name).withTypeText(typeText).bold().withIcon(icon), PRIORITY));
    }

    @NotNull
    private LookupElement createLookupElement(String lookupString, String type, @Nullable Icon icon) {
        return createLookupElement(lookupString, type, icon, PRIORITY);
    }

    @NotNull
    private LookupElement createLookupElement(String lookupString, String type, @Nullable Icon icon, double priority) {
        return PrioritizedLookupElement.withPriority(LookupElementBuilder.create(lookupString).withPresentableText(lookupString).withTypeText(type).bold().withIcon(icon), priority);
    }

}
