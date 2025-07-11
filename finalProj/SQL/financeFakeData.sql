
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
('2025年3季', '25Q3', '2025-07-01', '2025-09-30', '2025-07-01', GETDATE(), GETDATE(), 1, 1, 1, 'System generated',  1),
('2025年8月', '25M8', '2025-08-01', '2025-08-31', '2025-08-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated',  1),
('2025年9月', '25M9', '2025-09-01', '2025-09-30', '2025-09-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1),
('2025年4季', '25Q4', '2025-10-01', '2025-12-31', '2025-10-01', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1),
('2025年10月', '25M10', '2025-10-01', '2025-10-31', '2025-10-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1),
('2025年11月', '25M11', '2025-11-01', '2025-11-30', '2025-11-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1),
('2025年12月', '25M12', '2025-12-01', '2025-12-31', '2025-12-10', GETDATE(), GETDATE(), 1, 1, 1, 'System generated', 1);

-- 3. 應收帳款（請款單）
INSERT INTO finance_Invoice (amount_due, period_name, deadline, payment_plan, unit_count, unit_price, total_amount, payment_Status, created_at, last_updated, created_by, updated_by, status, note, users_id, fee_type_id, billing_period_id, community_id)
VALUES
(36000, '2025年8月', '2025-08-10', 'monthly', 30, 1200, 36000, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, 'Sys-Gen 管理費', 1, 1, 1, 1),
(300, '2025年9月', '2025-09-10', 'monthly', 1, 300, 300, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '清潔費', 1, 2, 2, 1),
(500, '2025年12月', '2025-12-10', 'monthly', 1, 500, 500, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, 'Sys-Gen 車位費', 1, 5, 5, 1),
(75, '2025年10月', '2025-10-10', 'monthly', 3, 25, 75, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '', 1, 3, 3, 1),
(90, '2025年11月', '2025-11-10', 'monthly', 30, 3, 90, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '公攤電費',1, 4, 4, 1);
(36000, '2025年8月', '2025-08-10', 'monthly', 30, 1200, 36000, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, 'Sys-Gen 管理費', 2, 1, 1, 1),
(300, '2025年9月', '2025-09-10', 'monthly', 1, 300, 300, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '清潔費', 2, 2, 2, 1),
(500, '2025年12月', '2025-12-10', 'monthly', 1, 500, 500, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, 'Sys-Gen 車位費', 2, 5, 5, 1),
(75, '2025年10月', '2025-10-10', 'monthly', 3, 25, 75, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '', 2, 3, 3, 1),
(90, '2025年11月', '2025-11-10', 'monthly', 30, 3, 90, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, '公攤電費',2, 4, 4, 1);

-- 4. 財務支出
INSERT INTO finance_Expense (category, amount, paid_on, paid_by, receipt, vendor_id, created_at, last_updated, created_by, updated_by, status, note, community_id)
VALUES
('水塔清洗', 5000, '2025-08-15', '管理員', NULL, 1, GETDATE(), GETDATE(), 1, 1, 1, '年度水塔清洗', 1),
('電梯保養', 8000, '2025-09-10', '管理員', NULL, 2, GETDATE(), GETDATE(), 1, 1, 1, '季度保養', 1),
('花園維護', 2000, '2025-10-05', '管理員', NULL, 3, GETDATE(), GETDATE(), 1, 1, 1, '秋季修剪', 2),
('消防檢查', 3000, '2025-11-20', '管理員', NULL, 4, GETDATE(), GETDATE(), 1, 1, 1, '年度消防', 2),
('社區活動', 1500, '2025-12-25', '管理員', NULL, 5, GETDATE(), GETDATE(), 1, 1, 1, '聖誕活動', 1);

-- 5. 請款單回覆
INSERT INTO finance_Invoice_Response (last_response_time, account_code, last_response, verified, verified_time, verified_by, users_id, invoice_id)
VALUES
('2025-08-11', '12345', '已匯款,請查收', 1, '2025-08-12', 1, 1, 1),
('2025-12-11', '67890', '我在12/12匯款了', 1, '2025-12-12', 1, 1, 6),
('2025-08-11', '12345', '已匯款,請查收', 1, '2025-08-12', 1, 1, 1);


INSERT INTO finance_Invoice_Response (last_response_time, account_code, verified, verified_time, verified_by, users_id, invoice_id)
VALUES
('2025-09-11', '54321', 1, '2025-09-12', 1, 6, 4),
('2025-09-11', '95384', 1, '2025-09-12', 1, 1, 2),


INSERT INTO finance_Invoice_Response (last_response_time, last_response, verified, verified_time, verified_by, users_id, invoice_id)
VALUES
('2025-08-11', '8/10給王管理員了', 1, '2025-08-12', 1, 2, 3),
('2025-09-11', '現金', 1, '2025-09-12', 1, 1, 5),
('2025-12-11', '以現金繳納,請確認', 1, '2025-12-12', 1, 8, 5),



-- 6. 實收明細
INSERT INTO finance_Receipt (receipt_num, payment_method, paid_at, debit_at, amount_pay, installments, created_at, last_updated, created_by, updated_by, status, note, invoice_id, users_id, community_id)
VALUES
('R20250801', '轉帳', '2025-08-11', '2025-08-12', 36000, NULL, GETDATE(), GETDATE(), 1, 1, 1, '管理費收據', 1, 1, 1),
('R20250901', '現金', '2025-09-11', '2025-09-12', 300, NULL, GETDATE(), GETDATE(), 1, 1, 1, '清潔費收據', 2, 2, 1),
('R20251201', '轉帳', '2025-12-11', '2025-12-12', 500, NULL, GETDATE(), GETDATE(), 1, 1, 1, '車位費收據', 3, 1, 1),
('R20251001', '現金', '2025-10-11', '2025-10-12', 75, NULL, GETDATE(), GETDATE(), 1, 1, 1, '公攤水費收據', 4, 1, 2),
('R20251101', '轉帳', '2025-11-11', '2025-11-12', 90, NULL, GETDATE(), GETDATE(), 1, 1, 1, '公攤電費收據', 5, 1, 2),
('R20250801', '轉帳', '2025-08-11', '2025-08-12', 36000, NULL, GETDATE(), GETDATE(), 1, 1, 1, '管理費收據', 1, 2, 1),
('R20250901', '現金', '2025-09-11', '2025-09-12', 300, NULL, GETDATE(), GETDATE(), 1, 1, 1, '清潔費收據', 2, 2, 1),
('R20251201', '轉帳', '2025-12-11', '2025-12-12', 500, NULL, GETDATE(), GETDATE(), 1, 1, 1, '車位費收據', 3, 2, 1),
('R20251001', '現金', '2025-10-11', '2025-10-12', 75, NULL, GETDATE(), GETDATE(), 1, 1, 1, '公攤水費收據', 4, 2, 2),
('R20251101', '轉帳', '2025-11-11', '2025-11-12', 90, NULL, GETDATE(), GETDATE(), 1, 1, 1, '公攤電費收據', 5, 2, 2);
