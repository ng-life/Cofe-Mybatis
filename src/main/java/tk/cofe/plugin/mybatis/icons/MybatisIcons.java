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

package tk.cofe.plugin.mybatis.icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Mybatis的图标
 * @author : zhengrf
 * @date : 2019-01-01
 */
public class MybatisIcons {
    public static final Icon MybatisInterface = load("/icons/mybatisInterface.png");
    public static final Icon NavigateToStatement = load("/icons/navigateToStatement.png");
    public static final Icon NavigateToMethod = load("/icons/navigateToMethod.png");

    private static Icon load(final String path) {
        return IconLoader.getIcon(path, MybatisIcons.class);
    }
}
