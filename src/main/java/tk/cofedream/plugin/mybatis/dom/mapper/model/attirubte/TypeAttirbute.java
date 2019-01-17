package tk.cofedream.plugin.mybatis.dom.mapper.model.attirubte;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.NameValue;
import com.intellij.util.xml.Required;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author : zhengrf
 * @date : 2019-01-15
 */
public interface TypeAttirbute extends DomElement {

    @Required
    @NameValue
    @Nullable
    @Attribute("id")
    GenericAttributeValue<String> getType();

    /**
     * 获取Id值
     * @return Id 值 如果为Null 则返回 ""
     */
    @NotNull
    default Optional<String> getTypeValue() {
        return BaseAttribute.getAttributeVlaue(getType());
    }

}
