package team7.inplace.admin.crawling.domain;

public enum ChannelType {
    FOOD("FD6");

    private final String code;

    ChannelType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
