-- 建立假資料
/*
TRUNCATE TABLE community;
*/

Use finalProj;

INSERT INTO community (name, address,[function])
VALUES 
(N'RiverBank', N'台北市大同區環河北路一段113號',15),
(N'高雄社區二號', N'高雄市三民區建國一路200號',15),
(N'新北社區三號', N'新北市板橋區文化路300號',15),
(N'台南社區四號', N'台南市東區林森路400號',15),
(N'桃園社區五號', N'桃園市中壢區中山東路500號',15);
------------------------------------------------------------------------------------tim開始

INSERT INTO roles (description, note, community_id)
VALUES
    (N'住戶', N'一般住戶', 1),    -- 住戶為1
    (N'保全', N'巡邏及收件', 1),    -- 保全為2
    (N'訪客', N'臨時進入者', 1);

	-- users
INSERT INTO users
    (name, gender, contact_info, emergency_contact_name, emergency_contact_phone, emergency_contact_relation, line_id, state, last_alter_at, photo, email, community_id)
VALUES
    (N'陳志明', N'男', N'0912345001', N'陳媽媽', N'0987654001', N'母親', N'chihmingline', N'啟用', GETDATE(), NULL, N'chihming@example.com', 1),
    (N'林慧如', N'女', N'0912345002', N'林爸爸', N'0987654002', N'父親', N'huiline', N'啟用', GETDATE(), NULL, N'huilu@example.com', 1),
    (N'張嘉豪', N'男', N'0912345003', N'張姐姐', N'0987654003', N'姊姊', N'jiahaoline', N'啟用', GETDATE(), NULL, N'jiahao@example.com', 1),
    (N'黃怡君', N'女', N'0912345004', N'黃妹妹', N'0987654004', N'妹妹', N'yijunline', N'啟用', GETDATE(), NULL, N'yijun@example.com', 1),
    (N'吳建宏', N'男', N'0912345005', N'吳弟弟', N'0987654005', N'弟弟', N'jianhongline', N'啟用', GETDATE(), NULL, N'jianhong@example.com', 1),
    (N'李靜芳', N'女', N'0912345006', N'李姊姊', N'0987654006', N'姊姊', N'jingfangline', N'啟用', GETDATE(), NULL, N'jingfang@example.com', 1),
    (N'許文龍', N'男', N'0912345007', N'許媽媽', N'0987654007', N'母親', N'wenlongline', N'啟用', GETDATE(), NULL, N'wenlong@example.com', 1),
    (N'鄭雅文', N'女', N'0912345008', N'鄭哥哥', N'0987654008', N'哥哥', N'yawenline', N'啟用', GETDATE(), NULL, N'yawen@example.com', 1),
    (N'徐俊傑', N'男', N'0912345009', N'徐媽媽', N'0987654009', N'母親', N'junjieline', N'啟用', GETDATE(), NULL, N'junjie@example.com', 1),
    (N'曾詩涵', N'女', N'0912345010', N'曾爸爸', N'0987654010', N'父親', N'shihanline', N'啟用', GETDATE(), NULL, N'shihan@example.com', 1),
    (N'賴怡婷', N'女', N'0912345011', N'賴妹妹', N'0987654011', N'妹妹', N'yitingline', N'啟用', GETDATE(), NULL, N'yiting@example.com', 1),
    (N'葉冠宇', N'男', N'0912345012', N'葉爸爸', N'0987654012', N'父親', N'guanyuline', N'啟用', GETDATE(), NULL, N'guanyu@example.com', 1),
    (N'蔡子晴', N'女', N'0912345013', N'蔡媽媽', N'0987654013', N'母親', N'ziqingline', N'啟用', GETDATE(), NULL, N'ziqing@example.com', 1),
    (N'簡承翰', N'男', N'0912345014', N'簡哥哥', N'0987654014', N'哥哥', N'chenghanline', N'啟用', GETDATE(), NULL, N'chenghan@example.com', 1),
    (N'馮偉翔', N'男', N'0912345015', N'馮姐姐', N'0987654015', N'姊姊', N'weixiangline', N'啟用', GETDATE(), NULL, N'weixiang@example.com', 1),
    (N'鄒佩君', N'女', N'0912345016', N'鄒媽媽', N'0987654016', N'母親', N'peijunline', N'啟用', GETDATE(), NULL, N'peijun@example.com', 1),
    (N'溫柏翰', N'男', N'0912345017', N'溫爸爸', N'0987654017', N'父親', N'bohanline', N'啟用', GETDATE(), NULL, N'bohan@example.com', 1),
    (N'洪雅婷', N'女', N'0912345018', N'洪姐姐', N'0987654018', N'姊姊', N'yatingline', N'啟用', GETDATE(), NULL, N'yating@example.com', 1),
    (N'郭家豪', N'男', N'0912345019', N'郭媽媽', N'0987654019', N'母親', N'jiahaoline2', N'啟用', GETDATE(), NULL, N'jiahao2@example.com', 1),
    (N'戴子涵', N'女', N'0912345020', N'戴哥哥', N'0987654020', N'哥哥', N'zihanline', N'啟用', GETDATE(), NULL, N'zihan@example.com', 1),
    (N'張明軒', N'男', N'0912345021', N'張弟弟', N'0987654021', N'弟弟', N'mingxianline', N'啟用', GETDATE(), NULL, N'mingxian@example.com', 1),
    (N'李冠霖', N'男', N'0912345022', N'李爸爸', N'0987654022', N'父親', N'guanlinline', N'啟用', GETDATE(), NULL, N'guanlin@example.com', 1),
    (N'黃子涵', N'女', N'0912345023', N'黃媽媽', N'0987654023', N'母親', N'zihanline2', N'啟用', GETDATE(), NULL, N'zihan2@example.com', 1),
    (N'陳柏睿', N'男', N'0912345024', N'陳姐姐', N'0987654024', N'姊姊', N'boruiline', N'啟用', GETDATE(), NULL, N'borui@example.com', 1),
    (N'林承恩', N'男', N'0912345025', N'林爸爸', N'0987654025', N'父親', N'chengenline', N'啟用', GETDATE(), NULL, N'chengen@example.com', 1),
    (N'吳子瑜', N'女', N'0912345026', N'吳媽媽', N'0987654026', N'母親', N'ziyuline', N'啟用', GETDATE(), NULL, N'ziyu@example.com', 1),
    (N'徐偉哲', N'男', N'0912345027', N'徐哥哥', N'0987654027', N'哥哥', N'weizheline', N'啟用', GETDATE(), NULL, N'weizhe@example.com', 1),
    (N'曾佳欣', N'女', N'0912345028', N'曾弟弟', N'0987654028', N'弟弟', N'jiaxinline', N'啟用', GETDATE(), NULL, N'jiaxin@example.com', 1),
    (N'鄭柏宇', N'男', N'0912345029', N'鄭媽媽', N'0987654029', N'母親', N'boyuline', N'啟用', GETDATE(), NULL, N'boyu@example.com', 1),
    (N'許冠宇', N'男', N'0912345030', N'許爸爸', N'0987654030', N'父親', N'guanyuline2', N'啟用', GETDATE(), NULL, N'guanyu2@example.com', 1),       
    (N'王保全', N'男', N'02-87654321', N'保全公司', N'02-12345678', N'保全公司', NULL, N'啟用', GETDATE(), N'https://example.com/photos/u4.jpg', N'safe@example.com', 1);-- 保全

-- units 
INSERT INTO units
    (unit, floor, building, ping, point, community_id)
VALUES
    (N'10', N'2F', N'A棟', 35, 50, 1),
    (N'10-1', N'2F', N'A棟', 35, 50, 1),
    (N'12', N'2F', N'A棟', 35, 50, 1),
    (N'12-1', N'2F', N'A棟', 35, 50, 1),
    (N'10', N'3F', N'A棟', 35, 50, 1),
    (N'10-1', N'3F', N'A棟', 35, 50, 1),
    (N'12', N'3F', N'A棟', 35, 50, 1),
    (N'12-1', N'3F', N'A棟', 35, 50, 1),
    (N'10', N'4F', N'A棟', 35, 50, 1),
    (N'10-1', N'4F', N'A棟', 35, 50, 1),
    (N'12', N'4F', N'A棟', 35, 50, 1),
    (N'12-1', N'4F', N'A棟', 35, 50, 1),
    (N'10', N'5F', N'A棟', 35, 50, 1),
    (N'10-1', N'5F', N'A棟', 35, 50, 1),
    (N'12', N'5F', N'A棟', 35, 50, 1),
    (N'12-1', N'5F', N'A棟', 35, 50, 1),
    (N'10', N'6F', N'A棟', 35, 50, 1),
    (N'10-1', N'6F', N'A棟', 35, 50, 1),
    (N'12', N'6F', N'A棟', 35, 50, 1),
    (N'12-1', N'6F', N'A棟', 35, 50, 1),
    (N'10', N'7F', N'A棟', 35, 50, 1),
    (N'10-1', N'7F', N'A棟', 35, 50, 1),
    (N'12', N'7F', N'A棟', 35, 50, 1),
    (N'12-1', N'7F', N'A棟', 35, 50, 1),
    (N'10', N'8F', N'A棟', 35, 50, 1),
    (N'10-1', N'8F', N'A棟', 35, 50, 1),
    (N'12', N'8F', N'A棟', 35, 50, 1),
    (N'12-1', N'8F', N'A棟', 35, 50, 1),
    (N'10', N'9F', N'A棟', 35, 50, 1),
    (N'10-1', N'9F', N'A棟', 35, 50, 1),    
	(N'99', N'99F', N'E棟', 99, 99999, 1);

-- units_users
INSERT INTO units_users
    (user_id, unit_id, community_id)
VALUES
    (1, 1, 1),
    (2, 2, 1),
    (3, 3, 1),
    (4, 4, 1),
    (5, 5, 1),
    (6, 6, 1),
    (7, 7, 1),
    (8, 8, 1),
    (9, 9, 1),
    (10, 10, 1),
    (11, 11, 1),
    (12, 12, 1),
    (13, 13, 1),
    (14, 14, 1),
    (15, 15, 1),
    (16, 16, 1),
    (17, 17, 1),
    (18, 18, 1),
    (19, 19, 1),
    (20, 20, 1),
    (21, 21, 1),
    (22, 22, 1),
    (23, 23, 1),
    (24, 24, 1),
    (25, 25, 1),
    (26, 26, 1),
    (27, 27, 1),
    (28, 28, 1),
    (29, 29, 1),
    (30, 30, 1),    
	(31, 31, 1);

-- roles_users
INSERT INTO roles_users
    (user_id, role_id, community_id)
VALUES
    (1, 1, 1),
    (2, 1, 1),
    (3, 1, 1),
    (4, 1, 1),
    (5, 1, 1),
    (6, 1, 1),
    (7, 1, 1),
    (8, 1, 1),
    (9, 1, 1),
    (10, 1, 1),
    (11, 1, 1),
    (12, 1, 1),
    (13, 1, 1),
    (14, 1, 1),
    (15, 1, 1),
    (16, 1, 1),
    (17, 1, 1),
    (18, 1, 1),
    (19, 1, 1),
    (20, 1, 1),
    (21, 1, 1),
    (22, 1, 1),
    (23, 1, 1),
    (24, 1, 1),
    (25, 1, 1),
    (26, 1, 1),
    (27, 1, 1),
    (28, 1, 1),
    (29, 1, 1),
    (30, 1, 1),   
    (31, 2, 1);    -- 保全



------------------------------------------------------------------------------------tim結束

------------------------------------------------------------------------------------johnson開始
INSERT INTO ticket (reporter_id, title, issue_description, Cost, notes, community_id)
VALUES
(1, N'電梯卡住', N'住戶反映電梯在3樓停止不動，門無法開啟。', 2000, '已聯絡維修廠商', 1),
(2, N'水管漏水', N'廚房水管接縫處滲水，地板濕滑。', 1500, '需要拆除部分牆面處理', 1),
(3, N'監視器畫面異常', N'中庭監視器無影像，疑似鏡頭損壞或線路問題。', 3000, '報價中', 1),
(4, N'地下室排水異味', N'聞到刺鼻氣味，懷疑排水管堵塞。', 800, '建議定期清潔', 1)


INSERT INTO issue_type (issue_type_name) VALUES (N'電梯');
INSERT INTO issue_type (issue_type_name) VALUES (N'水管');
INSERT INTO issue_type (issue_type_name) VALUES (N'水電');
INSERT INTO issue_type (issue_type_name) VALUES (N'瓦斯');
INSERT INTO issue_type (issue_type_name) VALUES (N'門窗');
INSERT INTO issue_type (issue_type_name) VALUES (N'冷氣');
INSERT INTO issue_type (issue_type_name) VALUES (N'照明');
INSERT INTO issue_type (issue_type_name) VALUES (N'網路');
INSERT INTO issue_type (issue_type_name) VALUES (N'地板');
INSERT INTO issue_type (issue_type_name) VALUES (N'牆面滲水');



