package com.maddox.sas1946.il2.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;

/**
 * "Reflection" Class of the "SAS Common Utils"
 * <p>
 * This Class is mainly used to gain access to fields and methods of other classes where the access modifier (private/protected/(none)/public) prohibits access to that field/method.
 * <p>
 * 
 * @version 1.1.0
 * @since 1.0.1
 * @author SAS~Storebror
 */
public class Reflection {

	/**
	 * Override default Constructor to avoid instanciation
	 * 
	 * @throws Exception
	 * @since 1.0.4
	 */
	private Reflection() throws Exception {
		throw new Exception("Class com.maddox.sas1946.il2.util.Reflection cannot be instanciated!");
	}

	// *****************************************************************************************************************************************************************************************************
	// Public interface section.
	// Methods and Arguments are supposed to be final here.
	// Take care of encapsulation and don't modify methods or arguments declared on this interface
	// to ensure future backward compatibility
	// If you need a new method with the same name but different parameters or return types,
	// simply overload the given methods in this interface.
	// *****************************************************************************************************************************************************************************************************


	/**
	 * Searches for the <b><i>Instance Method</i></b> specified by "methodName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the method will be called (with no arguments) and the return value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Method resides in
	 * @param methodName
	 *            The name of the Method
	 * @return The return value of the Method
	 * @since 1.0.4
	 */
	public static final Object invokeMethod(Object obj, String methodName) {
		return doGenericInvokeMethod(obj, methodName, new Object[] {});
	}

	/**
	 * Searches for the <b><i>Instance Method</i></b> specified by "methodName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the method will be called (with given arguments from "params") and the return value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Method resides in
	 * @param methodName
	 *            The name of the Method
	 * @param params
	 *            The arguments for calling the Method
	 * @return The return value of the Method
	 * @since 1.0.4
     * @deprecated As of release 1.1.0, replaced by {@link #invokeMethod(Object obj, String methodName, Class[] classParams, Object[] params)}
	 */
	public static final Object invokeMethod(Object obj, String methodName, Object[] params) {
		return doGenericInvokeMethod(obj, methodName, params);
	}

    /**
     * Searches for the <b><i>Instance Method</i></b> specified by "methodName" within the given Object "obj" back through it's inheritance chain.<br>
     * Once found, the method will be called (with given arguments from "params") and the return value will be returned to the caller.
     *
     * @param obj
     *            The instance Object where (or in which's inherited objects) the regarding Method resides in
     * @param methodName
     *            The name of the Method
     * @param classParams
     *            Class Types of the arguments for calling the Method, e.g. Actor.class, float.class, int.class
     * @param params
     *            The arguments for calling the Method
     * @return The return value of the Method
     * @since 1.1.0
     */
    public static final Object invokeMethod(Object obj, String methodName, Class[] classParams, Object[] params) {
        return doGenericInvokeMethod(obj, methodName, classParams, params);
    }

    /**
	 * Searches for the <b><i>Static Method</i></b> specified by "methodName" within the given Class "declaringClass".<br>
	 * Once found, the method will be called (with no arguments) and the return value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Method resides in
	 * @param methodName
	 *            The name of the Method
	 * @return The return value of the Method
	 * @since 1.0.4
	 */
	public static final Object invokeMethod(Class declaringClass, String methodName) {
		return doGenericInvokeMethod(declaringClass, methodName, new Object[] {});
	}

	/**
	 * Searches for the <b><i>Static Method</i></b> specified by "methodName" within the given Class "declaringClass".<br>
	 * Once found, the method will be called (with given arguments from "params") and the return value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Method resides in
	 * @param methodName
	 *            The name of the Method
	 * @param params
	 *            The arguments for calling the Method
	 * @return The return value of the Method
	 * @since 1.0.4
     * @deprecated As of release 1.1.0, replaced by {@link #invokeMethod(Class declaringClass, String methodName, Class[] classParams, Object[] params)}
     */
	public static final Object invokeMethod(Class declaringClass, String methodName, Object[] params) {
		return doGenericInvokeMethod(declaringClass, methodName, params);
	}

    /**
     * Searches for the <b><i>Static Method</i></b> specified by "methodName" within the given Class "declaringClass".<br>
     * Once found, the method will be called (with given arguments from "params") and the return value will be returned to the caller.
     *
     * @param declaringClass
     *            The class where the Method resides in
     * @param methodName
     *            The name of the Method
     * @param classParams
     *            Class Types of the arguments for calling the Method, e.g. Actor.class, float.class, int.class
     * @param params
     *            The arguments for calling the Method
     * @return The return value of the Method
     * @since 1.1.0
     */
    public static final Object invokeMethod(Class declaringClass, String methodName, Class[] classParams, Object[] params) {
        return doGenericInvokeMethod(declaringClass, methodName, classParams, params);
    }

