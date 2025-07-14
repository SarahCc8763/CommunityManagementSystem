

------------------------------------------------------------------------------------javert開始
--TRUNCATE TABLE facilities;
INSERT INTO facilities (
    community_id, facility_name, max_users, facility_description, facility_location,
    open_time, close_time, reservable_duration, fee, facility_status, created_at
) VALUES
(1, N'交誼廳', NULL, N'可供住戶聚會、會議或活動使用', N'A棟1樓', '09:00', '21:00', 120, 15, 'active', GETDATE()),
(1, N'健身房', 10, N'配有跑步機、啞鈴、飛輪等設備', N'B棟地下1樓', '06:00', '22:00', 120, 10, 'active', GETDATE()),
(1, N'游泳池', 30, N'僅限住戶使用，請著泳裝並遵守規定', N'戶外區', '09:00', '21:00', 120, 10, 'active', GETDATE()),
(1, N'桌球室B101', NULL, N'提供標準雙人桌球場地，限時使用', N'B1-B101', '09:00', '21:00', 120, 10, 'active', GETDATE()),
(1, N'桌球室B102', NULL, N'提供標準雙人桌球場地，限時使用', N'B1-B102', '09:00', '21:00', 120, 10, 'active', GETDATE()),
(1, N'撞球室', NULL, N'提供撞球桌與器材，需預約使用', N'B2撞球室', '09:00', '21:00', 120, 120, 'active', GETDATE()),
(1, N'停車格A1', NULL, N'地下停車場 A1 車位，可短時預約停車', N'地下1樓-A1', '00:00', '23:59', 720, 5, 'active', GETDATE()),
(1, N'停車格A2', NULL, N'地下停車場 A2 車位，可短時預約停車', N'地下1樓-A2', '00:00', '23:59', 720, 5, 'active', GETDATE());


--TRUNCATE TABLE facility_images;
INSERT INTO facility_images (facility_id, image_url, image_description, sort_order)
VALUES 
(1, '/images/facilities/lounge.png', N'交誼廳外觀', 0),
(2, '/images/facilities/gym.png', N'健身房設備照', 0),
(3, '/images/facilities/pool.png', N'游泳池環境', 0),
(4, '/images/facilities/tabletennis1.png', N'桌球室B101外觀', 0),
(4, '/images/facilities/tabletennis2.png', N'桌球室B102外觀', 1),
(5, '/images/facilities/tabletennis3.png', N'桌球室B102外觀', 0),
(6, '/images/facilities/snooker1.png', N'撞球室環境', 0),
(6, '/images/facilities/snooker2.png', N'撞球室環境', 1),
(6, '/images/facilities/snooker3.png', N'撞球室環境', 2),
(7, '/images/facilities/parking_grid.png', N'地下停車場 A1 車位', 0),
(8, '/images/facilities/parking_grid.png', N'地下停車場 A2 車位', 0);


-- 宣告月底時間（避免每筆都重算）
DECLARE @endOfMonth DATETIME = DATEADD(
    SECOND, -1,
    DATEADD(MONTH, 1, CAST(DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1) AS DATETIME))
);
--TRUNCATE TABLE facility_reservations;
INSERT INTO facility_reservations (
    community_id, unit_id, facility_id, number_of_users, reservation_start_time, reservation_end_time,
    is_admin, required_points, actual_used_points, point_expire_at, remark, reservation_status, created_at)