INSERT INTO ticket_comment (ticket_id,commenter_id,comment) VALUES (1,1,N'天花板漏水');
INSERT INTO ticket_comment (ticket_id,commenter_id,comment) VALUES (1,2,N'希望趕快處理');
INSERT INTO ticket_comment (ticket_id,commenter_id,comment) VALUES (1,1,N'很困擾');
INSERT INTO ticket_comment (ticket_id,commenter_id,comment) VALUES (1,4,N'我會盡快請廠商解決');



INSERT INTO vendor (vendor_name, contact_person, phone_number, [address], notes, tax_ID_number)
VALUES 
(N'台灣水電工程有限公司', N'陳大明', '0912-345-678', N'台北市信義區松仁路1號', N'配合社區水電維修', 12345678),
(N'安心保全股份有限公司', N'林淑芬', '0987-654-321', N'新北市新店區北新路200號', N'保全駐點合作廠商', 87654321),
(N'環保清潔公司', N'張清潔', '0922-111-333', N'台中市西屯區市政北七路99號', N'每週定期清潔社區', 45678901),
(N'東陽消防設備行', N'李安全', '0933-222-444', N'高雄市苓雅區五福一路123號', N'消防器材巡檢', 78901234),
(N'三重電梯維修中心', N'黃文德', '0955-666-888', N'新北市三重區重新路三段168號', N'社區電梯維保合約', 23456789);

------------------------------------------------------------------------------------johnson結束

------------------------------------------------------------------------------------javert開始
--TRUNCATE TABLE facilities;
INSERT INTO facilities (
    community_id, facility_name, max_users, facility_description, facility_location,
    open_time, close_time, reservable_duration, fee, facility_status, created_at
) VALUES
(1, N'交誼廳', NULL, N'可供住戶聚會、會議或活動使用', N'A棟1樓', '09:00', '21:00', 120, 15, 'active', GETDATE()),
(1, N'健身房', 10, N'配有跑步機、啞鈴、飛輪等設備', N'B棟地下1樓', '09:00', '21:00', 120, 5, 'active', GETDATE()),
(1, N'游泳池', 30, N'僅限住戶使用，請著泳裝並遵守規定', N'戶外區', '09:00', '21:00', 120, 10, 'active', GETDATE()),
(1, N'桌球室B101', NULL, N'提供標準雙人桌球場地，限時使用', N'B1-B101', '09:00', '21:00', 120, 10, 'active', GETDATE()),
(1, N'桌球室B102', NULL, N'提供標準雙人桌球場地，限時使用', N'B1-B102', '09:00', '21:00', 120, 10, 'active', GETDATE()),
(1, N'撞球室', NULL, N'提供撞球桌與器材，需預約使用', N'B2撞球室', '09:00', '21:00', 120, 30, 'active', GETDATE()),
(1, N'停車格A1', NULL, N'地下停車場 A1 車位，可短時預約停車', N'地下1樓-A1', '00:00', '23:59', 240, 10, 'active', GETDATE()),
(1, N'停車格A2', NULL, N'地下停車場 A2 車位，可短時預約停車', N'地下1樓-A2', '00:00', '23:59', 240, 10, 'active', GETDATE());


--TRUNCATE TABLE facility_images;
INSERT INTO facility_images (facility_id, image_url, image_description, sort_order)
VALUES 
(1, '/images/facilities/lounge.jpg', N'交誼廳外觀', 0),
(2, '/images/facilities/gym.jpg', N'健身房設備照', 0),
(3, '/images/facilities/pool.jpg', N'游泳池環境', 0),
(4, '/images/facilities/tabletennis1.jpg', N'桌球室B101外觀', 0),
(4, '/images/facilities/tabletennis2.jpg', N'桌球室B102外觀', 1),
(5, '/images/facilities/tabletennis3.jpg', N'桌球室B102外觀', 0),
(6, '/images/facilities/snooker1.jpg', N'撞球室環境', 0),
(6, '/images/facilities/snooker2.jpg', N'撞球室環境', 1),
(6, '/images/facilities/snooker3.jpg', N'撞球室環境', 2),
(7, '/images/facilities/parking_grid1.jpg', N'地下停車場 A1 車位', 0),
(8, '/images/facilities/parking_grid2.jpg', N'地下停車場 A2 車位', 0);


--TRUNCATE TABLE point_accounts;
-- 宣告月底時間（避免每筆都重算）

DECLARE @endOfMonth DATETIME = DATEADD(
    SECOND, -1,
    DATEADD(MONTH, 1, CAST(DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1) AS DATETIME))
);

-- 插入資料
INSERT INTO point_accounts (community_id, unit_id, total_balance, expired_at, updated_at)
VALUES
(1, 1, 100, @endOfMonth, GETDATE()),
(1, 2, 100, @endOfMonth, GETDATE()),
(1, 3, 100, @endOfMonth, GETDATE()),
(1, 4, 100, @endOfMonth, GETDATE()),
(1, 5, 100, @endOfMonth, GETDATE()),
(1, 6, 100, @endOfMonth, GETDATE()),
(1, 7, 100, @endOfMonth, GETDATE()),
(1, 8, 9999, @endOfMonth, GETDATE()),
(1, 9, 100, @endOfMonth, GETDATE()),
(1, 10, 100, @endOfMonth, GETDATE()),
(1, 11, 100, @endOfMonth, GETDATE()),
(1, 12, 100, @endOfMonth, GETDATE()),
(1, 13, 100, @endOfMonth, GETDATE()),
(1, 14, 100, @endOfMonth, GETDATE()),
(1, 15, 100, @endOfMonth, GETDATE()),
(1, 16, 100, @endOfMonth, GETDATE()),
(1, 17, 100, @endOfMonth, GETDATE()),
(1, 18, 100, @endOfMonth, GETDATE()),
(1, 19, 100, @endOfMonth, GETDATE()),
(1, 20, 100, @endOfMonth, GETDATE()),
(1, 21, 100, @endOfMonth, GETDATE()),
(1, 22, 100, @endOfMonth, GETDATE()),
(1, 23, 100, @endOfMonth, GETDATE()),
(1, 24, 100, @endOfMonth, GETDATE()),
(1, 25, 100, @endOfMonth, GETDATE()),
(1, 26, 100, @endOfMonth, GETDATE()),
(1, 27, 100, @endOfMonth, GETDATE()),
(1, 28, 100, @endOfMonth, GETDATE()),
(1, 29, 100, @endOfMonth, GETDATE()),
(1, 30, 100, @endOfMonth, GETDATE()),
(1, 31, 99999, @endOfMonth, GETDATE());

-- 預約紀錄 (共150筆)
INSERT INTO facility_reservations (
    community_id, unit_id, facility_id, number_of_users, reservation_start_time, reservation_end_time,
    is_admin, required_points, actual_used_points, point_expire_at, remark, reservation_status, created_at)
