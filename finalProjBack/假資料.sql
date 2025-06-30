-- create database finalProj
use finalProj

INSERT INTO community (name, address, [function])
VALUES 
(N'宏益花園社區', N'台北市中正區和平西路一段100號', 1),
(N'長青國宅', N'新北市板橋區文化路二段88號', 2);
 
INSERT INTO roles (description, note, community_id)
VALUES 
(N'一般住戶', N'社區內一般成員，擁有基本權限', 1),
(N'管理員', N'可管理車位與用戶資料，具備中等權限', 1),
(N'最高權限', N'系統管理者，擁有所有功能與操作權限', 1),
(N'管委會', N'社區決策單位，負責公告與審核等功能', 1);

INSERT INTO users (
    name, gender, contact_info, emergency_contact_name, emergency_contact_phone, emergency_contact_relation,
    line_id, state, create_at, last_alter_at, photo, email, [password],
    states, login_fail_times, last_failed_login, account_locked_until, community_id
) VALUES
('sa', null, null, null, null, null, null, null, GETDATE(), GETDATE(), NULL, null, 'pass123', 0, 0, NULL, NULL, 1),
('張大明', '男', '0955123456', '張媽媽', '0911000999', '母親', 'da_ming88', '正常', GETDATE(), GETDATE(), NULL, 'ming@example.com', 'pass123', 0, 0, NULL, NULL, 1),
('黃美麗', '女', '0966888777', '林爸爸', '0933666777', '父親', 'meili_huang', '正常', GETDATE(), GETDATE(), NULL, 'mei@example.com', 'pass123', 0, 0, NULL, NULL, 1),
('陳建志', '男', '0922111222', '陳媽媽', '0955888999', '母親', 'kenchen1990', '正常', GETDATE(), GETDATE(), NULL, 'ken@example.com', 'pass123', 0, 0, NULL, NULL, 1),
('吳小芳', '女', '0977666555', '吳先生', '0922333444', '父親', 'wufang2024', '正常', GETDATE(), GETDATE(), NULL, 'fang@example.com', 'pass123', 0, 0, NULL, NULL, 1),
('李政道', '男', '0988777666', '李媽媽', '0966333222', '母親', 'leephysics', '正常', GETDATE(), GETDATE(), NULL, 'lee@example.com', 'pass123', 0, 0, NULL, NULL, 1),
('何雅慧', '女', '0900123456', '何爸爸', '0933555666', '父親', 'yahui_2023', '正常', GETDATE(), GETDATE(), NULL, 'ya@example.com', 'pass123', 0, 0, NULL, NULL, 1),
('林榮昌', '男', '0911222333', '林媽媽', '0977999888', '母親', 'rongchang123', '正常', GETDATE(), GETDATE(), NULL, 'rong@example.com', 'pass123', 0, 0, NULL, NULL, 1),
('鄭雅婷', '女', '0933444555', '鄭先生', '0911888777', '父親', 'tingtingting', '正常', GETDATE(), GETDATE(), NULL, 'ting@example.com', 'pass123', 0, 0, NULL, NULL, 1),
('王大偉', '男', '0922000111', '王阿姨', '0966888999', '姑媽', 'davidwang', '正常', GETDATE(), GETDATE(), NULL, 'david@example.com', 'pass123', 0, 0, NULL, NULL, 2);

-- 每位 user_id 都綁定 role_id 1、2、4
INSERT INTO roles_users (community_id, user_id, role_id) VALUES 
(1, 1, 3), (1, 2, 1), (1, 2, 2), (1, 2, 4),
(1, 3, 1), (1, 3, 2), (1, 3, 4),
(1, 4, 1), (1, 4, 2), (1, 4, 4),
(1, 5, 1), (1, 5, 2), (1, 5, 4),
(1, 6, 1), (1, 6, 2), (1, 6, 4),
(1, 7, 1), (1, 7, 2), (1, 7, 4),
(1, 8, 1), (1, 8, 2), (1, 8, 4),
(1, 9, 1), (1, 9, 2), (1, 9, 4),
(1, 10, 1), (1, 10, 2), (1, 10, 4);

INSERT INTO Units (unit, floor, building, ping, point, community_id) VALUES
('A1', '1F', 'A棟', 25.50, 10, 1),
('A2', '1F', 'A棟', 30.25, 5, 1),
('A3', '1F', 'A棟', 28.75, 0, 1),
('B1', '2F', 'B棟', 27.00, 3, 1),
('B2', '2F', 'B棟', 26.50, 6, 1),
('B3', '2F', 'B棟', 29.00, 0, 1),
('C1', '3F', 'C棟', 24.00, 2, 1),
('C2', '3F', 'C棟', 23.50, 1, 1),
('C3', '3F', 'C棟', 22.75, 4, 1),
('D1', '4F', 'D棟', 26.00, 0, 1),
('D2', '4F', 'D棟', 27.75, 7, 1),
('D3', '4F', 'D棟', 28.50, 0, 1),
('E1', '5F', 'E棟', 30.00, 2, 1),
('E2', '5F', 'E棟', 31.25, 3, 1),
('E3', '5F', 'E棟', 29.75, 0, 1),
('F1', '6F', 'F棟', 25.00, 1, 1),
('F2', '6F', 'F棟', 26.50, 0, 1),
('F3', '6F', 'F棟', 24.75, 0, 1),
('G1', '7F', 'G棟', 32.00, 0, 1),
('G2', '7F', 'G棟', 33.50, 5, 1);

