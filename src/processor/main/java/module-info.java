import org.lidiuma.math.processor.processing.Processor;

module lidiuma.math.processor {
    requires java.compiler;
    provides javax.annotation.processing.Processor with Processor;
    exports org.lidiuma.math.processor;
}