//package com.chennian.storytelling.common.config;
//
//import cn.hutool.core.util.StrUtil;
//import com.chennian.storytelling.common.utils.PageParam;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import io.swagger.v3.oas.annotations.Hidden;
//import org.apache.commons.lang3.reflect.FieldUtils;
//import org.springdoc.core.annotations.ParameterObject;
//import org.springdoc.core.extractor.DelegatingMethodParameter;
//
//import org.springdoc.core.providers.JavadocProvider;
//import org.springdoc.core.providers.ObjectMapperProvider;
//import org.springdoc.core.providers.WebConversionServiceProvider;
//import org.springdoc.core.service.GenericParameterService;
//import org.springdoc.core.utils.PropertyResolverUtils;
//import org.springdoc.core.utils.SpringDocUtils;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.MethodParameter;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
///**
// * @author by chennian
// * @date 2023/3/14 16:05
// */
//@Configuration
//public class Swagger2Config {
//    static {
//        SpringDocUtils.getConfig().addAnnotationsToIgnore(JsonIgnore.class);
//    }
//
//    @Bean
//    public GenericParameterService parameterBuilder(PropertyResolverUtils propertyResolverUtils, Optional<WebConversionServiceProvider> optionalWebConversionServiceProvider,
//                                                    ObjectMapperProvider objectMapperProvider, Optional<JavadocProvider> javadocProviderOptional) {
//        return new GenericParameterService(propertyResolverUtils, delegatingMethodParameterCustomizer(),
//                optionalWebConversionServiceProvider, objectMapperProvider, javadocProviderOptional);
//    }
//
//    /**
//     * 解决@ParameterObject和@Hidden, JsonIgnore同时使用不生效的问题
//     *
//     * @return
//     */
//    private Optional<> delegatingMethodParameterCustomizer() {
//        // NOSONAR
//        return Optional.of((originalMethodParam, methodParam) -> {
//            // 这个方法类拥有的注解
//            Annotation[] annotations = originalMethodParam.getParameterType().getAnnotations();
//            boolean typeContainParameterObject = false;
//            if (annotations.length > 0) {
//                List<? extends Class<? extends Annotation>> annotationTypes = Arrays.stream(annotations).map(Annotation::annotationType).collect(Collectors.toList());
//                typeContainParameterObject = annotationTypes.contains(ParameterObject.class);
//            }
//
//            boolean hasParameterAnnotations = (originalMethodParam.hasParameterAnnotations() && originalMethodParam.hasParameterAnnotation(ParameterObject.class));
//            if (typeContainParameterObject || hasParameterAnnotations) {
//                try {
//                    if (isParameterIgnore(originalMethodParam, methodParam)) {
//                        Field field = FieldUtils.getDeclaredField(DelegatingMethodParameter.class, "additionalParameterAnnotations", true);
//                        try {
//                            field.set(methodParam, new Annotation[]{new Hidden() { // NOSONAR
//                                @Override
//                                public Class<? extends Annotation> annotationType() {
//                                    return Hidden.class;
//                                }
//                            }
//                            });
//                        } catch (IllegalArgumentException | IllegalAccessException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } catch (NoSuchFieldException | SecurityException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private boolean isParameterIgnore(MethodParameter originalMethodParam, MethodParameter methodParam) throws NoSuchFieldException, SecurityException {
//        String searchCount = "searchCount";
//        String parameterName = StrUtil.isBlank(methodParam.getParameterName()) ? "" : methodParam.getParameterName();
//        String fieldName = parameterName.indexOf('.') == -1 ? parameterName : parameterName.substring(0, parameterName.indexOf('.'));
//        // 解决mybatis-plus返回的查询参数污染的问题
//        if (originalMethodParam.getParameterType().isAssignableFrom(PageParam.class)) {
//            if (searchCount.equals(fieldName)) {
//                return true;
//            }
//        }
//        Field declaredField;
//        try {
//            declaredField = originalMethodParam.getParameterType().getDeclaredField(fieldName);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//            declaredField = originalMethodParam.getParameterType().getSuperclass().getDeclaredField(fieldName);
//        }
//        return Stream.of(declaredField.getAnnotations())
//                .filter(annot -> Arrays.asList(Hidden.class, JsonIgnore.class).contains(annot.annotationType())).count() > 0;
//    }
//}
