-- create database finalProj
use finalProj


-- parking_type 車位種類
CREATE TABLE parking_type (
    id INT IDENTITY(1,1) PRIMARY KEY,
    community_id INT,
    [type] NVARCHAR(10) NOT NULL,
    FOREIGN KEY (community_id) REFERENCES community(id)
);


-- parking_slot 車位資料
CREATE TABLE parking_slot (
    id INT IDENTITY(1,1) PRIMARY KEY,
    community_id INT,
    parking_type_id INT NOT NULL,
    users_id INT NOT NULL DEFAULT 1,
    slot_number VARCHAR(10) NOT NULL,
    [location] VARCHAR(20),
    license_plate VARCHAR(10),
    is_rentable BIT NOT NULL DEFAULT 0,
    FOREIGN KEY (community_id) REFERENCES community(id),
    FOREIGN KEY (parking_type_id) REFERENCES parking_type(id),
    FOREIGN KEY (users_id) REFERENCES Users([users_id])
);


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
    FOREIGN KEY (users_id) REFERENCES users(id),
    FOREIGN KEY (parking_slot_id) REFERENCES parking_slot(slot_number)
);


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


-- lottery_event_spaces 抽籤車位表
CREATE TABLE lottery_event_spaces (
    id INT IDENTITY(1,1) PRIMARY KEY,
    lottery_events_id INT NOT NULL,
    parking_slot_id INT NOT NULL,
--  FOREIGN KEY (lottery_events_id) REFERENCES lottery_events(id),
--  FOREIGN KEY (parking_slot_id) REFERENCES parking_slot(id)
);


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