    /**
	 * Calls the <b><i>Instance Method</i></b> "method" which must exist in the inheritance chain of the Object "obj".<br>
	 * If the method exists, it will be called (with given arguments from "params") and the return value will be returned to the caller.
	 *
	 * @param method
	 *            The Method to invoke
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Method resides in
	 * @param params
	 *            The arguments for calling the Method
	 * @return The return value of the Method
	 * @since 1.0.4
	 */
	public static final Object invokeMethod(Method method, Object obj, Object[] params) {
		return doGenericInvokeMethod(method, obj, params);
	}

    /**
	 * Calls the <b><i>Static Method</i></b> "method" (with no arguments), the return value will be returned to the caller.
	 *
	 * @param method
	 *            The Method to invoke
	 * @return The return value of the Method
	 * @since 1.0.4
	 */
	public static final Object invokeMethod(Method method) {
		return doGenericInvokeMethod(method, (Object) null, new Object[] {});
	}

	/**
	 * Calls the <b><i>Static Method</i></b> "method" (with given arguments from "params"), the return value will be returned to the caller.
	 *
	 * @param method
	 *            The Method to invoke
	 * @param params
	 *            The arguments for calling the Method
	 * @return The return value of the Method
	 * @since 1.0.4
	 */
	public static final Object invokeMethod(Method method, Object[] params) {
		return doGenericInvokeMethod(method, (Object) null, params);
	}

    /**
	 * Searches for the <b><i>Instance Method</i></b> specified by "methodName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the method will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Method resides in
	 * @param methodName
	 *            The name of the Method
	 * @return The Method found
	 * @since 1.0.4
	 */
	public static final Method getMethod(Object obj, String methodName) {
		return doGenericGetMethod(obj, null, methodName, new Object[] {});
	}

	/**
	 * Searches for the <b><i>Instance Method</i></b> specified by "methodName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the method will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Method resides in
	 * @param methodName
	 *            The name of the Method
	 * @param params
	 *            The arguments for calling the Method
	 * @return The Method found
	 * @since 1.0.4
     * @deprecated As of release 1.1.0, replaced by {@link #getMethod(Object obj, String methodName, Class[] classParams)}
	 */
	public static final Method getMethod(Object obj, String methodName, Object[] params) {
		return doGenericGetMethod(obj, null, methodName, params);
	}

    /**
     * Searches for the <b><i>Instance Method</i></b> specified by "methodName" within the given Object "obj" back through it's inheritance chain.<br>
     * Once found, the method will be returned to the caller.
     *
     * @param obj
     *            The instance Object where (or in which's inherited objects) the regarding Method resides in
     * @param methodName
     *            The name of the Method
     * @param classParams
     *            Class Types of the arguments for calling the Method, e.g. Actor.class, float.class, int.class
     * @return The Method found
     * @since 1.1.0
     */
    public static final Method getMethod(Object obj, String methodName, Class[] classParams) {
        return doGenericGetMethod(obj, null, methodName, classParams);
    }

    /**
	 * Searches for the <b><i>Static Method</i></b> specified by "methodName" within the given Class "declaringClass".<br>
	 * Once found, the method will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Method resides in
	 * @param methodName
	 *            The name of the Method
	 * @return The Method found
	 * @since 1.0.4
	 */
	public static final Method getMethod(Class declaringClass, String methodName) {
		return doGenericGetMethod(null, declaringClass, methodName, new Object[] {});
	}

	/**
	 * Searches for the <b><i>Static Method</i></b> specified by "methodName" within the given Class "declaringClass".<br>
	 * Once found, the method will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Method resides in
	 * @param methodName
	 *            The name of the Method
	 * @param params
	 *            The arguments for calling the Method
	 * @return The Method found
	 * @since 1.0.4
     * @deprecated As of release 1.1.0, replaced by {@link #getMethod(Class declaringClass, String methodName, Class[] classParams)}
	 */
	public static final Method getMethod(Class declaringClass, String methodName, Object[] params) {
		return doGenericGetMethod(null, declaringClass, methodName, params);
	}

    /**
     * Searches for the <b><i>Static Method</i></b> specified by "methodName" within the given Class "declaringClass".<br>
     * Once found, the method will be returned to the caller.
     *
     * @param declaringClass
     *            The class where the Method resides in
     * @param methodName
     *            The name of the Method
     * @param classParams
     *            Class Types of the arguments for calling the Method, e.g. Actor.class, float.class, int.class
     * @return The Method found
     * @since 1.1.0
     */
    public static final Method getMethod(Class declaringClass, String methodName, Class[] classParams) {
        return doGenericGetMethod(null, declaringClass, methodName, classParams);
    }

