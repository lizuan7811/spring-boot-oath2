package spring.boot.oath2.websecurity.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.apache.el.util.ReflectionUtil;
import org.springframework.util.ReflectionUtils;

import spring.boot.oath2.websecurity.service.ConvertEntityToModel;

public class convertEntityToModelImpl implements ConvertEntityToModel {

	@Override
	public <F, D> D convertEntity(F clazF, D clazD) {

		try {
			Field[] fields = clazF.getClass().getDeclaredFields();
			Arrays.asList(fields).forEach(field -> {
				try {
					ReflectionUtils.makeAccessible(field);
					Field fieldD = clazD.getClass().getDeclaredField(field.getName());
					ReflectionUtils.makeAccessible(fieldD);
					fieldD.set(clazD, field.get(clazF));
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			});

		} catch (IllegalArgumentException | SecurityException e) {
			e.printStackTrace();
		}
		return clazD;
	}

}
