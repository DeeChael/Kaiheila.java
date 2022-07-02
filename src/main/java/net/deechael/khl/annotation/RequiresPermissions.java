package net.deechael.khl.annotation;

import net.deechael.khl.type.Permissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface RequiresPermissions {

    Permissions[] value();

}
