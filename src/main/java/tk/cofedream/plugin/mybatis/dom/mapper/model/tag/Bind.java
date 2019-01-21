package tk.cofedream.plugin.mybatis.dom.mapper.model.tag;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.NameValue;
import org.jetbrains.annotations.Nullable;

/**
 * @author : zhengrf
 * @date : 2019-01-21
 */
public interface Bind extends DomElement {

    @NameValue
    @Nullable
    @Attribute("name")
    GenericAttributeValue<String> getName();

    @NameValue
    @Nullable
    @Attribute("value")
    GenericAttributeValue<String> getValue();

}
