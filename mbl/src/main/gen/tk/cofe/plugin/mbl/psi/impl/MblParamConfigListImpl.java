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
package tk.cofe.plugin.mbl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static tk.cofe.plugin.mbl.MblTypes.*;
import tk.cofe.plugin.mbl.psi.*;

public class MblParamConfigListImpl extends MblPsiCompositeElementBase implements MblParamConfigList {

  public MblParamConfigListImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull MblVisitor visitor) {
    visitor.visitParamConfigList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MblVisitor) accept((MblVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<MblJavaTypeConfig> getJavaTypeConfigList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MblJavaTypeConfig.class);
  }

  @Override
  @NotNull
  public List<MblJdbcTypeConfig> getJdbcTypeConfigList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MblJdbcTypeConfig.class);
  }

  @Override
  @NotNull
  public List<MblJdbcTypeNameConfig> getJdbcTypeNameConfigList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MblJdbcTypeNameConfig.class);
  }

  @Override
  @NotNull
  public List<MblModeConfig> getModeConfigList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MblModeConfig.class);
  }

  @Override
  @NotNull
  public List<MblNumericScaleConfig> getNumericScaleConfigList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MblNumericScaleConfig.class);
  }

  @Override
  @NotNull
  public List<MblResultMapConfig> getResultMapConfigList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MblResultMapConfig.class);
  }

  @Override
  @NotNull
  public List<MblTypeHandlerConfig> getTypeHandlerConfigList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MblTypeHandlerConfig.class);
  }

}
