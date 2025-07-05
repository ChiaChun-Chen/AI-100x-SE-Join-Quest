# AI-100x-SE-Join-Quest
水球軟體學院 - AI 軟工百倍研究組織入會任務之作答

《 AI 軟工百倍研究組織》是一場由《水球軟體學院》發起的技術研究社群，目標是集結全台具備軟體開發能力的工程師，共同推進 AI × BDD 開發流程的研究與實踐。

「如果大家都關注 AI x BDD 這件事，台灣軟工進度就有機會超前國外；
當國外 AI 軟工都只會寫 rules 時，我們就已經全部都在寫 spec，產值絕對爆增。」

### 本組織將專注於以下目標

1. 本組織相信 AI x BDD 的方法，一定能讓 AI 在背景就產出 80%~90% 可靠且正確的程式，而這一定是未來 Vibe Coding 的趨勢，你一定是想要追求最前沿的軟工技術才加入本組織。

2. 組織規劃好了初步研究藍圖，分為底下三大路線，每一大路線賞金十萬：​你的參與，不僅代表你願意走在 AI 軟體開發方法論的最前線，更代表你願意投身於一場嚴謹、務實、強調產出價值與技術驗證的研究歷程。

a. 開發流程全自動化（後端）— Feature file (BDD) 到 API Spec/ERD 到程式
b. 開發流程全自動化（前端）— 線框 到 User-flow (BDD) 到程式
c. 回饋流程智能化 (全端) — 前後端整合自動化建立新的驗收測試

這三者只要都研究完成，那 Vibe Coding 才算是成熟，軟體工程師能與與 AI 「平行」合作帶來百倍產出，故稱「AI 百倍軟工研究組織」。

### 歡迎所有人參與

你的參與，不僅代表你願意走在 AI 軟體開發方法論的最前線，更代表你願意投身於一場嚴謹、務實、強調產出價值與技術驗證的研究歷程（所有的研究紀錄都會使用 Github Repository 保存脈絡）。

報名方法：
1. 加入水球軟體學院 Discord：https://discord.gg/uWGTF7RSnW
2. 照著此 Discord 社群內 #加入研究計劃 置頂訊息的指示進行即可成功報名

若你已準備好成為推動 AI × BDD 開發方法的革新者，誠摯邀請你完成報名，與來自全台的技術夥伴攜手共創。

## 🚀 快速開始

### 系統需求
- Java 17 或更高版本
- Maven 3.6 或更高版本

### 專案結構
```
src/
├── main/java/com/ecommerce/order/
│   ├── model/           # 領域模型（Order, Product, Promotion 等）
│   └── service/         # 業務邏輯服務
└── test/
    ├── java/com/ecommerce/order/
    │   ├── AllFeaturesTest.java      # 統一測試入口
    │   ├── steps/
    │   │   └── OrderStepDefinitions.java  # BDD 步驟定義
    │   └── report/
    │       └── TestReportGenerator.java   # HTML 報告生成器
    └── resources/features/
        ├── order.feature       # 訂單促銷功能規格
        └── double11.feature    # 雙十一優惠功能規格
```

## 🧪 測試執行

### 運行所有 BDD 測試
```bash
# 執行所有功能測試
mvn clean test

# 或者指定執行統一測試入口
mvn test -Dtest=AllFeaturesTest
```

### 運行特定功能測試
```bash
# 如果只想測試特定 feature，可以使用 Cucumber 標籤
mvn test -Dcucumber.filter.tags="@order_pricing"
mvn test -Dcucumber.filter.tags="@double11_promotion"
```

### 測試結果說明
執行完成後會顯示類似以下結果：
```
[INFO] Results:
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

- **Tests run**: 執行的測試案例數量
- **Failures**: 測試失敗的案例數量  
- **Errors**: 測試錯誤的案例數量
- **Skipped**: 跳過的測試案例數量

## 📊 HTML 測試報告生成

### 生成詳細 HTML 報告
```bash
# 1. 先執行測試以生成測試結果
mvn clean test

# 2. 生成 HTML 報告
java -cp target/test-classes com.ecommerce.order.report.TestReportGenerator
```

### 查看報告
報告生成後會顯示：
```
HTML測試報告已生成: target/test-report.html
```

在瀏覽器中打開 `target/test-report.html` 即可查看包含以下內容的詳細報告：

- 📈 **測試統計圖表** - 通過/失敗/錯誤的分布圓餅圖
- ⏱️ **執行時間分析** - 各測試案例的執行時間柱狀圖  
- 📋 **測試案例詳情** - 每個 scenario 的狀態和描述
- ⚡ **性能摘要** - 總執行時間、平均時間、成功率等指標

### 報告功能特色
- 🎨 響應式設計，支援手機和桌面瀏覽
- 📊 互動式圖表使用 Chart.js
- 🔍 詳細的 scenario 描述和業務邏輯說明
- 📈 視覺化的測試結果統計

## 🎯 功能說明

### 實現的促銷功能

#### 1. 基本訂單處理 (`order.feature`)
- ✅ 單一商品無促銷的基本計算
- ✅ 滿額折扣（達 1000 元享 100 元折扣）
- ✅ 美妝商品買一送一促銷
- ✅ 多重促銷疊加計算

#### 2. 雙十一優惠 (`double11.feature`)  
- ✅ 同商品每 10 件享 20% 折扣
- ✅ 多組折扣計算（如 27 件 = 2組×10件享折扣 + 7件原價）
- ✅ 不同商品不適用批量折扣規則

### BDD 開發流程展示
本專案完整展示了嚴謹的 BDD (Behavior-Driven Development) 開發流程：

1. **Given-When-Then** 格式的功能規格
2. **Cucumber** 測試框架整合
3. **步驟定義 (Step Definitions)** 的實現
4. **領域模型** 的設計和實作
5. **測試報告** 的自動化生成
