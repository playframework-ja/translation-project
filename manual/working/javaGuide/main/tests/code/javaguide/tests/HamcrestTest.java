/*
 * Copyright (C) 2009-2019 Lightbend Inc. <https://www.lightbend.com>
 */
package javaguide.tests;

// #test-hamcrest
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HamcrestTest {

  @Test
  public void testString() {
    String str = "good";
    assertThat(str, allOf(equalTo("good"), startsWith("goo")));
  }
}
// #test-hamcrest
