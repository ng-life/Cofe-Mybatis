package tk.cofe.plugin.mybatis.psi;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlElementType;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;
import tk.cofe.plugin.mybatis.dom.convert.ResultMapConverter;
import tk.cofe.plugin.mybatis.dom.model.Mapper;
import tk.cofe.plugin.mybatis.dom.model.tag.ResultMap;
import tk.cofe.plugin.mybatis.util.MybatisUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author : zhengrf
 * @date : 2019-11-21
 * @see ResultMapConverter.IdReferencing
 */
public class ResultMapPsiReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull final PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(XmlElementType.XML_ATTRIBUTE_VALUE).withParent(PlatformPatterns.psiElement(XmlElementType.XML_ATTRIBUTE)), new PsiReferenceProvider() {
            @NotNull
            @Override
            public PsiReference[] getReferencesByElement(@NotNull final PsiElement element, @NotNull final ProcessingContext context) {
                if (!(element instanceof XmlAttributeValue)) {
                    return PsiReference.EMPTY_ARRAY;
                }
                if (!MybatisUtils.isMapperXmlFile(element.getContainingFile())) {
                    return PsiReference.EMPTY_ARRAY;
                }
                XmlAttribute xmlAttribute = PsiTreeUtil.getParentOfType(element, XmlAttribute.class);
                if (xmlAttribute == null) {
                    return PsiReference.EMPTY_ARRAY;
                }
                XmlTag xmlTag = PsiTreeUtil.getParentOfType(element, XmlTag.class);
                if (xmlTag == null) {
                    return PsiReference.EMPTY_ARRAY;
                }
                String value = ((XmlAttributeValue) element).getValue();
                switch (xmlTag.getName()) {
                    case "resultMap":
                        if (Objects.equals("id", xmlAttribute.getName())) {
                            return MybatisUtils.getMapper(xmlTag)
                                    .map(Mapper::getResultMaps)
                                    .map(resultMaps -> resultMaps.stream()
                                            .filter(resultMap -> !resultMap.isEqualsId(value) && Objects.equals(value, resultMap.getExtendsValue().orElse(null)))
                                            .map((Function<ResultMap, Object>) ResultMap::getExtends)
                                            .map(o -> ((GenericAttributeValue) o).getXmlAttributeValue())
                                            .filter(Objects::nonNull)
                                            .map(xmlAttributeValue -> new PsiReferenceBase.Immediate<>(element, xmlAttributeValue)).toArray(PsiReference[]::new)).orElse(PsiReference.EMPTY_ARRAY);
                        }
                        break;
                }
                return new PsiReference[0];
            }
        });
    }
}
