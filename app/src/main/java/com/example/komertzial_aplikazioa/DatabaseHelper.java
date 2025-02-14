package com.example.komertzial_aplikazioa;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import android.util.Xml;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "komertzial_aplikazioa"; //Datu base izena
    private static final int DATABASE_VERSION = 5;

    // Taulak
    public static final String TABLE_KOMERTZIALAK = "komertzialak";
    public static final String TABLE_AGENDA = "agenda";
    private static final String TABLE_PRODUCTOS = "Produktua";
    private static final String TABLE_ENCABEZADO_PEDIDO = "Eskaera_Goiburua";
    private static final String TABLE_DETALLES_PEDIDO = "Eskaera_Xehetasuna";


    // Komertzial taularen zutabeak
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_TELEFONO = "telefono";
    public static final String COLUMN_PASAHITZA = "pasahitza";
    public static final String COLUMN_EREMUA = "eremua";

    // Agenda taularen zutabeak
    public static final String COLUMN_ID_AGENDA = "id";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_DETALLES = "detalles";
    public static final String COLUMN_FECHA = "fecha";
    public static final String COLUMN_KOMERTZIAL_ID = "komertzial_id";

    //Partner taularen zutabeak
    public static final String TABLE_PARTNER = "partnerra";
    public static final String COLUMN_PARTNER_ID = "Partner_id";
    public static final String COLUMN_NOMBRE_PART = "Nombre";
    public static final String COLUMN_DIRECCION = "Direccion";
    public static final String COLUMN_TELEFONO_PART = "Telefono";
    public static final String COLUMN_ESTADO = "Estado";
    public static final String COLUMN_ID_COMERCIAL = "Id_Comercial";

    // Columnas de productos
    private static final String COLUMN_CODIGO_PRODUCTO = "codigo";
    private static final String COLUMN_NOMBRE_PRODUCTO = "nombre";
    private static final String COLUMN_PRECIO_PRODUCTO = "precio";
    private static final String COLUMN_STOCK_PRODUCTO = "stock";

    private static final String COLUMN_CODIGO_PEDIDO = "codigo_pedido";
    private static final String COLUMN_DIRECCION_ENVIO = "direccion_envio";
    private static final String COLUMN_FECHA_PEDIDO = "fecha";
    private static final String COLUMN_ID_COMERCIAL2 = "id_comercial";
    private static final String COLUMN_ID_PARTNER = "id_partner";
    private static final String COLUMN_ESTADO_PEDIDO = "estado";
    private static final String COLUMN_CANTIDAD  = "cantidad";


    // Columnas de detalles de pedido
    private static final String COLUMN_CODIGO_PRODUCTO_DETALLE = "codigo_producto";

    private static final String COLUMN_PRECIO_UNITARIO = "precio_x_unidad";
    private static final String COLUMN_TOTAL = "total";

    //Taulak sortzeko aginduak
    private static final String CREATE_TABLE_KOMERTZIALAK =
            "CREATE TABLE " + TABLE_KOMERTZIALAK + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOMBRE + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_TELEFONO + " TEXT, " +
                    COLUMN_PASAHITZA + " TEXT, " +
                    COLUMN_EREMUA + " TEXT)"; // Nueva columna

    private static final String CREATE_TABLE_AGENDA =
            "CREATE TABLE " + TABLE_AGENDA + " (" +
                    COLUMN_ID_AGENDA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITULO + " TEXT, " +
                    COLUMN_DETALLES + " TEXT, " +
                    COLUMN_FECHA + " TEXT, " +
                    COLUMN_KOMERTZIAL_ID + " INTEGER, " +
                    "FOREIGN KEY (" + COLUMN_KOMERTZIAL_ID + ") REFERENCES " +
                    TABLE_KOMERTZIALAK + "(" + COLUMN_ID + "))";

    String CREATE_TABLE_PARTNER = "CREATE TABLE " + TABLE_PARTNER + " (" +
            COLUMN_PARTNER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NOMBRE_PART + " TEXT NOT NULL, " +
            COLUMN_DIRECCION + " TEXT NOT NULL, " +
            COLUMN_TELEFONO_PART + " TEXT NOT NULL, " +
            COLUMN_ESTADO + " INTEGER NOT NULL, " + // 0 = No es partner, 1 = Es partner
            COLUMN_ID_COMERCIAL + " INTEGER, " +
            "FOREIGN KEY (" + COLUMN_ID_COMERCIAL + ") REFERENCES komertzialak(id));";

    // Sentencias SQL para crear las tablas
    private static final String CREATE_TABLE_PRODUCTOS =
            "CREATE TABLE " + TABLE_PRODUCTOS + " (" +
                    COLUMN_CODIGO_PRODUCTO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOMBRE_PRODUCTO + " TEXT, " +
                    COLUMN_PRECIO_PRODUCTO + " REAL, " +
                    COLUMN_STOCK_PRODUCTO + " INTEGER)";

    private static final String CREATE_TABLE_ENCABEZADO_PEDIDO =
            "CREATE TABLE " + TABLE_ENCABEZADO_PEDIDO + " (" +
                    COLUMN_CODIGO_PEDIDO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DIRECCION_ENVIO + " TEXT, " +
                    COLUMN_FECHA_PEDIDO + " TEXT, " +
                    COLUMN_ID_COMERCIAL2 + " INTEGER, " +
                    COLUMN_ID_PARTNER + " INTEGER, " +
                    COLUMN_ESTADO_PEDIDO + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_ID_COMERCIAL + ") REFERENCES " +
                    TABLE_KOMERTZIALAK + "(id), " +
                    "FOREIGN KEY (" + COLUMN_ID_PARTNER + ") REFERENCES " +
                    TABLE_KOMERTZIALAK + "(id))";

    private static final String CREATE_TABLE_DETALLES_PEDIDO =
            "CREATE TABLE " + TABLE_DETALLES_PEDIDO + " (" +
                    COLUMN_CODIGO_PEDIDO + " INTEGER, " +
                    COLUMN_CODIGO_PRODUCTO_DETALLE + " INTEGER, " +
                    COLUMN_PRECIO_UNITARIO + " REAL, " +
                    COLUMN_TOTAL + " REAL, " +
                    COLUMN_CANTIDAD + " INTEGER, " + // Aquí se agrega el campo CANTIDAD
                    "FOREIGN KEY (" + COLUMN_CODIGO_PEDIDO + ") REFERENCES " +
                    TABLE_ENCABEZADO_PEDIDO + "(" + COLUMN_CODIGO_PEDIDO + "), " +
                    "FOREIGN KEY (" + COLUMN_CODIGO_PRODUCTO_DETALLE + ") REFERENCES " +
                    TABLE_PRODUCTOS + "(" + COLUMN_CODIGO_PRODUCTO + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Aplikazioa abiaraztean taulak ez badaude sortuta, sortu egiten ditu
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_KOMERTZIALAK);
        db.execSQL(CREATE_TABLE_AGENDA);
        db.execSQL(CREATE_TABLE_PARTNER);
        db.execSQL(CREATE_TABLE_ENCABEZADO_PEDIDO);
        db.execSQL(CREATE_TABLE_PRODUCTOS);
        db.execSQL(CREATE_TABLE_DETALLES_PEDIDO);

        // Taula sortzean, komertzial bat sortzen du admin izenekoa
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_KOMERTZIALAK, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count == 0) {
            ContentValues comercialValues = new ContentValues();
            comercialValues.put(COLUMN_NOMBRE, "admin");
            comercialValues.put(COLUMN_EMAIL, "admin@admin.com");
            comercialValues.put(COLUMN_TELEFONO, "000000000");
            comercialValues.put(COLUMN_PASAHITZA, "admin");
            comercialValues.put(COLUMN_EREMUA, "administracion"); // Valor por defecto

            long comercialId = db.insert(TABLE_KOMERTZIALAK, null, comercialValues);

            // Partner bat gehitzen du
            ContentValues partnerValues = new ContentValues();
            partnerValues.put(COLUMN_NOMBRE, "Partner de prueba");
            partnerValues.put(COLUMN_DIRECCION, "Calle Ficticia 123");
            partnerValues.put(COLUMN_TELEFONO, "123456789");
            partnerValues.put(COLUMN_ESTADO, 1);
            partnerValues.put(COLUMN_ID_COMERCIAL, comercialId);

            db.insert(TABLE_PARTNER, null, partnerValues);
        }
    }


    //Datu basea bertsio zaharra badu, hau eguneratzen du
    @SuppressLint("Range")
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            try {

                Cursor cursor = db.rawQuery("PRAGMA table_info(" + TABLE_KOMERTZIALAK + ")", null);
                boolean columnExists = false;
                while (cursor.moveToNext()) {
                    if (cursor.getString(cursor.getColumnIndex("name")).equals(COLUMN_EREMUA)) {
                        columnExists = true;
                        break;
                    }
                }
                cursor.close();

                if (!columnExists) {
                    db.execSQL("ALTER TABLE " + TABLE_KOMERTZIALAK + " ADD COLUMN " + COLUMN_EREMUA + " TEXT");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Erabiltzailearen ID-a lortzen du izenaren bidez
    @SuppressLint("Range")
    public int ErabiltzaileIDlortu(String nombreUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_KOMERTZIALAK +
                " WHERE " + COLUMN_NOMBRE + " = ?", new String[]{nombreUsuario});

        int userId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            cursor.close();
        }
        return userId;
    }

    // Método para insertar 5 relojes en la base de datos
    public void agregarRelojes(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // Insertar 5 relojes con diferentes datos
        values.put(COLUMN_NOMBRE_PRODUCTO, "Reloj Deportivo");
        values.put(COLUMN_PRECIO_PRODUCTO, 49.99);
        values.put(COLUMN_STOCK_PRODUCTO, 10);
        db.insert(TABLE_PRODUCTOS, null, values); // Insertar reloj 1

        values.put(COLUMN_NOMBRE_PRODUCTO, "Reloj Clásico");
        values.put(COLUMN_PRECIO_PRODUCTO, 99.99);
        values.put(COLUMN_STOCK_PRODUCTO, 15);
        db.insert(TABLE_PRODUCTOS, null, values); // Insertar reloj 2

        values.put(COLUMN_NOMBRE_PRODUCTO, "Reloj Inteligente");
        values.put(COLUMN_PRECIO_PRODUCTO, 199.99);
        values.put(COLUMN_STOCK_PRODUCTO, 5);
        db.insert(TABLE_PRODUCTOS, null, values); // Insertar reloj 3

        values.put(COLUMN_NOMBRE_PRODUCTO, "Reloj de Buceo");
        values.put(COLUMN_PRECIO_PRODUCTO, 299.99);
        values.put(COLUMN_STOCK_PRODUCTO, 7);
        db.insert(TABLE_PRODUCTOS, null, values); // Insertar reloj 4

        values.put(COLUMN_NOMBRE_PRODUCTO, "Reloj de Moda");
        values.put(COLUMN_PRECIO_PRODUCTO, 79.99);
        values.put(COLUMN_STOCK_PRODUCTO, 20);
        db.insert(TABLE_PRODUCTOS, null, values); // Insertar reloj 5
    }
    @SuppressLint("Range")
    public List<Producto> obtenerProductos(SQLiteDatabase db) {
        List<Producto> productos = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_PRODUCTOS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Producto producto = new Producto();
                producto.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CODIGO_PRODUCTO)));
                producto.setNombre(cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE_PRODUCTO)));
                producto.setPrecio(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRECIO_PRODUCTO)));
                producto.setStock(cursor.getInt(cursor.getColumnIndex(COLUMN_STOCK_PRODUCTO)));
                productos.add(producto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productos;
    }

    public void guardarPedido(Context context, EskaeraGoiburua eskaeraGoiburua, List<EskaeraXehetasuna> detallesPedido) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            // Verificar si la base de datos se ha abierto correctamente
            if (db == null || !db.isOpen()) {
                Toast.makeText(context, "Error al acceder a la base de datos", Toast.LENGTH_LONG).show();
                return;
            }

            // Insertar en la tabla EskaeraGoiburua (cabecera del pedido)
            ContentValues values = new ContentValues();
            values.put("direccion_envio", eskaeraGoiburua.getDireccionEnvio());
            values.put("fecha", eskaeraGoiburua.getFechaPedido());
            values.put("id_comercial", eskaeraGoiburua.getIdComercial());
            values.put("id_partner", eskaeraGoiburua.getIdPartner());
            values.put("estado", eskaeraGoiburua.getEstadoPedido());

            long idGoiburua = db.insert("Eskaera_Goiburua", null, values);

            // Verificar si la inserción fue exitosa
            if (idGoiburua == -1) {
                Toast.makeText(context, "Error al guardar la cabecera del pedido", Toast.LENGTH_LONG).show();
                return; // Detener el proceso si no se pudo guardar la cabecera
            }

            // Insertar los detalles del pedido en la tabla EskaeraXehetasuna
            for (EskaeraXehetasuna detalle : detallesPedido) {
                ContentValues detalleValues = new ContentValues();
                detalleValues.put("codigo_pedido", detalle.getCodigoPedido());
                detalleValues.put("codigo_producto", detalle.getCodigoProducto());
                detalleValues.put("precio_x_unidad", detalle.getPrecioUnitario());
                detalleValues.put("total", detalle.getTotal());
                detalleValues.put("cantidad", detalle.getCantidad());

                // Relacionar el detalle con la cabecera usando el idGoiburua
                detalleValues.put("codigo_pedido", (int) idGoiburua);

                long idDetalle = db.insert("Eskaera_Xehetasuna", null, detalleValues);

                // Verificar si la inserción del detalle fue exitosa
                if (idDetalle == -1) {
                    Toast.makeText(context, "Error al guardar los detalles del pedido", Toast.LENGTH_LONG).show();
                    return; // Detener el proceso si no se pudo guardar un detalle
                }
            }
            // Llamar al método para guardar el pedido en XML
            guardarPedidoEnXml(context, idGoiburua, eskaeraGoiburua, detallesPedido);

            // Mensaje de éxito

            // Mensaje de éxito
            Toast.makeText(context, "Pedido guardado exitosamente", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            // Capturar cualquier excepción y mostrar el error
            Toast.makeText(context, "Error al guardar el pedido: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();  // Asegurarse de cerrar la base de datos
            }
        }
    }


    private void guardarPedidoEnXml(Context context, long idGoiburua, EskaeraGoiburua eskaeraGoiburua, List<EskaeraXehetasuna> detallesPedido) {
        FileOutputStream fos = null;
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "XML-ak/Bidaltzeko");

        try {
            // Crear el directorio si no existe
            if (!directory.exists()) {
                boolean created = directory.mkdirs();  // Crear directorios si no existen
                if (!created) {
                    Toast.makeText(context, "Error al crear directorios", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            // Crear archivo XML dentro del directorio
            File file = new File(directory, "pedido_" + idGoiburua + ".xml");
            fos = new FileOutputStream(file);

            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");

            // Iniciar el documento XML
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "pedido");

            // Guardar la cabecera del pedido en XML
            serializer.startTag("", "cabecera");
            serializer.startTag("", "codigo_pedido");
            serializer.text(String.valueOf(idGoiburua));  // Usar el ID generado para el pedido
            serializer.endTag("", "codigo_pedido");
            serializer.startTag("", "direccion_envio");
            serializer.text(eskaeraGoiburua.getDireccionEnvio());
            serializer.endTag("", "direccion_envio");
            serializer.startTag("", "fecha");
            serializer.text(eskaeraGoiburua.getFechaPedido());
            serializer.endTag("", "fecha");
            serializer.startTag("", "id_comercial");
            serializer.text(String.valueOf(eskaeraGoiburua.getIdComercial()));
            serializer.endTag("", "id_comercial");
            serializer.startTag("", "id_partner");
            serializer.text(String.valueOf(eskaeraGoiburua.getIdPartner()));
            serializer.endTag("", "id_partner");
            serializer.startTag("", "estado");
            serializer.text(eskaeraGoiburua.getEstadoPedido());
            serializer.endTag("", "estado");
            serializer.endTag("", "cabecera");

            // Guardar los detalles del pedido en XML
            serializer.startTag("", "detalles");
            for (EskaeraXehetasuna detalle : detallesPedido) {
                serializer.startTag("", "detalle");
                serializer.startTag("", "codigo_producto");
                serializer.text(String.valueOf(detalle.getCodigoProducto()));
                serializer.endTag("", "codigo_producto");
                serializer.startTag("", "precio_x_unidad");
                serializer.text(String.valueOf(detalle.getPrecioUnitario()));
                serializer.endTag("", "precio_x_unidad");
                serializer.startTag("", "total");
                serializer.text(String.valueOf(detalle.getTotal()));
                serializer.endTag("", "total");
                serializer.startTag("", "cantidad");
                serializer.text(String.valueOf(detalle.getCantidad()));
                serializer.endTag("", "cantidad");
                serializer.endTag("", "detalle");
            }
            serializer.endTag("", "detalles");

            // Cerrar el XML
            serializer.endTag("", "pedido");
            serializer.endDocument();

            Toast.makeText(context, "Archivo XML guardado en: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            // Capturar cualquier excepción y mostrar el error
            Toast.makeText(context, "Error al guardar el pedido en XML: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (fos != null) {
                try {
                    fos.close();  // Asegurarse de cerrar el archivo
                } catch (IOException e) {
                    Toast.makeText(context, "Error al cerrar el archivo XML: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public int obtenerMaxIdEskaeraGoiburua() {
        SQLiteDatabase db = null;
        int maxId = -1;  // Inicializamos en -1 por defecto en caso de no encontrar un ID

        try {
            db = this.getReadableDatabase();

            // Consulta SQL para obtener el ID máximo
            String query = "SELECT MAX(codigo_pedido) FROM Eskaera_Goiburua";
            Cursor cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                maxId = cursor.getInt(0);  // El primer valor en la columna es el ID máximo
            }

            if (cursor != null) {
                cursor.close();  // Cerramos el cursor
            }

        } catch (Exception e) {
            // Capturar cualquier excepción y mostrar el error
            Log.e("ErrorDB", "Error al obtener el ID máximo: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();  // Asegurarse de cerrar la base de datos
            }
        }

        return maxId;
    }
    public List<EskaeraXehetasuna> obtenerDetallesPedidoSeguro(SQLiteDatabase db, int codigoPedido) {
        List<EskaeraXehetasuna> detalles = new ArrayList<>();

        if (db == null) {
            Log.e("EditarPedidoActivity", "Base de datos no disponible.");
            return detalles;  // Si la base de datos no está disponible, retorna una lista vacía.
        }

        String query = "SELECT * FROM Eskaera_Xehetasuna WHERE codigo_pedido = ?";
        Cursor cursor = null;

        try {
            // Ejecutar la consulta y obtener el cursor
            cursor = db.rawQuery(query, new String[]{String.valueOf(codigoPedido)});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Verificar si las columnas existen y están disponibles
                    int columnaCodigoProducto = cursor.getColumnIndex("codigo_producto");
                    int columnaPrecioUnidad = cursor.getColumnIndex("precio_x_unidad");
                    int columnaTotal = cursor.getColumnIndex("total");
                    int columnaCantidad = cursor.getColumnIndex("cantidad");

                    // Verificar si todas las columnas son válidas
                    if (columnaCodigoProducto != -1 && columnaPrecioUnidad != -1 && columnaTotal != -1 && columnaCantidad != -1) {
                        int codigoProducto = cursor.getInt(columnaCodigoProducto);
                        double precioUnitario = cursor.getDouble(columnaPrecioUnidad);
                        double total = cursor.getDouble(columnaTotal);
                        int cantidad = cursor.getInt(columnaCantidad);

                        // Crear el objeto de detalle
                        EskaeraXehetasuna detalle = new EskaeraXehetasuna(codigoPedido, codigoProducto, precioUnitario, total, cantidad);
                        detalles.add(detalle);
                    } else {
                        Log.e("EditarPedidoActivity", "Faltan columnas en la consulta para el pedido: " + codigoPedido);
                    }
                } while (cursor.moveToNext());
            } else {
                Log.e("EditarPedidoActivity", "No se encontraron detalles para el pedido con código: " + codigoPedido);
            }
        } catch (Exception e) {
            Log.e("EditarPedidoActivity", "Error al obtener los detalles del pedido: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();  // Asegúrate de cerrar el cursor
            }
        }

        return detalles;  // Retornar la lista de detalles (vacía si no se encontraron registros)
    }

    // Bisita bat gordetzen du
    public long BisitaGorde(String titulo, String detalles, String fecha, int idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, titulo);
        values.put(COLUMN_DETALLES, detalles);
        values.put(COLUMN_FECHA, fecha);
        values.put(COLUMN_KOMERTZIAL_ID, idUsuario);

        return db.insert(TABLE_AGENDA, null, values);
    }

    // Bisitak lortzen ditu erabiltzaile eta data arabera
    public List<Visita> BisitakErabiltzaileDataArabera(int usuarioId, String fecha) {
        List<Visita> visitas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_AGENDA, null,
                COLUMN_KOMERTZIAL_ID + " = ? AND " + COLUMN_FECHA + " = ?",
                new String[]{String.valueOf(usuarioId), fecha},
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_AGENDA));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITULO));
                @SuppressLint("Range") String details = cursor.getString(cursor.getColumnIndex(COLUMN_DETALLES));
                visitas.add(new Visita(id, title, details, usuarioId));
            }
            cursor.close();
        }

        return visitas;
    }

    // Login-a egiteko kontsulta
    public Cursor LoginKontsulta(String nombre, String pasahitza) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_KOMERTZIALAK + " WHERE " +
                COLUMN_NOMBRE + " = ? AND " + COLUMN_PASAHITZA + " = ?", new String[]{nombre, pasahitza});
    }

    // Bisita bat ezabatzeko kontsulta
    public void BisitaEzabatu(int visitaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_AGENDA, COLUMN_ID_AGENDA + " = ?", new String[]{String.valueOf(visitaId)});
    }
    // Komertzial berri bat gordetzen du
    public long KomertzialaGehitu(String nombre, String email, String telefono, String pasahitza, String eremua) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_TELEFONO, telefono);
        values.put(COLUMN_PASAHITZA, pasahitza);
        values.put(COLUMN_EREMUA, eremua);

        return db.insert(TABLE_KOMERTZIALAK, null, values);
    }

    // Komertzial baten datuak eguneratzen ditu
    public int KomertzialaEguneratu(int id, String email, String telefono, String pasahitza, String eremua) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_TELEFONO, telefono);
        values.put(COLUMN_PASAHITZA, pasahitza);
        values.put(COLUMN_EREMUA, eremua);

        return db.update(TABLE_KOMERTZIALAK, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    //Hilabete osoko bisitak lortzen ditu logeatuta dagoen erabiltzailearen arabera
    public List<Visita> HilabetekoBilerak(String month, int idUsuario) {
        List<Visita> meetings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT fecha, titulo FROM agenda WHERE fecha LIKE ? AND komertzial_id = ?",
                new String[]{month + "%", String.valueOf(idUsuario)}
        );

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                String title = cursor.getString(1);
                meetings.add(new Visita(date, title));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return meetings;
    }


    //Partner guztien zerrenda bat itzultzen du
    public List<Partner> PartnerrakLortu() {
        List<Partner> partnerList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT p.Partner_id, p.Nombre, p.Direccion, p.Telefono, p.Estado, k.Nombre AS NombreComercial " +
                "FROM " + TABLE_PARTNER + " p " +
                "LEFT JOIN " + TABLE_KOMERTZIALAK + " k " +
                "ON p.Id_Comercial = k.id";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int partnerId = cursor.getInt(cursor.getColumnIndex(COLUMN_PARTNER_ID));
                    @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE_PART));
                    @SuppressLint("Range") String direccion = cursor.getString(cursor.getColumnIndex(COLUMN_DIRECCION));
                    @SuppressLint("Range") String telefono = cursor.getString(cursor.getColumnIndex(COLUMN_TELEFONO_PART));
                    @SuppressLint("Range") int estado = cursor.getInt(cursor.getColumnIndex(COLUMN_ESTADO));
                    @SuppressLint("Range") String nombreComercial = cursor.getString(cursor.getColumnIndex("NombreComercial")); // Obtener nombre del comercial

                    Partner partner = new Partner(partnerId, nombre, direccion, telefono, estado, nombreComercial);
                    partnerList.add(partner);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return partnerList;
    }


    //Partner berria gehitzen du
    public void PartnerraGehitu(String nombre, String direccion, String telefono, int estado, int idComercial) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Nombre", nombre);
        values.put("Direccion", direccion);
        values.put("Telefono", telefono);
        values.put("Estado", estado);
        values.put("Id_Comercial", idComercial); // Se usa el ID del usuario logueado

        db.insert(TABLE_PARTNER, null, values);
        db.close();
    }

    //Partner bat ezabatzen du
    public void PartnerEzabatu(int partnerId, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(TABLE_PARTNER, "Partner_id = ?", new String[]{String.valueOf(partnerId)});
        db.close();

        if (rowsDeleted > 0) {
            Toast.makeText(context, "Partner-a ezabatu da", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Errorea: Ez da aurkitu partner-ik ID horrekin", Toast.LENGTH_SHORT).show();
        }

        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

    //Jasotako datuekin,iada dauden partnerren datuak eguneratzen ditu(ID berdina badu) edo berri bat gehitzen du
    public void PartnerEguneratuSortu(int partnerId, String nombre, String direccion, String telefono, int estado, int idComercial) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Partner ID autoincrement, beraz ez da gehitzen
        values.put(COLUMN_NOMBRE_PART, nombre);
        values.put(COLUMN_DIRECCION, direccion);
        values.put(COLUMN_TELEFONO_PART, telefono);
        values.put(COLUMN_ESTADO, estado);
        values.put(COLUMN_ID_COMERCIAL, idComercial);

        int rowsUpdated = db.update(TABLE_PARTNER, values, COLUMN_PARTNER_ID + " = ?", new String[]{String.valueOf(partnerId)});

        if (rowsUpdated == 0) {
            db.insert(TABLE_PARTNER, null, values);
        }

        db.close();
    }

    //Partnerraren izena lortzen da bere ID-aren bitartez
    public Partner PartnerLortuIDbidez(int partnerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PARTNER, null, COLUMN_PARTNER_ID + " = ?", new String[]{String.valueOf(partnerId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") Partner partner = new Partner(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PARTNER_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE_PART)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DIRECCION)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TELEFONO_PART)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ESTADO)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID_COMERCIAL))
            );
            cursor.close();
            return partner;
        } else {
            return null;
        }
    }

    //Partner-aren datuak eguneratzen ditu
    public void updatePartner(int partnerId, String nombre, String direccion, String telefono, int estado, int idComercial) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE_PART, nombre);
        values.put(COLUMN_DIRECCION, direccion);
        values.put(COLUMN_TELEFONO_PART, telefono);
        values.put(COLUMN_ESTADO, estado);
        values.put(COLUMN_ID_COMERCIAL, idComercial);

        db.update(TABLE_PARTNER, values, COLUMN_PARTNER_ID + " = ?", new String[]{String.valueOf(partnerId)});
        db.close();
    }




























}
