DROP TABLE if exists finance_FeeType
DROP TABLE if exists finance_BillingPeriod
DROP TABLE if exists finance_Invoice
DROP TABLE if exists finance_Invoice_Response
DROP TABLE if exists finance_Expense
DROP TABLE if exists finance_Receipt


-- 費用類別 table
IF OBJECT_ID('finance_FeeType', 'U') IS NULL
BEGIN
    CREATE TABLE finance_FeeType (
        fee_type_id      INT IDENTITY(1,1) NOT NULL PRIMARY KEY,      -- 主鍵
        description      VARCHAR(50) NOT NULL,                        -- 費用類別名稱
        fee_code         VARCHAR(20) NOT NULL UNIQUE,                 -- 費用代碼
        amountPerUnit    DECIMAL(8,2),                                -- 每單位費用
        frequency        VARCHAR(20),                                 -- 費用頻率（例如：1、6、12）
        unit             VARCHAR(20),                                 -- 計費單位（例如：坪數、戶數）

        -- BaseEntity 共用欄位
        created_at       DATETIME NOT NULL DEFAULT SYSDATETIME(),
        last_updated     DATETIME NOT NULL DEFAULT SYSDATETIME(),
        created_by       INT,
        updated_by       INT,
        status           BIT NOT NULL DEFAULT 1,
        note             VARCHAR(MAX),
        community_id     INT

    );
END

-- 期別表 table
IF OBJECT_ID('finance_BillingPeriod', 'U') IS NULL
BEGIN
CREATE TABLE finance_BillingPeriod (
    billing_period_id INT IDENTITY(1,1) PRIMARY KEY,               -- 繳交期別流水號
    period_name       VARCHAR(50) NOT NULL UNIQUE,                -- 期別名稱
    period_code       VARCHAR(20) NOT NULL UNIQUE,                -- 期別代碼
    start_date        DATE NOT NULL,                              -- 期別開始日
    end_date          DATE NOT NULL,                              -- 期別結束日
    due_date          DATETIME,                                   -- 預設繳費截止日

    -- BaseEntity 共用欄位
    created_at       DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    last_updated     DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    created_by       INT,
    updated_by       INT,
    status           BIT        NOT NULL DEFAULT 1,
    note             VARCHAR(MAX),
    fee_type_id      INT NOT NULL,
    community_id     INT,

    FOREIGN KEY (fee_type_id) REFERENCES finance_FeeType(fee_type_id)
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END


-- 應收帳款（請款單）table
IF OBJECT_ID('finance_Invoice', 'U') IS NULL
BEGIN
CREATE TABLE finance_Invoice (
    invoice_id         INT IDENTITY(1,1) PRIMARY KEY,             -- 請款單主鍵
    amount_due         DECIMAL(10,2) NOT NULL,                   -- 應收金額
    period_name        VARCHAR(20),                              -- 期別名稱（文字）
    deadline           DATETIME,                                 -- 繳費截止日
    payment_plan       VARCHAR(20),                              -- 繳費計畫（如 monthly）
    
    -- 👉 單位數（如坪數、戶數、車位數）
    unit_count         DECIMAL(8,2),                            

    -- 👉 每單位費用（複製自 FeeType.amountPerUnit 以紀錄當時價格）
    unit_price         DECIMAL(8,2),                            

    -- 👉 總金額
    total_amount       DECIMAL(10,2),                            

    paymentStatus      VARCHAR(20),                              -- 付款狀態：UNPAID, PENDING, PAID

    -- BaseEntity 共用欄位
    created_at         DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    last_updated       DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    created_by         INT,
    updated_by         INT,
    status             BIT        NOT NULL DEFAULT 1,
    note               VARCHAR(MAX),
    community_id       INT,

    -- 關聯欄位
    users_id           INT NOT NULL,
    fee_type_id        INT NOT NULL,
    billing_period_id  INT NOT NULL,

    FOREIGN KEY (users_id) REFERENCES users(users_id),
    FOREIGN KEY (fee_type_id) REFERENCES finance_FeeType(fee_type_id),
    FOREIGN KEY (billing_period_id) REFERENCES finance_BillingPeriod(billing_period_id)
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END


-- 請款單回覆 table
IF OBJECT_ID('finance_Invoice_Response', 'U') IS NULL
BEGIN
CREATE TABLE finance_Invoice_Response (
    invoice_response_id INT IDENTITY(1,1) PRIMARY KEY,     -- 回覆主鍵
    last_response_time  DATETIME,                          -- 回覆時間
    account_code        VARCHAR(10),                       -- 匯款帳號後五碼
    last_response       VARCHAR(MAX),                      -- 回覆內容
    verified            BIT,                               -- 是否已審核
    verified_time       DATETIME,                          -- 審核時間
    verified_by         INT,                               -- 審核人員（userId）

    users_id            INT NOT NULL,                      -- 回覆人（用戶）
    invoice_id          INT NOT NULL,                      -- 對應發票 ID

    -- 關聯欄位
    FOREIGN KEY (users_id) REFERENCES users(users_id),
    FOREIGN KEY (invoice_id) REFERENCES finance_Invoice(invoice_id)
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END


-- 財務支出 table
IF OBJECT_ID('finance_Expense', 'U') IS NULL
BEGIN
CREATE TABLE Expense (
    expense_id     INT IDENTITY(1,1) PRIMARY KEY,
    category       VARCHAR(20),
    amount         INT NOT NULL,
    paid_on        DATETIME,
    paid_by        VARCHAR(20),
    receipt        VARBINARY(MAX),
    vendor_id      INT,
    -- BaseEntity 共用欄位
    created_at       DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    last_updated     DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    created_by       INT,
    updated_by       INT,
    status           BIT        NOT NULL DEFAULT 1,
    note             VARCHAR(MAX),
    community_id     INT
    --FOREIGN KEY (vendor_id) REFERENCES Vendor(vendor_id)
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END

-- 實收明細 table
IF OBJECT_ID('finance_Receipt', 'U') IS NULL
BEGIN
CREATE TABLE finance_Receipt (
    receipt_id        INT IDENTITY(1,1) PRIMARY KEY,
    receipt_num       VARCHAR(50),             -- 收據編號
    payment_method    VARCHAR(20),             -- 付款方式
    paid_at           DATETIME,                -- 實際付款時間
    debit_at          DATETIME,                -- 入帳時間
    amount_pay        DECIMAL(10,2),           -- 實付金額
    installments      VARCHAR(10),             -- 分期資訊

    -- BaseEntity 共用欄位
    created_at        DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    last_updated      DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    created_by        INT,
    updated_by        INT,
    status            BIT        NOT NULL DEFAULT 1,
    note              VARCHAR(MAX),
    invoice_id        INT NOT NULL,            -- 對應發票
    users_id          INT NOT NULL,            -- 使用者 ID
    community_id      INT,

    FOREIGN KEY (invoice_id) REFERENCES finance_Invoice(invoice_id),
    FOREIGN KEY (users_id) REFERENCES users(users_id)
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END

