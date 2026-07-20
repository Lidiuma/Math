module lidiuma.math.processor {
    requires java.compiler;
    provides javax.annotation.processing.Processor with org.lidiuma.math.processor.Processor;
    exports org.lidiuma.math.processor;
}