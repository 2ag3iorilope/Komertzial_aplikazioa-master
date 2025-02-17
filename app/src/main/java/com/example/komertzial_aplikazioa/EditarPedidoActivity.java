package com.example.komertzial_aplikazioa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
                // Obtener el código del pedido seleccionado
                int codigoPedidoSeleccionado = (int) parentView.getItemAtPosition(position);
                // Cargar los detalles del producto para ese pedido
                loadDetallesProducto(codigoPedidoSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        btnGuardar.setOnClickListener(view -> guardarCambios());
    }

    // Cargar los pedidos desde la base de datos en el Spinner
    private void loadPedidos() {
        List<Integer> pedidos = new ArrayList<>();
        String query = "SELECT codigo_pedido FROM Eskaera_Goiburua";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int codigoPedido = cursor.getInt(cursor.getColumnIndex("codigo_pedido"));
                    pedidos.add(codigoPedido);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("EditarPedidoActivity", "Error al cargar los pedidos", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pedidos);
        spinnerPedidos.setAdapter(adapter);
    }

    // Cargar los detalles del producto según el código del pedido seleccionado
    private void loadDetallesProducto(int codigoPedido) {
        detallesProducto.clear();  // Limpiar los detalles previos
        List<ProductoDetalle> detalles = obtenerDetallesDelPedido(db, codigoPedido);

        if (detalles.isEmpty()) {
            Log.e("EditarPedidoActivity", "No se encontraron detalles para el pedido con código: " + codigoPedido);
        } else {
            Log.d("EditarPedidoActivity", "Detalles cargados para el pedido con código: " + codigoPedido);
        }

        detallesProducto.addAll(detalles);
        adapter.notifyDataSetChanged();  // Notificar al adaptador que los datos han cambiado
    }

    // Guardar cambios realizados en los detalles de los productos
    private void guardarCambios() {
        db.beginTransaction();
        try {
            for (ProductoDetalle detalle : detallesProducto) {
                ContentValues values = new ContentValues();
                values.put("cantidad", detalle.getCantidad());  // Actualizamos la cantidad

                int rowsAffected = db.update("Eskaera_Xehetasuna", values,
                        "codigo_pedido = ? AND codigo_producto = ?",
                        new String[]{String.valueOf(detalle.getCodigoPedido()), String.valueOf(detalle.getCodigoProducto())});

                if (rowsAffected == 0) {
                    Log.e("EditarPedidoActivity", "No se pudo actualizar el producto con código: " + detalle.getCodigoProducto());
                }
            }
            db.setTransactionSuccessful();  // Confirmar la transacción si todo está bien
            Toast.makeText(this, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("EditarPedidoActivity", "Error al guardar los cambios", e);
        } finally {
            db.endTransaction();  // Finalizar la transacción
        }
    }

    // Obtener los detalles del pedido desde la base de datos
    private List<ProductoDetalle> obtenerDetallesDelPedido(SQLiteDatabase db, int codigoPedido) {
        List<ProductoDetalle> detalles = new ArrayList<>();
        String query = "SELECT * FROM Eskaera_Xehetasuna WHERE codigo_pedido = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(codigoPedido)});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Verificar el índice de cada columna antes de acceder a ella
                    int codigoProductoIndex = cursor.getColumnIndex("codigo_producto");
                    int precioUnitarioIndex = cursor.getColumnIndex("precio_x_unidad");
                    int cantidadIndex = cursor.getColumnIndex("cantidad");

                    if (codigoProductoIndex != -1  && precioUnitarioIndex != -1 && cantidadIndex != -1) {
                        int codigoProducto = cursor.getInt(codigoProductoIndex);

                        double precioUnitario = cursor.getDouble(precioUnitarioIndex);
                        int cantidad = cursor.getInt(cantidadIndex);

                        ProductoDetalle productoDetalle = new ProductoDetalle(codigoPedido, codigoProducto, nombreProducto, precioUnitario, cantidad);
                        detalles.add(productoDetalle);
                    } else {
                        Log.e("EditarPedidoActivity", "Column indices are invalid.");
                    }
                } while (cursor.moveToNext());
            } else {
                Log.e("EditarPedidoActivity", "No data found for codigo_pedido: " + codigoPedido);
            }
        } catch (Exception e) {
            Log.e("EditarPedidoActivity", "Error al obtener los detalles del pedido", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return detalles;
    }
}
