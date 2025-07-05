package com.ecommerce.order.report;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TestReportGenerator {
    
    public static void main(String[] args) {
        try {
            generateHTMLReport();
            System.out.println("HTML測試報告已生成: target/test-report.html");
        } catch (Exception e) {
            System.err.println("生成報告時發生錯誤: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void generateHTMLReport() throws Exception {
        // 讀取測試結果
        TestResults results = parseTestResults();
        
        // 生成 HTML 報告
        String html = generateHTML(results);
        
        // 寫入檔案
        Path outputPath = Paths.get("target/test-report.html");
        Files.createDirectories(outputPath.getParent());
        
        Files.write(outputPath, html.getBytes(StandardCharsets.UTF_8));
    }
    
    private static TestResults parseTestResults() throws Exception {
        // 讀取所有測試結果文件
        File surefireDir = new File("target/surefire-reports");
        if (!surefireDir.exists()) {
            throw new RuntimeException("測試報告目錄不存在: " + surefireDir.getAbsolutePath());
        }
        
        TestResults aggregatedResults = new TestResults();
        
        // 查找所有 TEST-*.xml 文件
        File[] xmlFiles = surefireDir.listFiles((dir, name) -> 
            name.startsWith("TEST-") && name.endsWith(".xml"));
            
        if (xmlFiles == null || xmlFiles.length == 0) {
            throw new RuntimeException("找不到測試報告文件");
        }
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        // 聚合所有測試結果
        for (File xmlFile : xmlFiles) {
            Document document = builder.parse(xmlFile);
            Element testSuite = document.getDocumentElement();
            
            // 累加測試統計
            aggregatedResults.totalTests += Integer.parseInt(testSuite.getAttribute("tests"));
            aggregatedResults.failures += Integer.parseInt(testSuite.getAttribute("failures"));
            aggregatedResults.errors += Integer.parseInt(testSuite.getAttribute("errors"));
            aggregatedResults.skipped += Integer.parseInt(testSuite.getAttribute("skipped"));
            aggregatedResults.time += Double.parseDouble(testSuite.getAttribute("time"));
            
            // 處理測試案例
            NodeList testCases = testSuite.getElementsByTagName("testcase");
            for (int i = 0; i < testCases.getLength(); i++) {
                Element testCase = (Element) testCases.item(i);
                TestCase tc = new TestCase();
                tc.name = testCase.getAttribute("name");
                tc.className = testCase.getAttribute("classname");
                tc.time = Double.parseDouble(testCase.getAttribute("time"));
                tc.status = "PASSED"; // 預設為通過
                
                if (testCase.getElementsByTagName("failure").getLength() > 0) {
                    tc.status = "FAILED";
                } else if (testCase.getElementsByTagName("error").getLength() > 0) {
                    tc.status = "ERROR";
                }
                
                aggregatedResults.testCases.add(tc);
            }
        }
        
        // 計算通過的測試數量
        aggregatedResults.passed = aggregatedResults.totalTests - aggregatedResults.failures - aggregatedResults.errors - aggregatedResults.skipped;
        
        return aggregatedResults;
    }
    
    private static String generateHTML(TestResults results) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"zh-TW\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>BDD 測試報告 - E-commerce Order Pricing</title>\n");
        html.append("    <script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\n");
        html.append("    <style>\n");
        html.append(getCSS());
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        
        // Header
        html.append("    <header>\n");
        html.append("        <h1>🎯 BDD 測試報告</h1>\n");
        html.append("        <h2>E-commerce Order Pricing Promotions</h2>\n");
        html.append("        <p class=\"timestamp\">報告生成時間: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("</p>\n");
        html.append("    </header>\n");
        
        // Summary Cards
        html.append("    <div class=\"container\">\n");
        html.append("        <div class=\"summary-grid\">\n");
        html.append("            <div class=\"card passed\">\n");
        html.append("                <h3>✅ 通過</h3>\n");
        html.append("                <p class=\"number\">").append(results.passed).append("</p>\n");
        html.append("            </div>\n");
        html.append("            <div class=\"card failed\">\n");
        html.append("                <h3>❌ 失敗</h3>\n");
        html.append("                <p class=\"number\">").append(results.failures).append("</p>\n");
        html.append("            </div>\n");
        html.append("            <div class=\"card error\">\n");
        html.append("                <h3>⚠️ 錯誤</h3>\n");
        html.append("                <p class=\"number\">").append(results.errors).append("</p>\n");
        html.append("            </div>\n");
        html.append("            <div class=\"card total\">\n");
        html.append("                <h3>📊 總計</h3>\n");
        html.append("                <p class=\"number\">").append(results.totalTests).append("</p>\n");
        html.append("            </div>\n");
        html.append("        </div>\n");
        
        // Charts Section
        html.append("        <div class=\"charts-section\">\n");
        html.append("            <div class=\"chart-container\">\n");
        html.append("                <h3>測試結果統計</h3>\n");
        html.append("                <canvas id=\"resultsChart\"></canvas>\n");
        html.append("            </div>\n");
        html.append("            <div class=\"chart-container\">\n");
        html.append("                <h3>執行時間分析</h3>\n");
        html.append("                <canvas id=\"timeChart\"></canvas>\n");
        html.append("            </div>\n");
        html.append("        </div>\n");
        
        // Test Cases Details
        html.append("        <div class=\"test-details\">\n");
        html.append("            <h3>📋 測試案例詳情</h3>\n");
        html.append("            <div class=\"test-list\">\n");
        
        for (TestCase testCase : results.testCases) {
            String statusClass = testCase.status.toLowerCase();
            String statusIcon = getStatusIcon(testCase.status);
            
            html.append("                <div class=\"test-item ").append(statusClass).append("\">\n");
            html.append("                    <div class=\"test-header\">\n");
            html.append("                        <span class=\"status-icon\">").append(statusIcon).append("</span>\n");
            html.append("                        <span class=\"test-name\">").append(testCase.name).append("</span>\n");
            html.append("                        <span class=\"test-time\">").append(String.format("%.3fs", testCase.time)).append("</span>\n");
            html.append("                    </div>\n");
            html.append("                    <div class=\"test-description\">\n");
            html.append("                        <p>").append(getScenarioDescription(testCase.name)).append("</p>\n");
            html.append("                    </div>\n");
            html.append("                </div>\n");
        }
        
        html.append("            </div>\n");
        html.append("        </div>\n");
        
        // Performance Summary
        html.append("        <div class=\"performance-summary\">\n");
        html.append("            <h3>⚡ 性能摘要</h3>\n");
        html.append("            <div class=\"perf-grid\">\n");
        html.append("                <div class=\"perf-item\">\n");
        html.append("                    <span class=\"label\">總執行時間:</span>\n");
        html.append("                    <span class=\"value\">").append(String.format("%.3f", results.time)).append(" 秒</span>\n");
        html.append("                </div>\n");
        html.append("                <div class=\"perf-item\">\n");
        html.append("                    <span class=\"label\">平均執行時間:</span>\n");
        html.append("                    <span class=\"value\">").append(String.format("%.3f", results.time / results.totalTests)).append(" 秒</span>\n");
        html.append("                </div>\n");
        html.append("                <div class=\"perf-item\">\n");
        html.append("                    <span class=\"label\">成功率:</span>\n");
        html.append("                    <span class=\"value success-rate\">").append(String.format("%.1f", (double) results.passed / results.totalTests * 100)).append("%</span>\n");
        html.append("                </div>\n");
        html.append("            </div>\n");
        html.append("        </div>\n");
        
        html.append("    </div>\n");
        
        // JavaScript for Charts
        html.append("    <script>\n");
        html.append(generateChartJS(results));
        html.append("    </script>\n");
        
        html.append("</body>\n");
        html.append("</html>\n");
        
        return html.toString();
    }
    
    private static String getCSS() {
        return """
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                color: #333;
            }
            
            header {
                background: rgba(255, 255, 255, 0.95);
                padding: 2rem;
                text-align: center;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            }
            
            header h1 {
                color: #2c3e50;
                margin-bottom: 0.5rem;
                font-size: 2.5rem;
            }
            
            header h2 {
                color: #7f8c8d;
                font-weight: 300;
                margin-bottom: 1rem;
            }
            
            .timestamp {
                color: #95a5a6;
                font-style: italic;
            }
            
            .container {
                max-width: 1200px;
                margin: 2rem auto;
                padding: 0 1rem;
            }
            
            .summary-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 1rem;
                margin-bottom: 2rem;
            }
            
            .card {
                background: white;
                padding: 1.5rem;
                border-radius: 10px;
                text-align: center;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                transition: transform 0.3s ease;
            }
            
            .card:hover {
                transform: translateY(-5px);
            }
            
            .card h3 {
                margin-bottom: 0.5rem;
                font-size: 1.1rem;
            }
            
            .card .number {
                font-size: 2.5rem;
                font-weight: bold;
            }
            
            .card.passed .number { color: #27ae60; }
            .card.failed .number { color: #e74c3c; }
            .card.error .number { color: #f39c12; }
            .card.total .number { color: #3498db; }
            
            .charts-section {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 2rem;
                margin-bottom: 2rem;
            }
            
            .chart-container {
                background: white;
                padding: 1.5rem;
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }
            
            .chart-container h3 {
                margin-bottom: 1rem;
                color: #2c3e50;
                text-align: center;
            }
            
            .test-details {
                background: white;
                padding: 1.5rem;
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                margin-bottom: 2rem;
            }
            
            .test-details h3 {
                margin-bottom: 1rem;
                color: #2c3e50;
            }
            
            .test-item {
                border: 1px solid #ecf0f1;
                border-radius: 8px;
                margin-bottom: 0.5rem;
                overflow: hidden;
                transition: all 0.3s ease;
            }
            
            .test-item:hover {
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            }
            
            .test-header {
                display: flex;
                align-items: center;
                padding: 1rem;
                background: #f8f9fa;
            }
            
            .status-icon {
                margin-right: 0.5rem;
                font-size: 1.2rem;
            }
            
            .test-name {
                flex-grow: 1;
                font-weight: 500;
            }
            
            .test-time {
                font-size: 0.9rem;
                color: #7f8c8d;
                background: #ecf0f1;
                padding: 0.2rem 0.5rem;
                border-radius: 12px;
            }
            
            .test-description {
                padding: 0.5rem 1rem 1rem 1rem;
                color: #7f8c8d;
                font-size: 0.9rem;
            }
            
            .test-item.passed {
                border-left: 4px solid #27ae60;
            }
            
            .test-item.failed {
                border-left: 4px solid #e74c3c;
            }
            
            .test-item.error {
                border-left: 4px solid #f39c12;
            }
            
            .performance-summary {
                background: white;
                padding: 1.5rem;
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }
            
            .performance-summary h3 {
                margin-bottom: 1rem;
                color: #2c3e50;
            }
            
            .perf-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 1rem;
            }
            
            .perf-item {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 0.75rem;
                background: #f8f9fa;
                border-radius: 6px;
            }
            
            .perf-item .label {
                font-weight: 500;
                color: #2c3e50;
            }
            
            .perf-item .value {
                font-weight: bold;
                color: #3498db;
            }
            
            .success-rate {
                color: #27ae60 !important;
            }
            
            @media (max-width: 768px) {
                .charts-section {
                    grid-template-columns: 1fr;
                }
                
                .summary-grid {
                    grid-template-columns: 1fr 1fr;
                }
                
                header h1 {
                    font-size: 2rem;
                }
            }
        """;
    }
    
    private static String getStatusIcon(String status) {
        return switch (status) {
            case "PASSED" -> "✅";
            case "FAILED" -> "❌";
            case "ERROR" -> "⚠️";
            default -> "❓";
        };
    }
    
    private static String getScenarioDescription(String name) {
        return switch (name) {
            // Order Pricing Scenarios
            case "Single product without promotions" -> "測試單一商品無促銷活動的基本訂單處理邏輯";
            case "Threshold discount applies when subtotal reaches 1000" -> "驗證滿額折扣促銷活動在達到門檻時正確計算折扣";
            case "Buy-one-get-one for cosmetics - multiple products" -> "測試化妝品類別的買一送一促銷活動，涵蓋多種商品";
            case "Buy-one-get-one for cosmetics - same product twice" -> "驗證同一化妝品商品購買多個時的買一送一邏輯";
            case "Buy-one-get-one for cosmetics - mixed categories" -> "測試混合商品類別時，僅化妝品享有買一送一優惠";
            case "Multiple promotions stacked" -> "驗證多重促銷活動同時生效時的正確計算邏輯";
            
            // Double 11 Scenarios
            case "購買 12 件襪子 - 一組享有折扣，剩餘按原價" -> "測試雙十一優惠：12件商品，10件享20%折扣，2件原價 (10×100×80% + 2×100 = 1000)";
            case "購買 27 件襪子 - 兩組享有折扣，剩餘按原價" -> "測試雙十一優惠：27件商品，20件享20%折扣，7件原價 (2組×10×100×80% + 7×100 = 2300)";
            case "購買 10 件不同商品 - 無折扣因為非同一種商品" -> "測試雙十一優惠：10種不同商品各1件，無折扣（需同種商品才享折扣）";
            
            // Default
            default -> "BDD 測試案例 - " + name;
        };
    }
    
    private static String generateChartJS(TestResults results) {
        StringBuilder js = new StringBuilder();
        
        // Results Chart (Doughnut)
        js.append("const resultsCtx = document.getElementById('resultsChart').getContext('2d');\n");
        js.append("new Chart(resultsCtx, {\n");
        js.append("    type: 'doughnut',\n");
        js.append("    data: {\n");
        js.append("        labels: ['通過', '失敗', '錯誤'],\n");
        js.append("        datasets: [{\n");
        js.append("            data: [").append(results.passed).append(", ").append(results.failures).append(", ").append(results.errors).append("],\n");
        js.append("            backgroundColor: ['#27ae60', '#e74c3c', '#f39c12'],\n");
        js.append("            borderWidth: 2,\n");
        js.append("            borderColor: '#fff'\n");
        js.append("        }]\n");
        js.append("    },\n");
        js.append("    options: {\n");
        js.append("        responsive: true,\n");
        js.append("        plugins: {\n");
        js.append("            legend: {\n");
        js.append("                position: 'bottom'\n");
        js.append("            }\n");
        js.append("        }\n");
        js.append("    }\n");
        js.append("});\n\n");
        
        // Time Chart (Bar)
        js.append("const timeCtx = document.getElementById('timeChart').getContext('2d');\n");
        js.append("new Chart(timeCtx, {\n");
        js.append("    type: 'bar',\n");
        js.append("    data: {\n");
        js.append("        labels: [");
        for (int i = 0; i < results.testCases.size(); i++) {
            js.append("'案例 ").append(i + 1).append("'");
            if (i < results.testCases.size() - 1) js.append(", ");
        }
        js.append("],\n");
        js.append("        datasets: [{\n");
        js.append("            label: '執行時間 (秒)',\n");
        js.append("            data: [");
        for (int i = 0; i < results.testCases.size(); i++) {
            js.append(String.format("%.3f", results.testCases.get(i).time));
            if (i < results.testCases.size() - 1) js.append(", ");
        }
        js.append("],\n");
        js.append("            backgroundColor: '#3498db',\n");
        js.append("            borderColor: '#2980b9',\n");
        js.append("            borderWidth: 1\n");
        js.append("        }]\n");
        js.append("    },\n");
        js.append("    options: {\n");
        js.append("        responsive: true,\n");
        js.append("        scales: {\n");
        js.append("            y: {\n");
        js.append("                beginAtZero: true,\n");
        js.append("                title: {\n");
        js.append("                    display: true,\n");
        js.append("                    text: '執行時間 (秒)'\n");
        js.append("                }\n");
        js.append("            }\n");
        js.append("        },\n");
        js.append("        plugins: {\n");
        js.append("            legend: {\n");
        js.append("                display: false\n");
        js.append("            }\n");
        js.append("        }\n");
        js.append("    }\n");
        js.append("});\n");
        
        return js.toString();
    }
    
    static class TestResults {
        int totalTests;
        int passed;
        int failures;
        int errors;
        int skipped;
        double time;
        List<TestCase> testCases = new ArrayList<>();
    }
    
    static class TestCase {
        String name;
        String className;
        double time;
        String status;
    }
}
