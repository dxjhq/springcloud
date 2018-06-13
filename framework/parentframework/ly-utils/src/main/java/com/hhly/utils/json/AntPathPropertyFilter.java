package com.hhly.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import org.springframework.util.AntPathMatcher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * see : <span style="color:red;">https://github.com/Antibrumm/jackson-antpathfilter</span><br><br>
 *
 * Implementation that allows to set nested properties. The filter will use the
 * parents from the context to identify if a property has to be filtered.
 * <pre>
 * Example: user -&gt; manager (user)
 *
 * "id", "firstName", "lastName", "manager", "manager.id", "manager.fullName"
 *
 * {
 *  "id": "2",
 *  "firstName": "Martin",
 *  "lastName": "Frey",
 *  "manager" : {
 *    "id": "1",
 *    "fullName": "System Administrator"
 *  }
 * }
 * </pre>
 * @author Martin Frey
 */
public class AntPathPropertyFilter extends SimpleBeanPropertyFilter {

    /** The matcher. */
    private static final AntPathMatcher MATCHER = new AntPathMatcher(".");

    /** The _properties to exclude. */
    protected final Set<String> _propertiesToExclude;

    /** Set of property names to include. */
    protected final Set<String> _propertiesToInclude;

    /** Cache of patterns to test, and match results */
    private final Map<String, Boolean> matchCache = new HashMap<String, Boolean>();

    /**
     * Instantiates a new ant path property filter.
     *
     * @param properties the properties
     */
    public AntPathPropertyFilter(final String... properties) {
        super();
        _propertiesToInclude = new HashSet<String>(properties.length);
        _propertiesToExclude = new HashSet<String>(properties.length);
        for (String property : properties) {
            if (property.startsWith("-") || property.startsWith("!")) {
                _propertiesToExclude.add(property.substring(1));
            } else {
                _propertiesToInclude.add(property);
            }
        }
    }

    /**
     * Gets the path to test.
     *
     * @param writer the writer
     * @param gen    the jsonGenerator
     * @return the path to test
     */
    private String getPathToTest(final PropertyWriter writer, final JsonGenerator gen) {
        StringBuilder nestedPath = new StringBuilder();
        nestedPath.append(writer.getName());
        JsonStreamContext sc = gen.getOutputContext();
        if (sc != null) {
            sc = sc.getParent();
        }
        while (sc != null) {
            if (sc.getCurrentName() != null) {
                if (nestedPath.length() > 0) {
                    nestedPath.insert(0, ".");
                }
                nestedPath.insert(0, sc.getCurrentName());
            }
            sc = sc.getParent();
        }
        return nestedPath.toString();
    }

    /**
     * @see com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter#include(
     * com.fasterxml.jackson.databind.ser.BeanPropertyWriter)
     */
    @Override
    protected boolean include(final BeanPropertyWriter writer) {
        throw new UnsupportedOperationException("Cannot call include without JsonGenerator");
    }

    /**
     * @see com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter#include(
     * com.fasterxml.jackson.databind.ser.PropertyWriter)
     */
    @Override
    protected boolean include(final PropertyWriter writer) {
        throw new UnsupportedOperationException("Cannot call include without JsonGenerator");
    }

    /**
     * Include.
     *
     * @param writer the writer
     * @param gen    the jsonGenerator
     * @return true, if successful
     */
    protected boolean include(final PropertyWriter writer, final JsonGenerator gen) {
        String pathToTest = getPathToTest(writer, gen);
        // Check cache first
        if (matchCache.containsKey(pathToTest)) {
            return matchCache.get(pathToTest);
        }
        // Only Excludes.
        if (_propertiesToInclude.isEmpty()) {
            for (String pattern : _propertiesToExclude) {
                if (matchPath(pathToTest, pattern)) {
                    matchCache.put(pathToTest, false);
                    return false;
                }
            }
            matchCache.put(pathToTest, true);
            return true;
        }
        // Else do full check
        boolean include = false;
        // Check Includes first
        for (String pattern : _propertiesToInclude) {
            if (matchPath(pathToTest, pattern)) {
                include = true;
                break;
            }
        }
        // Might still be excluded
        if (include && !_propertiesToExclude.isEmpty()) {
            for (String pattern : _propertiesToExclude) {
                if (matchPath(pathToTest, pattern)) {
                    include = false;
                    break;
                }
            }
        }
        matchCache.put(pathToTest, include);
        return include;
    }

    /** Only uses AntPathMatcher if the pattern contains wildcards, else use simple equals */
    private boolean matchPath(String pathToTest, String pattern) {
        if (pattern.contains("*")) {
            return MATCHER.match(pattern, pathToTest);
        } else {
            return pattern.equals(pathToTest);
        }
    }

    /**
     * @see com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter#serializeAsField(
     *  java.lang.Object,
     *  com.fasterxml.jackson.core.JsonGenerator,
     *  com.fasterxml.jackson.databind.SerializerProvider,
     *  com.fasterxml.jackson.databind.ser.PropertyWriter
     * )
     */
    @Override
    public void serializeAsField(final Object pojo, final JsonGenerator gen, final SerializerProvider provider,
                                 final PropertyWriter writer) throws Exception {
        if (include(writer, gen)) {
            writer.serializeAsField(pojo, gen, provider);
        } else if (!gen.canOmitFields()) { // since 2.3
            writer.serializeAsOmittedField(pojo, gen, provider);
        }
    }
}
