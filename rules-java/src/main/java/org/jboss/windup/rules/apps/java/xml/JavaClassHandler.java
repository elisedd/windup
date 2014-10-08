package org.jboss.windup.rules.apps.java.xml;

import static org.joox.JOOX.$;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.windup.config.exception.ConfigurationException;
import org.jboss.windup.config.parser.ElementHandler;
import org.jboss.windup.config.parser.NamespaceElementHandler;
import org.jboss.windup.config.parser.ParserContext;
import org.jboss.windup.rules.apps.java.config.JavaClass;
import org.jboss.windup.rules.apps.java.config.JavaClassBuilderAt;
import org.jboss.windup.rules.apps.java.scan.ast.TypeReferenceLocation;
import org.jboss.windup.util.exception.WindupException;
import org.w3c.dom.Element;

/**
 * 
 * Represents a {@link JavaClass} {@link Condition}.
 * 
 * Example:
 * 
 * <pre>
 * &lt;javaclass type="javax.servlet.http.HttpServletRequest"&gt;
 *         &lt;location&gt;METHOD_PARAMETER&lt;/location&gt;
 * &lt;/javaclass&gt;
 * </pre>
 * 
 * @author jsightler <jesse.sightler@gmail.com>
 *
 */
@NamespaceElementHandler(elementName = "javaclass", namespace = "http://windup.jboss.org/v1/xml")
public class JavaClassHandler implements ElementHandler<JavaClassBuilderAt>
{

    @Override
    public JavaClassBuilderAt processElement(ParserContext handlerManager, Element element)
                throws ConfigurationException
    {
        String type = $(element).attr("type");
        if (StringUtils.isBlank(type))
        {
            throw new WindupException("Error, 'javaclass' element must have a non-empty 'type' attribute");
        }

        List<TypeReferenceLocation> locations = new ArrayList<TypeReferenceLocation>();
        List<Element> children = $(element).children().get();
        for (Element child : children)
        {
            TypeReferenceLocation location = handlerManager.processElement(child);
            locations.add(location);
        }

        JavaClassBuilderAt javaClass = JavaClass.references(type).at(
                    locations.toArray(new TypeReferenceLocation[locations.size()]));
        return javaClass;
    }
}
