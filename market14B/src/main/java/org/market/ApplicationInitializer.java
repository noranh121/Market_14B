// package org.market;

// import java.io.BufferedReader;
// import java.io.InputStream;
// import java.io.InputStreamReader;
// import java.lang.reflect.Method;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;

// import org.market.DomainLayer.backend.Market;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.ApplicationContext;
// import org.springframework.core.io.ClassPathResource;
// import org.springframework.stereotype.Component;

// @Component
// public class ApplicationInitializer implements CommandLineRunner {

//     @Autowired
//     private ApplicationContext context;
//     private Market market;

//     @Override
//     public void run(String... args) throws Exception {
//         market = (Market) context.getBean(Market.class);

//         List<MethodCall> methodCalls = readInitializationFile("initFile2.txt");
//         for (MethodCall methodCall : methodCalls) {
//             invokeMethod(methodCall);
//         }
//     }

//     private static List<MethodCall> readInitializationFile(String fileName) throws Exception {
//         List<MethodCall> methodCalls = new ArrayList<>();
//         InputStream resource = new ClassPathResource(fileName).getInputStream();
//         try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
//             String line;
//             Pattern pattern = Pattern.compile("(\\w+)\\((.*)\\);");
//             while ((line = reader.readLine()) != null) {
//                 Matcher matcher = pattern.matcher(line.trim());
//                 if (matcher.matches()) {
//                     String methodName = matcher.group(1).trim();
//                     String[] params = matcher.group(2).split(",");
//                     for (int i = 0; i < params.length; i++) {
//                         params[i] = params[i].trim().replaceAll("^\"|\"$", "");
//                     }
//                     methodCalls.add(new MethodCall(methodName, params));
//                 } else {
//                     System.out.println("No match found for line: " + line);
//                 }
//             }
//         }
//         return methodCalls;
//     }

//     private void invokeMethod(MethodCall methodCall) {
//         try {
//             Method method = findMethod(methodCall.getMethodName(), methodCall.getParams().length);
//             Object[] params = convertParams(method, methodCall.getParams());
//             method.setAccessible(true);
//             method.invoke(market, params);  // Invoke method on the market instance
//         } catch (Exception e) {
//             System.err.println("Failed to execute method: " + methodCall.getMethodName());
//             e.printStackTrace();
//         }
//     }

//     private Method findMethod(String methodName, int paramCount) throws NoSuchMethodException {
//         for (Method method : market.getClass().getDeclaredMethods()) {  // Check methods on market class
//             if (method.getName().equals(methodName) && method.getParameterCount() == paramCount) {
//                 return method;
//             }
//         }
//         throw new NoSuchMethodException("No method found with name " + methodName + " and parameter count " + paramCount);
//     }

//     private Object[] convertParams(Method method, String[] params) {
//         Class<?>[] paramTypes = method.getParameterTypes();
//         Object[] convertedParams = new Object[params.length];
//         for (int i = 0; i < params.length; i++) {
//             convertedParams[i] = convertParam(paramTypes[i], params[i]);
//         }
//         return convertedParams;
//     }

//     private Object convertParam(Class<?> paramType, String param) {
//         if (paramType == int.class || paramType == Integer.class) {
//             return Integer.parseInt(param);
//         } else if (paramType == long.class || paramType == Long.class) {
//             return Long.parseLong(param);
//         } else if (paramType == double.class || paramType == Double.class) {
//             return Double.parseDouble(param);
//         } else if (paramType == boolean.class || paramType == Boolean.class) {
//             return Boolean.parseBoolean(param);
//         } else {
//             return param;
//         }
//     }

//     private static class MethodCall {
//         private final String methodName;
//         private final String[] params;

//         public MethodCall(String methodName, String[] params) {
//             this.methodName = methodName;
//             this.params = params;
//         }

//         public String getMethodName() {
//             return methodName;
//         }

//         public String[] getParams() {
//             return params;
//         }
//     }
// }
