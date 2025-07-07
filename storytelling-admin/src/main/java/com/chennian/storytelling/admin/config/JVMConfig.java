package com.chennian.storytelling.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JVM配置类
 * 用于读取application-performance.yml中的JVM相关配置
 *
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jvm.performance")
public class JVMConfig {

    /**
     * 监控配置
     */
    private Monitoring monitoring = new Monitoring();

    /**
     * 内存配置
     */
    private Memory memory = new Memory();

    /**
     * GC配置
     */
    private Gc gc = new Gc();

    /**
     * 线程配置
     */
    private Threads threads = new Threads();

    /**
     * CPU配置
     */
    private Cpu cpu = new Cpu();

    /**
     * 编译器配置
     */
    private Compiler compiler = new Compiler();

    /**
     * 诊断配置
     */
    private Diagnostics diagnostics = new Diagnostics();

    @Data
    public static class Monitoring {
        private boolean enabled = true;
        private long collectionInterval = 30000L;
    }

    @Data
    public static class Memory {
        private UsageRatio usageRatio = new UsageRatio();
        private Heap heap = new Heap();
        private NonHeap nonHeap = new NonHeap();

        @Data
        public static class UsageRatio {
            private boolean enabled = true;
            private double warningThreshold = 0.8;
            private double criticalThreshold = 0.9;
        }

        @Data
        public static class Heap {
            private String initialSize = "2g";
            private String maxSize = "4g";
        }

        @Data
        public static class NonHeap {
            private String maxMetaspace = "512m";
        }
    }

    @Data
    public static class Gc {
        private String algorithm = "G1";
        private G1 g1 = new G1();
        private GcMonitoring monitoring = new GcMonitoring();

        @Data
        public static class G1 {
            private int maxPauseMillis = 200;
            private String heapRegionSize = "16m";
        }

        @Data
        public static class GcMonitoring {
            private boolean enabled = true;
            private boolean logGc = true;
            private long gcTimeThreshold = 100L;
        }
    }

    @Data
    public static class Threads {
        private ThreadMonitoring monitoring = new ThreadMonitoring();

        @Data
        public static class ThreadMonitoring {
            private boolean enabled = true;
            private int maxThreadCount = 200;
            private boolean deadlockDetection = true;
        }
    }

    @Data
    public static class Cpu {
        private CpuMonitoring monitoring = new CpuMonitoring();

        @Data
        public static class CpuMonitoring {
            private boolean enabled = true;
            private double usageThreshold = 0.8;
        }
    }

    @Data
    public static class Compiler {
        private boolean tieredCompilation = true;
    }

    @Data
    public static class Diagnostics {
        private boolean flightRecorder = true;
        private boolean heapDumpOnOom = true;
        private String heapDumpPath = "logs/heapdump.hprof";
    }
}