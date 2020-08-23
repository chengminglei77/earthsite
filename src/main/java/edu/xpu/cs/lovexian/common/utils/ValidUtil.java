package edu.xpu.cs.lovexian.common.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public class ValidUtil {

    public static String getAllErrorMessage(BindingResult bindingResult) {
        StringBuilder result = new StringBuilder();
        for (ObjectError error : bindingResult.getAllErrors()) {
            System.out.println(error);
            result.append(error.getDefaultMessage()).append(";");
        }
        return result.toString();
    }
}