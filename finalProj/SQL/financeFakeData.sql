
--費用類別
INSERT INTO fee_type (
    description, fee_code, amount, frequency, unit,
    created_at, last_updated, created_by, updated_by,
    status, note, community_id
) VALUES 
('管理費', 'MGF001', 1200.00, '1', '坪', GETDATE(), GETDATE(), 1, 1, 1, '依坪數收費，每坪1200元/月', 1),
('清潔費', 'CLF001', 300.00, '1', '戶', GETDATE(), GETDATE(), 1, 1, 1, '每戶固定費用，每月300元', 1),
('水費', 'WTF001', 25.00, '1', '度', GETDATE(), GETDATE(), 1, 1, 1, '依用量收費，每度25元', 1),
('電費', 'ELF001', 3.00, '1', '度', GETDATE(), GETDATE(), 1, 1, 1, '社區公共用電，每度3元', 1),
('車位管理費', 'CPF001', 500.00, '1', '車位', GETDATE(), GETDATE(), 1, 1, 1, '每個車位月管理費500元', 1),
('冷氣保養費', 'ACM001', 200.00, '6', '戶', GETDATE(), GETDATE(), 1, 1, 1, '半年一次的冷氣設備保養', 1),
('垃圾處理費', 'GBF001', 150.00, '1', '戶', GETDATE(), GETDATE(), 1, 1, 1, '每月垃圾清運費150元', 1),
('保全費', 'SEC001', 800.00, '1', '戶', GETDATE(), GETDATE(), 1, 1, 1, '社區保全人員與巡邏支出', 1),
('公共設施維護費', 'PUB001', 300.00, '6', '戶', GETDATE(), GETDATE(), 1, 1, 1, '公共設施如電梯、健身房保養', 1),
('花園維護費', 'GRD001', 200.00, '12', '戶', GETDATE(), GETDATE(), 1, 1, 1, '年度花園與綠化維護費用', 1);


--期別表
INSERT INTO dbo.billing_period (
    community_id,
    created_at,
    created_by,
    last_updated,
    note,
    status,
    updated_by,
    due_date,
    end_date,
    period_code,
    period_name,
    start_date,
    fee_type_id
)
VALUES
-- 2025 年第 8 月期 - 管理費
(1, GETDATE(), '1', GETDATE(), '本期管理費依坪數計費', 'true', '1', '2025-08-10T23:59:59', '2025-08-31', '25M8', '2025年8月管理費', '2025-08-01', 3),

-- 2025 年第 9 月期 - 清潔費
(1, GETDATE(), '1', GETDATE(), '本期清潔費為固定金額', 'true', '1', '2025-09-10T23:59:59', '2025-09-30', '25M9', '2025年9月清潔費', '2025-09-01', 4),

-- 2025 年第 10 月期 - 車位費
(1, GETDATE(), '1', GETDATE(), '每車位單位收費', 'true', '1', '2025-10-10T23:59:59', '2025-10-31', '25M10', '2025年10月車位費', '2025-10-01', 3),

-- 2025 第三季活動費（假設季繳）
(1, GETDATE(), '1', GETDATE(), '季度社區活動收費', 'true', '1', '2025-09-15T23:59:59', '2025-09-30', '25Q3', '2025年第3季活動費', '2025-07-01', 4),

-- 2025 下半年水塔費（假設半年繳）
(1, GETDATE(), '1', GETDATE(), '半年保養費用', 'true', '1', '2025-08-31T23:59:59', '2025-12-31', '25H2', '2025年下半年水塔費', '2025-07-01', 5);
