package finalProj.enumCommunity;

public enum CommunityFunction {

    PACKAGE(1L << 0), // 2^0 = 1
    BOOKING(1L << 1), // 2^1 = 2
    INVOICE(1L << 2), // 2^2 = 4
    TICKET(1L << 3), // 2^3 = 8 ← 原本是 2^4，這邊調整為連續
    FQA(1L << 4), // 2^4 = 16
    PARK(1L << 5), // 2^5 = 32
    NOTICE(1L << 6), // 2^6 = 64
    VENDOR(1L << 7), // 2^7 = 128

    // 包裹子功能（8～11）
    PACKAGEPENDING(1L << 8),
    PACKAGEHISTORY(1L << 9),
    PACKAGESEARCH(1L << 10),
    ADDPACKAGE(1L << 11),

    // 公設預約子功能（12～17）
    FHV(1L << 12),
    FFAV(1L << 13),
    RHV(1L << 14),
    PTV(1L << 15),
    PTUV(1L << 16),
    PHV(1L << 17),

    // 財務子功能（18～26）
    FINUSER(1L << 18),
    INVOICEBILL(1L << 19),
    RECEIPT(1L << 20),
    FEETYPEADD(1L << 21),
    BILLINGPERIODADD(1L << 22),
    INVOICEADD(1L << 23),
    RECEIPTADD(1L << 24),
    INVOICEVALIDATE(1L << 25),
    INVOICEWITHRESPONSE(1L << 26),

    // 報修子功能（27～30）
    TICKETFORM(1L << 27),
    TICKETLIST(1L << 28),
    TICKETASSIGN(1L << 29),
    TICKETDETAIL(1L << 30),

    // FAQ 子功能（31～34）
    FAQQANDA(1L << 31),
    FQACONTACT(1L << 32),
    FQAFEEDBACK(1L << 33),
    FAQADMIN(1L << 34),
    FEEDBACKADMIN(1L << 35),

    // 停車子功能（36～44）
    PARKFRONT(1L << 36),
    MYPARK(1L << 37),
    PARKRENT(1L << 38),
    PARKAPP(1L << 39),
    PARKBACK(1L << 40),
    PARKINIT(1L << 41),
    PARKSLOT(1L << 42),
    PARKREC(1L << 43),
    PARKEVE(1L << 44),

    // 公告子功能（45～47）
    NOTICEIMPORTANT(1L << 45),
    NOTICELATEST(1L << 46),
    BULLETINADMIN(1L << 47),

    // 管理員（例如後台功能）（48～）
    FINADMIN(1L << 48);

    private final Long value;

    CommunityFunction(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

}