VALUES
(1, 2, 2, 2, '2025-07-23 14:00:00', '2025-07-23 15:00:00', 0, 5, 0, @endOfMonth, N'', 'CANCELLED', GETDATE()),
(1, 4, 1, 3, '2025-07-19 11:00:00', '2025-07-19 12:00:00', 0, 15, 15, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 4, 4, 1, '2025-07-30 13:00:00', '2025-07-30 14:00:00', 0, 10, 0, @endOfMonth, N'', 'CANCELLED', GETDATE()),
(1, 4, 4, 5, '2025-07-23 09:00:00', '2025-07-23 10:00:00', 0, 10, 10, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 1, 4, 3, '2025-08-05 18:00:00', '2025-08-05 19:00:00', 0, 10, 10, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 3, 4, 5, '2025-08-04 19:00:00', '2025-08-04 20:00:00', 0, 10, 10, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 3, 2, 5, '2025-08-01 18:00:00', '2025-08-01 19:00:00', 0, 5, 5, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 1, 4, 3, '2025-08-04 14:00:00', '2025-08-04 15:00:00', 0, 10, 10, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 4, 3, 15, '2025-07-31 11:00:00', '2025-07-31 12:00:00', 0, 10, 0, @endOfMonth, N'朋友聚會', 'CANCELLED', GETDATE()),
(1, 4, 2, 4, '2025-07-28 16:00:00', '2025-07-28 17:00:00', 0, 5, 5, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 4, 1, 2, '2025-08-03 14:00:00', '2025-08-03 15:00:00', 0, 15, 0, @endOfMonth, N'家庭練習', 'CANCELLED', GETDATE()),
(1, 4, 2, 2, '2025-07-22 18:00:00', '2025-07-22 19:00:00', 0, 5, 5, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 3, 6, 5, '2025-08-02 18:00:00', '2025-08-02 19:00:00', 0, 30, 30, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 3, 1, 3, '2025-07-25 10:00:00', '2025-07-25 11:00:00', 0, 15, 15, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 3, 6, 4, '2025-08-04 11:00:00', '2025-08-04 12:00:00', 0, 30, 30, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 2, 6, 3, '2025-07-22 17:00:00', '2025-07-22 18:00:00', 0, 30, 30, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 2, 3, 1, '2025-07-27 18:00:00', '2025-07-27 19:00:00', 0, 10, 10, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 2, 6, 1, '2025-08-04 17:00:00', '2025-08-04 18:00:00', 0, 30, 30, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 3, 4, 1, '2025-08-02 17:00:00', '2025-08-02 18:00:00', 0, 10, 0, @endOfMonth, N'家庭練習', 'CANCELLED', GETDATE()),
(1, 1, 4, 5, '2025-07-19 10:00:00', '2025-07-19 11:00:00', 0, 10, 0, @endOfMonth, N'', 'CANCELLED', GETDATE()),
(1, 2, 4, 2, '2025-08-05 12:00:00', '2025-08-05 13:00:00', 0, 10, 0, @endOfMonth, N'團體活動', 'CANCELLED', GETDATE()),
(1, 4, 6, 1, '2025-07-31 14:00:00', '2025-07-31 15:00:00', 0, 30, 0, @endOfMonth, N'', 'CANCELLED', GETDATE()),
(1, 1, 2, 1, '2025-07-27 19:00:00', '2025-07-27 20:00:00', 0, 5, 5, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 4, 3, 19, '2025-07-17 18:00:00', '2025-07-17 19:00:00', 0, 10, 10, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 1, 1, 2, '2025-07-17 17:00:00', '2025-07-17 18:00:00', 0, 15, 15, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 4, 6, 3, '2025-07-26 17:00:00', '2025-07-26 18:00:00', 0, 30, 30, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 4, 2, 4, '2025-07-21 17:00:00', '2025-07-21 18:00:00', 0, 5, 5, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 1, 4, 5, '2025-07-29 12:00:00', '2025-07-29 13:00:00', 0, 10, 0, @endOfMonth, N'家庭練習', 'CANCELLED', GETDATE()),
(1, 4, 3, 27, '2025-07-21 10:00:00', '2025-07-21 11:00:00', 0, 10, 10, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 3, 2, 4, '2025-07-22 09:00:00', '2025-07-22 10:00:00', 0, 5, 5, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 2, 2, 4, '2025-07-24 09:00:00', '2025-07-24 10:00:00', 0, 5, 5, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 4, 6, 4, '2025-08-01 16:00:00', '2025-08-01 17:00:00', 0, 30, 30, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 1, 1, 1, '2025-07-17 18:00:00', '2025-07-17 19:00:00', 0, 15, 15, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 3, 1, 3, '2025-07-30 10:00:00', '2025-07-30 11:00:00', 0, 15, 15, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 3, 6, 4, '2025-07-29 12:00:00', '2025-07-29 13:00:00', 0, 30, 30, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 3, 4, 1, '2025-07-24 16:00:00', '2025-07-24 17:00:00', 0, 10, 10, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 2, 6, 3, '2025-07-30 13:00:00', '2025-07-30 14:00:00', 0, 30, 30, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 3, 1, 1, '2025-07-18 14:00:00', '2025-07-18 15:00:00', 0, 15, 15, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 2, 4, 1, '2025-07-24 18:00:00', '2025-07-24 19:00:00', 0, 10, 10, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 3, 1, 2, '2025-07-21 10:00:00', '2025-07-21 11:00:00', 0, 15, 15, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 3, 2, 4, '2025-08-04 15:00:00', '2025-08-04 16:00:00', 0, 5, 0, @endOfMonth, N'朋友聚會', 'CANCELLED', GETDATE()),
(1, 2, 3, 8, '2025-07-18 13:00:00', '2025-07-18 14:00:00', 0, 10, 10, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 1, 3, 30, '2025-07-24 12:00:00', '2025-07-24 13:00:00', 0, 10, 10, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 3, 4, 5, '2025-07-24 17:00:00', '2025-07-24 18:00:00', 0, 10, 10, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 4, 2, 2, '2025-07-31 14:00:00', '2025-07-31 15:00:00', 0, 5, 5, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 2, 1, 2, '2025-07-24 12:00:00', '2025-07-24 13:00:00', 0, 15, 0, @endOfMonth, N'朋友聚會', 'CANCELLED', GETDATE()),
(1, 2, 3, 21, '2025-07-25 11:00:00', '2025-07-25 12:00:00', 0, 10, 10, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 4, 3, 23, '2025-07-28 14:00:00', '2025-07-28 15:00:00', 0, 10, 10, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 3, 3, 24, '2025-07-28 11:00:00', '2025-07-28 12:00:00', 0, 10, 0, @endOfMonth, N'朋友聚會', 'CANCELLED', GETDATE()),
(1, 4, 6, 5, '2025-07-19 17:00:00', '2025-07-19 18:00:00', 0, 30, 30, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 2, 1, 2, '2025-07-18 12:00:00', '2025-07-18 13:00:00', 0, 15, 15, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 1, 2, 2, '2025-07-22 14:00:00', '2025-07-22 15:00:00', 0, 5, 5, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 1, 2, 2, '2025-08-03 19:00:00', '2025-08-03 20:00:00', 0, 5, 5, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 2, 6, 2, '2025-07-23 19:00:00', '2025-07-23 20:00:00', 0, 30, 30, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 3, 3, 24, '2025-08-05 15:00:00', '2025-08-05 16:00:00', 0, 10, 10, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 2, 1, 4, '2025-08-01 14:00:00', '2025-08-01 15:00:00', 0, 15, 15, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 2, 4, 1, '2025-07-17 10:00:00', '2025-07-17 11:00:00', 0, 10, 10, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 3, 6, 3, '2025-07-18 09:00:00', '2025-07-18 10:00:00', 0, 30, 30, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 4, 6, 1, '2025-07-17 15:00:00', '2025-07-17 16:00:00', 0, 30, 0, @endOfMonth, N'', 'CANCELLED', GETDATE()),
(1, 4, 1, 4, '2025-07-21 14:00:00', '2025-07-21 15:00:00', 0, 15, 0, @endOfMonth, N'', 'CANCELLED', GETDATE()),
(1, 2, 1, 4, '2025-07-18 14:00:00', '2025-07-18 15:00:00', 0, 15, 15, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 1, 6, 2, '2025-08-02 11:00:00', '2025-08-02 12:00:00', 0, 30, 30, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 1, 1, 3, '2025-07-27 10:00:00', '2025-07-27 11:00:00', 0, 15, 15, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 2, 2, 2, '2025-08-05 11:00:00', '2025-08-05 12:00:00', 0, 5, 5, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 1, 3, 4, '2025-08-04 14:00:00', '2025-08-04 15:00:00', 0, 10, 0, @endOfMonth, N'家庭練習', 'CANCELLED', GETDATE()),
(1, 1, 1, 3, '2025-08-05 14:00:00', '2025-08-05 15:00:00', 0, 15, 0, @endOfMonth, N'團體活動', 'CANCELLED', GETDATE()),
(1, 1, 1, 1, '2025-08-01 09:00:00', '2025-08-01 10:00:00', 0, 15, 0, @endOfMonth, N'', 'CANCELLED', GETDATE()),
(1, 3, 2, 1, '2025-07-31 14:00:00', '2025-07-31 15:00:00', 0, 5, 5, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 4, 1, 2, '2025-07-31 11:00:00', '2025-07-31 12:00:00', 0, 15, 15, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 4, 1, 3, '2025-07-18 10:00:00', '2025-07-18 11:00:00', 0, 15, 15, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 2, 3, 13, '2025-07-29 18:00:00', '2025-07-29 19:00:00', 0, 10, 10, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 1, 1, 1, '2025-08-03 09:00:00', '2025-08-03 10:00:00', 0, 15, 15, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 3, 4, 4, '2025-08-04 19:00:00', '2025-08-04 20:00:00', 0, 10, 0, @endOfMonth, N'', 'CANCELLED', GETDATE()),
(1, 4, 6, 1, '2025-08-03 13:00:00', '2025-08-03 14:00:00', 0, 30, 30, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 2, 3, 20, '2025-07-27 15:00:00', '2025-07-27 16:00:00', 0, 10, 10, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 2, 3, 9, '2025-07-27 09:00:00', '2025-07-27 10:00:00', 0, 10, 10, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 2, 3, 13, '2025-07-22 13:00:00', '2025-07-22 14:00:00', 0, 10, 10, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 2, 6, 1, '2025-07-25 18:00:00', '2025-07-25 19:00:00', 0, 30, 30, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 3, 4, 1, '2025-07-30 16:00:00', '2025-07-30 17:00:00', 0, 10, 10, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 3, 3, 6, '2025-07-26 15:00:00', '2025-07-26 16:00:00', 0, 10, 10, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 2, 1, 1, '2025-07-17 15:00:00', '2025-07-17 16:00:00', 0, 15, 15, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 2, 4, 1, '2025-07-25 10:00:00', '2025-07-25 11:00:00', 0, 10, 10, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 1, 1, 1, '2025-08-03 16:00:00', '2025-08-03 17:00:00', 0, 15, 0, @endOfMonth, N'朋友聚會', 'CANCELLED', GETDATE()),
(1, 3, 6, 4, '2025-07-23 16:00:00', '2025-07-23 17:00:00', 0, 30, 30, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 1, 4, 4, '2025-08-05 17:00:00', '2025-08-05 18:00:00', 0, 10, 10, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 1, 3, 7, '2025-07-17 17:00:00', '2025-07-17 18:00:00', 0, 10, 0, @endOfMonth, N'', 'CANCELLED', GETDATE()),
(1, 3, 6, 3, '2025-07-31 11:00:00', '2025-07-31 12:00:00', 0, 30, 0, @endOfMonth, N'家庭練習', 'CANCELLED', GETDATE()),
(1, 3, 2, 3, '2025-08-05 12:00:00', '2025-08-05 13:00:00', 0, 5, 5, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 4, 1, 1, '2025-07-22 16:00:00', '2025-07-22 17:00:00', 0, 15, 15, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 2, 1, 5, '2025-08-01 10:00:00', '2025-08-01 11:00:00', 0, 15, 0, @endOfMonth, N'朋友聚會', 'CANCELLED', GETDATE()),
(1, 1, 6, 4, '2025-07-31 12:00:00', '2025-07-31 13:00:00', 0, 30, 30, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 2, 1, 4, '2025-07-30 15:00:00', '2025-07-30 16:00:00', 0, 15, 0, @endOfMonth, N'', 'CANCELLED', GETDATE()),
(1, 2, 1, 2, '2025-08-01 10:00:00', '2025-08-01 11:00:00', 0, 15, 15, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 1, 3, 15, '2025-07-21 12:00:00', '2025-07-21 13:00:00', 0, 10, 10, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 1, 4, 3, '2025-07-25 14:00:00', '2025-07-25 15:00:00', 0, 10, 10, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 3, 2, 2, '2025-07-30 13:00:00', '2025-07-30 14:00:00', 0, 5, 0, @endOfMonth, N'', 'CANCELLED', GETDATE()),
(1, 1, 2, 1, '2025-07-18 13:00:00', '2025-07-18 14:00:00', 0, 5, 0, @endOfMonth, N'朋友聚會', 'CANCELLED', GETDATE()),
(1, 1, 3, 15, '2025-07-26 19:00:00', '2025-07-26 20:00:00', 0, 10, 0, @endOfMonth, N'朋友聚會', 'CANCELLED', GETDATE()),
(1, 2, 6, 3, '2025-07-19 15:00:00', '2025-07-19 16:00:00', 0, 30, 30, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 4, 3, 27, '2025-08-04 15:00:00', '2025-08-04 16:00:00', 0, 10, 0, @endOfMonth, N'家庭練習', 'CANCELLED', GETDATE()),
(1, 3, 2, 2, '2025-07-27 11:00:00', '2025-07-27 12:00:00', 0, 5, 0, @endOfMonth, N'家庭練習', 'CANCELLED', GETDATE()),
(1, 1, 3, 13, '2025-07-19 09:00:00', '2025-07-19 10:00:00', 0, 10, 10, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 1, 4, 3, '2025-08-01 17:00:00', '2025-08-01 18:00:00', 0, 10, 10, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 4, 1, 4, '2025-07-30 14:00:00', '2025-07-30 15:00:00', 0, 15, 0, @endOfMonth, N'團體活動', 'CANCELLED', GETDATE()),
(1, 2, 2, 3, '2025-07-17 09:00:00', '2025-07-17 10:00:00', 0, 5, 5, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 3, 1, 4, '2025-08-02 16:00:00', '2025-08-02 17:00:00', 0, 15, 15, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 4, 2, 5, '2025-07-30 18:00:00', '2025-07-30 19:00:00', 0, 5, 0, @endOfMonth, N'朋友聚會', 'CANCELLED', GETDATE()),
(1, 3, 2, 3, '2025-07-20 10:00:00', '2025-07-20 11:00:00', 0, 5, 5, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 1, 4, 1, '2025-07-24 09:00:00', '2025-07-24 10:00:00', 0, 10, 10, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 3, 1, 3, '2025-07-23 10:00:00', '2025-07-23 11:00:00', 0, 15, 15, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 4, 6, 5, '2025-08-05 15:00:00', '2025-08-05 16:00:00', 0, 30, 30, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 2, 4, 4, '2025-07-24 10:00:00', '2025-07-24 11:00:00', 0, 10, 10, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 2, 6, 1, '2025-08-04 16:00:00', '2025-08-04 17:00:00', 0, 30, 30, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 1, 4, 2, '2025-07-27 15:00:00', '2025-07-27 16:00:00', 0, 10, 0, @endOfMonth, N'團體活動', 'CANCELLED', GETDATE()),
(1, 4, 4, 1, '2025-08-05 10:00:00', '2025-08-05 11:00:00', 0, 10, 10, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 2, 3, 21, '2025-07-23 11:00:00', '2025-07-23 12:00:00', 0, 10, 10, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 4, 2, 1, '2025-07-26 09:00:00', '2025-07-26 10:00:00', 0, 5, 5, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 4, 1, 1, '2025-07-21 10:00:00', '2025-07-21 11:00:00', 0, 15, 15, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 3, 4, 5, '2025-08-03 19:00:00', '2025-08-03 20:00:00', 0, 10, 10, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 3, 1, 5, '2025-07-25 10:00:00', '2025-07-25 11:00:00', 0, 15, 15, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 4, 1, 4, '2025-08-05 19:00:00', '2025-08-05 20:00:00', 0, 15, 15, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 4, 1, 1, '2025-07-28 18:00:00', '2025-07-28 19:00:00', 0, 15, 15, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 1, 3, 4, '2025-07-21 17:00:00', '2025-07-21 18:00:00', 0, 10, 10, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 2, 1, 2, '2025-07-29 19:00:00', '2025-07-29 20:00:00', 0, 15, 0, @endOfMonth, N'團體活動', 'CANCELLED', GETDATE()),
(1, 4, 2, 2, '2025-07-18 09:00:00', '2025-07-18 10:00:00', 0, 5, 5, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 4, 4, 4, '2025-07-22 13:00:00', '2025-07-22 14:00:00', 0, 10, 10, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 1, 2, 1, '2025-07-17 12:00:00', '2025-07-17 13:00:00', 0, 5, 0, @endOfMonth, N'家庭練習', 'CANCELLED', GETDATE()),
(1, 3, 6, 1, '2025-08-01 14:00:00', '2025-08-01 15:00:00', 0, 30, 30, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 4, 4, 4, '2025-08-01 17:00:00', '2025-08-01 18:00:00', 0, 10, 10, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 3, 6, 2, '2025-08-02 19:00:00', '2025-08-02 20:00:00', 0, 30, 0, @endOfMonth, N'家庭練習', 'CANCELLED', GETDATE()),
(1, 1, 2, 2, '2025-07-18 18:00:00', '2025-07-18 19:00:00', 0, 5, 5, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 2, 4, 5, '2025-07-19 12:00:00', '2025-07-19 13:00:00', 0, 10, 10, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 3, 2, 3, '2025-07-21 18:00:00', '2025-07-21 19:00:00', 0, 5, 5, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 4, 3, 15, '2025-07-26 13:00:00', '2025-07-26 14:00:00', 0, 10, 0, @endOfMonth, N'家庭練習', 'CANCELLED', GETDATE()),
(1, 3, 6, 5, '2025-07-30 17:00:00', '2025-07-30 18:00:00', 0, 30, 0, @endOfMonth, N'團體活動', 'CANCELLED', GETDATE()),
(1, 2, 2, 5, '2025-07-21 18:00:00', '2025-07-21 19:00:00', 0, 5, 5, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 4, 4, 4, '2025-07-24 18:00:00', '2025-07-24 19:00:00', 0, 10, 0, @endOfMonth, N'團體活動', 'CANCELLED', GETDATE()),
(1, 1, 2, 5, '2025-07-30 16:00:00', '2025-07-30 17:00:00', 0, 5, 5, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 2, 4, 2, '2025-07-31 19:00:00', '2025-07-31 20:00:00', 0, 10, 10, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 1, 1, 4, '2025-07-20 19:00:00', '2025-07-20 20:00:00', 0, 15, 15, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 2, 1, 1, '2025-07-22 09:00:00', '2025-07-22 10:00:00', 0, 15, 15, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 1, 3, 29, '2025-07-28 18:00:00', '2025-07-28 19:00:00', 0, 10, 10, @endOfMonth, N'', 'APPROVED', GETDATE()),
(1, 3, 1, 4, '2025-07-19 11:00:00', '2025-07-19 12:00:00', 0, 15, 15, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 2, 4, 1, '2025-07-22 17:00:00', '2025-07-22 18:00:00', 0, 10, 10, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 3, 4, 5, '2025-07-27 19:00:00', '2025-07-27 20:00:00', 0, 10, 0, @endOfMonth, N'朋友聚會', 'CANCELLED', GETDATE()),
(1, 4, 2, 2, '2025-07-31 15:00:00', '2025-07-31 16:00:00', 0, 5, 5, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 1, 4, 1, '2025-07-19 11:00:00', '2025-07-19 12:00:00', 0, 10, 10, @endOfMonth, N'團體活動', 'APPROVED', GETDATE()),
(1, 4, 1, 5, '2025-07-25 15:00:00', '2025-07-25 16:00:00', 0, 15, 15, @endOfMonth, N'家庭練習', 'APPROVED', GETDATE()),
(1, 3, 1, 2, '2025-08-01 12:00:00', '2025-08-01 13:00:00', 0, 15, 15, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE()),
(1, 4, 3, 18, '2025-08-04 09:00:00', '2025-08-04 10:00:00', 0, 10, 10, @endOfMonth, N'朋友聚會', 'APPROVED', GETDATE());

