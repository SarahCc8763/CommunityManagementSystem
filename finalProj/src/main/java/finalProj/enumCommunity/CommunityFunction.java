package finalProj.enumCommunity;

public enum CommunityFunction {
// 主功能（2^0 ~ 2^7）
PACKAGE(1L),                // 2^0
BOOKING(2L),                // 2^1
INVOICE(4L),                // 2^2
MANBERSERVICE(8L),          // 2^3
TICKET(16L),                // 2^4
FQA(32L),                   // 2^5
PARK(64L),                  // 2^6
NOTICE(128L),               // 2^7

// 子功能（2^8 起）
PACKAGEPENDING(256L),               // 2^8
PACKAGEHISTORY(512L),               // 2^9

BOOKINGGYM(1024L),                  // 2^10
BOOKINGPOOL(2048L),                // 2^11
BOOKINGPARKING(4096L),             // 2^12

INVOICEBILL(8192L),                // 2^13
INVOICEHISTORY(16384L),            // 2^14
INVOICETYPEADD(32768L),            // 2^15
INVOICEPERIODADD(65536L),          // 2^16
INVOICEINVOICEADD(131072L),        // 2^17
INVOICERECEIPTADD(262144L),        // 2^18
INVOICEREPLY(524288L),             // 2^19

MANBERSERVICEEDIT(1048576L),       // 2^20
MANBERSERVICETRANSFER(2097152L),   // 2^21

TICKETFORM(4194304L),              // 2^22
TICKETLIST(8388608L),              // 2^23
TICKETDETAIL(16777216L),           // 2^24
TICKETASSIGN(33554432L),           // 2^25
TICKETCOMMUNITY(67108864L),        // 2^26

FAQQANDA(134217728L),              // 2^27
FQACONTACT(268435456L),            // 2^28
FQAFEEDBACK(536870912L),           // 2^29

PARKINIT(1073741824L),             // 2^30
PARKSLOT(2147483648L),             // 2^31
PARKRENT(4294967296L),             // 2^32
PARKREC(8589934592L),              // 2^33
PARKEVE(17179869184L),             // 2^34
PARKAPP(34359738368L),             // 2^35
MYPARK(68719476736L),             // 2^36
PARKFRONT(137438953472L),          // 2^37
PARKBACK(274877906944L),           // 2^38

NOTICEIMPORTANT(549755813888L),    // 2^39
NOTICELATEST(1099511627776L);      // 2^40


    private final Long value;

    CommunityFunction(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

}
