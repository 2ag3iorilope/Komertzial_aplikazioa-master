package com.example.komertzial_aplikazioa;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductoDetalleAdapter extends RecyclerView.Adapter<ProductoDetalleAdapter.ViewHolder> {

    private List<ProductoDetalle> productos;
    private Context context;

    public ProductoDetalleAdapter(List<ProductoDetalle> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto_detalle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductoDetalle producto = productos.get(position);
        holder.nombreProducto.setText(producto.getNombreProducto());
        holder.precioUnitario.setText(String.format("€ %.2f", producto.getPrecioUnitario()));
        holder.cantidad.setText(String.valueOf(producto.getCantidad()));

        // Se asegura de que el TextWatcher solo se agregue una vez y se actualice la cantidad
        holder.cantidad.setTag(producto); // Asignar el producto al tag para usarlo en el TextWatcher

        // Si ya hay un TextWatcher, no lo agregues nuevamente
        holder.cantidad.removeTextChangedListener(holder.textWatcher);
        holder.cantidad.addTextChangedListener(holder.textWatcher);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    // Método para actualizar la lista de productos
    public void actualizarProductos(List<ProductoDetalle> nuevosProductos) {
        this.productos = nuevosProductos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombreProducto, precioUnitario;
        EditText cantidad;
        TextWatcher textWatcher;

        public ViewHolder(View itemView) {
            super(itemView);
            nombreProducto = itemView.findViewById(R.id.nombreProducto);
            precioUnitario = itemView.findViewById(R.id.precioUnitario);
            cantidad = itemView.findViewById(R.id.cantidad);

            // Crear el TextWatcher una sola vez
            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int after) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    ProductoDetalle producto = (ProductoDetalle) cantidad.getTag();
                    if (producto != null) {
                        try {
                            int newCantidad = Integer.parseInt(editable.toString());
                            producto.setCantidad(newCantidad);
                        } catch (NumberFormatException e) {
                            // Handle invalid input
                        }
                    }
                }
            };
        }
    }
}
