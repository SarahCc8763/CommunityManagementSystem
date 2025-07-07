-- 0. 社區
INSERT INTO community ( name, address, create_time, [function]) VALUES
( '陽光花園', '台北市信義區松仁路100號', GETDATE(), 1),
( '星光大廈', '新北市板橋區文化路200號', GETDATE(), 2);

-- 0. 用戶
INSERT INTO users (name, gender, contact_info, emergency_contact_name, emergency_contact_phone, emergency_contact_relation, line_id, state, create_at, last_alter_at, photo, email, password, states, login_fail_times, last_failed_login, account_locked_until, community_id)
VALUES
('王小明', '男', '0912345678', '王媽媽', '0911222333', '母子', 'mingline', '正常', GETDATE(), GETDATE(), NULL, 'ming@example.com', 'pw1', 0, 0, NULL, NULL, 1),
( '李小華', '女', '0922333444', '李爸爸', '0922111222', '父女', 'hualine', '正常', GETDATE(), GETDATE(), NULL, 'hua@example.com', 'pw2', 0, 0, NULL, NULL, 1),
( '陳大同', '男', '0933444555', '陳媽媽', '0933111222', '母子', 'tongline', '正常', GETDATE(), GETDATE(), NULL, 'tong@example.com', 'pw3', 0, 0, NULL, NULL, 2),
( '林美麗', '女', '0944555666', '林爸爸', '0944111222', '父女', 'meiline', '正常', GETDATE(), GETDATE(), NULL, 'mei@example.com', 'pw4', 0, 0, NULL, NULL, 2),
( '張偉', '男', '0955666777', '張媽媽', '0955111222', '母子', 'weiline', '正常', GETDATE(), GETDATE(), NULL, 'wei@example.com', 'pw5', 0, 0, NULL, NULL, 1);

-- 1. 費用類別
INSERT INTO Fee_Type (description, fee_code, amount_Per_Unit, frequency, unit, created_at, last_updated, created_by, updated_by, status, note, community_id)
VALUES
('管理費', 'MGF001', 50.00, '1', '每坪', GETDATE(), GETDATE(), 1, 1, 1, '依坪數收費', 1),
('清潔費', 'CLF001', 300.00, '1', '每戶', GETDATE(), GETDATE(), 1, 1, 1, '每戶固定費用', 1),
('水費', 'WTF001', 25.00, '1', '每度', GETDATE(), GETDATE(), 1, 1, 1, '依用量收費', 2),
('電費', 'ELF001', 3.00, '1', '每度', GETDATE(), GETDATE(), 1, 1, 1, '公共用電', 2),
('車位管理費', 'CPF001', 500.00, '1', '車位', GETDATE(), GETDATE(), 1, 1, 1, '每車位月管理費', 1);

-- 2. 期別表
INSERT INTO Billing_Period (period_name, period_code, start_date, end_date, due_date, created_at, last_updated, created_by, updated_by, status, note, fee_type_id, community_id)
VALUES
('2025年3季', '25Q3', '2025-07-01', '2025-09-30', '2025-07-01', GETDATE(), GETDATE(), 1, 1, 1, '', 1, 1),
('2025年8月', '25M8', '2025-08-01', '2025-08-31', '2025-08-10', GETDATE(), GETDATE(), 1, 1, 1, '', 1, 1),
('2025年9月', '25M9', '2025-09-01', '2025-09-30', '2025-09-10', GETDATE(), GETDATE(), 1, 1, 1, '', 2, 1),
('2025年4季', '25Q4', '2025-10-01', '2025-12-31', '2025-10-01', GETDATE(), GETDATE(), 1, 1, 1, '', 1, 1),
('2025年10月', '25M10', '2025-10-01', '2025-10-31', '2025-10-10', GETDATE(), GETDATE(), 1, 1, 1, '', 3, 2),
('2025年11月', '25M11', '2025-11-01', '2025-11-30', '2025-11-10', GETDATE(), GETDATE(), 1, 1, 1, '', 4, 2),
('2025年12月', '25M12', '2025-12-01', '2025-12-31', '2025-12-10', GETDATE(), GETDATE(), 1, 1, 1, '', 5, 1);

