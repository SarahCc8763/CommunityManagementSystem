package finalProj.enumCommunity;

public enum CommunityFunction {
// 主功能（2^0 ~ 2^7）
 // 主功能
 PACKAGE(1L),                // 2^0
 BOOKING(1L << 1),           // 2^1
 INVOICE(1L << 2),           // 2^2
 MANBERSERVICE(1L << 3),     // 2^3
 TICKET(1L << 4),            // 2^4
 FQA(1L << 5),               // 2^5
 PARK(1L << 6),              // 2^6
 NOTICE(1L << 7),            // 2^7

 // 包裹子功能
 PACKAGEPENDING(1L << 8),    // 2^8
 PACKAGEHISTORY(1L << 9),    // 2^9

 // 預約子功能
 FHV(1L << 10),              // 2^10
 FFAV(1L << 11),             // 2^11
 RHV(1L << 12),              // 2^12
 PTV(1L << 13),              // 2^13
 PTUV(1L << 14),             // 2^14
 PHV(1L << 15),              // 2^15

 // 繳費子功能
 INVOICEBILL(1L << 16),          // 2^16
 INVOICEHISTORY(1L << 17),       // 2^17
 INVOICETYPEADD(1L << 18),       // 2^18
 INVOICEPERIODADD(1L << 19),     // 2^19
 INVOICEINVOICEADD(1L << 20),    // 2^20
 INVOICERECEIPTADD(1L << 21),    // 2^21
 INVOICEREPLY(1L << 22),         // 2^22

 // 會員服務子功能
 MANBERSERVICEEDIT(1L << 23),        // 2^23
 MANBERSERVICETRANSFER(1L << 24),    // 2^24

 // 報修子功能
 TICKETFORM(1L << 25),           // 2^25
 TICKETLIST(1L << 26),           // 2^26
 TICKETASSIGN(1L << 27),         // 2^27
 TICKETCOMMUNITY(1L << 28),      // 2^28

 // FAQ 子功能
 FAQQANDA(1L << 29),             // 2^29
 FQACONTACT(1L << 30),           // 2^30
 FQAFEEDBACK(1L << 31),          // 2^31
 FAQADMIN(1L << 32),             // 2^32
 FEEDBACKADMIN(1L << 33),        // 2^33

 // 停車子功能
 PARKFRONT(1L << 34),            // 2^34
 MYPARK(1L << 35),               // 2^35
 PARKRENT(1L << 36),             // 2^36
 PARKAPP(1L << 37),              // 2^37
 PARKBACK(1L << 38),             // 2^38
 PARKINIT(1L << 39),             // 2^39
 PARKSLOT(1L << 40),             // 2^40
 PARKREC(1L << 41),              // 2^41
 PARKEVE(1L << 42),              // 2^42

 // 公告子功能
 NOTICEIMPORTANT(1L << 43),      // 2^43
 NOTICELATEST(1L << 44),         // 2^44
 BULLETINADMIN(1L << 45),       // 2^45

 //廠商
 VENDOR(1L << 46);                // 2^46

    private final Long value;

    CommunityFunction(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

}
