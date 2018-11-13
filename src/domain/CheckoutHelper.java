package domain;

public class CheckoutHelper{
    private long totalOpenTime;
    private long totalNotUtilizedTime;

    CheckoutHelper( long totalOpenTime,long totalNotUtilizedTime){
        this.totalOpenTime
                 = totalOpenTime;
        this.totalNotUtilizedTime
                = totalNotUtilizedTime;
    }

    public long getTotalOpenTime() {
        return totalOpenTime;
    }

    public void setTotalOpenTime(long totalOpenTime) {
        this.totalOpenTime = totalOpenTime;
    }

    public long getTotalNotUtilizedTime() {
        return totalNotUtilizedTime;
    }

    public void setTotalNotUtilizedTime(long totalNotUtilizedTime) {
        this.totalNotUtilizedTime = totalNotUtilizedTime;
    }

    public double avgUtilizationTime(){
        return  ((double)(totalOpenTime-totalNotUtilizedTime)/totalOpenTime);
    }
}
