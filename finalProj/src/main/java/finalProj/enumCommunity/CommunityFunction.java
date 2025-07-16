package finalProj.enumCommunity;

public enum CommunityFunction {
    // 主功能（2^0 ~ 2^7）
    // 主功能
    PACKAGE(1L), // 2^0
    BOOKING(1L << 1), // 2^1
    INVOICE(1L << 2), // 2^2
    MANBERSERVICE(1L << 3), // 2^3
    TICKET(1L << 4), // 2^4
    FQA(1L << 5), // 2^5
    PARK(1L << 6), // 2^6
    NOTICE(1L << 7), // 2^7

    // 包裹子功能
    PACKAGEPENDING(1L << 8),
    PACKAGEHISTORY(1L << 9),
    PACKAGESEARCH(1L << 10),
    ADDPACKAGE(1L << 11),

    // 預約子功能
    FHV(1L << 12),
    FFAV(1L << 13),
    RHV(1L << 14),
    PTV(1L << 15),
    PTUV(1L << 16),
    PHV(1L << 17),

    // 繳費子功能
    FINUSER(1L << 18),
    INVOICEBILL(1L << 19),
    RECEIPT(1L << 20),
    FEETYPEADD(1L << 21),
    INVOICEADD(1L << 22),
    RECEIPTADD(1L << 23),
    INVOICEVALIDATE(1L << 23),
    INVOICEWITHRESPONSE(1L << 24),
    BILLINGPERIODADD(1L << 25),
    FINADMIN(1L << 50),

    // 會員服務子功能
    MANBERSERVICEEDIT(1L << 26),
    MANBERSERVICETRANSFER(1L << 27),

    // 報修子功能
    TICKETFORM(1L << 29),
    TICKETLIST(1L << 30),
    TICKETASSIGN(1L << 31),
    TICKETDETAIL(1L << 32),

    // FAQ 子功能
    FAQQANDA(1L << 33),
    FQACONTACT(1L << 34),
    FQAFEEDBACK(1L << 35),
    FAQADMIN(1L << 36),
    FEEDBACKADMIN(1L << 37),

    // 停車子功能
    PARKFRONT(1L << 38),
    MYPARK(1L << 39),
    PARKRENT(1L << 40),
    PARKAPP(1L << 41),
    PARKBACK(1L << 42),
    PARKINIT(1L << 43),
    PARKSLOT(1L << 44),
    PARKREC(1L << 45),
    PARKEVE(1L << 46),

    // 公告子功能
    NOTICEIMPORTANT(1L << 47),
    NOTICELATEST(1L << 48),
    BULLETINADMIN(1L << 49),

    // 廠商
    VENDOR(1L << 50);

    private final Long value;

    CommunityFunction(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

}
