/*
 * Copyright (C) 2019 cofe
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package tk.cofe.plugin.mybatis.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import tk.cofe.plugin.mybatis.util.CompletionUtils;

/**
 * SQL 文件中的SQL 参数完成
 *
 * @author : zhengrf
 * @date : 2019-08-11
 */
public class MbspFileSqlParameterCompletionContributor extends BaseSqlParameterCompletionContributor {

    @Override
    PsiFile getTargetPsiFile(@NotNull final CompletionParameters parameters, @NotNull final CompletionResultSet result) {
        PsiElement position = parameters.getPosition();
        InjectedLanguageManager manager = InjectedLanguageManager.getInstance(position.getProject());
        return manager.getTopLevelFile(position);
    }

    @Override
    PsiElement getTargetElement(@NotNull final PsiFile psiFile, @NotNull final CompletionParameters parameters, @NotNull final CompletionResultSet result) {
        PsiElement position = parameters.getPosition();
        return psiFile.findElementAt(InjectedLanguageManager.getInstance(position.getProject()).injectedToHost(position, position.getTextOffset()));
    }

    @NotNull
    String getPrefixText(final PsiElement position, @NotNull CompletionResultSet result) {
        PsiFile file = position.getContainingFile();
        String fileText = file.getText();
        String substring = fileText.substring(fileText.indexOf("{") + 1, fileText.lastIndexOf("}"));
        return CompletionUtils.getPrefixStr(substring);
    }

}