-- 交易紀錄 (含對應預約與轉帳)
INSERT INTO point_transactions (
    community_id, unit_id, transaction_type, amount,
    related_unit_id, related_reservation_id, transaction_description, created_at)
VALUES
(1, 2, 'CANCEL', +5, NULL, 1, N'林慧如 cancel 健身房', GETDATE()),
(1, 4, 'RESERVATION', -15, NULL, 2, N'黃怡君 reservation 交誼廳', GETDATE()),
(1, 4, 'CANCEL', +10, NULL, 3, N'黃怡君 cancel 桌球室B101', GETDATE()),
(1, 4, 'RESERVATION', -10, NULL, 4, N'黃怡君 reservation 桌球室B101', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 5, N'陳志明 reservation 桌球室B101', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 6, N'張嘉豪 reservation 桌球室B101', GETDATE()),
(1, 3, 'RESERVATION', -5, NULL, 7, N'張嘉豪 reservation 健身房', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 8, N'陳志明 reservation 桌球室B101', GETDATE()),
(1, 4, 'CANCEL', +10, NULL, 9, N'黃怡君 cancel 游泳池', GETDATE()),
(1, 4, 'RESERVATION', -5, NULL, 10, N'黃怡君 reservation 健身房', GETDATE()),
(1, 4, 'CANCEL', +15, NULL, 11, N'黃怡君 cancel 交誼廳', GETDATE()),
(1, 4, 'RESERVATION', -5, NULL, 12, N'黃怡君 reservation 健身房', GETDATE()),
(1, 3, 'RESERVATION', -30, NULL, 13, N'張嘉豪 reservation 撞球室', GETDATE()),
(1, 3, 'RESERVATION', -15, NULL, 14, N'張嘉豪 reservation 交誼廳', GETDATE()),
(1, 3, 'RESERVATION', -30, NULL, 15, N'張嘉豪 reservation 撞球室', GETDATE()),
(1, 2, 'RESERVATION', -30, NULL, 16, N'林慧如 reservation 撞球室', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 17, N'林慧如 reservation 游泳池', GETDATE()),
(1, 2, 'RESERVATION', -30, NULL, 18, N'林慧如 reservation 撞球室', GETDATE()),
(1, 3, 'CANCEL', +10, NULL, 19, N'張嘉豪 cancel 桌球室B101', GETDATE()),
(1, 1, 'CANCEL', +10, NULL, 20, N'陳志明 cancel 桌球室B101', GETDATE()),
(1, 2, 'CANCEL', +10, NULL, 21, N'林慧如 cancel 桌球室B101', GETDATE()),
(1, 4, 'CANCEL', +30, NULL, 22, N'黃怡君 cancel 撞球室', GETDATE()),
(1, 1, 'RESERVATION', -5, NULL, 23, N'陳志明 reservation 健身房', GETDATE()),
(1, 4, 'RESERVATION', -10, NULL, 24, N'黃怡君 reservation 游泳池', GETDATE()),
(1, 1, 'RESERVATION', -15, NULL, 25, N'陳志明 reservation 交誼廳', GETDATE()),
(1, 4, 'RESERVATION', -30, NULL, 26, N'黃怡君 reservation 撞球室', GETDATE()),
(1, 4, 'RESERVATION', -5, NULL, 27, N'黃怡君 reservation 健身房', GETDATE()),
(1, 1, 'CANCEL', +10, NULL, 28, N'陳志明 cancel 桌球室B101', GETDATE()),
(1, 4, 'RESERVATION', -10, NULL, 29, N'黃怡君 reservation 游泳池', GETDATE()),
(1, 3, 'RESERVATION', -5, NULL, 30, N'張嘉豪 reservation 健身房', GETDATE()),
(1, 2, 'RESERVATION', -5, NULL, 31, N'林慧如 reservation 健身房', GETDATE()),
(1, 4, 'RESERVATION', -30, NULL, 32, N'黃怡君 reservation 撞球室', GETDATE()),
(1, 1, 'RESERVATION', -15, NULL, 33, N'陳志明 reservation 交誼廳', GETDATE()),
(1, 3, 'RESERVATION', -15, NULL, 34, N'張嘉豪 reservation 交誼廳', GETDATE()),
(1, 3, 'RESERVATION', -30, NULL, 35, N'張嘉豪 reservation 撞球室', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 36, N'張嘉豪 reservation 桌球室B101', GETDATE()),
(1, 2, 'RESERVATION', -30, NULL, 37, N'林慧如 reservation 撞球室', GETDATE()),
(1, 3, 'RESERVATION', -15, NULL, 38, N'張嘉豪 reservation 交誼廳', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 39, N'林慧如 reservation 桌球室B101', GETDATE()),
(1, 3, 'RESERVATION', -15, NULL, 40, N'張嘉豪 reservation 交誼廳', GETDATE()),
(1, 3, 'CANCEL', +5, NULL, 41, N'張嘉豪 cancel 健身房', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 42, N'林慧如 reservation 游泳池', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 43, N'陳志明 reservation 游泳池', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 44, N'張嘉豪 reservation 桌球室B101', GETDATE()),
(1, 4, 'RESERVATION', -5, NULL, 45, N'黃怡君 reservation 健身房', GETDATE()),
(1, 2, 'CANCEL', +15, NULL, 46, N'林慧如 cancel 交誼廳', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 47, N'林慧如 reservation 游泳池', GETDATE()),
(1, 4, 'RESERVATION', -10, NULL, 48, N'黃怡君 reservation 游泳池', GETDATE()),
(1, 3, 'CANCEL', +10, NULL, 49, N'張嘉豪 cancel 游泳池', GETDATE()),
(1, 4, 'RESERVATION', -30, NULL, 50, N'黃怡君 reservation 撞球室', GETDATE()),
(1, 2, 'RESERVATION', -15, NULL, 51, N'林慧如 reservation 交誼廳', GETDATE()),
(1, 1, 'RESERVATION', -5, NULL, 52, N'陳志明 reservation 健身房', GETDATE()),
(1, 1, 'RESERVATION', -5, NULL, 53, N'陳志明 reservation 健身房', GETDATE()),
(1, 2, 'RESERVATION', -30, NULL, 54, N'林慧如 reservation 撞球室', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 55, N'張嘉豪 reservation 游泳池', GETDATE()),
(1, 2, 'RESERVATION', -15, NULL, 56, N'林慧如 reservation 交誼廳', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 57, N'林慧如 reservation 桌球室B101', GETDATE()),
(1, 3, 'RESERVATION', -30, NULL, 58, N'張嘉豪 reservation 撞球室', GETDATE()),
(1, 4, 'CANCEL', +30, NULL, 59, N'黃怡君 cancel 撞球室', GETDATE()),
(1, 4, 'CANCEL', +15, NULL, 60, N'黃怡君 cancel 交誼廳', GETDATE()),
(1, 2, 'RESERVATION', -15, NULL, 61, N'林慧如 reservation 交誼廳', GETDATE()),
(1, 1, 'RESERVATION', -30, NULL, 62, N'陳志明 reservation 撞球室', GETDATE()),
(1, 1, 'RESERVATION', -15, NULL, 63, N'陳志明 reservation 交誼廳', GETDATE()),
(1, 2, 'RESERVATION', -5, NULL, 64, N'林慧如 reservation 健身房', GETDATE()),
(1, 1, 'CANCEL', +10, NULL, 65, N'陳志明 cancel 游泳池', GETDATE()),
(1, 1, 'CANCEL', +15, NULL, 66, N'陳志明 cancel 交誼廳', GETDATE()),
(1, 1, 'CANCEL', +15, NULL, 67, N'陳志明 cancel 交誼廳', GETDATE()),
(1, 3, 'RESERVATION', -5, NULL, 68, N'張嘉豪 reservation 健身房', GETDATE()),
(1, 4, 'RESERVATION', -15, NULL, 69, N'黃怡君 reservation 交誼廳', GETDATE()),
(1, 4, 'RESERVATION', -15, NULL, 70, N'黃怡君 reservation 交誼廳', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 71, N'林慧如 reservation 游泳池', GETDATE()),
(1, 1, 'RESERVATION', -15, NULL, 72, N'陳志明 reservation 交誼廳', GETDATE()),
(1, 3, 'CANCEL', +10, NULL, 73, N'張嘉豪 cancel 桌球室B101', GETDATE()),
(1, 4, 'RESERVATION', -30, NULL, 74, N'黃怡君 reservation 撞球室', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 75, N'林慧如 reservation 游泳池', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 76, N'林慧如 reservation 游泳池', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 77, N'林慧如 reservation 游泳池', GETDATE()),
(1, 2, 'RESERVATION', -30, NULL, 78, N'林慧如 reservation 撞球室', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 79, N'張嘉豪 reservation 桌球室B101', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 80, N'張嘉豪 reservation 游泳池', GETDATE()),
(1, 2, 'RESERVATION', -15, NULL, 81, N'林慧如 reservation 交誼廳', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 82, N'林慧如 reservation 桌球室B101', GETDATE()),
(1, 1, 'CANCEL', +15, NULL, 83, N'陳志明 cancel 交誼廳', GETDATE()),
(1, 3, 'RESERVATION', -30, NULL, 84, N'張嘉豪 reservation 撞球室', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 85, N'陳志明 reservation 桌球室B101', GETDATE()),
(1, 1, 'CANCEL', +10, NULL, 86, N'陳志明 cancel 游泳池', GETDATE()),
(1, 3, 'CANCEL', +30, NULL, 87, N'張嘉豪 cancel 撞球室', GETDATE()),
(1, 3, 'RESERVATION', -5, NULL, 88, N'張嘉豪 reservation 健身房', GETDATE()),
(1, 4, 'RESERVATION', -15, NULL, 89, N'黃怡君 reservation 交誼廳', GETDATE()),
(1, 2, 'CANCEL', +15, NULL, 90, N'林慧如 cancel 交誼廳', GETDATE()),
(1, 1, 'RESERVATION', -30, NULL, 91, N'陳志明 reservation 撞球室', GETDATE()),
(1, 2, 'CANCEL', +15, NULL, 92, N'林慧如 cancel 交誼廳', GETDATE()),
(1, 2, 'RESERVATION', -15, NULL, 93, N'林慧如 reservation 交誼廳', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 94, N'陳志明 reservation 游泳池', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 95, N'陳志明 reservation 桌球室B101', GETDATE()),
(1, 3, 'CANCEL', +5, NULL, 96, N'張嘉豪 cancel 健身房', GETDATE()),
(1, 1, 'CANCEL', +5, NULL, 97, N'陳志明 cancel 健身房', GETDATE()),
(1, 1, 'CANCEL', +10, NULL, 98, N'陳志明 cancel 游泳池', GETDATE()),
(1, 2, 'RESERVATION', -30, NULL, 99, N'林慧如 reservation 撞球室', GETDATE()),
(1, 4, 'CANCEL', +10, NULL, 100, N'黃怡君 cancel 游泳池', GETDATE()),
(1, 3, 'CANCEL', +5, NULL, 101, N'張嘉豪 cancel 健身房', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 102, N'陳志明 reservation 游泳池', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 103, N'陳志明 reservation 桌球室B101', GETDATE()),
(1, 4, 'CANCEL', +15, NULL, 104, N'黃怡君 cancel 交誼廳', GETDATE()),
(1, 2, 'RESERVATION', -5, NULL, 105, N'林慧如 reservation 健身房', GETDATE()),
(1, 3, 'RESERVATION', -15, NULL, 106, N'張嘉豪 reservation 交誼廳', GETDATE()),
(1, 4, 'CANCEL', +5, NULL, 107, N'黃怡君 cancel 健身房', GETDATE()),
(1, 3, 'RESERVATION', -5, NULL, 108, N'張嘉豪 reservation 健身房', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 109, N'陳志明 reservation 桌球室B101', GETDATE()),
(1, 3, 'RESERVATION', -15, NULL, 110, N'張嘉豪 reservation 交誼廳', GETDATE()),
(1, 4, 'RESERVATION', -30, NULL, 111, N'黃怡君 reservation 撞球室', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 112, N'林慧如 reservation 桌球室B101', GETDATE()),
(1, 2, 'RESERVATION', -30, NULL, 113, N'林慧如 reservation 撞球室', GETDATE()),
(1, 1, 'CANCEL', +10, NULL, 114, N'陳志明 cancel 桌球室B101', GETDATE()),
(1, 4, 'RESERVATION', -10, NULL, 115, N'黃怡君 reservation 桌球室B101', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 116, N'林慧如 reservation 游泳池', GETDATE()),
(1, 4, 'RESERVATION', -5, NULL, 117, N'黃怡君 reservation 健身房', GETDATE()),
(1, 4, 'RESERVATION', -15, NULL, 118, N'黃怡君 reservation 交誼廳', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 119, N'張嘉豪 reservation 桌球室B101', GETDATE()),
(1, 3, 'RESERVATION', -15, NULL, 120, N'張嘉豪 reservation 交誼廳', GETDATE()),
(1, 4, 'RESERVATION', -15, NULL, 121, N'黃怡君 reservation 交誼廳', GETDATE()),
(1, 4, 'RESERVATION', -15, NULL, 122, N'黃怡君 reservation 交誼廳', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 123, N'陳志明 reservation 游泳池', GETDATE()),
(1, 2, 'CANCEL', +15, NULL, 124, N'林慧如 cancel 交誼廳', GETDATE()),
(1, 4, 'RESERVATION', -5, NULL, 125, N'黃怡君 reservation 健身房', GETDATE()),
(1, 4, 'RESERVATION', -10, NULL, 126, N'黃怡君 reservation 桌球室B101', GETDATE()),
(1, 1, 'CANCEL', +5, NULL, 127, N'陳志明 cancel 健身房', GETDATE()),
(1, 3, 'RESERVATION', -30, NULL, 128, N'張嘉豪 reservation 撞球室', GETDATE()),
(1, 4, 'RESERVATION', -10, NULL, 129, N'黃怡君 reservation 桌球室B101', GETDATE()),
(1, 3, 'CANCEL', +30, NULL, 130, N'張嘉豪 cancel 撞球室', GETDATE()),
(1, 1, 'RESERVATION', -5, NULL, 131, N'陳志明 reservation 健身房', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 132, N'林慧如 reservation 桌球室B101', GETDATE()),
(1, 3, 'RESERVATION', -5, NULL, 133, N'張嘉豪 reservation 健身房', GETDATE()),
(1, 4, 'CANCEL', +10, NULL, 134, N'黃怡君 cancel 游泳池', GETDATE()),
(1, 3, 'CANCEL', +30, NULL, 135, N'張嘉豪 cancel 撞球室', GETDATE()),
(1, 2, 'RESERVATION', -5, NULL, 136, N'林慧如 reservation 健身房', GETDATE()),
(1, 4, 'CANCEL', +10, NULL, 137, N'黃怡君 cancel 桌球室B101', GETDATE()),
(1, 1, 'RESERVATION', -5, NULL, 138, N'陳志明 reservation 健身房', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 139, N'林慧如 reservation 桌球室B101', GETDATE()),
(1, 1, 'RESERVATION', -15, NULL, 140, N'陳志明 reservation 交誼廳', GETDATE()),
(1, 2, 'RESERVATION', -15, NULL, 141, N'林慧如 reservation 交誼廳', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 142, N'陳志明 reservation 游泳池', GETDATE()),
(1, 3, 'RESERVATION', -15, NULL, 143, N'張嘉豪 reservation 交誼廳', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 144, N'林慧如 reservation 桌球室B101', GETDATE()),
(1, 3, 'CANCEL', +10, NULL, 145, N'張嘉豪 cancel 桌球室B101', GETDATE()),
(1, 4, 'RESERVATION', -5, NULL, 146, N'黃怡君 reservation 健身房', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 147, N'陳志明 reservation 桌球室B101', GETDATE()),
(1, 4, 'RESERVATION', -15, NULL, 148, N'黃怡君 reservation 交誼廳', GETDATE()),
(1, 3, 'RESERVATION', -15, NULL, 149, N'張嘉豪 reservation 交誼廳', GETDATE()),
(1, 4, 'RESERVATION', -10, NULL, 150, N'黃怡君 reservation 游泳池', GETDATE()),
(1, 2, 'TRANSFER_OUT', -43, 3, NULL, N'林慧如 轉出給 張嘉豪', GETDATE()),
(1, 3, 'TRANSFER_IN', 43, 2, NULL, N'張嘉豪 收到來自 林慧如', GETDATE()),
(1, 2, 'TRANSFER_OUT', -13, 1, NULL, N'林慧如 轉出給 陳志明', GETDATE()),
(1, 1, 'TRANSFER_IN', 13, 2, NULL, N'陳志明 收到來自 林慧如', GETDATE()),
(1, 3, 'TRANSFER_OUT', -42, 4, NULL, N'張嘉豪 轉出給 黃怡君', GETDATE()),
(1, 4, 'TRANSFER_IN', 42, 3, NULL, N'黃怡君 收到來自 張嘉豪', GETDATE()),
(1, 1, 'TRANSFER_OUT', -41, 3, NULL, N'陳志明 轉出給 張嘉豪', GETDATE()),
(1, 3, 'TRANSFER_IN', 41, 1, NULL, N'張嘉豪 收到來自 陳志明', GETDATE()),
(1, 4, 'TRANSFER_OUT', -38, 2, NULL, N'黃怡君 轉出給 林慧如', GETDATE()),
(1, 2, 'TRANSFER_IN', 38, 4, NULL, N'林慧如 收到來自 黃怡君', GETDATE()),
(1, 2, 'TRANSFER_OUT', -59, 1, NULL, N'林慧如 轉出給 陳志明', GETDATE()),
(1, 1, 'TRANSFER_IN', 59, 2, NULL, N'陳志明 收到來自 林慧如', GETDATE()),
(1, 4, 'TRANSFER_OUT', -16, 1, NULL, N'黃怡君 轉出給 陳志明', GETDATE()),
(1, 1, 'TRANSFER_IN', 16, 4, NULL, N'陳志明 收到來自 黃怡君', GETDATE()),
(1, 3, 'TRANSFER_OUT', -32, 2, NULL, N'張嘉豪 轉出給 林慧如', GETDATE()),
(1, 2, 'TRANSFER_IN', 32, 3, NULL, N'林慧如 收到來自 張嘉豪', GETDATE()),
(1, 4, 'TRANSFER_OUT', -47, 2, NULL, N'黃怡君 轉出給 林慧如', GETDATE()),
(1, 2, 'TRANSFER_IN', 47, 4, NULL, N'林慧如 收到來自 黃怡君', GETDATE()),
(1, 3, 'TRANSFER_OUT', -44, 4, NULL, N'張嘉豪 轉出給 黃怡君', GETDATE()),
(1, 4, 'TRANSFER_IN', 44, 3, NULL, N'黃怡君 收到來自 張嘉豪', GETDATE());
------------------------------------------------------------------------------------javert結束

