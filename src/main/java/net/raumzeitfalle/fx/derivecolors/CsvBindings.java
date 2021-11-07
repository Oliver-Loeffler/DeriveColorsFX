package net.raumzeitfalle.fx.derivecolors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class CsvBindings {
	private static final String COLUMN_SEPARATOR = ",";
	public void toCsv(Collection<DerivedColorBean> payload, FileWriter writer) throws IOException {
		List<Method> accessors = Arrays.stream(DerivedColorBean.class.getDeclaredMethods())
				                     .filter(this::isGetterMethod)
				                     .sorted(this::byMethodName)
				                     .toList();
		
	    String header = accessors.stream()
					             .map(this::columnNameFromGetter)
					             .map(String::toUpperCase)
					             .map(this::inQuotes)
					             .collect(Collectors.joining(COLUMN_SEPARATOR));
	    
	    List<String> lines = new ArrayList<>();
	    lines.add(header);
	    for (DerivedColorBean bean : payload) {
	    	List<String> results = new ArrayList<>(accessors.size());
	    	for (Method m : accessors) {
	    		try {
					Object result = m.invoke(bean);
					String resultValue = String.valueOf(result);
					if (result instanceof Number) {
						results.add(inQuotes(resultValue));
					} else if (result instanceof String) {
						results.add(inQuotes(resultValue));
					} else {
						throwUnsupportedResultType(results);
					}
				} catch (Exception e) {
					throwMethodInvocationError(e, m);
				} 
	    	}
	    	lines.add(results.stream().collect(Collectors.joining(COLUMN_SEPARATOR)));
	    }
	    
    	for (int i = 0; i < lines.size(); i++) {
    		writer.write(lines.get(i));
    		if (i < lines.size()) {
    			writer.append(System.lineSeparator());
    		}
    	}
	}

	private void throwMethodInvocationError(Exception cause, Method m) {
		String template = "Error during on attempt to invoke method: %s";
		String message = String.format(template, m.getName());
		throw new UnsupportedOperationException(message, cause);
	}

	private void throwUnsupportedResultType(Object result) {
		String template = "Unsupported result type: %s";
		String message = String.format(template, result.getClass().getName());
	    throw new UnsupportedOperationException(message);
	}

	private boolean isGetterMethod(Method m) {
		return m.getName().startsWith("get");
	}

	public List<DerivedColorBean> fromCsv(FileReader reader) {
		Method[] methods = DerivedColorBean.class.getDeclaredMethods();
		Map<String,Method> methodHandles = new HashMap<>();
		for (Method m : methods) {
			if (m.getName().startsWith("set")) {
				methodHandles.put(m.getName()
						           .replace("set", "")
						           .toUpperCase(), m);
			}
		}
		
		List<DerivedColorBean> beans = new ArrayList<>();
		String line = null;
		try (BufferedReader br = new BufferedReader(reader)) {
			line = br.readLine();
			String[] header = line.split(COLUMN_SEPARATOR);
			Map<Integer,String> methodMapping = new HashMap<>();
			for (int m = 0; m < header.length; m++) {
				String setterName = withoutQuotes(header[m]).toUpperCase();
				methodMapping.put(m, setterName);
			}
			
			Set<String> mappedMethods = methodMapping.entrySet().stream()
			                        .map(Entry::getValue)
			                        .map(this::withoutQuotes)
			                        .collect(Collectors.toSet());
			for (String reqMethod : methodHandles.keySet()) {
				if (!mappedMethods.contains(reqMethod)) {
					throw new IllegalStateException("Unsupported CSV format!");
				}
 			}	 
			
			String[] items = null;
			int i = 0;
			while ((line = br.readLine()) != null) {
				DerivedColorBean bean = new DerivedColorBean();
				items = line.split(COLUMN_SEPARATOR);
				for (i = 0; i < items.length; i++) {
					String methodToCall = methodMapping.get(i);
					if (methodHandles.containsKey(methodToCall)) {
						Method m = methodHandles.get(methodToCall);
						Class<?>[] types = m.getParameterTypes();
						if (types.length == 1) {
							Class<?> argType = types[0];
							if (argType.equals(String.class)) {
								String arg = removeQuotes(items[i]);
								m.invoke(bean, arg);
								continue;
							} else if (argType.equals(Double.class)) {
								double arg = Double.parseDouble(removeQuotes(items[i]).trim());
								m.invoke(bean, arg);
								continue;
							} else if (argType.getName().equals("double")) {
								double arg = Double.parseDouble(removeQuotes(items[i]).trim());
								m.invoke(bean, arg);
								continue;
							} else {
								throwUnsupportedSetterArgumentType(argType);
							}
						} else {
							throwUnsupportedNumberOfSetterArguments(types.length);			
						}
					} else {
						throwUnsupportedColumnError(bean.getClass(), methodToCall);
					}
				}
				beans.add(bean);
			}
		} catch (Exception error) {
			throw new IllegalStateException(error);
		}
		return beans;
	}

	private void throwUnsupportedColumnError(Class<?> type, String methodToCall) {
		String template = "The method %s to be called is not available for type: %s.";
		String message = String.format(template, methodToCall, type.getName());
	    throw new UnsupportedOperationException(message);
	}

	private void throwUnsupportedNumberOfSetterArguments(int length) {
		String template = "Only one setter argument is supported but there are %d arguments expected.";
		String message = String.format(template, length);
	    throw new UnsupportedOperationException(message);
	}

	private void throwUnsupportedSetterArgumentType(Class<?> argType) {
		String template = "Unsupported setter argument type: %s";
		String message = String.format(template, argType.getName());
	    throw new UnsupportedOperationException(message);
	}

	private String removeQuotes(String item) {
		return item.replace("\"","");
	}
	
	private String inQuotes(String source) {
		return "\""+source.trim()+"\"";
	}
	
	private String columnNameFromGetter(Method setterMethod) {
		return setterMethod.getName()
				           .replace("get","");
	}
	
	private String withoutQuotes(String fieldName) {
		return fieldName.trim().replace("\"", "");
	}

	private int byMethodName(Method a, Method b) {
		return a.getName().compareTo(b.getName());
	}
}
