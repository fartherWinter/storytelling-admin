package com.chennian.storytelling.admin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 测试覆盖率分析工具
 * 提供代码覆盖率统计、测试建议和覆盖率报告生成
 */
@Component
public class TestCoverageAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(TestCoverageAnalyzer.class);

    // 正则表达式模式
    private static final Pattern METHOD_PATTERN = Pattern.compile(
        "(public|private|protected)\\s+(?:static\\s+)?(?:\\w+\\s+)*\\w+\\s+(\\w+)\\s*\\([^)]*\\)\\s*\\{");
    private static final Pattern CLASS_PATTERN = Pattern.compile(
        "(public|private|protected)?\\s*(class|interface|enum)\\s+(\\w+)");
    private static final Pattern TEST_METHOD_PATTERN = Pattern.compile(
        "@Test\\s*(?:\\([^)]*\\))?\\s*(?:public\\s+)?(?:void\\s+)?(\\w+)\\s*\\(");
    private static final Pattern IMPORT_PATTERN = Pattern.compile(
        "import\\s+([\\w.]+);");

    /**
     * 覆盖率报告
     */
    public static class CoverageReport {
        private final String projectPath;
        private final LocalDateTime generatedAt;
        private final Map<String, ClassCoverage> classCoverages;
        private final CoverageSummary summary;
        private final List<String> recommendations;
        private final List<String> uncoveredClasses;
        private final List<String> uncoveredMethods;

        public CoverageReport(String projectPath, Map<String, ClassCoverage> classCoverages,
                            CoverageSummary summary, List<String> recommendations,
                            List<String> uncoveredClasses, List<String> uncoveredMethods) {
            this.projectPath = projectPath;
            this.generatedAt = LocalDateTime.now();
            this.classCoverages = classCoverages;
            this.summary = summary;
            this.recommendations = recommendations;
            this.uncoveredClasses = uncoveredClasses;
            this.uncoveredMethods = uncoveredMethods;
        }

        // Getters
        public String getProjectPath() { return projectPath; }
        public LocalDateTime getGeneratedAt() { return generatedAt; }
        public Map<String, ClassCoverage> getClassCoverages() { return classCoverages; }
        public CoverageSummary getSummary() { return summary; }
        public List<String> getRecommendations() { return recommendations; }
        public List<String> getUncoveredClasses() { return uncoveredClasses; }
        public List<String> getUncoveredMethods() { return uncoveredMethods; }
    }

    /**
     * 类覆盖率信息
     */
    public static class ClassCoverage {
        private final String className;
        private final String packageName;
        private final List<String> methods;
        private final List<String> testedMethods;
        private final List<String> untestedMethods;
        private final double coveragePercentage;
        private final boolean hasTestClass;
        private final String testClassName;
        private final List<String> testMethods;

        public ClassCoverage(String className, String packageName, List<String> methods,
                           List<String> testedMethods, List<String> untestedMethods,
                           double coveragePercentage, boolean hasTestClass,
                           String testClassName, List<String> testMethods) {
            this.className = className;
            this.packageName = packageName;
            this.methods = methods;
            this.testedMethods = testedMethods;
            this.untestedMethods = untestedMethods;
            this.coveragePercentage = coveragePercentage;
            this.hasTestClass = hasTestClass;
            this.testClassName = testClassName;
            this.testMethods = testMethods;
        }

        // Getters
        public String getClassName() { return className; }
        public String getPackageName() { return packageName; }
        public List<String> getMethods() { return methods; }
        public List<String> getTestedMethods() { return testedMethods; }
        public List<String> getUntestedMethods() { return untestedMethods; }
        public double getCoveragePercentage() { return coveragePercentage; }
        public boolean isHasTestClass() { return hasTestClass; }
        public String getTestClassName() { return testClassName; }
        public List<String> getTestMethods() { return testMethods; }
    }

    /**
     * 覆盖率摘要
     */
    public static class CoverageSummary {
        private final int totalClasses;
        private final int testedClasses;
        private final int totalMethods;
        private final int testedMethods;
        private final double classCoveragePercentage;
        private final double methodCoveragePercentage;
        private final double overallScore;

        public CoverageSummary(int totalClasses, int testedClasses, int totalMethods,
                             int testedMethods, double classCoveragePercentage,
                             double methodCoveragePercentage, double overallScore) {
            this.totalClasses = totalClasses;
            this.testedClasses = testedClasses;
            this.totalMethods = totalMethods;
            this.testedMethods = testedMethods;
            this.classCoveragePercentage = classCoveragePercentage;
            this.methodCoveragePercentage = methodCoveragePercentage;
            this.overallScore = overallScore;
        }

        // Getters
        public int getTotalClasses() { return totalClasses; }
        public int getTestedClasses() { return testedClasses; }
        public int getTotalMethods() { return totalMethods; }
        public int getTestedMethods() { return testedMethods; }
        public double getClassCoveragePercentage() { return classCoveragePercentage; }
        public double getMethodCoveragePercentage() { return methodCoveragePercentage; }
        public double getOverallScore() { return overallScore; }
    }

    /**
     * 分析项目测试覆盖率
     */
    public CoverageReport analyzeProjectCoverage(String projectPath) {
        log.info("开始分析项目测试覆盖率: {}", projectPath);
        
        try {
            // 扫描源代码文件
            List<Path> sourceFiles = scanSourceFiles(projectPath);
            List<Path> testFiles = scanTestFiles(projectPath);
            
            log.info("找到源文件: {} 个，测试文件: {} 个", sourceFiles.size(), testFiles.size());
            
            // 分析每个类的覆盖率
            Map<String, ClassCoverage> classCoverages = new HashMap<>();
            
            for (Path sourceFile : sourceFiles) {
                ClassCoverage coverage = analyzeClassCoverage(sourceFile, testFiles);
                if (coverage != null) {
                    classCoverages.put(coverage.getClassName(), coverage);
                }
            }
            
            // 生成摘要
            CoverageSummary summary = generateSummary(classCoverages);
            
            // 生成建议
            List<String> recommendations = generateRecommendations(classCoverages, summary);
            
            // 找出未覆盖的类和方法
            List<String> uncoveredClasses = findUncoveredClasses(classCoverages);
            List<String> uncoveredMethods = findUncoveredMethods(classCoverages);
            
            log.info("覆盖率分析完成，总体覆盖率: {:.1f}%", summary.getOverallScore());
            
            return new CoverageReport(projectPath, classCoverages, summary, 
                                    recommendations, uncoveredClasses, uncoveredMethods);
            
        } catch (Exception e) {
            log.error("分析测试覆盖率失败", e);
            throw new RuntimeException("覆盖率分析失败: " + e.getMessage(), e);
        }
    }

    /**
     * 扫描源代码文件
     */
    private List<Path> scanSourceFiles(String projectPath) throws IOException {
        Path srcPath = Paths.get(projectPath, "src", "main", "java");
        if (!Files.exists(srcPath)) {
            log.warn("源代码路径不存在: {}", srcPath);
            return Collections.emptyList();
        }
        
        try (Stream<Path> paths = Files.walk(srcPath)) {
            return paths
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .filter(path -> !path.toString().contains("Test.java"))
                .collect(Collectors.toList());
        }
    }

    /**
     * 扫描测试文件
     */
    private List<Path> scanTestFiles(String projectPath) throws IOException {
        Path testPath = Paths.get(projectPath, "src", "test", "java");
        if (!Files.exists(testPath)) {
            log.warn("测试代码路径不存在: {}", testPath);
            return Collections.emptyList();
        }
        
        try (Stream<Path> paths = Files.walk(testPath)) {
            return paths
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .filter(path -> path.toString().contains("Test"))
                .collect(Collectors.toList());
        }
    }

    /**
     * 分析单个类的覆盖率
     */
    private ClassCoverage analyzeClassCoverage(Path sourceFile, List<Path> testFiles) {
        try {
            String content = Files.readString(sourceFile);
            
            // 提取类信息
            String className = extractClassName(content);
            String packageName = extractPackageName(content);
            List<String> methods = extractMethods(content);
            
            if (className == null || methods.isEmpty()) {
                return null;
            }
            
            // 查找对应的测试类
            Path testFile = findTestFile(className, testFiles);
            boolean hasTestClass = testFile != null;
            String testClassName = hasTestClass ? extractClassName(Files.readString(testFile)) : null;
            List<String> testMethods = hasTestClass ? extractTestMethods(Files.readString(testFile)) : Collections.emptyList();
            
            // 分析哪些方法被测试覆盖
            List<String> testedMethods = findTestedMethods(methods, testMethods, className);
            List<String> untestedMethods = new ArrayList<>(methods);
            untestedMethods.removeAll(testedMethods);
            
            // 计算覆盖率
            double coveragePercentage = methods.isEmpty() ? 0.0 : 
                (double) testedMethods.size() / methods.size() * 100;
            
            return new ClassCoverage(className, packageName, methods, testedMethods,
                                   untestedMethods, coveragePercentage, hasTestClass,
                                   testClassName, testMethods);
            
        } catch (Exception e) {
            log.warn("分析类覆盖率失败: {}", sourceFile, e);
            return null;
        }
    }

    /**
     * 提取类名
     */
    private String extractClassName(String content) {
        Matcher matcher = CLASS_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(3);
        }
        return null;
    }

    /**
     * 提取包名
     */
    private String extractPackageName(String content) {
        Pattern packagePattern = Pattern.compile("package\\s+([\\w.]+);");
        Matcher matcher = packagePattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    /**
     * 提取方法名
     */
    private List<String> extractMethods(String content) {
        List<String> methods = new ArrayList<>();
        Matcher matcher = METHOD_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String methodName = matcher.group(2);
            // 排除构造函数和一些特殊方法
            if (!methodName.equals("main") && !methodName.startsWith("get") && 
                !methodName.startsWith("set") && !methodName.equals("toString") &&
                !methodName.equals("equals") && !methodName.equals("hashCode")) {
                methods.add(methodName);
            }
        }
        
        return methods;
    }

    /**
     * 提取测试方法
     */
    private List<String> extractTestMethods(String content) {
        List<String> testMethods = new ArrayList<>();
        Matcher matcher = TEST_METHOD_PATTERN.matcher(content);
        
        while (matcher.find()) {
            testMethods.add(matcher.group(1));
        }
        
        return testMethods;
    }

    /**
     * 查找测试文件
     */
    private Path findTestFile(String className, List<Path> testFiles) {
        String testClassName = className + "Test";
        
        return testFiles.stream()
            .filter(path -> path.getFileName().toString().contains(testClassName))
            .findFirst()
            .orElse(null);
    }

    /**
     * 查找被测试的方法
     */
    private List<String> findTestedMethods(List<String> methods, List<String> testMethods, String className) {
        List<String> testedMethods = new ArrayList<>();
        
        for (String method : methods) {
            boolean isTested = testMethods.stream()
                .anyMatch(testMethod -> 
                    testMethod.toLowerCase().contains(method.toLowerCase()) ||
                    testMethod.toLowerCase().contains("test" + method.toLowerCase()) ||
                    testMethod.toLowerCase().contains(method.toLowerCase() + "test"));
            
            if (isTested) {
                testedMethods.add(method);
            }
        }
        
        return testedMethods;
    }

    /**
     * 生成覆盖率摘要
     */
    private CoverageSummary generateSummary(Map<String, ClassCoverage> classCoverages) {
        int totalClasses = classCoverages.size();
        int testedClasses = (int) classCoverages.values().stream()
            .filter(ClassCoverage::isHasTestClass)
            .count();
        
        int totalMethods = classCoverages.values().stream()
            .mapToInt(coverage -> coverage.getMethods().size())
            .sum();
        
        int testedMethods = classCoverages.values().stream()
            .mapToInt(coverage -> coverage.getTestedMethods().size())
            .sum();
        
        double classCoveragePercentage = totalClasses > 0 ? 
            (double) testedClasses / totalClasses * 100 : 0.0;
        
        double methodCoveragePercentage = totalMethods > 0 ? 
            (double) testedMethods / totalMethods * 100 : 0.0;
        
        // 综合评分（类覆盖率权重30%，方法覆盖率权重70%）
        double overallScore = classCoveragePercentage * 0.3 + methodCoveragePercentage * 0.7;
        
        return new CoverageSummary(totalClasses, testedClasses, totalMethods, testedMethods,
                                 classCoveragePercentage, methodCoveragePercentage, overallScore);
    }

    /**
     * 生成改进建议
     */
    private List<String> generateRecommendations(Map<String, ClassCoverage> classCoverages, 
                                                CoverageSummary summary) {
        List<String> recommendations = new ArrayList<>();
        
        // 总体建议
        if (summary.getOverallScore() < 60) {
            recommendations.add("整体测试覆盖率较低(" + String.format("%.1f", summary.getOverallScore()) + 
                              "%)，建议优先提升核心业务逻辑的测试覆盖率");
        } else if (summary.getOverallScore() < 80) {
            recommendations.add("测试覆盖率中等(" + String.format("%.1f", summary.getOverallScore()) + 
                              "%)，建议继续完善测试用例");
        } else {
            recommendations.add("测试覆盖率良好(" + String.format("%.1f", summary.getOverallScore()) + 
                              "%)，建议关注测试质量和边界条件测试");
        }
        
        // 类覆盖率建议
        if (summary.getClassCoveragePercentage() < 70) {
            recommendations.add("类覆盖率偏低，建议为核心业务类添加测试类");
        }
        
        // 方法覆盖率建议
        if (summary.getMethodCoveragePercentage() < 60) {
            recommendations.add("方法覆盖率偏低，建议增加方法级别的单元测试");
        }
        
        // 具体类的建议
        List<ClassCoverage> lowCoverageClasses = classCoverages.values().stream()
            .filter(coverage -> coverage.getCoveragePercentage() < 50 && !coverage.getMethods().isEmpty())
            .sorted((a, b) -> Double.compare(a.getCoveragePercentage(), b.getCoveragePercentage()))
            .limit(5)
            .collect(Collectors.toList());
        
        if (!lowCoverageClasses.isEmpty()) {
            recommendations.add("优先关注以下低覆盖率类: " + 
                lowCoverageClasses.stream()
                    .map(coverage -> coverage.getClassName() + "(" + 
                           String.format("%.1f", coverage.getCoveragePercentage()) + "%)")
                    .collect(Collectors.joining(", ")));
        }
        
        // 无测试类的建议
        List<String> noTestClasses = classCoverages.values().stream()
            .filter(coverage -> !coverage.isHasTestClass() && !coverage.getMethods().isEmpty())
            .map(ClassCoverage::getClassName)
            .limit(5)
            .collect(Collectors.toList());
        
        if (!noTestClasses.isEmpty()) {
            recommendations.add("以下类缺少测试类: " + String.join(", ", noTestClasses));
        }
        
        // 测试策略建议
        recommendations.add("建议采用TDD(测试驱动开发)方式，先写测试再写实现");
        recommendations.add("建议增加集成测试和端到端测试以提升测试质量");
        recommendations.add("建议使用代码覆盖率工具(如JaCoCo)进行精确的覆盖率统计");
        
        return recommendations;
    }

    /**
     * 查找未覆盖的类
     */
    private List<String> findUncoveredClasses(Map<String, ClassCoverage> classCoverages) {
        return classCoverages.values().stream()
            .filter(coverage -> !coverage.isHasTestClass())
            .map(ClassCoverage::getClassName)
            .collect(Collectors.toList());
    }

    /**
     * 查找未覆盖的方法
     */
    private List<String> findUncoveredMethods(Map<String, ClassCoverage> classCoverages) {
        List<String> uncoveredMethods = new ArrayList<>();
        
        for (ClassCoverage coverage : classCoverages.values()) {
            for (String method : coverage.getUntestedMethods()) {
                uncoveredMethods.add(coverage.getClassName() + "." + method);
            }
        }
        
        return uncoveredMethods;
    }

    /**
     * 生成HTML格式的覆盖率报告
     */
    public String generateHtmlReport(CoverageReport report) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n")
            .append("<html>\n")
            .append("<head>\n")
            .append("    <title>测试覆盖率报告</title>\n")
            .append("    <style>\n")
            .append("        body { font-family: Arial, sans-serif; margin: 20px; }\n")
            .append("        .summary { background: #f5f5f5; padding: 15px; border-radius: 5px; margin-bottom: 20px; }\n")
            .append("        .coverage-high { color: #28a745; }\n")
            .append("        .coverage-medium { color: #ffc107; }\n")
            .append("        .coverage-low { color: #dc3545; }\n")
            .append("        table { border-collapse: collapse; width: 100%; }\n")
            .append("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n")
            .append("        th { background-color: #f2f2f2; }\n")
            .append("        .recommendations { background: #e7f3ff; padding: 15px; border-radius: 5px; margin-top: 20px; }\n")
            .append("    </style>\n")
            .append("</head>\n")
            .append("<body>\n");
        
        // 标题和摘要
        html.append("    <h1>测试覆盖率报告</h1>\n")
            .append("    <div class='summary'>\n")
            .append("        <h2>覆盖率摘要</h2>\n")
            .append("        <p>项目路径: ").append(report.getProjectPath()).append("</p>\n")
            .append("        <p>生成时间: ").append(report.getGeneratedAt()).append("</p>\n")
            .append("        <p>总体评分: <span class='").append(getCoverageClass(report.getSummary().getOverallScore()))
            .append("'>").append(String.format("%.1f%%", report.getSummary().getOverallScore())).append("</span></p>\n")
            .append("        <p>类覆盖率: ").append(String.format("%.1f%%", report.getSummary().getClassCoveragePercentage()))
            .append(" (").append(report.getSummary().getTestedClasses()).append("/")
            .append(report.getSummary().getTotalClasses()).append(")</p>\n")
            .append("        <p>方法覆盖率: ").append(String.format("%.1f%%", report.getSummary().getMethodCoveragePercentage()))
            .append(" (").append(report.getSummary().getTestedMethods()).append("/")
            .append(report.getSummary().getTotalMethods()).append(")</p>\n")
            .append("    </div>\n");
        
        // 类覆盖率详情表格
        html.append("    <h2>类覆盖率详情</h2>\n")
            .append("    <table>\n")
            .append("        <tr><th>类名</th><th>包名</th><th>方法数</th><th>已测试方法</th><th>覆盖率</th><th>测试类</th></tr>\n");
        
        for (ClassCoverage coverage : report.getClassCoverages().values()) {
            html.append("        <tr>\n")
                .append("            <td>").append(coverage.getClassName()).append("</td>\n")
                .append("            <td>").append(coverage.getPackageName()).append("</td>\n")
                .append("            <td>").append(coverage.getMethods().size()).append("</td>\n")
                .append("            <td>").append(coverage.getTestedMethods().size()).append("</td>\n")
                .append("            <td><span class='").append(getCoverageClass(coverage.getCoveragePercentage()))
                .append("'>").append(String.format("%.1f%%", coverage.getCoveragePercentage())).append("</span></td>\n")
                .append("            <td>").append(coverage.isHasTestClass() ? "✓" : "✗").append("</td>\n")
                .append("        </tr>\n");
        }
        
        html.append("    </table>\n");
        
        // 改进建议
        html.append("    <div class='recommendations'>\n")
            .append("        <h2>改进建议</h2>\n")
            .append("        <ul>\n");
        
        for (String recommendation : report.getRecommendations()) {
            html.append("            <li>").append(recommendation).append("</li>\n");
        }
        
        html.append("        </ul>\n")
            .append("    </div>\n");
        
        html.append("</body>\n")
            .append("</html>");
        
        return html.toString();
    }

    /**
     * 获取覆盖率对应的CSS类
     */
    private String getCoverageClass(double coverage) {
        if (coverage >= 80) {
            return "coverage-high";
        } else if (coverage >= 60) {
            return "coverage-medium";
        } else {
            return "coverage-low";
        }
    }

    /**
     * 生成简化的覆盖率报告（用于API返回）
     */
    public Map<String, Object> generateSimpleReport(CoverageReport report) {
        Map<String, Object> simpleReport = new HashMap<>();
        
        simpleReport.put("summary", Map.of(
            "overallScore", String.format("%.1f%%", report.getSummary().getOverallScore()),
            "classCoverage", String.format("%.1f%%", report.getSummary().getClassCoveragePercentage()),
            "methodCoverage", String.format("%.1f%%", report.getSummary().getMethodCoveragePercentage()),
            "totalClasses", report.getSummary().getTotalClasses(),
            "testedClasses", report.getSummary().getTestedClasses(),
            "totalMethods", report.getSummary().getTotalMethods(),
            "testedMethods", report.getSummary().getTestedMethods()
        ));
        
        simpleReport.put("topIssues", Map.of(
            "uncoveredClasses", report.getUncoveredClasses().stream().limit(10).collect(Collectors.toList()),
            "lowCoverageClasses", report.getClassCoverages().values().stream()
                .filter(coverage -> coverage.getCoveragePercentage() < 50)
                .map(coverage -> Map.of(
                    "className", coverage.getClassName(),
                    "coverage", String.format("%.1f%%", coverage.getCoveragePercentage())
                ))
                .limit(5)
                .collect(Collectors.toList())
        ));
        
        simpleReport.put("recommendations", report.getRecommendations());
        simpleReport.put("generatedAt", report.getGeneratedAt());
        
        return simpleReport;
    }
}