------------------------------------------------------------------------------------Sarah開始

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

------------------------------------------------------------------------------------Sarah結束

------------------------------------------------------------------------------------yu開始
go
-- FAQ 分類
INSERT INTO faq_category (faq_category_name, community_id) VALUES
(N'社區管理系統', 1),
(N'住戶服務', 1),
(N'公共設備', 1),
(N'社區設施', 1),
(N'安全與門禁', 1);
GO

-- FAQ 問答
INSERT INTO faq (faq_question, faq_answer, faq_category_id, faq_last_modified, faq_post_status, community_id) VALUES
(N'如何修改社區 App 密碼？', N'請至 App 的帳號設定中選擇「修改密碼」，輸入舊密碼與新密碼後確認即可。', 1, '2025-06-10 10:00:00', 1, 1),
(N'申請訪客車位需要提前多久？', N'建議至少提前一天透過 App 或管理室登記，以確保車位保留。', 2, '2025-06-11 10:00:00', 1, 1),
(N'地下室燈光太暗可以反映嗎？', N'可至反映專區提出建議，設施組將依實際情況調整照明。', 3, '2025-06-12 10:00:00', 1, 1),
(N'社區健身房使用時間為何？', N'每日早上 6 點至晚上 10 點開放，請攜帶住戶卡入場。', 4, '2025-06-13 10:00:00', 1, 1),
(N'出入門禁卡遺失怎麼辦？', N'請立即聯絡管理室掛失，並攜帶證件辦理補發手續。', 5, '2025-06-14 10:00:00', 1, 1);
GO

-- FAQ 關鍵字
INSERT INTO faq_keyword (faq_keyword, community_id) VALUES
(N'App', 1),
(N'車位申請', 1),
(N'照明問題', 1),
(N'健身房', 1),
(N'門禁卡', 1);
GO

-- FAQ 關鍵字連結
INSERT INTO faq_faq_keyword (faq_id, faq_keyword_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);
GO

-- Feedback 分類
INSERT INTO feedback_category (feedback_category_name, feedback_category_description, community_id) VALUES
(N'設備維修', N'設備損壞、異常或故障相關問題', 1),
(N'環境清潔', N'垃圾、異味、水漬等環境問題', 1),
(N'管理服務', N'管理員服務態度、效率等問題', 1),
(N'停車空間', N'車位劃分、違停、使用規範等', 1),
(N'其他建議', N'任何不屬於上述分類的反映事項', 1);
GO

-- Feedback 主表（含 user_id, handler_id）
INSERT INTO feedback (feedback_title, feedback_description, feedback_category_id, user_id, handler_id, feedback_status, feedback_last_updated_at, feedback_submit_at, feedback_resolved_at, feedback_user_rating, community_id) VALUES
(N'地下室感應燈壞了', N'晚間回家時整個地下停車場都很暗，感應燈沒有啟動。', 1, 1, 2, N'待處理', '2025-06-13 10:00:00', '2025-06-11 10:00:00', NULL, 3, 1),
(N'大廳地板有積水', N'最近下雨後大廳靠窗位置常出現積水，容易滑倒。', 2, 2, 3, N'待處理', '2025-06-12 10:00:00', '2025-06-11 10:00:00', NULL, 5, 1),
(N'管理員服務不佳', N'昨日下午詢問事情時，值班人員態度不耐煩，建議加強教育訓練。', 3, 3, 4, N'待處理', '2025-06-14 10:00:00', '2025-06-14 10:00:00', NULL, 4, 1),
(N'機車停車格不足', N'近期有許多新住戶，機車格不足導致常有亂停情形。', 4, 4, 1, N'待處理', '2025-06-15 10:00:00', '2025-06-12 10:00:00', NULL, 5, 1),
(N'希望加裝戶外遮雨棚', N'希望在管理室外空地設置遮雨棚，讓等待訪客或外送不會淋雨。', 5, 1, 2, N'待處理', '2025-06-15 10:00:00', '2025-06-13 10:00:00', NULL, 5, 1);
GO

-- 公告分類
INSERT INTO bulletin_category (bulletin_category_name, community_id) VALUES
(N'停水公告', 1),
(N'活動訊息', 1),
(N'維修通知', 1),
(N'社區公告', 1),
(N'設備更新', 1);
GO