-- 3. 應收帳款（請款單）
INSERT INTO Invoice (amount_due, period_name, deadline, payment_plan, unit_count, unit_price, total_amount, payment_Status, created_at, last_updated, created_by, updated_by, status, note, users_id, fee_type_id, billing_period_id, community_id)
VALUES
(36000, '2025年8月', '2025-08-10', 'monthly', 30, 1200, 36000, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, 'A戶管理費', 1, 1, 1, 1),
(300, '2025年9月', '2025-09-10', 'monthly', 1, 300, 300, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, 'A戶清潔費', 2, 2, 2, 1),
(500, '2025年12月', '2025-12-10', 'monthly', 1, 500, 500, 'paid', GETDATE(), GETDATE(), 1, 1, 1, 'A戶車位費', 3, 5, 5, 1),
(75, '2025年10月', '2025-10-10', 'monthly', 3, 25, 75, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, 'B戶水費', 4, 3, 3, 2),
(90, '2025年11月', '2025-11-10', 'monthly', 30, 3, 90, 'unpaid', GETDATE(), GETDATE(), 1, 1, 1, 'B戶電費', 5, 4, 4, 2);

-- 4. 財務支出
INSERT INTO Expense (category, amount, paid_on, paid_by, receipt, vendor_id, created_at, last_updated, created_by, updated_by, status, note, community_id)
VALUES
('水塔清洗', 5000, '2025-08-15', '管理員', NULL, 1, GETDATE(), GETDATE(), 1, 1, 1, '年度水塔清洗', 1),
('電梯保養', 8000, '2025-09-10', '管理員', NULL, 2, GETDATE(), GETDATE(), 1, 1, 1, '季度保養', 1),
('花園維護', 2000, '2025-10-05', '管理員', NULL, 3, GETDATE(), GETDATE(), 1, 1, 1, '秋季修剪', 2),
('消防檢查', 3000, '2025-11-20', '管理員', NULL, 4, GETDATE(), GETDATE(), 1, 1, 1, '年度消防', 2),
('社區活動', 1500, '2025-12-25', '管理員', NULL, 5, GETDATE(), GETDATE(), 1, 1, 1, '聖誕活動', 1);

-- 5. 請款單回覆
INSERT INTO Invoice_Response (last_response_time, account_code, last_response, verified, verified_time, verified_by, users_id, invoice_id)
VALUES
('2025-08-11', '12345', '已匯款', 1, '2025-08-12', 1, 1, 1),
('2025-09-11', '54321', '已匯款', 1, '2025-09-12', 1, 2, 2),
('2025-12-11', '67890', '已匯款', 1, '2025-12-12', 1, 3, 3),
('2025-10-11', '98765', '已匯款', 1, '2025-10-12', 1, 4, 4),
('2025-11-11', '11223', '已匯款', 1, '2025-11-12', 1, 5, 5);

-- 6. 實收明細
INSERT INTO Receipt (receipt_num, payment_method, paid_at, debit_at, amount_pay, installments, created_at, last_updated, created_by, updated_by, status, note, invoice_id, users_id, community_id)
VALUES
('R20250801', '轉帳', '2025-08-11', '2025-08-12', 36000, NULL, GETDATE(), GETDATE(), 1, 1, 1, 'A戶管理費收據', 1, 1, 1),
('R20250901', '現金', '2025-09-11', '2025-09-12', 300, NULL, GETDATE(), GETDATE(), 1, 1, 1, 'A戶清潔費收據', 2, 2, 1),
('R20251201', '轉帳', '2025-12-11', '2025-12-12', 500, NULL, GETDATE(), GETDATE(), 1, 1, 1, 'A戶車位費收據', 3, 3, 1),
('R20251001', '現金', '2025-10-11', '2025-10-12', 75, NULL, GETDATE(), GETDATE(), 1, 1, 1, 'B戶水費收據', 4, 4, 2),
('R20251101', '轉帳', '2025-11-11', '2025-11-12', 90, NULL, GETDATE(), GETDATE(), 1, 1, 1, 'B戶電費收據', 5, 5, 2);
