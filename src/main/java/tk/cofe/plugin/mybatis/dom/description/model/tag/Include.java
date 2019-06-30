package tk.cofe.plugin.mybatis.dom.description.model.tag;

import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.Nullable;
import tk.cofe.plugin.mybatis.dom.convert.IncludeConverter;

/**
 * {@code <include></include} 标签
 * @author : zhengrf
 * @date : 2019-01-20
 */
public interface Include extends Property, DynamicTag, DynamicSql {

    @Nullable
    @Attribute("refid")
    @Convert(IncludeConverter.class)
    GenericAttributeValue<XmlTag> getRefId();
}
