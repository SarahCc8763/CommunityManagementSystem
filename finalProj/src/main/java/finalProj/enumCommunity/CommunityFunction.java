package finalProj.enumCommunity;

public enum CommunityFunction {


    TICKET(1L),            // 2^0  =   1    => 0000 0000 0001
    ANNOUNCEMENT(2L),      // 2^1  =   2    => 0000 0000 0010
    PACKAGE(4L),           // 2^2  =   4    => 0000 0000 0100
    PARKING(8L),           // 2^3  =   8    => 0000 0000 1000
    FAQ(16L),              // 2^4  =  16    => 0000 0001 0000
    REPORT(32L),           // 2^5  =  32    => 0000 0010 0000
    VISITOR(64L),          // 2^6  =  64    => 0000 0100 0000
    VOTE(128L),            // 2^7  = 128    => 0000 1000 0000
    RESERVATION(256L),     // 2^8  = 256    => 0001 0000 0000
    MESSAGE_BOARD(512L);   // 2^9  = 512    => 0010 0000 0000

	private final Long value;

	CommunityFunction(Long value) {
		this.value = value;
	}

	public Long getValue() {
		return value;
	}

}
