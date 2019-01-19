package hungryfirma.pedrorocha.com.hungryfirma.models;

public class ItemEstoque {

    private String nome;
    private double quantidadeVendas;
    private double quantidadeEstoque;
    private double valorGasto;
    private double valorVendas;
    private double ultimoPrecoVenda;
    private double ultimoPrecoCompra;

    public ItemEstoque(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getQuantidadeVendas() {
        return quantidadeVendas;
    }

    public void setQuantidadeVendas(double quantidadeVendas) {
        this.quantidadeVendas = quantidadeVendas;
    }

    private void acumulaQuantidadeVendas() {
        this.quantidadeVendas++;
    }

    public double getValorGasto() {
        return valorGasto;
    }

    public void setValorGasto(double valorGasto) {
        this.valorGasto = valorGasto;
    }

    private void acumulaValorGasto(double gasto) {
        this.valorGasto += gasto;
    }

    public double getValorVendas() {
        return valorVendas;
    }

    public void setValorVendas(double valorVendas) {
        this.valorVendas = valorVendas;
    }

    private void acumulaValorVendas(double venda) {
        this.valorVendas += venda;
    }

    public double getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(double quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    private void decrementarEstoque() {
        if (this.quantidadeEstoque > 0) this.quantidadeEstoque--;
    }

    private void setUltimoPrecoVenda(double ultimoPrecoVenda) {
        this.ultimoPrecoVenda = ultimoPrecoVenda;
    }

    private void setUltimoPrecoCompra(double ultimoPrecoCompra) {
        this.ultimoPrecoCompra = ultimoPrecoCompra;
    }

    public double getUltimoPrecoVenda() {
        return ultimoPrecoVenda;
    }

    public double getUltimoPrecoCompra() {
        return ultimoPrecoCompra;
    }

    public void processaVenda(double gasto, double valorVenda) {
        decrementarEstoque();
        acumulaQuantidadeVendas();
        acumulaValorVendas(valorVenda);
        acumulaValorGasto(gasto);

        setUltimoPrecoCompra(gasto);
        setUltimoPrecoVenda(valorVenda);
    }
}
