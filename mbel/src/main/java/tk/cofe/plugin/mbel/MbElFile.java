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

package tk.cofe.plugin.mbel;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

/**
 * @author : zhengrf
 * @date : 2019-10-26
 */
public class MbElFile extends PsiFileBase {
    protected MbElFile(@NotNull final FileViewProvider viewProvider) {
        super(viewProvider, MbELLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return MbELFileType.INSTANCE;
    }
    @Override
    public String toString() {
        return "Mybatis(expression language) file:" + getName();
    }
}
