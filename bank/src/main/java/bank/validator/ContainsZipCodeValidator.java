package bank.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContainsZipCodeValidator implements ConstraintValidator<ContainsZipCode, String> {
	
	Pattern pattern;
	
	@Override
	public void initialize(ContainsZipCode constraintAnnotation) {
		int requiredDigits = constraintAnnotation.requiredDigits();
		pattern = Pattern.compile("\\d{" + requiredDigits +"},?");
	}

	

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null)
			return true;
		
		String[] splitted = value.split(" ");
		for (int i = 0; i < splitted.length; i++) {
			if(pattern.matcher(splitted[i]).matches())
				return true;
		}
		
		return false;
	}

	
}
