-- create database finalProj
use finalProj
 
SELECT * FROM users
INSERT INTO [dbo].[users] ([name],last_alter_at) VALUES ('A',getDate()), ('B',getDate()), ('C',getDate());

-- parking_type 車位種類
CREATE TABLE parking_type (
    id INT IDENTITY(1,1) PRIMARY KEY,
    community_id INT,
    [type] NVARCHAR(10) NOT NULL,
--  FOREIGN KEY (community_id) REFERENCES community(id)
);
INSERT INTO parking_type ([type]) VALUES (N'汽車'), (N'機車'), (N'電動車');

-- parking_slot 車位資料
CREATE TABLE parking_slot (
    id INT IDENTITY(1,1) PRIMARY KEY,
    community_id INT,
    slot_number VARCHAR(10) NOT NULL,
    [location] VARCHAR(20),
    parking_type_id INT NOT NULL,
    users_id INT NOT NULL DEFAULT 1,
    license_plate VARCHAR(10),
    is_rentable BIT NOT NULL DEFAULT 0,
--  FOREIGN KEY (community_id) REFERENCES community(id),
--  FOREIGN KEY (parking_type_id) REFERENCES parking_type(id),
--  FOREIGN KEY (users_id) REFERENCES Users(id)
);
INSERT INTO parking_slot ([community_id],slot_number, [location], parking_type_id, users_id, license_plate, is_rentable)
VALUES 
(1,'B1-001', 'B1 A區', 1, 1, NULL, 1),
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
(1, 'B2-M01', N'B2層機車區', 2, 1, NULL, 1),
(1, 'B2-01', N'B2層A排', 1, 2, 'KFC-43', 0);

-- parking_rentals 承租紀錄
CREATE TABLE parking_rentals (
    id INT IDENTITY(1,1) PRIMARY KEY,
	community_id INT,
    users_id INT NOT NULL,
    parking_slot_id INT NOT NULL,
    rent_buy_start DATETIME NOT NULL,
    rent_end DATETIME NOT NULL,
    license_plate VARCHAR(10) NOT NULL,
    [status] BIT NOT NULL DEFAULT 0,
	approved BIT NOT NULL DEFAULT 0,
	approved_id INT,
	updated_at DATETIME,
	created_at DATETIME,
--  FOREIGN KEY (users_id) REFERENCES users(id),
--  FOREIGN KEY (parking_slot_id) REFERENCES parking_slot(slot_number)
);
INSERT INTO parking_rentals (
    community_id, users_id, parking_slot_id, rent_buy_start, rent_end,
    license_plate, status, approved, approved_id, updated_at, created_at
) VALUES
(1, 201, 301, '2025-07-01 08:00:00', '2025-08-01 08:00:00', 'ABC-1234', 0, 0, NULL, GETDATE(), GETDATE()),
(1, 202, 302, '2025-07-02 09:00:00', '2025-08-02 09:00:00', 'DEF-5678', 1, 0, NULL, GETDATE(), GETDATE()),
(2, 203, 303, '2025-07-03 10:00:00', '2025-08-03 10:00:00', 'GHI-9012', 1, 0, NULL, GETDATE(), GETDATE()),
(2, 204, 304, '2025-07-04 11:00:00', '2025-08-04 11:00:00', 'JKL-3456', 0, 0, NULL, GETDATE(), GETDATE()),
(3, 205, 305, '2025-07-05 12:00:00', '2025-08-05 12:00:00', 'MNO-7890', 1, 0, NULL, GETDATE(), GETDATE()),
(1, 206, 306, '2025-06-01 08:00:00', '2025-07-01 08:00:00', 'PQR-1111', 1, 1, 101, GETDATE(), GETDATE()),
(2, 207, 307, '2025-06-02 09:00:00', '2025-07-02 09:00:00', 'STU-2222', 1, 1, 102, GETDATE(), GETDATE()),
(2, 208, 308, '2025-06-03 10:00:00', '2025-07-03 10:00:00', 'VWX-3333', 0, 1, 102, GETDATE(), GETDATE()),
(3, 209, 309, '2025-06-04 11:00:00', '2025-07-04 11:00:00', 'YZA-4444', 1, 1, 103, GETDATE(), GETDATE()),
(3, 210, 310, '2025-06-05 12:00:00', '2025-07-05 12:00:00', 'BCD-5555', 0, 1, 101, GETDATE(), GETDATE());


-- temporary_parking_applications 臨停申請紀錄
CREATE TABLE temporary_parking_applications (
    id INT IDENTITY(1,1) PRIMARY KEY,
    units_id INT NOT NULL,
    visitor_name NVARCHAR(10) NOT NULL,
    license_plate VARCHAR(10) NOT NULL,
    parking_type_id INT NOT NULL,
    request_time DATETIME DEFAULT GETDATE(),
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    purpose NVARCHAR(50) NOT NULL,
    parking_slot_id INT NOT NULL,
--  FOREIGN KEY (units_id) REFERENCES units(id),
--  FOREIGN KEY (parking_type_id) REFERENCES parking_type(id),
--  FOREIGN KEY (parking_slot_id) REFERENCES parking_slot(id)
);
INSERT INTO temporary_parking_applications 
(units_id, visitor_name, license_plate, parking_type_id, start_time, end_time, purpose, parking_slot_id)
VALUES 
(1, N'Julia', 'KFC-43', 1, '2025-04-28 12:00:00', '2025-04-28 16:00:00', N'維修', 2);

-- lottery_events 抽籤活動
CREATE TABLE lottery_events (
    id INT PRIMARY KEY,
    title NVARCHAR(50) NOT NULL,
    started_at DATETIME NOT NULL DEFAULT GETDATE(),
    ended_at DATETIME NOT NULL,
	users_id INT NOT NULL,
	created_at DATETIME NOT NULL,
--  FOREIGN KEY (id) REFERENCES bulletin(id)
);
INSERT INTO [dbo].[lottery_events] (id, title, started_at, ended_at, users_id, created_at)
VALUES 
(1, N'114年度機車位抽籤','2025-06-15','2025-06-15', 1, getDate()),
(2, N'115年度機車位抽籤','2025-06-15','2025-06-15',1, getDate());

-- lottery_event_spaces 抽籤車位表
CREATE TABLE lottery_event_spaces (
    id INT IDENTITY(1,1) PRIMARY KEY,
    lottery_events_id INT NOT NULL,
    parking_slot_id INT NOT NULL,
--  FOREIGN KEY (lottery_events_id) REFERENCES lottery_events(id),
--  FOREIGN KEY (parking_slot_id) REFERENCES parking_slot(id)
);
INSERT INTO lottery_event_spaces (lottery_events_id, parking_slot_id)
VALUES 
(1, 1),
(2, 2);

-- lottery_apply 抽籤申請表
CREATE TABLE lottery_apply (
    id INT IDENTITY(1,1) PRIMARY KEY,
    users_id INT NOT NULL,
    lottery_events_id INT NOT NULL,
    applied_at DATETIME DEFAULT GETDATE(),
    lottery_event_spaces_id INT,
--  FOREIGN KEY (users_id) REFERENCES users(id),
--  FOREIGN KEY (lottery_events_id) REFERENCES lottery_events(id),
--  FOREIGN KEY (lottery_event_spaces_id) REFERENCES lottery_event_spaces(id)
);
ALTER TABLE lottery_apply
ADD CONSTRAINT uq_lottery_applicant UNIQUE (lottery_events_id, users_id);

INSERT INTO lottery_apply (users_id, lottery_events_id, lottery_event_spaces_id)
VALUES (2, 1, 1), (3, 2, NULL);
