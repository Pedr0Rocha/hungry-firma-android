package hungryfirma.pedrorocha.com.hungryfirma.models;

public class Cliente {

    private String picPayId;
    private int totalCompras;
    private double totalGasto;

    public Cliente(String picPayId) {
        this.picPayId = picPayId;
        this.totalCompras = 1;
    }

    public Cliente(String picPayId, int totalCompras, double totalGasto) {
        this.picPayId = picPayId;
        this.totalCompras = totalCompras;
        this.totalGasto = totalGasto;
    }

    public String getPicPayId() {
        return picPayId;
    }

    public String getId() {
        return picPayId.replaceAll("[^A-Za-z0-9]", "");
    }

    public void setPicPayId(String picPayId) {
        this.picPayId = picPayId;
    }

    public int getTotalCompras() {
        return totalCompras;
    }

    public void setTotalCompras(int totalCompras) {
        this.totalCompras = totalCompras;
    }

    public double getTotalGasto() {
        return totalGasto;
    }

    public void setTotalGasto(double totalGasto) {
        this.totalGasto = totalGasto;
    }
}