    /**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field
	 * @since 1.0.4
	 */
	public static final Field getField(Object obj, String fieldName) {
		return doGenericGetField(obj, fieldName);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field
	 * @since 1.0.4
	 */
	public static final Field getField(Class declaringClass, String fieldName) {
		return doGenericGetField(declaringClass, fieldName);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final Object getValue(Object obj, String fieldName) {
		return doGenericGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final Object getValue(Class declaringClass, String fieldName) {
		return doGenericGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final boolean getBoolean(Object obj, String fieldName) {
		return doBooleanGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final boolean getBoolean(Class declaringClass, String fieldName) {
		return doBooleanGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final byte getByte(Object obj, String fieldName) {
		return doByteGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final byte getByte(Class declaringClass, String fieldName) {
		return doByteGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final char getChar(Object obj, String fieldName) {
		return doCharGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final char getChar(Class declaringClass, String fieldName) {
		return doCharGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final double getDouble(Object obj, String fieldName) {
		return doDoubleGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final double getDouble(Class declaringClass, String fieldName) {
		return doDoubleGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final float getFloat(Object obj, String fieldName) {
		return doFloatGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final float getFloat(Class declaringClass, String fieldName) {
		return doFloatGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final int getInt(Object obj, String fieldName) {
		return doIntGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final int getInt(Class declaringClass, String fieldName) {
		return doIntGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final long getLong(Object obj, String fieldName) {
		return doLongGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final long getLong(Class declaringClass, String fieldName) {
		return doLongGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final short getShort(Object obj, String fieldName) {
		return doShortGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final short getShort(Class declaringClass, String fieldName) {
		return doShortGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field type will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field type
	 * @since 1.0.4
	 */
	public static final Class getType(Object obj, String fieldName) {
		return doGenericGetFieldType(obj, fieldName);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field type will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field type
	 * @since 1.0.4
	 */
	public static final Class getType(Class declaringClass, String fieldName) {
		return doGenericGetFieldType(declaringClass, fieldName);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final String getString(Object obj, String fieldName) {
		return (String) doGenericGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.4
	 */
	public static final String getString(Class declaringClass, String fieldName) {
		return (String) doGenericGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setValue(Object obj, String fieldName, Object value) {
		doGenericSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setValue(Class declaringClass, String fieldName, Object value) {
		doGenericSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setBoolean(Object obj, String fieldName, boolean value) {
		doBooleanSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setBoolean(Class declaringClass, String fieldName, boolean value) {
		doBooleanSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setByte(Object obj, String fieldName, byte value) {
		doByteSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setByte(Class declaringClass, String fieldName, byte value) {
		doByteSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setChar(Object obj, String fieldName, char value) {
		doCharSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setChar(Class declaringClass, String fieldName, char value) {
		doCharSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setDouble(Object obj, String fieldName, double value) {
		doDoubleSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setDouble(Class declaringClass, String fieldName, double value) {
		doDoubleSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setFloat(Object obj, String fieldName, float value) {
		doFloatSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setFloat(Class declaringClass, String fieldName, float value) {
		doFloatSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setInt(Object obj, String fieldName, int value) {
		doIntSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setInt(Class declaringClass, String fieldName, int value) {
		doIntSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setLong(Object obj, String fieldName, long value) {
		doLongSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setLong(Class declaringClass, String fieldName, long value) {
		doLongSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setShort(Object obj, String fieldName, short value) {
		doShortSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a <b><i>Static Field</i></b> specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setShort(Class declaringClass, String fieldName, short value) {
		doShortSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for an <b><i>Instance Field</i></b> specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setString(Object obj, String fieldName, String value) {
		doGenericSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.4
	 */
	public static final void setString(Class declaringClass, String fieldName, String value) {
		doGenericSetFieldValue(declaringClass, fieldName, value);
	}
	
	
	
	
	
	
	
	/**
	 * Searches for the Method specified by "methodName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the method will be called (with no arguments) and the return value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Method resides in
	 * @param methodName
	 *            The name of the Method
	 * @return The return value of the Method
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #invokeMethod(Object obj, String methodName)}
	 */
	public static final Object genericInvokeMethod(Object obj, String methodName) {
		return doGenericInvokeMethod(obj, methodName, new Object[] {});
	}

	/**
	 * Searches for the Method specified by "methodName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the method will be called (with given arguments from "params") and the return value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Method resides in
	 * @param methodName
	 *            The name of the Method
	 * @param params
	 *            The arguments for calling the Method
	 * @return The return value of the Method
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #invokeMethod(Object obj, String methodName, Object[] params)}
	 */
	public static final Object genericInvokeMethod(Object obj, String methodName, Object[] params) {
		return doGenericInvokeMethod(obj, methodName, params);
	}

	/**
	 * Searches for the static Method specified by "methodName" within the given Class "declaringClass".<br>
	 * Once found, the method will be called (with no arguments) and the return value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Method resides in
	 * @param methodName
	 *            The name of the Method
	 * @return The return value of the Method
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #invokeMethod(Class declaringClass, String methodName)}
	 */
	public static final Object genericInvokeMethodStatic(Class declaringClass, String methodName) {
		return doGenericInvokeMethod(declaringClass, methodName, new Object[] {});
	}

	/**
	 * Searches for the static Method specified by "methodName" within the given Class "declaringClass".<br>
	 * Once found, the method will be called (with given arguments from "params") and the return value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Method resides in
	 * @param methodName
	 *            The name of the Method
	 * @param params
	 *            The arguments for calling the Method
	 * @return The return value of the Method
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #invokeMethod(Class declaringClass, String methodName, Object[] params)}
	 */
	public static final Object genericInvokeMethodStatic(Class declaringClass, String methodName, Object[] params) {
		return doGenericInvokeMethod(declaringClass, methodName, params);
	}

	/**
	 * Calls the Method "method" which must exist in the inheritance chain of the Object "obj".<br>
	 * If the method exists, it will be called (with given arguments from "params") and the return value will be returned to the caller.
	 *
	 * @param method
	 *            The Method to invoke
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Method resides in
	 * @param params
	 *            The arguments for calling the Method
	 * @return The return value of the Method
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #invokeMethod(Method method, Object obj, Object[] params)}
	 */
	public static final Object genericInvokeMethod(Method method, Object obj, Object[] params) {
		return doGenericInvokeMethod(method, obj, params);
	}

	/**
	 * Calls the static Method "method" (with no arguments), the return value will be returned to the caller.
	 *
	 * @param method
	 *            The Method to invoke
	 * @return The return value of the Method
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #invokeMethod(Method method)}
	 */
	public static final Object genericInvokeMethodStatic(Method method) {
		return doGenericInvokeMethod(method, (Object) null, new Object[] {});
	}

	/**
	 * Calls the static Method "method" (with given arguments from "params"), the return value will be returned to the caller.
	 *
	 * @param method
	 *            The Method to invoke
	 * @param params
	 *            The arguments for calling the Method
	 * @return The return value of the Method
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #invokeMethod(Method method, Object[] params)}
	 */
	public static final Object genericInvokeMethodStatic(Method method, Object[] params) {
		return doGenericInvokeMethod(method, (Object) null, params);
	}

	/**
	 * Searches for the Method specified by "methodName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the method will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Method resides in
	 * @param methodName
	 *            The name of the Method
	 * @return The Method found
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getMethod(Object obj, String methodName)}
	 */
	public static final Method genericGetMethod(Object obj, String methodName) {
		return doGenericGetMethod(obj, null, methodName, new Object[] {});
	}

	/**
	 * Searches for the Method specified by "methodName" within the given Object "obj" back through it's inheritance chain.<br>
	 * The parameter set of the method found must match the parameterset given in "params".<br>
	 * Once found, the method will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Method resides in
	 * @param methodName
	 *            The name of the Method
	 * @param params
	 *            The arguments for calling the Method
	 * @return The Method found
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getMethod(Object obj, String methodName, Object[] params)}
	 */
	public static final Method genericGetMethod(Object obj, String methodName, Object[] params) {
		return doGenericGetMethod(obj, null, methodName, params);
	}

	/**
	 * Searches for the static Method specified by "methodName" within the given Class "declaringClass".<br>
	 * Once found, the method will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Method resides in
	 * @param methodName
	 *            The name of the Method
	 * @return The Method found
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getMethod(Class declaringClass, String methodName)}
	 */
	public static final Method genericGetMethodStatic(Class declaringClass, String methodName) {
		return doGenericGetMethod(null, declaringClass, methodName, new Object[] {});
	}

	/**
	 * Searches for the static Method specified by "methodName" within the given Class "declaringClass".<br>
	 * The parameter set of the method found must match the parameterset given in "params".<br>
	 * Once found, the method will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Method resides in
	 * @param methodName
	 *            The name of the Method
	 * @param params
	 *            The arguments for calling the Method
	 * @return The Method found
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getMethod(Class declaringClass, String methodName, Object[] params)}
	 */
	public static final Method genericGetMethodStatic(Class declaringClass, String methodName, Object[] params) {
		return doGenericGetMethod(null, declaringClass, methodName, params);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getField(Object obj, String fieldName)}
	 */
	public static final Field genericGetField(Object obj, String fieldName) {
		return doGenericGetField(obj, fieldName);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getField(Class declaringClass, String fieldName)}
	 */
	public static final Field genericGetFieldStatic(Class declaringClass, String fieldName) {
		return doGenericGetField(declaringClass, fieldName);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getValue(Object obj, String fieldName)}
	 */
	public static final Object genericGetFieldValue(Object obj, String fieldName) {
		return doGenericGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getValue(Class declaringClass, String fieldName)}
	 */
	public static final Object genericGetFieldValueStatic(Class declaringClass, String fieldName) {
		return doGenericGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getBoolean(Object obj, String fieldName)}
	 */
	public static final boolean booleanGetFieldValue(Object obj, String fieldName) {
		return doBooleanGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getBoolean(Class declaringClass, String fieldName)}
	 */
	public static final boolean booleanGetFieldValueStatic(Class declaringClass, String fieldName) {
		return doBooleanGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getByte(Object obj, String fieldName)}
	 */
	public static final byte byteGetFieldValue(Object obj, String fieldName) {
		return doByteGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getByte(Class declaringClass, String fieldName)}
	 */
	public static final byte byteGetFieldValueStatic(Class declaringClass, String fieldName) {
		return doByteGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getChar(Object obj, String fieldName)}
	 */
	public static final char charGetFieldValue(Object obj, String fieldName) {
		return doCharGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getChar(Class declaringClass, String fieldName)}
	 */
	public static final char charGetFieldValueStatic(Class declaringClass, String fieldName) {
		return doCharGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getDouble(Object obj, String fieldName)}
	 */
	public static final double doubleGetFieldValue(Object obj, String fieldName) {
		return doDoubleGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getDouble(Class declaringClass, String fieldName)}
	 */
	public static final double doubleGetFieldValueStatic(Class declaringClass, String fieldName) {
		return doDoubleGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getFloat(Object obj, String fieldName)}
	 */
	public static final float floatGetFieldValue(Object obj, String fieldName) {
		return doFloatGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getFloat(Class declaringClass, String fieldName)}
	 */
	public static final float floatGetFieldValueStatic(Class declaringClass, String fieldName) {
		return doFloatGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getInt(Object obj, String fieldName)}
	 */
	public static final int intGetFieldValue(Object obj, String fieldName) {
		return doIntGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getInt(Class declaringClass, String fieldName)}
	 */
	public static final int intGetFieldValueStatic(Class declaringClass, String fieldName) {
		return doIntGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getLong(Object obj, String fieldName)}
	 */
	public static final long longGetFieldValue(Object obj, String fieldName) {
		return doLongGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getLong(Class declaringClass, String fieldName)}
	 */
	public static final long longGetFieldValueStatic(Class declaringClass, String fieldName) {
		return doLongGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getShort(Object obj, String fieldName)}
	 */
	public static final short shortGetFieldValue(Object obj, String fieldName) {
		return doShortGetFieldValue(obj, fieldName);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field value
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getShort(Class declaringClass, String fieldName)}
	 */
	public static final short shortGetFieldValueStatic(Class declaringClass, String fieldName) {
		return doShortGetFieldValue(declaringClass, fieldName);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field type will be returned to the caller.
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field type
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getType(Object obj, String fieldName)}
	 */
	public static final Class genericGetFieldType(Object obj, String fieldName) {
		return doGenericGetFieldType(obj, fieldName);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field type will be returned to the caller.
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @return The Field type
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #getType(Class declaringClass, String fieldName)}
	 */
	public static final Class genericGetFieldTypeStatic(Class declaringClass, String fieldName) {
		return doGenericGetFieldType(declaringClass, fieldName);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setValue(Object obj, String fieldName, Object value)}
	 */
	public static final void genericSetFieldValue(Object obj, String fieldName, Object value) {
		doGenericSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setValue(Class declaringClass, String fieldName, Object value)}
	 */
	public static final void genericSetFieldValueStatic(Class declaringClass, String fieldName, Object value) {
		doGenericSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setBoolean(Object obj, String fieldName, boolean value)}
	 */
	public static final void booleanSetFieldValue(Object obj, String fieldName, boolean value) {
		doBooleanSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setBoolean(Class declaringClass, String fieldName, boolean value)}
	 */
	public static final void booleanSetFieldValueStatic(Class declaringClass, String fieldName, boolean value) {
		doBooleanSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setByte(Object obj, String fieldName, byte value)}
	 */
	public static final void byteSetFieldValue(Object obj, String fieldName, byte value) {
		doByteSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setByte(Class declaringClass, String fieldName, byte value)}
	 */
	public static final void byteSetFieldValueStatic(Class declaringClass, String fieldName, byte value) {
		doByteSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setChar(Object obj, String fieldName, char value)}
	 */
	public static final void charSetFieldValue(Object obj, String fieldName, char value) {
		doCharSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setChar(Class declaringClass, String fieldName, char value)}
	 */
	public static final void charSetFieldValueStatic(Class declaringClass, String fieldName, char value) {
		doCharSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setDouble(Object obj, String fieldName, double value)}
	 */
	public static final void doubleSetFieldValue(Object obj, String fieldName, double value) {
		doDoubleSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setDouble(Class declaringClass, String fieldName, double value)}
	 */
	public static final void doubleSetFieldValueStatic(Class declaringClass, String fieldName, double value) {
		doDoubleSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setFloat(Object obj, String fieldName, float value)}
	 */
	public static final void floatSetFieldValue(Object obj, String fieldName, float value) {
		doFloatSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setFloat(Class declaringClass, String fieldName, float value)}
	 */
	public static final void floatSetFieldValueStatic(Class declaringClass, String fieldName, float value) {
		doFloatSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setInt(Object obj, String fieldName, int value)}
	 */
	public static final void intSetFieldValue(Object obj, String fieldName, int value) {
		doIntSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setInt(Class declaringClass, String fieldName, int value)}
	 */
	public static final void intSetFieldValueStatic(Class declaringClass, String fieldName, int value) {
		doIntSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setLong(Object obj, String fieldName, long value)}
	 */
	public static final void longSetFieldValue(Object obj, String fieldName, long value) {
		doLongSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setLong(Class declaringClass, String fieldName, long value)}
	 */
	public static final void longSetFieldValueStatic(Class declaringClass, String fieldName, long value) {
		doLongSetFieldValue(declaringClass, fieldName, value);
	}

	/**
	 * Searches for a Field specified by "fieldName" within the given Object "obj" back through it's inheritance chain.<br>
	 * Once found, the Field value will be set to the value given by "value".
	 *
	 * @param obj
	 *            The instance Object where (or in which's inherited objects) the regarding Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setShort(Object obj, String fieldName, short value)}
	 */
	public static final void shortSetFieldValue(Object obj, String fieldName, short value) {
		doShortSetFieldValue(obj, fieldName, value);
	}

	/**
	 * Searches for a static Field specified by "fieldName" within the class "declaringClass".<br>
	 * If found, the Field value will be set to the value given by "value".
	 *
	 * @param declaringClass
	 *            The class where the Field resides in
	 * @param fieldName
	 *            The name of the Field
	 * @param value
	 *            The value to set the Field to
	 * @since 1.0.1
	 * @deprecated As of release 1.0.4, replaced by {@link #setShort(Class declaringClass, String fieldName, short value)}
	 */
	public static final void shortSetFieldValueStatic(Class declaringClass, String fieldName, short value) {
		doShortSetFieldValue(declaringClass, fieldName, value);
	}


	// *****************************************************************************************************************************************************************************************************
	// Private implementation section.
	// Do whatever you like here but keep it private to this class.
	// *****************************************************************************************************************************************************************************************************

	private static Object doGenericInvokeMethod(Object obj, String methodName, Object[] params) {
		return doGenericInvokeMethod(obj, null, methodName, params);
	}

    private static Object doGenericInvokeMethod(Object obj, String methodName, Class[] classParams, Object[] params) {
        if (classParams.length != params.length) {
            InvalidParameterException ipe = new InvalidParameterException("Class Array length doesn't match Parameter Array length!");
            ipe.printStackTrace();
            return null;
        }
        return doGenericInvokeMethod(obj, null, methodName, classParams, params);
    }

    private static Object doGenericInvokeMethod(Class declaringClass, String methodName, Object[] params) {
		return doGenericInvokeMethod(null, declaringClass, methodName, params);
	}

    private static Object doGenericInvokeMethod(Class declaringClass, String methodName, Class[] classParams, Object[] params) {
        if (classParams.length != params.length) {
            InvalidParameterException ipe = new InvalidParameterException("Class Array length doesn't match Parameter Array length!");
            ipe.printStackTrace();
            return null;
        }
        return doGenericInvokeMethod(null, declaringClass, methodName, classParams, params);
    }

    private static Object doGenericInvokeMethod(Object obj, Class declaringClass, String methodName, Object[] params) {
		return doGenericInvokeMethod(doGenericGetMethod(obj, declaringClass, methodName, params), obj, params);
	}

    private static Object doGenericInvokeMethod(Object obj, Class declaringClass, String methodName, Class[] classParams, Object[] params) {
        if (classParams.length != params.length) {
            InvalidParameterException ipe = new InvalidParameterException("Class Array length doesn't match Parameter Array length!");
            ipe.printStackTrace();
            return null;
        }
        return doGenericInvokeMethod(doGenericGetMethod(obj, declaringClass, methodName, classParams), obj, params);
    }

    private static Object doGenericInvokeMethod(Method method, Object obj, Object[] params) {
		Object requiredObj = null;
		if (method != null) {
			try {
				requiredObj = method.invoke(obj, params);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return requiredObj;
	}

	private static Method doGenericGetMethod(Object obj, Class declaringClass, String methodName, Object[] params) {
		Method method = null;
		Class[] classArray = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			classArray[i] = params[i].getClass();
		}
		if (obj != null) {
			Class objClass = obj.getClass();
			do {
				if (objClass == null) {
					break;
				}
				try {
					method = objClass.getDeclaredMethod(methodName, classArray);
					method.setAccessible(true);
				} catch (Exception e) {
				}
				objClass = objClass.getSuperclass();
			} while (method == null);
		} else if (declaringClass != null) {
			try {
				method = declaringClass.getDeclaredMethod(methodName, classArray);
				method.setAccessible(true);
			} catch (Exception e) {
			}
		}
		return method;
	}

    private static Method doGenericGetMethod(Object obj, Class declaringClass, String methodName, Class[] classParams) {
        Method method = null;
        if (obj != null) {
            Class objClass = obj.getClass();
            do {
                if (objClass == null) {
                    break;
                }
                try {
                    method = objClass.getDeclaredMethod(methodName, classParams);
                    method.setAccessible(true);
                } catch (Exception e) {
                }
                objClass = objClass.getSuperclass();
            } while (method == null);
        } else if (declaringClass != null) {
            try {
                method = declaringClass.getDeclaredMethod(methodName, classParams);
                method.setAccessible(true);
            } catch (Exception e) {
            }
        }
        return method;
    }

    private static Field doGenericGetField(Object obj, String methodName) {
		return doGenericGetField(obj, null, methodName);
	}

	private static Field doGenericGetField(Class declaringClass, String methodName) {
		return doGenericGetField(null, declaringClass, methodName);
	}

	private static Field doGenericGetField(Object obj, Class declaringClass, String fieldName) {
		Field field = null;
		if (obj != null) {
			Class objClass = obj.getClass();
			do {
				if (objClass == null) {
					break;
				}
				try {
					field = objClass.getDeclaredField(fieldName);
					field.setAccessible(true);
				} catch (Exception e) {
				}
				objClass = objClass.getSuperclass();
			} while (field == null);
		} else if (declaringClass != null) {
			try {
				field = declaringClass.getDeclaredField(fieldName);
				field.setAccessible(true);
			} catch (Exception e) {
			}
		}
		return field;
	}

	private static Object doGenericGetFieldValue(Object obj, String methodName) {
		return doGenericGetFieldValue(obj, null, methodName);
	}

	private static Object doGenericGetFieldValue(Class declaringClass, String methodName) {
		return doGenericGetFieldValue(null, declaringClass, methodName);
	}

	private static Object doGenericGetFieldValue(Object obj, Class declaringClass, String fieldName) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		Object requiredObj = null;
		if (field != null) {
			try {
				requiredObj = field.get(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return requiredObj;
	}

	private static boolean doBooleanGetFieldValue(Object obj, String methodName) {
		return doBooleanGetFieldValue(obj, null, methodName);
	}

	private static boolean doBooleanGetFieldValue(Class declaringClass, String methodName) {
		return doBooleanGetFieldValue(null, declaringClass, methodName);
	}

	private static boolean doBooleanGetFieldValue(Object obj, Class declaringClass, String fieldName) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		boolean retVal = false;
		if (field != null) {
			try {
				retVal = field.getBoolean(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return retVal;
	}

	private static byte doByteGetFieldValue(Object obj, String methodName) {
		return doByteGetFieldValue(obj, null, methodName);
	}

	private static byte doByteGetFieldValue(Class declaringClass, String methodName) {
		return doByteGetFieldValue(null, declaringClass, methodName);
	}

	private static byte doByteGetFieldValue(Object obj, Class declaringClass, String fieldName) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		byte retVal = 0;
		if (field != null) {
			try {
				retVal = field.getByte(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return retVal;
	}

	private static char doCharGetFieldValue(Object obj, String methodName) {
		return doCharGetFieldValue(obj, null, methodName);
	}

	private static char doCharGetFieldValue(Class declaringClass, String methodName) {
		return doCharGetFieldValue(null, declaringClass, methodName);
	}

	private static char doCharGetFieldValue(Object obj, Class declaringClass, String fieldName) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		char retVal = 0;
		if (field != null) {
			try {
				retVal = field.getChar(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return retVal;
	}

	private static double doDoubleGetFieldValue(Object obj, String methodName) {
		return doDoubleGetFieldValue(obj, null, methodName);
	}

	private static double doDoubleGetFieldValue(Class declaringClass, String methodName) {
		return doDoubleGetFieldValue(null, declaringClass, methodName);
	}

	private static double doDoubleGetFieldValue(Object obj, Class declaringClass, String fieldName) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		double retVal = 0;
		if (field != null) {
			try {
				retVal = field.getDouble(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return retVal;
	}

	private static float doFloatGetFieldValue(Object obj, String methodName) {
		return doFloatGetFieldValue(obj, null, methodName);
	}

	private static float doFloatGetFieldValue(Class declaringClass, String methodName) {
		return doFloatGetFieldValue(null, declaringClass, methodName);
	}

	private static float doFloatGetFieldValue(Object obj, Class declaringClass, String fieldName) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		float retVal = 0;
		if (field != null) {
			try {
				retVal = field.getFloat(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return retVal;
	}

	private static int doIntGetFieldValue(Object obj, String methodName) {
		return doIntGetFieldValue(obj, null, methodName);
	}

	private static int doIntGetFieldValue(Class declaringClass, String methodName) {
		return doIntGetFieldValue(null, declaringClass, methodName);
	}

	private static int doIntGetFieldValue(Object obj, Class declaringClass, String fieldName) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		int retVal = 0;
		if (field != null) {
			try {
				retVal = field.getInt(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return retVal;
	}

	private static long doLongGetFieldValue(Object obj, String methodName) {
		return doLongGetFieldValue(obj, null, methodName);
	}

	private static long doLongGetFieldValue(Class declaringClass, String methodName) {
		return doLongGetFieldValue(null, declaringClass, methodName);
	}

	private static long doLongGetFieldValue(Object obj, Class declaringClass, String fieldName) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		long retVal = 0;
		if (field != null) {
			try {
				retVal = field.getLong(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return retVal;
	}

	private static short doShortGetFieldValue(Object obj, String methodName) {
		return doShortGetFieldValue(obj, null, methodName);
	}

	private static short doShortGetFieldValue(Class declaringClass, String methodName) {
		return doShortGetFieldValue(null, declaringClass, methodName);
	}

	private static short doShortGetFieldValue(Object obj, Class declaringClass, String fieldName) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		short retVal = 0;
		if (field != null) {
			try {
				retVal = field.getShort(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return retVal;
	}

	private static Class doGenericGetFieldType(Object obj, String methodName) {
		return doGenericGetFieldType(obj, null, methodName);
	}

	private static Class doGenericGetFieldType(Class declaringClass, String methodName) {
		return doGenericGetFieldType(null, declaringClass, methodName);
	}

	private static Class doGenericGetFieldType(Object obj, Class declaringClass, String fieldName) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		Class retVal = null;
		if (field != null) {
			try {
				retVal = field.getType();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		return retVal;
	}

	private static void doGenericSetFieldValue(Object obj, String methodName, Object value) {
		doGenericSetFieldValue(obj, null, methodName, value);
	}

	private static void doGenericSetFieldValue(Class declaringClass, String methodName, Object value) {
		doGenericSetFieldValue(null, declaringClass, methodName, value);
	}

	private static void doGenericSetFieldValue(Object obj, Class declaringClass, String fieldName, Object value) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		if (field != null) {
			try {
				field.set(obj, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

	}

	private static void doBooleanSetFieldValue(Object obj, String methodName, boolean value) {
		doBooleanSetFieldValue(obj, null, methodName, value);
	}

	private static void doBooleanSetFieldValue(Class declaringClass, String methodName, boolean value) {
		doBooleanSetFieldValue(null, declaringClass, methodName, value);
	}

	private static void doBooleanSetFieldValue(Object obj, Class declaringClass, String fieldName, boolean value) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		if (field != null) {
			try {
				field.setBoolean(obj, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private static void doByteSetFieldValue(Object obj, String methodName, byte value) {
		doByteSetFieldValue(obj, null, methodName, value);
	}

	private static void doByteSetFieldValue(Class declaringClass, String methodName, byte value) {
		doByteSetFieldValue(null, declaringClass, methodName, value);
	}

	private static void doByteSetFieldValue(Object obj, Class declaringClass, String fieldName, byte value) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		if (field != null) {
			try {
				field.setByte(obj, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private static void doCharSetFieldValue(Object obj, String methodName, char value) {
		doCharSetFieldValue(obj, null, methodName, value);
	}

	private static void doCharSetFieldValue(Class declaringClass, String methodName, char value) {
		doCharSetFieldValue(null, declaringClass, methodName, value);
	}

	private static void doCharSetFieldValue(Object obj, Class declaringClass, String fieldName, char value) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		if (field != null) {
			try {
				field.setChar(obj, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private static void doDoubleSetFieldValue(Object obj, String methodName, double value) {
		doDoubleSetFieldValue(obj, null, methodName, value);
	}

	private static void doDoubleSetFieldValue(Class declaringClass, String methodName, double value) {
		doDoubleSetFieldValue(null, declaringClass, methodName, value);
	}

	private static void doDoubleSetFieldValue(Object obj, Class declaringClass, String fieldName, double value) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		if (field != null) {
			try {
				field.setDouble(obj, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private static void doFloatSetFieldValue(Object obj, String methodName, float value) {
		doFloatSetFieldValue(obj, null, methodName, value);
	}

	private static void doFloatSetFieldValue(Class declaringClass, String methodName, float value) {
		doFloatSetFieldValue(null, declaringClass, methodName, value);
	}

	private static void doFloatSetFieldValue(Object obj, Class declaringClass, String fieldName, float value) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		if (field != null) {
			try {
				field.setFloat(obj, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private static void doIntSetFieldValue(Object obj, String methodName, int value) {
		doIntSetFieldValue(obj, null, methodName, value);
	}

	private static void doIntSetFieldValue(Class declaringClass, String methodName, int value) {
		doIntSetFieldValue(null, declaringClass, methodName, value);
	}

	private static void doIntSetFieldValue(Object obj, Class declaringClass, String fieldName, int value) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		if (field != null) {
			try {
				field.setInt(obj, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private static void doLongSetFieldValue(Object obj, String methodName, long value) {
		doLongSetFieldValue(obj, null, methodName, value);
	}

	private static void doLongSetFieldValue(Class declaringClass, String methodName, long value) {
		doLongSetFieldValue(null, declaringClass, methodName, value);
	}

	private static void doLongSetFieldValue(Object obj, Class declaringClass, String fieldName, long value) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		if (field != null) {
			try {
				field.setLong(obj, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private static void doShortSetFieldValue(Object obj, String methodName, short value) {
		doShortSetFieldValue(obj, null, methodName, value);
	}

	private static void doShortSetFieldValue(Class declaringClass, String methodName, short value) {
		doShortSetFieldValue(null, declaringClass, methodName, value);
	}

	private static void doShortSetFieldValue(Object obj, Class declaringClass, String fieldName, short value) {
		Field field = doGenericGetField(obj, declaringClass, fieldName);
		if (field != null) {
			try {
				field.setShort(obj, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

}
