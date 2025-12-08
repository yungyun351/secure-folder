package com.marttapps.securefolder.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class ApplicationPropUtil {

	private ApplicationPropUtil() {
		throw new UnsupportedOperationException("Class should not be instantiated");
	}

	private static final Map<String, Object> root = new HashMap<>();

	static {
		try (InputStream input = ApplicationPropUtil.class.getClassLoader().getResourceAsStream("application.yaml")) {
			Map<String, Object> loaded = new Yaml().load(input);
			if (loaded != null)
				root.putAll(loaded);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String get(String... path) {
		Object current = root;
		for (String key : path) {
			if (!(current instanceof Map))
				return null;
			current = ((Map<?, ?>) current).get(key);
		}
		return current != null ? current.toString() : null;
	}

}
