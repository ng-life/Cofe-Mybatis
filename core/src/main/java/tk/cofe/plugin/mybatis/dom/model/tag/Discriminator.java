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

package tk.cofe.plugin.mybatis.dom.model.tag;

import com.intellij.util.xml.SubTagList;
import tk.cofe.plugin.mybatis.dom.model.attirubte.JavaTypeAttribute;
import tk.cofe.plugin.mybatis.dom.model.attirubte.JdbcTypeAttribute;
import tk.cofe.plugin.mybatis.dom.model.dynamic.Case;

import java.util.List;

/**
 * {@code <discriminator/>} 标签
 * @author : zhengrf
 * @date : 2019-01-21
 */
public interface Discriminator extends JavaTypeAttribute, JdbcTypeAttribute {

    @SubTagList("case")
    List<Case> getCases();

}
