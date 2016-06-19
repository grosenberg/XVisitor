/*******************************************************************************
 * Copyright (c) 2008-2014 G Rosenberg. All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.certiv.antlr.runtime.xvisitor.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reflect {

	public static final Class<?>[] emptyParams = new Class[] {};
	public static final Object[] emptyArgs = new Object[] {};

	private Reflect() {}

	public static void set(Object target, String fieldName, Object value) {
		try {
			Field f = target.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			f.set(target, value);
		} catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void setSuper(Object target, String fieldName, Object value) {
		try {
			Field f = target.getClass().getSuperclass().getDeclaredField(fieldName);
			f.setAccessible(true);
			f.set(target, value);
		} catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the value of the named field in the target class
	 * 
	 * @param target
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static Object get(Object target, String fieldName) throws NoSuchFieldException {
		try {
			Field f = target.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			return f.get(target);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {}
		return null;
	}

	public static Object getSuper(Object target, String fieldName) {
		try {
			Field f = target.getClass().getSuperclass().getDeclaredField(fieldName);
			f.setAccessible(true);
			return f.get(target);
		} catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {}
		return null;
	}

	public static Object invoke(Object target, String methodName, Class<?>[] params, Object[] args) {

		try {
			Method m = target.getClass().getMethod(methodName, params);
			m.setAccessible(true);
			return m.invoke(target, args);
		} catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException |
				InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object invokeSuperDeclared(Object target, String methodName, Class<?>[] params, Object[] args) {

		try {
			Method m = target.getClass().getSuperclass().getDeclaredMethod(methodName, params);
			m.setAccessible(true);
			return m.invoke(target, args);
		} catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException |
				InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Class<?>> declaredClasses(Object target) {
		Map<String, Class<?>> classNames = new HashMap<>();
		for (Class<?> c : target.getClass().getDeclaredClasses()) {
			classNames.put(c.getSimpleName(), c);
		}
		return classNames;
	}

	public static Object make(Class<?> clazz, Object[] args) {
		Constructor<?> c = clazz.getDeclaredConstructors()[0];
		c.setAccessible(true);
		Object object = null;
		try {
			object = c.newInstance(args);
		} catch (SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return object;
	}

	public static String simpleClassName(Object arg) {
		String name = arg.getClass().getCanonicalName();
		if (name != null) {
			int mark = name.lastIndexOf('.');
			if (mark > 0) {
				return name.substring(mark + 1);
			}
		}
		return "<unknown>";
	}

	public static <T> T instantiate(final String className, final Class<T> type) {
		try {
			return type.cast(Class.forName(className).newInstance());
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////

	public static List<String> getMethodReturnDesc(Object target, String methodName) {
		return getMethodReturnDesc(target, methodName, emptyParams);
	}

	public static List<String> getMethodReturnDesc(Object target, String methodName, Class<?>[] params) {
		List<String> types = new ArrayList<>();
		Method m = getMethod(target, methodName, params);
		if (m != null) {
			Type rType = m.getGenericReturnType();
			if (rType instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) rType;
				types.add(pt.getRawType().getTypeName());
				for (Type args : pt.getActualTypeArguments()) {
					types.add(args.getTypeName());
				}
			} else {
				types.add(rType.getTypeName());
			}
		}
		return types;
	}

	public static Class<?> getMethodReturnType(Object target, String methodName, Class<?>[] params) {
		Method m = getMethod(target, methodName, params);
		if (m != null) return m.getReturnType();
		return null;
	}

	public static Method getMethod(Object target, String methodName, Class<?>[] params) {
		try {
			return target.getClass().getMethod(methodName, params);
		} catch (SecurityException | NoSuchMethodException e) {}
		return null;
	}

	/**
	 * Returns a list containing the FQ typename, followed by the FQ typenames of the
	 * generic parameters, if any, for the named field in the target class. The list is
	 * empty if there is no such named field.
	 */
	public static List<String> getFieldTypeNames(Object target, String fieldname) {
		List<String> types = new ArrayList<>();
		try {
			Field f = getField(target, fieldname);
			types.add(f.getType().getTypeName());
			for (Type args : f.getType().getTypeParameters()) {
				types.add(args.getTypeName());
			}
		} catch (NoSuchFieldException e) {}
		return types;
	}

	/**
	 * Returns the field object for the named field in the target class. Throws a
	 * NoSuchFieldException if there is no such named field.
	 * 
	 * @param target
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static Field getField(Object target, String fieldName) throws NoSuchFieldException {
		return target.getClass().getDeclaredField(fieldName);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////

	// public static Type[] getParameterizedTypes(Class<?> target) {
	// Type targetType = target.getGenericSuperclass();
	// if (targetType instanceof ParameterizedType) {
	// return ((ParameterizedType) targetType).getActualTypeArguments();
	// }
	// return null;
	// }
}
