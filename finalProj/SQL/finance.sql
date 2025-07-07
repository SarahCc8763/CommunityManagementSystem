

-- 費用類別 table
IF OBJECT_ID('finance_FeeType', 'U') IS NULL
BEGIN
CREATE TABLE FeeType (
    fee_type_id      INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    description      VARCHAR(50) NOT NULL,
    fee_code         VARCHAR(20) NOT NULL UNIQUE,
    amountPerUnit    DECIMAL(8,2),
    frequency        VARCHAR(20),
    unit             VARCHAR(20),
    -- BaseEntity 共用欄位
    created_at       DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    last_updated     DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    created_by       INT,
    updated_by       INT,
    status           BIT        NOT NULL DEFAULT 1,
    note             VARCHAR(MAX),
    community_id     INT
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END

-- 期別表 table
IF OBJECT_ID('finance_BillingPeriod', 'U') IS NULL
BEGIN
CREATE TABLE BillingPeriod (
    billing_period_id INT IDENTITY(1,1) PRIMARY KEY,
    period_name       VARCHAR(50) NOT NULL UNIQUE,
    period_code       VARCHAR(20) NOT NULL UNIQUE,
    start_date        DATE NOT NULL,
    end_date          DATE NOT NULL,
    due_date          DATETIME,
    -- BaseEntity 共用欄位
    created_at       DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    last_updated     DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    created_by       INT,
    updated_by       INT,
    status           BIT        NOT NULL DEFAULT 1,
    note             VARCHAR(MAX),
    fee_type_id      INT NOT NULL,
    community_id     INT,
    FOREIGN KEY (fee_type_id) REFERENCES FeeType(fee_type_id)
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END

-- 應收帳款（請款單）table
IF OBJECT_ID('finance_Invoice', 'U') IS NULL
BEGIN
CREATE TABLE Invoice (
    invoice_id        INT IDENTITY(1,1) PRIMARY KEY,
    amount_due        DECIMAL(10,2) NOT NULL,
    period_name       VARCHAR(20),
    deadline          DATETIME,
    payment_plan      VARCHAR(20),
    unit_count        DECIMAL(8,2),
    unit_price        DECIMAL(8,2),
    total_amount      DECIMAL(10,2),
    paymentStatus     VARCHAR(20),
    -- BaseEntity 共用欄位
    created_at       DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    last_updated     DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    created_by       INT,
    updated_by       INT,
    status           BIT        NOT NULL DEFAULT 1,
    note             VARCHAR(MAX),
    users_id         INT NOT NULL,
    fee_type_id      INT NOT NULL,
    billing_period_id INT NOT NULL,
    community_id     INT,
    FOREIGN KEY (fee_type_id) REFERENCES FeeType(fee_type_id),
    FOREIGN KEY (billing_period_id) REFERENCES BillingPeriod(billing_period_id)
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END

-- 請款單回覆 table
IF OBJECT_ID('finance_Invoice_Response', 'U') IS NULL
BEGIN
CREATE TABLE Invoice_Response (
    invoice_response_id INT IDENTITY(1,1) PRIMARY KEY,
    last_response_time  DATETIME,
    account_code        VARCHAR(10),
    last_response       VARCHAR(MAX),
    verified            BIT,
    verified_time       DATETIME,
    verified_by         INT,
    users_id            INT NOT NULL,
    invoice_id          INT NOT NULL,
    -- BaseEntity 共用欄位（如需）
    -- community_id     INT,
    FOREIGN KEY (users_id) REFERENCES [User](users_id),
    FOREIGN KEY (invoice_id) REFERENCES Invoice(invoice_id)
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
CREATE TABLE Receipt (
    receipt_id        INT IDENTITY(1,1) PRIMARY KEY,
    receipt_num       VARCHAR(50),
    payment_method    VARCHAR(20),
    paid_at           DATETIME,
    debit_at          DATETIME,
    amount_pay        DECIMAL(10,2),
    installments      VARCHAR(10),
    -- BaseEntity 共用欄位
    created_at       DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    last_updated     DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    created_by       INT,
    updated_by       INT,
    status           BIT        NOT NULL DEFAULT 1,
    note             VARCHAR(MAX),
    invoice_id       INT NOT NULL,
    users_id         INT NOT NULL,
    community_id     INT,
    FOREIGN KEY (invoice_id) REFERENCES Invoice(invoice_id)
    FOREIGN KEY (users_id) REFERENCES [User](users_id)
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END
