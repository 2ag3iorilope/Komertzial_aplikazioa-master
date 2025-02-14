package com.example.komertzial_aplikazioa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private Context context;
    private List<Producto> listaProductos;
    private List<Producto> productosSeleccionados;
    private List<EskaeraXehetasuna> detallesPedido; // Lista de detalles del pedido

    public ProductoAdapter(Context context, List<Producto> listaProductos, List<Producto> productosSeleccionados, List<EskaeraXehetasuna> detallesPedido) {
        this.context = context;
        this.listaProductos = listaProductos;
        this.productosSeleccionados = productosSeleccionados;
        this.detallesPedido = detallesPedido;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        holder.txtNombreProducto.setText(producto.getIzena());

        // Cargar imagen según el nombre (p1, p2, p3...)
        int imageResId = context.getResources().getIdentifier("p" + producto.getId(), "drawable", context.getPackageName());
        holder.imgProducto.setImageResource(imageResId);

        // Mostrar el precio del producto
        holder.txtPrecioProducto.setText(String.format("$%.2f", producto.getPrezio()));

        holder.chkSeleccionar.setOnCheckedChangeListener(null);
        holder.chkSeleccionar.setChecked(productosSeleccionados.contains(producto));

        holder.chkSeleccionar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                productosSeleccionados.add(producto);
            } else {
                productosSeleccionados.remove(producto);
            }
        });

        // Establecer la cantidad inicial en el EditText
        holder.editCantidad.setText("1");

        // Guardar la cantidad ingresada y crear EskaeraXehetasuna
        holder.editCantidad.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String cantidadStr = holder.editCantidad.getText().toString();
                if (!cantidadStr.isEmpty()) {
                    int cantidad = Integer.parseInt(cantidadStr);

                    // Verificar si el producto ya está en detallesPedido
                    EskaeraXehetasuna detalleExistente = null;
                    for (EskaeraXehetasuna detalle : detallesPedido) {
                        if (detalle.getCodigoProducto() == producto.getId()) {
                            detalleExistente = detalle;
                            break;
                        }
                    }

                    if (detalleExistente != null) {
                        // Si ya existe, actualizamos la cantidad y el total
                        detalleExistente.setCantidad(cantidad);
                        detalleExistente.setTotal(producto.getPrezio() * cantidad);
                    } else {
                        // Si no existe, creamos un nuevo detalle
                        EskaeraXehetasuna detallePedido = new EskaeraXehetasuna(producto.getId(), producto.getId(), producto.getPrezio(), producto.getPrezio() * cantidad, cantidad);
                        detallesPedido.add(detallePedido);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProducto;
        TextView txtNombreProducto;
        TextView txtPrecioProducto;
        EditText editCantidad;
        CheckBox chkSeleccionar;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            txtNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            txtPrecioProducto = itemView.findViewById(R.id.txtPrecioProducto); // Nuevo TextView para el precio
            editCantidad = itemView.findViewById(R.id.editCantidad); // EditText para la cantidad
            chkSeleccionar = itemView.findViewById(R.id.chkSeleccionar);
        }
    }
}
