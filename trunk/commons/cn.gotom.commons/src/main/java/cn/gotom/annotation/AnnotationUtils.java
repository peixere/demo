package cn.gotom.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import cn.gotom.util.Assert;
import cn.gotom.util.StringUtils;

public abstract class AnnotationUtils
{

	static final String VALUE = "value";

	public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType)
	{
		Assert.notNull(clazz, "Class must not be null");
		A annotation = clazz.getAnnotation(annotationType);
		if (annotation != null)
		{
			return annotation;
		}
		for (Class<?> ifc : clazz.getInterfaces())
		{
			annotation = findAnnotation(ifc, annotationType);
			if (annotation != null)
			{
				return annotation;
			}
		}
		if (!Annotation.class.isAssignableFrom(clazz))
		{
			for (Annotation ann : clazz.getAnnotations())
			{
				annotation = findAnnotation(ann.annotationType(), annotationType);
				if (annotation != null)
				{
					return annotation;
				}
			}
		}
		Class<?> superClass = clazz.getSuperclass();
		if (superClass == null || superClass.equals(Object.class))
		{
			return null;
		}
		return findAnnotation(superClass, annotationType);
	}

	/**
	 * Find the first {@link Class} in the inheritance hierarchy of the specified <code>clazz</code> (including the specified <code>clazz</code> itself) which declares an annotation for the specified <code>annotationType</code>, or <code>null</code> if not found. If the supplied
	 * <code>clazz</code> is <code>null</code>, <code>null</code> will be returned.
	 * <p>
	 * If the supplied <code>clazz</code> is an interface, only the interface itself will be checked; the inheritance hierarchy for interfaces will not be traversed.
	 * <p>
	 * The standard {@link Class} API does not provide a mechanism for determining which class in an inheritance hierarchy actually declares an {@link Annotation}, so we need to handle this explicitly.
	 * 
	 * @param annotationType
	 *            the Class object corresponding to the annotation type
	 * @param clazz
	 *            the Class object corresponding to the class on which to check for the annotation, or <code>null</code>.
	 * @return the first {@link Class} in the inheritance hierarchy of the specified <code>clazz</code> which declares an annotation for the specified <code>annotationType</code>, or <code>null</code> if not found.
	 * @see Class#isAnnotationPresent(Class)
	 * @see Class#getDeclaredAnnotations()
	 */
	public static Class<?> findAnnotationDeclaringClass(Class<? extends Annotation> annotationType, Class<?> clazz)
	{
		Assert.notNull(annotationType, "Annotation type must not be null");
		if (clazz == null || clazz.equals(Object.class))
		{
			return null;
		}
		return (isAnnotationDeclaredLocally(annotationType, clazz)) ? clazz : findAnnotationDeclaringClass(annotationType, clazz.getSuperclass());
	}

	/**
	 * Determine whether an annotation for the specified <code>annotationType</code> is declared locally on the supplied <code>clazz</code>. The supplied {@link Class} may represent any type.
	 * <p>
	 * Note: This method does <strong>not</strong> determine if the annotation is {@link java.lang.annotation.Inherited inherited}. For greater clarity regarding inherited annotations, consider using {@link #isAnnotationInherited(Class, Class)} instead.
	 * 
	 * @param annotationType
	 *            the Class object corresponding to the annotation type
	 * @param clazz
	 *            the Class object corresponding to the class on which to check for the annotation
	 * @return <code>true</code> if an annotation for the specified <code>annotationType</code> is declared locally on the supplied <code>clazz</code>
	 * @see Class#getDeclaredAnnotations()
	 * @see #isAnnotationInherited(Class, Class)
	 */
	public static boolean isAnnotationDeclaredLocally(Class<? extends Annotation> annotationType, Class<?> clazz)
	{
		Assert.notNull(annotationType, "Annotation type must not be null");
		Assert.notNull(clazz, "Class must not be null");
		boolean declaredLocally = false;
		for (Annotation annotation : Arrays.asList(clazz.getDeclaredAnnotations()))
		{
			if (annotation.annotationType().equals(annotationType))
			{
				declaredLocally = true;
				break;
			}
		}
		return declaredLocally;
	}

	/**
	 * Determine whether an annotation for the specified <code>annotationType</code> is present on the supplied <code>clazz</code> and is {@link java.lang.annotation.Inherited inherited} i.e., not declared locally for the class).
	 * <p>
	 * If the supplied <code>clazz</code> is an interface, only the interface itself will be checked. In accordance with standard meta-annotation semantics, the inheritance hierarchy for interfaces will not be traversed. See the {@link java.lang.annotation.Inherited JavaDoc} for
	 * the &#064;Inherited meta-annotation for further details regarding annotation inheritance.
	 * 
	 * @param annotationType
	 *            the Class object corresponding to the annotation type
	 * @param clazz
	 *            the Class object corresponding to the class on which to check for the annotation
	 * @return <code>true</code> if an annotation for the specified <code>annotationType</code> is present on the supplied <code>clazz</code> and is {@link java.lang.annotation.Inherited inherited}
	 * @see Class#isAnnotationPresent(Class)
	 * @see #isAnnotationDeclaredLocally(Class, Class)
	 */
	public static boolean isAnnotationInherited(Class<? extends Annotation> annotationType, Class<?> clazz)
	{
		Assert.notNull(annotationType, "Annotation type must not be null");
		Assert.notNull(clazz, "Class must not be null");
		return (clazz.isAnnotationPresent(annotationType) && !isAnnotationDeclaredLocally(annotationType, clazz));
	}

	/**
	 * Retrieve the given annotation's attributes as a Map, preserving all attribute types as-is.
	 * 
	 * @param annotation
	 *            the annotation to retrieve the attributes for
	 * @return the Map of annotation attributes, with attribute names as keys and corresponding attribute values as values
	 */
	public static Map<String, Object> getAnnotationAttributes(Annotation annotation)
	{
		return getAnnotationAttributes(annotation, false);
	}

	/**
	 * Retrieve the given annotation's attributes as a Map.
	 * 
	 * @param annotation
	 *            the annotation to retrieve the attributes for
	 * @param classValuesAsString
	 *            whether to turn Class references into Strings (for compatibility with {@link org.springframework.core.type.AnnotationMetadata} or to preserve them as Class references
	 * @return the Map of annotation attributes, with attribute names as keys and corresponding attribute values as values
	 */
	public static Map<String, Object> getAnnotationAttributes(Annotation annotation, boolean classValuesAsString)
	{
		Map<String, Object> attrs = new HashMap<String, Object>();
		Method[] methods = annotation.annotationType().getDeclaredMethods();
		for (Method method : methods)
		{
			if (method.getParameterTypes().length == 0 && method.getReturnType() != void.class)
			{
				try
				{
					Object value = method.invoke(annotation);
					if (classValuesAsString)
					{
						if (value instanceof Class)
						{
							value = ((Class<?>) value).getName();
						}
						else if (value instanceof Class[])
						{
							Class<?>[] clazzArray = (Class[]) value;
							String[] newValue = new String[clazzArray.length];
							for (int i = 0; i < clazzArray.length; i++)
							{
								newValue[i] = clazzArray[i].getName();
							}
							value = newValue;
						}
					}
					attrs.put(method.getName(), value);
				}
				catch (Exception ex)
				{
					throw new IllegalStateException("Could not obtain annotation attribute values", ex);
				}
			}
		}
		return attrs;
	}

	/**
	 * Retrieve the <em>value</em> of the <code>&quot;value&quot;</code> attribute of a single-element Annotation, given an annotation instance.
	 * 
	 * @param annotation
	 *            the annotation instance from which to retrieve the value
	 * @return the attribute value, or <code>null</code> if not found
	 * @see #getValue(Annotation, String)
	 */
	public static Object getValue(Annotation annotation)
	{
		return getValue(annotation, VALUE);
	}

	/**
	 * Retrieve the <em>value</em> of a named Annotation attribute, given an annotation instance.
	 * 
	 * @param annotation
	 *            the annotation instance from which to retrieve the value
	 * @param attributeName
	 *            the name of the attribute value to retrieve
	 * @return the attribute value, or <code>null</code> if not found
	 * @see #getValue(Annotation)
	 */
	public static Object getValue(Annotation annotation, String attributeName)
	{
		try
		{
			Method method = annotation.annotationType().getDeclaredMethod(attributeName, new Class[0]);
			return method.invoke(annotation);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Retrieve the <em>default value</em> of the <code>&quot;value&quot;</code> attribute of a single-element Annotation, given an annotation instance.
	 * 
	 * @param annotation
	 *            the annotation instance from which to retrieve the default value
	 * @return the default value, or <code>null</code> if not found
	 * @see #getDefaultValue(Annotation, String)
	 */
	public static Object getDefaultValue(Annotation annotation)
	{
		return getDefaultValue(annotation, VALUE);
	}

	/**
	 * Retrieve the <em>default value</em> of a named Annotation attribute, given an annotation instance.
	 * 
	 * @param annotation
	 *            the annotation instance from which to retrieve the default value
	 * @param attributeName
	 *            the name of the attribute value to retrieve
	 * @return the default value of the named attribute, or <code>null</code> if not found
	 * @see #getDefaultValue(Class, String)
	 */
	public static Object getDefaultValue(Annotation annotation, String attributeName)
	{
		return getDefaultValue(annotation.annotationType(), attributeName);
	}

	/**
	 * Retrieve the <em>default value</em> of the <code>&quot;value&quot;</code> attribute of a single-element Annotation, given the {@link Class annotation type}.
	 * 
	 * @param annotationType
	 *            the <em>annotation type</em> for which the default value should be retrieved
	 * @return the default value, or <code>null</code> if not found
	 * @see #getDefaultValue(Class, String)
	 */
	public static Object getDefaultValue(Class<? extends Annotation> annotationType)
	{
		return getDefaultValue(annotationType, VALUE);
	}

	/**
	 * Retrieve the <em>default value</em> of a named Annotation attribute, given the {@link Class annotation type}.
	 * 
	 * @param annotationType
	 *            the <em>annotation type</em> for which the default value should be retrieved
	 * @param attributeName
	 *            the name of the attribute value to retrieve.
	 * @return the default value of the named attribute, or <code>null</code> if not found
	 * @see #getDefaultValue(Annotation, String)
	 */
	public static Object getDefaultValue(Class<? extends Annotation> annotationType, String attributeName)
	{
		try
		{
			Method method = annotationType.getDeclaredMethod(attributeName, new Class[0]);
			return method.getDefaultValue();
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Class<?>> findAnnotation(@SuppressWarnings("rawtypes") Class annotation, JarFile... jfs) throws Exception
	{
		List<Class<?>> clazzList = new ArrayList<Class<?>>();

		for (JarFile jar : jfs)
		{
			Enumeration<?> enumJar = jar.entries();
			while (enumJar.hasMoreElements())
			{
				JarEntry entry = (JarEntry) enumJar.nextElement();
				if (!entry.isDirectory())
				{
					String className = entry.getName();
					if (StringUtils.isNotEmpty(className) && className.endsWith(".class") && className.indexOf("$") == -1)
					{
						className = className.substring(0, className.lastIndexOf(".class")).replaceAll("/", ".");
						Class<?> clazz = Class.forName(className);
						if (!clazz.isEnum() && !clazz.isAnnotation() && !Modifier.isAbstract(clazz.getModifiers()) && clazz.getAnnotation(annotation) != null)
						{
							clazzList.add(clazz);
						}
					}
				}
			}
		}
		return clazzList;
	}
}
