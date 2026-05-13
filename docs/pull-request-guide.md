# Pull Request Collaboration Guide

本文件是本專案用來教學的 pull request 範例準則。目標不是把 PR 寫得很長，而是讓 reviewer 能快速理解「為什麼改、改了什麼、怎麼驗證、希望討論什麼」。

本專案的 PR 內容以中文為主，方便組員與同學直接閱讀。必要的技術名詞、class name、method name 與指令可以保留英文。

## Good PR Shape

一個好的 PR 建議包含：

- 清楚的標題：用一句話說明主要改動，例如 `新增第一階段架構文件與 PR 範本`。
- 背景：說明這次改動服務哪個階段、哪個問題或哪個教學目的。
- 變更摘要：列出 reviewer 應該優先看的檔案與行為。
- 驗證方式：提供可以重現的編譯、執行或人工檢查步驟。
- 討論點：主動指出希望 reviewer 給意見的地方。
- 後續工作：說明哪些東西刻意留到下一階段，避免 PR 範圍失控。

## Suggested Phase Plan

### Phase 1: Playable Core and Collaboration Baseline

重點是完成可遊玩的 Tetris，並建立清楚的協作方式。

- 保留目前 Swing 基礎遊戲。
- 補上架構文件與 UML。
- 補上 PR template。
- 讓 reviewer 能從文件知道遊戲怎麼啟動、哪些 class 負責哪些事。

### Phase 2: Gameplay Experience

重點是操作體驗與策略性。

- 下一個方塊預覽。
- Ghost Piece。
- 暫停功能。
- 等級與速度調整。
- Hold 方塊。
- Combo 分數。

### Phase 3: Complete Game Feel

重點是完整作品感與重玩動機。

- 主選單與模式選擇。
- 限時模式或挑戰模式。
- 最高分紀錄。
- 音效與背景音樂。
- 更完整的結束統計與 UI 美化。

## Constructive Review Examples

好的 review comment 會指出原因與替代方向：

```text
這裡把計分邏輯放在 GameState 很合理。不過第二階段若加入 combo 與 level，
可以考慮把 score calculation 抽成獨立方法或類別，讓規則更容易測試。
```

```text
這段繪圖邏輯目前可讀性還可以。若之後加入 next piece / hold / pause overlay，
建議先拆出 drawSidePanel 的小方法，避免 GamePanel 變太長。
```

較不理想的 review comment 只說結論：

```text
這樣不好。
```

```text
請重寫。
```

## Reviewer Checklist

- 這個 PR 的範圍是否符合目前階段？
- README 或文件是否足夠讓新同學跑起來？
- 類別責任是否清楚？
- 是否有明確驗證方式？
- 是否有把不做的事情留到下一階段，而不是混在同一個 PR？

## PR Language

建議 PR 使用中文撰寫，並保留必要的英文技術名稱：

- 標題用中文描述目的，例如 `新增第一階段架構文件與 PR 範本`。
- 摘要、背景、驗證方式與 review focus 用中文撰寫。
- Java class、method、檔名、branch name、指令維持原文，例如 `GameState`、`tick()`、`docs/architecture.md`、`javac -d out src/tetris/*.java`。
- 若 reviewer 提出英文 comment，可以用中文回覆並引用原本的 class 或方法名稱。