VALUES
(1, 1, 1, 5, '2025-06-25 10:00', '2025-06-25 11:00', 0, 15, 15, @endOfMonth, N'首次預約交誼廳', 'APPROVED', GETDATE()),
(1, 2, 2, 2, '2025-06-25 08:00', '2025-06-25 09:00', 0, 10, 10, @endOfMonth, N'早晨健身', 'APPROVED', GETDATE()),
(1, 3, 3, 1, '2025-06-25 17:00', '2025-06-25 18:00', 0, 10, 10, @endOfMonth, N'傍晚游泳', 'APPROVED', GETDATE()),
(1, 1, 4, 2, '2025-06-26 14:00', '2025-06-26 15:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 2, 5, 2, '2025-06-26 15:00', '2025-06-26 16:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 3, 6, 4, '2025-06-27 19:00', '2025-06-27 20:00', 0, 20, 20, @endOfMonth, N'週五撞球', 'APPROVED', GETDATE()),
(1, 1, 2, 1, '2025-06-28 07:00', '2025-06-28 08:00', 0, 10, 10, @endOfMonth, N'健身重訓', 'APPROVED', GETDATE()),
(1, 2, 1, 6, '2025-06-28 18:00', '2025-06-28 19:00', 0, 15, 15, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 3, 3, 2, '2025-06-29 12:00', '2025-06-29 13:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 3, 4, 2, '2025-06-29 20:00', '2025-06-29 21:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 4, 1, 3, '2025-06-30 09:00', '2025-06-30 10:00', 0, 15, 15, @endOfMonth, N'交誼廳早會', 'APPROVED', GETDATE()),
(1, 5, 2, 2, '2025-07-01 07:00', '2025-07-01 08:00', 0, 10, 10, @endOfMonth, N'健身有氧', 'APPROVED', GETDATE()),
(1, 6, 3, 1, '2025-07-01 18:00', '2025-07-01 19:00', 0, 10, 10, @endOfMonth, N'游泳課', 'APPROVED', GETDATE()),
(1, 7, 4, 2, '2025-07-02 15:00', '2025-07-02 16:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 8, 5, 3, '2025-07-02 20:00', '2025-07-02 21:00', 0, 10, 10, @endOfMonth, N'桌球比賽', 'APPROVED', GETDATE()),
(1, 1, 6, 2, '2025-07-03 19:00', '2025-07-03 20:00', 0, 20, 20, @endOfMonth, N'撞球練習', 'APPROVED', GETDATE()),
(1, 2, 1, 4, '2025-07-04 10:00', '2025-07-04 11:00', 0, 15, 15, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 3, 2, 1, '2025-07-04 08:00', '2025-07-04 09:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 4, 3, 2, '2025-07-05 17:00', '2025-07-05 18:00', 0, 10, 10, @endOfMonth, N'假日游泳', 'APPROVED', GETDATE()),
(1, 5, 4, 3, '2025-07-05 13:00', '2025-07-05 14:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 6, 5, 1, '2025-07-06 14:00', '2025-07-06 15:00', 0, 10, 10, @endOfMonth, N'休閒桌球', 'APPROVED', GETDATE()),
(1, 7, 6, 2, '2025-07-06 19:00', '2025-07-06 20:00', 0, 20, 20, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 8, 1, 5, '2025-07-07 10:00', '2025-07-07 11:00', 0, 15, 15, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 1, 2, 2, '2025-07-07 07:00', '2025-07-07 08:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 2, 3, 3, '2025-07-08 17:00', '2025-07-08 18:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 3, 4, 2, '2025-07-08 15:00', '2025-07-08 16:00', 0, 10, 10, @endOfMonth, N'打電動', 'APPROVED', GETDATE()),
(1, 4, 5, 1, '2025-07-09 16:00', '2025-07-09 17:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 5, 6, 4, '2025-07-09 19:00', '2025-07-09 20:00', 0, 20, 20, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 6, 2, 3, '2025-07-10 08:00', '2025-07-10 09:00', 0, 10, 10, @endOfMonth, N'晨間健身', 'APPROVED', GETDATE()),
(1, 7, 3, 2, '2025-08-02 17:00', '2025-08-02 18:00', 0, 10, 10, @endOfMonth, N'八月游泳開跑', 'APPROVED', GETDATE());




--TRUNCATE TABLE point_accounts;
-- 宣告月底時間（避免每筆都重算）

DECLARE @endOfMonth DATETIME = DATEADD(
    SECOND, -1,
    DATEADD(MONTH, 1, CAST(DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1) AS DATETIME))
);

-- 插入資料
INSERT INTO point_accounts (
    community_id, unit_id, total_balance, expired_at, updated_at
) VALUES
(1, 1, 100, @endOfMonth, GETDATE()),   -- 張三：剛領 100 系統點
(1, 2, 150, @endOfMonth, GETDATE()),   -- 李四：有儲值 50 點
(1, 3, 80, @endOfMonth, GETDATE()),    -- 王五：系統點已用部分
(1, 4, 200, @endOfMonth, GETDATE()),   -- 陳六：用戶很活躍
(1, 5, 0, @endOfMonth, GETDATE()),     -- 林七：尚未領點
(1, 6, 120, @endOfMonth, GETDATE()),   -- 馬八：剛領完點尚未使用
(1, 7, 60, @endOfMonth, GETDATE()),    -- 趙九：點數所剩不多
(1, 8, 180, @endOfMonth, GETDATE());   -- 孫十：儲值大戶


--TRUNCATE TABLE point_transactions;
INSERT INTO point_transactions (
    community_id, unit_id, transaction_type, amount,
    related_unit_id, related_reservation_id, transaction_description, created_at
) VALUES
(1, 1, 'RESERVATION', -15, NULL, 1, N'預約交誼廳扣點', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 2, N'預約健身房扣點', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 3, N'預約游泳池扣點', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 4, N'預約電玩室扣點', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 5, N'預約桌球室扣點', GETDATE()),
(1, 3, 'RESERVATION', -20, NULL, 6, N'預約撞球室扣點', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 7, N'早晨健身房', GETDATE()),
(1, 2, 'RESERVATION', -15, NULL, 8, N'交誼廳活動', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 9, N'游泳', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 10, N'桌球', GETDATE()),
(1, 4, 'RESERVATION', -15, NULL, 11, N'交誼廳早會', GETDATE()),
(1, 5, 'RESERVATION', -10, NULL, 12, N'健身有氧', GETDATE()),
(1, 6, 'RESERVATION', -10, NULL, 13, N'游泳課', GETDATE()),
(1, 7, 'RESERVATION', -10, NULL, 14, N'電玩時間', GETDATE()),
(1, 8, 'RESERVATION', -10, NULL, 15, N'桌球對戰', GETDATE()),
(1, 1, 'RESERVATION', -20, NULL, 16, N'撞球練習', GETDATE()),
(1, 2, 'RESERVATION', -15, NULL, 17, N'交誼廳聊天', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 18, N'健身早晨', GETDATE()),
(1, 4, 'RESERVATION', -10, NULL, 19, N'游泳週末', GETDATE()),
(1, 5, 'RESERVATION', -10, NULL, 20, N'電玩', GETDATE()),
(1, 6, 'RESERVATION', -10, NULL, 21, N'桌球練習', GETDATE()),
(1, 7, 'RESERVATION', -20, NULL, 22, N'撞球日常', GETDATE()),
(1, 8, 'RESERVATION', -15, NULL, 23, N'交誼聚會', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 24, N'晨間健身', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 25, N'游泳補習', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 26, N'電玩活動', GETDATE()),
(1, 4, 'RESERVATION', -10, NULL, 27, N'桌球爭霸', GETDATE()),
(1, 5, 'RESERVATION', -20, NULL, 28, N'撞球邀請賽', GETDATE()),
(1, 6, 'RESERVATION', -10, NULL, 29, N'健身特訓', GETDATE()),
(1, 7, 'RESERVATION', -10, NULL, 30, N'八月游泳開跑', GETDATE()),
(1, 2, 'CANCEL', +10, NULL, 2, N'取消健身房退還點數', GETDATE()),
(1, 1, 'TOPUP', +50, NULL, NULL, N'管理室加值 50 點', GETDATE()),
(1, 5, 'TOPUP', +100, NULL, NULL, N'系統儲值', GETDATE()),
(1, 4, 'TRANSFER_OUT', -30, 1, NULL, N'轉給張三', GETDATE()),
(1, 1, 'TRANSFER_IN', +30, 4, NULL, N'陳六轉點', GETDATE());
------------------------------------------------------------------------------------javert結束