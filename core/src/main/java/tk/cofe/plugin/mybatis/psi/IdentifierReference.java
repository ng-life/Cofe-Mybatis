/*
 * Copyright (C) 2019-2021 cofe
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

package tk.cofe.plugin.mybatis.psi;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.ArrayUtil;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tk.cofe.plugin.common.utils.PsiMethodUtils;
import tk.cofe.plugin.common.utils.PsiTypeUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author : zhengrf
 * @date : 2021-03-06
 */
public abstract class IdentifierReference extends PsiReferenceBase<PsiElement> {
    protected final PsiClass psiClass;
    private final PsiElement targetElement;

    public IdentifierReference(@NotNull PsiElement element, final TextRange rangeInElement, PsiElement targetElement, PsiType psiType) {
        super(element, rangeInElement);
        this.targetElement = targetElement;
        this.psiClass = PsiTypeUtils.isCustomType(psiType) ? ((PsiClassType) psiType).resolve() : null;
    }

    @Override
    public @Nullable PsiElement resolve() {
        return targetElement;
    }

    @Override
    public Object @NotNull [] getVariants() {
        List<LookupElementBuilder> list = new ArrayList<>();
        for (PsiMember member : getClassMember()) {
            if (member instanceof PsiMethod) {
                list.add(createLookup(((PsiMethod) member)));
            }
            if (member instanceof PsiField) {
                list.add(createLookup(((PsiField) member)));
            }
        }
        return list.toArray(ArrayUtil.EMPTY_OBJECT_ARRAY);
    }

    protected abstract Collection<PsiMember> getClassMember();

    private LookupElementBuilder createLookup(PsiField psiField) {
        return LookupElementBuilder.create(psiField, psiField.getName())
                .withIcon(PlatformIcons.FIELD_ICON)
                // .withTailText(psiField.getName())
                .withPresentableText(psiField.getName())
                .withTypeText(psiField.getType().getPresentableText())
                ;
    }

    private LookupElementBuilder createLookup(PsiMethod psiMethod) {
        String methodName = PsiMethodUtils.replaceGetPrefix(psiMethod);
        return LookupElementBuilder.create(psiMethod, methodName)
                // .withTypeText(psiMethod.getName())
                .withIcon(PlatformIcons.METHOD_ICON)
                .withPresentableText(methodName)
                .withTailText(psiMethod.getName())
                .withTypeText(Optional.ofNullable(psiMethod.getReturnType()).map(PsiType::getPresentableText).orElse(""))
                ;
    }

}
