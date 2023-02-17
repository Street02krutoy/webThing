
package com.srit.modules.web.lib;

import java.lang.annotation.Repeatable;

@Repeatable(value = BodyKeyContainer.class)
public @interface BodyKey {
    String key();

    BodyTypes value();
}

@interface BodyKeyContainer {
    BodyKey[] value();
}