INSERT INTO units_users (user_id, unit_id, community_id) VALUES
(2, 1, 1),
(2, 2, 1),
(3, 3, 1),
(3, 4, 1),
(4, 5, 1),
(4, 6, 1),
(5, 7, 1),
(5, 8, 1),
(6, 9, 1),
(6, 10, 1),
(7, 11, 1),
(7, 12, 1),
(8, 13, 1),
(8, 14, 1),
(9, 15, 1),
(9, 16, 1),
(10, 17, 1),
(10, 18, 1);

-- parking_type 車位種類
INSERT INTO parking_type ([community_id],[type]) VALUES (1, N'汽車'), (1, N'機車'), (1, N'電動車');

-- parking_slot 車位資料
INSERT INTO [dbo].[parking_slot] (community_id, slot_number, location, parking_type_id, users_id, license_plate, is_rentable)
VALUES 
(1, 'B1-001', 'B1 A區', 1, 1, NULL, 1),
(1, 'B1-002', 'B1 A區', 1, 1, NULL, 1),
(1, 'B1-003', 'B1 A區', 2, 1, NULL, 1),
(1, 'B1-004', 'B1 B區', 2, 1, NULL, 1),
(1, 'B1-005', 'B1 B區', 3, 1, NULL, 0),
(1, 'B1-006', 'B1 B區', 3, 1, 'ABC-1234', 0),
(1, 'B2-001', 'B2 A區', 1, 1, NULL, 1),
(1, 'B2-002', 'B2 A區', 2, 1, NULL, 1),
(1, 'B2-003', 'B2 A區', 2, 1, NULL, 0),
(1, 'B2-004', 'B2 B區', 1, 1, 'XYZ-8888', 0),
(1, 'B2-005', 'B2 B區', 1, 1, NULL, 1),
(2, 'B3-001', 'B3 A區', 1, 1, NULL, 1),
(2, 'B3-002', 'B3 A區', 2, 1, NULL, 1),
(2, 'B3-003', 'B3 B區', 3, 1, NULL, 0),
(2, 'B3-004', 'B3 B區', 3, 1, NULL, 1),
(2, 'B4-001', 'B4 A區', 2, 1, NULL, 1),
(2, 'B4-002', 'B4 A區', 1, 1, NULL, 0),
(2, 'B4-003', 'B4 B區', 2, 1, NULL, 1),
(2, 'B4-004', 'B4 B區', 2, 1, NULL, 1),
(2, 'B4-005', 'B4 B區', 1, 1, NULL, 1),
(1, 'B2-M01', 'B2層機車區', 2, 1, NULL, 1),
(1, 'B2-01', 'B2層A排', 1, 2, 'KFC-43', 0);

-- parking_rentals 承租紀錄
INSERT INTO parking_rentals (
    community_id, users_id, parking_slot_id, rent_buy_start, rent_end,
    license_plate, status, approved, approved_id, updated_at, created_at
) VALUES
(1, 1, 1, '2025-07-01 08:00:00', '2025-08-01 08:00:00', 'ABC-1234', 0, 0, NULL, GETDATE(), GETDATE()),
(1, 2, 2, '2025-07-02 09:00:00', '2025-08-02 09:00:00', 'DEF-5678', 1, 0, NULL, GETDATE(), GETDATE()),
(2, 3, 3, '2025-07-03 10:00:00', '2025-08-03 10:00:00', 'GHI-9012', 1, 0, NULL, GETDATE(), GETDATE()),
(2, 4, 4, '2025-07-04 11:00:00', '2025-08-04 11:00:00', 'JKL-3456', 0, 0, NULL, GETDATE(), GETDATE()),
(1, 5, 5, '2025-07-05 12:00:00', '2025-08-05 12:00:00', 'MNO-7890', 1, 0, NULL, GETDATE(), GETDATE()),
(1, 6, 6, '2025-06-01 08:00:00', '2025-07-01 08:00:00', 'PQR-1111', 1, 1, 1, GETDATE(), GETDATE()),
(2, 7, 7, '2025-06-02 09:00:00', '2025-07-02 09:00:00', 'STU-2222', 1, 1, 2, GETDATE(), GETDATE()),
(2, 8, 8, '2025-06-03 10:00:00', '2025-07-03 10:00:00', 'VWX-3333', 0, 1, 2, GETDATE(), GETDATE()),
(2, 9, 9, '2025-06-04 11:00:00', '2025-07-04 11:00:00', 'YZA-4444', 1, 1, 3, GETDATE(), GETDATE()),
(1, 10, 10, '2025-06-05 12:00:00', '2025-07-05 12:00:00', 'BCD-5555', 0, 1, 1, GETDATE(), GETDATE());


