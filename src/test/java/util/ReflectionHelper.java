package util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionHelper<T> {

	private Class<T> target;

	public ReflectionHelper(Class<T> target) {
		this.target = target;
	}

	public T construct(Object... args) {
		Class<?>[] types = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			types[i] = args[i].getClass();
		}
		return construct(types, args);
	}

	public T construct(Class<?>[] types, Object... args) {
		if (types == null) {
			return construct(args);
		}
		if (types.length != args.length) {
			throw new RuntimeException("Incorrect number of args; expected " + types.length + ", found " + args.length);
		}

		Constructor<T> ctor;
		try {
			ctor = target.getDeclaredConstructor(types);
		} catch (NoSuchMethodException e) {
			String message = "Unable to find constructor " + target.getName() + "(";
			message += prettyPrint(types);
			message += "). Available constructors: [\n";
			for (Constructor<?> constructor : target.getDeclaredConstructors()) {
				message += "\t" + prettyPrint(constructor) + ",\n";
			}
			message += "]";
			throw new RuntimeException(message, e);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Reflection not allowed!", e);
		}

		ctor.setAccessible(true);

		try {
			return ctor.newInstance(args);
		} catch (InstantiationException e) {
			throw new RuntimeException("Attempted to construct abstract class", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("This should be unreachable", e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Error while converting parameters to correct types", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Constructor threw an exception", e);
		}
	}

	@SuppressWarnings("static-method")
	private String prettyPrint(Class<?>[] types) {
		String ret = "";
		for (int i = 0; i < types.length; i++) {
			if (i != 0) {
				ret += ", ";
			}
			ret += types[i].getName();
		}
		return ret;
	}

	private String prettyPrint(Constructor<?> ctor) {
		return target.getName() + "(" + prettyPrint(ctor.getParameterTypes()) + ")";
	}

	private String prettyPrint(Method method) {
		return target.getName() + "::" + method.getName() + "(" + prettyPrint(method.getParameterTypes()) + ")";
	}

	private String prettyPrint(Field field) {
		return target.getName() + "::" + field.getName();
	}
}
