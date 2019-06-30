package tk.cofe.plugin.mybatis.dom.description.model.attirubte;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.NameValue;
import org.jetbrains.annotations.Nullable;

/**
 * @author : zhengrf
 * @date : 2019-06-30
 */
public interface OfTypeAttribute extends DomElement {

    @NameValue
    @Nullable
    @Attribute("ofType")
    GenericAttributeValue<String> getOfType();
}
