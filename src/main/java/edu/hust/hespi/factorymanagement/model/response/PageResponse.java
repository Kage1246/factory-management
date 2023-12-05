package edu.hust.hespi.factorymanagement.model.response;

public class PageResponse<T> extends CustomResponse<T> {
    private long dataCount;

    public PageResponse() {}

    public PageResponse(long dataCount) {
        this.dataCount = dataCount;
    }

    public long getDataCount() {
        return dataCount;
    }

    public void setDataCount(long dataCount) {
        this.dataCount = dataCount;
    }

    public PageResponse<T> success() {
        super.success();
        return this;
    }

    public PageResponse<T> dataCount(long dataCount) {
        this.dataCount = dataCount;
        return this;
    }

    @Override
    public PageResponse<T> data(T data) {
        super.data(data);
        return this;
    }
}
