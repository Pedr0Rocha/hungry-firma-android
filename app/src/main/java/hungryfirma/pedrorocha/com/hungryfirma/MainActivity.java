package hungryfirma.pedrorocha.com.hungryfirma;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import hungryfirma.pedrorocha.com.hungryfirma.models.Cliente;
import hungryfirma.pedrorocha.com.hungryfirma.models.Item;
import hungryfirma.pedrorocha.com.hungryfirma.models.Venda;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    DatabaseReference mDatabase;
    DatabaseReference vendasReference;
    DatabaseReference clientesReference;

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
//            }
//            return false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Button btnAddCompra = findViewById(R.id.btnAddVenda);

        btnAddCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVenda();
            }
        });

        initDatabase();
    }

    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        vendasReference = FirebaseDatabase.getInstance()
                .getReference(HungryFirmaConstants.FIREBASE.HOLDER_MAIN)
                .child(HungryFirmaConstants.FIREBASE.HOLDER_VENDAS);

        clientesReference = FirebaseDatabase.getInstance()
                .getReference(HungryFirmaConstants.FIREBASE.HOLDER_MAIN)
                .child(HungryFirmaConstants.FIREBASE.HOLDER_CLIENTES.NOME);

        vendasReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);

                int totalVendas = 0;
                double valorTotal = 0.0;
                double totalGasto = 0.0;

                HashMap<String, Cliente> clientesAtualizar = new HashMap<>();

                for (DataSnapshot diasVendas : dataSnapshot.getChildren()) {

                    totalVendas += diasVendas.getChildrenCount();

                    for (DataSnapshot venda : diasVendas.getChildren()) {

                        double valorVenda = Double.parseDouble(venda.child("valor").getValue().toString());
                        double valorCompra = Double.parseDouble(venda.child("item").child("valorCompra").getValue().toString());
                        valorTotal += valorVenda;
                        totalGasto += valorCompra;

                        /* Atualiza Clientes */
                        String picPayId = venda.child("picPayId").getValue().toString();
                        Cliente cliente = new Cliente(picPayId);

                        if (clientesAtualizar.containsKey(cliente.getId())) {
                            Cliente clienteAtualizar = clientesAtualizar.get(cliente.getId());
                            clientesAtualizar.remove(cliente.getId());

                            clienteAtualizar.acumulaTotalCompras();
                            clienteAtualizar.acumulaTotalGasto(valorVenda);
                            clientesAtualizar.put(cliente.getId(), clienteAtualizar);
                        } else {
                            cliente.setTotalCompras(1);
                            cliente.setTotalGasto(valorVenda);
                            clientesAtualizar.put(cliente.getId(), cliente);
                        }

                    }
                }

                for (Cliente clienteAtualizar : clientesAtualizar.values()) {
                    mDatabase.child(HungryFirmaConstants.FIREBASE.HOLDER_MAIN)
                            .child(HungryFirmaConstants.FIREBASE.HOLDER_CLIENTES.NOME)
                            .child(clienteAtualizar.getId())
                            .setValue(clienteAtualizar);
                }

                double mediaPorVenda = 0;
                if (totalVendas > 0) {
                    mediaPorVenda = valorTotal / totalVendas;
                }

                double mediaPorDia = 0;
                if (dataSnapshot.getChildrenCount() > 0) {
                    mediaPorDia = valorTotal / dataSnapshot.getChildrenCount();
                }

                mDatabase.child(HungryFirmaConstants.FIREBASE.HOLDER_ESTATISTICAS.NOME)
                        .child(HungryFirmaConstants.FIREBASE.HOLDER_ESTATISTICAS.TOTAL_VENDAS)
                        .setValue(totalVendas);

                mDatabase.child(HungryFirmaConstants.FIREBASE.HOLDER_ESTATISTICAS.NOME)
                        .child(HungryFirmaConstants.FIREBASE.HOLDER_ESTATISTICAS.TOTAL_GASTO)
                        .setValue(totalGasto);

                mDatabase.child(HungryFirmaConstants.FIREBASE.HOLDER_ESTATISTICAS.NOME)
                        .child(HungryFirmaConstants.FIREBASE.HOLDER_ESTATISTICAS.TOTAL_VALOR)
                        .setValue(valorTotal);

                mDatabase.child(HungryFirmaConstants.FIREBASE.HOLDER_ESTATISTICAS.NOME)
                        .child(HungryFirmaConstants.FIREBASE.HOLDER_ESTATISTICAS.MEDIA_POR_VENDA)
                        .setValue(mediaPorVenda);

                mDatabase.child(HungryFirmaConstants.FIREBASE.HOLDER_ESTATISTICAS.NOME)
                        .child(HungryFirmaConstants.FIREBASE.HOLDER_ESTATISTICAS.MEDIA_POR_DIA)
                        .setValue(mediaPorDia);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addVenda() {
        Venda venda = getVendaFromInput();

        if (venda != null) {
            mDatabase.child(HungryFirmaConstants.FIREBASE.HOLDER_MAIN)
                    .child(HungryFirmaConstants.FIREBASE.HOLDER_VENDAS)
                    .child(venda.getData().replace('/', '-'))
                    .child(String.valueOf(System.currentTimeMillis()))
                    .setValue(venda);

            Toast.makeText(this, "Sucesso demais, bicho!", Toast.LENGTH_SHORT).show();
        }
    }

    private Venda getVendaFromInput() {
        /*
        * TODO
        * - auto completar campos de acordo com informacoes ja inseridas
        *
        * */


        EditText etComprador = findViewById(R.id.etComprador);
        EditText etNomeItem = findViewById(R.id.etNomeItem);
        EditText etPrecoCompraItem = findViewById(R.id.etPrecoCompraItem);
        EditText etPrecoVendaItem = findViewById(R.id.etPrecoVendaItem);
        EditText etQuando = findViewById(R.id.etQuando);

        Item item;
        try {
            item = new Item(
                    etNomeItem.getText().toString(),
                    Double.valueOf(etPrecoCompraItem.getText().toString()),
                    Double.valueOf(etPrecoVendaItem.getText().toString())
            );
        } catch (Exception ex) {
            Toast.makeText(this, "Aí não né, escreve direito os dados do item", Toast.LENGTH_SHORT).show();
            return null;
        }

        Venda venda;
        try {
            venda = new Venda(
                    etComprador.getText().toString(),
                    item,
                    1,
                    etQuando.getText().toString()
            );
        } catch (Exception ex) {
            Toast.makeText(this, "Aí não né, escreve direito os dados da venda", Toast.LENGTH_SHORT).show();
            return null;
        }

        etComprador.setText("");
        etComprador.requestFocus();
        etNomeItem.setText("");
        etPrecoCompraItem.setText("");
        etPrecoVendaItem.setText("");

        return venda;
    }



}
