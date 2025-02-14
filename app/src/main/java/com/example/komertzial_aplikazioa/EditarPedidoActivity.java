package com.example.komertzial_aplikazioa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EditarPedidoActivity extends AppCompatActivity {

    private Spinner spinnerPedidos;
    private RecyclerView recyclerDetalles;
    private Button btnGuardar;
    private ProductoDetalleAdapter adapter;
    private List<ProductoDetalle> detallesProducto = new ArrayList<>();
    private SQLiteDatabase db;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pedido);

        spinnerPedidos = findViewById(R.id.spinnerPedidos);
        recyclerDetalles = findViewById(R.id.recyclerDetalles);
        btnGuardar = findViewById(R.id.btnGuardar);

        db = new DatabaseHelper(context).getWritableDatabase();

        // Configurar RecyclerView
        recyclerDetalles.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductoDetalleAdapter(detallesProducto, this);
        recyclerDetalles.setAdapter(adapter);

        // Cargar los pedidos en el Spinner
        loadPedidos();

        spinnerPedidos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int codigoPedidoSeleccionado = (int) parentView.getItemAtPosition(position);
                loadDetallesProducto(codigoPedidoSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        btnGuardar.setOnClickListener(view -> guardarCambios());
    }

    private void loadPedidos() {
        List<Integer> pedidos = new ArrayList<>();
        String query = "SELECT codigo_pedido FROM Eskaera_Goiburua";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int codigoPedido = cursor.getInt(cursor.getColumnIndex("codigo_pedido"));
                pedidos.add(codigoPedido);
            } while (cursor.moveToNext());
            cursor.close();
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pedidos);
        spinnerPedidos.setAdapter(adapter);
    }

    private void loadDetallesProducto(int codigoPedido) {
        detallesProducto.clear();
        List<ProductoDetalle> detalles = obtenerDetallesDelPedido(db, codigoPedido);
        detallesProducto.addAll(detalles);
        adapter.notifyDataSetChanged();
    }

    private void guardarCambios() {
        for (ProductoDetalle detalle : detallesProducto) {
            // Actualizar la cantidad en la base de datos
            ContentValues values = new ContentValues();
            values.put("cantidad", detalle.getCantidad());  // Actualizamos la cantidad

            // Asegurarse de que el método getCodigoPedido() existe
            db.update("Eskaera_Xehetasuna", values, "codigo_pedido = ? AND codigo_producto = ?",
                    new String[]{String.valueOf(detalle.getCodigoPedido()), String.valueOf(detalle.getCodigoProducto())});
        }
        Toast.makeText(this, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show();
    }

    private List<ProductoDetalle> obtenerDetallesDelPedido(SQLiteDatabase db, int codigoPedido) {
        List<ProductoDetalle> detalles = new ArrayList<>();
        String query = "SELECT * FROM Eskaera_Xehetasuna WHERE codigo_pedido = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(codigoPedido)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int codigoProducto = cursor.getInt(cursor.getColumnIndex("codigo_producto"));
                @SuppressLint("Range") String nombreProducto = cursor.getString(cursor.getColumnIndex("nombre_producto"));
                @SuppressLint("Range") double precioUnitario = cursor.getDouble(cursor.getColumnIndex("precio_unitario"));
                @SuppressLint("Range") int cantidad = cursor.getInt(cursor.getColumnIndex("cantidad"));

                ProductoDetalle productoDetalle = new ProductoDetalle(codigoPedido,codigoProducto, nombreProducto, precioUnitario, cantidad);
                detalles.add(productoDetalle);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return detalles;
    }
}