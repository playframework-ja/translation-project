/*
 * Copyright (C) 2009-2019 Lightbend Inc. <https://www.lightbend.com>
 */
package javaguide.tests.guice;

// #component-module
import com.google.inject.AbstractModule;

public class ComponentModule extends AbstractModule {
  protected void configure() {
    bind(Component.class).to(DefaultComponent.class);
  }
}
// #component-module
