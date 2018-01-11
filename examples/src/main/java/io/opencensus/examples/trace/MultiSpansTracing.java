/*
 * Copyright 2017, OpenCensus Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opencensus.examples.trace;

import java.util.logging.Level;
import java.util.logging.Logger;

import io.opencensus.exporter.trace.logging.LoggingExporter;
import io.opencensus.exporter.trace.zipkin.ZipkinExporter;
import io.opencensus.trace.Span;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;

/** Example showing how to directly create a child {@link Span} and add annotations. */
public final class MultiSpansTracing {
  // Per class Tracer.
  private static final Tracer tracer = Tracing.getTracer();

  private MultiSpansTracing() {}

  private static void doWork() {
    Span rootSpan = tracer.spanBuilderWithExplicitParent("MyRootSpan", null).startSpan();
    rootSpan.addAnnotation("Annotation to the root Span before child is created.");
    Span childSpan = tracer.spanBuilderWithExplicitParent("MyChildSpan", rootSpan).startSpan();
    childSpan.addAnnotation("Annotation to the child Span");
    childSpan.end();
    rootSpan.addAnnotation("Annotation to the root Span after child is ended.");
    rootSpan.end();
  }

  /**
   * Main method.
   *
   * @param args the main arguments.
   */
  public static void main(String[] args) throws InterruptedException {
		ZipkinExporter.createAndRegister("http://127.0.0.1:9411/api/v2/spans", MultiSpansTracing.class.getSimpleName());
//	    LoggingExporter.register();
    LoggingExporter.register();
    doWork();
    // Wait for the Exporter to log the spans before exit. Exporter run every 5 second hardcoded.
    Thread.sleep(6000);
    Logger.getLogger(MultiSpansScopedTracing.class.getName()).log(Level.INFO, "Done");
  }
}
