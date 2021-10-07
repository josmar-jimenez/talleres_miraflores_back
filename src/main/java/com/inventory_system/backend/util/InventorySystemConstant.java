package com.inventory_system.backend.util;

public class InventorySystemConstant {

    public static final String RECORD_NOT_FOUND = "Registro no encontrado";
    public static final int RECORD_NOT_FOUND_CODE = 1;

    public static final String RECORD_EXIST = "Registro duplicado, chequear los siguientes campos: ";
    public static final int RECORD_EXIST_CODE = 2;

    public static final String INSUFFICIENT_PRIVILEGES = "Insuficiente privilegios para realizar esta acción";
    public static final int INSUFFICIENT_PRIVILEGES_CODE = 3;

    public static final String OPERATION_NOT_ALLOWED = "Operación no permitida";
    public static final int OPERATION_NOT_ALLOWED_CODE = 5;

    public static final String INVALID_SALE_REQUEST_NO_PRODUCT = "Debe indicar al menos un producto para realizar una venta";
    public static final int INVALID_SALE_REQUEST_NO_PRODUCT_CODE = 6;

    public static final String INVALID_SALE_REQUEST_INSUFFICIENT_STOCK = "Insuficiente stock para realizar esta operación";
    public static final int INVALID_SALE_REQUEST_INSUFFICIENT_STOCK_CODE = 7;
}
