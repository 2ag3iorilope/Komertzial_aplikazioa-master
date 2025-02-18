package com.example.komertzial_aplikazioa;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PedidoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private List<Producto> listaProductos;
    private List<Producto> productosSeleccionados;
    private List<EskaeraXehetasuna> detallesPedido; // Lista para almacenar los detalles del pedido
    private Button btnConfirmarPedido;
    EskaeraGoiburua eskaeraGoiburua = new EskaeraGoiburua();
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        recyclerView = findViewById(R.id.recyclerProductos);
        btnConfirmarPedido = findViewById(R.id.btnConfirmarPedido);
        dbHelper = new DatabaseHelper(this);

        listaProductos = new ArrayList<>();
        productosSeleccionados = new ArrayList<>();
        detallesPedido = new ArrayList<>(); // Crear la lista de detalles del pedido

        // Acceder a la base de datos y obtener los productos
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        listaProductos = databaseHelper.obtenerProductos(databaseHelper.getReadableDatabase());

        // Configurar el adaptador con los productos obtenidos de la base de datos
        adapter = new ProductoAdapter(this, listaProductos, productosSeleccionados, detallesPedido); // Pasar la lista de detalles
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnConfirmarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarPedido();
            }
        });
    }

    private void confirmarPedido() {
        if (productosSeleccionados.isEmpty()) {
            Toast.makeText(this, "No has seleccionado productos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lista para los detalles confirmados del pedido
        List<EskaeraXehetasuna> detallesPedidoConfirmados = new ArrayList<>();
        for (Producto producto : productosSeleccionados) {
            int cantidad = 0;

            // Buscar la cantidad en detallesPedido usando el id del producto
            for (EskaeraXehetasuna detalle : detallesPedido) {
                if (detalle.getCodigoProducto() == producto.getId()) {
                    cantidad = detalle.getCantidad(); // Obtener la cantidad del detalle
                    break;
                }
            }

            // Validar que la cantidad sea mayor a cero
            if (cantidad <= 0) {
                Toast.makeText(this, "Cantidad inválida para el producto: " + producto.getIzena(), Toast.LENGTH_SHORT).show();
                return;  // Si alguna cantidad es inválida, mostramos un error y salimos del método
            }

            // Calcular el total para el producto seleccionado
            double total = producto.getPrezio() * cantidad;
            EskaeraXehetasuna detalle = new EskaeraXehetasuna(dbHelper.obtenerMaxIdEskaeraGoiburua(), producto.getId(), producto.getPrezio(), total, cantidad);
            detallesPedidoConfirmados.add(detalle);
        }

        // Crear y guardar el pedido
        EskaeraGoiburua eskaeraGoiburua = new EskaeraGoiburua("Direccion de envio", "2025-02-14", 1, 1, "Confirmado");
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.guardarPedido(getApplicationContext(), eskaeraGoiburua, detallesPedidoConfirmados);

        Toast.makeText(this, "Pedido confirmado con " + productosSeleccionados.size() + " productos", Toast.LENGTH_LONG).show();
    }

}