-- 公告內容
INSERT INTO bulletin ( bulletin_title, bulletin_description, bulletin_category_id, user_id, bulletin_is_pinned, bulletin_post_time, bulletin_modify_time, bulletin_remove_time, bulletin_post_status, community_id) VALUES
( N'本週三社區將進行停水作業', N'由於管線檢修，將於 6/12(三) 上午9點至下午3點暫停供水，請提前儲水備用。', 1, 1, 0, '2025-06-11 10:00:00', '2025-06-12 10:00:00', '2025-06-13 10:00:00', 1, 1),
( N'中庭週末舉辦社區野餐活動', N'本週六下午4點起於中庭舉辦住戶野餐，歡迎全家參加，現場備有小點與飲品。', 2, 2, 1, '2025-06-13 10:00:00', '2025-06-14 10:00:00', '2025-06-16 10:00:00', 1, 1),
( N'B棟電梯維修通知', N'B棟南側電梯將於 6/15~6/17 進行維修保養，請改搭北側電梯，造成不便敬請見諒。', 3, 3, 0, '2025-06-12 10:00:00', '2025-06-14 10:00:00', '2025-06-17 10:00:00', 1, 1),
( N'新住戶報到須知', N'提醒近期搬入的新住戶，請至管理室登記並領取門禁卡及垃圾分類手冊。', 4, 4, 0, '2025-06-10 10:00:00', '2025-06-11 10:00:00', '2025-06-15 10:00:00', 1, 1),
( N'健身房將汰換新設備', N'下週一健身房將更換跑步機與啞鈴設備，期間暫停開放一天，敬請見諒。', 5, 1, 1, '2025-06-15 10:00:00', '2025-06-16 10:00:00', '2025-06-19 10:00:00', 1, 1);
GO

-- 公告留言（主留言）
INSERT INTO bulletin_comment (bulletin_id, bulletin_comment, user_id, bulletin_comment_time, bulletin_comment_is_alive, parent_comment_id) VALUES
(1, N'請問停水範圍包含A棟嗎？', 2, '2025-06-10 10:00:00', 1, NULL),
(2, N'太好了，小孩一定會喜歡這活動！', 3, '2025-06-11 10:00:00', 1, NULL),
(3, N'維修期間會提供臨時指引嗎？', 4, '2025-06-12 10:00:00', 1, NULL),
(4, N'剛入住一週，感謝提醒。', 1, '2025-06-13 10:00:00', 1, NULL),
(5, N'希望新設備能有說明書可參考。', 2, '2025-06-14 10:00:00', 1, NULL);
GO

-- 公告留言子留言
INSERT INTO bulletin_comment (bulletin_id, bulletin_comment, user_id, bulletin_comment_time, bulletin_comment_is_alive, parent_comment_id) VALUES
(1, N'A棟也會一起停水，建議當天備水喔。', 1, '2025-06-11 10:00:00', 1, 1),
(2, N'當天也會設有氣墊床，孩子應該很開心！', 4, '2025-06-12 10:00:00', 1, 2),
(3, N'會在大廳張貼告示指引，請留意。', 1, '2025-06-13 10:00:00', 1, 3),
(4, N'歡迎入住，有任何問題都可以聯絡管理室。', 3, '2025-06-14 10:00:00', 1, 4),
(5, N'會提供操作指導圖卡，感謝建議。', 4, '2025-06-15 10:00:00', 1, 5);
GO

-- 留言按讚
INSERT INTO bulletin_comment_like (bulletin_comment_id, user_id, bulletin_comment_like_is_valid) VALUES
(1, 3, 1),
(2, 1, 1),
(3, 2, 1),
(4, 3, 1),
(5, 4, 1);
GO

-- 公告附件
INSERT INTO bulletin_attachment (bulletin_id, bulletin_attachment_file_name, bulletin_attachment, bulletin_attachment_mime_type) VALUES
(1, N'停水區域示意圖.jpg', 0x1234, 'image/jpeg'),
(2, N'活動流程表.pdf', 0x1234, 'application/pdf'),
(3, N'電梯維修公告.png', 0x1234, 'image/png'),
(4, N'報到流程說明.jpg', 0x1234, 'image/jpeg'),
(5, N'設備更新清單.pdf', 0x1234, 'application/pdf');
GO

-- 投票主題（每篇公告對應一題）
INSERT INTO poll (poll_id, poll_title, poll_start, poll_end, poll_is_multiple) VALUES
(1, N'您是否備妥停水所需水量？', '2025-06-10 10:00:00', '2025-06-16 10:00:00', 0),
(2, N'您是否會參加社區野餐活動？', '2025-06-11 10:00:00', '2025-06-17 10:00:00', 0),
(3, N'您對電梯維修安排是否滿意？', '2025-06-12 10:00:00', '2025-06-18 10:00:00', 0),
(4, N'新住戶報到流程是否清楚？', '2025-06-11 10:00:00', '2025-06-16 10:00:00', 0),
(5, N'您最期待哪些健身設備更新？', '2025-06-13 10:00:00', '2025-06-19 10:00:00', 1);
GO

-- 投票選項
INSERT INTO poll_option (poll_id, poll_option_text) VALUES
(1, N'已準備完成'), (1, N'尚未準備'),
(2, N'會參加'), (2, N'不會參加'),
(3, N'滿意'), (3, N'不滿意'),
(4, N'非常清楚'), (4, N'有點不清楚'),
(5, N'跑步機'), (5, N'啞鈴'), (5, N'飛輪車');
GO

-- 投票紀錄
INSERT INTO poll_vote (poll_id, user_id, poll_option_id, poll_vote_is_checked) VALUES
(1, 1, 1, 1),
(2, 2, 3, 1),
(3, 3, 5, 1),
(4, 4, 7, 1),
(5, 1, 9, 1),
(5, 2, 10, 1);
GO

INSERT INTO feedback_reply (feedback_id, user_id, feedback_reply, feedback_reply_replied_at) VALUES
(1, 3, N'您好，社區會在本週安排水電檢查，請放心。', '2025-06-15 10:00:00'),
(2, 4, N'已通知清潔團隊，將加強公共區域整理。', '2025-06-16 10:00:00'),
(3, 1, N'我們已安排師傅前往檢修，請保持手機暢通。', '2025-06-17 10:00:00'),
(4, 2, N'此問題已提報委員會，將列入下次會議討論。', '2025-06-18 10:00:00'),
(5, 3, N'謝謝您的反映，將由客服專員持續追蹤進度。', '2025-06-19 10:00:00');
GO
------------------------------------------------------------------------------------yu結束

------------------------------------------------------------------------------------Julie 開始

INSERT INTO parking_type ([type], community_id)
VALUES
(N'汽車', 1),
(N'機車', 1),
(N'電動車', 1);
GO

INSERT INTO parking_slot (
    slot_number, [location], parking_type_id,
    users_id, license_plate, is_rentable, community_id
)
VALUES
-- 社區車位 (可承租)
(N'B1-001', N'B1 A區', 1, 31, NULL, 1, 1),
(N'B1-002', N'B1 A區', 1, 31, NULL, 1, 1),
(N'B1-003', N'B1 A區', 1, 31, NULL, 1, 1),
(N'B1-004', N'B1 A區', 1, 31, NULL, 1, 1),
(N'B1-005', N'B1 A區', 1, 31, NULL, 1, 1),
(N'B1-006', N'B1 A區', 1, 31, NULL, 1, 1),
(N'B1-007', N'B1 A區', 1, 31, NULL, 1, 1),
(N'B1-008', N'B1 A區', 1, 31, NULL, 1, 1),
(N'B1-009', N'B1 A區', 1, 31, NULL, 1, 1),
(N'B1-010', N'B1 A區', 1, 31, NULL, 1, 1),
(N'B1-011', N'B1 B區', 1, 31, NULL, 1, 1),
(N'B1-012', N'B1 B區', 1, 31, NULL, 1, 1),
(N'B1-013', N'B1 B區', 1, 31, NULL, 1, 1),
(N'B1-014', N'B1 B區', 1, 31, NULL, 1, 1),
(N'B1-015', N'B1 B區', 1, 31, NULL, 1, 1),
(N'B1-016', N'B1 B區', 1, 31, NULL, 1, 1),
(N'B1-017', N'B1 B區', 1, 31, NULL, 1, 1),
(N'B1-018', N'B1 B區', 1, 31, NULL, 1, 1),
(N'B1-019', N'B1 B區', 1, 31, NULL, 1, 1),
(N'B1-020', N'B1 B區', 1, 31, NULL, 1, 1),
(N'B1-021', N'B1 C區', 2, 31, NULL, 1, 1),
(N'B1-022', N'B1 C區', 2, 31, NULL, 1, 1),
(N'B1-023', N'B1 C區', 2, 31, NULL, 1, 1),
(N'B1-024', N'B1 C區', 2, 31, NULL, 1, 1),
(N'B1-025', N'B1 C區', 2, 31, NULL, 1, 1),
(N'B1-026', N'B1 C區', 2, 31, NULL, 1, 1),
(N'B1-027', N'B1 C區', 2, 31, NULL, 1, 1),
(N'B1-028', N'B1 C區', 2, 31, NULL, 1, 1),
(N'B1-029', N'B1 C區', 2, 31, NULL, 1, 1),
(N'B1-030', N'B1 C區', 2, 31, NULL, 1, 1),
(N'B1-031', N'B1 D區', 2, 31, NULL, 1, 1),
(N'B1-032', N'B1 D區', 2, 31, NULL, 1, 1),
(N'B1-033', N'B1 D區', 2, 31, NULL, 1, 1),
(N'B1-034', N'B1 D區', 2, 31, NULL, 1, 1),
(N'B1-035', N'B1 D區', 2, 31, NULL, 1, 1),
(N'B1-036', N'B1 D區', 2, 31, NULL, 1, 1),
(N'B1-037', N'B1 D區', 2, 31, NULL, 1, 1),
(N'B1-038', N'B1 D區', 2, 31, NULL, 1, 1),
(N'B1-039', N'B1 D區', 2, 31, NULL, 1, 1),
(N'B1-040', N'B1 D區', 2, 31, NULL, 1, 1),
(N'B1-041', N'B1 E區', 3, 31, NULL, 1, 1),
(N'B1-042', N'B1 E區', 3, 31, NULL, 1, 1),
(N'B1-043', N'B1 E區', 3, 31, NULL, 1, 1),
(N'B1-044', N'B1 E區', 3, 31, NULL, 1, 1),
(N'B1-045', N'B1 E區', 3, 31, NULL, 1, 1),
(N'B1-046', N'B1 E區', 3, 31, NULL, 1, 1),
(N'B1-047', N'B1 E區', 3, 31, NULL, 1, 1),
(N'B1-048', N'B1 E區', 3, 31, NULL, 1, 1),
(N'B1-049', N'B1 E區', 3, 31, NULL, 1, 1),
(N'B1-050', N'B1 E區', 3, 31, NULL, 1, 1),
(N'B1-051', N'B1 F區', 3, 31, NULL, 1, 1),
(N'B1-052', N'B1 F區', 3, 31, NULL, 1, 1),
(N'B1-053', N'B1 F區', 3, 31, NULL, 1, 1),
(N'B1-054', N'B1 F區', 3, 31, NULL, 1, 1),
(N'B1-055', N'B1 F區', 3, 31, NULL, 1, 1),
(N'B1-056', N'B1 F區', 3, 31, NULL, 1, 1),
(N'B1-057', N'B1 F區', 3, 31, NULL, 1, 1),
(N'B1-058', N'B1 F區', 3, 31, NULL, 1, 1),
(N'B1-059', N'B1 F區', 3, 31, NULL, 1, 1),
(N'B1-060', N'B1 F區', 3, 31, NULL, 1, 1),

