package bank.service;

import java.util.Arrays;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class LoggingInterceptor {

	@AroundInvoke
	public Object log(InvocationContext ic) throws Exception {
		String methodName = ic.getMethod().getName();
		Class<? extends Object> clazz = ic.getTarget().getClass();
		Object[] parameters = ic.getParameters();
		System.out.format("Method %s called on %s with arguments %s%n", methodName, clazz, Arrays.toString(parameters));
		
		Object retVal = ic.proceed();
		//retVal-lal csinálhatunk bármit
		return retVal;
	}
	
}
