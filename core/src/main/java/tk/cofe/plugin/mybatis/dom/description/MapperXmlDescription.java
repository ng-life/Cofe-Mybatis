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

package tk.cofe.plugin.mybatis.dom.description;

import com.intellij.util.xml.DomFileDescription;
import com.intellij.util.xml.highlighting.DomElementsAnnotator;
import org.jetbrains.annotations.Nullable;
import tk.cofe.plugin.common.icons.MybatisIcons;
import tk.cofe.plugin.mybatis.config.MybatisConstants;
import tk.cofe.plugin.mybatis.dom.model.Mapper;

import javax.swing.*;

/**
 * 标注 mybatis mapper XML 文件,并提供值参考,<a href="https://www.jetbrains.org/intellij/sdk/docs/reference_guide/frameworks_and_external_apis/xml_dom_api.html">详情</a>
 *
 * @author : zhengrf
 * @date : 2019-01-02
 */
public class MapperXmlDescription extends DomFileDescription<Mapper> {

    public MapperXmlDescription() {
        super(Mapper.class, Mapper.TAG_NAME);
    }

    @Override
    protected void initializeFileDescription() {
        registerNamespacePolicy(MybatisConstants.MAPPER_NAMESPACE_KEY, MybatisConstants.MAPPER_NAME_SPACES);
    }

    @Nullable
    @Override
    public Icon getFileIcon(int flags) {
        return MybatisIcons.INTERFACE;
    }

    /**
     * <a href="https://www.jetbrains.org/intellij/sdk/docs/reference_guide/frameworks_and_external_apis/xml_dom_api.html#useful-methods-of-domelement-and-dommanager">详情</a>
     */
    @Nullable
    @Override
    public DomElementsAnnotator createAnnotator() {
        return null;
    }
}