-- 私人車位 (不可承租)
(N'B2-001', N'B2 A區', 1, 1, N'BWL-3951', 0, 1),
(N'B2-002', N'B2 A區', 1, 2, N'YMN-8042', 0, 1),
(N'B2-003', N'B2 A區', 1, 3, N'DQR-1397', 0, 1),
(N'B2-004', N'B2 A區', 1, 4, N'MVC-6174', 0, 1),
(N'B2-005', N'B2 A區', 1, 5, N'ZJP-2809', 0, 1),
(N'B2-006', N'B2 A區', 1, 6, N'GXL-4016', 0, 1),
(N'B2-007', N'B2 A區', 1, 7, N'NAH-7625', 0, 1),
(N'B2-008', N'B2 A區', 1, 8, N'CTU-9280', 0, 1),
(N'B2-009', N'B2 A區', 1, 9, N'RFD-6437', 0, 1),
(N'B2-010', N'B2 A區', 1, 10, N'KTF-7283', 0, 1),
(N'B2-011', N'B2 B區', 1, 31, NULL, 0, 1),
(N'B2-012', N'B2 B區', 1, 31, NULL, 0, 1),
(N'B2-013', N'B2 B區', 1, 31, NULL, 0, 1),
(N'B2-014', N'B2 B區', 1, 31, NULL, 0, 1),
(N'B2-015', N'B2 B區', 1, 31, NULL, 0, 1),
(N'B2-016', N'B2 B區', 1, 31, NULL, 0, 1),
(N'B2-017', N'B2 B區', 1, 31, NULL, 0, 1),
(N'B2-018', N'B2 B區', 1, 31, NULL, 0, 1),
(N'B2-019', N'B2 B區', 1, 31, NULL, 0, 1),
(N'B2-020', N'B2 B區', 1, 31, NULL, 0, 1),
(N'B2-021', N'B2 C區', 2, 1, N'AB-3241', 0, 1),
(N'B2-022', N'B2 C區', 2, 2, N'RT-2650', 0, 1),
(N'B2-023', N'B2 C區', 2, 3, N'HK-9873', 0, 1),
(N'B2-024', N'B2 C區', 2, 4, N'JG-7305', 0, 1),
(N'B2-025', N'B2 C區', 2, 5, N'MW-1052', 0, 1),
(N'B2-026', N'B2 C區', 2, 6, N'ZN-8936', 0, 1),
(N'B2-027', N'B2 C區', 2, 7, N'DE-1748', 0, 1),
(N'B2-028', N'B2 C區', 2, 8, N'XP-6620', 0, 1),
(N'B2-029', N'B2 C區', 2, 9, N'CY-5471', 0, 1),
(N'B2-030', N'B2 C區', 2, 10, N'LN-8039', 0, 1),
(N'B2-031', N'B2 D區', 2, 31, NULL, 0, 1),
(N'B2-032', N'B2 D區', 2, 31, NULL, 0, 1),
(N'B2-033', N'B2 D區', 2, 31, NULL, 0, 1),
(N'B2-034', N'B2 D區', 2, 31, NULL, 0, 1),
(N'B2-035', N'B2 D區', 2, 31, NULL, 0, 1),
(N'B2-036', N'B2 D區', 2, 31, NULL, 0, 1),
(N'B2-037', N'B2 D區', 2, 31, NULL, 0, 1),
(N'B2-038', N'B2 D區', 2, 31, NULL, 0, 1),
(N'B2-039', N'B2 D區', 2, 31, NULL, 0, 1),
(N'B2-040', N'B2 D區', 2, 31, NULL, 0, 1),
(N'B2-041', N'B2 E區', 3, 1, N'EVB-7123', 0, 1),
(N'B2-042', N'B2 E區', 3, 2, N'EAC-3950', 0, 1),
(N'B2-043', N'B2 E區', 3, 3, N'EEX-8471', 0, 1),
(N'B2-044', N'B2 E區', 3, 4, N'EBM-6205', 0, 1),
(N'B2-045', N'B2 E區', 3, 5, N'EPL-9037', 0, 1),
(N'B2-046', N'B2 E區', 3, 6, N'ENX-2184', 0, 1),
(N'B2-047', N'B2 E區', 3, 7, N'EFD-7632', 0, 1),
(N'B2-048', N'B2 E區', 3, 8, N'EEQ-4395', 0, 1),
(N'B2-049', N'B2 E區', 3, 9, N'EGS-1109', 0, 1),
(N'B2-050', N'B2 E區', 3, 10, N'ETH-5740', 0, 1),
(N'B2-051', N'B2 F區', 3, 31, NULL, 0, 1),
(N'B2-052', N'B2 F區', 3, 31, NULL, 0, 1),
(N'B2-053', N'B2 F區', 3, 31, NULL, 0, 1),
(N'B2-054', N'B2 F區', 3, 31, NULL, 0, 1),
(N'B2-055', N'B2 F區', 3, 31, NULL, 0, 1),
(N'B2-056', N'B2 F區', 3, 31, NULL, 0, 1),
(N'B2-057', N'B2 F區', 3, 31, NULL, 0, 1),
(N'B2-058', N'B2 F區', 3, 31, NULL, 0, 1),
(N'B2-059', N'B2 F區', 3, 31, NULL, 0, 1),
(N'B2-060', N'B2 F區', 3, 31, NULL, 0, 1),

-- 公共車位 (可承租 用於抽籤)
(N'B4-001', N'B4 A區', 1, 31, NULL, 1, 1),
(N'B4-002', N'B4 A區', 1, 31, NULL, 1, 1),
(N'B4-003', N'B4 A區', 1, 31, NULL, 1, 1),
(N'B4-004', N'B4 A區', 1, 31, NULL, 1, 1),
(N'B4-005', N'B4 A區', 1, 31, NULL, 1, 1),
(N'B4-006', N'B4 A區', 1, 31, NULL, 1, 1),
(N'B4-007', N'B4 A區', 1, 31, NULL, 1, 1),
(N'B4-008', N'B4 A區', 1, 31, NULL, 1, 1),
(N'B4-009', N'B4 A區', 1, 31, NULL, 1, 1),
(N'B4-010', N'B4 A區', 1, 31, NULL, 1, 1),
(N'B4-011', N'B4 B區', 1, 31, NULL, 1, 1),
(N'B4-012', N'B4 B區', 1, 31, NULL, 1, 1),
(N'B4-013', N'B4 B區', 1, 31, NULL, 1, 1),
(N'B4-014', N'B4 B區', 1, 31, NULL, 1, 1),
(N'B4-015', N'B4 B區', 1, 31, NULL, 1, 1),
(N'B4-016', N'B4 B區', 1, 31, NULL, 1, 1),
(N'B4-017', N'B4 B區', 1, 31, NULL, 1, 1),
(N'B4-018', N'B4 B區', 1, 31, NULL, 1, 1),
(N'B4-019', N'B4 B區', 1, 31, NULL, 1, 1),
(N'B4-020', N'B4 B區', 1, 31, NULL, 1, 1),
(N'B4-021', N'B4 C區', 2, 31, NULL, 1, 1),
(N'B4-022', N'B4 C區', 2, 31, NULL, 1, 1),
(N'B4-023', N'B4 C區', 2, 31, NULL, 1, 1),
(N'B4-024', N'B4 C區', 2, 31, NULL, 1, 1),
(N'B4-025', N'B4 C區', 2, 31, NULL, 1, 1),
(N'B4-026', N'B4 C區', 2, 31, NULL, 1, 1),
(N'B4-027', N'B4 C區', 2, 31, NULL, 1, 1),
(N'B4-028', N'B4 C區', 2, 31, NULL, 1, 1),
(N'B4-029', N'B4 C區', 2, 31, NULL, 1, 1),
(N'B4-030', N'B4 C區', 2, 31, NULL, 1, 1),
(N'B4-031', N'B4 D區', 2, 31, NULL, 1, 1),
(N'B4-032', N'B4 D區', 2, 31, NULL, 1, 1),
(N'B4-033', N'B4 D區', 2, 31, NULL, 1, 1),
(N'B4-034', N'B4 D區', 2, 31, NULL, 1, 1),
(N'B4-035', N'B4 D區', 2, 31, NULL, 1, 1),
(N'B4-036', N'B4 D區', 2, 31, NULL, 1, 1),
(N'B4-037', N'B4 D區', 2, 31, NULL, 1, 1),
(N'B4-038', N'B4 D區', 2, 31, NULL, 1, 1),
(N'B4-039', N'B4 D區', 2, 31, NULL, 1, 1),
(N'B4-040', N'B4 D區', 2, 31, NULL, 1, 1),
(N'B4-041', N'B4 E區', 3, 31, NULL, 1, 1),
(N'B4-042', N'B4 E區', 3, 31, NULL, 1, 1),
(N'B4-043', N'B4 E區', 3, 31, NULL, 1, 1),
(N'B4-044', N'B4 E區', 3, 31, NULL, 1, 1),
(N'B4-045', N'B4 E區', 3, 31, NULL, 1, 1),
(N'B4-046', N'B4 E區', 3, 31, NULL, 1, 1),
(N'B4-047', N'B4 E區', 3, 31, NULL, 1, 1),
(N'B4-048', N'B4 E區', 3, 31, NULL, 1, 1),
(N'B4-049', N'B4 E區', 3, 31, NULL, 1, 1),
(N'B4-050', N'B4 E區', 3, 31, NULL, 1, 1),
(N'B4-051', N'B4 F區', 3, 31, NULL, 1, 1),
(N'B4-052', N'B4 F區', 3, 31, NULL, 1, 1),
(N'B4-053', N'B4 F區', 3, 31, NULL, 1, 1),
(N'B4-054', N'B4 F區', 3, 31, NULL, 1, 1),
(N'B4-055', N'B4 F區', 3, 31, NULL, 1, 1),
(N'B4-056', N'B4 F區', 3, 31, NULL, 1, 1),
(N'B4-057', N'B4 F區', 3, 31, NULL, 1, 1),
(N'B4-058', N'B4 F區', 3, 31, NULL, 1, 1),
(N'B4-059', N'B4 F區', 3, 31, NULL, 1, 1),
(N'B4-060', N'B4 F區', 3, 31, NULL, 1, 1);
GO

INSERT INTO parking_rentals (
    approved, created_at, license_plate,
    rent_buy_start, rent_end, [status], updated_at,
    approved_id, community_id, parking_slot_id, users_id
)
VALUES
-- 已審核承租紀錄
(1, '2025-03-10 09:00:00', N'LFX-3289', '2025-07-01', '2025-07-31', 1, '2025-03-10 16:30:00', 31, 1, 11, 1),
(1, '2025-03-10 09:00:00', N'MWT-1947', '2025-07-01', '2025-07-31', 1, '2025-03-10 16:30:00', 31, 1, 12, 2),
(1, '2025-03-10 09:00:00', N'ZKD-7061', '2025-07-01', '2025-07-31', 1, '2025-03-10 16:30:00', 31, 1, 13, 3),

(1, '2025-03-10 09:00:00', N'UP-6384', '2025-06-01', '2025-06-30', 1, '2025-03-10 16:30:00', 31, 1, 31, 1),
(1, '2025-03-10 09:00:00', N'AY-2975', '2025-06-01', '2025-06-30', 1, '2025-03-10 16:30:00', 31, 1, 32, 2),
(1, '2025-03-10 09:00:00', N'HM-8046', '2025-06-01', '2025-06-30', 1, '2025-03-10 16:30:00', 31, 1, 33, 3),

(1, '2025-03-10 09:00:00', N'ECL-2810', '2025-05-01', '2025-05-31', 1, '2025-03-10 16:30:00', 31, 1, 51, 1),
(1, '2025-03-10 09:00:00', N'EBR-7036', '2025-05-01', '2025-05-31', 1, '2025-03-10 16:30:00', 31, 1, 52, 2),
(1, '2025-03-10 09:00:00', N'EVK-9254', '2025-05-01', '2025-05-31', 1, '2025-03-10 16:30:00', 31, 1, 53, 3),

-- 未審核承租紀錄
(0, '2025-03-10 09:00:00', N'NBD-6302', '2025-08-01', '2025-08-31', 0, '2025-03-10 16:30:00', NULL, 1, 16, 1),
(0, '2025-03-10 09:00:00', N'TXR-9175', '2025-08-01', '2025-08-31', 0, '2025-03-10 16:30:00', NULL, 1, 17, 2),
(0, '2025-03-10 09:00:00', N'ZKD-7061', '2025-08-01', '2025-08-31', 0, '2025-03-10 16:30:00', NULL, 1, 18, 3),

(0, '2025-03-10 09:00:00', N'UP-6384', '2025-09-01', '2025-09-30', 0, '2025-03-10 16:30:00', NULL, 1, 36, 4),
(0, '2025-03-10 09:00:00', N'AY-2975', '2025-09-01', '2025-09-30', 0, '2025-03-10 16:30:00', NULL, 1, 37, 5),
(0, '2025-03-10 09:00:00', N'HM-8046', '2025-09-01', '2025-09-30', 0, '2025-03-10 16:30:00', NULL, 1, 38, 6),

(0, '2025-03-10 09:00:00', N'ECL-2810', '2025-10-01', '2025-10-31', 0, '2025-03-10 16:30:00', NULL, 1, 56, 7),
(0, '2025-03-10 09:00:00', N'EBR-7036', '2025-10-01', '2025-10-31', 0, '2025-03-10 16:30:00', NULL, 1, 57, 8),
(0, '2025-03-10 09:00:00', N'EVK-9254', '2025-10-01', '2025-10-31', 0, '2025-03-10 16:30:00', NULL, 1, 58, 9),

