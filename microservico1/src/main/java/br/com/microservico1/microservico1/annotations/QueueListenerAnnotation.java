package br.com.microservico1.microservico1.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;


@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueueListenerAnnotation {

    String[] value() default {};

    String pollTimeoutSeconds() default "0";

    String maxMessagesPerPoll() default "1";

    String messageVisibilitySeconds() default "30";

}
