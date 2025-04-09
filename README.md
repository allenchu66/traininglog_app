# 💪 重訓紀錄 App

一款專為重訓愛好者打造的 Android 記錄工具，讓你方便追蹤每日訓練項目、重量與組數，並支援資料備份與還原。

---

## 📱 功能特色

- [x] 重訓紀錄（分類：部位 / 動作 / 組數 / 重量）
- [x] 使用 Sectioned RecyclerView 呈現清晰的訓練清單
- [x] 日曆標記功能，可查看哪幾天有訓練
- [x] 資料匯出為 JSON，可儲存至下載資料夾
- [x] 資料匯入功能，支援備份還原
- [x] 匯出後可透過 LINE / Gmail 等方式分享
- [x] 可在 App 中自訂與管理部位與動作項目
- [x] 體驗優化：重量支援小數點選擇（例如 7.5 kg）

---

## 📦 技術架構

- Room Database（資料儲存）
- ViewModel + LiveData（狀態管理）
- Gson（JSON 轉換）
- MaterialCalendarView（日曆打點）
- FileProvider + SAF（檔案讀寫 + 分享）
- SectionedRecyclerViewAdapter（資料分群呈現）

---

## 📂 備份與還原

- 點選側邊選單中「匯出資料」可備份訓練紀錄為 JSON
- 匯出檔案儲存於 `下載資料夾`，可分享至LINE或是Google雲端
- 點選「匯入資料」可選擇 JSON 檔還原訓練紀錄
- 匯入會清空現有資料並完全還原備份

---

## 🧠 延伸規劃（未來）

- [ ] 支援雲端備份（Google Drive / Firebase）
- [ ] 自動化訓練週期安排
- [ ] 圖表化重量與進度追蹤

---

## 👨‍💻 作者

Allen｜Tainan, Taiwan  
熱愛寫程式，持續進化中 🏋️‍♂️

