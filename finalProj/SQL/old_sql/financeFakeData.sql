
-- 1. 費用類別
INSERT INTO finance_Fee_Type (description, fee_code, amount_Per_Unit, frequency, unit, created_at, last_updated, created_by, updated_by, status, note, community_id)
VALUES
('管理費', 'MGF001', 50.00, '1', '每坪', GETDATE(), GETDATE(), 1, 1, 1, '依坪數收費', 1),
('清潔費', 'CLF001', 300.00, '1', '每戶', GETDATE(), GETDATE(), 1, 1, 1, '每戶固定費用', 1),
('公攤水費', 'WTF001', 25.00, '1', '每住戶', GETDATE(), GETDATE(), 1, 1, 1, '依用量收費', 1),
('公攤電費', 'ELF001', 3.00, '1', '每住戶', GETDATE(), GETDATE(), 1, 1, 1, '公共用電', 1),
('車位管理費', 'CPF001', 500.00, '1', '車位', GETDATE(), GETDATE(), 1, 1, 1, '每車位月管理費', 1);

-- 2. 期別表
INSERT INTO finance_Billing_Period (period_name, period_code, start_date, end_date, due_date, created_at, last_updated, created_by, updated_by, status, note, community_id)
VALUES
('2025年1月', '25M1', '2025-01-01', '2025-01-31', '2025-01-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1),
('2025年2月', '25M2', '2025-02-01', '2025-02-28', '2025-02-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1),
('2025年3月', '25M3', '2025-03-01', '2025-03-31', '2025-03-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1),
('2025年4月', '25M4', '2025-04-01', '2025-04-30', '2025-04-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1),
('2025年5月', '25M5', '2025-05-01', '2025-05-31', '2025-05-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1),
('2025年6月', '25M6', '2025-06-01', '2025-06-30', '2025-06-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1),
('2025年1季', '25Q1', '2025-01-01', '2025-03-31', '2025-01-01', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1),
('2025年2季', '25Q2', '2025-04-01', '2025-06-30', '2025-04-01', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1),
('2025年7月', '25M7', '2025-07-01', '2025-07-31', '2025-07-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated',  1),
('2025年3季', '25Q3', '2025-07-01', '2025-09-30', '2025-07-01', GETDATE(), GETDATE(), 1, 1, 1, 'System generated',  1),
('2025年8月', '25M8', '2025-08-01', '2025-08-31', '2025-08-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated',  1),
('2025年9月', '25M9', '2025-09-01', '2025-09-30', '2025-09-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1),
('2025年4季', '25Q4', '2025-10-01', '2025-12-31', '2025-10-01', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1),
('2025年10月', '25M10', '2025-10-01', '2025-10-31', '2025-10-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1),
('2025年11月', '25M11', '2025-11-01', '2025-11-30', '2025-11-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1),
('2025年12月', '25M12', '2025-12-01', '2025-12-31', '2025-12-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1);

-- 3. 應收帳款（請款單）
INSERT INTO finance_Invoice (
    amount_due, period_name, deadline, payment_plan,
    unit_count, unit_price, total_amount, payment_Status,
    created_at, last_updated, created_by, updated_by,
    status, note, users_id, fee_type_id, billing_period_id, community_id
) VALUES
(100, '2025年8月', '2025-08-05', 'monthly', 4, 25, 100, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '清潔費', 1, 2, 11, 1),
(300, '2025年8月', '2025-08-10', 'monthly', 1, 300, 300, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '車位費', 1, 5, 11, 1),
(36000, '2025年7月', '2025-07-10', 'monthly', 30, 1200, 36000, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, 'Sys-Gen 管理費', 1, 1, 9, 1),
(180, '2025年9月', '2025-09-10', 'monthly', 6, 30, 180, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '電費', 1, 4, 12, 1),
(240, '2025年9月', '2025-09-25', 'monthly', 8, 30, 240, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '公設費', 1, 3, 12, 1),
(36000, '2025年7月', '2025-07-12', 'monthly', 30, 1200, 36000, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, 'Sys-Gen 管理費', 2, 1, 9, 1),
(75, '2025年8月', '2025-08-08', 'monthly', 3, 25, 75, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '', 2, 2, 11, 1),
(500, '2025年8月', '2025-08-18', 'monthly', 1, 500, 500, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '', 2, 5, 11, 1),
(90, '2025年9月', '2025-09-10', 'monthly', 30, 3, 90, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '電費', 2, 4, 12, 1),
(200, '2025年9月', '2025-09-20', 'monthly', 2, 100, 200, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '', 2, 3, 12, 1),
(36000, '2025年8月', '2025-08-10', 'monthly', 30, 1200, 36000, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '管理費', 5, 1, 11, 1),
(36000, '2025年8月', '2025-08-11', 'monthly', 30, 1200, 36000, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '管理費', 9, 1, 11, 1),
(36000, '2025年8月', '2025-08-12', 'monthly', 30, 1200, 36000, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '管理費', 10, 1, 11, 1),

-- 上半年已繳資料
(1200, '2025年1月', '2025-01-10', 'monthly', 24, 50, 1200, 'paid', GETDATE(), GETDATE(), 1, 1, 1, '管理費', 1, 1, 1, 1),
(300,  '2025年2月', '2025-02-10', 'monthly', 1, 300, 300, 'paid', GETDATE(), GETDATE(), 1, 1, 1, '清潔費', 1, 2, 2, 1),
(500,  '2025年3月', '2025-03-10', 'monthly', 1, 500, 500, 'paid', GETDATE(), GETDATE(), 1, 1, 1, '車位費', 1, 5, 3, 1),
(75,   '2025年4月', '2025-04-10', 'monthly', 3, 25, 75, 'paid', GETDATE(), GETDATE(), 1, 1, 1, '水費', 1, 3, 4, 1),
(90,   '2025年5月', '2025-05-10', 'monthly', 30, 3, 90, 'paid', GETDATE(), GETDATE(), 1, 1, 1, '電費', 1, 4, 5, 1),
(1200, '2025年1月', '2025-01-11', 'monthly', 24, 50, 1200, 'paid', GETDATE(), GETDATE(), 1, 1, 1, '管理費', 2, 1, 1, 1),
(300,  '2025年2月', '2025-02-11', 'monthly', 1, 300, 300, 'paid', GETDATE(), GETDATE(), 1, 1, 1, '清潔費', 2, 2, 2, 1),
(500,  '2025年3月', '2025-03-11', 'monthly', 1, 500, 500, 'paid', GETDATE(), GETDATE(), 1, 1, 1, '車位費', 2, 5, 3, 1),
(75,   '2025年4月', '2025-04-11', 'monthly', 3, 25, 75, 'paid', GETDATE(), GETDATE(), 1, 1, 1, '水費', 2, 3, 4, 1),
(90,   '2025年6月', '2025-06-10', 'monthly', 30, 3, 90, 'paid', GETDATE(), GETDATE(), 1, 1, 1, '電費', 2, 4, 6, 1);


-- 4. 財務支出
INSERT INTO finance_Expense (category, amount, paid_on, paid_by, receipt, vendor_id, created_at, last_updated, created_by, updated_by, status, note, community_id)
VALUES
('水塔清洗', 5000, '2025-08-15', '管理員', NULL, 1, GETDATE(), GETDATE(), 1, 1, 1, '年度水塔清洗', 1),
('電梯保養', 8000, '2025-09-10', '管理員', NULL, 2, GETDATE(), GETDATE(), 1, 1, 1, '季度保養', 1),
('花園維護', 2000, '2025-10-05', '管理員', NULL, 3, GETDATE(), GETDATE(), 1, 1, 1, '秋季修剪', 2),
('消防檢查', 3000, '2025-11-20', '管理員', NULL, 4, GETDATE(), GETDATE(), 1, 1, 1, '年度消防', 2),
('社區活動', 1500, '2025-12-25', '管理員', NULL, 5, GETDATE(), GETDATE(), 1, 1, 1, '聖誕活動', 1);

-- 更新後的請款單回覆（時間調整為 7/25 前一個月內）
INSERT INTO finance_Invoice_Response (last_response_time, account_code, last_response, verified, verified_time, verified_by, users_id, invoice_id)
VALUES 
('2025-06-26', '12345', '已匯款,請查收', 1, '2025-06-27', 1, 1, 1),
('2025-06-28', null, '我已經匯款等很久了，請快點審核', 1, '2025-06-29', 1, 2, 1),
('2025-07-01', '54321', '已繳交', 1, '2025-07-02', 1, 2, 2),
('2025-07-14', null, '現金繳交，已交給管理員', 1, '2025-07-14', 1, 1, 3),
('2025-07-15', null, '麻煩快點更新狀態，謝謝', 1, '2025-07-15', 1, 2, 3),
('2025-07-07', '11223', '我已匯款', 1, '2025-07-08', 1, 1, 4),
('2025-07-09', null, '10月已繳費，請確認', 1, '2025-07-10', 1, 2, 5),
('2025-07-10', null, '匯款後沒更新狀態', 1, '2025-07-11', 1, 1, 5),
('2025-07-12', '88991', '轉帳完成', 1, '2025-07-13', 1, 2, 6),
('2025-07-13', null, '我用現金給櫃檯了', 1, '2025-07-14', 1, 1, 7),
('2025-07-14', '00112', '請查收匯款', 1, '2025-07-15', 1, 1, 8),
('2025-07-16', null, '已繳交費用', 1, '2025-07-17', 1, 2, 8),
('2025-07-17', null, '現金繳交完成', 1, '2025-07-18', 1, 1, 9),
('2025-07-19', '44556', '轉帳成功', 1, '2025-07-20', 1, 2, 10),
('2025-07-20', null, '已過三天尚未審核，請處理', 1, '2025-07-21', 1, 1, 10),
('2025-07-21', null, '付款完成', 1, '2025-07-22', 1, 1, 11),
('2025-07-22', '77889', '已繳交費用', 1, '2025-07-23', 1, 2, 12),
('2025-07-23', null, '管理室已收款', 1, '2025-07-24', 1, 9, 12),
('2025-07-24', null, '匯款後沒更新狀態', 1, '2025-07-24', 1, 10, 13),
('2025-07-24', null, '我轉帳給會計了，請查收', 1, '2025-07-24', 1, 10, 13);



-- 6. 實收明細
INSERT INTO finance_Receipt (receipt_num, payment_method, paid_at, debit_at, amount_pay, installments, created_at, last_updated, created_by, updated_by, status, note, invoice_id, users_id, community_id)
VALUES
('R20250101', '轉帳', '2025-01-10', '2025-01-11', 1200, NULL, GETDATE(), GETDATE(), 1, 1, 1, '管理費收據', 14, 1, 1),
('R20250201', '現金', '2025-02-15', '2025-02-16', 300, NULL, GETDATE(), GETDATE(), 1, 1, 1, '清潔費收據', 15, 1, 1),
('R20250301', '轉帳', '2025-03-12', '2025-03-13', 500, NULL, GETDATE(), GETDATE(), 1, 1, 1, '車位費收據', 16, 1, 1),
('R20250401', '現金', '2025-04-18', '2025-04-19', 75, NULL, GETDATE(), GETDATE(), 1, 1, 1, '水費收據', 17, 1, 1),
('R20250501', '轉帳', '2025-05-22', '2025-05-23', 90, NULL, GETDATE(), GETDATE(), 1, 1, 1, '電費收據', 18, 1, 1),

('R20250102', '轉帳', '2025-01-11', '2025-01-12', 1200, NULL, GETDATE(), GETDATE(), 1, 1, 1, '管理費收據', 19, 2, 1),
('R20250202', '現金', '2025-02-14', '2025-02-15', 300, NULL, GETDATE(), GETDATE(), 1, 1, 1, '清潔費收據', 20, 2, 1),
('R20250302', '轉帳', '2025-03-18', '2025-03-19', 500, NULL, GETDATE(), GETDATE(), 1, 1, 1, '車位費收據', 21, 2, 1),
('R20250402', '現金', '2025-04-20', '2025-04-21', 75, NULL, GETDATE(), GETDATE(), 1, 1, 1, '水費收據', 22, 2, 1),
('R20250602', '轉帳', '2025-06-10', '2025-06-11', 90, NULL, GETDATE(), GETDATE(), 1, 1, 1, '電費收據', 23, 2, 1);