-- 抽籤用承租紀錄
(0, '2025-04-01 16:30:00', N'', '2025-07-01', '2025-09-30', 0, '2025-06-01 16:30:00', NULL, 1, 141, 1),
(0, '2025-04-01 16:30:00', N'', '2025-07-01', '2025-09-30', 0, '2025-06-01 16:30:00', NULL, 1, 142, 2),
(0, '2025-04-01 16:30:00', N'', '2025-07-01', '2025-09-30', 0, '2025-06-01 16:30:00', NULL, 1, 143, 3),
(0, '2025-04-01 16:30:00', N'', '2025-07-01', '2025-09-30', 0, '2025-06-01 16:30:00', NULL, 1, 144, 4),
(0, '2025-04-01 16:30:00', N'', '2025-07-01', '2025-09-30', 0, '2025-06-01 16:30:00', NULL, 1, 145, 5),
(1, '2025-04-01 16:30:00', N'ABC-521', '2025-07-01', '2025-09-30', 1, '2025-06-01 16:30:00', 31, 1, 146, 6),
(1, '2025-04-01 16:30:00', N'236-FGK', '2025-07-01', '2025-09-30', 1, '2025-06-01 16:30:00', 31, 1, 147, 7),
(1, '2025-04-01 16:30:00', N'MNO-874', '2025-07-01', '2025-09-30', 1, '2025-06-01 16:30:00', 31, 1, 148, 8),
(1, '2025-04-01 16:30:00', N'589-KTL', '2025-07-01', '2025-09-30', 1, '2025-06-01 16:30:00', 31, 1, 149, 9),
(1, '2025-04-01 16:30:00', N'JIR-319', '2025-07-01', '2025-09-30', 1, '2025-06-01 16:30:00', 31, 1, 150, 10),
(1, '2025-04-01 16:30:00', N'274-CXA', '2025-07-01', '2025-09-30', 1, '2025-06-01 16:30:00', 31, 1, 151, 11),
(1, '2025-04-01 16:30:00', N'BTD-905', '2025-07-01', '2025-09-30', 1, '2025-06-01 16:30:00', 31, 1, 152, 12),
(1, '2025-04-01 16:30:00', N'31-ZKY', '2025-07-01', '2025-09-30', 1, '2025-06-01 16:30:00', 31, 1, 153, 13),
(1, '2025-04-01 16:30:00', N'PQE-762', '2025-07-01', '2025-09-30', 1, '2025-06-01 16:30:00', 31, 1, 154, 14),
(1, '2025-04-01 16:30:00', N'601-HVN', '2025-07-01', '2025-09-30', 1, '2025-06-01 16:30:00', 31, 1, 155, 15),

-- 抽籤預留承租紀錄 (未抽籤)
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 161, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 162, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 163, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 164, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 165, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 166, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 167, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 168, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 169, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 170, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 171, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 172, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 173, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 174, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 175, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 176, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 177, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 178, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 179, 31),
(1, '2025-05-01 16:30:00', N'抽籤車位', '2025-08-01', '2025-10-31', 0, '2025-05-01 16:30:00', 31, 1, 180, 31),

(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 121, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 122, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 123, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 124, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 125, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 126, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 127, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 128, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 129, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 130, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 131, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 132, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 133, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 134, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 135, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 136, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 137, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 138, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 139, 31),
(1, '2025-06-01 16:30:00', N'抽籤車位', '2024-09-01 ', '2024-11-30', 0, '2025-06-01 16:30:00', 31, 1, 140, 31),

(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 141, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 142, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 143, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 144, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 145, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 146, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 147, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 148, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 149, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 150, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 151, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 152, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 153, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 154, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 155, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 156, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 157, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 158, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 159, 31),
(1, '2025-07-01 16:30:00', N'抽籤車位', '2025-10-01', '2025-12-31', 0, '2025-07-01 16:30:00', 31, 1, 160, 31);
GO

-- 先新增公告分類
INSERT INTO [finalProj].[dbo].[bulletin_category] (
    bulletin_category_name,
    community_id
)
VALUES ( N'抽籤', 1);
GO

-- 這邊 IDENTITY 的 id 要跟車位抽籤的 id 對應
SET IDENTITY_INSERT bulletin ON;
INSERT INTO bulletin (
    bulletin_id, bulletin_description, bulletin_is_pinned, bulletin_modify_time,
    bulletin_post_status, bulletin_post_time, bulletin_remove_time,
    bulletin_title, bulletin_category_id, community_id, user_id
)
VALUES
-- 已發布
(101, N'請參考車位抽籤活動頁面：114年度7月機車位抽籤', 0, '2025-05-01 16:30:00', 1, '2025-05-01 16:30:00', '2025-05-31 16:30:00', N'114年度7月機車位抽籤', 6, 1, 31), -- 結束
(102, N'請參考車位抽籤活動頁面：114年度8月電動車位抽籤', 0, '2025-06-01 16:30:00', 1, '2025-06-01 16:30:00', '2025-06-30 16:30:00', N'114年度8月電動車位抽籤', 6, 1, 31), -- 未抽籤
(103, N'請參考車位抽籤活動頁面：114年度9月汽車位抽籤', 0, '2025-07-01 16:30:00', 1, '2025-07-01 16:30:00', '2025-07-31 16:30:00', N'114年度9月汽車位抽籤', 6, 1, 31), -- 進行中

-- 未發布
(104, N'請參考車位抽籤活動頁面：114年度10月機車位抽籤', 0, '2025-07-22 16:30:00', 0, '2025-08-01 16:30:00', '2025-08-31 16:30:00', N'114年度10月機車位抽籤', 6, 1, 31); -- 未開始

SET IDENTITY_INSERT bulletin OFF;
GO

INSERT INTO lottery_events (
    bulletin_id, created_at, started_at, ended_at, 
    [status], title, parking_type_id, users_id, rental_start, rental_end
)
VALUES
(101, '2025-04-01 16:30:00', '2025-05-01 16:30:00', '2025-05-31 16:30:00', 1, N'114年度7月機車位抽籤', 2, 31, '2025-07-01', '2025-09-30'),
(102, '2025-05-01 16:30:00', '2025-06-01 16:30:00', '2025-06-30 16:30:00', 0, N'114年度8月電動車位抽籤', 3, 31, '2025-08-01', '2025-10-31'),
(103, '2025-06-01 16:30:00', '2025-07-01 16:30:00', '2025-07-31 16:30:00', 0, N'114年度9月汽車位抽籤', 1, 31, '2024-09-01 ', '2024-11-30'),
(104, '2025-07-01 16:30:00', '2025-08-01 16:30:00', '2025-08-31 16:30:00', 0, N'114年度10月機車位抽籤', 2, 31, '2025-10-01', '2025-12-31');
GO

INSERT INTO lottery_event_spaces (
    lottery_events_id, parking_slot_id
)
VALUES
-- 101 機車 141-160
(101, 141),
(101, 142),
(101, 143),
(101, 144),
(101, 145),
(101, 146),
(101, 147),
(101, 148),
(101, 149),
(101, 150),
(101, 151),
(101, 152),
(101, 153),
(101, 154),
(101, 155),
(101, 156),
(101, 157),
(101, 158),
(101, 159),
(101, 160),
-- 102 電動車 161-180
(102, 161),
(102, 162),
(102, 163),
(102, 164),
(102, 165),
(102, 166),
(102, 167),
(102, 168),
(102, 169),
(102, 170),
(102, 171),
(102, 172),
(102, 173),
(102, 174),
(102, 175),
(102, 176),
(102, 177),
(102, 178),
(102, 179),
(102, 180),
-- 103 汽車 121-140
(103, 121),
(103, 122),
(103, 123),
(103, 124),
(103, 125),
(103, 126),
(103, 127),
(103, 128),
(103, 129),
(103, 130),
(103, 131),
(103, 132),
(103, 133),
(103, 134),
(103, 135),
(103, 136),
(103, 137),
(103, 138),
(103, 139),
(103, 140),
-- 104 機車 141-160
(104, 141),
(104, 142),
(104, 143),
(104, 144),
(104, 145),
(104, 146),
(104, 147),
(104, 148),
(104, 149),
(104, 150),
(104, 151),
(104, 152),
(104, 153),
(104, 154),
(104, 155),
(104, 156),
(104, 157),
(104, 158),
(104, 159),
(104, 160);
GO

INSERT INTO lottery_apply (
    applied_at, lottery_event_spaces_id,
    lottery_events_id, users_id
)
VALUES
('2025-05-15 16:30:00', 1, 101, 1),
('2025-05-15 16:30:00', 2, 101, 2),
('2025-05-15 16:30:00', 3, 101, 3),
('2025-05-15 16:30:00', 4, 101, 4),
('2025-05-15 16:30:00', 5, 101, 5),
('2025-05-15 16:30:00', 6, 101, 6),
('2025-05-15 16:30:00', 7, 101, 7),
('2025-05-15 16:30:00', 8, 101, 8),
('2025-05-15 16:30:00', 9, 101, 9),
('2025-05-15 16:30:00', 10, 101, 10),
('2025-05-15 16:30:00', 11, 101, 11),
('2025-05-15 16:30:00', 12, 101, 12),
('2025-05-15 16:30:00', 13, 101, 13),
('2025-05-15 16:30:00', 14, 101, 14),
('2025-05-15 16:30:00', 15, 101, 15),
('2025-05-15 16:30:00', 16, 101, 16),
('2025-05-15 16:30:00', 17, 101, 17),
('2025-05-15 16:30:00', 18, 101, 18),
('2025-05-15 16:30:00', 19, 101, 19),
('2025-05-15 16:30:00', 20, 101, 20),
('2025-05-15 16:30:00', NULL, 101, 21),
('2025-05-15 16:30:00', NULL, 101, 22),
('2025-05-15 16:30:00', NULL, 101, 23),
('2025-05-15 16:30:00', NULL, 101, 24),
('2025-05-15 16:30:00', NULL, 101, 25),
('2025-05-15 16:30:00', NULL, 101, 26),
('2025-05-15 16:30:00', NULL, 101, 27),
('2025-05-15 16:30:00', NULL, 101, 28),
('2025-05-15 16:30:00', NULL, 101, 29),
('2025-05-15 16:30:00', NULL, 101, 30),

('2025-06-15 16:30:00', NULL, 102, 1),
('2025-06-15 16:30:00', NULL, 102, 2),
('2025-06-15 16:30:00', NULL, 102, 3),
('2025-06-15 16:30:00', NULL, 102, 4),
('2025-06-15 16:30:00', NULL, 102, 5),
('2025-06-15 16:30:00', NULL, 102, 6),
('2025-06-15 16:30:00', NULL, 102, 7),
('2025-06-15 16:30:00', NULL, 102, 8),
('2025-06-15 16:30:00', NULL, 102, 9),
('2025-06-15 16:30:00', NULL, 102, 10),
('2025-06-15 16:30:00', NULL, 102, 11),
('2025-06-15 16:30:00', NULL, 102, 12),
('2025-06-15 16:30:00', NULL, 102, 13),
('2025-06-15 16:30:00', NULL, 102, 14),
('2025-06-15 16:30:00', NULL, 102, 15),
('2025-06-15 16:30:00', NULL, 102, 16),
('2025-06-15 16:30:00', NULL, 102, 17),
('2025-06-15 16:30:00', NULL, 102, 18),
('2025-06-15 16:30:00', NULL, 102, 19),
('2025-06-15 16:30:00', NULL, 102, 20),
('2025-06-15 16:30:00', NULL, 102, 21),
('2025-06-15 16:30:00', NULL, 102, 22),
('2025-06-15 16:30:00', NULL, 102, 23),
('2025-06-15 16:30:00', NULL, 102, 24),
('2025-06-15 16:30:00', NULL, 102, 25),
('2025-06-15 16:30:00', NULL, 102, 26),
('2025-06-15 16:30:00', NULL, 102, 27),
('2025-06-15 16:30:00', NULL, 102, 28),
('2025-06-15 16:30:00', NULL, 102, 29),
('2025-06-15 16:30:00', NULL, 102, 30),

('2025-07-15 16:30:00', NULL, 103, 11),
('2025-07-15 16:30:00', NULL, 103, 12),
('2025-07-15 16:30:00', NULL, 103, 13),
('2025-07-15 16:30:00', NULL, 103, 14),
('2025-07-15 16:30:00', NULL, 103, 15),
('2025-07-15 16:30:00', NULL, 103, 16),
('2025-07-15 16:30:00', NULL, 103, 17),
('2025-07-15 16:30:00', NULL, 103, 18),
('2025-07-15 16:30:00', NULL, 103, 19),
('2025-07-15 16:30:00', NULL, 103, 20);
GO

------------------------------------------------------------------------------------ Julie結束