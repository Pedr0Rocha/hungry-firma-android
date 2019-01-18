package hungryfirma.pedrorocha.com.hungryfirma.models;

public class Venda {

    private String picPayId;
    private Item item;
    private int quantidade;
    private double valor;
    private long createdAt;
    private String data;

    public Venda(String picPayId, Item item, int quantidade, String data) {
        this.picPayId = picPayId;
        this.item = item;
        this.quantidade = quantidade;
        this.valor = item.getValorVenda() * quantidade;
        this.createdAt = System.currentTimeMillis();
        this.data = data;
    }

    public String getPicPayId() {
        return picPayId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
